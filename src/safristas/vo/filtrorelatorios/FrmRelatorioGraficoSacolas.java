package safristas.vo.filtrorelatorios;

import impressos.RelatoriosSafristas;
import safristas.bo.EmpregadoBO;
import safristas.dao.EmpregadoDao;
import safristas.vo.FrmMenuGeralMaca;
import safristas.vo.consultcadast.FrmConsultaEmpregado;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyVetoException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import gerais.vo.FrmRelatorioPai;

public class FrmRelatorioGraficoSacolas extends FrmRelatorioPai {
	private static final long serialVersionUID = 8184591208670585466L;

	protected GridBagConstraints constraints = new GridBagConstraints();

	protected JLabel lblDataInicial, lblDataFinal;
	public JTextField txtDataInicial, txtDataFinal;
	protected ButtonGroup grupo;
	public EmpregadoBO adoBO;
	protected EmpregadoDao adoDao = new EmpregadoDao();

	public FrmRelatorioGraficoSacolas() {
		setTitle("Gráfico - Totais Sacolas");
		setSize(getWidth()+60, getHeight());
		lblTitulo.setText("Totais Sacolas");
		constraints.insets = new Insets(2, 2, 2, 2);
		
		lblDataInicial = new JLabel("Data Inicial");
		lblDataInicial.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblDataInicial, constraints);
		
		txtDataInicial = new JTextField(7);
		txtDataInicial.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtDataInicial, constraints);
		
		lblDataFinal = new JLabel("Data Final");
		lblDataFinal.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblDataFinal, constraints);
		
		txtDataFinal = new JTextField(7);
		txtDataFinal.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtDataFinal, constraints);
	}

	@Override
	public void confirmar() {
		RelatoriosSafristas rel = new RelatoriosSafristas();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		LocalDate dataInicial = LocalDate.parse(txtDataInicial.getText(), formatter);
		LocalDate dataFinal = LocalDate.parse(txtDataFinal.getText(), formatter);
		
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		rel.relatorioGraficoTotaisSacolas(dataInicial, dataFinal);
		
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
}
