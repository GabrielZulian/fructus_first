package safristas.dao.safristas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import exceptions.StringVaziaException;
import exceptions.ValorErradoException;
import gerais.dao.Conexao;
import safristas.bo.safristas.DiaEmpregadoBO;

public class DiaEmpregadoDao {
	Connection conexao;

	public DiaEmpregadoDao() {
		conexao = Conexao.conectaBanco();
	}

	public ArrayList <DiaEmpregadoBO> consultaPorCodigo(int cod) {
		ArrayList<DiaEmpregadoBO> diaAdoBO = consulta("diaemp_codigo =" + cod, "diaemp_codigo");
		return diaAdoBO;
	}

	public ArrayList <DiaEmpregadoBO> consultaPorData(String nome) {
		ArrayList<DiaEmpregadoBO> diaAdoBO = consulta("dia_data LIKE '%" +
				nome + "%'", "dia_data");
		return diaAdoBO;
	}

	public ArrayList <DiaEmpregadoBO> consultaPorCodEquipe(int cod) {
		ArrayList<DiaEmpregadoBO> diaAdoBO = consulta("equipe.equipe_codigo =" + cod, "equipe.equipe_codigo");
		return diaAdoBO;
	}

	public ArrayList <DiaEmpregadoBO> consultaPorCodigoDia (int cod) {
		ArrayList<DiaEmpregadoBO> diaAdoBO = consulta("diaemp_codigodia =" + cod, "diaemp_codigodia");
		return diaAdoBO;
	}

	public int getUltimoCod () {
		Statement sentenca;
		ResultSet registros;
		try {
			sentenca = conexao.createStatement();
			registros = sentenca.executeQuery("SELECT MAX(dia_codigo) FROM diatrabalho");
			if (registros.next()) {
				return registros.getInt(1);
			} else {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
			}

		} catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível carregar os dados!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
		return 0;
	}

	private ArrayList<DiaEmpregadoBO> consulta(String sentencaSQL, String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();

			// faz a consulta
			registros = sentenca.executeQuery("SELECT diaempregado.diaemp_codigo, diaempregado.diaemp_codempregado, diaempregado.diaemp_codigodia,"
					+ " diaempregado.diaemp_valoremp, diaempregado.diaemp_presenca, diaempregado.diaemp_rateio, diaempregado.diaemp_classif,"
					+ " diaempregado.diaemp_pagou, empregado.ado_nome, funcao.fun_nome"
					+ " FROM diaempregado INNER JOIN diatrabalho ON dia_codigo = diaemp_codigodia"
					+ " INNER JOIN empregado ON diaemp_codempregado = ado_codigo"
					+ " INNER JOIN funcao ON empregado.ado_codfuncao = funcao.fun_codigo WHERE " + sentencaSQL + " ORDER BY " + ordem);

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
			}
			else {
				int i = 0;
				ArrayList<DiaEmpregadoBO> diaAdoBO = new ArrayList<DiaEmpregadoBO>();
				do {
					diaAdoBO.add(new DiaEmpregadoBO());
					diaAdoBO.get(i).setCodigo(registros.getInt("diaemp_codigo"));
					try {
						diaAdoBO.get(i).adoBO.setCodigo(registros.getInt("diaemp_codempregado"));
						diaAdoBO.get(i).adoBO.setNome(registros.getString("ado_nome"));
						diaAdoBO.get(i).diaBO.setCodigo(registros.getInt("diaemp_codigodia"));
						diaAdoBO.get(i).setValor(registros.getDouble("diaemp_valoremp"));
						diaAdoBO.get(i).setPresenca(registros.getString("diaemp_presenca").charAt(0));
						diaAdoBO.get(i).setRateio(registros.getBigDecimal("diaemp_rateio"));
						diaAdoBO.get(i).setClassificador(registros.getString("diaemp_classif").charAt(0));
						diaAdoBO.get(i).setPagou(registros.getString("diaemp_pagou").charAt(0));
						diaAdoBO.get(i).adoBO.funcaoBO.setNome(registros.getString("fun_nome"));
					} catch (ValorErradoException e) {
					} catch (StringVaziaException e) {}
					i++;
				} while (registros.next());
				return diaAdoBO;   
			}
			sentenca.close();
		}catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível carregar os dados!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	public void incluir(DiaEmpregadoBO diaAdoBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "INSERT INTO diaempregado (" +
					"diaemp_codigo, diaemp_codempregado, diaemp_valoremp, diaemp_presenca, diaemp_rateio, diaemp_codigodia, diaemp_classif, diaemp_pagou) "+
					"VALUES ((SELECT COALESCE(MAX(diaemp_codigo), 0) + 1 FROM diaempregado), "+ 
					diaAdoBO.adoBO.getCodigo() + ", " +
					diaAdoBO.getValor() +", '" +
					diaAdoBO.getPresenca() + "', " +
					diaAdoBO.getRateio() + ", " +
					diaAdoBO.diaBO.getCodigo() + ", '" +
					diaAdoBO.getClassificador() + "', '" + 
					"N" + "')";				 	  
			sentenca.executeUpdate(sentencaSQL); 
			sentenca.close();
		} catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível realizar a inclusão!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	public void alterar(DiaEmpregadoBO diaAdoBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "UPDATE diaempregado SET diaemp_codempregado = " + 
					diaAdoBO.adoBO.getCodigo() + ", diaemp_valoremp = " + 
					diaAdoBO.getValor() + ", diaemp_presenca = '" +
					diaAdoBO.getPresenca() + "', diaemp_rateio = " + 
					diaAdoBO.getRateio() + ", diaemp_classif = '" +  
					diaAdoBO.getClassificador() + "' WHERE diaemp_codigodia = " + diaAdoBO.diaBO.getCodigo() + "AND diaemp_codempregado = " + diaAdoBO.adoBO.getCodigo();	 

			sentenca.executeUpdate(sentencaSQL); 
			sentenca.close();
		}
		catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível realizar a alteração!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	public boolean excluir(int cod) {
		Statement sentenca;
		ArrayList<DiaEmpregadoBO> diaBO = consultaPorCodigoDia(cod);
		if (diaBO.get(0).getPagou() == 'S') {
			JOptionPane.showMessageDialog(null, "Este dia já contém pagamento! Exclua o pagamento primeiro, caso deseje excluir este registro.", "Excluir Registro",JOptionPane.ERROR_MESSAGE);
			return false;
		} else {
			try {
				sentenca = conexao.createStatement();
				String sentencaSQL ="DELETE FROM diaempregado WHERE diaemp_codigodia = " + cod; 
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
}
