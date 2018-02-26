package util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

public class Parse {
	static DecimalFormatSymbols symbols = new DecimalFormatSymbols();
	static String pattern = "#,##0.0#";
	static DecimalFormat decimalFormat;
	
	public static final BigDecimal stringToBigDecimal (String string) throws ParseException {
		symbols.setGroupingSeparator('.');
		symbols.setDecimalSeparator(',');
		decimalFormat = new DecimalFormat(pattern, symbols);
		decimalFormat.setParseBigDecimal(true);
		
		BigDecimal numero = (BigDecimal) decimalFormat.parse(string);
		return numero;
	}
	
	public static final BigDecimal stringToBigDecimalPonto (String string) throws ParseException {
		symbols.setGroupingSeparator(',');
		symbols.setDecimalSeparator('.');
		decimalFormat = new DecimalFormat(pattern, symbols);
		decimalFormat.setParseBigDecimal(true);
		
		BigDecimal numero = (BigDecimal) decimalFormat.parse(string);
		return numero;
	}
	
	

}
