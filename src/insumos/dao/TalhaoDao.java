package insumos.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.joda.time.base.AbstractDateTime;

import gerais.dao.Conexao;
import insumos.bo.TalhaoBO;

public class TalhaoDao {
	Connection conexao;

	public TalhaoDao() {
		conexao = Conexao.conectaBanco();
	}
	
	public ArrayList<TalhaoBO> consultaPorCodigo(int cod) {
		ArrayList<TalhaoBO> insumoBO = consulta("tal_codigo = " + cod, "tal_codigo");
		return insumoBO;
	}
	
	public ArrayList<TalhaoBO> consultaPorNumero(int cod) {
		ArrayList<TalhaoBO> insumoBO = consulta("tal_numero = " + cod, "tal_numero");
		return insumoBO;
	}

	public ArrayList<TalhaoBO> consultaPorArea(BigDecimal area) {
		ArrayList<TalhaoBO> insumoBO = consulta("tal_area =" +
				area, "tal_area");
		return insumoBO;
	}
	
	public ArrayList<TalhaoBO> consultaTodos() {
		ArrayList<TalhaoBO> insumoBO = consulta("tal_numero >= 0", "tal_numero");
		return insumoBO;
	}

	private ArrayList<TalhaoBO> consulta(String sentencaSQL, String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();

			// faz a consulta
			registros = sentenca.executeQuery("SELECT * FROM talhao WHERE " + sentencaSQL + " Order By " + ordem);

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
				return null;
			}
			else {
				int i = 0;
				ArrayList<TalhaoBO> lavouraBO = new ArrayList<TalhaoBO>();
				do {
					lavouraBO.add(new TalhaoBO());
					lavouraBO.get(i).setCodigo(registros.getInt("tal_codigo"));
					lavouraBO.get(i).setTipo(registros.getString("tal_tipo"));
					lavouraBO.get(i).setNumero(registros.getInt("tal_numero"));
					lavouraBO.get(i).setArea(new BigDecimal(registros.getString("tal_area")));
					i++;
				} while (registros.next());
				sentenca.close();
				return lavouraBO;   
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

	public void incluir(TalhaoBO lavouraBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "INSERT INTO talhao (" +
					"tal_codigo, tal_tipo, tal_numero, tal_area) " +
					"VALUES ((SELECT COALESCE(MAX(tal_codigo), 0) + 1 FROM talhao), '" +
					lavouraBO.getTipo() + "', " +
					lavouraBO.getNumero() + ", " + 
					lavouraBO.getArea() + ")";				 	  
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

	public void alterar(TalhaoBO lavouraBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "UPDATE talhao SET tal_tipo = '" +
					lavouraBO.getTipo() + "', tal_numero = " +
					lavouraBO.getNumero() + ", tal_area = " +
					lavouraBO.getArea() + " WHERE tal_codigo = " + lavouraBO.getCodigo(); 

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

	public boolean excluir(int codigo) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = "DELETE FROM talhao WHERE tal_codigo = " + codigo;
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