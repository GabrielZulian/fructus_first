package safristas.bo;

import exceptions.CpfInvalidoException;
import exceptions.StringVaziaException;

public class EmpregadorBO {
	private int codigo;
	private String nome, cpf, ie, telefone;
	public CidadeBO cidBO;

	public EmpregadorBO() {
		this.codigo = 1;
		this.cidBO = new CidadeBO();
		this.nome = "";
		this.cpf = "";
		this.ie = "";
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

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) throws CpfInvalidoException{
		if (!cpf.matches("[0-9]+"))
			throw new CpfInvalidoException();
		else
			this.cpf = cpf;
	}

	public String getIe() {
		return ie;
	}

	public void setIe(String ie) throws StringVaziaException {
		if (ie.trim().equals(""))
			throw new StringVaziaException();
		else
			this.ie = ie;
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
