package safristas.bo.outros;

import java.awt.TextArea;

import exceptions.ValorErradoException;
import safristas.bo.AdiantamentoBO;

public class PagamentoUnitarioOutrosBO {
	private int codigo, qntdDias;
	private double valor, desconto, valorTotal;
	public LancaDiaOutrosBO diaoutBO;
	public TextArea histDesconto;
	public PagamentoTotalOutrosBO pgtoTotalOutBO;
	public AdiantamentoBO adiantBO;

	public PagamentoUnitarioOutrosBO() {
		super();
		this.codigo = 1;
		this.qntdDias = 0;
		this.valor = 0.0;
		this.desconto = 0.0;
		this.valorTotal = 0.0;
		this.diaoutBO = new LancaDiaOutrosBO();
		this.histDesconto = new TextArea();
		this.pgtoTotalOutBO = new PagamentoTotalOutrosBO();
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

	public double getDesconto() {
		return desconto;
	}

	public void setDesconto(double desconto) throws ValorErradoException {
		if (desconto < 0)
			throw new ValorErradoException();
		else
			this.desconto = desconto;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}
}
