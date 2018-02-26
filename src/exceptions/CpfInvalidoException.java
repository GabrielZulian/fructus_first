package exceptions;

public class CpfInvalidoException extends Exception {
	private static final long serialVersionUID = 3753420095111310789L;

	@Override
	public String toString() {
		return "CPF Inválido!";
	}
	
}
