package safristas.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import exceptions.StringVaziaException;
import gerais.dao.Conexao;
import safristas.bo.ProvaBO;

public class FuncaoDao {
	Connection conexao;

	public FuncaoDao() {
		conexao = Conexao.conectaBanco();
	}
	
	public ArrayList <ProvaBO> consultaPorCodigo(int cod) {
		ArrayList<ProvaBO> funBO = consulta("fun_codigo =" + cod, "fun_codigo");
		 return funBO;
	}
	
	public ArrayList <ProvaBO> consultaPorNome(String nome) {
		ArrayList<ProvaBO> funBO = consulta("fun_nome LIKE '%" + nome + "%'", "fun_nome");
		 return funBO;
	}
	
	private ArrayList<ProvaBO> consulta(String sentencaSQL, String ordem) {
		Statement sentenca;
		ResultSet registros;
				
	    try {
	      sentenca = conexao.createStatement();     
	      // faz a consulta
	      registros = sentenca.executeQuery("SELECT * FROM funcao WHERE " + sentencaSQL + " Order By " + ordem);
	      
	      if (!registros.next()) {
	        JOptionPane.showMessageDialog(null,
	                "Nenhum registro foi encontrado!",
	       	        "Mensagem", JOptionPane.WARNING_MESSAGE);
	      }
	      else {
	      	int i = 0;
	      	ArrayList<ProvaBO> funBO = new ArrayList<ProvaBO>();
	       	do {
	       		funBO.add(new ProvaBO());
	       		funBO.get(i).setCodigo(Integer.parseInt(registros.getString("fun_codigo")));
	       		try {
	       			funBO.get(i).setNome(registros.getString("fun_nome"));
				} catch (StringVaziaException e) {}
	            i++;
	    	} while (registros.next());
	      	return funBO;   
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
	
	 public void incluir(ProvaBO funBO) {
		 	Statement sentenca;
		 	try {
		 		sentenca = conexao.createStatement();
			 	String sentencaSQL = null;
			    sentencaSQL = "INSERT INTO funcao (" +
				             	"fun_codigo, fun_nome) "+
				             	"VALUES ((SELECT COALESCE(MAX(fun_codigo), 0) + 1 FROM funcao), '"+ 
				             	funBO.getNome() + "')";				 	  
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

	 public void alterar(ProvaBO funBO){
		 	Statement sentenca;
		 	try {
		 		sentenca = conexao.createStatement();
			 	String sentencaSQL = null;
			    sentencaSQL = "UPDATE funcao SET fun_nome = '" +
			    		funBO.getNome()+ "' WHERE fun_codigo = " + funBO.getCodigo(); 
			    					 	  
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
	   	  String sentencaSQL ="DELETE FROM funcao WHERE fun_codigo= " + cod; 
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
