package util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Numerals {

	public static class NumeralsUnit {

		public String[] integer;
		public String[] decimal;

		/**
		 * @param integer singular, plural
		 * @param decimal singular, plural
		 */
		public NumeralsUnit(String[] integer, String[] decimal){
			this.integer=integer;
			this.decimal=decimal;
		}

	}

	public static final NumeralsUnit UNIT_REAL = new NumeralsUnit(
		new String[]{"Real","Reais"}, new String[]{"Centavo","Centavos"});

	public static final NumeralsUnit UNIT_METER = new NumeralsUnit(
		new String[]{"Metro","Metros"}, new String[]{"Centímetro","Centímetros"});

	private enum QUALIFIERS {

		SINGULAR (new String[]{
			"mil", "milhão", "bilhão", "trilhão", "quatrilhão",
			"quintilhão", "sextilhão", "septilhão", "octilhão", "nonilhão"}),

		PLURAL (new String[]{
			"mil", "milhões", "bilhões", "trilhões", "quatrilhões",
			"quintilhões", "sextilhões", "septilhões", "octilhões", "nonilhões"});

		public String[] names;

		QUALIFIERS(String[] names){
			this.names=names;
		}

	}

	private enum NUMERALS {

		GROUP0_19 (new String[]{"zero",
			"um", "dois", "três", "quatro", "cinco",
			"seis", "sete", "oito", "nove", "dez",
			"onze", "doze", "treze", "quatorze", "quinze",
			"dezesseis", "dezessete", "dezoito", "dezenove"}),

		GROUP20_90 (new String[]{
			"vinte", "trinta", "quarenta", "cinquenta",
			"sessenta",	"setenta", "oitenta", "noventa"}),

		GROUP100 (new String[]{"cem"}),

		GROUP101_900 (new String[]{
			"cento", "duzentos", "trezentos", "quatrocentos", "quinhentos",
			"seiscentos", "setecentos", "oitocentos", "novecentos"});

		public String[] names;

		NUMERALS(String[] names){
			this.names=names;
		}

	}

	private enum CONJUNCTION {

		AND ("e"),
		OF ("de");

		public String name;

		CONJUNCTION(String name){
			this.name=name;
		}

	}

	//number of decimals
	private final int scale;
	//unit label
	private final NumeralsUnit unit;

	/** EURO by default */
	public Numerals(int scale) {
		this(scale,UNIT_REAL);
	}

	public Numerals(int scale, NumeralsUnit unit) {
		this.scale=scale;
		this.unit=unit;
	}


	public String toString(BigDecimal value) {

		String s="";

		value=value.abs().setScale(scale, RoundingMode.HALF_EVEN);

		//retrieves integer and decimal
		BigInteger integer=value.abs().toBigInteger();
		/*
		BigInteger decimal=value.remainder(BigDecimal.ONE).
			multiply(BigDecimal.valueOf(100)).toBigInteger();
		*/
		BigInteger decimal=(value.subtract(new BigDecimal(integer))).
				multiply(new BigDecimal(100)).toBigInteger();

		//processes ZERO
		if (value.signum()==0){
			s+=orderToString(0)+unit.integer[1];
		}
		//processes integer
		else if (integer.signum()>0){
			s+=ordersToString(getOrders(integer), unit.integer);
		}
		//processes decimal
		if (decimal.signum()>0){
			s+=s.isEmpty() ? "" : " "+CONJUNCTION.AND.name+" ";
			s+=ordersToString(getOrders(decimal), unit.decimal);
		}

		//capitalize
		return s.substring(0,1).toUpperCase() + s.substring(1);

	}


	/** Dismembers value into orders */
	private List<Integer> getOrders(BigInteger value) {

		List<Integer> orders=new ArrayList();

		while(value.compareTo(BigInteger.valueOf(1000))>=0) {
			orders.add(value.remainder(BigInteger.valueOf(1000)).intValue());
			value=value.divide(BigInteger.valueOf(1000));
		}
		orders.add(value.intValue());

		return orders;

	}


	/** Concatenates all the orders */
	private String ordersToString(List<Integer> orders, String[] unit) {

		String s="";

		try{
			//last order index
			int last=0;
			//first order value
			Integer order0=orders.get(0);
			//processes below 999
			s+=order0>0 ? orderToString(order0) : "";
			//processes above 999
			for (int i=1; i<orders.size(); i++){

				Integer value=orders.get(i);

				if (value!=0){

					String q=value==1 ?
						QUALIFIERS.SINGULAR.names[i-1] :
						QUALIFIERS.PLURAL.names[i-1];
					/*
					 * ordem actual >= MILHOES
					 * ordem anterior = CENTENAS
					 * nao existem centenas
					 * (MILHOES DE; BILIOES DE; etc)
					 */
					if (i>=2 && last==0 && order0==0){
						q+=" "+CONJUNCTION.OF.name+" ";
					}
					/*
					 * ordem anterior = CENTENAS
					 * existem centenas (EVITA MIL E ?)
					 * centenas inferiores a 100 (E UM; E DOIS; etc)
					 * centenas multiplas de 100 (E CEM; E DUZENTOS; etc)
					 */
					else if (last==0 && order0>0 && (order0<100 || order0%100==0)){
						q+=" "+CONJUNCTION.AND.name+" ";
					}
					/*
					 * ordem actual >= MILHOES
					 * (SEPARA MILHOES, BILIOES, etc)
					 */
					else if (i>=2){
						q+=", ";
					}
					else q+=" ";

					/*
					 * ordem actual = MILHARES
					 * milhares = 1
					 * (EVITA "UM MIL")
					 */

						s=orderToString(value)+q+s;

					last=i;

				}

			}

			//para evitar "UM" + PLURAL da moeda
			if (orders.size()==1 && order0==1)
				s+=unit[0];
			else
				s+=unit[1];

		}catch(Exception e){
			e.printStackTrace();
		}

		return s;
	}


	/** Creates order string */
	private String orderToString(Integer value) {

		String s="";

		if (value<20){
			s+=NUMERALS.GROUP0_19.names[value]+" ";
		}
		else if (value<100){
			s+=NUMERALS.GROUP20_90.names[value/10-2]+" ";
			s+=value%10!=0 ? CONJUNCTION.AND.name+" "+orderToString(value%10) : "";
		}
		else if (value==100){
			s+=NUMERALS.GROUP100.names[0]+" ";
		}
		else if (value<1000){
			s+=NUMERALS.GROUP101_900.names[value/100-1]+" ";
			s+=value%100!=0 ? CONJUNCTION.AND.name+" "+orderToString(value%100) : "";
		}

		return s;

	}
}