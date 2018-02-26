package impressos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.joda.time.DateTime;
import org.joda.time.Days;

import gerais.dao.Conexao;

public class Planilhas {
	private Connection con;
	Map<String, Object> parametros = new HashMap<String, Object>();

	public Planilhas() {
		con = Conexao.conectaBanco();

		ImageIcon gto = new ImageIcon(getClass().getResource("/icons/logo_varaschin_agro2.gif"));  
		parametros.put("logo", gto.getImage());  
	}

	public void geraPlanilhaColetaDiasTrabalho(DateTime dataInicial, DateTime dataFinal, int codEmpreiteiro) {
		parametros.put("CodEmpreiteiro", codEmpreiteiro);
		
		ArrayList<Date> datas = new ArrayList<Date>();
		int dias = Days.daysBetween(dataInicial, dataFinal).getDays();
		for (int i=0; i <= dias; i++) {
			Date d = dataInicial.plusDays(i).toDate();
		    datas.add(d);
		}
		
		while(datas.size() <= 14)  {
			datas.add(null);
		}
		
	    parametros.put("DataInicial", dataInicial.toDate());
	    parametros.put("DataFinal", dataFinal.toDate());
		parametros.put("Datas", datas);
		
		try {
			JasperReport report = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/jasper/ColetaDias.jasper"));
			GeraRelatorio(report);
		} catch (JRException e) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar o relatório! \nErro: " + e.getMessage(), "Erro Relatório", JOptionPane.ERROR_MESSAGE);
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
			JOptionPane.showMessageDialog(null, "Erro ao gerar o relatório! \nErro: " + e.getMessage(), "Erro Relatório", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Erro no acesso ao banco de dados!\n Erro: " + e.getMessage(), "Erro Banco de dados", JOptionPane.ERROR_MESSAGE);
		}
	}
}
