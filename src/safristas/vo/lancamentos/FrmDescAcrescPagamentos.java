package safristas.vo.lancamentos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import safristas.bo.AdiantamentoBO;
import safristas.dao.AdiantamentoDao;
import safristas.vo.FrmMenuGeralMaca;

public class FrmDescAcrescPagamentos extends JInternalFrame implements ActionListener {
	private static final long serialVersionUID = 6373737040899714397L;
	
	protected GridBagConstraints constraints = new GridBagConstraints();
	DecimalFormat decimal = new DecimalFormat( "#,##0.00" );
	protected JLabel lblTitulo, lblImg, lblCodigo, lblNome, lblCheckBox,lblValorInicial, lblValorDesconto,
	lblValorDescontoHAT, lblValorDecimoFerias, lblHistorico, lblCodAdiantamento, lblValorFinal;
	protected JTextField txtCodigo, txtNome, txtValorInicial, txtValorDesconto, txtValorDescontoHAT,
	txtValorDecimoFerias, txtCodAdiantamento, txtMostraAdiantamento, txtValorFinal;
	protected JTextArea txtHistorico;
	protected JButton btnConfirmar, btnCancelar, btnProcurarAdiantamento;
	protected JCheckBox cBoxAdiantamento;
	protected DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	protected char tipo;
	protected int codAdiant;

	protected JPanel painelMeio = new JPanel();
	protected Font f = new Font("Arial", Font.BOLD, 14);
	protected Font f2 = new Font("Tahoma", Font.PLAIN, 14);

	protected FrmPagamentoTotal pgtoTotal = null;
	protected FrmPagamentoTotalOutros pgtoTotalOutros = null;
	protected AdiantamentoDao adiantDao = new AdiantamentoDao();
	protected AdiantamentoBO adiantBO;

	public FrmDescAcrescPagamentos (FrmPagamentoTotalOutros pgtoTotalOutros, char tipo) {
		this();
		this.pgtoTotalOutros = pgtoTotalOutros;
		this.tipo = tipo;
		this.codAdiant = Integer.parseInt(pgtoTotalOutros.modelo.getValueAt(pgtoTotalOutros.tabela.getSelectedRow(), 12).toString());
		txtCodigo.setText(pgtoTotalOutros.modelo.getValueAt(pgtoTotalOutros.tabela.getSelectedRow(), 1).toString());
		txtNome.setText(pgtoTotalOutros.modelo.getValueAt(pgtoTotalOutros.tabela.getSelectedRow(), 2).toString());
		txtValorDesconto.setText(pgtoTotalOutros.decimal.format(pgtoTotalOutros.modelo.getValueAt(pgtoTotalOutros.tabela.getSelectedRow(), 5)));
		txtHistorico.setText(pgtoTotalOutros.modelo.getValueAt(pgtoTotalOutros.tabela.getSelectedRow(), 11).toString());
		txtValorDescontoHAT.setEnabled(false);
		txtValorDecimoFerias.setEnabled(false);
		if (codAdiant != 0) {
			cBoxAdiantamento.doClick();
			txtCodAdiantamento.setText(String.valueOf(codAdiant));
			txtMostraAdiantamento.setText(pgtoTotalOutros.modelo.getValueAt(pgtoTotalOutros.tabela.getSelectedRow(), 11).toString());
		}
	}

