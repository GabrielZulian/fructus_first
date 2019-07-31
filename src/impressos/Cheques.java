package impressos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import safristas.bo.DadosChequeBO;

public class Cheques {
	Map<String, Object> parametros = new HashMap<String, Object>();

	public Cheques() {

	}
	
	public void geraCheques(List<DadosChequeBO> chequeBO) {
		try {
			JasperReport reportSafristas = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/jasper/Cheques.jasper"));
			JasperPrint printSafristas = JasperFillManager.fillReport(reportSafristas, parametros, new JRBeanCollectionDataSource(chequeBO));
			JasperViewer view = new JasperViewer(printSafristas, false);
			view.setTitle("Varaschin Software - Cheques");
			ImageIcon icon = new ImageIcon(getClass().getResource("/icons/icon_logo_varaschin.gif"));
			view.setIconImage(icon.getImage());
			view.setVisible(true);
		} catch (JRException e) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar o relatório! \n Erro: " + e.getMessage(), "Erro Relatório", JOptionPane.ERROR_MESSAGE);
		}
	}
}