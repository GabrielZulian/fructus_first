package safristas.bo.safristas;

import exceptions.StringVaziaException;

public class EquipeBO {
	private int codigo;
	private String nome;
	public EmpreiteiroBO iroBO;

	public EquipeBO(){
		iroBO = new EmpreiteiroBO();
		nome = "";
		codigo = 0;
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

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo){
		this.codigo = codigo;
	}
}
