package safristas.bo.outros;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import exceptions.QuantidadeErradaException;
import safristas.bo.EmpregadorBO;

public class PagamentoTotalOutrosBO {
	private int codigo, qntdEmpregado;
	private double valorTotal;
	public EmpregadorBO dorBO;
	public DateTime data, dataInicial, dataFinal;

	public PagamentoTotalOutrosBO() {
		super();
		this.codigo = 1;
		this.qntdEmpregado = 1;
		this.valorTotal = 0.0;
		this.dorBO = new EmpregadorBO();
		data = new DateTime(DateTimeZone.forID("Etc/GMT+3"));
		dataInicial = new DateTime(DateTimeZone.forID("Etc/GMT+3"));
		dataFinal = new DateTime(DateTimeZone.forID("Etc/GMT+3"));
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getQntdEmpregado() {
		return qntdEmpregado;
	}

	public void setQntdEmpregado(int qntdEmpregado) throws QuantidadeErradaException {
		if (qntdEmpregado < 0)
			throw new QuantidadeErradaException();
		else
			this.qntdEmpregado = qntdEmpregado;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}
}
