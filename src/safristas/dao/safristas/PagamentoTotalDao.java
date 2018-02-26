package safristas.dao.safristas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.joda.time.DateTime;

import exceptions.StringVaziaException;
import exceptions.ValorErradoException;
import gerais.dao.Conexao;
import safristas.bo.safristas.PagamentoTotalBO;

public class PagamentoTotalDao {
	Connection conexao;

	public PagamentoTotalDao() {
		conexao = Conexao.conectaBanco();
	}

	public ArrayList <PagamentoTotalBO> consultaPorCodigo(int cod)
	{
		ArrayList<PagamentoTotalBO> pgtoTotalBO = consulta("pgtotal_codigo = " + cod, "pgtotal_codigo");
		return pgtoTotalBO;
	}
	
	public ArrayList <PagamentoTotalBO> consultaPorData(String data)
	{
		ArrayList<PagamentoTotalBO> pgtoTotalBO = consulta("pgtotal_data LIKE '%" + data + "%'", "pgtotal_data, pgtotal_codigo");
		return pgtoTotalBO;
	}

	public ArrayList <PagamentoTotalBO> consultaPorDataInicial(String data)
	{
		ArrayList<PagamentoTotalBO> pgtoTotalBO = consulta("pgtotal_datainicial LIKE '%" + data + "%'", "pgtotal_datainicial, pgtotal_codigo");
		return pgtoTotalBO;
	}

	public ArrayList <PagamentoTotalBO> consultaPorDataFinal(String data)
	{
		ArrayList<PagamentoTotalBO> pgtoTotalBO = consulta("pgtotal_datafinal LIKE '%" + data + "%'", "pgtotal_datafinal, pgtotal_codigo");
		return pgtoTotalBO;
	}

	public ArrayList <PagamentoTotalBO> consultaPorValorTotal(double valor)
	{
		ArrayList<PagamentoTotalBO> pgtoTotalBO = consulta("pgtotal_valortotal = " + valor, "pgtotal_valortotal");
		return pgtoTotalBO;
	}

	public ArrayList <PagamentoTotalBO> consultaPorNomeEmpreiteiro(String nome)
	{
		ArrayList<PagamentoTotalBO> pgtoTotalBO = consulta("EMPREITEIRO.IRO_NOME containing '" + nome + "'", "EMPREITEIRO.IRO_NOME, pgtotal_data");
		return pgtoTotalBO;
	}

	public ArrayList <PagamentoTotalBO> consultaPorCodEmpreiteiro(int cod)
	{
		ArrayList<PagamentoTotalBO> pgtoTotalBO = consulta("iro_codigo = " + cod, "iro_codigo, pgtotal_data, pgtotal_codigo");
		return pgtoTotalBO;
	}

	public ArrayList <PagamentoTotalBO> consultaPorNomeEmpregador(String nome)
	{
		ArrayList<PagamentoTotalBO> pgtoTotalBO = consulta("EMPREGADOR.DOR_NOME containing '" + nome + "'", "EMPREGADOR.DOR_NOME, pgtotal_data");
		return pgtoTotalBO;
	}

	public ArrayList <PagamentoTotalBO> consultaPorCodEmpregador(int cod)
	{
		ArrayList<PagamentoTotalBO> pgtoTotalBO = consulta("dor_codigo = " + cod, "dor_codigo");
		return pgtoTotalBO;
	}

