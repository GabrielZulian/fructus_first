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
			JOptionPane.showMessageDialog(null, "Erro ao gerar o relat�rio! \n Erro: " + e.getMessage(), "Erro Relat�rio", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void RelatorioEmpreiteiros() {
		try {
			JasperReport report = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/jasper/RelatorioEmpreiteiros.jasper"));
			geraRelatorio(report);
		} catch (JRException e) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar o relat�rio! \n Erro: " + e.getMessage(), "Erro Relat�rio", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void RelatorioEmpregados() {
		try {
			JasperReport report = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/jasper/RelatorioEmpregados.jasper"));
			geraRelatorio(report);
		} catch (JRException e) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar o relat�rio! \n Erro: " + e.getMessage(), "Erro Relat�rio", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void RelatorioEmpregadosFiltro(int codigo) {
		try {
			parametros.put("cod", codigo);
			JasperReport report = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/jasper/RelatorioEmpregadosFiltro.jasper"));
			geraRelatorio(report);
		} catch (JRException e) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar o relat�rio! \n Erro: " + e.getMessage(), "Erro Relat�rio", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void RelatorioEmpregadoVisaoGeral() {
		try {
			JasperReport report = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/jasper/RelatorioEmpregadosGeral.jasper"));
			geraRelatorio(report);
		} catch (JRException e) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar o relat�rio! \n Erro: " + e.getMessage(), "Erro Relat�rio", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void RelatorioPagamentoSafristas(int codPgto) {
		parametros.put("CodPagamento", codPgto);
		try {
			JasperReport report = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/jasper/RelatorioPagamentos.jasper"));
			geraRelatorio(report);
		} catch (JRException e) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar o relat�rio! \n Erro: " + e.getMessage(), "Erro Relat�rio", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void RelatorioPagamentoOutros(int codPgto) {
		parametros.put("CodPagamento", codPgto);
		try {
			JasperReport report = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/jasper/RelatorioPagamentosOutros.jasper"));
			geraRelatorio(report);
		} catch (JRException e) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar o relat�rio! \n Erro: " + e.getMessage(), "Erro Relat�rio", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void RelatorioDiasTrabSafristas(int codEmpregado) {
		parametros.put("CodPagamento", codEmpregado);
		try {
			JasperReport report = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/jasper/RelatorioDiasTrabalhadosSafristas.jasper"));
			geraRelatorio(report);
		} catch (JRException e) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar o relat�rio! \n Erro: " + e.getMessage(), "Erro Relat�rio", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void geraRelatorio(JasperReport report) {
		try {
			JasperPrint print = JasperFillManager.fillReport(report, parametros, con);
			JasperViewer view = new JasperViewer(print, false);
			view.setTitle("Varaschin Software - Relat�rios");
			ImageIcon icon = new ImageIcon(getClass().getResource("/icons/icon_logo_varaschin.gif"));
			view.setIconImage(icon.getImage());
			view.setVisible(true);
			con.close();
		} catch (JRException e) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar o relat�rio! \n Erro: " + e.getMessage(), "Erro Relat�rio", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Erro no acesso ao banco de dados! \n Erro: " + e.getMessage(), "Erro Banco de dados", JOptionPane.ERROR_MESSAGE);
		}
		parametros.clear();
	}
}
