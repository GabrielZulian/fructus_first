package safristas.dao.outros;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.joda.time.DateTime;

import exceptions.QuantidadeErradaException;
import exceptions.StringVaziaException;
import exceptions.ValorErradoException;
import gerais.dao.Conexao;
import safristas.bo.outros.LancaDiaOutrosBO;

public class LancaDiaOutrosDao {
	Connection conexao;

	public LancaDiaOutrosDao() {
		conexao = Conexao.conectaBanco();
	}

	public ArrayList <LancaDiaOutrosBO> consultaPorCodigo(int cod)
	{                           
		ArrayList<LancaDiaOutrosBO> diaOutBO = consulta("diaout_codigo =" + cod, "diaout_codigo");
		return diaOutBO;
	}

	public ArrayList <LancaDiaOutrosBO> consultaPorNomeEmpregado(String nome)
	{
		ArrayList<LancaDiaOutrosBO> diaOutBO = consulta("ado_nome containing '" +
				nome + "'", "ado_nome, diaout_data");
		return diaOutBO;
	}

	public ArrayList <LancaDiaOutrosBO> consultaPorData(String nome)
	{
		ArrayList<LancaDiaOutrosBO> diaOutBO = consulta("diaout_data containing '" +
				nome + "'", "diaout_data, diaout_codigo");
		return diaOutBO;
	}

	public ArrayList <LancaDiaOutrosBO> consultaPorCodFuncao(int cod)
	{
		ArrayList<LancaDiaOutrosBO> diaOutBO = consulta("fun_codigo =" + cod, "fun_codigo");
		return diaOutBO;
	}

	public ArrayList <LancaDiaOutrosBO> consultaPorNomeFuncao(String nome)
	{
		ArrayList<LancaDiaOutrosBO> diaOutBO = consulta("fun_nome containing '" +
				nome + "'", "fun_nome, diaout_data");
		return diaOutBO;
	}

	public ArrayList <LancaDiaOutrosBO> consultaPorValor (double valor)
	{
		ArrayList<LancaDiaOutrosBO> diaOutBO = consulta("diaout_valortotal =" + valor, "diaout_valortotal, diaout_data");
		return diaOutBO;
	}

	public ArrayList <LancaDiaOutrosBO> consultaPorPeriodo (DateTime data1, DateTime data2)
	{
		ArrayList<LancaDiaOutrosBO> diaOutBO = consultaPeriodo("diaout_data between '" + data1.getDayOfMonth() + "." + data1.getMonthOfYear() + "." + data1.getYear()
				+ "' and '" + data2.getDayOfMonth() + "." + data2.getMonthOfYear() + "." + data2.getYear() + "'");
		return diaOutBO;
	}

	public int getUltimoCod () {
		Statement sentenca;
		ResultSet registros;
		try {
			sentenca = conexao.createStatement();
			registros = sentenca.executeQuery("SELECT MAX(diaout_codigo) FROM diaoutros");
			if(registros.next())
				return registros.getInt(1);  
			else 
				JOptionPane.showMessageDialog(null, "Nenhum registro foi encontrado!", "Mensagem", JOptionPane.WARNING_MESSAGE);

		} catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null, "Não foi possível carregar os dados!\n" +"Mensagem: " + eSQL.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
		return 0;
	}

