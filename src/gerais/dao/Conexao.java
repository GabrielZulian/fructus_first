package gerais.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class Conexao {
	
	public static Connection conectaBanco() {
	    Connection conexao = null;
	    try {
	    	Class.forName("org.firebirdsql.jdbc.FBDriver");
	        conexao = DriverManager.getConnection(
//	  	   "jdbc:firebirdsql:172.16.2.250/3050:/BANCOS/BANCO.FDB?defaultResultSetHoldable=True",
	  	   "jdbc:firebirdsql:localhost/3050:C:/BANCOS/BANCO.FDB?defaultResultSetHoldable=True",
	               "sysdba", "masterkey");
	    } catch (SQLException eSQL) {
	      // exceções de SQL
	      eSQL.printStackTrace();
	      JOptionPane.showMessageDialog(null,
	              "Falha na conexão com o banco!\n" +
	              "Mensagem: " + eSQL.getMessage(),
	              "Erro", JOptionPane.ERROR_MESSAGE);
	    } catch (Exception e) {
	      // demais exceções
	      e.printStackTrace();
	      JOptionPane.showMessageDialog(null,
	       	      "Falha na conexão com o banco!\n" +
	       	      "Mensagem: " + e.getMessage(),
	              "Erro", JOptionPane.ERROR_MESSAGE);
	    }
	    return conexao;
	}
	
	 public static void desconectaBanco(Connection c) {
		 	try {
		        c.close();
		      } catch (SQLException eSQL) {
		        // exceções de SQL
		       	eSQL.printStackTrace();
		        JOptionPane.showMessageDialog(null,
		                "Não foi possível desconectar o banco!\n" +
		                "Mensagem: " + eSQL.getMessage(),
		                "Erro", JOptionPane.ERROR_MESSAGE);
		      }
		 }
}