	public FrmDescAcrescPagamentos (FrmPagamentoTotal pgtoTotal, char tipo) {
		this();
		this.pgtoTotal = pgtoTotal;
		this.tipo = tipo;
		if (tipo == 'A') {
			this.codAdiant = Integer.parseInt(pgtoTotal.modelo.getValueAt(pgtoTotal.tabela.getSelectedRow(), FrmPagamentoTotal.COLUNA_CODADIANTAMENTO).toString());
			txtCodigo.setText(pgtoTotal.modelo.getValueAt(pgtoTotal.tabela.getSelectedRow(), FrmPagamentoTotal.COLUNA_CODIGO).toString());
			txtNome.setText(pgtoTotal.modelo.getValueAt(pgtoTotal.tabela.getSelectedRow(), FrmPagamentoTotal.COLUNA_NOME).toString());
			txtValorInicial.setText(decimal.format(pgtoTotal.modelo.getValueAt(pgtoTotal.tabela.getSelectedRow(), FrmPagamentoTotal.COLUNA_VALOR)));
			txtValorDesconto.setText(decimal.format(pgtoTotal.modelo.getValueAt(pgtoTotal.tabela.getSelectedRow(), FrmPagamentoTotal.COLUNA_DESCONTO)));
			txtValorDescontoHAT.setText(decimal.format(pgtoTotal.modelo.getValueAt(pgtoTotal.tabela.getSelectedRow(), FrmPagamentoTotal.COLUNA_DESCHAT)));
			txtValorDecimoFerias.setText(decimal.format(pgtoTotal.modelo.getValueAt(pgtoTotal.tabela.getSelectedRow(), FrmPagamentoTotal.COLUNA_ACRESCIMO)));
			txtValorFinal.setText(decimal.format(pgtoTotal.modelo.getValueAt(pgtoTotal.tabela.getSelectedRow(), FrmPagamentoTotal.COLUNA_VALORTOTAL)));
			txtHistorico.setText(pgtoTotal.modelo.getValueAt(pgtoTotal.tabela.getSelectedRow(), FrmPagamentoTotal.COLUNA_OBDESCONTO).toString());
			if (codAdiant != 0) {
				cBoxAdiantamento.doClick();
				txtCodAdiantamento.setText(String.valueOf(codAdiant));
				txtMostraAdiantamento.setText(pgtoTotal.modelo.getValueAt(pgtoTotal.tabela.getSelectedRow(), FrmPagamentoTotal.COLUNA_OBDESCONTO).toString());
			}
		} else if (tipo == 'I') {
			this.codAdiant = pgtoTotal.codAdiantIro;
			txtCodigo.setText(pgtoTotal.txtCodEmpreiteiro.getText());
			txtNome.setText(pgtoTotal.txtMostraEmpreiteiro.getText());
			txtValorDesconto.setText(pgtoTotal.txtValorDescontoIro.getText());
			txtHistorico.setText(pgtoTotal.txtMostraDescontoIro.getText());
			txtValorDescontoHAT.setEnabled(false);
			txtValorDecimoFerias.setEnabled(false);
			if (codAdiant != 0) {
				cBoxAdiantamento.doClick();
				txtCodAdiantamento.setText(String.valueOf(codAdiant));
				txtMostraAdiantamento.setText(pgtoTotal.txtMostraDescontoIro.getText());
			}
		}
	}

	public FrmDescAcrescPagamentos() {
		super("Desconto / Acréscimo",false,true,false,true);

		setFrameIcon(new ImageIcon(getClass().getResource("/icons/icon_logo_varaschin.gif")));
		setResizable(false);
		setSize(610, 440);

		JPanel painelGeral = new JPanel();
		painelGeral.setLayout(new BorderLayout());

		JPanel painelCima = new JPanel();
		painelCima.setLayout(new GridBagLayout());

		painelMeio.setLayout(new GridBagLayout());

		JPanel painelBaixo = new JPanel();
		painelBaixo.setLayout(new GridBagLayout());

		constraints.insets = new Insets(3, 3, 3, 3);

		lblTitulo = new JLabel("Lançar Descontos e Acréscimos");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		painelCima.add(lblTitulo, constraints);

		painelGeral.add(painelCima, BorderLayout.NORTH);

		//-----------------------------------------------
		lblCodigo = new JLabel("Código");
		lblCodigo.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblCodigo, constraints);

