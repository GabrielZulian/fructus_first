package safristas.dao.outros;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.joda.time.DateTime;

import exceptions.CpfInvalidoException;
import exceptions.StringVaziaException;
import exceptions.ValorErradoException;
import gerais.dao.Conexao;
import safristas.bo.outros.PagamentoUnitarioOutrosBO;

public class PagamentoUnitarioOutrosDao {
	Connection conexao;

	public PagamentoUnitarioOutrosDao() {
		conexao = Conexao.conectaBanco();
	}

	public ArrayList <PagamentoUnitarioOutrosBO> consultaPorCodPagamento(int cod)
	{
		ArrayList<PagamentoUnitarioOutrosBO> pgtoUnitOutBO = consulta("PGUNITOUT_CODPAGAMENTO =" + cod, "PGUNITOUT_CODPAGAMENTO");
		return pgtoUnitOutBO;
	}

	public ArrayList <PagamentoUnitarioOutrosBO> consultaPorPeriodo(DateTime data1, DateTime data2)
	{

		ArrayList<PagamentoUnitarioOutrosBO> pgtoUnitOutBO = consultaNaInclusao("'" + data1.getYear() + "." + data1.getMonthOfYear() + "." + data1.getDayOfMonth() 
				+ "' and '" + data2.getYear() + "." + data2.getMonthOfYear() + "." + data2.getDayOfMonth() + "'");
		return pgtoUnitOutBO;
	}

	private ArrayList<PagamentoUnitarioOutrosBO> consultaNaInclusao(String periodo) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();

