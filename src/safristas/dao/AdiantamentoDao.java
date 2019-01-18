package safristas.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.joda.time.DateTime;

import exceptions.StringVaziaException;
import exceptions.ValorErradoException;
import gerais.dao.Conexao;
import safristas.bo.AdiantamentoBO;

public class AdiantamentoDao {
	Connection conexao;

	public AdiantamentoDao() {
		conexao = Conexao.conectaBanco();
	}

	public ArrayList <AdiantamentoBO> consultaPorCodigo(int cod) {
		ArrayList<AdiantamentoBO> adiantBO = consulta("adi_codigo =" + cod, "adi_codigo");
		return adiantBO;
	}

	public ArrayList <AdiantamentoBO> consultaPorData(String data) {
		ArrayList<AdiantamentoBO> adiantBO = consulta("adi_data LIKE '%" +
				data + "%'", "adi_data, adi_codigo");
		return adiantBO;
	}

	public ArrayList <AdiantamentoBO> consultaPorValor(double valor) {
		ArrayList<AdiantamentoBO> adiantBO = consulta("adi_valor =  " +
				valor, "adi_valor, adi_codigo");
		return adiantBO;
	}
	
	public ArrayList <AdiantamentoBO> consultaPorSituacao(char pagou) {
		ArrayList<AdiantamentoBO> adiantBO = consulta("adi_pagou LIKE '%" +
				pagou + "%'", "adi_codigo");
		return adiantBO;
	}
	
	public ArrayList <AdiantamentoBO> consultaPorCodigoNPagos(int codAdiantamento) {
		ArrayList<AdiantamentoBO> adiantBO = consulta("adi_codigo =  " +
				codAdiantamento + " and adi_pagou = 'N'", "adi_codigo");
		return adiantBO;
	}

	private ArrayList<AdiantamentoBO> consulta(String sentencaSQL, String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();

			// faz a consulta
			registros = sentenca.executeQuery("SELECT adiantamento.*, empregado.ado_nome, empregado.ado_codigo,"
					+ " empreiteiro.iro_nome, empreiteiro.iro_codigo FROM adiantamento"
					+ " LEFT JOIN empregado on ado_codigo = adi_codempregado"
					+ " LEFT JOIN empreiteiro on iro_codigo = adi_codempregado WHERE " + sentencaSQL + " Order By " + ordem);

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
				return null;
			}
			else {
				int i = 0;
				ArrayList<AdiantamentoBO> adiantBO = new ArrayList<AdiantamentoBO>();
				do {
					try {
						adiantBO.add(new AdiantamentoBO());
						adiantBO.get(i).setCodigo(Integer.parseInt(registros.getString("adi_codigo")));
						adiantBO.get(i).data = new DateTime(registros.getDate("adi_data"));
						adiantBO.get(i).setTipo(registros.getString("adi_tipoempregado").charAt(0));
						if (adiantBO.get(i).getTipo() == 'A') {
							adiantBO.get(i).adoBO.setCodigo(registros.getInt("ado_codigo"));
							adiantBO.get(i).adoBO.setNome(registros.getString("ado_nome"));
						} else {
							adiantBO.get(i).iroBO.setCodigo(registros.getInt("iro_codigo"));
							adiantBO.get(i).iroBO.setNome(registros.getString("iro_nome"));
						}
						adiantBO.get(i).setPagou(registros.getString("adi_pagou").charAt(0));
						adiantBO.get(i).setValor(registros.getDouble("adi_valor"));
					} catch (StringVaziaException e) {}
					catch (ValorErradoException e2) {}
					i++;
				} while (registros.next());
				sentenca.close();
				return adiantBO;   
			}
		}catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"N�o foi poss�vel carregar os dados!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}
	
	public int getUltimoCod () {
		Statement sentenca;
		ResultSet registros;
		try {
			sentenca = conexao.createStatement();
			registros = sentenca.executeQuery("SELECT MAX(adi_codigo) FROM adiantamento");
			if(registros.next())
				return registros.getInt(1);  
			else 
				JOptionPane.showMessageDialog(null, "Nenhum registro foi encontrado!", "Mensagem", JOptionPane.WARNING_MESSAGE);

		} catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null, "N�o foi poss�vel carregar os dados!\n" +"Mensagem: " + eSQL.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
		return 0;
	}
	
	public void atualizaPago(int codigo) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "UPDATE adiantamento SET adi_pagou = 'S' where adi_codigo = " + codigo; 
			sentenca.executeUpdate(sentencaSQL); 
			sentenca.close();
		}
		catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"N�o foi poss�vel realizar a opera��o!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void desatualizaPago(int codigo) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "UPDATE adiantamento SET adi_pagou = 'N' where adi_codigo = " + codigo; 
			sentenca.executeUpdate(sentencaSQL); 
			sentenca.close();
		}
		catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"N�o foi poss�vel realizar a opera��o!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void incluir(AdiantamentoBO adiBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "INSERT INTO adiantamento (" +
					"adi_codigo, adi_data, adi_tipoempregado, adi_codempregado, adi_valor, adi_pagou) " +
					"VALUES ((SELECT COALESCE(MAX(adi_codigo), 0) + 1 FROM adiantamento), '" + 
					 adiBO.data.getYear() + "." + adiBO.data.getMonthOfYear() + "." + adiBO.data.getDayOfMonth() + "', '" +
					 adiBO.getTipo() + "', " +
					 (adiBO.getTipo() == 'A'?adiBO.adoBO.getCodigo():adiBO.iroBO.getCodigo()) + " , " + 
					 adiBO.getValor() + ", '" +
					 "N' )"; 
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

	public void alterar(AdiantamentoBO adiBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "UPDATE adiantamento SET adi_data = '" +
					adiBO.data.getYear() + "." + adiBO.data.getMonthOfYear() + "." + adiBO.data.getDayOfMonth() + "', adi_tipoempregado = '" + 
					adiBO.getTipo() + "', adi_codempregado = " + 
					(adiBO.getTipo() == 'A'?adiBO.adoBO.getCodigo():adiBO.iroBO.getCodigo()) + ", adi_valor = " +
					adiBO.getValor() + " WHERE adi_codigo = " + adiBO.getCodigo(); 

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
			String sentencaSQL ="DELETE FROM adiantamento WHERE adi_codigo = " + cod; 
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
