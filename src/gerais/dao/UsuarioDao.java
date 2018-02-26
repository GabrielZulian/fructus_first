package gerais.dao;

import java.sql.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import exceptions.StringVaziaException;
import gerais.bo.UsuarioBO;

public class UsuarioDao {
	Connection conexao;

	public UsuarioDao() {
		conexao = Conexao.conectaBanco();
	}

	public ArrayList <UsuarioBO> consultaPorCodigo(int cod) {
		ArrayList<UsuarioBO> usuBO = consulta("usu_codigo =" + cod, "usu_codigo");
		return usuBO;
	}
	
	public ArrayList <UsuarioBO> consultaNomes() {
		ArrayList<UsuarioBO> usuBO = consulta("usu_codigo > 0", "usu_codigo");
		return usuBO;
	}

	public ArrayList <UsuarioBO> consultaPorNomeESenha(String nome, String senha) {
		ArrayList<UsuarioBO> usuBO = consulta("usu_nome = '" +
				nome + "' and usu_senha = '" + senha + "'", "usu_nome, usu_codigo");
		return usuBO;
	}

	private ArrayList<UsuarioBO> consulta(String sentencaSQL, String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();

			// faz a consulta
			registros = sentenca.executeQuery("SELECT * FROM usuario WHERE " + sentencaSQL + " Order By " + ordem);

			if (registros.next()) {
				int i = 0;
				ArrayList<UsuarioBO> usuBO = new ArrayList<UsuarioBO>();
				do {
					usuBO.add(new UsuarioBO());
					usuBO.get(i).setCodigo(Integer.parseInt(registros.getString("usu_codigo")));
					try {
						usuBO.get(i).setNome(registros.getString("usu_nome"));
					} catch (StringVaziaException e) {}
					usuBO.get(i).setSenha(registros.getString("usu_senha"));
					i++;
				} while (registros.next());
				sentenca.close();
				return usuBO;   
			}
		} catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível carregar os dados!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	public void incluir(UsuarioBO usuBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "INSERT INTO usuario (" +
					"usu_codigo, usu_nome, usu_senha) "+
					"VALUES ((SELECT COALESCE(MAX(usu_codigo), 0) + 1 FROM usuario), '"+ 
					usuBO.getNome() + "', '"+ 
					usuBO.getSenha() + "')";				 	  
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

	public void alterar(UsuarioBO usuBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "UPDATE usuario SET usu_nome = '" +
					usuBO.getNome()+ "', usu_senha = '" + 
					usuBO.getSenha() + "' " + " WHERE usu_codigo = " + usuBO.getCodigo(); 

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
			String sentencaSQL ="DELETE FROM usuario WHERE usu_codigo= " + cod; 
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
