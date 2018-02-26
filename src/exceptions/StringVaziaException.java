package exceptions;

public class StringVaziaException extends Exception{
	private static final long serialVersionUID = -5887892795146638812L;

	@Override
	public String toString() {
		return "Texto não pode ser vazio";
	}
	

}
