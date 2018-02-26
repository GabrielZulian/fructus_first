package gerais.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import exceptions.QuantidadeErradaException;
import gerais.bo.QuadraBO;

public class QuadraDao {
	Connection conexao;

	public QuadraDao() {
		conexao = Conexao.conectaBanco();
	}

	public ArrayList <QuadraBO> consultaPorNumero(int numero) {
		ArrayList<QuadraBO> quadBO = consulta2("quad_numero = " + numero, "quad_numero");
		return quadBO;
	}

	public ArrayList <QuadraBO> consultaPorArea(BigDecimal area) {
		ArrayList<QuadraBO> quadBO = consulta("quad_area = " + area, "quad_area, quad_numero");
		return quadBO;
	}

	public ArrayList <QuadraBO> consultaPorNroPlantas(int numero) {
		ArrayList<QuadraBO> quadBO = consulta("quad_nroplantas = " + numero, "quad_nroplantas, quad_numero");
		return quadBO;
	}
	
	public ArrayList <QuadraBO> consultaPorAnoPlantio(int ano) {
		ArrayList<QuadraBO> quadBO = consulta("quad_nroplantas = " + ano, "quad_nroplantas, quad_numero");
		return quadBO;
	}
	
	public ArrayList <QuadraBO> consultaPorCodigo(int codigo) {
		ArrayList<QuadraBO> quadBO = consulta("quad_codigo = " + codigo, "quad_codigo");
		return quadBO;
	}

	public ArrayList <QuadraBO> consultaPorNumeroETipo(int numero, char tipo) {
		ArrayList<QuadraBO> quadBO = consulta("fra_nroquadra = " + numero + " and fra_tipopraga = '" + tipo + "'", "fra_nroquadra");
		return quadBO;
	}

	public ArrayList <QuadraBO> consultaPorFrascos(int qntd) {
		ArrayList<QuadraBO> quadBO = consulta("quad_qntdfrascos = " + qntd, "quad_qntdfrascos");
		return quadBO;
	}

	private ArrayList<QuadraBO> consulta(String sentencaSQL, String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();

			registros = sentenca.executeQuery("SELECT quadra.quad_numero, quadra.quad_area, quadra.quad_anoplantio, quadra.quad_nroplantas, frascos.fra_nroquadra, frascos.fra_qntdfrascos, frascos.fra_tipopraga FROM quadra"
					+ " INNER JOIN frascos on quad_numero = fra_nroquadra"
					+ " WHERE " + sentencaSQL + " ORDER BY " + ordem);

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
			} else {
				int i = 0;
				ArrayList<QuadraBO> quadBO = new ArrayList<QuadraBO>();
				do {
					quadBO.add(new QuadraBO());
					try {
						quadBO.get(i).setNumero(registros.getInt("quad_numero"));
						quadBO.get(i).setArea(registros.getBigDecimal("quad_area"));
						quadBO.get(i).setNroPlantasHectare(registros.getInt("quad_nroplantas"));
						quadBO.get(i).setAnoPlantio(registros.getInt("quad_anoplantio"));
						quadBO.get(i).setNumero(registros.getInt("quad_numero"));
						quadBO.get(i).setQntdFrascos(registros.getInt("fra_qntdfrascos"));
						quadBO.get(i).setTipoPraga(registros.getString("fra_tipopraga").charAt(0));
					} catch (QuantidadeErradaException e) {
					}
					i++;
				} while (registros.next());
				return quadBO;   
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
	
	private ArrayList<QuadraBO> consulta2(String sentencaSQL, String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();

			registros = sentenca.executeQuery("SELECT quadra.quad_numero, quadra.quad_area, quadra.quad_anoplantio, quadra.quad_nroplantas FROM quadra"
					+ " WHERE " + sentencaSQL + " ORDER BY " + ordem);

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
			} else {
				int i = 0;
				ArrayList<QuadraBO> quadBO = new ArrayList<QuadraBO>();
				do {
					quadBO.add(new QuadraBO());
					try {
						quadBO.get(i).setNumero(registros.getInt("quad_numero"));
						quadBO.get(i).setArea(registros.getBigDecimal("quad_area"));
						quadBO.get(i).setNroPlantasHectare(registros.getInt("quad_nroplantas"));
						quadBO.get(i).setAnoPlantio(registros.getInt("quad_anoplantio"));
						quadBO.get(i).setNumero(registros.getInt("quad_numero"));
					} catch (QuantidadeErradaException e) {
					}
					i++;
				} while (registros.next());
				return quadBO;   
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
