package com.travelog.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Gender {
	FEMALE("여성", Arrays.asList("female","F")), 
	MALE("남성", Arrays.asList("male","M")),
	EMPTY("알수없음", Collections.EMPTY_LIST);
	
	private String gender;
	private List<String> asList;

	Gender(String gender, List<String> asList) {
		// TODO Auto-generated constructor stub
		this.gender = gender;
		this.asList = asList;
	}
	
	public static Gender findByGenderCode(String code) {
		return Arrays.stream(Gender.values())
				.filter(gender -> gender.hashCode(code))
				.findAny()
				.orElse(EMPTY);
	}

	public boolean hashCode(String code) {
		// TODO Auto-generated method stub
		return asList.stream().anyMatch(genderCode -> genderCode.equals(code));
	}
	
	public String getGender() {
		return gender;
	}
	
}