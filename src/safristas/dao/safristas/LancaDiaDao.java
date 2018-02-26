package safristas.dao.safristas;

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
import safristas.bo.safristas.LancaDiaBO;

public class LancaDiaDao {
	Connection conexao;

	public LancaDiaDao() {
		conexao = Conexao.conectaBanco();
	}

	public ArrayList <LancaDiaBO> consultaPorCodigo(int cod)
	{                             
		ArrayList<LancaDiaBO> diaBO = consulta("dia_codigo =" + cod, "dia_codigo");
		return diaBO;
	}

	public ArrayList <LancaDiaBO> consultaPorData(String nome)
	{
		ArrayList<LancaDiaBO> diaBO = consulta("dia_data LIKE '%" +
				nome + "%'", "dia_data, dia_codigo");
		return diaBO;
	}

	public ArrayList <LancaDiaBO> consultaPorCodEmpreiteiro(int cod)
	{
		ArrayList<LancaDiaBO> diaBO = consulta("dia_codempreiteiro =" + cod, "dia_codempreiteiro, dia_data, dia_codigo");
		return diaBO;
	}

	public ArrayList <LancaDiaBO> consultaPorNomeEmpreiteiro (String nome)
	{
		ArrayList<LancaDiaBO> diaBO = consulta("empreiteiro.iro_nome containing '" +
				nome + "'", "empreiteiro.iro_nome, dia_data");
		return diaBO;
	}

	public ArrayList <LancaDiaBO> consultaPorCodEquipe(int cod)
	{
		ArrayList<LancaDiaBO> diaBO = consulta("equipe.equipe_codigo =" + cod, "equipe.equipe_codigo, dia_data, dia_codigo");
		return diaBO;
	}

	public ArrayList <LancaDiaBO> consultaPorNomeEquipe (String nome)
	{
		ArrayList<LancaDiaBO> diaBO = consulta("equipe.equipe_nome containing '" +
				nome + "'", "equipe.equipe_nome, dia_data");
		return diaBO;
	}

	public ArrayList <LancaDiaBO> consultaPorValor (double valor)
	{
		ArrayList<LancaDiaBO> diaBO = consulta("dia_valortotal =" + valor, "dia_valortotal");
		return diaBO;
	}

