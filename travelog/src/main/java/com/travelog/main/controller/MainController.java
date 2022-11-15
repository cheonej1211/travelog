package com.travelog.main.controller;

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
public class MainController {

	private final JwtTokenProvider jwtTokenProvider;
	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	private final String TITLE = "L O G I N";

	@GetMapping("/main")
	public String sec(Principal principal, Model model) {
		log.info("성공");
		model.addAttribute("title", TITLE);
		//Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		//Member member = (Member) authentication.getPrincipal();
		model.addAttribute("username", principal.getName());
		return "travelog/main";
	}
	
	

}
