package insumos.bo;

import org.joda.time.DateTime;

import exceptions.CodigoErradoException;
import exceptions.StringVaziaException;

public class AplicacaoBO {
	private int codigo;
	private DateTime data, dataFinal;
	private double quantidade;
	private String historico;
	public TalhaoBO talhaoBO;
	public InsumoBO insumoBO;
	
	public AplicacaoBO() {
		this.codigo = 0;
		this.data = new DateTime();
		this.dataFinal = new DateTime();
		this.talhaoBO = new TalhaoBO();
		this.insumoBO = new InsumoBO();
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) throws CodigoErradoException {
		if (codigo < 0)
			throw new CodigoErradoException();
		else
			this.codigo = codigo;
	}

	public DateTime getData() {
		return data;
	}

	public void setData(DateTime data) {
		this.data = data;
	}

	public DateTime getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(DateTime dataFinal) {
		this.dataFinal = dataFinal;
	}

	public double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(double quantidade) {
		this.quantidade = quantidade;
	}

	public String getHistorico() {
		return historico;
	}

	public void setHistorico(String historico) throws StringVaziaException {
		if (historico.equals(""))
			throw new StringVaziaException();
		else
			this.historico = historico;
	}

}
