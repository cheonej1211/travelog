package com.travelog.member.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

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
@RequestMapping("/member")
public class MemberController {
	
	private final JwtTokenProvider jwtTokenProvider;
	private final MemberService memberService;
	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	private final String TITLE = "J O I N";
	private final String EORROR = "회원가입 실패, 다시 시도해주세요.";
	
	
	@GetMapping(value="/join")
	public String join(Model model, MemberDTO memberDTO) {
		log.info("회원가입 페이지");
		model.addAttribute("title", TITLE);
		return "travelog/member/join";
	}
	
	@PostMapping(value="/join")
	public String joinAction(Model model, @Validated MemberDTO memberDTO, 
			Errors errors, BindingResult bindingResult, HttpServletResponse response) {
		log.info("회원가입 시작");
		Member member = memberRepository.findByLoginId(memberDTO.getLoginId()).orElse(null);
		if (member != null) {
			bindingResult.rejectValue("loginId", "key","사용중인 아이디입니다.");
		}
		if (bindingResult.hasErrors() || errors.hasErrors()){
            model.addAttribute("title", TITLE);
            return "travelog/member/join";
        }else{
        	member = Member.createMember(memberDTO, passwordEncoder);
			try {
				memberService.join(member);
				String token = jwtTokenProvider.createToken(member.getLoginId(), member.getRole());
	        	Cookie cookie = new Cookie("token",token);
	        	cookie.setPath("/");
	            cookie.setMaxAge(Integer.MAX_VALUE);
	            response.addCookie(cookie);
			} catch (Exception e) {
				// TODO: handle exception
				log.debug(e.toString());
				model.addAttribute("title", TITLE);
				model.addAttribute("error", EORROR);
				
	            return "travelog/member/join";
			}
        }
		
		return "redirect:/sec";

	}
}
