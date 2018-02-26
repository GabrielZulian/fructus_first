package safristas.bo;

import exceptions.StringVaziaException;

public class ProvaBO {

	private int codigo;
	private String nome;

	public ProvaBO() {
		codigo = 0;
		nome = "";
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
}
