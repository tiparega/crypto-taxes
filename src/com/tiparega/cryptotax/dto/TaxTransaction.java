package com.tiparega.cryptotax.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaxTransaction {
	private BigDecimal amount;
	private BigDecimal priceSold;
	private BigDecimal sellFees;
	private BigDecimal priceBought;
	private BigDecimal buyFees;
	public BigDecimal getBenefit() {
		return amount.multiply(priceSold).subtract(amount.multiply(priceBought)).subtract(sellFees).subtract(buyFees);
	};
	public BigDecimal getValueSold() {
		return amount.multiply(priceSold);
	}
	public BigDecimal getValueBought() {
		return amount.multiply(priceBought);
	}
}
