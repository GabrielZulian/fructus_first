package insumos.bo;

import java.math.BigDecimal;

public class TalhaoBO {

	int codigo, numero;
	String tipo;
	BigDecimal area;
	
	public TalhaoBO() {
		this.numero = 0;
		this.area = new BigDecimal("0.00");
	}
	
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public BigDecimal getArea() {
		return area;
	}

	public void setArea(BigDecimal area) {
		this.area = area;
	}
}
