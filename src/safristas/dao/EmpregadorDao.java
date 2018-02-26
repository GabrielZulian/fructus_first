package safristas.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import exceptions.CpfInvalidoException;
import exceptions.StringVaziaException;
import gerais.dao.Conexao;
import safristas.bo.EmpregadorBO;

public class EmpregadorDao {
	Connection conexao;

	public EmpregadorDao() {
		conexao = Conexao.conectaBanco();
	}

	public ArrayList <EmpregadorBO> consultaPorCodigo(int cod)
	{                             //where dor_codigo = xxx, "order by dor_codigo"
		ArrayList<EmpregadorBO> dorBO = consulta("dor_codigo =" + cod, "dor_codigo");
		return dorBO;
	}

	public ArrayList <EmpregadorBO> consultaPorNome(String nome)
	{
		ArrayList<EmpregadorBO> dorBO = consulta("dor_nome containing '" +
				nome + "'", "dor_nome");
		return dorBO;
	}

	public ArrayList <EmpregadorBO> consultaPorCPF(String nome)
	{
		ArrayList<EmpregadorBO> dorBO = consulta("dor_cpf LIKE '%" +
				nome + "%'", "dor_cpf");
		return dorBO;
	}

	public ArrayList <EmpregadorBO> consultaPorIE(String nome)
	{
		ArrayList<EmpregadorBO> dorBO = consulta("dor_ie LIKE '%" +
				nome + "%'", "dor_ie");
		return dorBO;
	}

	private ArrayList<EmpregadorBO> consulta(String sentencaSQL, String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();

			// faz a consulta
			registros = sentenca.executeQuery("SELECT empregador.*, cidades.cid_codigo, cidades.cid_nome, cidades.cid_uf FROM empregador inner join cidades ON dor_codcidade = cid_codigo WHERE " + sentencaSQL + "ORDER BY " + ordem);

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
				return null;
			}
			else {
				int i = 0;
				ArrayList<EmpregadorBO> dorBO = new ArrayList<EmpregadorBO>();
				do {
					dorBO.add(new EmpregadorBO());
					dorBO.get(i).setCodigo(Integer.parseInt(registros.getString("dor_codigo")));
					try {
						dorBO.get(i).setNome(registros.getString("dor_nome"));
						dorBO.get(i).cidBO.setCodigo(Integer.parseInt(registros.getString("cid_codigo")));
						dorBO.get(i).cidBO.setNome(registros.getString("cid_nome"));
						dorBO.get(i).cidBO.setUf(registros.getString("cid_uf"));
						dorBO.get(i).setCpf(registros.getString("dor_cpf"));
						dorBO.get(i).setIe(registros.getString("dor_ie"));
						dorBO.get(i).setTelefone(registros.getString("dor_telefone"));
					} catch (StringVaziaException e) {
					} catch (CpfInvalidoException e) {}
					i++;
				} while (registros.next());
				sentenca.close();
				return dorBO;   
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

	public void incluir(EmpregadorBO dorBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "INSERT INTO empregador (" +
					"dor_codigo, dor_nome, dor_cpf, dor_ie, dor_codcidade, dor_telefone) "+
					"VALUES ((SELECT COALESCE(MAX(dor_codigo), 0) + 1 FROM empregador), '"+ 
					dorBO.getNome() + "', '"+ 
					dorBO.getCpf() + "', '" +
					dorBO.getIe() +"', '" +
					dorBO.cidBO.getCodigo() +"', '"+
					dorBO.getTelefone() + "')";				 	  
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

	public void alterar(EmpregadorBO dorBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "UPDATE empregador SET dor_nome = '" +
					dorBO.getNome()+"', dor_cpf = '"+ 
					dorBO.getCpf() + "', dor_ie = '"+ 
					dorBO.getIe() + "', dor_codcidade = '"+
					dorBO.cidBO.getCodigo() + "', dor_telefone = '"+ 
					dorBO.getTelefone() + "' WHERE dor_codigo = " + dorBO.getCodigo();	 

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
			String sentencaSQL ="DELETE FROM empregador WHERE dor_codigo= " + cod; 
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
