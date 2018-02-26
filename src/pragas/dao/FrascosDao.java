package pragas.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import gerais.bo.QuadraBO;
import gerais.dao.Conexao;
import pragas.bo.FrascosBO;

public class FrascosDao {
	Connection conexao;

	public FrascosDao() {
		conexao = Conexao.conectaBanco();
	}

	public ArrayList <FrascosBO> consultaPorNumero(int numero) {
		ArrayList<FrascosBO> frascoBO = consulta("fra_nroquadra = " + numero, "fra_nroquadra");
		return frascoBO;
	}

	public ArrayList <FrascosBO> consultaPorNumeroETipo(int numero, char tipo) {
		ArrayList<FrascosBO> frascoBO = consulta("fra_nroquadra = " + numero + " and fra_tipopraga = '" + tipo + "'", "fra_nroquadra");
		return frascoBO;
	}

	private ArrayList<FrascosBO> consulta(String sentencaSQL, String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();

			registros = sentenca.executeQuery("SELECT f.fra_codquadra, f.fra_nroquadra, f.fra_tipopraga, f.fra_qntdfrascos FROM frascos f"
					+ " WHERE " + sentencaSQL + " ORDER BY " + ordem);

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
			} else {
				int i = 0;
				ArrayList<FrascosBO> frascoBO = new ArrayList<FrascosBO>();
				do {
					frascoBO.add(new FrascosBO());
					frascoBO.get(i).setCodigo(registros.getInt("fra_codquadra"));
					frascoBO.get(i).setQntdFrascos(registros.getInt("fra_qntdfrascos"));
					frascoBO.get(i).quadra.setNumero(registros.getInt("fra_nroquadra"));
					frascoBO.get(i).setTipoPraga(registros.getString("fra_tipopraga").charAt(0));
					i++;
				} while (registros.next());
				return frascoBO;   
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

	public void incluir(QuadraBO quadBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "INSERT INTO quadra (" +
					"quad_numero, quad_area, quad_anoplantio, quad_nroplantas) "+
					"VALUES ("+ 
					quadBO.getNumero() + ", "+ 
					quadBO.getArea() + ", " +
					quadBO.getAnoPlantio() + ", " +
					quadBO.getNroPlantasHectare() + ")";				 	  
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
	
	public void alterar(QuadraBO quadBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "UPDATE quadra SET quad_numero = " +
					quadBO.getNumero() +", quad_area = " +
					quadBO.getArea() + ", quad_anoplantio = " + 
					quadBO.getAnoPlantio() + ", quad_nroplantas = " +
					quadBO.getNroPlantasHectare() + " WHERE quad_numero = " + quadBO.getNumero();	 

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
			String sentencaSQL ="DELETE FROM quadra WHERE quad_codigo = " + cod; 
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
