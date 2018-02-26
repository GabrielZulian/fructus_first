package safristas.bo;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import exceptions.ValorErradoException;
import safristas.bo.safristas.EmpreiteiroBO;

public class AdiantamentoBO {

	private int codigo;
	public DateTime data;
	public EmpregadoBO adoBO;
	public EmpreiteiroBO iroBO;
	private char tipo, pagou;
	private double valor;

	public AdiantamentoBO() {
		super();
		this.codigo = 0;
		this.data = new DateTime(DateTimeZone.forID("Etc/GMT+3"));
		this.adoBO = new EmpregadoBO();
		this.iroBO = new EmpreiteiroBO();
		this.tipo = 'A';
		this.valor = 0.00;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	public char getTipo() {
		return tipo;
	}
	
	public String getTipoString() {
		if (this.tipo == 'A')
			return "Empregado";
		else
			return "Empreiteiro";
	}

	public void setTipo(char tipo) {
		this.tipo = tipo;
	}
	
	public char getPagou() {
		return pagou;
	}
	
	public String getPagouString() {
		if (pagou == 'S')
			return "Pago";
		else
			return "Aberto";
	}

	public void setPagou(char pagou) {
		this.pagou = pagou;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) throws ValorErradoException {
		if (valor <= 0.00)
			throw new ValorErradoException();
		else
			this.valor = valor;
	}
}
