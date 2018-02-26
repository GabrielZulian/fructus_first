package gerais.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import gerais.bo.VersaoBO;

public class VersaoDao {
	Connection conexao;
	
	public VersaoDao() {
		conexao = Conexao.conectaBanco();
	}
	
	public VersaoBO consultaVersao() {
		VersaoBO versao = consulta();
		return versao;
	}

	private VersaoBO consulta() {
		Statement sentenca;
		ResultSet registros;
		
		VersaoBO versao = new VersaoBO();
		try {
			sentenca = conexao.createStatement();

			registros = sentenca.executeQuery("SELECT * from versao");

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
			} else {
					versao.setUltimaVersao(registros.getString("versao"));
				return versao;   
			}
			sentenca.close();
		} catch (SQLException eSQL) {
			eSQL.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível carregar os dados!\n" +
							"Mensagem: " + eSQL.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}
}
