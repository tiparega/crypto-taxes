package com.tiparega.cryptotax.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Transaction {
	private BigDecimal amount;
	private BigDecimal price;
	private BigDecimal fee;
}
