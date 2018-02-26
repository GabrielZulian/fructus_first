package safristas.dao;

import java.sql.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import exceptions.CodigoErradoException;
import exceptions.QuantidadeErradaException;
import exceptions.StringVaziaException;
import gerais.dao.Conexao;
import safristas.bo.CidadeBO;
import safristas.bo.QuadVariedBO;

public class QuadVariDao {
	Connection conexao;

	public QuadVariDao() {
		conexao = Conexao.conectaBanco();
	}

	public ArrayList <QuadVariedBO> consultaPorNroQuadra(int nro) {
		ArrayList<QuadVariedBO> qvBO = consulta("variquad_nroquadra =" + nro, "variquad_nroquadra");
		return qvBO;
	}

	public ArrayList <QuadVariedBO> consultaPorVariedade(String variedade) {
		ArrayList<QuadVariedBO> qvBO = consulta("vari_descricao containing '" +
				variedade + "'", "vari_descricao, variquad_nroquadra");
		return qvBO;
	}

//	public ArrayList <QuadVariedBO> consultaPorUF(String nome) {
//		ArrayList<QuadVariedBO> qvBO = consulta("cid_uf containing '" +
//				nome + "'", "cid_uf, cid_nome");
//		return qvBO;
//	}

	private ArrayList<QuadVariedBO> consulta(String sentencaSQL, String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();

			// faz a consulta
			registros = sentenca.executeQuery("SELECT variedadequadra.*, variedade.vari_descricao FROM variedadequadra"
					+ " INNER JOIN variedade ON variquad_codvariedade = vari_codigo"
					+ " WHERE " + sentencaSQL + " Order By " + ordem);

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
				return null;
			} else {
				int i = 0;
				ArrayList<QuadVariedBO> qvBO = new ArrayList<QuadVariedBO>();
				do {
					qvBO.add(new QuadVariedBO());
					try {
						qvBO.get(i).setCodigo(Integer.parseInt(registros.getString("variquad_codigo")));
					} catch (NumberFormatException e1) {
					} catch (CodigoErradoException e1) {}
					try {
						qvBO.get(i).setArea(registros.getBigDecimal("variquad_area"));
					} catch (QuantidadeErradaException e) {
					}
					try {
						qvBO.get(i).variBO.setDescricao(registros.getString("vari_descricao"));
					} catch (StringVaziaException e) {
					}
					i++;
				} while (registros.next());
				sentenca.close();
				return qvBO;   
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

	public void incluir(QuadVariedBO qvBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "INSERT INTO variedadequadra (" +
					"variquad_codigo, variquad_nroquadra, variquad_codvariedade, variquad_area) " +
					"VALUES ((SELECT COALESCE(MAX(variquad_codigo), 0) + 1 FROM variedadequadra), " + 
					qvBO.quadBO.getNumero() + ", " + 
					qvBO.variBO.getCodigo() + ", " +
					qvBO.getArea() + ")";				 	  
			sentenca.executeUpdate(sentencaSQL); 
			sentenca.close();
		} catch (SQLException eSQL) {
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
		} catch (SQLException eSQL) {
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
