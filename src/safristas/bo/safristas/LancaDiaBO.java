package safristas.bo.safristas;

import java.awt.TextArea;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import exceptions.QuantidadeErradaException;
import exceptions.ValorErradoException;

public class LancaDiaBO {
	
	private int codigo, qntdBinsClassif, qntBinsEquipe;
	public EmpreiteiroBO iroBO;
	public EquipeBO equipeBO; 
	private double valorBins, valorDia, valorSacola, valorSacolaEscada, valorClassif, valorTotalResto, valorTotal, valorComissao, 
	valorComissIroClassif, valorOutroIro, valorTotalComissao, metaChao, metaEscada, metaChaoEscada;
	private char metodo;
	public DateTime data;
	public TextArea observacao;
	public PagamentoTotalBO pgtoBO;

	public LancaDiaBO() {
		this.codigo = 0;
		this.data = new DateTime(DateTimeZone.forID("Etc/GMT+3"));
		this.iroBO = new EmpreiteiroBO();
		this.equipeBO = new EquipeBO();
		this.qntdBinsClassif = 0;
		this.qntBinsEquipe = 0;
		this.valorBins = 0;
		this.valorDia = 0;
		this.valorSacola = 0;
		this.valorSacola = 0;
		this.valorTotalResto = 0;
		this.valorComissao = 0;
		this.valorComissIroClassif = 0;
		this.valorOutroIro = 0;
		this.valorTotalComissao = 0;
		this.metaChao = 0;
		this.metaEscada = 0;
		this.metaChaoEscada = 0;
		this.metodo = 'B';
		this.observacao = new TextArea();
		this.pgtoBO = new PagamentoTotalBO();
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getQntdBinsClassif() {
		return qntdBinsClassif;
	}

	public void setQntdBinsClassif(int qntdBinsClassif) throws QuantidadeErradaException {
		if (qntdBinsClassif < 0)
			throw new QuantidadeErradaException();
		else
			this.qntdBinsClassif = qntdBinsClassif;
	}

	public int getQntBinsEquipe() {
		return qntBinsEquipe;
	}

	public void setQntBinsEquipe(int qntBinsEquipe) throws QuantidadeErradaException {
		if (qntBinsEquipe < 0)
			throw new QuantidadeErradaException();
		else
			this.qntBinsEquipe = qntBinsEquipe;
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

	public double getValorSacola() {
		return valorSacola;
	}

	public void setValorSacola(double valorSacola) {
		this.valorSacola = valorSacola;
	}

	public double getValorSacolaEscada() {
		return valorSacolaEscada;
	}

	public void setValorSacolaEscada(double valorSacolaEscada) {
		this.valorSacolaEscada = valorSacolaEscada;
	}

	public double getValorDia() {
		return valorDia;
	}

	public void setValorDia(double valorDia) throws ValorErradoException {
		if (valorDia < 0)
			throw new ValorErradoException();
		else
			this.valorDia = valorDia;
	}

	public double getValorTotalResto() {
		return valorTotalResto;
	}
	
	public double getValorClassif() {
		return valorClassif;
	}

	public void setValorClassif(double valorClassif) throws ValorErradoException {
		if (valorClassif < 0)
			throw new ValorErradoException();
		else
			this.valorClassif = valorClassif;
	}


	public void setValorTotalResto(double valorTotalResto) throws ValorErradoException {
		if (valorTotalResto < 0)
			throw new ValorErradoException();
		else
			this.valorTotalResto = valorTotalResto;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) throws ValorErradoException {
		if (valorTotal <= 0)
			throw new ValorErradoException();
		else
			this.valorTotal = valorTotal;
	}

	public double getValorComissao() {
		return valorComissao;
	}

	public void setValorComissao(double valorComissao) throws ValorErradoException {
		if (valorTotal <= 0)
			throw new ValorErradoException();
		else
			this.valorComissao = valorComissao;
	}

	public double getValorComissIroClassif() {
		return valorComissIroClassif;
	}

	public void setValorComissIroClassif(double valorComissIroClassif) throws ValorErradoException {
		if (valorComissIroClassif < 0)
			throw new ValorErradoException();
		else
			this.valorComissIroClassif = valorComissIroClassif;
	}

	public double getValorOutroIro() {
		return valorOutroIro;
	}

	public void setValorOutroIro(double valorOutroIro) throws ValorErradoException {
		if (valorOutroIro < 0)
			throw new ValorErradoException();
		else
			this.valorOutroIro = valorOutroIro;
	}

	public double getValorTotalComissao() {
		return valorTotalComissao;
	}

	public void setValorTotalComissao(double valorTotalComissao) {
		this.valorTotalComissao = valorTotalComissao;
	}

	public double getMetaChao() {
		return metaChao;
	}

	public void setMetaChao(double metaChao) {
		this.metaChao = metaChao;
	}

	public double getMetaEscada() {
		return metaEscada;
	}

	public void setMetaEscada(double metaEscada) {
		this.metaEscada = metaEscada;
	}

	public double getMetaChaoEscada() {
		return metaChaoEscada;
	}

	public void setMetaChaoEscada(double metaChaoEscada) {
		this.metaChaoEscada = metaChaoEscada;
	}

	public char getMetodo() {
		return metodo;
	}

	public void setMetodo(char metodo) {
		this.metodo = metodo;
	}
}