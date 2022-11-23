package com.travelog.config.oAuth;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

import com.travelog.domain.MemberProfile;

public enum OAuthAttributes {
	GOOGLE("google", (attributes) -> {
        MemberProfile memberProfile = new MemberProfile();
        memberProfile.setName((String) attributes.get("name"));
        memberProfile.setEmail((String) attributes.get("email"));
        return memberProfile;
    }),

    NAVER("naver", (attributes) -> {
        Map<String, Object> response = (Map<String, Object>)attributes.get("response");
        System.out.println(response);
        MemberProfile memberProfile = new MemberProfile();
        memberProfile.setEmail((String) response.get("email"));
        memberProfile.setName((String) response.get("name"));
        memberProfile.setNick((String) response.get("nickname"));
        memberProfile.setGender((String) response.get("gender"));
        memberProfile.setBirth((String) response.get("birthyear")+"-"+(String) response.get("birthday"));
        return memberProfile;
    }),

    KAKAO("kakao", (attributes) -> {
        // kakao는 kakao_account에 유저정보가 있다. (email)
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
        // kakao_account안에 또 profile이라는 JSON객체가 있다. (nickname)
        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");
        System.out.println(kakaoAccount);
        System.out.println(kakaoAccount);
        MemberProfile memberProfile = new MemberProfile();
        memberProfile.setEmail((String) kakaoAccount.get("email"));
        memberProfile.setName((String) kakaoProfile.get("nickname"));
        memberProfile.setNick((String) kakaoProfile.get("nickname"));
        memberProfile.setGender((String) kakaoAccount.get("gender"));
        memberProfile.setBirth((String) kakaoAccount.get("birthyear")+"-"+(String) kakaoAccount.get("birthday"));
        return memberProfile;
    });
	
	private final String registrationId;
    private final Function<Map<String, Object>, MemberProfile> of;

    OAuthAttributes(String registrationId, Function<Map<String, Object>, MemberProfile> of) {
        this.registrationId = registrationId;
        this.of = of;
    }
    
    public static MemberProfile extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(provider -> registrationId.equals(provider.registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of.apply(attributes);
    }
}
