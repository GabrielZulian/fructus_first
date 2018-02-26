package safristas.bo.safristas;

import exceptions.StringVaziaException;

public class FechamentoBO {
	private int codigo, codEmpreiteiro, codEquipe, qntBinsEquipe;
	private double valorClassificador, valorBins, valorTotal; 
	private String data;
	
	public FechamentoBO() {
		this.codigo = 0;
		this.data = "";
		this.codEmpreiteiro = 0;
		this.codEquipe = 0;
		this.qntBinsEquipe = 0;
		this.valorClassificador = 0;
		this.valorBins = 0;
		this.valorTotal = 0;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) throws StringVaziaException{
		if (data.trim().equals(""))
			throw new StringVaziaException();
		else 
			this.data = data;
	}

	public int getCodEmpreiteiro() {
		return codEmpreiteiro;
	}

	public void setCodEmpreiteiro(int codEmpreiteiro) {
		this.codEmpreiteiro = codEmpreiteiro;
	}

	public int getCodEquipe() {
		return codEquipe;
	}

	public void setCodEquipe(int codEquipe) {
		this.codEquipe = codEquipe;
	}

	public int getQntBinsEquipe() {
		return qntBinsEquipe;
	}

	public void setQntBinsEquipe(int qntBinsEquipe) {
		this.qntBinsEquipe = qntBinsEquipe;
	}

	public double getValorClassificador() {
		return valorClassificador;
	}

	public void setValorClassificador(double valorClassificador) {
		this.valorClassificador = valorClassificador;
	}

	public double getValorBins() {
		return valorBins;
	}

	public void setValorBins(double valorBins) {
		this.valorBins = valorBins;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}
}
