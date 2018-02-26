package insumos.bo;

import exceptions.StringVaziaException;

public class InsumoBO {

	int codigo, diasResidual;
	String descricao, unidade;
	
	public InsumoBO() {
		super();
		this.codigo = 0;
		this.diasResidual = 0;
		this.descricao = "";
		this.unidade = "";
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getDiasResidual() {
		return diasResidual;
	}

	public void setDiasResidual(int diasResidual) {
		this.diasResidual = diasResidual;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) throws StringVaziaException {
		if (descricao.equals(""))
			throw new StringVaziaException();
		else
			this.descricao = descricao;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}
	
}