	private ArrayList<PagamentoTotalBO> consulta(String sentencaSQL, String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();

			// faz a consulta
			registros = sentenca.executeQuery("select PAGAMENTOTOTAL.PGTOTAL_CODIGO, PAGAMENTOTOTAL.PGTOTAL_DATA, PAGAMENTOTOTAL.PGTOTAL_DATAINICIAL, PAGAMENTOTOTAL.PGTOTAL_DATAFINAL"
					+ ", EMPREITEIRO.IRO_CODIGO, EMPREITEIRO.IRO_NOME, EMPREGADOR.DOR_CODIGO, EMPREGADOR.DOR_NOME, PAGAMENTOTOTAL.PGTOTAL_VALORTOTAL"
					+ ", (PAGAMENTOTOTAL.PGTOTAL_VALORTOTAL-PAGAMENTOTOTAL.PGTOTAL_VALOREMPREITEIRO) as VALOR_EMPREGADO, PAGAMENTOTOTAL.PGTOTAL_QNTDEMPREGADO"
					+ ", PAGAMENTOTOTAL.PGTOTAL_QNTDBINS, PAGAMENTOTOTAL.PGTOTAL_VALOREMPREGADO, PAGAMENTOTOTAL.PGTOTAL_VALOREMPREITEIRO"
					+ ", PAGAMENTOTOTAL.PGTOTAL_VALORDESCONTO, PAGAMENTOTOTAL.PGTOTAL_CODADIANTAMENTO, PAGAMENTOTOTAL.PGTOTAL_VALORBINS, PAGAMENTOTOTAL.PGTOTAL_HISTORICODESC FROM PAGAMENTOTOTAL"
					+ " INNER JOIN EMPREITEIRO ON EMPREITEIRO.IRO_CODIGO = PAGAMENTOTOTAL.PGTOTAL_CODEMPREITEIRO"
					+ " INNER JOIN EMPREGADOR ON EMPREGADOR.DOR_CODIGO = PAGAMENTOTOTAL.PGTOTAL_CODEMPREGADOR"
					+ " WHERE " + sentencaSQL + " ORDER BY " + ordem);

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
			}
			else {
				int i = 0;
				ArrayList<PagamentoTotalBO> pagTotalBO = new ArrayList<PagamentoTotalBO>();
				do {
					pagTotalBO.add(new PagamentoTotalBO());
					try {
						pagTotalBO.get(i).setCodigo(Integer.parseInt(registros.getString("PGTOTAL_CODIGO")));
						pagTotalBO.get(i).data = new DateTime(registros.getDate("PGTOTAL_DATA"));
						pagTotalBO.get(i).dataInicial = new DateTime(registros.getDate("PGTOTAL_DATAINICIAL"));
						pagTotalBO.get(i).dataFinal = new DateTime(registros.getDate("PGTOTAL_DATAFINAL"));
						pagTotalBO.get(i).iroBO.setCodigo(registros.getInt("IRO_CODIGO"));
						pagTotalBO.get(i).iroBO.setNome(registros.getString("IRO_NOME"));
						pagTotalBO.get(i).dorBO.setCodigo(registros.getInt("DOR_CODIGO"));
						pagTotalBO.get(i).dorBO.setNome(registros.getString("DOR_NOME"));
						pagTotalBO.get(i).setQntdBins(registros.getInt("PGTOTAL_QNTDBINS"));
						pagTotalBO.get(i).setQntdEmpregado(registros.getInt("PGTOTAL_QNTDEMPREGADO"));
						pagTotalBO.get(i).setValorBins(registros.getDouble("PGTOTAL_VALORBINS"));
						pagTotalBO.get(i).setValorEmpregado(registros.getDouble("PGTOTAL_VALOREMPREGADO"));
						pagTotalBO.get(i).setValorTotalEmpregados(registros.getDouble("VALOR_EMPREGADO"));
						pagTotalBO.get(i).setValorEmpreiteiro(registros.getDouble("PGTOTAL_VALOREMPREITEIRO"));
						pagTotalBO.get(i).setValorTotal(registros.getDouble("PGTOTAL_VALORTOTAL"));
						pagTotalBO.get(i).setValorDesconto(registros.getDouble("PGTOTAL_VALORDESCONTO"));
						pagTotalBO.get(i).adiantBO.setCodigo(registros.getInt("PGTOTAL_CODADIANTAMENTO"));
						pagTotalBO.get(i).setHistDesconto(registros.getString("PGTOTAL_HISTORICODESC"));
					} catch (StringVaziaException e) {
					} catch (ValorErradoException e) {}
					i++;
				} while (registros.next());
				return pagTotalBO;   
			}
			sentenca.close();
		}catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível carregar os dados!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	public int getUltimoCod () {
		Statement sentenca;
		ResultSet registros;
		try {
			sentenca = conexao.createStatement();
			registros = sentenca.executeQuery("SELECT MAX(pgtotal_codigo) FROM pagamentototal");
			if(registros.next())
				return registros.getInt(1);  
			else 
				JOptionPane.showMessageDialog(null, "Nenhum registro foi encontrado!", "Mensagem", JOptionPane.WARNING_MESSAGE);

		} catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null, "Não foi possível carregar os dados!\n" +"Mensagem: " + eSQL.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
		return 0;
	}

	public int consultaQntdBins (int cod, DateTime data1, DateTime data2) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();
			// faz a consulta
			registros = sentenca.executeQuery("select sum(DIATRABALHO.DIA_QNTDBINS + DIATRABALHO.DIA_QNTDBINSCLASSIF) as qntdbins from "
					+ " DIATRABALHO WHERE DIATRABALHO.DIA_DATA BETWEEN '" + data1.getDayOfMonth() + "." + data1.getMonthOfYear() + "." + data1.getYear()
					+ "' and '" + data2.getDayOfMonth() + "." + data2.getMonthOfYear() + "." + data2.getYear()
					+ "' and DIATRABALHO.DIA_CODEMPREITEIRO = " + cod
					+ " and DIATRABALHO.DIA_CODPAGAMENTO IS NULL");

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
			}else {
				int qntd = registros.getInt("qntdbins");
				return qntd;
			}

			sentenca.close();
		} catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível carregar os dados!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
		return 0;
	}
	
	public double consultaValorEmpreiteiro (int cod, DateTime data1, DateTime data2) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();
			// faz a consulta
			registros = sentenca.executeQuery("select sum(DIATRABALHO.DIA_VALORTOTALCOMISSAO) as valorEmpreiteiro from "
					+ " DIATRABALHO WHERE DIATRABALHO.DIA_DATA BETWEEN '" + data1.getDayOfMonth() + "." + data1.getMonthOfYear() + "." + data1.getYear()
					+ "' and '" + data2.getDayOfMonth() + "." + data2.getMonthOfYear() + "." + data2.getYear()
					+ "' and DIATRABALHO.DIA_CODEMPREITEIRO = " + cod
					+ " and DIATRABALHO.DIA_CODPAGAMENTO IS NULL");
			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
			}else {
				double valorEmpreiteiro = registros.getFloat("valorEmpreiteiro");
				return valorEmpreiteiro;
			}

			sentenca.close();
		}catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível carregar os dados!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
		return 0;
	}

	public void atualizaPagos(DateTime data1, DateTime data2, int codEmpreitreiro, int codPagamento, String codigosEmpregados) {
		Statement sentenca;

		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "update DIAEMPREGADO de set de.DIAEMP_PAGOU = 'S', de.DIAEMP_CODPAGAMENTO = " + codPagamento + " where de.DIAEMP_CODIGO in ( "
					+ "select de1.DIAEMP_CODIGO from DIAEMPREGADO de1 inner join DIATRABALHO dt on de1.DIAEMP_CODIGODIA = dt.DIA_CODIGO "
					+ "inner join EMPREGADO on de1.DIAEMP_CODEMPREGADO = EMPREGADO.ADO_CODIGO "
					+ "inner join EQUIPE on EMPREGADO.ADO_CODEQUIPE = EQUIPE.EQUIPE_CODIGO "
					+ "inner join EMPREITEIRO on EQUIPE.EQUIPE_CODEMPREITEIRO = EMPREITEIRO.IRO_CODIGO "
					+ "where EMPREITEIRO.IRO_CODIGO = " + codEmpreitreiro + " and dt.DIA_DATA between '"
					+ data1.getDayOfMonth() + "." + data1.getMonthOfYear() + "." + data1.getYear()
					+ "' and '" + data2.getDayOfMonth() + "." + data2.getMonthOfYear() + "." + data2.getYear() + "'"
					+ "and de1.DIAEMP_CODPAGAMENTO IS NULL "
					+ "and de1.DIAEMP_CODEMPREGADO in (" + codigosEmpregados + "))";	
			sentenca.executeUpdate(sentencaSQL);
			sentenca.close();
		} catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível realizar a atualização!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void atualizaPagosDia(DateTime data1, DateTime data2, int codEmpreitreiro, int codPagamento) {
		Statement sentenca;

		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "update DIATRABALHO set DIA_CODPAGAMENTO = " + codPagamento + " where DIA_DATA between '"
					+ data1.getDayOfMonth() + "." + data1.getMonthOfYear() + "." + data1.getYear() 
					+ "' and '" + data2.getDayOfMonth() + "." + data2.getMonthOfYear() + "." + data2.getYear()
					+ "' and DIA_CODPAGAMENTO IS NULL";	
			sentenca.executeUpdate(sentencaSQL);
			sentenca.close();
		} catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível realizar a atualização!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	public void desatualizaPagos(DateTime data1, DateTime data2, int codEmpreitreiro, int codPagamento, String codigosEmpregados) {
		Statement sentenca;

		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "update DIAEMPREGADO de set de.DIAEMP_PAGOU = 'N', de.DIAEMP_CODPAGAMENTO = null where de.DIAEMP_CODIGO in ("
					+ "select de1.DIAEMP_CODIGO from DIAEMPREGADO de1 inner join DIATRABALHO dt on de1.DIAEMP_CODIGODIA = dt.DIA_CODIGO "
					+ "inner join EMPREGADO ado on de1.DIAEMP_CODEMPREGADO = ado.ADO_CODIGO "
					+ "inner join EQUIPE equi on ado.ADO_CODEQUIPE = equi.EQUIPE_CODIGO "
					+ "inner join EMPREITEIRO iro on equi.EQUIPE_CODEMPREITEIRO = iro.IRO_CODIGO "
					+ "where iro.IRO_CODIGO = " + codEmpreitreiro + " and dt.DIA_DATA between '"
					+ data1.getYear() + "." + data1.getMonthOfYear() + "." + data1.getDayOfMonth()
					+ "' and '" + data2.getYear() + "." + data2.getMonthOfYear() + "." + data2.getDayOfMonth() + "'"
					+ " and DIAEMP_CODPAGAMENTO = " + codPagamento
					+ " and DIAEMP_CODEMPREGADO in " + codigosEmpregados + ")";	
			sentenca.executeUpdate(sentencaSQL);
			sentenca.close();

		} catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível realizar a atualização!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
		desatualizaPagosDia(data1, data2, codEmpreitreiro, codPagamento);
	}
	
	public void desatualizaPagosDia(DateTime data1, DateTime data2, int codEmpreitreiro, int codPagamento) {
		Statement sentenca;

		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "update DIATRABALHO set DIA_CODPAGAMENTO = NULL where DIA_DATA between '"
					+ data1.getDayOfMonth() + "." + data1.getMonthOfYear() + "." + data1.getYear() 
					+ "' and '" + data2.getDayOfMonth() + "." + data2.getMonthOfYear() + "." + data2.getYear()
					+ "' and DIA_CODPAGAMENTO = " + codPagamento;	
			sentenca.executeUpdate(sentencaSQL);
			sentenca.close();
		} catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível realizar a atualização!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
		
	}

	public void incluir(PagamentoTotalBO pagTotalBO) {
		Statement sentenca;

		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "INSERT INTO pagamentototal ("
					+ " pgtotal_codigo, pgtotal_data, pgtotal_datainicial, pgtotal_datafinal, pgtotal_codempregador, pgtotal_codempreiteiro,"
					+ " pgtotal_valorempreiteiro, pgtotal_qntdempregado, pgtotal_qntdbins, pgtotal_valorempregado, pgtotal_valorbins, pgtotal_valortotal, pgtotal_valordesconto, pgtotal_codadiantamento, pgtotal_historicodesc) " +
					"VALUES ((SELECT COALESCE(MAX(pgtotal_codigo), 0) + 1 FROM pagamentototal ), '" + 
					pagTotalBO.data.getYear() + "." + pagTotalBO.data.getMonthOfYear() + "." + pagTotalBO.data.getDayOfMonth() + "', '" +
					pagTotalBO.dataInicial.getYear() + "." + pagTotalBO.dataInicial.getMonthOfYear() + "." + pagTotalBO.dataInicial.getDayOfMonth() + "', '" +
					pagTotalBO.dataFinal.getYear() + "." + pagTotalBO.dataFinal.getMonthOfYear() + "." + pagTotalBO.dataFinal.getDayOfMonth() + "', " +
					pagTotalBO.dorBO.getCodigo() + ", " +
					pagTotalBO.iroBO.getCodigo() + ", " +
					pagTotalBO.getValorEmpreiteiro() + ", " +
					pagTotalBO.getQntdEmpregado() + ", " +
					pagTotalBO.getQntdBins() + ", " +
					pagTotalBO.getValorEmpregado() + ", " +
					pagTotalBO.getValorBins() + ", " +
					pagTotalBO.getValorTotal() + ", " +
					pagTotalBO.getValorDesconto() + ", " +
					(pagTotalBO.adiantBO.getCodigo() == 0 ? null : pagTotalBO.adiantBO.getCodigo()) + ", '" +
					pagTotalBO.getHistDesconto() + "')";	 	  
			sentenca.executeUpdate(sentencaSQL);
			sentenca.close();
		}
		catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível realizar a inclusão!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	public void alterar(PagamentoTotalBO pagTotalBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "UPDATE pagamentototal SET pgtotal_data = '" +
					pagTotalBO.data.getYear() + "." + pagTotalBO.data.getMonthOfYear() + "." + pagTotalBO.data.getDayOfMonth() + "', pgtotal_codempregador = " +
					pagTotalBO.dorBO.getCodigo() + ", pgtotal_valorempreiteiro = " +
					pagTotalBO.getValorEmpreiteiro() + ", pgtotal_valorempregado = " + 
					pagTotalBO.getValorEmpregado() + ", pgtotal_valorbins = " +
					pagTotalBO.getValorBins() + ", pgtotal_valortotal = " +
					pagTotalBO.getValorTotal() + ", pgtotal_valordesconto =" + 
					pagTotalBO.getValorDesconto() + ", pgtotal_codadiantamento = " +
					(pagTotalBO.adiantBO.getCodigo() == 0 ? null : pagTotalBO.adiantBO.getCodigo()) + ", pgtotal_historicodesc = '" +
					pagTotalBO.getHistDesconto() + "' WHERE pgtotal_codigo = " + pagTotalBO.getCodigo();
			sentenca.executeUpdate(sentencaSQL);
			sentenca.close();
		} catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível realizar a alteração!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	public boolean excluir(int cod) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL ="DELETE FROM pagamentototal WHERE pgtotal_codigo = " + cod; 
			sentenca.executeUpdate(sentencaSQL); 
			sentenca.close();
		} catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível realizar a operação!\n" +
							"Mensagem: Esse registro está sendo referenciado por outra tabela",
							"Erro", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
}
