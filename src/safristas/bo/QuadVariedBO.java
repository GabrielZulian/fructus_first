package safristas.bo;

import java.math.BigDecimal;

import exceptions.CodigoErradoException;
import exceptions.ObjetoNuloException;
import exceptions.QuantidadeErradaException;
import gerais.bo.QuadraBO;

public class QuadVariedBO {
	
	private int codigo;
	public QuadraBO quadBO;
	public VariedadeBO variBO;
	private BigDecimal area;
	
	public QuadVariedBO() {
		this.codigo = 0;
		this.quadBO = new QuadraBO();
		this.variBO = new VariedadeBO();
		this.area = new BigDecimal("0.00");
	}
	
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) throws CodigoErradoException {
		if (codigo < 0)
			throw new CodigoErradoException();
		else
			this.codigo = codigo;
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

	public QuadraBO getQuadBO() {
		return quadBO;
	}

	public void setQuadBO(QuadraBO quadBO) throws ObjetoNuloException {
		if (quadBO == null)
			throw new ObjetoNuloException();
		else
			this.quadBO = quadBO;
	}

	public VariedadeBO getVariBO() {
		return variBO;
	}

	public void setVariBO(VariedadeBO variBO) throws ObjetoNuloException {
		if (variBO == null)
			throw new ObjetoNuloException();
		else
			this.variBO = variBO;
	}
}
