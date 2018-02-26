package safristas.vo.filtrorelatorios;

import impressos.Planilhas;
import safristas.bo.safristas.EmpreiteiroBO;
import safristas.dao.safristas.EmpreiteiroDao;
import safristas.vo.FrmMenuGeralMaca;
import safristas.vo.consultcadast.FrmConsultaEmpreiteiro;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyVetoException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import org.joda.time.DateTime;
import org.joda.time.Days;

import gerais.vo.FrmRelatorioPai;

public class FrmPlanilhaColetaDias extends FrmRelatorioPai {
	private static final long serialVersionUID = -9181747584561580375L;
	
	protected JLabel lblDataInicial, lblDataFinal, lblCodigoEmpreiteiro, lblMaxDias;
	public JTextField txtCodEmpreiteiro, txtMostraEmpreiteiro;
	protected JFormattedTextField txtDataInicial, txtDataFinal;
	protected JButton btnProcuraEmpreiteiro;
	private Locale brasil = new Locale("pt", "BR");
	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy", brasil);
	private MaskFormatter mascaraData;
	protected EmpreiteiroDao iroDao = new EmpreiteiroDao();
	protected EmpreiteiroBO iroBO;

	public FrmPlanilhaColetaDias() {
		setTitle("Planilha p/ coleta de dias");
		lblTitulo.setText("Planilha p/ coleta de dias de trabalho");
		setSize(getWidth()+40, getHeight()-50);

		lblDataInicial = new JLabel("Data Inicial");
		lblDataInicial.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblDataInicial, constraints);
		
		try {
			mascaraData = new MaskFormatter("##/##/####");
		} catch (ParseException e) {}
		
		txtDataInicial = new JFormattedTextField(mascaraData);
		txtDataInicial.setColumns(7);
		txtDataInicial.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtDataInicial, constraints);
		constraints.gridwidth = 1;
		
		lblDataFinal = new JLabel("Data Final");
		lblDataFinal.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblDataFinal, constraints);
		
		txtDataFinal = new JFormattedTextField(mascaraData);
		txtDataFinal.setColumns(7);
		txtDataFinal.setFont(f2);
		constraints.gridwidth = 2;
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtDataFinal, constraints);
		constraints.gridwidth = 1;
		
		lblMaxDias = new JLabel("Máx. 15 dias");
		constraints.gridx = 3;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(lblMaxDias, constraints);
		
		lblCodigoEmpreiteiro = new JLabel("Código Empreiteiro");
		lblCodigoEmpreiteiro.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblCodigoEmpreiteiro, constraints);
		
		txtCodEmpreiteiro = new JTextField(4);
		txtCodEmpreiteiro.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtCodEmpreiteiro, constraints);
		txtCodEmpreiteiro.addFocusListener(new FocusAdapter() {
			
			@Override
			public void focusLost(FocusEvent e) {
				iroBO = new EmpreiteiroBO();
				iroBO = iroDao.consultaPorCodigo(Integer.parseInt(txtCodEmpreiteiro.getText())).get(0);
				
				txtMostraEmpreiteiro.setText(iroBO.getNome());
			}
		});
		
		txtMostraEmpreiteiro = new JTextField(15);
		txtMostraEmpreiteiro.setFont(f2);
		txtMostraEmpreiteiro.setEditable(false);
		txtMostraEmpreiteiro.setFocusable(false);
		constraints.gridx = 2;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtMostraEmpreiteiro, constraints);
		constraints.gridwidth = 1;
		
		btnProcuraEmpreiteiro = new JButton("Procurar", new ImageIcon(getClass().getResource("/icons/icon_procurar.gif")));
		btnProcuraEmpreiteiro.setFocusable(false);
		constraints.ipady = -5;
		constraints.gridx = 4;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(btnProcuraEmpreiteiro, constraints);
		constraints.ipady = 0;
		
		btnProcuraEmpreiteiro.addActionListener(this);
	}

	@Override
	public void confirmar() {
		try {
			DateTime dataInicial = new DateTime(df.parse(txtDataInicial.getText()));
			DateTime dataFinal = new DateTime(df.parse(txtDataFinal.getText()));
			int codigoEmpreiteiro = Integer.parseInt(txtCodEmpreiteiro.getText());
			
			if (Days.daysBetween(dataInicial, dataFinal).getDays() < 0) {
				JOptionPane.showMessageDialog(this, "Período incorreto - Data final deve ser maior que a Data Inicial!", "Datas incorretas", JOptionPane.ERROR_MESSAGE);
				txtDataFinal.requestFocus();
				txtDataFinal.selectAll();
				return;
			}
			
			if (Days.daysBetween(dataInicial, dataFinal).getDays() > 14) {
				JOptionPane.showMessageDialog(this, "Período deve ser de no máximo 15 dias!", "Datas incorretas", JOptionPane.ERROR_MESSAGE);
				txtDataFinal.requestFocus();
				txtDataFinal.selectAll();
				return;
			}
		
			Planilhas planilha = new Planilhas();
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			planilha.geraPlanilhaColetaDiasTrabalho(dataInicial, dataFinal, codigoEmpreiteiro);
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(this, "Data Incorreta!", "Datas incorretas", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	public void actionPerformed(ActionEvent e) {
		Object origem = e.getSource();
		super.actionPerformed(e);
		if (origem == btnProcuraEmpreiteiro) {
			FrmConsultaEmpreiteiro fr = new FrmConsultaEmpreiteiro(this);
			fr.setVisible(true);
			getDesktopPane().add(fr);
			try {
				fr.setSelected(true);
			}
			catch (PropertyVetoException exc) { }
			FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
		}
	}
}
