package safristas.bo.safristas;

import java.awt.TextArea;

import exceptions.ValorErradoException;
import safristas.bo.AdiantamentoBO;
import safristas.bo.EmpregadoBO;

public class PagamentoUnitarioBO {
	private int codigo, qntdDias;
	private double valor, valorAcrescimo, desconto, valorTotal, descHAT;
	public EmpregadoBO adoBO;
	public TextArea histDesconto;
	public PagamentoTotalBO pgtoTotalBO;
	public AdiantamentoBO adiantBO;

	public PagamentoUnitarioBO() {
		super();
		this.codigo = 1;
		this.qntdDias = 1;
		this.valor = 0.0;
		this.valorAcrescimo = 0.0;
		this.desconto = 0.0;
		this.descHAT = 0.0;
		this.valorTotal = 0.0;
		this.adoBO = new EmpregadoBO();
		this.histDesconto = new TextArea();
		this.pgtoTotalBO = new PagamentoTotalBO();
		this.adiantBO = new AdiantamentoBO();
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getQntdDias() {
		return qntdDias;
	}

	public void setQntdDias(int qntdDias) {
		this.qntdDias = qntdDias;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public double getValorAcrescimo() {
		return valorAcrescimo;
	}

	public void setValorAcrescimo(double valorAcrescimo) throws ValorErradoException {
		if (valorAcrescimo < 0)
			throw new ValorErradoException();
		else
			this.valorAcrescimo = valorAcrescimo;
	}

	public double getDesconto() {
		return desconto;
	}

	public void setDesconto(double desconto) throws ValorErradoException {
		if (desconto < 0)
			throw new ValorErradoException();
		else
			this.desconto = desconto;
	}

	public double getDescHAT() {
		return descHAT;
	}

	public void setDescHAT(double descHAT) throws ValorErradoException {
		if (descHAT < 0)
			throw new ValorErradoException();
		else
			this.descHAT = descHAT;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) throws ValorErradoException {
		if (descHAT < 0)
			throw new ValorErradoException();
		else
			this.valorTotal = valorTotal;
	}
}
