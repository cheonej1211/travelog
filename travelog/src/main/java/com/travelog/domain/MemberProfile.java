package com.travelog.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberProfile {
	private String name;
    private String email;
    private String provider;
    private String nick;
    private String gender;
    private String birth;

    public Member toMember() {
    	
    	EntityInfo entityInfo = new EntityInfo();
    	LocalDateTime localDateTime = LocalDateTime.now();
    	entityInfo.setRegisterId(email);
    	entityInfo.setUpdateId(email);
    	
        return Member.builder()
                     .name(name)
                     .nick(nick)
                     .email(email)
                     .loginId(email)
                     .provider(provider)
                     .birth(birth)
                     .gender(Gender.findByGenderCode(gender))
                     .entityInfo(new EntityInfo())
                     .lastLogin(localDateTime)
                     .build();
    }
}
