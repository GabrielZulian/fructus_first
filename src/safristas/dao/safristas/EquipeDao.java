package safristas.dao.safristas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import exceptions.StringVaziaException;
import gerais.dao.Conexao;
import safristas.bo.safristas.EquipeBO;

public class EquipeDao {
	Connection conexao;

	public EquipeDao() {
		conexao = Conexao.conectaBanco();
	}

	public ArrayList <EquipeBO> consultaPorCodigo(int cod) {
		ArrayList<EquipeBO> equiBO = consulta("equipe_codigo =" + cod, "equipe_codigo");
		return equiBO;
	}

	public ArrayList <EquipeBO> consultaPorNome(String nome) {
		ArrayList<EquipeBO> equiBO = consulta("equipe_nome LIKE '%" +
				nome + "%'", "equipe_nome");
		return equiBO;
	}

	public ArrayList <EquipeBO> consultaPorCodEmpreiteiro(int cod) {
		ArrayList<EquipeBO> equiBO = consulta("empreiteiro.iro_codigo =" + cod, "empreiteiro.iro_codigo, equipe_nome");
		return equiBO;
	}

	public ArrayList <EquipeBO> consultaPorNomeEmpreiteiro(String nome) {
		ArrayList<EquipeBO> equiBO = consulta("empreiteiro.iro_nome LIKE '%" +
				nome + "%'", "empreiteiro.iro_nome, equipe_nome");
		return equiBO;
	}
	
	public ArrayList <EquipeBO> consultaPorCodEmpreiteiroSomenteAtivas(int cod) {
		ArrayList<EquipeBO> equiBO = consultaAtivas(cod, "e.equipe_nome");
		return equiBO;
	}
	
	private ArrayList<EquipeBO> consultaAtivas(int cod, String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();
			// faz a consulta
			registros = sentenca.executeQuery("SELECT DISTINCT e.equipe_codigo, e.equipe_nome FROM equipe e" + 
					" INNER JOIN empreiteiro i ON e.equipe_codempreiteiro = i.iro_codigo" + 
					" INNER JOIN empregado a ON a.ado_codequipe = e.equipe_codigo" + 
					" WHERE a.ado_codequipe IS NOT NULL AND i.iro_codigo = " + cod + " ORDER BY " + ordem);

			if (!registros.next())
			{
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
			}
			else {
				int i = 0;
				ArrayList<EquipeBO> equiBO = new ArrayList<EquipeBO>();
				do {
					equiBO.add(new EquipeBO());
					try {
						equiBO.get(i).setCodigo(registros.getInt("equipe_codigo"));
						equiBO.get(i).setNome(registros.getString("equipe_nome"));
					} catch (StringVaziaException e) {}
					i++;
				} while (registros.next());
				return equiBO;   
			}
			sentenca.close();
		} catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"N�o foi poss�vel carregar os dados!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	private ArrayList<EquipeBO> consulta(String sentencaSQL, String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();
			// faz a consulta
			registros = sentenca.executeQuery("SELECT equipe.*, empreiteiro.iro_codigo, empreiteiro.iro_nome FROM equipe"
					+ " INNER JOIN empreiteiro ON equipe_codempreiteiro = iro_codigo WHERE " + sentencaSQL + " Order By " + ordem);

			if (!registros.next())
			{
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
			}
			else {
				int i = 0;
				ArrayList<EquipeBO> equiBO = new ArrayList<EquipeBO>();
				do {
					equiBO.add(new EquipeBO());
					try {
						equiBO.get(i).setCodigo(registros.getInt("equipe_codigo"));
						equiBO.get(i).setNome(registros.getString("equipe_nome"));
						equiBO.get(i).iroBO.setCodigo(registros.getInt("iro_codigo"));
						equiBO.get(i).iroBO.setNome(registros.getString("iro_nome"));
					} catch (StringVaziaException e) {}
					i++;
				} while (registros.next());
				return equiBO;   
			}
			sentenca.close();
		} catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"N�o foi poss�vel carregar os dados!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	public void incluir(EquipeBO equiBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "INSERT INTO equipe (" +
					"equipe_codigo, equipe_nome, equipe_codempreiteiro) "+
					"VALUES ((SELECT COALESCE(MAX(equipe_codigo), 0) + 1 FROM equipe), '"+ 
					equiBO.getNome() + "', " +
					equiBO.iroBO.getCodigo() + ")";				 	  
			sentenca.executeUpdate(sentencaSQL); 
			sentenca.close();
		}
		catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"N�o foi poss�vel realizar a inclus�o!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void alterar(EquipeBO equiBO){
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "UPDATE equipe SET equipe_nome = '" +equiBO.getNome() + "', " +
					"equipe_codempreiteiro = " + equiBO.iroBO.getCodigo() +
					" WHERE equipe_codigo = " + equiBO.getCodigo();

			sentenca.executeUpdate(sentencaSQL);
			sentenca.close();
		} catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"N�o foi poss�vel realizar a inclus�o!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	public boolean excluir(int cod) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL ="DELETE FROM equipe WHERE equipe_codigo= " + cod; 
			sentenca.executeUpdate(sentencaSQL); 
			sentenca.close();
		}catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"N�o foi poss�vel realizar a opera��o!\n" +
							"Mensagem: Esse registro est� sendo referenciado por outra tabela",
							"Erro", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	} 
}
