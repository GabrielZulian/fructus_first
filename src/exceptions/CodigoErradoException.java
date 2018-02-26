package exceptions;

public class CodigoErradoException extends Exception{
	private static final long serialVersionUID = -8828805978534927754L;

	@Override
	public String toString() {
		return "Código errado ou inexistente";
	}
	

}
