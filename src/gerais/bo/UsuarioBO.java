package gerais.bo;

import exceptions.StringVaziaException;

public class UsuarioBO {
	private int codigo;
	private String nome, senha;
	
	public UsuarioBO(){
		codigo = 0;
		nome = "";
		senha = "";
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

	public void setNome(String nome) throws StringVaziaException{
		if (nome.trim().equals(""))
			throw new StringVaziaException();
		else
			this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
}
