package com.tiparega.cryptotax.svc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.tiparega.cryptotax.dto.CalcOperation;
import com.tiparega.cryptotax.dto.Operation;
import com.tiparega.cryptotax.dto.Subtype;
import com.tiparega.cryptotax.dto.TaxTransaction;
import com.tiparega.cryptotax.dto.Type;

public class Csv {
	public static List<Operation> readCsv(String path) throws FileNotFoundException, IOException {
		try (BufferedReader br= new BufferedReader(new FileReader(path))) {
			br.readLine();
			
			List<Operation> ops= new ArrayList<>();
			String line= br.readLine();
			while (line != null) {
				String[] opRecord= line.split(",");
				ops.add(new Operation(Long.parseLong(opRecord[0]),
						opRecord[1],
						Type.fromValue(opRecord[2]),
						Subtype.fromValue(opRecord[3]),
						Instant.parse(opRecord[4]),
						new BigDecimal(opRecord[5]),
						opRecord[6],
						opRecord.length > 7 && opRecord[7].length()>0 ? new BigDecimal(opRecord[7]):null,
						opRecord.length > 8 ? opRecord[8] : null,
						opRecord.length > 9 && opRecord[9].length()>0 ? new BigDecimal(opRecord[9]) : null,
						opRecord.length > 10 ? opRecord[10] : null,
						opRecord.length > 11 && opRecord[11].length()>0 ? new BigDecimal(opRecord[11]) : null,
						opRecord.length > 12 ? opRecord[12] : null,
						opRecord.length > 13 ? opRecord[13] : null));

				line= br.readLine();
			}
			
			return ops;
		}
	}
	public static void writeCsv(String path, List<CalcOperation> ops) throws FileNotFoundException, IOException {
		try (FileWriter fw= new FileWriter(path)) {
			fw.write("ID,Account,Type,Subtype,Datetime,Amount,Amount currency,Value,Value currency,Rate,Rate currency,Fee,Fee currency,Order ID,Profit,Taxes\n");
			
			for (CalcOperation op: ops) {
				fw.write(Long.toString(op.getId()));
				fw.write(',');
				fw.write(op.getAccount());
				fw.write(',');
				fw.write(op.getType().getValue());
				fw.write(',');
				if (op.getSubtype() != null) {
					fw.write(op.getSubtype().getValue());
				}
				fw.write(',');
				fw.write(DateTimeFormatter.ISO_INSTANT.format(op.getDatetime()));
				fw.write(',');
				fw.write(op.getAmount().toPlainString());
				fw.write(',');
				fw.write(op.getAmountCur());
				fw.write(',');
				if (op.getValue() != null) {
					fw.write(op.getValue().toPlainString());
				}
				fw.write(',');
				if (op.getValueCur() != null) {
					fw.write(op.getValueCur());
				}
				fw.write(',');
				if (op.getRate() != null) {
					fw.write(op.getRate().toPlainString());
				}
				fw.write(',');
				if (op.getRateCur() != null) {
					fw.write(op.getRateCur());
				}
				fw.write(',');
				if (op.getFee() != null) {
					fw.write(op.getFee().toPlainString());
				}
				fw.write(',');
				if (op.getFeeCur() != null) {
					fw.write(op.getFeeCur());
				}
				fw.write(',');
				if (op.getOrderId() != null) {
					fw.write(op.getOrderId());
				}
				fw.write(',');
				if (op.getBenefit() != null) {
					fw.write(op.getBenefit().toPlainString());
				}
				fw.write(',');
				if (op.getTaxTransactions() != null) {
					for (TaxTransaction tt: op.getTaxTransactions()) {
						fw.write(tt.getAmount().toPlainString() + ":" + tt.getPriceSold().toPlainString() + ":" + tt.getSellFees().toPlainString() + ":" + tt.getPriceBought().toPlainString() + ":" + tt.getBuyFees().toPlainString() + "|");
					}
				}
				fw.write("\n");
			}
		}
	}
	
	public static void writeTaxesCsv(String path, List<CalcOperation> ops) throws FileNotFoundException, IOException {
		try (FileWriter fw= new FileWriter(path)) {
			fw.write("Datetime,currency,Operation amount,Sell price,Sell value,Sell fees,Buy price,Buy value,Buy fees,Profit\n");
			
			for (CalcOperation op: ops) {
				if (op.getTaxTransactions() != null) {
					for (TaxTransaction tt: op.getTaxTransactions()) {
						fw.write(DateTimeFormatter.ISO_INSTANT.format(op.getDatetime()));
						fw.write(',');
						fw.write(op.getAmountCur());
						fw.write(',');
						fw.write(tt.getAmount().toPlainString());
						fw.write(',');
						fw.write(tt.getPriceSold().toPlainString());
						fw.write(',');
						fw.write(tt.getValueSold().toPlainString());
						fw.write(',');
						fw.write(tt.getSellFees().toPlainString());
						fw.write(',');
						fw.write(tt.getPriceBought().toPlainString());
						fw.write(',');
						fw.write(tt.getValueBought().toPlainString());
						fw.write(',');
						fw.write(tt.getBuyFees().toPlainString());
						fw.write(',');
						fw.write(tt.getBenefit().toPlainString());
						fw.write("\n");
					}
				}
			}
		}
	}
	
	public static void writeSimplifiedTaxesCsv(String path, List<CalcOperation> ops) throws FileNotFoundException, IOException {
		try (FileWriter fw= new FileWriter(path)) {
			fw.write("Datetime,currency,Sell value,Sell fees,Buy value,Buy fees,Profit\n");
			
			for (CalcOperation op: ops) {
				if (op.getTaxTransactions() != null) {
					for (TaxTransaction tt: op.getTaxTransactions()) {
						fw.write(DateTimeFormatter.ISO_INSTANT.format(op.getDatetime()));
						fw.write(',');
						fw.write(op.getAmountCur());
						fw.write(',');
						fw.write(tt.getValueSold().setScale(2, RoundingMode.HALF_UP).toPlainString());
						fw.write(',');
						fw.write(tt.getSellFees().setScale(2, RoundingMode.HALF_UP).toPlainString());
						fw.write(',');
						fw.write(tt.getValueBought().setScale(2, RoundingMode.HALF_UP).toPlainString());
						fw.write(',');
						fw.write(tt.getBuyFees().setScale(2, RoundingMode.HALF_UP).toPlainString());
						fw.write(',');
						fw.write(tt.getBenefit().setScale(2, RoundingMode.HALF_UP).toPlainString());
						fw.write("\n");
					}
				}
			}
		}
	}
}
