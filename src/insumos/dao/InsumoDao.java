package insumos.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import exceptions.StringVaziaException;
import gerais.dao.Conexao;
import insumos.bo.InsumoBO;

public class InsumoDao {
	Connection conexao;

	public InsumoDao() {
		conexao = Conexao.conectaBanco();
	}
	
	public ArrayList <InsumoBO> consultaPorCodigo(int cod) {
		ArrayList<InsumoBO> insumoBO = consulta("ins_codigo =" + cod, "ins_codigo");
		return insumoBO;
	}

	public ArrayList <InsumoBO> consultaPorNome(String nome) {
		ArrayList<InsumoBO> insumoBO = consulta("ins_descricao containing '" +
				nome + "'", "ins_descricao, ins_codigo");
		return insumoBO;
	}

	private ArrayList<InsumoBO> consulta(String sentencaSQL, String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();

			// faz a consulta
			registros = sentenca.executeQuery("SELECT * FROM insumo WHERE " + sentencaSQL + " Order By " + ordem);

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
				return null;
			}
			else {
				int i = 0;
				ArrayList<InsumoBO> insumoBO = new ArrayList<InsumoBO>();
				do {
					insumoBO.add(new InsumoBO());
					insumoBO.get(i).setCodigo(registros.getInt("ins_codigo"));
					try {
						insumoBO.get(i).setDescricao(registros.getString("ins_descricao"));
					} catch (StringVaziaException e) {}
					insumoBO.get(i).setUnidade(registros.getString("ins_unidade"));
					insumoBO.get(i).setDiasResidual(registros.getInt("ins_diasresidual"));
					i++;
				} while (registros.next());
				sentenca.close();
				return insumoBO;   
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

	public void incluir(InsumoBO insumoBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "INSERT INTO insumo (" +
					"ins_codigo, ins_descricao, ins_unidade, ins_diasresidual) "+
					"VALUES ((SELECT COALESCE(MAX(ins_codigo), 0) + 1 FROM insumo), '"+ 
					insumoBO.getDescricao() + "', '" + 
					insumoBO.getUnidade() + "', " +
					insumoBO.getDiasResidual() + ")";				 	  
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

	public void alterar(InsumoBO insumoBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "UPDATE insumo SET ins_descricao = '" +
					insumoBO.getDescricao()+"', ins_unidade = '" + 
					insumoBO.getUnidade() + "', ins_diasresidual = " +
					insumoBO.getDiasResidual() + " WHERE ins_codigo = " + insumoBO.getCodigo(); 

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
			String sentencaSQL ="DELETE FROM insumo WHERE ins_codigo = " + cod; 
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