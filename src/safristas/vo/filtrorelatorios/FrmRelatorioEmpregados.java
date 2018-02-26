package safristas.vo.filtrorelatorios;

import impressos.RelatoriosSafristas;
import safristas.bo.safristas.EmpreiteiroBO;
import safristas.dao.safristas.EmpreiteiroDao;
import safristas.vo.FrmMenuGeralMaca;
import safristas.vo.consultcadast.FrmConsultaEmpreiteiro;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyVetoException;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import gerais.vo.FrmRelatorioPai;

public class FrmRelatorioEmpregados extends FrmRelatorioPai {
	private static final long serialVersionUID = 8184591208670585466L;

	protected GridBagConstraints constraints = new GridBagConstraints();

	protected JLabel lblTipo, lblCodEmpreiteiro;
	public JTextField txtCodEmpreiteiro, txtMostraEmpreiteiro;
	protected JRadioButton rBtnGeral, rBtnComFiltros;
	protected JButton btnProcuraEmpreiteiro;
	protected ButtonGroup grupo;
	public EmpreiteiroBO iroBO;
	protected EmpreiteiroDao iroDao = new EmpreiteiroDao();

	public FrmRelatorioEmpregados() {
		setTitle("Relatório - Empregados");
		setSize(getWidth()+60, getHeight());
		lblTitulo.setText("Relatório de Empregados");
		constraints.insets = new Insets(2, 2, 2, 2);
		
		lblTipo = new JLabel("Tipo");
		lblTipo.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblTipo, constraints);
		
		rBtnGeral = new JRadioButton("Geral", true);
		rBtnGeral.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(rBtnGeral, constraints);
		constraints.gridwidth = 1;
		
		rBtnComFiltros = new JRadioButton("Com filtro", false);
		rBtnComFiltros.setFont(f2);
		constraints.gridx = 3;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(rBtnComFiltros, constraints);
		
		grupo = new ButtonGroup();
		grupo.add(rBtnGeral);
		grupo.add(rBtnComFiltros);
		
		lblCodEmpreiteiro = new JLabel("Cód. empreiteiro");
		lblCodEmpreiteiro.setEnabled(false);
		lblCodEmpreiteiro.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblCodEmpreiteiro, constraints);
		
		txtCodEmpreiteiro = new JTextField(4);
		txtCodEmpreiteiro.setEnabled(false);
		txtCodEmpreiteiro.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 1;
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
		
		txtMostraEmpreiteiro = new JTextField(18);
		txtMostraEmpreiteiro.setEditable(false);
		txtMostraEmpreiteiro.setFocusable(false);
		txtMostraEmpreiteiro.setFont(f2);
		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtMostraEmpreiteiro, constraints);
		constraints.gridwidth = 1;

		btnProcuraEmpreiteiro = new JButton("Procurar", new ImageIcon(getClass().getResource("/icons/icon_procurar.gif")));
		btnProcuraEmpreiteiro.setEnabled(false);
		btnProcuraEmpreiteiro.setFocusable(false);
		constraints.ipady = -5;
		constraints.gridx = 4;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(btnProcuraEmpreiteiro, constraints);
		constraints.ipady = 0;
		
		btnProcuraEmpreiteiro.addActionListener(this);
		rBtnGeral.addActionListener(this);
		rBtnComFiltros.addActionListener(this);
	}

	@Override
	public void confirmar() {
		RelatoriosSafristas rel = new RelatoriosSafristas();
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		if (rBtnGeral.isSelected())
			rel.RelatorioEmpregados();
		else
			rel.RelatorioEmpregadosFiltro(Integer.parseInt(txtCodEmpreiteiro.getText()));
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object origem = e.getSource();
		if (origem == rBtnGeral) {
			lblCodEmpreiteiro.setEnabled(false);
			txtMostraEmpreiteiro.setText("");
			txtCodEmpreiteiro.setText("");
			txtCodEmpreiteiro.setEnabled(false);
			btnProcuraEmpreiteiro.setEnabled(false);
		} else if (origem == rBtnComFiltros) {
			lblCodEmpreiteiro.setEnabled(true);
			txtCodEmpreiteiro.setEnabled(true);
			btnProcuraEmpreiteiro.setEnabled(true);
		} else if (origem == btnProcuraEmpreiteiro) {
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
