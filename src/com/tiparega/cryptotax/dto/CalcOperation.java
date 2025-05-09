package com.tiparega.cryptotax.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CalcOperation extends Operation {
	private BigDecimal benefit;
	private List<TaxTransaction> taxTransactions; 

	public CalcOperation(Operation instance) {
		super(instance.toBuilder());
	}
}
