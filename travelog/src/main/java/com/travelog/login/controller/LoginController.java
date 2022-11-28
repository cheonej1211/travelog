package com.travelog.login.controller;

import java.security.Principal;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.travelog.config.security.JwtTokenProvider;
import com.travelog.domain.Member;
import com.travelog.login.vo.LoginDTO;
import com.travelog.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Controller
@Slf4j
public class LoginController {

	private final JwtTokenProvider jwtTokenProvider;
	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	private final String TITLE = "L O G I N";

	@GetMapping("/sec")
	public String sec(Principal principal, Model model) {
		log.info("성공");
		model.addAttribute("title", TITLE);
		//Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		//Member member = (Member) authentication.getPrincipal();
		model.addAttribute("username", principal.getName());
		return "travelog/security_test";
	}
	
	@GetMapping("/login")
	public String login(Model model, LoginDTO loginDTO) {
		
		log.info("로그인 페이지");
		model.addAttribute("title", TITLE);
		return "travelog/login";
	}

	@PostMapping(value="/login")
	public String loginAction(Model model, @Validated LoginDTO loginDTO, 
			Errors errors, BindingResult bindingResult, HttpServletResponse response){
		log.info("로그인 시작");
		// 소셜 회원 아닌 회원 중 loginId 조회
		Member member = memberRepository.findByLoginIdAndProvider(loginDTO.getLoginId(),null).orElse(null);
		
		// 소셜 회원일 경우 로그인 아이디 없음 처리
		if (member.getProvider() != null) {
			member = null;
		}
		
    	if (member == null) {
    		bindingResult.rejectValue("loginId", "key","아이디가 존재하지 않습니다.");		
		}else {
			if (!passwordEncoder.matches(loginDTO.getLoginPw(), member.getLoginPw())) {
				bindingResult.rejectValue("loginPw", "key","비밀번호가 일치하지 않습니다.");
	        }
		}
		
        if (bindingResult.hasErrors() || errors.hasErrors()){
            model.addAttribute("title", TITLE);
            return "travelog/login";
        }else{
        	String token = jwtTokenProvider.createToken(member.getLoginId(), member.getRole());
        	Cookie cookie = new Cookie("token",token);
        	cookie.setPath("/");
            cookie.setMaxAge(Integer.MAX_VALUE);
            response.addCookie(cookie);
            
            return "redirect:/sec";
        }

	}

}