			// faz a consulta
			registros = sentenca.executeQuery("select EMPREGADO.ADO_CODIGO , EMPREGADO.ADO_NOME , EMPREGADO.ADO_CPF , FUNCAO.FUN_NOME,"
							+ " count(*) as QNTD_DIAS , SUM(DIAOUTROS.DIAOUT_VALORTOTAL) as VALOR_TOTAL, DIAOUT_CODVEICULO from DIAOUTROS"
							+ " inner join EMPREGADO  on EMPREGADO.ADO_CODIGO = DIAOUTROS.DIAOUT_CODEMPREGADO"
							+ " inner join FUNCAO on FUNCAO.FUN_CODIGO = EMPREGADO.ADO_CODFUNCAO"
							+ " where DIAOUTROS.DIAOUT_PAGOU = 'N' and DIAOUTROS.DIAOUT_DATA between " + periodo
							+ " group by EMPREGADO.ADO_CODIGO, EMPREGADO.ADO_NOME, EMPREGADO.ADO_CPF, FUNCAO.FUN_NOME, DIAOUT_CODVEICULO"
							+ " order by EMPREGADO.ADO_NOME");

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
			} else {
				int i = 0;
				ArrayList<PagamentoUnitarioOutrosBO> pgtoUnitOutBO = new ArrayList<PagamentoUnitarioOutrosBO>();
				do {
					pgtoUnitOutBO.add(new PagamentoUnitarioOutrosBO());
					try {
						pgtoUnitOutBO.get(i).diaoutBO.adoBO.setCodigo(Integer.parseInt(registros.getString("ADO_CODIGO")));
						pgtoUnitOutBO.get(i).diaoutBO.adoBO.setNome(registros.getString("ADO_NOME"));
						pgtoUnitOutBO.get(i).diaoutBO.adoBO.setCpf(registros.getString("ADO_CPF"));
						pgtoUnitOutBO.get(i).diaoutBO.adoBO.funcaoBO.setNome(registros.getString("FUN_NOME"));
						pgtoUnitOutBO.get(i).setQntdDias(registros.getInt("QNTD_DIAS"));
						pgtoUnitOutBO.get(i).setValor(registros.getFloat("VALOR_TOTAL"));
						pgtoUnitOutBO.get(i).diaoutBO.veicBO.setCodigo(registros.getInt("DIAOUT_CODVEICULO"));
					} catch (StringVaziaException e) {
					} catch (CpfInvalidoException e) {}
					i++;
				} while (registros.next());
				return pgtoUnitOutBO;   
			}
			sentenca.close();
		} catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível carregar os dados!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	private ArrayList<PagamentoUnitarioOutrosBO> consulta(String sentencaSQL, String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();

			// faz a consulta
			registros = sentenca.executeQuery("select a.PGUNITOUT_CODIGO, a.PGUNITOUT_CODPAGAMENTO, a.PGUNITOUT_QNTDDIAS"
					+ ", a.PGUNITOUT_VALOR, a.PGUNITOUT_DESCONTO, a.PGUNITOUT_VALORTOTAL, a.PGUNITOUT_HISTORICO, a.PGUNITOUT_CODADIANTAMENTO, EMPREGADO.ADO_CODIGO, EMPREGADO.ADO_NOME"
					+ ", EMPREGADO.ADO_CPF, FUNCAO.FUN_NOME FROM PAGAMENTOUNITOUTROS a"
					+ " inner join EMPREGADO on EMPREGADO.ADO_CODIGO = a.PGUNITOUT_CODEMPREGADO"
					+ " inner join FUNCAO on FUNCAO.FUN_CODIGO = EMPREGADO.ADO_CODFUNCAO where " + sentencaSQL + " order by " + ordem);

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
			}
			else {
				int i = 0;
				ArrayList<PagamentoUnitarioOutrosBO> pagUnitOutBO = new ArrayList<PagamentoUnitarioOutrosBO>();
				do {
					pagUnitOutBO.add(new PagamentoUnitarioOutrosBO());
					try {
						pagUnitOutBO.get(i).setCodigo(Integer.parseInt(registros.getString("PGUNITOUT_CODIGO")));
						pagUnitOutBO.get(i).pgtoTotalOutBO.setCodigo(registros.getInt("PGUNITOUT_CODPAGAMENTO"));
						pagUnitOutBO.get(i).setQntdDias(registros.getInt("PGUNITOUT_QNTDDIAS"));
						pagUnitOutBO.get(i).setValor(registros.getFloat("PGUNITOUT_VALOR"));
						pagUnitOutBO.get(i).setDesconto(registros.getDouble("PGUNITOUT_DESCONTO"));
						pagUnitOutBO.get(i).setValorTotal(registros.getDouble("PGUNITOUT_VALORTOTAL"));
						pagUnitOutBO.get(i).histDesconto.setText(registros.getString("PGUNITOUT_HISTORICO"));
						pagUnitOutBO.get(i).diaoutBO.adoBO.setCodigo(registros.getInt("ADO_CODIGO"));
						pagUnitOutBO.get(i).diaoutBO.adoBO.setNome(registros.getString("ADO_NOME"));
						pagUnitOutBO.get(i).diaoutBO.adoBO.funcaoBO.setNome(registros.getString("FUN_NOME"));
						pagUnitOutBO.get(i).diaoutBO.adoBO.setCpf(registros.getString("ADO_CPF"));
						pagUnitOutBO.get(i).adiantBO.setCodigo(registros.getInt("PGUNITOUT_CODADIANTAMENTO"));
					} catch (StringVaziaException e) {
					} catch (CpfInvalidoException e) {
					} catch (ValorErradoException e) {}
					i++;
				} while (registros.next());
				return pagUnitOutBO;   
			}
			sentenca.close();
		} catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível carregar os dados!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	public void incluir(PagamentoUnitarioOutrosBO pagUnitOutBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "INSERT INTO pagamentounitoutros (" +
					"pgunitout_codigo, pgunitout_codpagamento, pgunitout_codempregado, pgunitout_qntddias, pgunitout_valor, pgunitout_desconto, pgunitout_valortotal, pgunitout_historico, pgunitout_codadiantamento) " +
					"VALUES ((SELECT COALESCE(MAX(pgunitout_codigo), 0) + 1 FROM pagamentounitoutros), " +
					pagUnitOutBO.pgtoTotalOutBO.getCodigo() + ", " +
					pagUnitOutBO.diaoutBO.adoBO.getCodigo() + ", " +
					pagUnitOutBO.getQntdDias() + ", " +
					pagUnitOutBO.getValor() + ", " +
					pagUnitOutBO.getDesconto() +", " +
					pagUnitOutBO.getValorTotal() + ", '" +
					pagUnitOutBO.histDesconto.getText() + "', " +
					(pagUnitOutBO.adiantBO.getCodigo() == 0? null : pagUnitOutBO.adiantBO.getCodigo()) + ")";				 	  
			sentenca.executeUpdate(sentencaSQL); 
			sentenca.close();
		} catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível realizar a inclusão!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
		return;
	}

	public void alterar(PagamentoUnitarioOutrosBO pagUnitOutBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "UPDATE pagamentounitoutros SET pgunitout_codempregado = " +
					pagUnitOutBO.diaoutBO.adoBO.getCodigo() +", pgunitout_qntddias = " +
					pagUnitOutBO.getQntdDias() + ", pgunitout_valor = " + 
					pagUnitOutBO.getValor() + ", pgunitout_desconto = " +
					pagUnitOutBO.getDesconto() + ", pgunitout_valortotal = " +
					pagUnitOutBO.getValorTotal() + ", pgunitout_historico = '" + 
					pagUnitOutBO.histDesconto.getText() + "', pgunitout_codadiantamento = " +
					(pagUnitOutBO.adiantBO.getCodigo() == 0 ? null : pagUnitOutBO.adiantBO.getCodigo()) + " WHERE pgunitout_codigo = " + pagUnitOutBO.pgtoTotalOutBO.getCodigo() +
					" AND pgunitout_codempregado = " + pagUnitOutBO.diaoutBO.adoBO.getCodigo();	 

			sentenca.executeUpdate(sentencaSQL); 
			sentenca.close();
		} catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,"Não foi possível realizar a alteração!\n Mensagem: " + eSQL.getMessage(),
			"Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	public boolean excluir(int cod) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL ="DELETE FROM pagamentounitoutros WHERE pgunitout_codpagamento = " + cod; 
			sentenca.executeUpdate(sentencaSQL); 
			sentenca.close();
		}catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível realizar a operação!\n Mensagem: Esse registro está sendo referenciado por outra tabela",
							"Erro", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	} 
}
