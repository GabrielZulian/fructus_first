package insumos.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.joda.time.DateTime;

import exceptions.CodigoErradoException;
import exceptions.StringVaziaException;
import gerais.dao.Conexao;
import insumos.bo.AplicacaoBO;

public class AplicacaoDao {
	Connection conexao;

	public AplicacaoDao() {
		conexao = Conexao.conectaBanco();
	}

	public ArrayList <AplicacaoBO> consultaPorCodigo(int cod) {
		ArrayList<AplicacaoBO> aplicacaoBO = consulta("apl_codigo = " + cod, "apl_codigo");
		return aplicacaoBO;
	}
	
	public ArrayList <AplicacaoBO> consultaPorNroTalhao(int cod) {
		ArrayList<AplicacaoBO> aplicacaoBO = consulta("tal_numero = " + cod, "tal_numero");
		return aplicacaoBO;
	}

	public ArrayList <AplicacaoBO> consultaPorHistorico(String string) {
		ArrayList<AplicacaoBO> aplicacaoBO = consulta("apl_historico containing '" + string + "'", "apl_historico, apl_data");
		return aplicacaoBO;
	}

	public ArrayList <AplicacaoBO> consultaPorData(String data) {
		ArrayList<AplicacaoBO> aplicacaoBO = consulta("apl_data LIKE '%" +
				data + "%'", "apl_data, tal_numero");
		return aplicacaoBO;
	}
	
	public ArrayList<AplicacaoBO> consultaPorTipoTalhao(String tipo) {
		ArrayList<AplicacaoBO> aplicacaoBO = consulta("tal_tipo LIKE '%" +
				tipo + "%'", "tal_tipo, apl_data");
		return aplicacaoBO;
	}
	
	public ArrayList<AplicacaoBO> consultaPorInsumo(String insumo) {
		ArrayList<AplicacaoBO> aplicacaoBO = consulta("ins_descricao LIKE '%" +
				insumo + "%'", "ins_descricao, apl_data");
		return aplicacaoBO;
	}

	public ArrayList <AplicacaoBO> consultaGeralPorNro() {
		ArrayList<AplicacaoBO> aplicacaoBO = consultaGeral("tal_numero");
		return aplicacaoBO;
	}

	public ArrayList <AplicacaoBO> consultaGeralPorDataApl() {
		ArrayList<AplicacaoBO> aplicacaoBO = consultaGeral("data_aplicacao");
		return aplicacaoBO;
	}

	public ArrayList <AplicacaoBO> consultaGeralPorDataFinal() {
		ArrayList<AplicacaoBO> aplicacaoBO = consultaGeral("data_final");
		return aplicacaoBO;
	}

	public ArrayList <AplicacaoBO> consultaGeralPorInsumo() {
		ArrayList<AplicacaoBO> aplicacaoBO = consultaGeral("ins_descricao");
		return aplicacaoBO;
	}

