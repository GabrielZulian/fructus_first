package safristas.bo.outros;

import exceptions.StringVaziaException;
import safristas.bo.EmpregadoBO;
import safristas.bo.EmpregadorBO;
import safristas.bo.safristas.EmpreiteiroBO;

public class VeiculoBO {

	private int codigo;
	private String placa, descricao;
	public EmpregadoBO proprietarioAdo;
	public EmpregadorBO proprietarioDor;
	public EmpreiteiroBO proprietarioIro;
	public char tipoEmpregado;
	private char tipoVeiculo;

	public VeiculoBO() {
		super();
		this.codigo = 0;
		this.placa = "";
		this.descricao = "";
		this.proprietarioIro = new EmpreiteiroBO();
		this.proprietarioAdo = new EmpregadoBO();
		this.proprietarioDor = new EmpregadorBO();
		this.tipoVeiculo = 'T';
		this.tipoEmpregado = 'I';
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) throws StringVaziaException {
		if (placa.equals(""))
			throw new StringVaziaException();
		else
			this.placa = placa;
	}	

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public char getTipoVeiculo() {
		return tipoVeiculo;
	}

	public String getTipoVeiculoString() {
		if (this.tipoVeiculo == 'T')
			return "Trator";
		else
			return "Caminhão";
	}

	public void setTipoVeiculo(char tipoVeiculo) {
		this.tipoVeiculo = tipoVeiculo;
	}

	public char getTipoEmpregado() {
		return tipoEmpregado;
	}
	
	public String getTipoEmpregadoString() {
		if (tipoEmpregado == 'I')
			return "Empreiteiro";
		else if (tipoEmpregado == 'A')
			return "Empregado";
		else if (tipoEmpregado == 'D')
			return "Empregador";
		
		return "";
	}
	
	public String getNomeProprietario() {
		if (tipoEmpregado == 'I')
			return proprietarioIro.getNome();
		else if (tipoEmpregado == 'A')
			return proprietarioAdo.getNome();
		else if (tipoEmpregado == 'D')
			return proprietarioDor.getNome();
		
		return "";
	}

	public void setTipoEmpregado(char tipoEmpregado) {
		this.tipoEmpregado = tipoEmpregado;
	}
}
