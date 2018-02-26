package safristas.dao.outros;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.joda.time.DateTime;

import exceptions.QuantidadeErradaException;
import exceptions.StringVaziaException;
import gerais.dao.Conexao;
import safristas.bo.outros.PagamentoTotalOutrosBO;

public class PagamentoTotalOutrosDao {
	Connection conexao;

	public PagamentoTotalOutrosDao() {
		conexao = Conexao.conectaBanco();
	}

	public ArrayList <PagamentoTotalOutrosBO> consultaPorCodigo(int cod)
	{
		ArrayList<PagamentoTotalOutrosBO> pgtoTotalOutBO = consulta("pgtotalout_codigo = " + cod, "pgtotalout_codigo");
		return pgtoTotalOutBO;
	}
	
	public ArrayList <PagamentoTotalOutrosBO> consultaPorData(String data)
	{
		ArrayList<PagamentoTotalOutrosBO> pgtoTotalOutBO = consulta("pgtotalout_data LIKE '%" + data + "%'", "pgtotalout_data, pgtotalout_codigo");
		return pgtoTotalOutBO;
	}

	public ArrayList <PagamentoTotalOutrosBO> consultaPorDataInicial(String data)
	{
		ArrayList<PagamentoTotalOutrosBO> pgtoTotalOutBO = consulta("pgtotalout_datainicial LIKE '%" + data + "%'", "pgtotalout_datainicial");
		return pgtoTotalOutBO;
	}

	public ArrayList <PagamentoTotalOutrosBO> consultaPorDataFinal(String data)
	{
		ArrayList<PagamentoTotalOutrosBO> pgtoTotalOutBO = consulta("pgtotalout_datafinal LIKE '%" + data + "%'", "pgtotalout_datafinal");
		return pgtoTotalOutBO;
	}

	public ArrayList <PagamentoTotalOutrosBO> consultaPorValorTotal(double valor)
	{
		ArrayList<PagamentoTotalOutrosBO> pgtoTotalOutBO = consulta("pgtotalout_valortotal = " + valor, "pgtotalout_valortotal");
		return pgtoTotalOutBO;
	}

	public ArrayList <PagamentoTotalOutrosBO> consultaPorNomeEmpregador(String nome)
	{
		ArrayList<PagamentoTotalOutrosBO> pgtoTotalOutBO = consulta("EMPREGADOR.DOR_NOME containing '" + nome + "'", "EMPREGADOR.DOR_NOME, pgtotalout_data");
		return pgtoTotalOutBO;
	}

	public ArrayList <PagamentoTotalOutrosBO> consultaPorCodEmpregador(int cod)
	{
		ArrayList<PagamentoTotalOutrosBO> pgtoTotalOutBO = consulta("pgtotalout_codempregador = " + cod, "pgtotalout_codempregador, pgtotalout_data, pgtotalout_codigo");
		return pgtoTotalOutBO;
	}

