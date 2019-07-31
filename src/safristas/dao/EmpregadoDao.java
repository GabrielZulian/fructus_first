package safristas.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import javax.swing.JOptionPane;

import exceptions.CpfInvalidoException;
import exceptions.StringVaziaException;
import gerais.dao.Conexao;
import safristas.bo.EmpregadoBO;

public class EmpregadoDao {
	Connection conexao;

	public EmpregadoDao() {
		conexao = Conexao.conectaBanco();
	}

	public ArrayList <EmpregadoBO> consultaPorCodigo(int cod)
	{                             //where ado_codigo = xxx, "order by ado_codigo"
		ArrayList<EmpregadoBO> adoBO = consulta("ado_codigo =" + cod, "ado_codigo");
		return adoBO;
	}

	public ArrayList <EmpregadoBO> consultaPorNome(String nome)
	{
		ArrayList<EmpregadoBO> adoBO = consulta("ado_nome containing '" +
				nome + "'", "ado_nome");
		return adoBO;
	}
	
	
	public ArrayList <EmpregadoBO> consultaPorCPF(String cpf)
	{
		ArrayList<EmpregadoBO> adoBO = consulta("ado_cpf LIKE '%" + cpf + "%'", "ado_cpf");
		return adoBO;
	}

	public ArrayList <EmpregadoBO> consultaPorApelido(String nome)
	{
		ArrayList<EmpregadoBO> adoBO = consulta("ado_apelido containing '" +
				nome + "'", "ado_apelido, empregado.ado_nome");
		return adoBO;
	}

	public ArrayList <EmpregadoBO> consultaPorNomeEmpreiteiro(String nome)
	{
		ArrayList<EmpregadoBO> adoBO = consulta("empreiteiro.iro_nome containing '" +
				nome + "'", "empreiteiro.iro_nome, empregado.ado_nome");
		return adoBO;
	}

	public ArrayList <EmpregadoBO> consultaPorNomeFuncao(String nome)
	{
		ArrayList<EmpregadoBO> adoBO = consulta("funcao.fun_nome containing '" +
				nome + "'", "funcao.fun_nome, empregado.ado_nome");
		return adoBO;
	}

	public ArrayList <EmpregadoBO> consultaPorNomeEquipe(String nome)
	{
		ArrayList<EmpregadoBO> adoBO = consulta("equipe.equipe_nome containing '" +
				nome + "'", "equipe.equipe_nome, empregado.ado_nome");
		return adoBO;
	}

	public ArrayList <EmpregadoBO> consultaPorCodEmpreiteiro(int cod)
	{
		ArrayList<EmpregadoBO> adoBO = consulta("iro_codigo = " + cod, "iro_codigo, empregado.ado_nome");
		return adoBO;
	}

	public ArrayList <EmpregadoBO> consultaPorCodFuncao(int cod)
	{
		ArrayList<EmpregadoBO> adoBO = consulta("ado_codfuncao =" + cod, "ado_codfuncao, empregado.ado_nome");
		return adoBO;
	}

	public ArrayList <EmpregadoBO> consultaPorCodEquipe(int cod)
	{
		ArrayList<EmpregadoBO> adoBO = consulta("equipe_codigo =" + cod, "equipe_codigo, empregado.ado_nome");
		return adoBO;
	}

	private ArrayList<EmpregadoBO> consulta(String sentencaSQL, String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();

			// faz a consulta
			registros = sentenca.executeQuery("SELECT empregado.*, empreiteiro.iro_nome, empreiteiro.iro_codigo, equipe.equipe_codigo, "
					+ "equipe.equipe_nome, funcao.fun_codigo, funcao.fun_nome FROM empregado "
					+ "INNER JOIN funcao ON ado_codfuncao = fun_codigo "
					+ "INNER JOIN equipe ON ado_codequipe = equipe_codigo "
					+ "INNER JOIN empreiteiro ON equipe_codempreiteiro = iro_codigo WHERE " + sentencaSQL + "ORDER BY " + ordem);

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
			}
			else {
				int i = 0;
				ArrayList<EmpregadoBO> adoBO = new ArrayList<EmpregadoBO>();
				do {
					adoBO.add(new EmpregadoBO());
					adoBO.get(i).setCodigo(Integer.parseInt(registros.getString("ado_codigo")));
					try {
						adoBO.get(i).setNome(registros.getString("ado_nome"));
						adoBO.get(i).setApelido(registros.getString("ado_apelido"));
						adoBO.get(i).setCpf(registros.getString("ado_cpf"));
						adoBO.get(i).equipeBO.iroBO.setCodigo(registros.getInt("iro_codigo"));
						adoBO.get(i).equipeBO.iroBO.setNome(registros.getString("iro_nome"));
						adoBO.get(i).equipeBO.setCodigo(registros.getInt("ado_codequipe"));
						adoBO.get(i).equipeBO.setNome(registros.getString("equipe_nome"));
						adoBO.get(i).funcaoBO.setCodigo(registros.getInt("ado_codfuncao"));
						adoBO.get(i).funcaoBO.setNome(registros.getString("fun_nome"));
					} catch (StringVaziaException e) {
					} catch (CpfInvalidoException e) {}
					i++;
				} while (registros.next());
				return adoBO;   
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

	public void incluir(EmpregadoBO adoBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "INSERT INTO empregado (" +
					"ado_codigo, ado_nome, ado_apelido, ado_cpf, ado_codequipe, ado_codfuncao) "+
					"VALUES ((SELECT COALESCE(MAX(ado_codigo), 0) + 1 FROM empregado), '"+ 
					adoBO.getNome() + "', '"+ 
					adoBO.getApelido() + "', '" +
					adoBO.getCpf() + "', " +
					adoBO.equipeBO.getCodigo() +", "+
					adoBO.funcaoBO.getCodigo() + ")";				 	  
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

	public void alterar(EmpregadoBO adoBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "UPDATE empregado SET ado_nome = '" +
					adoBO.getNome() +"', ado_apelido = '"+
					adoBO.getApelido() + "', ado_cpf = '" + 
					adoBO.getCpf() + "', ado_codequipe = " +
					adoBO.equipeBO.getCodigo() + ", ado_codfuncao = " + 
					adoBO.funcaoBO.getCodigo() + " WHERE ado_codigo = " + adoBO.getCodigo();	 

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
			String sentencaSQL ="DELETE FROM empregado WHERE ado_codigo= " + cod; 
			sentenca.executeUpdate(sentencaSQL); 
			sentenca.close();
		}catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"N�o foi poss�vel realizar a opera��o!\n" +
							"Mensagem: Esse registro está sendo referenciado por outra tabela",
							"Erro", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	public void moverEmpregadosParaEquipe(List<Integer> codPessoal, int codEquipe) {
		
		String listString = codPessoal.toString();
		listString = listString.substring(1, listString.length()-1);
		
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "UPDATE empregado SET ado_codequipe = " +
					codEquipe + " WHERE ado_codigo IN (" + listString + ")";

			sentenca.executeUpdate(sentencaSQL);
			sentenca.close();
		}
		catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível realizar a troca de equipe!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
	} 

}
