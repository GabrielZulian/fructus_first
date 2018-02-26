package safristas.bo;

import exceptions.CpfInvalidoException;
import exceptions.StringVaziaException;
import safristas.bo.safristas.EquipeBO;

public class EmpregadoBO {
	private int codigo;
	private String nome, apelido, cpf;
	public ProvaBO funcaoBO;
	public EquipeBO equipeBO;

	public EmpregadoBO() {
		codigo = 1;
		nome = "";
		apelido = "";
		equipeBO = new EquipeBO();
		funcaoBO = new ProvaBO();
		cpf = "";
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

	public void setApelido(String apelido) {
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

}