	public int getUltimoCod () {
		Statement sentenca;
		ResultSet registros;
		try {
			sentenca = conexao.createStatement();
			registros = sentenca.executeQuery("SELECT MAX(dia_codigo) FROM diatrabalho");
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

	private ArrayList<LancaDiaBO> consulta(String sentencaSQL, String ordem) {
		Statement sentenca;
		ResultSet registros;

		try {
			sentenca = conexao.createStatement();
			// faz a consulta
			registros = sentenca.executeQuery("SELECT diatrabalho.dia_codigo, diatrabalho.dia_data, diatrabalho.dia_metodo, diatrabalho.dia_qntdbins,"
					+ " diatrabalho.dia_qntdbinsclassif, diatrabalho.dia_valorbins, diatrabalho.dia_valordia, diatrabalho.dia_valorclassif,"
					+ " diatrabalho.dia_valortotalresto, diatrabalho.dia_valortotal, diatrabalho.dia_valorcomissao, diatrabalho.dia_valorcomissaoiroclassif,"
					+ " diatrabalho.dia_valoroutroiro, diatrabalho.dia_valortotalcomissao, diatrabalho.dia_codempreiteiro, empreiteiro.iro_nome, diatrabalho.dia_codequipe,"
					+ " equipe.equipe_nome, diatrabalho.dia_observacao, diatrabalho.dia_codpagamento FROM diatrabalho"
					+ " INNER JOIN empreiteiro ON dia_codempreiteiro = iro_codigo"
					+ " INNER JOIN equipe ON dia_codequipe = equipe_codigo WHERE " + sentencaSQL + "ORDER BY " + ordem);

			if (!registros.next()) {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro foi encontrado!",
						"Mensagem", JOptionPane.WARNING_MESSAGE);
			}
			else {
				int i = 0;
				ArrayList<LancaDiaBO> diaBO = new ArrayList<LancaDiaBO>();
				do {
					diaBO.add(new LancaDiaBO());
					diaBO.get(i).setCodigo(Integer.parseInt(registros.getString("dia_codigo")));
					try {
						diaBO.get(i).data = new DateTime(registros.getDate("dia_data"));
						diaBO.get(i).setMetodo(registros.getString("dia_metodo").charAt(0));
						diaBO.get(i).setQntBinsEquipe(registros.getInt("dia_qntdbins"));
						diaBO.get(i).setQntdBinsClassif(registros.getInt("dia_qntdbinsclassif"));
						diaBO.get(i).setValorBins(registros.getDouble("dia_valorbins"));
						diaBO.get(i).setValorDia(registros.getDouble("dia_valordia"));
						diaBO.get(i).setValorClassif(registros.getDouble("dia_valorclassif"));
						diaBO.get(i).setValorTotalResto(registros.getDouble("dia_valortotalresto"));
						diaBO.get(i).setValorTotal(registros.getDouble("dia_valortotal"));
						diaBO.get(i).setValorComissao(registros.getDouble("dia_valorcomissao"));
						diaBO.get(i).setValorComissIroClassif(registros.getDouble("dia_valorcomissaoiroclassif"));
						diaBO.get(i).setValorOutroIro(registros.getDouble("dia_valoroutroiro"));
						diaBO.get(i).setValorTotalComissao(registros.getDouble("dia_valortotalcomissao"));
						diaBO.get(i).iroBO.setCodigo(registros.getInt("dia_codempreiteiro"));
						diaBO.get(i).iroBO.setNome(registros.getString("iro_nome"));
						diaBO.get(i).equipeBO.setCodigo(registros.getInt("dia_codequipe"));
						diaBO.get(i).equipeBO.setNome(registros.getString("equipe_nome"));
						diaBO.get(i).observacao.setText(registros.getString("dia_observacao"));
						diaBO.get(i).pgtoBO.setCodigo(registros.getInt("dia_codpagamento"));
					} catch (StringVaziaException e) {}
					catch (ValorErradoException e) {}
					catch (QuantidadeErradaException e) {}
					i++;
				} while (registros.next());
				return diaBO;   
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

	public void incluir(LancaDiaBO diaBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "INSERT INTO diatrabalho (" +
					"dia_codigo, dia_data, dia_codempreiteiro, dia_codequipe, dia_qntdbins, dia_qntdbinsclassif,"
					+ " dia_metodo, dia_valorbins, dia_valordia, dia_valorclassif, dia_valortotalresto, dia_valortotal,"
					+ " dia_valorcomissao, dia_valorcomissaoiroclassif, dia_valoroutroiro, dia_valortotalcomissao, dia_observacao) " +
					"VALUES ((SELECT COALESCE(MAX(dia_codigo), 0) + 1 FROM diatrabalho), '" +
					diaBO.data.getDayOfMonth() + "." + diaBO.data.getMonthOfYear() + "." + diaBO.data.getYear() + "', " + 
					diaBO.iroBO.getCodigo() + ", " +
					diaBO.equipeBO.getCodigo() + ", " +
					diaBO.getQntBinsEquipe() + ", " +
					diaBO.getQntdBinsClassif() + ", '" +
					diaBO.getMetodo() + "', " +
					diaBO.getValorBins() + ", " +
					diaBO.getValorDia() + ", " +
					diaBO.getValorClassif() + ", " +
					diaBO.getValorTotalResto() + ", " +
					diaBO.getValorTotal() + ", " +
					diaBO.getValorComissao() + ", " +
					diaBO.getValorComissIroClassif() + ", " +
					diaBO.getValorOutroIro() + ", " +
					diaBO.getValorTotalComissao() + ", '" +
					diaBO.observacao.getText() + "')";
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

	public void alterar(LancaDiaBO diaBO) {
		Statement sentenca;
		try {
			sentenca = conexao.createStatement();
			String sentencaSQL = null;
			sentencaSQL = "UPDATE diatrabalho SET dia_data = '" +
					diaBO.data.getDayOfMonth() + "." + diaBO.data.getMonthOfYear() + "." + diaBO.data.getYear() + "', dia_codempreiteiro = " + 
					diaBO.iroBO.getCodigo() + ", dia_codequipe = " +
					diaBO.equipeBO.getCodigo() + ", dia_qntdbins = " +
					diaBO.getQntBinsEquipe() + ", dia_metodo = '" +
					diaBO.getMetodo() + "', dia_valorbins = " +
					diaBO.getValorBins() + ", dia_valordia = " +
					diaBO.getValorDia() + ", dia_valorclassif = " +
					diaBO.getValorClassif() + ", dia_valortotalresto = " +
					diaBO.getValorTotalResto()  + ", dia_valortotal = " + 
					diaBO.getValorTotal() + ", dia_valorcomissao = " +
					diaBO.getValorComissao() + ", dia_qntdbinsclassif = " +
					diaBO.getQntdBinsClassif() + ", dia_valorcomissaoiroclassif = " +
					diaBO.getValorComissIroClassif() + ", dia_valoroutroiro = " +
					diaBO.getValorOutroIro() + ", dia_valortotalcomissao = " +
					diaBO.getValorTotalComissao() + ", dia_observacao = '" +
					diaBO.observacao.getText() + "' WHERE dia_codigo = " + diaBO.getCodigo();

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
			String sentencaSQL ="DELETE FROM diatrabalho WHERE dia_codigo = " + cod; 
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
