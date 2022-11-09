package com.travelog.login.vo;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter 
@ToString
public class LoginDTO {
	@NotBlank(message = "아이디를 입력해주세요.")
	private String loginId;
	@NotBlank(message = "비밀번호를 입력해주세요.")
	private String loginPw;
}
