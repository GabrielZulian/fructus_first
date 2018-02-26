package pragas.bo;

import gerais.bo.QuadraBO;

public class FrascosBO {
	private int codigo, qntdFrascos;
	public QuadraBO quadra;
	private char tipoPraga;

	public FrascosBO() {
		super();
		this.codigo = 0;
		this.qntdFrascos = 0;
		this.quadra = new QuadraBO();
		this.tipoPraga = ' ';
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getQntdFrascos() {
		return qntdFrascos;
	}

	public void setQntdFrascos(int qntdFrascos) {
		this.qntdFrascos = qntdFrascos;
	}

	public char getTipoPraga() {
		return tipoPraga;
	}

	public void setTipoPraga(char tipoPraga) {
		this.tipoPraga = tipoPraga;
	}

	public String getTipoPragaString() {
		switch (tipoPraga) {
		case 'G': return "Grapholita";
		case 'B': return "Bonagota";
		case 'M': return "Mosca da fruta";
		default: return "";
		}
	}
}
