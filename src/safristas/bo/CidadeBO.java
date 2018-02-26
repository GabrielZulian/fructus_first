package safristas.bo;

import exceptions.StringVaziaException;

public class CidadeBO {
	private int codigo;
	private String nome, uf;
	
	public CidadeBO(){
		codigo = 0;
		nome = "";
		uf = "";
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) throws StringVaziaException {
		if (nome.trim().equals(""))
			throw new StringVaziaException();
		else
			this.nome = nome;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}
	
	
}
