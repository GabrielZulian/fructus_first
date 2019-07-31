package safristas.dao.safristas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import exceptions.CpfInvalidoException;
import exceptions.StringVaziaException;
import gerais.dao.Conexao;
import safristas.bo.safristas.EmpreiteiroBO;

public class EmpreiteiroDao {
	Connection conexao;

	public EmpreiteiroDao() {
		conexao = Conexao.conectaBanco();
	}

	public ArrayList <EmpreiteiroBO> consultaPorCodigo(int cod)
	{                             //where iro_codigo = xxx, "order by iro_codigo"
		ArrayList<EmpreiteiroBO> iroBO = consulta("iro_codigo =" + cod, "iro_codigo");
		return iroBO;
	}

	public ArrayList <EmpreiteiroBO> consultaPorNome(String nome)
	{
		ArrayList<EmpreiteiroBO> iroBO = consulta("iro_nome containing '" +
				nome + "'", "iro_nome");
		return iroBO;
	}

	public ArrayList <EmpreiteiroBO> consultaPorCPF(String nome)
	{
		ArrayList<EmpreiteiroBO> iroBO = consulta("iro_cpf LIKE '%" +
				nome + "%'", "iro_cpf");
		return iroBO;
	}

	public ArrayList <EmpreiteiroBO> consultaPorApelido(String nome)
	{
		ArrayList<EmpreiteiroBO> iroBO = consulta("iro_apelido containing '" +
				nome + "'", "iro_apelido");
		return iroBO;
	}

	private ArrayList<EmpreiteiroBO> consulta(String sentencaSQL, String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();

			// faz a consulta
			registros = sentenca.executeQuery("SELECT empreiteiro.*, cidades.cid_codigo, cidades.cid_nome, cidades.cid_uf FROM empreiteiro inner join cidades ON iro_codcidade = cid_codigo WHERE " + sentencaSQL + "ORDER BY " + ordem);

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
			}
			else {
				int i = 0;
				ArrayList<EmpreiteiroBO> iroBO = new ArrayList<EmpreiteiroBO>();
				do {
					iroBO.add(new EmpreiteiroBO());
					iroBO.get(i).setCodigo(Integer.parseInt(registros.getString("iro_codigo")));
					try {
						iroBO.get(i).setNome(registros.getString("iro_nome"));
						iroBO.get(i).cidBO.setCodigo(Integer.parseInt(registros.getString("cid_codigo")));
						iroBO.get(i).cidBO.setNome(registros.getString("cid_nome"));
						iroBO.get(i).cidBO.setUf(registros.getString("cid_uf"));
						iroBO.get(i).setCpf(registros.getString("iro_cpf"));
						iroBO.get(i).setApelido(registros.getString("iro_apelido"));
						iroBO.get(i).setTelefone(registros.getString("iro_telefone"));
					} catch (StringVaziaException e) {
					} catch (CpfInvalidoException e) {}
					i++;
				} while (registros.next());
				return iroBO;   
			}
			sentenca.close();
		}catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"N�o foi poss�vel carregar os dados!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	public void incluir(EmpreiteiroBO iroBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "INSERT INTO empreiteiro (" +
					"iro_codigo, iro_nome, iro_cpf, iro_apelido, iro_codcidade, iro_telefone) "+
					"VALUES ((SELECT COALESCE(MAX(iro_codigo), 0) + 1 FROM empreiteiro), '"+ 
					iroBO.getNome() + "', '"+ 
					iroBO.getCpf() + "', '" +
					iroBO.getApelido() +"', '" +
					iroBO.cidBO.getCodigo() +"', '"+
					iroBO.getTelefone() + "')";				 	  
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

	public void alterar(EmpreiteiroBO iroBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "UPDATE empreiteiro SET iro_nome = '" +
					iroBO.getNome()+"', iro_cpf = '"+ 
					iroBO.getCpf() + "', iro_apelido = '"+ 
					iroBO.getApelido() + "', iro_codcidade = '"+
					iroBO.cidBO.getCodigo() + "', iro_telefone = '"+ 
					iroBO.getTelefone() + "' WHERE iro_codigo = " + iroBO.getCodigo();	 

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

	public boolean excluir(int cod) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL ="DELETE FROM empreiteiro WHERE iro_codigo= " + cod; 
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
