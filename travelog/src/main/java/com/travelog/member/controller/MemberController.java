package com.travelog.member.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.travelog.domain.Member;
import com.travelog.member.service.MemberService;
import com.travelog.member.vo.MemberDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/member")
public class MemberController {
	
	private final MemberService memberService;
	private final BCryptPasswordEncoder passwordEncoder;
	
	@PostMapping(value="/join", produces="application/json;charset=UTF-8")
	public Long join(@RequestBody MemberDTO requestData) {
		log.info("회원가입 시작");
		
		
		Member newMember = Member.createMember(requestData, passwordEncoder);
	  
		Long id = memberService.join(newMember); 
		
		return id;

	}
}
