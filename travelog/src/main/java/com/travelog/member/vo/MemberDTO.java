package com.travelog.member.vo;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.travelog.domain.EntityInfo;
import com.travelog.domain.Gender;
import com.travelog.domain.MemberRole;
import com.travelog.domain.MemberStatus;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberDTO {
	
	private long id;
	@NotBlank(message = "아이디를 입력해주세요.")
	private String loginId;
	@NotBlank(message = "비밀번호를 입력해주세요.")
	private String loginPw;
	@NotBlank(message = "이름을 입력해주세요.")
    private String name;
    private String nick;
    @NotBlank(message = "휴대폰번호를 입력해주세요.")
    @Pattern(regexp="^\\d{3}-\\d{3,4}-\\d{4}$",
            message = "휴대폰번호 형식에 맞게 입력해주세요.")
    private String mobile;
    private String email;
    @NotBlank(message = "생년월일을 입력해주세요.")
    private String birth;
    private Gender gender; //FEMALE, MALE
    private MemberStatus memberStatus;
    private int agree;
    private LocalDateTime lastLogin;
    private EntityInfo entityInfo; //등록일, 등록자, 수정일, 수정자
    private MemberRole role; 
	
}
