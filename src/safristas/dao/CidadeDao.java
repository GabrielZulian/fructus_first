package safristas.dao;

import java.sql.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import exceptions.StringVaziaException;
import gerais.dao.Conexao;
import safristas.bo.CidadeBO;

public class CidadeDao {
	Connection conexao;

	public CidadeDao() {
		conexao = Conexao.conectaBanco();
	}

	public ArrayList <CidadeBO> consultaPorCodigo(int cod) {	//where cid_codigo = xxx, "order by cid_codigo"
		ArrayList<CidadeBO> cidBO = consulta("cid_codigo =" + cod, "cid_codigo");
		return cidBO;
	}

	public ArrayList <CidadeBO> consultaPorNome(String nome) {
		ArrayList<CidadeBO> cidBO = consulta("cid_nome containing '" +
				nome + "'", "cid_nome, cid_codigo");
		return cidBO;
	}

	public ArrayList <CidadeBO> consultaPorUF(String nome) {
		ArrayList<CidadeBO> cidBO = consulta("cid_uf containing '" +
				nome + "'", "cid_uf, cid_nome");
		return cidBO;
	}

	private ArrayList<CidadeBO> consulta(String sentencaSQL, String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();

			// faz a consulta
			registros = sentenca.executeQuery("SELECT * FROM cidades WHERE " + sentencaSQL + " Order By " + ordem);

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
				return null;
			}
			else {
				int i = 0;
				ArrayList<CidadeBO> cidBO = new ArrayList<CidadeBO>();
				do {
					cidBO.add(new CidadeBO());
					cidBO.get(i).setCodigo(Integer.parseInt(registros.getString("cid_codigo")));
					try {
						cidBO.get(i).setNome(registros.getString("cid_nome"));
					} catch (StringVaziaException e) {}
					cidBO.get(i).setUf(registros.getString("cid_uf"));
					i++;
				} while (registros.next());
				sentenca.close();
				return cidBO;   
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

	public void incluir(CidadeBO cidBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "INSERT INTO cidades (" +
					"cid_codigo, cid_nome, cid_uf) "+
					"VALUES ((SELECT COALESCE(MAX(cid_codigo), 0) + 1 FROM cidades), '"+ 
					cidBO.getNome() + "', '"+ 
					cidBO.getUf() + "')";				 	  
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

	public void alterar(CidadeBO cidBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "UPDATE cidades SET cid_nome = '" +
					cidBO.getNome()+"', cid_uf = '" + 
					cidBO.getUf() + "' " + " WHERE cid_codigo = " + cidBO.getCodigo(); 

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
			String sentencaSQL ="DELETE FROM cidades WHERE cid_codigo= " + cod; 
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
