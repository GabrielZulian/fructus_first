package impressos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import gerais.dao.Conexao;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class ReciboSafristas {
	private Connection con;
	Map<String, Object> parametros = new HashMap<String, Object>();

	public ReciboSafristas(int codPagamento) {
		con = Conexao.conectaBanco();

		ImageIcon gto = new ImageIcon(getClass().getResource("/icons/logo_varaschin_agro2.gif"));  
		parametros.put("logo", gto.getImage());
		parametros.put("CodigoPagamento", codPagamento);
	}

	public void geraReciboSafristas(String codigosEmpregados) {
		try {
			parametros.put("ArrayEmpregados", codigosEmpregados);
			
			JasperReport reportSafristas = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/jasper/ReciboSafristas.jasper"));
			JasperPrint printSafristas = JasperFillManager.fillReport(reportSafristas, parametros, con);
			JasperViewer view = new JasperViewer(printSafristas, false);
			view.setTitle("Varaschin Software - Recibos Safristas");
			ImageIcon icon = new ImageIcon(getClass().getResource("/icons/icon_logo_varaschin.gif"));
			view.setIconImage(icon.getImage());
			view.setVisible(true);
			
			con.close();
		} catch (JRException e) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar o relatório! \n Erro: " + e.getMessage() + e.toString(), "Erro Relatório", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Erro no acesso ao banco de dados! \n Erro: " + e.getMessage(), "Erro Banco de dados", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void geraReciboSafristasSacola(String codigosEmpregados) {
		try {
			parametros.put("ArrayEmpregados", codigosEmpregados);
			
			JasperReport reportSafristas = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/jasper/ReciboSafristasSacola.jasper"));
			JasperPrint printSafristas = JasperFillManager.fillReport(reportSafristas, parametros, con);
			JasperViewer view = new JasperViewer(printSafristas, false);
			view.setTitle("Varaschin Software - Recibos Safristas");
			ImageIcon icon = new ImageIcon(getClass().getResource("/icons/icon_logo_varaschin.gif"));
			view.setIconImage(icon.getImage());
			view.setVisible(true);
			
			con.close();
		} catch (JRException e) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar o relatório! \n Erro: " + e.getMessage() + e.toString(), "Erro Relatório", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Erro no acesso ao banco de dados! \n Erro: " + e.getMessage(), "Erro Banco de dados", JOptionPane.ERROR_MESSAGE);
		}
	}
}
