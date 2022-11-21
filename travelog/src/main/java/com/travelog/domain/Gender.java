package com.travelog.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Gender {
	FEMALE(Arrays.asList("female","F")), MALE(Arrays.asList("male","M")),
	EMPTY(Collections.EMPTY_LIST);
	
	private String gender;
	private List<String> asList;

	Gender(List<String> asList) {
		// TODO Auto-generated constructor stub
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
	
	
}