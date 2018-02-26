package safristas.bo.safristas;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import exceptions.ValorErradoException;
import safristas.bo.AdiantamentoBO;
import safristas.bo.EmpregadorBO;

public class PagamentoTotalBO {
	private int codigo, qntdBins, qntdEmpregado;
	private double valorEmpreiteiro, valorTotalEmpregados, valorPorEmpregado, valorPorBins, valorDesconto, valorTotal;
	public EmpreiteiroBO iroBO;
	public EmpregadorBO dorBO;
	public DateTime data, dataInicial, dataFinal;
	public AdiantamentoBO adiantBO;
	private String histDesconto;

	public PagamentoTotalBO() {
		super();
		this.codigo = 1;
		this.qntdBins = 1;
		this.qntdEmpregado = 1;
		this.valorEmpreiteiro = 0.0;
		this.valorTotalEmpregados = 0.0;
		this.valorPorEmpregado = 0.0;
		this.valorPorBins = 0.0;
		this.valorDesconto = 0.0;
		this.valorTotal = 0.0;
		this.iroBO = new EmpreiteiroBO();
		this.dorBO = new EmpregadorBO();
		data = new DateTime(DateTimeZone.forID("Etc/GMT+3"));
		dataInicial = new DateTime(DateTimeZone.forID("Etc/GMT+3"));
		dataFinal = new DateTime(DateTimeZone.forID("Etc/GMT+3"));
		adiantBO = new AdiantamentoBO();
		this.histDesconto = "";
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getQntdBins() {
		return qntdBins;
	}

	public void setQntdBins(int qntdBins) {
		this.qntdBins = qntdBins;
	}

	public int getQntdEmpregado() {
		return qntdEmpregado;
	}

	public void setQntdEmpregado(int qntdEmpregado) {
		this.qntdEmpregado = qntdEmpregado;
	}

	public double getValorEmpreiteiro() {
		return valorEmpreiteiro;
	}

	public void setValorEmpreiteiro(double valorEmpreiteiro) throws ValorErradoException {
		if (valorEmpreiteiro < 0)
			throw new ValorErradoException();
		else
			this.valorEmpreiteiro = valorEmpreiteiro;
	}

	public double getValorTotalEmpregados() {
		return valorTotalEmpregados;
	}

	public void setValorTotalEmpregados(double valorTotalEmpregados) {
		this.valorTotalEmpregados = valorTotalEmpregados;
	}

	public double getValorEmpregado() {
		return valorPorEmpregado;
	}

	public void setValorEmpregado(double valorPorEmpregado) throws ValorErradoException {
		if (valorPorEmpregado < 0)
			throw new ValorErradoException();
		else
			this.valorPorEmpregado = valorPorEmpregado;
	}

	public double getValorBins() {
		return valorPorBins;
	}

	public void setValorBins(double valorPorBins) throws ValorErradoException {
		if (valorPorBins < 0)
			throw new ValorErradoException();
		else
			this.valorPorBins = valorPorBins;
	}

	public double getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(double valorDesconto) throws ValorErradoException {
		if (valorDesconto < 0)
			throw new ValorErradoException();
		else
			this.valorDesconto = valorDesconto;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getHistDesconto() {
		return histDesconto;
	}

	public void setHistDesconto(String histDesconto) {
		this.histDesconto = histDesconto;
	}
}
