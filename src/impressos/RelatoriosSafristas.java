package impressos;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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

public class RelatoriosSafristas {
	static private Connection con;
	static Map<String, Object> parametros = new HashMap<String, Object>();

	public RelatoriosSafristas() {
		con = Conexao.conectaBanco();

		ImageIcon gto = new ImageIcon(getClass().getResource("/icons/logo_varaschin_agro2.gif"));  
		parametros.put("logo", gto.getImage());  
	}

	public void RelatorioEmpregadores() {
		try {
			JasperReport report = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/jasper/RelatorioEmpregadores.jasper"));
			geraRelatorio(report);
		} catch (JRException e) {
			showErrorMessage(e);
		}
	}

	public void RelatorioEmpreiteiros() {
		try {
			JasperReport report = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/jasper/RelatorioEmpreiteiros.jasper"));
			geraRelatorio(report);
		} catch (JRException e) {
			showErrorMessage(e);
		}
	}

	public void RelatorioEmpregados() {
		try {
			JasperReport report = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/jasper/RelatorioEmpregados.jasper"));
			geraRelatorio(report);
		} catch (JRException e) {
			showErrorMessage(e);
		}
	}
	
	public void RelatorioEmpregadosFiltro(int codigo) {
		try {
			parametros.put("cod", codigo);
			JasperReport report = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/jasper/RelatorioEmpregadosFiltro.jasper"));
			geraRelatorio(report);
		} catch (JRException e) {
			showErrorMessage(e);
		}
	}
	
	public void RelatorioEmpregadoVisaoGeral() {
		try {
			JasperReport report = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/jasper/RelatorioEmpregadosGeral.jasper"));
			geraRelatorio(report);
		} catch (JRException e) {
			showErrorMessage(e);
		}
	}
	
	public void RelatorioPagamentoSafristas(int codPgto) {
		parametros.put("CodPagamento", codPgto);
		try {
			JasperReport report = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/jasper/RelatorioPagamentos.jasper"));
			geraRelatorio(report);
		} catch (JRException e) {
			showErrorMessage(e);
		}
	}
	
	public void RelatorioPagamentoOutros(int codPgto) {
		parametros.put("CodPagamento", codPgto);
		try {
			JasperReport report = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/jasper/RelatorioPagamentosOutros.jasper"));
			geraRelatorio(report);
		} catch (JRException e) {
			showErrorMessage(e);
		}
	}
	
	public void RelatorioDiasTrabSafristas(int codEmpregado) {
		parametros.put("CodPagamento", codEmpregado);
		try {
			JasperReport report = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/jasper/RelatorioDiasTrabalhadosSafristas.jasper"));
			geraRelatorio(report);
		} catch (JRException e) {
			showErrorMessage(e);
		}
	}
	
	public void relatorioGraficoTotaisSacolas(LocalDate dataInicial, LocalDate dataFinal) {
		parametros.put("dataInicial", Date.from(dataInicial.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		parametros.put("dataFinal", Date.from(dataFinal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		try {
			JasperReport report = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/jasper/RelatorioTotaisSacolas.jasper"));
			geraRelatorio(report);
		} catch (JRException e) {
			showErrorMessage(e);
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
			showErrorMessage(e);
		} catch (SQLException e) {
			showSQLErrorMessage(e);
		}
		parametros.clear();
	}
	
	private void showSQLErrorMessage(SQLException e) {
		JOptionPane.showMessageDialog(null, "Erro ao gerar o relatório! \n Erro: " + e.getMessage(), "Erro Relatório", JOptionPane.ERROR_MESSAGE);
	}
	
	private void showErrorMessage(JRException e) {
		JOptionPane.showMessageDialog(null, "Erro ao gerar o relatório! \n Erro: " + e.getMessage(), "Erro Relatório", JOptionPane.ERROR_MESSAGE);
	}
}
