package safristas.bo;

import exceptions.CodigoErradoException;
import exceptions.StringVaziaException;

public class VariedadeBO {
	
	int codigo;
	String descricao;
	
	public VariedadeBO() {
		this.codigo = 0;
		this.descricao = "";
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) throws StringVaziaException {
		if (descricao.equals(""))
			throw new StringVaziaException();
		else
			this.descricao = descricao;
	}
}
