package safristas.bo.safristas;

import exceptions.CpfInvalidoException;
import exceptions.StringVaziaException;
import safristas.bo.CidadeBO;

public class EmpreiteiroBO {
	private int codigo;
	private String nome, apelido, cpf, telefone;
	public CidadeBO cidBO;
	
	

	public EmpreiteiroBO(){
		this.codigo = 1;
		this.cidBO = new CidadeBO();
		this.nome = "";
		this.apelido = "";
		this.cpf = "";
		this.telefone = "";
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

	public String getApelido() {
		return apelido;
	}

	public void setApelido(String apelido) throws StringVaziaException{
		if (apelido.trim().equals(""))
			throw new StringVaziaException();
		else
			this.apelido = apelido;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) throws CpfInvalidoException{
		if (cpf.length() != 11 || !cpf.matches("[0-9]+"))
			throw new CpfInvalidoException();
		else
			this.cpf = cpf;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) throws StringVaziaException{
		if (telefone.trim().equals(""))
			throw new StringVaziaException();
		else
			this.telefone = telefone;
	}
}
