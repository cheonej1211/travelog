package com.travelog.config.security;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import com.travelog.domain.Member;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor{

    private final JwtTokenProvider jwtTokenProvider;
    public static final String COOKIE_NAME = "token";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws ModelAndViewDefiningException {

        String token = Arrays.stream(request.getCookies())
                            .filter(cookie -> cookie.getName().equals(JwtInterceptor.COOKIE_NAME))
                            .findFirst().map(Cookie::getValue)
                            .orElse("dummy");

        log.info("token : {}", token);

        try {
            Map<String, Object> info = new HashMap<>(); //jwtTokenProvider.getAuthentication(token);
            info.put("id", "1");
            info.put("name", "psy");

            // View 에서 session.id 처럼 로그인 정보 쉽게 가져다 쓸수 있도록 request 에 verify 한 사용자 정보 설정
            Member member = Member.builder()
                    .loginId((String) info.get("id"))
                    .name((String) info.get("name"))
                    .build();

            // view 에서 login.id 로 접근가능
            request.setAttribute("login", member);
            
        } catch (ExpiredJwtException ex) {
            log.error("토큰이 만료됨");

            ModelAndView mav = new ModelAndView("login");
            mav.addObject("return_url", request.getRequestURI());

            throw new ModelAndViewDefiningException(mav);
        } catch (JwtException ex) {
            log.error("비정상 토큰");

            ModelAndView mav = new ModelAndView("login");

            throw new ModelAndViewDefiningException(mav);
        }

        return true;
    }
}
