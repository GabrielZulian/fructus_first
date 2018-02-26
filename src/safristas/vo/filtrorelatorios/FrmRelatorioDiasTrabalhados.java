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

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import gerais.vo.FrmRelatorioPai;

public class FrmRelatorioDiasTrabalhados extends FrmRelatorioPai {
	private static final long serialVersionUID = 8184591208670585466L;

	protected GridBagConstraints constraints = new GridBagConstraints();

	protected JLabel lblTipo, lblCodEmpreiteiro;
	public JTextField txtCodEmpregado, txtMostraEmpregado;
	protected JRadioButton rBtnSafristas, rBtnOutros;
	protected JButton btnProcuraEmpregado;
	protected ButtonGroup grupo;
	public EmpregadoBO adoBO;
	protected EmpregadoDao adoDao = new EmpregadoDao();

	public FrmRelatorioDiasTrabalhados() {
		setTitle("Relatório - Dias de trabalho");
		setSize(getWidth()+60, getHeight());
		lblTitulo.setText("Relatório de Empregados");
		constraints.insets = new Insets(2, 2, 2, 2);
		
		lblTipo = new JLabel("Tipo");
		lblTipo.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblTipo, constraints);
		
		rBtnSafristas = new JRadioButton("Safristas", true);
		rBtnSafristas.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(rBtnSafristas, constraints);
		constraints.gridwidth = 1;
		
		rBtnOutros = new JRadioButton("Outros", false);
		rBtnOutros.setFont(f2);
		constraints.gridx = 3;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(rBtnOutros, constraints);
		
		grupo = new ButtonGroup();
		grupo.add(rBtnSafristas);
		grupo.add(rBtnOutros);
		
		lblCodEmpreiteiro = new JLabel("Cód. empregado");
		lblCodEmpreiteiro.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblCodEmpreiteiro, constraints);
		
		txtCodEmpregado = new JTextField(4);
		txtCodEmpregado.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtCodEmpregado, constraints);
		txtCodEmpregado.addFocusListener(new FocusAdapter() {
			
			@Override
			public void focusLost(FocusEvent e) {
				adoBO = new EmpregadoBO();
				adoBO = adoDao.consultaPorCodigo(Integer.parseInt(txtCodEmpregado.getText())).get(0);
				
				txtMostraEmpregado.setText(adoBO.getNome());
			}
		});
		
		txtMostraEmpregado = new JTextField(18);
		txtMostraEmpregado.setEditable(false);
		txtMostraEmpregado.setFocusable(false);
		txtMostraEmpregado.setFont(f2);
		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtMostraEmpregado, constraints);
		constraints.gridwidth = 1;

		btnProcuraEmpregado = new JButton("Procurar", new ImageIcon(getClass().getResource("/icons/icon_procurar.gif")));
		btnProcuraEmpregado.setFocusable(false);
		constraints.ipady = -5;
		constraints.gridx = 4;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(btnProcuraEmpregado, constraints);
		constraints.ipady = 0;
		
		btnProcuraEmpregado.addActionListener(this);
		rBtnSafristas.addActionListener(this);
		rBtnOutros.addActionListener(this);
	}

	@Override
	public void confirmar() {
		RelatoriosSafristas rel = new RelatoriosSafristas();
		int codEmpregado = Integer.parseInt(txtCodEmpregado.getText());
		
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		if (rBtnSafristas.isSelected())
			rel.RelatorioDiasTrabSafristas(codEmpregado);
		else
			rel.RelatorioDiasTrabSafristas(codEmpregado);
		
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object origem = e.getSource();
		if (origem == rBtnSafristas) {
			txtMostraEmpregado.setText("");
			txtCodEmpregado.setText("");
			txtCodEmpregado.requestFocus();
		} else if (origem == rBtnOutros) {
			txtMostraEmpregado.setText("");
			txtCodEmpregado.setText("");
			txtCodEmpregado.requestFocus();
		} else if (origem == btnProcuraEmpregado) {
			FrmConsultaEmpregado fr = new FrmConsultaEmpregado(this);
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
