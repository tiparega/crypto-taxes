package com.tiparega.cryptotax.svc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.tiparega.cryptotax.dto.CalcOperation;
import com.tiparega.cryptotax.dto.Operation;
import com.tiparega.cryptotax.dto.TaxTransaction;
import com.tiparega.cryptotax.dto.Transaction;
import com.tiparega.cryptotax.dto.Type;

public class BenefitsCalc {
	public static List<CalcOperation> calculateBenefits(List<Operation> ops) {
		Map<String,BigDecimal> balances= new HashMap<>();
		Map<String,List<Transaction>> transactions= new HashMap<>();
		List<CalcOperation> calculated= new ArrayList<>();
		
		for (Operation op: ops) {
			if (op.getType().equals(Type.MARKET))  {
				switch (op.getSubtype()) {
					case BUY:
						add(op,balances, transactions);
						CalcOperation calcOp= new CalcOperation(op);
						if (op.getFee() != null) {
							calcOp.setBenefit(op.getFee().negate());
						}
						calculated.add(calcOp);
						break;
					case SELL:
						calculated.add(sub(op,balances,transactions));
						break;
					default:
						System.err.println("Unknown subtype: " + op.getId());
						System.exit(4);
				}
			} else {
				calculated.add(new CalcOperation(op));
			}
		}
		
		return calculated;
	}

	private static void add(Operation op, Map<String, BigDecimal> balances, Map<String,List<Transaction>> transactions) {
		BigDecimal balance= balances.get(op.getAmountCur());
		if (balance == null) {
			balances.put(op.getAmountCur(), op.getAmount());
		} else {
			balances.put(op.getAmountCur(), balance.add(op.getAmount()));
		}
		
		List<Transaction> curTransactions= transactions.get(op.getAmountCur());
		if (curTransactions == null) {
			curTransactions= new ArrayList<>();
			transactions.put(op.getAmountCur(), curTransactions);
		}
		curTransactions.add(new Transaction(op.getAmount(),op.getRate(), op.getFee() != null? op.getFee():BigDecimal.ZERO));
	}

	private static CalcOperation sub(Operation op, Map<String, BigDecimal> balances, Map<String, List<Transaction>> transactions) {
		BigDecimal balance= balances.get(op.getAmountCur());
		if (balance == null) {
			System.err.println("Tried to sell without balance: " + op.getId());
			System.exit(1);
		} else {
			BigDecimal newBalance= balance.subtract(op.getAmount());
			if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
				System.err.println("Tried to sell with not enough balance: " + op.getId() + ". Results in " + newBalance);
				System.exit(2);
			}
			balances.put(op.getAmountCur(), newBalance);
		}
		
		BigDecimal amountRemaining= op.getAmount();
		Iterator<Transaction> it= transactions.get(op.getAmountCur()).iterator();
		CalcOperation calcOp= new CalcOperation(op);
		calcOp.setBenefit(op.getValue().subtract(op.getFee()));
		calcOp.setTaxTransactions(new ArrayList<>());
		while (amountRemaining.compareTo(BigDecimal.ZERO) > 0) {
			Transaction next= it.next();
			if (next == null) {
				System.err.println("Not enough transactions to sell: " + op.getId());
				System.exit(3);
			}
			BigDecimal sellFeePart= BigDecimal.ZERO;
			if (next.getAmount().compareTo(amountRemaining) > 0) {
				BigDecimal buyFeePart= BigDecimal.ZERO;
				if (!next.getFee().equals(BigDecimal.ZERO)) {
					buyFeePart=next.getFee().multiply(amountRemaining.divide(next.getAmount(),RoundingMode.HALF_UP));
					next.setFee(next.getFee().subtract(buyFeePart));
				}
				if (!op.getFee().equals(BigDecimal.ZERO)) {
					sellFeePart=op.getFee().multiply(amountRemaining.divide(op.getAmount(),RoundingMode.HALF_UP));
				}
				next.setAmount(next.getAmount().subtract(amountRemaining));
				calcOp.setBenefit(calcOp.getBenefit().subtract(amountRemaining.multiply(next.getPrice())));
				calcOp.getTaxTransactions().add(new TaxTransaction(amountRemaining, op.getRate(), sellFeePart, next.getPrice(), buyFeePart));
				amountRemaining= BigDecimal.ZERO;
			} else {
				if (!op.getFee().equals(BigDecimal.ZERO)) {
					sellFeePart=op.getFee().multiply(amountRemaining.divide(next.getAmount(),RoundingMode.HALF_UP));
				}
				if (!op.getFee().equals(BigDecimal.ZERO)) {
					sellFeePart=op.getFee().multiply(next.getAmount().divide(op.getAmount(),RoundingMode.HALF_UP));
				}
				calcOp.setBenefit(calcOp.getBenefit().subtract(next.getAmount().multiply(next.getPrice())));
				amountRemaining= amountRemaining.subtract(next.getAmount());
				calcOp.getTaxTransactions().add(new TaxTransaction(next.getAmount(), op.getRate(), sellFeePart, next.getPrice(), next.getFee()));
				it.remove();
			}
		}
		
		return calcOp;
	}
}
