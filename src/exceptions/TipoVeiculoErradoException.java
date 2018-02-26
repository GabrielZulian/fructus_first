package exceptions;

public class TipoVeiculoErradoException extends Exception{
	private static final long serialVersionUID = 5855668113354339654L;

	@Override
	public String toString() {
		return "Tipo veículo errado ou inexistente";
	}
}
