package exceptions;

public class QuantidadeErradaException extends Exception{
	private static final long serialVersionUID = -6673769867080174307L;

	@Override
	public String toString() {
		return "Quantidade errada ou inexistente";
	}
	

}
