package com.tiparega.cryptotax.dto;

import lombok.Getter;

public enum Type {
	MARKET("Market"), DEPOSIT("Deposit"), INT_TRANSFER("Inter Account Transfer");
	
	@Getter
	private String value;
	
	private Type(String value) {
		this.value= value;
	}
	
	public static Type fromValue(String value) {
		for (Type t: Type.values()) {
			if (t.getValue().equals(value)) {
				return t;
			}
		}
		return null;
	}
}
