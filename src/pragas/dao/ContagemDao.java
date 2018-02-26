package pragas.dao;

import java.sql.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.joda.time.DateTime;

import exceptions.QuantidadeErradaException;
import gerais.dao.Conexao;
import pragas.bo.ContagemBO;
import pragas.bo.FrascosBO;

public class ContagemDao {
	Connection conexao;

	public ContagemDao() {
		conexao = Conexao.conectaBanco();
	}
	
	public ArrayList <ContagemBO> consultaPorCodigo (int codigo) {
		ArrayList<ContagemBO> contBO = consulta("cont_codigo = " + codigo, "cont_codigo");
		return contBO;
	}
	
	public ArrayList <ContagemBO> consultaPorTipo(char tipo) {
		ArrayList<ContagemBO> contBO = consultaUltimaData("f.fra_tipopraga ='" + tipo + "'", "q.quad_numero");
		return contBO;
	}

	public ArrayList <ContagemBO> consultaPorNumeroETipo(int numero, char tipo) {
		ArrayList<ContagemBO> contBO = consulta("quad_numero = " + numero + " and fra_tipopraga ='" + tipo + "'", "quad_numero, cont_data desc");
		return contBO;
	}
	
	public ArrayList <ContagemBO> consultaPorNumeroeTipoUltimas (int numero, char tipo) {
		ArrayList<ContagemBO> contBO = consultaUltimasAtualizacoes("q.QUAD_NUMERO = " + numero + " and f.fra_tipopraga = '" + tipo + "'");
		return contBO;
	}

	public ArrayList <ContagemBO> consultaPorData (String data) {
		ArrayList<ContagemBO> contBO = consulta("cont_data LIKE '%" + data + "%'", "cont_data, cont_codigo");
		return contBO;
	}
	
	public ArrayList <ContagemBO> consultaPorQntdInsetos (int qntd) {
		ArrayList<ContagemBO> contBO = consulta("cont_qntdinsetos = " + qntd, "cont_qntdinsetos, cont_codigo");
		return contBO;
	}
	
	public ArrayList <ContagemBO> consultaPorNroQuadra (int numero) {
		ArrayList<ContagemBO> contBO = consulta("quad_numero = " + numero, "quad_numero, cont_codigo");
		return contBO;
	}
	
	public ArrayList <ContagemBO> consultaPorEspecie (char especie) {
		ArrayList<ContagemBO> contBO = consulta("frascos.fra_tipopraga = '" + especie + "'", "frascos.fra_tipopraga, contagem.cont_codigo");
		return contBO;
	}

