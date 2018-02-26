package gerais.bo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import exceptions.ObjetoNuloException;
import exceptions.QuantidadeErradaException;
import safristas.bo.VariedadeBO;

public class QuadraBO {
	
	private int numero, anoPlantio, nroPlantasHectare, qntdFrascos;
	private BigDecimal area;
	private List<VariedadeBO> variedades = new ArrayList<VariedadeBO>();
	private char tipoPraga;
	
	public QuadraBO() {
		this.numero = 1;
		this.anoPlantio = 1900;
		this.nroPlantasHectare = 1;
		this.area = new BigDecimal("0.00");
		variedades = null;
		this.qntdFrascos = 1;
		this.tipoPraga = ' ';
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public int getAnoPlantio() {
		return anoPlantio;
	}

	public void setAnoPlantio(int anoPlantio) {
		this.anoPlantio = anoPlantio;
	}

	public int getNroPlantasHectare() {
		return nroPlantasHectare;
	}

	public void setNroPlantasHectare(int nroPlantasHectare) throws QuantidadeErradaException {
		if (nroPlantasHectare < 0)
			throw new QuantidadeErradaException();
		else
			this.nroPlantasHectare = nroPlantasHectare;
	}

	public BigDecimal getArea() {
		return area;
	}

	public void setArea(BigDecimal area) throws QuantidadeErradaException {
		if (area.compareTo(BigDecimal.ZERO) < 0)
			throw new QuantidadeErradaException();
		else
			this.area = area;
	}

	public List<VariedadeBO> getVariedades() {
		return variedades;
	}

	public void setVariedades(List<VariedadeBO> variedades) throws ObjetoNuloException {
		if (variedades == null)
			throw new ObjetoNuloException();
		else
			this.variedades = variedades;
	}
	
	public int getQntdFrascos() {
		return qntdFrascos;
	}

	public void setQntdFrascos(int qntdFrascos) {
		this.qntdFrascos = qntdFrascos;
	}

	public char getTipoPraga() {
		return tipoPraga;
	}

	public void setTipoPraga(char tipoPraga) {
		this.tipoPraga = tipoPraga;
	}
	
	public String getTipoPragaString() {
		switch (tipoPraga) {
		case 'G': return "Grapholita";
		case 'B': return "Bonagota";
		case 'M': return "Mosca da fruta";
		default: return "";
		}
	}
}