	private ArrayList<PagamentoTotalOutrosBO> consulta(String sentencaSQL, String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();

			// faz a consulta
			registros = sentenca.executeQuery("select PAGAMENTOTOTALOUTROS.PGTOTALOUT_CODIGO, PAGAMENTOTOTALOUTROS.PGTOTALOUT_DATA,"
					+ " PAGAMENTOTOTALOUTROS.PGTOTALOUT_DATAINICIAL, PAGAMENTOTOTALOUTROS.PGTOTALOUT_DATAFINAL,"
					+ " PAGAMENTOTOTALOUTROS.PGTOTALOUT_CODEMPREGADOR, EMPREGADOR.DOR_NOME, PAGAMENTOTOTALOUTROS.PGTOTALOUT_QNTDEMPREGADO,"
					+ " PAGAMENTOTOTALOUTROS.PGTOTALOUT_VALORTOTAL from PAGAMENTOTOTALOUTROS"
					+ " inner join EMPREGADOR on EMPREGADOR.DOR_CODIGO = PAGAMENTOTOTALOUTROS.PGTOTALOUT_CODEMPREGADOR"
					+ " where " + sentencaSQL + " order by " + ordem);
			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
			}
			else {
				int i = 0;
				ArrayList<PagamentoTotalOutrosBO> pagTotalOutBO = new ArrayList<PagamentoTotalOutrosBO>();
				do {
					pagTotalOutBO.add(new PagamentoTotalOutrosBO());
					try {
						pagTotalOutBO.get(i).setCodigo(Integer.parseInt(registros.getString("PGTOTALOUT_CODIGO")));
						pagTotalOutBO.get(i).data = new DateTime(registros.getDate("PGTOTALOUT_DATA"));
						pagTotalOutBO.get(i).dataInicial = new DateTime(registros.getDate("PGTOTALOUT_DATAINICIAL"));
						pagTotalOutBO.get(i).dataFinal = new DateTime(registros.getDate("PGTOTALOUT_DATAFINAL"));
						pagTotalOutBO.get(i).dorBO.setCodigo(registros.getInt("PGTOTALOUT_CODEMPREGADOR"));
						pagTotalOutBO.get(i).dorBO.setNome(registros.getString("DOR_NOME"));
						pagTotalOutBO.get(i).setQntdEmpregado(registros.getInt("PGTOTALOUT_QNTDEMPREGADO"));
						pagTotalOutBO.get(i).setValorTotal(registros.getDouble("PGTOTALOUT_VALORTOTAL"));

					} catch (StringVaziaException e) {
					} catch (QuantidadeErradaException e) {}
					i++;
				} while (registros.next());
				return pagTotalOutBO;   
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
			registros = sentenca.executeQuery("SELECT MAX(pgtotalout_codigo) FROM pagamentototaloutros");
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

	public void atualizaPagos (DateTime data1, DateTime data2, int codPagamento, String codigosEmpregados) {
		Statement sentenca;

		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "update DIAOUTROS set DIAOUT_PAGOU = 'S', DIAOUT_CODPAGAMENTO = " + codPagamento + " where DIAOUT_DATA between '"
					+ data1.getDayOfMonth() + "." + data1.getMonthOfYear() + "." + data1.getYear() 
					+ "' and '" + data2.getDayOfMonth() + "." + data2.getMonthOfYear() + "." + data2.getYear()
					+ "' and DIAOUT_CODPAGAMENTO IS NULL and DIAOUT_CODEMPREGADO in (" + codigosEmpregados + ")";	
			sentenca.executeUpdate(sentencaSQL);
			sentenca.close();
		} catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível realizar a inclusão!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void desatualizaPagos (DateTime data1, DateTime data2, int codPagamento) {
		Statement sentenca;

		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "update DIAOUTROS set DIAOUT_PAGOU = 'N', DIAOUT_CODPAGAMENTO = NULL  where DIAOUT_DATA between '"
					+ data1.getDayOfMonth() + "." + data1.getMonthOfYear() + "." + data1.getYear() 
					+ "' and '" + data2.getDayOfMonth() + "." + data2.getMonthOfYear() + "." + data2.getYear() + "'"
					+ " and DIAOUT_CODPAGAMENTO = " + codPagamento;	
			sentenca.executeUpdate(sentencaSQL);
			sentenca.close();
		} catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível realizar a inclusão!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void incluir(PagamentoTotalOutrosBO pagTotalOutBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "INSERT INTO pagamentototaloutros (" +
					"pgtotalout_codigo, pgtotalout_data, pgtotalout_datainicial, pgtotalout_datafinal, pgtotalout_codempregador, pgtotalout_qntdempregado, pgtotalout_valortotal) " +
					"VALUES ((SELECT COALESCE(MAX(pgtotalout_codigo), 0) + 1 FROM pagamentototaloutros), '" + 
					pagTotalOutBO.data.getYear() + "." + pagTotalOutBO.data.getMonthOfYear() + "." + pagTotalOutBO.data.getDayOfMonth() + "', '" +
					pagTotalOutBO.dataInicial.getYear() + "." + pagTotalOutBO.dataInicial.getMonthOfYear() + "." + pagTotalOutBO.dataInicial.getDayOfMonth() + "', '" +
					pagTotalOutBO.dataFinal.getYear() + "." + pagTotalOutBO.dataFinal.getMonthOfYear() + "." + pagTotalOutBO.dataFinal.getDayOfMonth() + "', " +
					pagTotalOutBO.dorBO.getCodigo() + ", " +
					pagTotalOutBO.getQntdEmpregado() + ", " +
					pagTotalOutBO.getValorTotal() + ")"; 	  
			sentenca.executeUpdate(sentencaSQL);
			sentenca.close();
		}
		catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível realizar a inclusão!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void alterar(PagamentoTotalOutrosBO pagTotalOutBO) {
		Statement sentenca;

		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "UPDATE pagamentototaloutros SET pgtotalout_data = '" +
					pagTotalOutBO.data.getYear() + "." + pagTotalOutBO.data.getMonthOfYear() + "." + pagTotalOutBO.data.getDayOfMonth() + "', pgtotalout_codempregador = " +
					pagTotalOutBO.dorBO.getCodigo() + ", pgtotalout_valortotal = " +
					pagTotalOutBO.getValorTotal() + " WHERE pgtotalout_codigo = " + pagTotalOutBO.getCodigo(); 

			sentenca.executeUpdate(sentencaSQL); 
			sentenca.close();
		} catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível realizar a alteração!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	public boolean excluir(int cod) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL ="DELETE FROM pagamentototaloutros WHERE pgtotalout_codigo = " + cod; 
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
