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

public class RelatoriosPragas {
	static private Connection con;
	static Map<String, Object> parametros = new HashMap<String, Object>();

	public RelatoriosPragas() {
		con = Conexao.conectaBanco();

		ImageIcon gto = new ImageIcon(getClass().getResource("/icons/logo_varaschin_agro2.gif"));  
		parametros.put("logo", gto.getImage());  
	}

	public void RelatorioIndiceGeral(char tipoPraga) {
		JasperReport report = null;
		try {
			switch (tipoPraga) {

			case 'G': report = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/jasper/RelatorioPrIndiceGrapholitaGrafico.jasper"));
			break;

			case 'B': report = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/jasper/RelatorioPrIndiceBonagotaGrafico.jasper"));
			break;

			case 'M': report = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/jasper/RelatorioPrIndiceMoscaGrafico.jasper"));
			break;
			}

			GeraRelatorio(report);
		} catch (JRException e) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar o relatório! \n Erro: " + e.getMessage(), "Erro Relatório", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void RelatorioIndicePorQuadra(String tipoPraga, int nroQuadra) {
		try {
			parametros.put("tipoPraga", tipoPraga);
			parametros.put("nroQuadra", nroQuadra);
			JasperReport report = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/jasper/RelatorioPrIndiceQuadraGrafico.jasper"));

			GeraRelatorio(report);
		} catch (JRException e) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar o relatório! \n Erro: " + e.getMessage(), "Erro Relatório", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void GeraRelatorio(JasperReport report) {
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