		txtCodigo = new JTextField(4);
		txtCodigo.setFont(f2);
		txtCodigo.setFocusable(false);
		txtCodigo.setEditable(false);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtCodigo, constraints);
		constraints.gridwidth = 1;

		lblNome = new JLabel("Nome");
		lblNome.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblNome, constraints);

		txtNome = new JTextField(25);
		txtNome.setFont(f2);
		txtNome.setFocusable(false);
		txtNome.setEditable(false);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 4;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtNome, constraints);
		constraints.gridwidth = 1;
		
		lblValorInicial = new JLabel("Valor Inicial");
		lblValorInicial.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblValorInicial, constraints);
		
		txtValorInicial = new JTextField(8);
		txtValorInicial.setFont(f2);
		txtValorInicial.setFocusable(false);
		txtValorInicial.setEditable(false);
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtValorInicial, constraints);
		constraints.gridwidth = 1;

		lblCheckBox = new JLabel("Adiantamento pré-lançado");
		lblCheckBox.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblCheckBox, constraints);

		cBoxAdiantamento = new JCheckBox("",false);
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(cBoxAdiantamento, constraints);

		lblCodAdiantamento = new JLabel("Cód. Adiantamento");
		lblCodAdiantamento.setFont(f2);
		lblCodAdiantamento.setEnabled(false);
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblCodAdiantamento, constraints);

		txtCodAdiantamento = new JTextField(4);
		txtCodAdiantamento.setText("0");
		txtCodAdiantamento.setEnabled(false);
		txtCodAdiantamento.setFont(f2);
		txtCodAdiantamento.setFocusable(false);
		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtCodAdiantamento, constraints);

		txtCodAdiantamento.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				int codAdiant = Integer.parseInt(txtCodAdiantamento.getText());
				adiantBO = new AdiantamentoBO();
				adiantBO = adiantDao.consultaPorCodigoNPagos(codAdiant).get(0);

				txtMostraAdiantamento.setText(df.format(adiantBO.data.toDate()) + " - " + decimal.format(adiantBO.getValor()));
				txtValorDesconto.setText(decimal.format(adiantBO.getValor()));
				txtHistorico.setText("Adiantamento " + df.format(adiantBO.data.toDate()));
			}
		});

		txtMostraAdiantamento = new JTextField(15);
		txtMostraAdiantamento.setFont(f2);
		txtMostraAdiantamento.setEnabled(false);
		txtMostraAdiantamento.setEditable(false);
		txtMostraAdiantamento.setFocusable(false);
		constraints.gridx = 2;
		constraints.gridy = 4;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtMostraAdiantamento, constraints);
		constraints.gridwidth = 1;

		btnProcurarAdiantamento = new JButton("Procurar", new ImageIcon(getClass().getResource("/icons/icon_procurar.gif")));
		btnProcurarAdiantamento.setEnabled(false);
		constraints.gridx = 4;
		constraints.gridy = 4;
		constraints.ipady = -5;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(btnProcurarAdiantamento, constraints);
		constraints.ipady = 0;

		lblValorDesconto = new JLabel("- Valor desconto");
		lblValorDesconto.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblValorDesconto, constraints);

		txtValorDesconto = new JTextField(8);
		txtValorDesconto.setFont(f2);
		txtValorDesconto.setBackground(new Color(250, 200, 200));
		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtValorDesconto, constraints);
		constraints.gridwidth = 1;
		txtValorDesconto.addFocusListener(new FocusAdapter() {
			
			@Override
			public void focusLost(FocusEvent e) {
				txtHistorico.setText("ADIANTAMENTO");
			}
		});
		
		lblValorDescontoHAT = new JLabel("- Valor desc. Hab./Ali./Tra.");
		lblValorDescontoHAT.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 6;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblValorDescontoHAT, constraints);
		
		txtValorDescontoHAT = new JTextField(8);
		txtValorDescontoHAT.setFont(f2);
		txtValorDescontoHAT.setBackground(new Color(250, 200, 200));
		constraints.gridx = 1;
		constraints.gridy = 6;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtValorDescontoHAT, constraints);
		constraints.gridwidth = 1;
				
		lblValorDecimoFerias = new JLabel("+ Valor 13°/Férias");
		lblValorDecimoFerias.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 7;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblValorDecimoFerias, constraints);
		
		txtValorDecimoFerias = new JTextField(8);
		txtValorDecimoFerias.setFont(f2);
		txtValorDecimoFerias.setBackground(new Color(200, 250, 200));
		constraints.gridx = 1;
		constraints.gridy = 7;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtValorDecimoFerias, constraints);
		constraints.gridwidth = 1;
		txtValorDecimoFerias.addFocusListener(new FocusAdapter() {
			
			@Override
			public void focusLost(FocusEvent e) {
				calculaValorFinal();
			}
		});

		lblHistorico = new JLabel("Histórico desc.");
		lblHistorico.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 8;
		constraints.anchor = GridBagConstraints.NORTHEAST;
		painelMeio.add(lblHistorico, constraints);

		txtHistorico = new JTextArea(4, 30);
		txtHistorico.setFont(f2);
		txtHistorico.setText("Adiantamento");
		constraints.gridx = 1;
		constraints.gridy = 8;
		constraints.gridwidth = 4;
		constraints.anchor = GridBagConstraints.WEST;
		JScrollPane rolagemTxt = new JScrollPane(txtHistorico);
		painelMeio.add(rolagemTxt, constraints);
		constraints.gridwidth = 1;
		
		lblValorFinal = new JLabel("Valor Final");
		lblValorFinal.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 9;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblValorFinal, constraints);
		
		txtValorFinal = new JTextField(8);
		txtValorFinal.setFont(f2);
		txtValorFinal.setEditable(false);
		txtValorFinal.setFocusable(false);
		constraints.gridx = 1;
		constraints.gridy = 9;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtValorFinal, constraints);
		constraints.gridwidth = 1;
		

		painelMeio.setBorder(BorderFactory.createEtchedBorder());
		painelGeral.add(painelMeio, BorderLayout.CENTER);

		//-----------------------------------------------

		constraints.ipadx = 20;

		btnConfirmar = new JButton("Confirmar (F1)", new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelBaixo.add(btnConfirmar, constraints);
		btnConfirmar.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F1"), "KEY_F1");
		btnConfirmar.getActionMap().put("KEY_F1", new AbstractAction() {
			private static final long serialVersionUID = 5779522274432064950L;

			public void actionPerformed(ActionEvent evt) {
				btnConfirmar.doClick();
		    }
		});

		btnCancelar = new JButton("Cancelar (F4)", new ImageIcon(getClass().getResource("/icons/icon_cancelar.gif")));
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelBaixo.add(btnCancelar, constraints);
		btnCancelar.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F4"), "KEY_F4");
		btnCancelar.getActionMap().put("KEY_F4", new AbstractAction() {
			private static final long serialVersionUID = -7547882810266193631L;

			public void actionPerformed(ActionEvent evt) {
				btnCancelar.doClick();
		    }
		});

		painelGeral.add(painelBaixo, BorderLayout.SOUTH);

		constraints.ipadx = 0;

		Container p = getContentPane();
		p.add(painelGeral);

		cBoxAdiantamento.addActionListener(this);
		btnProcurarAdiantamento.addActionListener(this);
		btnConfirmar.addActionListener(this);
		btnCancelar.addActionListener(this);
		
		addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameActivated(InternalFrameEvent e) {
				txtValorDesconto.requestFocusInWindow();
			}
		});

		KeyboardFocusManager.getCurrentKeyboardFocusManager()
		.addPropertyChangeListener("permanentFocusOwner", new PropertyChangeListener()
		{
			public void propertyChange(final PropertyChangeEvent e)
			{
				if (e.getNewValue() instanceof JTextField)
				{
					SwingUtilities.invokeLater(new Runnable()
					{
						public void run()
						{
							JTextField textField = (JTextField)e.getNewValue();
							textField.selectAll();
						}
					});
				}
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object origem = e.getSource();
		if (origem == cBoxAdiantamento) {
			if (cBoxAdiantamento.isSelected()) {
				lblCodAdiantamento.setEnabled(true);
				txtCodAdiantamento.setEnabled(true);
				txtCodAdiantamento.setFocusable(true);
				txtMostraAdiantamento.setEnabled(true);
				btnProcurarAdiantamento.setEnabled(true);
				txtValorDesconto.setEditable(false);
			} else {
				txtCodAdiantamento.setText("0");
				txtMostraAdiantamento.setText("");
				lblCodAdiantamento.setEnabled(false);
				txtCodAdiantamento.setEnabled(false);
				txtCodAdiantamento.setFocusable(false);
				txtMostraAdiantamento.setEnabled(false);
				btnProcurarAdiantamento.setEnabled(false);
				txtValorDesconto.setEditable(true);
				adiantDao.desatualizaPago(codAdiant);
			}
		} else if (origem == btnProcurarAdiantamento) {
			FrmConsultaAdiantamentos fr = new FrmConsultaAdiantamentos(this);
			fr.setVisible(true);
			getDesktopPane().add(fr);
			try {
				fr.setSelected(true);
			}
			catch (PropertyVetoException exc) { }
			FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
		} else if (origem == btnConfirmar) {
			txtValorDesconto.setText(txtValorDesconto.getText().replace(".", ""));
			txtValorDescontoHAT.setText(txtValorDescontoHAT.getText().replace(".", ""));
			txtValorDecimoFerias.setText(txtValorDecimoFerias.getText().replace(".", ""));
			
			double valorDesconto = 0.0;
			double valorDescontoHAT = 0.0;
			double valorDecimoFerias = 0.0;
			
			if (!txtValorDesconto.getText().equals("")) {
				valorDesconto = Double.parseDouble(txtValorDesconto.getText().trim().replace(',', '.'));
			}
			
			if (valorDesconto == 0) {
				txtHistorico.setText("");
			}
			
			if (!txtValorDescontoHAT.getText().equals("")) {
				valorDescontoHAT = Double.parseDouble(txtValorDescontoHAT.getText().trim().replace(',', '.'));
			}
			
			if (!txtValorDecimoFerias.getText().equals("")) {
				valorDecimoFerias = Double.parseDouble(txtValorDecimoFerias.getText().trim().replace(',', '.'));
			}
			
			
			if (pgtoTotal != null) {
				if (tipo == 'A') {
					double valorInicial = Double.parseDouble(pgtoTotal.modelo.getValueAt(pgtoTotal.tabela.getSelectedRow(), FrmPagamentoTotal.COLUNA_VALOR).toString());					
					double valorTotal = (((valorInicial - valorDesconto) - valorDescontoHAT) + valorDecimoFerias);
					
					int linha = pgtoTotal.tabela.getSelectedRow();
					if (valorTotal >= 0) {
						pgtoTotal.modelo.setValueAt(valorDesconto, linha, FrmPagamentoTotal.COLUNA_DESCONTO);
						pgtoTotal.modelo.setValueAt(valorDescontoHAT, linha, FrmPagamentoTotal.COLUNA_DESCHAT);
						pgtoTotal.modelo.setValueAt(valorDecimoFerias, linha, FrmPagamentoTotal.COLUNA_ACRESCIMO);
						pgtoTotal.modelo.setValueAt(valorTotal, linha, FrmPagamentoTotal.COLUNA_VALORTOTAL);
						pgtoTotal.modelo.setValueAt(txtHistorico.getText(), linha, FrmPagamentoTotal.COLUNA_OBDESCONTO);
						pgtoTotal.modelo.setValueAt(txtCodAdiantamento.getText(), linha, FrmPagamentoTotal.COLUNA_CODADIANTAMENTO);
						pgtoTotal.modelo.fireTableDataChanged();
						pgtoTotal.calculaTotalEmpreiteiro();
					} else {
						JOptionPane.showMessageDialog(this, "O valor final do empregado não pode ser negativo!", "Saldo Negativo", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_cancelar.gif")));
						txtValorDesconto.selectAll();
						txtValorDesconto.requestFocus();
						return;
					}
				} else if (tipo == 'I') {
					pgtoTotal.txtValorDescontoIro.setText(decimal.format(valorDesconto));
					pgtoTotal.txtMostraDescontoIro.setText(txtHistorico.getText());
					pgtoTotal.valorDescIro = valorDesconto;
					pgtoTotal.codAdiantIro = Integer.parseInt(txtCodAdiantamento.getText());
					pgtoTotal.calculaTotalEmpreiteiro();
				}
			} else if (pgtoTotalOutros != null) {
				double valorTotal = Double.parseDouble(pgtoTotalOutros.modelo.getValueAt(pgtoTotalOutros.tabela.getSelectedRow(), 4).toString()) - valorDesconto;
				int linha = pgtoTotalOutros.tabela.getSelectedRow();
				if (valorTotal >= 0) {
					pgtoTotalOutros.modelo.setValueAt(valorDesconto, linha, 5);
					pgtoTotalOutros.modelo.setValueAt(valorTotal, linha, 6);
					pgtoTotalOutros.modelo.setValueAt(txtHistorico.getText(), linha, 11);
					pgtoTotalOutros.modelo.setValueAt(txtCodAdiantamento.getText(), linha, 12);
					pgtoTotalOutros.modelo.fireTableDataChanged();
					pgtoTotalOutros.calculaTotal();
				} else {
					JOptionPane.showMessageDialog(this, "O valor final do empregado não pode ser negativo!", "Saldo Negativo", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_cancelar.gif")));
					txtValorDesconto.selectAll();
					txtValorDesconto.requestFocus();
					return;
				}
			}

			int codAdiant = Integer.parseInt(txtCodAdiantamento.getText());
			if (cBoxAdiantamento.isSelected()) {
				adiantDao.atualizaPago(codAdiant);
			}
			doDefaultCloseAction();
		} else if (origem == btnCancelar)
			doDefaultCloseAction();
	}
	
	private void calculaValorFinal() {
		try {
			double valorInicial = Double.parseDouble(decimal.parse(txtValorInicial.getText()).toString());
			double valorDesconto = Double.parseDouble(decimal.parse(txtValorDesconto.getText()).toString());
			double valorDescHAT = Double.parseDouble(decimal.parse(txtValorDescontoHAT.getText()).toString());
			double valorDecimoFerias = Double.parseDouble(decimal.parse(txtValorDecimoFerias.getText()).toString());
			
			double valorFinal = valorInicial - valorDesconto - valorDescHAT + valorDecimoFerias;
			
			txtValorFinal.setText(decimal.format(valorFinal));
		} catch (NumberFormatException e) {
		} catch (ParseException e) {}
		
	}
}