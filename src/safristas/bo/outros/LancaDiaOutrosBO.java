package safristas.bo.outros;

import java.awt.TextArea;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import exceptions.QuantidadeErradaException;
import exceptions.ValorErradoException;
import safristas.bo.EmpregadoBO;

public class LancaDiaOutrosBO {
	private int codigo, qntBins, nroNF, lote, codPagamento;
	private double valorBins, valorTotal;
	private char pagou;
	public EmpregadoBO adoBO;
	public VeiculoBO veicBO;
	public DateTime data;
	private String variedade;
	public TextArea observacao;

	public LancaDiaOutrosBO() {
		this.codigo = 0;
		this.qntBins = 1;
		this.nroNF = 0;
		this.lote = 0;
		this.setCodPagamento(0);
		this.valorBins = 0.0;
		this.valorTotal = 0.0;
		this.pagou = 'N';
		this.adoBO = new EmpregadoBO();
		this.veicBO = new VeiculoBO();
		this.data = new DateTime(DateTimeZone.forID("Etc/GMT+3"));
		this.variedade = "";
		this.observacao = new TextArea();
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getQntBins() {
		return qntBins;
	}

	public void setQntBins(int qntBins) throws QuantidadeErradaException {
		if (qntBins <= 0)
			throw new QuantidadeErradaException();
		else
			this.qntBins = qntBins;
	}

	public int getNroNF() {
		return nroNF;
	}

	public void setNroNF(int nroNF) {
		this.nroNF = nroNF;
	}

	public int getLote() {
		return lote;
	}

	public void setLote(int lote) {
		this.lote = lote;
	}

	public String getVariedade() {
		return variedade;
	}

	public void setVariedade(String variedade) {
		this.variedade = variedade;
	}

	public double getValorBins() {
		return valorBins;
	}

	public void setValorBins(double valorBins) throws ValorErradoException {
		if (valorBins < 0)
			throw new ValorErradoException();
		else
			this.valorBins = valorBins;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) throws ValorErradoException {
		if (valorTotal < 0)
			throw new ValorErradoException();
		else
			this.valorTotal = valorTotal;
	}

	public char getPagou() {
		return pagou;
	}

	public void setPagou(char pagou) {
		this.pagou = pagou;
	}

	public int getCodPagamento() {
		return codPagamento;
	}

	public void setCodPagamento(int codPagamento) {
		this.codPagamento = codPagamento;
	}
}