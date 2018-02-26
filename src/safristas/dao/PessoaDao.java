package safristas.dao;

import java.sql.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import exceptions.StringVaziaException;
import gerais.dao.Conexao;
import safristas.bo.EmpregadoBO;
import safristas.bo.EmpregadorBO;
import safristas.bo.safristas.EmpreiteiroBO;
import safristas.bo.safristas.EquipeBO;

public class PessoaDao {
	Connection conexao;

	public PessoaDao() {
		conexao = Conexao.conectaBanco();
	}

	public ArrayList <EmpregadoBO> consultaEmpregado() {
		ArrayList<EmpregadoBO> adoBO = consultaAdo();
		return adoBO;
	}

	public ArrayList <EmpreiteiroBO> consultaEmpreiteiro() {
		ArrayList<EmpreiteiroBO> iroBO = consultaIro();
		return iroBO;
	}

	public ArrayList <EmpregadorBO> consultaEmpregador(String nome) {
		ArrayList<EmpregadorBO> dorBO = consultaEmpregador("cid_uf =  '" +
				nome + "'", "cid_uf");
		return dorBO;
	}
	
	public ArrayList <EquipeBO> consultaEquipes() {
		ArrayList<EquipeBO> equipeBO = consultaEquipe();
		return equipeBO;
	}

	private ArrayList<EmpregadoBO> consultaAdo () {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();

			// faz a consulta
			registros = sentenca.executeQuery("SELECT ado_codigo, ado_nome, ado_codequipe FROM empregado Order By ado_nome");

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
				return null;
			}
			else {
				int i = 0;
				ArrayList<EmpregadoBO> adoBO = new ArrayList<EmpregadoBO>();
				do {
					try {
						adoBO.add(new EmpregadoBO());
						adoBO.get(i).setCodigo(Integer.parseInt(registros.getString("ado_codigo")));
						adoBO.get(i).setNome(registros.getString("ado_nome"));
						adoBO.get(i).equipeBO.setCodigo(registros.getInt("ado_codequipe"));
						i++;
					} catch (StringVaziaException e) {}
				} while (registros.next());
				sentenca.close();
				return adoBO;   
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
	
	private ArrayList<EmpregadorBO> consultaEmpregador (String sentencaSQL, String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();

			// faz a consulta
			registros = sentenca.executeQuery("SELECT dor_codigo, dor_nome FROM empregador WHERE " + sentencaSQL + " Order By " + ordem);

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
					try {
						dorBO.add(new EmpregadorBO());
						dorBO.get(i).setCodigo(Integer.parseInt(registros.getString("dor_codigo")));
						dorBO.get(i).setNome(registros.getString("dor_nome"));
						i++;
					} catch (StringVaziaException e) {}
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
	
	private ArrayList<EmpreiteiroBO> consultaIro() {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();

			// faz a consulta
			registros = sentenca.executeQuery("SELECT iro_codigo, iro_nome FROM empreiteiro Order By iro_nome");

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
				return null;
			}
			else {
				int i = 0;
				ArrayList<EmpreiteiroBO> iroBO = new ArrayList<EmpreiteiroBO>();
				do {
					try {
						iroBO.add(new EmpreiteiroBO());
						iroBO.get(i).setCodigo(Integer.parseInt(registros.getString("iro_codigo")));
						iroBO.get(i).setNome(registros.getString("iro_nome"));
						i++;
					} catch (StringVaziaException e) {}
				} while (registros.next());
				sentenca.close();
				return iroBO;   
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
	
	private ArrayList<EquipeBO> consultaEquipe() {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();

			// faz a consulta
			registros = sentenca.executeQuery("SELECT equipe_codigo, equipe_nome, equipe_codempreiteiro FROM equipe Order By equipe_nome");

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
				return null;
			}
			else {
				int i = 0;
				ArrayList<EquipeBO> equipeBO = new ArrayList<EquipeBO>();
				do {
					try {
						equipeBO.add(new EquipeBO());
						equipeBO.get(i).setCodigo(Integer.parseInt(registros.getString("equipe_codigo")));
						equipeBO.get(i).setNome(registros.getString("equipe_nome"));
						equipeBO.get(i).iroBO.setCodigo(registros.getInt("equipe_codempreiteiro"));
						i++;
					} catch (StringVaziaException e) {}
				} while (registros.next());
				sentenca.close();
				return equipeBO;   
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
}
