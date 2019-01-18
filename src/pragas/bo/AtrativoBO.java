package pragas.bo;

import exceptions.StringVaziaException;

public class AtrativoBO {
	private int codigo, diasTroca;
	private String descricao;
	
	public AtrativoBO(){
		codigo = 0;
		descricao = "";
		diasTroca = 0;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) throws StringVaziaException {
		if (descricao.trim().equals(""))
			throw new StringVaziaException();
		else
			this.descricao = descricao;
	}

	public int getDiasTroca() {
		return diasTroca;
	}

	public void setDiasTroca(int diasTroca) {
		this.diasTroca = diasTroca;
	}
}
