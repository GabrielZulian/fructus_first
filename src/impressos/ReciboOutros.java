package impressos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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

public class ReciboOutros {
	private Connection con;
	Map<String, Object> parametros = new HashMap<String, Object>();

	public ReciboOutros(int codPagamento, ArrayList<Integer> codVeiculos) {
		con = Conexao.conectaBanco();

		ImageIcon gto = new ImageIcon(getClass().getResource("/icons/logo_varaschin_agro2.gif"));  
		parametros.put("logo", gto.getImage());
		parametros.put("CodigoPagamento", codPagamento);
	}

	public void geraRecibo() {
		try {
			JasperReport report = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/jasper/ReciboOutros.jasper"));
			JasperPrint print = JasperFillManager.fillReport(report, parametros, con);
			JasperViewer.viewReport(print, false);
			JasperViewer view = new JasperViewer(print, false);
			view.setTitle("Varaschin Software - Recibos");
			ImageIcon icon = new ImageIcon(getClass().getResource("/icons/icon_logo_varaschin.gif"));
			view.setIconImage(icon.getImage());
			view.setVisible(true);
			con.close();
		} catch (JRException e) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar o relat�rio! \n Erro: " + e.getMessage(), "Erro Relat�rio", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Erro no acesso ao banco de dados! \n Erro: " + e.getMessage(), "Erro Banco de dados", JOptionPane.ERROR_MESSAGE);
		}
	}
}
