package com.tiparega.cryptotax;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.tiparega.cryptotax.dto.CalcOperation;
import com.tiparega.cryptotax.dto.Operation;
import com.tiparega.cryptotax.svc.BenefitsCalc;
import com.tiparega.cryptotax.svc.Csv;

public class Main {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		//"/home/alex/Documentos/Documentos/Renta/2024/2025-04.csv";
		if (args.length != 1) {
			System.out.println("USAGE: java -jar cryptotax.jar <csv_file>");
			System.exit(10);
		}
		
		String original=args[0];
		String[] parts= original.split("\\.");
		String filename= "";
		for (int i=0; i<parts.length-1;i++) {
			filename+=parts[i] + ".";
		}
		filename=filename.substring(0,filename.length()-1);
		String extension= "."+parts[parts.length-1];
		
		String calc=filename+"-calc"+extension;
		String taxes=filename+"-taxes"+extension;
		String simpleTaxes=filename+"-simple-taxes"+extension;
		
		List<Operation> ops= Csv.readCsv(original);
		List<CalcOperation> calcOps= BenefitsCalc.calculateBenefits(ops);
		Csv.writeCsv(calc, calcOps);
		System.out.println("Written calc: " + calc);
		Csv.writeTaxesCsv(taxes, calcOps);
		System.out.println("Written taxes: " + taxes);
		Csv.writeSimplifiedTaxesCsv(simpleTaxes, calcOps);
		System.out.println("Written taxes: " + taxes);
	}
}
