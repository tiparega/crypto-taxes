package com.tiparega.cryptotax.dto;

import lombok.Getter;

public enum Subtype {
	BUY("Buy"), SELL("Sell");
	
	@Getter
	private String value;
	
	private Subtype(String value) {
		this.value= value;
	}
	
	public static Subtype fromValue(String value) {
		for (Subtype t: Subtype.values()) {
			if (t.getValue().equals(value)) {
				return t;
			}
		}
		return null;
	}
}
