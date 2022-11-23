package com.travelog.domain;

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
        return Member.builder()
                     .name(name)
                     .nick(nick)
                     .email(email)
                     .loginId(email)
                     .provider(provider)
                     .birth(birth)
                     .gender(Gender.findByGenderCode(gender))
                     .entityInfo(new EntityInfo())
                     .build();
    }
}
