package safristas.dao.outros;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import exceptions.StringVaziaException;
import gerais.dao.Conexao;
import safristas.bo.outros.VeiculoBO;

public class VeiculoDao {
	Connection conexao;

	public VeiculoDao() {
		conexao = Conexao.conectaBanco();
	}

	public ArrayList <VeiculoBO> consultaPorCodigo(int cod) {
		ArrayList<VeiculoBO> veicBO = consulta("veic_codigo =" + cod, "veic_codigo");
		return veicBO;
	}

	public ArrayList <VeiculoBO> consultaPorPlaca(String nome) {
		ArrayList<VeiculoBO> veicBO = consulta("veic_placa containing '" +
				nome + "'", "veic_placa");
		return veicBO;
	}

	public ArrayList <VeiculoBO> consultaPorTipo(String nome) {
		ArrayList<VeiculoBO> veicBO = consulta("veic_tipo =  '" +
				nome.charAt(0) + "'", "veic_tipo");
		return veicBO;
	}

	private ArrayList<VeiculoBO> consulta(String sentencaSQL, String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();

			// faz a consulta
			registros = sentenca.executeQuery("SELECT a.VEIC_CODIGO, a.VEIC_PLACA, a.VEIC_TIPO, a.VEIC_DESC,"
					+ " a.VEIC_CODPROP, a.VEIC_TIPOPROP, a.PESSOA_NOME FROM VIEW_PESSOAS_VEICULOS a"
					+ " WHERE " + sentencaSQL + " Order By " + ordem);

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
				return null;
			}
			else {
				int i = 0;
				ArrayList<VeiculoBO> veicBO = new ArrayList<VeiculoBO>();
				do {
					try {
						veicBO.add(new VeiculoBO());
						veicBO.get(i).setCodigo(Integer.parseInt(registros.getString("veic_codigo")));
						veicBO.get(i).setPlaca(registros.getString("veic_placa"));
						veicBO.get(i).setTipoVeiculo(registros.getString("veic_tipo").charAt(0));
						veicBO.get(i).setDescricao(registros.getString("veic_desc"));
						veicBO.get(i).setTipoEmpregado(registros.getString("veic_tipoprop").charAt(0));
						if (veicBO.get(i).getTipoEmpregado() == 'I'){
							veicBO.get(i).proprietarioIro.setCodigo(registros.getInt("veic_codprop"));
							veicBO.get(i).proprietarioIro.setNome(registros.getString("pessoa_nome"));
						} else if (veicBO.get(i).getTipoEmpregado() == 'D'){
							veicBO.get(i).proprietarioDor.setCodigo(registros.getInt("veic_codprop"));
							veicBO.get(i).proprietarioDor.setNome(registros.getString("pessoa_nome"));
						} else if (veicBO.get(i).getTipoEmpregado() == 'A'){
							veicBO.get(i).proprietarioAdo.setCodigo(registros.getInt("veic_codprop"));
							veicBO.get(i).proprietarioAdo.setNome(registros.getString("pessoa_nome"));
						}

					} catch (StringVaziaException e) {}
					i++;
				} while (registros.next());
				sentenca.close();
				return veicBO;   
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

	public void incluir(VeiculoBO veicBO) {
		Statement sentenca;

		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "INSERT INTO veiculo (" +
					"veic_codigo, veic_placa, veic_tipo, veic_desc, veic_codprop, veic_tipoprop) " +
					"VALUES ((SELECT COALESCE(MAX(veic_codigo), 0) + 1 FROM veiculo), '" + 
					veicBO.getPlaca() + "', '" + 
					veicBO.getTipoVeiculo() + "', '" +
					veicBO.getDescricao() + "', " +
					verifCodProp(veicBO) + ", '" +
					veicBO.getTipoEmpregado() + "')";				 	  
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

	public void alterar(VeiculoBO veicBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "UPDATE veiculo SET veic_placa = '" +
					veicBO.getPlaca() + "', veic_tipo = '" + 
					veicBO.getTipoVeiculo() + "', veic_desc = '" +
					veicBO.getDescricao() + "', veic_codprop = " +
					verifCodProp(veicBO) + ", veic_tipoprop = '" +
					veicBO.getTipoEmpregado() + "' WHERE veic_codigo = " + veicBO.getCodigo(); 

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
			String sentencaSQL ="DELETE FROM veiculo WHERE veic_codigo= " + cod; 
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
	
	public int verifCodProp (VeiculoBO veicBO) {
		if (veicBO.getTipoEmpregado() == 'I')
			return veicBO.proprietarioIro.getCodigo();
		else if (veicBO.getTipoEmpregado() == 'A')
			return veicBO.proprietarioAdo.getCodigo();
		else if (veicBO.getTipoEmpregado() == 'D')
			return veicBO.proprietarioDor.getCodigo();
		
		return 0;
	}
}
