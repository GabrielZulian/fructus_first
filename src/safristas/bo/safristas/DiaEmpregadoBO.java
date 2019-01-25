package safristas.bo.safristas;

import java.math.BigDecimal;

import exceptions.ValorErradoException;
import safristas.bo.EmpregadoBO;

public class DiaEmpregadoBO {
	private int codigo;
	public EmpregadoBO adoBO;
	public LancaDiaBO diaBO;
	private double valor;
	private char presenca;
	private char classificador;
	private char chaoEscada;
	private Integer qntdSacola;
	private BigDecimal rateio;
	private char pagou;

	public DiaEmpregadoBO() {
		super();
		this.codigo = 0;
		this.adoBO = new EmpregadoBO();
		this.diaBO = new LancaDiaBO();
		this.valor = 0.0;
		this.presenca = 'S';
		this.classificador = 'N';
		this.chaoEscada = 'C';
		this.qntdSacola = 0;
		this.rateio = new BigDecimal("0");
		this.pagou = 'N';
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) throws ValorErradoException {
		if (valor<0)
			throw new ValorErradoException();
		else
			this.valor = valor;
	}

	public void setPresenca(char presenca) {
		this.presenca = presenca;
	}

	public char getPresenca() {
		return presenca;
	}

	public String getPresencaString() {
		if (presenca == 'S')
			return "Presente";
		else if (presenca == 'N')
			return "Ausente";
		else
			return "Meio turno";

	}

	public char getClassificador() {
		return classificador;
	}

	public void setClassificador(char classificador) {
		this.classificador = classificador;
	}
	
	public void setRateio(BigDecimal rateio) {
		this.rateio = rateio;
	}
	
	public BigDecimal getRateio() {
		return rateio;
	}

	public char getPagou() {
		return pagou;
	}

	public void setPagou(char pagou) {
		this.pagou = pagou;
	}

	public void setChaoEscada(char chaoEscada) {
		this.chaoEscada = chaoEscada;
	}

	public char getChaoEscada() {
		return chaoEscada;
	}

	public Integer getQntdSacola() {
		return qntdSacola;
	}

	public void setQntdSacola(Integer qntdSacola) {
		this.qntdSacola = qntdSacola;
	}
}
