package com.tiparega.cryptotax.dto;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Operation {
	private long id;
	private String account;
	private Type type;
	private Subtype subtype;
	private Instant datetime;
	private BigDecimal amount;
	private String amountCur;
	private BigDecimal value;
	private String valueCur;
	private BigDecimal rate;
	private String rateCur;
	private BigDecimal fee;
	private String feeCur;
	private String orderId;
}