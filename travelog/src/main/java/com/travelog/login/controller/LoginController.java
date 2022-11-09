package com.travelog.login.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.travelog.config.security.JwtTokenProvider;
import com.travelog.domain.Member;
import com.travelog.login.vo.LoginDTO;
import com.travelog.member.repository.MemberRepository;
import com.travelog.member.service.MemberService;
import com.travelog.member.vo.MemberDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Controller
@Slf4j
public class LoginController {

	private final JwtTokenProvider jwtTokenProvider;
	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	private final String TITLE = "로그인";

	@GetMapping("/sec")
	public String sec() {
		
		log.info("성공");
		return "시큐리티 테스트";
	}
	
	@GetMapping("/login")
	public String login(Model model, LoginDTO loginDTO) {
		
		log.info("로그인 페이지");
		model.addAttribute("title", TITLE);
		return "travelog/login";
	}

	@PostMapping(value="/login")
	public String loginAction(Model model, @Validated LoginDTO loginDTO, 
			Errors errors, BindingResult bindingResult, HttpServletResponse response) throws IllegalArgumentException{

		Member member = memberRepository.findByLoginId(loginDTO.getLoginId()).orElse(null);
    	if (member == null) {
    		errors.rejectValue("loginId", "아이디를 찾을 수 없습니다.");				
		}else {
			if (!passwordEncoder.matches(loginDTO.getLoginPw(), member.getLoginPw())) {
	        	errors.rejectValue("loginPw", "비밀번호가 틀렸습니다.");
	        }
		}
		
        if (errors.hasErrors()){
            model.addAttribute("title", TITLE);
            return "travelog/login";
        }else{
        	String token = null;
        	token = jwtTokenProvider.createToken(member.getLoginId(), member.getRole());
        	Cookie cookie = new Cookie("token",token);
        	cookie.setPath("/");
            cookie.setMaxAge(Integer.MAX_VALUE);
            response.addCookie(cookie);
            
            return "redirect:/travelog";
        }
        
        
		
	}

	

}
