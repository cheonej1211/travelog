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
    private String birthday;

    public Member toMember() {
        return Member.builder()
                     .name(name)
                     .nick(nick)
                     .email(email)
                     .loginId(email)
                     .provider(provider)
                     .build();
    }
}
