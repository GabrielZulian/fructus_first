package exceptions;

public class PresencaErradaException extends Exception{
	private static final long serialVersionUID = 3579007385562347346L;

	@Override
	public String toString() {
		return "Presença errada ou inexistente";
	}
	

}