	private ArrayList<ContagemBO> consulta(String sentencaSQL, String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();
			registros = sentenca.executeQuery("SELECT contagem.*, quadra.*, frascos.* FROM contagem"
					+ " INNER JOIN frascos on fra_codquadra = cont_codquadra"
					+ " INNER JOIN quadra on quad_numero = fra_nroquadra"
					+ " WHERE " + sentencaSQL + " Order By " + ordem);

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
				return null;
			}
			else {
				int i = 0;
				ArrayList<ContagemBO> contBO = new ArrayList<ContagemBO>();
				do {
					contBO.add(new ContagemBO());
					contBO.get(i).setCodigo(registros.getInt("cont_codigo"));
					contBO.get(i).data = new DateTime(registros.getTimestamp("cont_data"));
					contBO.get(i).setIndiceFinal(registros.getBigDecimal("cont_indicefinal"));
					contBO.get(i).setQntdDias(registros.getInt("cont_qntddias"));
					try {
						contBO.get(i).setQntdInsetos(registros.getInt("cont_qntdinsetos"));
					} catch (QuantidadeErradaException e) {}
					contBO.get(i).frasco.setCodigo(registros.getInt("fra_codquadra"));
					contBO.get(i).frasco.setTipoPraga(registros.getString("fra_tipopraga").charAt(0));
					contBO.get(i).frasco.quadra.setNumero(registros.getInt("quad_numero"));
					contBO.get(i).frasco.setQntdFrascos(registros.getInt("fra_qntdfrascos"));
					i++;
				} while (registros.next());
				sentenca.close();
				return contBO;   
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
	
	private ArrayList<ContagemBO> consultaUltimaData(String sentencaSQL, String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();
			registros = sentenca.executeQuery("select c.CONT_INDICEFINAL, c.CONT_DATA, q.QUAD_NUMERO, c.CONT_CODQUADRA from contagem c"
					+ " inner join (SELECT FRA_NROQUADRA, FRA_CODQUADRA, max(CONT_DATA) as cont_data FROM contagem inner join FRASCOS"
					+ " on CONT_CODQUADRA = FRA_CODQUADRA group by FRA_NROQUADRA, FRA_CODQUADRA) as c1"
					+ " on c.CONT_CODQUADRA = c1.FRA_CODQUADRA and c.CONT_DATA = c1.cont_data"
					+ " inner join FRASCOS f on f.FRA_CODQUADRA = c.CONT_CODQUADRA "
					+ " inner join QUADRA q on q.QUAD_NUMERO = f.FRA_NROQUADRA "
					+ "where " + sentencaSQL + " Order By " + ordem);

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
				return null;
			}
			else {
				int i = 0;
				ArrayList<ContagemBO> contBO = new ArrayList<ContagemBO>();
				do {
					contBO.add(new ContagemBO());
					contBO.get(i).data = new DateTime(registros.getTimestamp("cont_data"));
					contBO.get(i).setIndiceFinal(registros.getBigDecimal("cont_indicefinal"));
					contBO.get(i).frasco.quadra.setNumero(registros.getInt("quad_numero"));
					i++;
				} while (registros.next());
				sentenca.close();
				return contBO;   
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
	
	private ArrayList<ContagemBO> consultaUltimasAtualizacoes(String sentencaSQL) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();
			registros = sentenca.executeQuery("select first 3 c.CONT_DATA, c.CONT_QNTDINSETOS, q.QUAD_NUMERO, f.FRA_CODQUADRA, f.FRA_QNTDFRASCOS,"
					+ "c.cont_indicefinal from CONTAGEM c inner join FRASCOS f on f.FRA_CODQUADRA = c.CONT_CODQUADRA "
					+ "inner join QUADRA q on q.QUAD_NUMERO = f.FRA_NROQUADRA "
					+ "where " + sentencaSQL + " order by c.CONT_DATA desc");

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
				return null;
			}
			else {
				int i = 0;
				ArrayList<ContagemBO> contBO = new ArrayList<ContagemBO>();
				do {
					contBO.add(new ContagemBO());
					contBO.get(i).data = new DateTime(registros.getTimestamp("cont_data"));
					try {
						contBO.get(i).setQntdInsetos(registros.getInt("cont_qntdinsetos"));
					} catch (QuantidadeErradaException e) {}
					contBO.get(i).frasco.quadra.setNumero(registros.getInt("quad_numero"));
					contBO.get(i).frasco.setCodigo(registros.getInt("fra_codquadra"));
					contBO.get(i).frasco.quadra.setNumero(registros.getInt("fra_qntdfrascos"));
					contBO.get(i).setIndiceFinal(registros.getBigDecimal("cont_indicefinal"));
					i++;
				} while (registros.next());
				sentenca.close();
				return contBO;   
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
	
	public DateTime getDataUltimaContagem(int nroQuadra, char tipopraga) {
		Statement sentenca;
		ResultSet registros;
		DateTime data = new DateTime();
		try {
			sentenca = conexao.createStatement();
			registros = sentenca.executeQuery("SELECT MAX(cont_data) as cont_data FROM contagem"
					+ " INNER JOIN frascos on fra_codquadra = cont_codquadra WHERE fra_nroquadra = " + nroQuadra + " and fra_tipopraga = '" + tipopraga + "'");
			if(registros.next()) {
				if (registros.getTimestamp("cont_data") == null)
					data = null;
				else
					data = new DateTime(registros.getTimestamp("cont_data"));
			} else 
				JOptionPane.showMessageDialog(null, "Nenhum registro foi encontrado!", "Mensagem", JOptionPane.WARNING_MESSAGE);

		} catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null, "Não foi possível carregar os dados!\n" +"Mensagem: " + eSQL.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
		return data;
	}
	
	public DateTime getDataUltimaContagem(int codQuadra) {
		Statement sentenca;
		ResultSet registros;
		DateTime data = new DateTime();
		try {
			sentenca = conexao.createStatement();
			registros = sentenca.executeQuery("SELECT MAX(cont_data) as cont_data FROM contagem"
					+ " INNER JOIN frascos on fra_codquadra = cont_codquadra WHERE fra_codquadra = " + codQuadra);
			if(registros.next()) {
				if (registros.getTimestamp("cont_data") == null)
					data = null;
				else
					data = new DateTime(registros.getTimestamp("cont_data"));
			} else 
				JOptionPane.showMessageDialog(null, "Nenhum registro foi encontrado!", "Mensagem", JOptionPane.WARNING_MESSAGE);

		} catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null, "Não foi possível carregar os dados!\n" +"Mensagem: " + eSQL.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
		return data;
	}
	
	public int getUltimoCod () {
		Statement sentenca;
		ResultSet registros;
		try {
			sentenca = conexao.createStatement();
			registros = sentenca.executeQuery("SELECT MAX(cont_codigo) FROM contagem");
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

	public void incluir(ContagemBO contBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "INSERT INTO contagem (cont_codigo, cont_data, cont_codquadra, cont_qntdinsetos, cont_qntddias, cont_indicefinal) " +
					"VALUES ((SELECT COALESCE(MAX(cont_codigo), 0) + 1 FROM contagem), '" +
					contBO.data.getYear() + "." + contBO.data.getMonthOfYear() + "." + contBO.data.getDayOfMonth() + "', " +
					contBO.frasco.getCodigo() + ", " +
					contBO.getQntdInsetos() + ", " +
					contBO.getQntdDias() + ", " +
					contBO.getIndiceFinal() + ")";
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

//	public void alterar(ContagemBO contBO) {
//		Statement sentenca;
//		try {
//			sentenca = conexao.createStatement();
//			String sentencaSQL = null;
//			sentencaSQL = "UPDATE contagem SET cont_data = '" +
//					contBO.data.getYear() + "." + contBO.data.getMonthOfYear() + "." + contBO.data.getDayOfMonth() +"', cont_qntdinsetos = " + 
//					contBO.getQntdInsetos() + ", cont_qntddias = " +
//					contBO.getQntdDias() + ", cont_indicefinal = " +
//					contBO.getIndiceFinal() + " WHERE cont_codigo = " + contBO.getCodigo(); 
//
//			sentenca.executeUpdate(sentencaSQL); 
//			sentenca.close();
//		}
//		catch (SQLException eSQL) {
//			eSQL.printStackTrace();
//			JOptionPane.showMessageDialog(null,
//					"Não foi possível realizar a inclusão!\n" +
//							"Mensagem: " + eSQL.getMessage(),
//							"Erro", JOptionPane.ERROR_MESSAGE);
//		}
//	}

	public boolean excluir(int cod) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL ="DELETE FROM contagem WHERE cont_codigo = " + cod; 
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
