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

public class RelatorioInsumos {
	static private Connection con;
	static Map<String, Object> parametros = new HashMap<String, Object>();

	public RelatorioInsumos() {
		con = Conexao.conectaBanco();

		ImageIcon gto = new ImageIcon(getClass().getResource("/icons/logo_varaschin_agro2.gif"));  
		parametros.put("logo", gto.getImage());  
	}

	public void relatorioAplicacoes(String ordem) {
		parametros.put("ordem", ordem);
		try {
			JasperReport report = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/jasper/insumos/RelatorioAplicacoes.jasper"));
			geraRelatorio(report);
		} catch (JRException e) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar o relatório! \n Erro: " + e.getMessage(), "Erro Relatório", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void relatorioVisaoGeralLavouras(String ordem) {
		parametros.put("ordem", ordem);
		try {
			JasperReport report = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/jasper/insumos/RelatorioVisaoGeralLavouras.jasper"));
			geraRelatorio(report);
		} catch (JRException e) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar o relatório! \n Erro: " + e.getMessage() + e.getLocalizedMessage(), "Erro Relatório", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void geraRelatorio(JasperReport report) {
		try {
			JasperPrint print = JasperFillManager.fillReport(report, parametros, con);
			JasperViewer view = new JasperViewer(print, false);
			view.setTitle("Varaschin Software - Relatórios");
			ImageIcon icon = new ImageIcon(getClass().getResource("/icons/icon_logo_varaschin.gif"));
			view.setIconImage(icon.getImage());
			view.setVisible(true);
			con.close();
		} catch (JRException e) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar o relatório! \n Erro: " + e.getMessage(), "Erro Relatório", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Erro no acesso ao banco de dados! \n Erro: " + e.getMessage(), "Erro Banco de dados", JOptionPane.ERROR_MESSAGE);
		}
		parametros.clear();
	}
}
