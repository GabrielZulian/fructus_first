package safristas.dao.safristas;

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
import safristas.bo.safristas.PagamentoUnitarioBO;

public class PagamentoUnitarioDao {
	Connection conexao;

	public PagamentoUnitarioDao() {
		conexao = Conexao.conectaBanco();
	}

	public ArrayList <PagamentoUnitarioBO> consultaPorCodPagamento(int cod)
	{
		ArrayList<PagamentoUnitarioBO> adoBO = consulta("PGUNIT_CODPAGAMENTO =" + cod, "PGUNIT_CODPAGAMENTO");
		return adoBO;
	}

	public ArrayList <PagamentoUnitarioBO> consultaPorPeriodoECod(DateTime data1, DateTime data2, int cod)
	{
		ArrayList<PagamentoUnitarioBO> adoBO = consultaNaInclusao("'" + data1.getDayOfMonth() + "." + data1.getMonthOfYear() + "." + data1.getYear()
				+ "' and '" + data2.getDayOfMonth() + "." + data2.getMonthOfYear() + "." + data2.getYear() + "'", cod);
		return adoBO;
	}

	private ArrayList<PagamentoUnitarioBO> consultaNaInclusao(String periodo, int cod) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();

			// faz a consulta
			registros = sentenca.executeQuery("select EMPREGADO.ADO_CODIGO , EMPREGADO.ADO_NOME, EMPREGADO.ADO_CPF "
					+ ", sum(DIAEMPREGADO.DIAEMP_VALOREMP) as valor_total, sum(iif (DIAEMPREGADO.DIAEMP_PRESENCA = 'S' OR DIAEMPREGADO.DIAEMP_PRESENCA = 'M', 1, 0)) as Qntd_dias" 
					+ ", FUNCAO.FUN_NOME from DIAEMPREGADO "
					+ "inner join DIATRABALHO on DIAEMPREGADO.DIAEMP_CODIGODIA = DIATRABALHO.DIA_CODIGO "
					+ "inner join EMPREGADO on DIAEMPREGADO.DIAEMP_CODEMPREGADO = EMPREGADO.ADO_CODIGO "
					+ "inner join FUNCAO on EMPREGADO.ADO_CODFUNCAO = FUNCAO.FUN_CODIGO "
					+ "inner join EQUIPE on EMPREGADO.ADO_CODEQUIPE = EQUIPE.EQUIPE_CODIGO "
					+ "inner join EMPREITEIRO on EQUIPE.EQUIPE_CODEMPREITEIRO = EMPREITEIRO.IRO_CODIGO "
					+ "where DIATRABALHO.DIA_DATA between " + periodo
					+ " and EMPREITEIRO.IRO_CODIGO = " + cod
					+ " and DIAEMPREGADO.DIAEMP_PAGOU = 'N'"
					+ " group by EMPREGADO.ADO_CODIGO, EMPREGADO.ADO_NOME, EMPREGADO.ADO_CPF,"
					+ " FUNCAO.FUN_NOME order by EMPREGADO.ADO_NOME");

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
			}
			else {
				int i = 0;
				ArrayList<PagamentoUnitarioBO> pagUnitBO = new ArrayList<PagamentoUnitarioBO>();
				do {
					pagUnitBO.add(new PagamentoUnitarioBO());
					pagUnitBO.get(i).adoBO.setCodigo(Integer.parseInt(registros.getString("ado_codigo")));
					try {
						pagUnitBO.get(i).adoBO.setNome(registros.getString("ado_nome"));
						pagUnitBO.get(i).setValor(registros.getFloat("valor_total"));
						pagUnitBO.get(i).setQntdDias(registros.getInt("qntd_dias"));
						pagUnitBO.get(i).adoBO.funcaoBO.setNome(registros.getString("fun_nome"));
						pagUnitBO.get(i).adoBO.setCpf(registros.getString("ado_cpf"));
					} catch (StringVaziaException e) {
					} catch (CpfInvalidoException e) {}
					i++;
				} while (registros.next());
				return pagUnitBO;   
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

	private ArrayList<PagamentoUnitarioBO> consulta(String sentencaSQL, String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();

			// faz a consulta
			registros = sentenca.executeQuery("select a.PGUNIT_CODIGO, a.PGUNIT_CODPAGAMENTO, a.PGUNIT_QNTDDIAS"
					+ ", a.PGUNIT_VALOR, a.PGUNIT_VALORACRESCIMO, a.PGUNIT_DESCONTO, a.PGUNIT_DESCONTOHAT, a.PGUNIT_VALORTOTAL, a.PGUNIT_HISTORICO, a.PGUNIT_CODADIANTAMENTO, EMPREGADO.ADO_CODIGO, EMPREGADO.ADO_NOME"
					+ ", EMPREGADO.ADO_CPF, FUNCAO.FUN_NOME FROM PAGAMENTOUNIT a"
					+ " inner join EMPREGADO on EMPREGADO.ADO_CODIGO = a.PGUNIT_CODEMPREGADO"
					+ " inner join FUNCAO on FUNCAO.FUN_CODIGO = EMPREGADO.ADO_CODFUNCAO where " + sentencaSQL + " order by " + ordem);

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
			}
			else {
				int i = 0;
				ArrayList<PagamentoUnitarioBO> pagUnitBO = new ArrayList<PagamentoUnitarioBO>();
				do {
					pagUnitBO.add(new PagamentoUnitarioBO());
					try {
						pagUnitBO.get(i).setCodigo(Integer.parseInt(registros.getString("PGUNIT_CODIGO")));
						pagUnitBO.get(i).pgtoTotalBO.setCodigo(registros.getInt("PGUNIT_CODPAGAMENTO"));
						pagUnitBO.get(i).setQntdDias(registros.getInt("PGUNIT_QNTDDIAS"));
						pagUnitBO.get(i).setValor(registros.getFloat("PGUNIT_VALOR"));
						pagUnitBO.get(i).setValorAcrescimo(registros.getDouble("PGUNIT_VALORACRESCIMO"));
						pagUnitBO.get(i).setDesconto(registros.getDouble("PGUNIT_DESCONTO"));
						pagUnitBO.get(i).setDescHAT(registros.getDouble("PGUNIT_DESCONTOHAT"));
						pagUnitBO.get(i).setValorTotal(registros.getDouble("PGUNIT_VALORTOTAL"));
						pagUnitBO.get(i).histDesconto.setText(registros.getString("PGUNIT_HISTORICO"));
						pagUnitBO.get(i).adoBO.setCodigo(registros.getInt("ADO_CODIGO"));
						pagUnitBO.get(i).adoBO.setNome(registros.getString("ADO_NOME"));
						pagUnitBO.get(i).adoBO.funcaoBO.setNome(registros.getString("FUN_NOME"));
						pagUnitBO.get(i).adoBO.setCpf(registros.getString("ADO_CPF"));
						pagUnitBO.get(i).adiantBO.setCodigo(registros.getInt("PGUNIT_CODADIANTAMENTO"));
					} catch (StringVaziaException e) {
					} catch (CpfInvalidoException e) {
					} catch (ValorErradoException e) {}
					i++;
				} while (registros.next());
				return pagUnitBO;   
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

	public void incluir(PagamentoUnitarioBO pagUnitBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "INSERT INTO pagamentounit (" +
					"pgunit_codigo, pgunit_codpagamento, pgunit_codempregado, pgunit_qntddias, pgunit_valor, pgunit_valoracrescimo, pgunit_desconto, pgunit_descontohat, pgunit_valortotal, pgunit_historico, pgunit_codadiantamento) " +
					"VALUES ((SELECT COALESCE(MAX(pgunit_codigo), 0) + 1 FROM pagamentounit), " +
					pagUnitBO.pgtoTotalBO.getCodigo() + ", " +
					pagUnitBO.adoBO.getCodigo() + ", " +
					pagUnitBO.getQntdDias() + ", " +
					pagUnitBO.getValor() + ", " +
					pagUnitBO.getValorAcrescimo() + ", " +
					pagUnitBO.getDesconto() + ", " +
					pagUnitBO.getDescHAT() + ", " +
					pagUnitBO.getValorTotal() + ", '" +
					pagUnitBO.histDesconto.getText() + "', " +
					(pagUnitBO.adiantBO.getCodigo() == 0? null : pagUnitBO.adiantBO.getCodigo()) + ")";
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
		return;
	}

	public void alterar(PagamentoUnitarioBO pagUnitBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "UPDATE pagamentounit SET pgunit_codempregado = " +
					pagUnitBO.adoBO.getCodigo() +", pgunit_qntddias = "+
					pagUnitBO.getQntdDias() + ", pgunit_valor = " + 
					pagUnitBO.getValor() + ", pgunit_valoracrescimo = " + 
					pagUnitBO.getValorAcrescimo() + ", pgunit_desconto = " +
					pagUnitBO.getDesconto() + ", pgunit_descontohat = " + 
					pagUnitBO.getDescHAT() + ", pgunit_valortotal = " +
					pagUnitBO.getValorTotal() + ", pgunit_historico = '" + 
					pagUnitBO.histDesconto.getText() + "', pgunit_codadiantamento = " +
					(pagUnitBO.adiantBO.getCodigo() == 0?null:pagUnitBO.adiantBO.getCodigo()) + " WHERE pgunit_codpagamento = " + pagUnitBO.pgtoTotalBO.getCodigo() +
					" AND pgunit_codempregado = " + pagUnitBO.adoBO.getCodigo();

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

	public boolean excluir(int cod) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL ="DELETE FROM pagamentounit WHERE pgunit_codpagamento = " + cod; 
			sentenca.executeUpdate(sentencaSQL); 
			sentenca.close();
		}catch (SQLException eSQL) {
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
