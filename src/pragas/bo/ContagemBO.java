package pragas.bo;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import exceptions.QuantidadeErradaException;

public class ContagemBO {
	private int codigo;
	public DateTime data;
	public FrascosBO frasco;
	private int qntdInsetos;
	private int qntdDias;
	private BigDecimal indiceFinal;

	public ContagemBO() {
		super();
		this.codigo = 1;
		this.data = new DateTime();
		this.frasco = new FrascosBO();
		this.qntdInsetos = 1;
		this.qntdDias = 1;
		this.indiceFinal = new BigDecimal("0.00");
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getQntdInsetos() {
		return qntdInsetos;
	}

	public void setQntdInsetos(int qntdInsetos) throws QuantidadeErradaException {
		if (qntdInsetos < 0)
			throw new QuantidadeErradaException();
		else
			this.qntdInsetos = qntdInsetos;
	}

	public int getQntdDias() {
		return qntdDias;
	}

	public void setQntdDias(int qntdDias) {
		this.qntdDias = qntdDias;
	}


	public BigDecimal getIndiceFinal() {
		return indiceFinal;
	}

	public void setIndiceFinal(BigDecimal indiceFinal) {
		this.indiceFinal = indiceFinal;
	}

}
