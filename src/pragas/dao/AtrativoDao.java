package pragas.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import exceptions.StringVaziaException;
import gerais.dao.Conexao;
import pragas.bo.AtrativoBO;

public class AtrativoDao {
	Connection conexao;

	public AtrativoDao() {
		conexao = Conexao.conectaBanco();
	}

	public ArrayList <AtrativoBO> consultaPorCodigo(int cod) {
		ArrayList<AtrativoBO> atratBO = consulta("atrat_codigo =" + cod, "atrat_codigo");
		return atratBO;
	}

	public ArrayList <AtrativoBO> consultaPorNome(String nome) {
		ArrayList<AtrativoBO> atratBO = consulta("atrat_descricao containing '" +
				nome + "'", "atrat_descricao, atrat_codigo");
		return atratBO;
	}

	private ArrayList<AtrativoBO> consulta(String sentencaSQL, String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();

			// faz a consulta
			registros = sentenca.executeQuery("SELECT * FROM atrativo WHERE " + sentencaSQL + " Order By " + ordem);

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
				return null;
			}
			else {
				int i = 0;
				ArrayList<AtrativoBO> atratBO = new ArrayList<AtrativoBO>();
				do {
					atratBO.add(new AtrativoBO());
					atratBO.get(i).setCodigo(Integer.parseInt(registros.getString("atrat_codigo")));
					try {
						atratBO.get(i).setDescricao(registros.getString("atrat_descricao"));
					} catch (StringVaziaException e) {}
					atratBO.get(i).setDiasTroca(registros.getInt("atrat_diastroca"));
					i++;
				} while (registros.next());
				sentenca.close();
				return atratBO;   
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

	public void incluir(AtrativoBO atratBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "INSERT INTO atrativo (" +
					"atrat_codigo, atrat_descricao, atrat_diastroca) "+
					"VALUES ((SELECT COALESCE(MAX(atrat_codigo), 0) + 1 FROM atrativo), '"+ 
					atratBO.getDescricao() + "', "+ 
					atratBO.getDiasTroca() + ")";				 	  
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

	public void alterar(AtrativoBO atratBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "UPDATE atrativo SET atrat_descricao = '" +
					atratBO.getDescricao()+"', atrat_diastroca = " + 
					atratBO.getDiasTroca() + " WHERE atrat_codigo = " + atratBO.getCodigo(); 

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
			String sentencaSQL ="DELETE FROM atrativo WHERE atrat_codigo = " + cod;
			sentenca.executeUpdate(sentencaSQL);
			sentenca.close();
		} catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível realizar a exclusão!\n" +
							"Mensagem: Esse registro está sendo referenciado por outra tabela",
							"Erro", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	} 
}
