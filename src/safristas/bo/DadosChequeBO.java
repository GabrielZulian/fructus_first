package safristas.bo;

public class DadosChequeBO {
	private String nome, valorExtenso, valor;

	public DadosChequeBO() {
		nome = "";
		valorExtenso = "";
		valor = "";
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getValorExtenso() {
		return valorExtenso;
	}

	public void setValorExtenso(String valorExtenso) {
		this.valorExtenso = valorExtenso;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
}