	private ArrayList<AplicacaoBO> consultaGeral(String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();

			// faz a consulta
			registros = sentenca.executeQuery("SELECT max(APL_DATA) as data_aplicacao, tal_numero, tal_tipo, ins_descricao"
					+ ", ins_diasresidual, dateadd(day, ins_diasresidual, max(apl_data)) as data_final FROM APLICACAO "
					+ "INNER JOIN INSUMO ON ins_codigo = apl_codinsumo "
					+ "INNER JOIN TALHAO ON apl_codtalhao = tal_codigo "
					+ "group by tal_numero, tal_tipo, ins_descricao, ins_diasresidual "
					+ "order by " + ordem);

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
				return null;
			}
			else {
				int i = 0;
				ArrayList<AplicacaoBO> aplicacaoBO = new ArrayList<AplicacaoBO>();
				do {
					aplicacaoBO.add(new AplicacaoBO());
					aplicacaoBO.get(i).setData(new DateTime(registros.getDate("data_aplicacao")));
					aplicacaoBO.get(i).talhaoBO.setNumero(registros.getInt("tal_numero"));
					aplicacaoBO.get(i).talhaoBO.setTipo(registros.getString("tal_tipo"));
					aplicacaoBO.get(i).insumoBO.setDiasResidual(registros.getInt("ins_diasresidual"));
					try {
						aplicacaoBO.get(i).insumoBO.setDescricao(registros.getString("ins_descricao"));
					} catch (StringVaziaException e) {}
					aplicacaoBO.get(i).setDataFinal(new DateTime(registros.getDate("data_final")));
					i++;
				} while (registros.next());
				sentenca.close();
				return aplicacaoBO;
			}
		}catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível carregar os dados!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}


	private ArrayList<AplicacaoBO> consulta(String sentencaSQL, String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();

			// faz a consulta
			registros = sentenca.executeQuery("SELECT aplicacao.*, talhao.tal_codigo, talhao.tal_numero, talhao.tal_tipo, insumo.ins_codigo, "
					+ "ins_descricao, ins_unidade FROM aplicacao INNER JOIN talhao ON aplicacao.apl_codtalhao = talhao.tal_codigo"
					+ " INNER JOIN insumo ON insumo.ins_codigo = apl_codinsumo WHERE " + sentencaSQL + " Order By " + ordem);

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
				return null;
			}
			else {
				int i = 0;
				ArrayList<AplicacaoBO> aplicacaoBO = new ArrayList<AplicacaoBO>();
				do {
					aplicacaoBO.add(new AplicacaoBO());
					try {
						aplicacaoBO.get(i).setCodigo(registros.getInt("apl_codigo"));
						aplicacaoBO.get(i).setData(new DateTime(registros.getDate("apl_data")));
						aplicacaoBO.get(i).insumoBO.setCodigo(registros.getInt("apl_codinsumo"));
						aplicacaoBO.get(i).insumoBO.setDescricao(registros.getString("ins_descricao"));
						aplicacaoBO.get(i).insumoBO.setUnidade(registros.getString("ins_unidade"));
						aplicacaoBO.get(i).talhaoBO.setCodigo(registros.getInt("tal_codigo"));
						aplicacaoBO.get(i).talhaoBO.setNumero(registros.getInt("tal_numero"));
						aplicacaoBO.get(i).talhaoBO.setTipo(registros.getString("tal_tipo"));
						aplicacaoBO.get(i).setQuantidade(registros.getDouble("apl_quantidade"));
						aplicacaoBO.get(i).setHistorico(registros.getString("apl_historico"));
					} catch (StringVaziaException e) {
					} catch (CodigoErradoException e) {}
					i++;
				} while (registros.next());
				sentenca.close();
				return aplicacaoBO;   
			}
		}catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível carregar os dados!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	public void incluir(AplicacaoBO aplicacaoBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "INSERT INTO aplicacao (" +
					"apl_codigo, apl_data, apl_codinsumo, apl_codtalhao, apl_quantidade, apl_historico) " +
					"VALUES ((SELECT COALESCE(MAX(apl_codigo), 0) + 1 FROM aplicacao), '" +
					aplicacaoBO.getData().getYear() + "." +
					aplicacaoBO.getData().getMonthOfYear() + "." +
					aplicacaoBO.getData().getDayOfMonth() + "', " +
					aplicacaoBO.insumoBO.getCodigo() + ", " +
					aplicacaoBO.talhaoBO.getCodigo() + ", " +
					aplicacaoBO.getQuantidade() + ", '" +
					aplicacaoBO.getHistorico() + "')";
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

	public void alterar(AplicacaoBO aplicacaoBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "UPDATE aplicacao SET apl_data = '" +
					aplicacaoBO.getData().getYear() + "." +
					aplicacaoBO.getData().getMonthOfYear() + "." +
					aplicacaoBO.getData().getDayOfMonth() + "', apl_codinsumo = " +
					aplicacaoBO.insumoBO.getCodigo() + ", apl_codtalhao = " +
					aplicacaoBO.talhaoBO.getCodigo() + ", apl_quantidade = " +
					aplicacaoBO.getQuantidade() + ", apl_historico = '" +
					aplicacaoBO.getHistorico() + "' WHERE apl_codigo = " + aplicacaoBO.getCodigo(); 

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

	public boolean excluir(int codigo) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = "DELETE FROM aplicacao WHERE apl_codigo = " + codigo;
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