	private ArrayList<LancaDiaOutrosBO> consulta(String sentencaSQL, String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();
			// faz a consulta
			registros = sentenca.executeQuery("SELECT * from VIEW_DIAOUTRO_EMPR_VEIC WHERE " + sentencaSQL + "ORDER BY " + ordem);
			
			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
			}
			else {
				int i = 0;
				ArrayList<LancaDiaOutrosBO> diaOutBO = new ArrayList<LancaDiaOutrosBO>();
				do {
					try {
						diaOutBO.add(new LancaDiaOutrosBO());
						diaOutBO.get(i).setCodigo(Integer.parseInt(registros.getString("diaout_codigo")));
						diaOutBO.get(i).data = new DateTime(registros.getDate("diaout_data"));
						diaOutBO.get(i).setQntBins(registros.getInt("diaout_qntdbins"));
						diaOutBO.get(i).setLote(registros.getInt("diaout_lote"));
						diaOutBO.get(i).setNroNF(registros.getInt("diaout_nronf"));
						diaOutBO.get(i).setVariedade(registros.getString("diaout_variedade"));
						diaOutBO.get(i).setValorBins(registros.getDouble("diaout_valorbins"));
						diaOutBO.get(i).setValorTotal(registros.getDouble("diaout_valortotal"));
						diaOutBO.get(i).observacao.setText(registros.getString("diaout_observacao"));
						diaOutBO.get(i).adoBO.setCodigo(registros.getInt("diaout_codempregado"));
						diaOutBO.get(i).adoBO.setNome(registros.getString("ado_nome"));
						diaOutBO.get(i).adoBO.funcaoBO.setCodigo(registros.getInt("fun_codigo"));
						diaOutBO.get(i).adoBO.funcaoBO.setNome(registros.getString("fun_nome"));
						diaOutBO.get(i).veicBO.setCodigo(registros.getInt("veic_codigo"));
						diaOutBO.get(i).veicBO.setPlaca(registros.getString("veic_placa"));
						diaOutBO.get(i).veicBO.setTipoVeiculo(registros.getString("veic_tipo").charAt(0));
						diaOutBO.get(i).setPagou(registros.getString("diaout_pagou").charAt(0));
						diaOutBO.get(i).setCodPagamento(registros.getInt("diaout_codpagamento"));
					} catch (StringVaziaException e) {}
					catch (ValorErradoException e) {System.out.println("erro valor");}
					catch (QuantidadeErradaException e) {System.out.println("erro qntd");}
		
					i++;
				} while (registros.next());
				return diaOutBO;   
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
	
	private ArrayList<LancaDiaOutrosBO> consultaPeriodo(String periodo) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();
			// faz a consulta
			registros = sentenca.executeQuery("SELECT * from VIEW_DIAOUTRO_EMPR_VEIC WHERE " + periodo + "ORDER BY ado_nome" );

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
			}
			else {
				int i = 0;
				ArrayList<LancaDiaOutrosBO> diaOutBO = new ArrayList<LancaDiaOutrosBO>();
				do {
					try {
						diaOutBO.add(new LancaDiaOutrosBO());
						diaOutBO.get(i).setCodigo(Integer.parseInt(registros.getString("diaout_codigo")));
						diaOutBO.get(i).data = new DateTime(registros.getDate("diaout_data"));
						diaOutBO.get(i).setQntBins(registros.getInt("diaout_qntdbins"));
						diaOutBO.get(i).setLote(registros.getInt("diaout_lote"));
						diaOutBO.get(i).setNroNF(registros.getInt("diaout_nronf"));
						diaOutBO.get(i).setVariedade(registros.getString("diaout_variedade"));
						diaOutBO.get(i).setValorBins(registros.getDouble("diaout_valorbins"));
						diaOutBO.get(i).setValorTotal(registros.getDouble("diaout_valortotal"));
						diaOutBO.get(i).observacao.setText(registros.getString("diaout_observacao"));
						diaOutBO.get(i).adoBO.setCodigo(registros.getInt("diaout_codempregado"));
						diaOutBO.get(i).adoBO.setNome(registros.getString("ado_nome"));
						diaOutBO.get(i).adoBO.funcaoBO.setCodigo(registros.getInt("fun_codigo"));
						diaOutBO.get(i).adoBO.funcaoBO.setNome(registros.getString("fun_nome"));
						diaOutBO.get(i).veicBO.setCodigo(registros.getInt("veic_codigo"));
						diaOutBO.get(i).veicBO.setPlaca(registros.getString("veic_placa"));
					} catch (StringVaziaException e) {}
					catch (ValorErradoException e) {System.out.println("erro valor");}
					catch (QuantidadeErradaException e) {System.out.println("erro qntd");}
		
					i++;
				} while (registros.next());
				return diaOutBO;   
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
	
	
	public ArrayList<LancaDiaOutrosBO> consultaVeiculoPagamento(int cod) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();
			// faz a consulta
			registros = sentenca.executeQuery("select a.DIAOUT_CODEMPREGADO,a.DIAOUT_CODVEICULO from diaoutros a where a.DIAOUT_CODPAGAMENTO = " + cod
					+ " and a.DIAOUT_VALORTOTAL <> 0 group by a.DIAOUT_CODEMPREGADO, a.DIAOUT_CODVEICULO order by a.DIAOUT_CODEMPREGADO");

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
			}
			else {
				int i = 0;
				ArrayList<LancaDiaOutrosBO> diaOutBO = new ArrayList<LancaDiaOutrosBO>();
				do {
						diaOutBO.add(new LancaDiaOutrosBO());
						diaOutBO.get(i).adoBO.setCodigo(registros.getInt("DIAOUT_CODEMPREGADO"));
						diaOutBO.get(i).veicBO.setCodigo(registros.getInt("DIAOUT_CODVEICULO"));
					i++;
				} while (registros.next());
				return diaOutBO;   
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

	public void incluir(LancaDiaOutrosBO diaOutBO) {
		Statement sentenca;
		
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "INSERT INTO diaoutros (" +
					"diaout_codigo, diaout_data, diaout_qntdbins, diaout_nronf, diaout_lote, diaout_variedade, diaout_valorbins, diaout_valortotal, diaout_observacao, diaout_codempregado, diaout_codveiculo, diaout_pagou, diaout_codpagamento) "+
					"VALUES ((SELECT COALESCE(MAX(diaout_codigo), 0) + 1 FROM diaoutros), '"+ 
					diaOutBO.data.getDayOfMonth() + "." + diaOutBO.data.getMonthOfYear() + "." + diaOutBO.data.getYear() + "', " +
					diaOutBO.getQntBins() + ", " +
					diaOutBO.getNroNF() + ", " +
					diaOutBO.getLote() + ", '" +
					diaOutBO.getVariedade() + "', " +
					diaOutBO.getValorBins() + ", " +
					diaOutBO.getValorTotal() + ", '" +
					diaOutBO.observacao.getText() + "', " +
					diaOutBO.adoBO.getCodigo() + ", " +
					diaOutBO.veicBO.getCodigo() + ", '" +
					 "N', "
					 + "null)";
			sentenca.executeUpdate(sentencaSQL);
			sentenca.close();
		}
		catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível realizar a inclusão!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	public void alterar(LancaDiaOutrosBO diaOutBO) {
		Statement sentenca;

		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "UPDATE diaoutros SET diaout_data = '" +
					diaOutBO.data.getDayOfMonth() + "." + diaOutBO.data.getMonthOfYear() + "." + diaOutBO.data.getYear() + "', diaout_qntdbins = " + 
					diaOutBO.getQntBins() + ", diaout_nronf = " +
					diaOutBO.getNroNF() + ", diaout_lote = " +
					diaOutBO.getLote() + ", diaout_valorbins = " + 
					diaOutBO.getValorBins()  + ", diaout_valortotal = " + 
					diaOutBO.getValorTotal() + ", diaout_observacao = '" +
					diaOutBO.observacao.getText() + "', diaout_codempregado = " +
					diaOutBO.adoBO.getCodigo() + ", diaout_codveiculo = " +
					diaOutBO.veicBO.getCodigo() + " WHERE diaout_codigo = " + diaOutBO.getCodigo();	 

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
			String sentencaSQL ="DELETE FROM diaoutros WHERE diaout_codigo = " + cod; 
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
