package safristas.vo.lancamentos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
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
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.MaskFormatter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import exceptions.QuantidadeErradaException;
import exceptions.StringVaziaException;
import exceptions.ValorErradoException;
import safristas.bo.EmpregadoBO;
import safristas.bo.outros.LancaDiaOutrosBO;
import safristas.bo.outros.VeiculoBO;
import safristas.bo.safristas.EquipeBO;
import safristas.dao.EmpregadoDao;
import safristas.dao.outros.LancaDiaOutrosDao;
import safristas.dao.outros.VeiculoDao;
import safristas.dao.safristas.EquipeDao;
import safristas.vo.FrmMenuGeralMaca;
import safristas.vo.consultcadast.FrmConsultaDiaOutros;
import safristas.vo.consultcadast.FrmConsultaEmpregado;
import safristas.vo.consultcadast.FrmConsultaVeiculo;

public class FrmDiaTrabalhoOutros extends JInternalFrame implements ActionListener {
	private static final long serialVersionUID = -7327351827889124407L;

	private MaskFormatter mascaraData;
	DecimalFormat decimal = new DecimalFormat( "#,##0.00" );

	private GridBagConstraints constraints = new GridBagConstraints();

	private double valorbins = 0.0, valorTotal = 0.0;

	private JLabel lblCodigoDia, lblData, lblTipo, lblCodigoEmpregado, lblCodVeiculo, lblQntdBins, lblValorBins,
	lblValorTotal, lblNf, lblLote, lblVariedade, lblHistorico;
	public JTextField txtCodigoDia, txtCodEmpregado, txtMostraEmpregado, txtCodVeiculo, txtMostraVeiculo, txtQntdBins,
	txtValorBins, txtValorTotal, txtNF, txtLote, txtCodVariedade, txtMostraVariedade;
	private JButton btnProcuraEmpregado, btnProcuraVeiculo, btnConfirmar, btnCancelar;
	private JFormattedTextField txtData;
	private String[] variedade = {"Gala - Royal", "Gala - Maxi", "Fuji - Standard" , "Fuji - Kiku", "Fuji - Suprema"};
	private JComboBox cbVariedade;
	private JTextArea txtAreaObserv;
	private JPanel painelGeral = new JPanel();
	private JPanel painelCima = new JPanel();
	private JPanel painelEsq = new JPanel();
	private JPanel painelMeio = new JPanel();
	private JPanel painelBaixo = new JPanel();
	private Locale brasil = new Locale("pt", "BR");
	protected DateTimeZone zona = DateTimeZone.forID("Etc/GMT+3");
	protected DateTime data = new DateTime(zona);
	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy", brasil);
	public JRadioButton rBtnCaminhao, rBtnTrator, rBtnOutros;
	protected ButtonGroup grupo;

	protected ArrayList<EquipeBO> equiBO = new ArrayList<EquipeBO>();
	protected EquipeDao equiDao = new EquipeDao();
	protected EmpregadoBO adoBO = new EmpregadoBO();
	protected EmpregadoDao adoDao = new EmpregadoDao();
	protected LancaDiaOutrosBO diaOutBO;
	protected LancaDiaOutrosDao diaOutDao = new LancaDiaOutrosDao();
	protected VeiculoBO veicBO = new VeiculoBO();
	protected VeiculoDao veicDao = new VeiculoDao();

	FrmConsultaDiaOutros consDiaOut = null;

	public FrmDiaTrabalhoOutros (FrmConsultaDiaOutros consDiaOut) {
		this();
		this.consDiaOut = consDiaOut;
		txtCodigoDia.setText(String.valueOf(consDiaOut.diaOutBO.getCodigo()));
		txtData.setValue(df.format(consDiaOut.diaOutBO.data.toDate()));
		txtCodEmpregado.setText(String.valueOf(consDiaOut.diaOutBO.adoBO.getCodigo()));
		txtMostraEmpregado.setText(consDiaOut.diaOutBO.adoBO.getNome());
		txtCodVeiculo.setText(String.valueOf(consDiaOut.diaOutBO.veicBO.getCodigo()));
		txtMostraVeiculo.setText(consDiaOut.diaOutBO.veicBO.getPlaca() + " - " + consDiaOut.diaOutBO.veicBO.getTipoVeiculoString());
		txtQntdBins.setText(String.valueOf(consDiaOut.diaOutBO.getQntBins()));
		txtValorBins.setText(decimal.format(consDiaOut.diaOutBO.getValorBins()));
		txtValorTotal.setText(decimal.format(consDiaOut.diaOutBO.getValorTotal()));
		txtNF.setText(String.valueOf(consDiaOut.diaOutBO.getNroNF()));
		txtLote.setText(String.valueOf(consDiaOut.diaOutBO.getLote()));
		txtAreaObserv.setText(consDiaOut.diaOutBO.observacao.getText());
		cbVariedade.setSelectedItem(consDiaOut.diaOutBO.getVariedade());

		if (consDiaOut.diaOutBO.adoBO.funcaoBO.getCodigo() == 2) {
			rBtnTrator.setSelected(true);
			rBtnTrator.doClick();
		} else if (consDiaOut.diaOutBO.adoBO.funcaoBO.getCodigo() == 3) {
			rBtnCaminhao.setSelected(true);
			rBtnCaminhao.doClick();
		} else
			rBtnOutros.setSelected(true);
		calculaValores();
		setTitle("Alterar dia outras funções");
		btnConfirmar.setText("Alterar (F1)");
	}

	public FrmDiaTrabalhoOutros() {

		super("Lan�amento Dia Outros",false,true,false,true);

		setSize(600, 500);
		setResizable(false);
		setTitle("Lançar dia outras Funções");
		setFrameIcon(new ImageIcon(getClass().getResource("/icons/icon_logo_varaschin.gif")));

		painelGeral.setLayout(new BorderLayout(2, 2));

		painelCima.setLayout(new GridBagLayout());

		painelEsq.setLayout(new FlowLayout());

		painelMeio.setLayout(new GridBagLayout());

		painelBaixo.setLayout(new GridBagLayout());

		Font f = new Font("Arial", Font.PLAIN, 14);

		//----------------------------------------------------

		constraints.insets = new Insets(2, 4, 2, 2);

		lblCodigoDia = new JLabel("Código");
		lblCodigoDia.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblCodigoDia, constraints);

		txtCodigoDia = new JTextField(4);
		txtCodigoDia.setText("-");
		txtCodigoDia.setEditable(false);
		txtCodigoDia.setFocusable(false);
		txtCodigoDia.setFont(f);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtCodigoDia, constraints);

		lblData = new JLabel("Data");
		lblData.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblData, constraints);

		try {
			mascaraData = new MaskFormatter("##'/##'/####");
		} catch (ParseException e) {}

		txtData = new JFormattedTextField(mascaraData);
		txtData.setColumns(8);
		txtData.setFont(f);
		txtData.setText(df.format(data.toDate()));
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtData, constraints);
		constraints.gridwidth = 1;

		lblTipo = new JLabel("Tipo");
		lblTipo.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblTipo, constraints);

		JPanel painelRadio = new JPanel();
		painelRadio.setLayout(new FlowLayout());

		painelRadio.setBorder(BorderFactory.createEtchedBorder());

		rBtnCaminhao = new JRadioButton("Motorista", true);
		painelRadio.add(rBtnCaminhao);

		rBtnTrator = new JRadioButton("Tratorista", false);
		painelRadio.add(rBtnTrator);

		rBtnOutros = new JRadioButton("Outros", false);
		painelRadio.add(rBtnOutros);

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 3;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(painelRadio, constraints);
		constraints.gridwidth = 1;

		grupo = new ButtonGroup();
		grupo.add(rBtnCaminhao);
		grupo.add(rBtnTrator);
		grupo.add(rBtnOutros);

		lblCodigoEmpregado = new JLabel("C�digo empregado");
		lblCodigoEmpregado.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblCodigoEmpregado, constraints);

		txtCodEmpregado = new JTextField(4);
		txtCodEmpregado.setFont(f);
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtCodEmpregado, constraints);
		txtCodEmpregado.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				ArrayList<EmpregadoBO> adoBO2 = adoDao.consultaPorCodigo(Integer.parseInt(txtCodEmpregado.getText()));
				if (adoBO == null) {
					txtCodEmpregado.requestFocus();
					txtCodEmpregado.selectAll();
					txtCodEmpregado.setText("");
				} else
					txtMostraEmpregado.setText(adoBO2.get(0).getNome());	
			}
		});

		txtMostraEmpregado = new JTextField(20);
		txtMostraEmpregado.setEditable(false);
		txtMostraEmpregado.setFont(f);
		txtMostraEmpregado.setFocusable(false);
		constraints.gridx = 2;
		constraints.gridy = 3;
		
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtMostraEmpregado, constraints);
		constraints.gridwidth = 1;

		btnProcuraEmpregado = new JButton("Procurar", new ImageIcon(getClass().getResource("/icons/icon_procurar.gif")));
		btnProcuraEmpregado.setFocusable(false);
		constraints.gridx = 4;
		constraints.gridy = 3;
		constraints.ipady = -5;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(btnProcuraEmpregado, constraints);
		constraints.ipady = 0;

		lblCodVeiculo = new JLabel("C�digo ve�culo");
		lblCodVeiculo.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblCodVeiculo, constraints);

		txtCodVeiculo = new JTextField(4);
		txtCodVeiculo.setFont(f);
		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtCodVeiculo, constraints);
		txtCodVeiculo.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				ArrayList<VeiculoBO> veicBO = veicDao.consultaPorCodigo(Integer.parseInt(txtCodVeiculo.getText()));
				if (veicBO == null) {
					txtCodVeiculo.requestFocus();
					txtCodVeiculo.selectAll();
					txtCodVeiculo.setText("");
				} else
					txtMostraVeiculo.setText(veicBO.get(0).getPlaca() + " - " + veicBO.get(0).getTipoVeiculoString());	
			}
		});

		txtMostraVeiculo = new JTextField(20);
		txtMostraVeiculo.setEditable(false);
		txtMostraVeiculo.setFont(f);
		txtMostraVeiculo.setFocusable(false);
		constraints.gridx = 2;
		constraints.gridy = 4;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtMostraVeiculo, constraints);
		constraints.gridwidth = 1;

		btnProcuraVeiculo = new JButton("Procurar", new ImageIcon(getClass().getResource("/icons/icon_procurar.gif")));
		btnProcuraVeiculo.setFocusable(false);
		constraints.gridx = 4;
		constraints.gridy = 4;
		constraints.ipady = -5;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(btnProcuraVeiculo, constraints);
		constraints.ipady = 0;

		lblQntdBins = new JLabel("Quantidade bins");
		lblQntdBins.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblQntdBins, constraints);

		txtQntdBins = new JTextField(4);
		txtQntdBins.setFont(f);
		txtQntdBins.setText("1");
		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtQntdBins, constraints);

		lblValorBins = new JLabel("Valor bins");
		lblValorBins.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 6;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblValorBins, constraints);

		txtValorBins = new JTextField(10);
		txtValorBins.setFont(f);
		constraints.gridx = 1;
		constraints.gridy = 6;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtValorBins, constraints);
		constraints.gridwidth = 1;
		txtValorBins.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				calculaValores();
			}
		});;

		lblNf = new JLabel("N� nota fiscal");
		lblNf.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 7;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblNf, constraints);

		txtNF = new JTextField(7);
		txtNF.setFont(f);
		constraints.gridx = 1;
		constraints.gridy = 7;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtNF, constraints);
		constraints.gridwidth = 1;

		lblLote = new JLabel("Lote");
		lblLote.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 8;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblLote, constraints);

		txtLote = new JTextField(7);
		txtLote.setFont(f);
		constraints.gridx = 1;
		constraints.gridy = 8;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtLote, constraints);
		constraints.gridwidth = 1;

		lblVariedade = new JLabel("Variedade");
		lblVariedade.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 9;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblVariedade, constraints);

		cbVariedade = new JComboBox(variedade);
		cbVariedade.setFont(f);
		constraints.gridx = 1;
		constraints.gridy = 9;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(cbVariedade, constraints);
		constraints.gridwidth = 1;

		lblValorTotal = new JLabel("Valor total");
		lblValorTotal.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 10;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblValorTotal, constraints);

		txtValorTotal = new JTextField(10);
		txtValorTotal.setBackground(new Color(187,244,188));
		txtValorTotal.setFont(f);
		txtValorTotal.setEditable(false);
		txtValorTotal.setFocusable(false);
		constraints.gridx = 1;
		constraints.gridy = 10;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtValorTotal, constraints);
		constraints.gridwidth = 1;

		lblHistorico = new JLabel("Hist�rico");
		lblHistorico.setFont(f);
		constraints.anchor = GridBagConstraints.NORTHEAST;
		constraints.gridx = 0;
		constraints.gridy = 11;
		constraints.gridwidth = 1;
		painelMeio.add(lblHistorico, constraints);

		txtAreaObserv = new JTextArea(5, 32);
		txtAreaObserv.setFont(f);
		txtAreaObserv.setLineWrap(true);
		txtAreaObserv.setMargin(new Insets(4, 4, 4, 4));
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridx = 1;
		constraints.gridy = 11;
		constraints.gridwidth = 4;
		JScrollPane rolagemObserv = new JScrollPane(txtAreaObserv);
		painelMeio.add(rolagemObserv, constraints);

		constraints.gridwidth = 1;

		painelGeral.add(painelMeio, BorderLayout.CENTER);

		//----------------------------fim painel por equipe------------------------------

		constraints.weightx = 0;
		constraints.weighty = 0;

		constraints.ipadx = 10;
		constraints.ipady = 4;

		btnConfirmar = new JButton("Confirmar (F1)", new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelBaixo.add(btnConfirmar, constraints);
		btnConfirmar.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F1"), "KEY_F1");
		btnConfirmar.getActionMap().put("KEY_F1", new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -1756441381703493693L;

			public void actionPerformed(ActionEvent evt) {
				btnConfirmar.doClick();
			}
		});

		constraints.ipadx = 50;
		constraints.ipady = -3;

		btnCancelar = new JButton("Sair (F4)", new ImageIcon(getClass().getResource("/icons/icon_sair.gif")));
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelBaixo.add(btnCancelar, constraints);
		btnCancelar.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F4"), "KEY_F4");
		btnCancelar.getActionMap().put("KEY_F4", new AbstractAction() {
			private static final long serialVersionUID = -6348846116199971629L;

			public void actionPerformed(ActionEvent evt) {
				btnCancelar.doClick();
			}
		});

		painelBaixo.setBorder(BorderFactory.createEtchedBorder());
		painelGeral.add(painelBaixo, BorderLayout.SOUTH);

		Container p = getContentPane();
		p.add(painelGeral);

		btnConfirmar.addActionListener(this);
		btnCancelar.addActionListener(this);
		btnProcuraEmpregado.addActionListener(this);
		btnProcuraVeiculo.addActionListener(this);
		rBtnCaminhao.addActionListener(this);
		rBtnTrator.addActionListener(this);
		rBtnOutros.addActionListener(this);

		KeyboardFocusManager.getCurrentKeyboardFocusManager()
		.addPropertyChangeListener("permanentFocusOwner", new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent e) {
				if (e.getNewValue() instanceof JTextField) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							JTextField textField = (JTextField)e.getNewValue();
							textField.selectAll();
						}
					});
				}
			}
		});
	}

	private void calculaValores() {
		int qntd = 1;

		if (txtQntdBins.isEnabled()) {
			qntd = Integer.parseInt(txtQntdBins.getText());
		}
		
			try {
				valorbins = Double.parseDouble(decimal.parse(txtValorBins.getText()).toString());
			} catch (NumberFormatException e) {} catch (ParseException e) {}
		
		valorTotal = valorbins * qntd;
		txtValorTotal.setText(decimal.format(valorTotal));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object origem = e.getSource();

		if (origem == rBtnTrator) {
			lblQntdBins.setEnabled(false);
			lblQntdBins.setText("Quantidade");
			txtQntdBins.setEnabled(false);
			lblValorBins.setText("Valor da di�ria");
			lblLote.setEnabled(false);
			txtLote.setEnabled(false);
			lblNf.setEnabled(false);
			txtNF.setEnabled(false);
			lblVariedade.setEnabled(false);
			if (cbVariedade.isEnabled()) {
				cbVariedade.setEnabled(false);
				cbVariedade.addItem("-");
				cbVariedade.setSelectedItem("-");
			}
		} else if (origem == rBtnOutros) {
			lblQntdBins.setEnabled(false);
			lblQntdBins.setText("Quantidade");
			txtQntdBins.setEnabled(false);
			lblValorBins.setText("Valor");
			lblLote.setEnabled(false);
			txtLote.setEnabled(false);
			lblNf.setEnabled(false);
			txtNF.setEnabled(false);
			lblVariedade.setEnabled(false);
			if (cbVariedade.isEnabled()) {
				cbVariedade.setEnabled(false);
				cbVariedade.addItem("-");
				cbVariedade.setSelectedItem("-");
			}
		} else if (origem == rBtnCaminhao) {
			lblQntdBins.setEnabled(true);
			lblQntdBins.setText("Quantidade bins");
			txtQntdBins.setEnabled(true);
			lblValorBins.setText("Valor bins");
			lblLote.setEnabled(true);
			txtLote.setEnabled(true);
			lblNf.setEnabled(true);
			txtNF.setEnabled(true);
			lblVariedade.setEnabled(true);
			cbVariedade.setEnabled(true);
			cbVariedade.removeItem("-");
			cbVariedade.setSelectedItem("Gala");
		} else if (origem == btnProcuraEmpregado) {
			FrmConsultaEmpregado frame = new FrmConsultaEmpregado(this);
			frame.setVisible(true);
			getDesktopPane().add(frame);
			try {
				frame.setSelected(true);
			}
			catch (PropertyVetoException exc) { }
			FrmMenuGeralMaca.centralizaInternalFrame(frame, getDesktopPane());
		} else if (origem == btnProcuraVeiculo) {
			FrmConsultaVeiculo frame = new FrmConsultaVeiculo(this);
			frame.setVisible(true);
			getDesktopPane().add(frame);
			try {
				frame.setSelected(true);
			}
			catch (PropertyVetoException exc) { }
			FrmMenuGeralMaca.centralizaInternalFrame(frame, getDesktopPane());
		}
		else if (origem == btnConfirmar) {
			diaOutBO = new LancaDiaOutrosBO();
			veicBO = new VeiculoBO();

			if (txtCodigoDia.getText().equals("-"))
				diaOutBO.setCodigo(diaOutDao.getUltimoCod()+1);
			else
				diaOutBO.setCodigo(Integer.parseInt(txtCodigoDia.getText()));

			try {
				diaOutBO.data = new DateTime(df.parse(txtData.getText()));
			} catch (ParseException e2) {
				JOptionPane.showMessageDialog(this, "Data inv�lida!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtData.requestFocus();
				txtData.selectAll();
				return;
			}

			if (!txtCodEmpregado.getText().trim().equals("")) {
				diaOutBO.adoBO.setCodigo(Integer.parseInt(txtCodEmpregado.getText()));
			} else {
				JOptionPane.showMessageDialog(this, "Codigo empregado deve ser preenchido!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtCodEmpregado.requestFocus();
				txtCodEmpregado.selectAll();
				return;
			}

			diaOutBO.adoBO = adoDao.consultaPorCodigo(diaOutBO.adoBO.getCodigo()).get(0);

			try {
				diaOutBO.adoBO.setNome(txtMostraEmpregado.getText());
			} catch (StringVaziaException e3) {}


			if (!txtCodVeiculo.getText().trim().equals("")) {
				diaOutBO.veicBO.setCodigo(Integer.parseInt(txtCodVeiculo.getText()));
			} else {
				JOptionPane.showMessageDialog(this, "Codigo do ve�culo deve ser preenchido!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtCodEmpregado.requestFocus();
				txtCodEmpregado.selectAll();
				return;
			}

			if (txtQntdBins.isEnabled()) {
				try {
					diaOutBO.setQntBins(Integer.parseInt(txtQntdBins.getText()));
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(this, "Quantidade de deve ser num�rico!", "ERRO", JOptionPane.ERROR_MESSAGE);
					txtQntdBins.requestFocus();
					txtQntdBins.selectAll();
					return;
				} catch (QuantidadeErradaException e1) {
					JOptionPane.showMessageDialog(this, "Quantidade de bins incorreta!", "ERRO", JOptionPane.ERROR_MESSAGE);
					txtQntdBins.requestFocus();
					txtQntdBins.selectAll();
					return;
				}
			} else
				try {
					diaOutBO.setQntBins(1);
				} catch (QuantidadeErradaException e2) {}

			try {
				diaOutBO.setValorBins(valorbins);
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "Valor bins deve ser num�rico!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtValorBins.requestFocus();
				txtValorBins.selectAll();
				return;
			} catch (ValorErradoException e1) {
				JOptionPane.showMessageDialog(this, "Valor bins incorreto!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtValorBins.requestFocus();
				txtValorBins.selectAll();
				return;
			}

			try {
				diaOutBO.setValorTotal(valorTotal);
			} catch (NumberFormatException e1) {} 
			catch (ValorErradoException e1) {}

			if (txtNF.isEnabled())
				diaOutBO.setNroNF(Integer.parseInt(txtNF.getText()));
			else
				diaOutBO.setNroNF(0);

			if (txtLote.isEnabled())
				diaOutBO.setLote(Integer.parseInt(txtLote.getText()));
			else
				diaOutBO.setLote(0);

			if (cbVariedade.isEnabled())
				diaOutBO.setVariedade(cbVariedade.getSelectedItem().toString());
			else
				diaOutBO.setVariedade("");

			diaOutBO.observacao.setText(txtAreaObserv.getText());

			if (consDiaOut == null) {
				diaOutDao.incluir(diaOutBO);
				txtCodVeiculo.setText("");
				txtMostraVeiculo.setText("");
				txtCodEmpregado.setText("");
				txtMostraEmpregado.setText("");
				txtQntdBins.setText("");
				txtValorBins.setText("");
				txtValorTotal.setText("");
				txtNF.setText("");
				txtLote.setText("");
				txtAreaObserv.setText("");
				txtData.selectAll();
				txtData.requestFocus();
				if (cbVariedade.isEnabled())
					cbVariedade.setSelectedItem("Gala");
				JOptionPane.showMessageDialog(this, "Registro salvo com sucesso!", "Registro Salvo", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
			} else {
				if (consDiaOut.diaOutBO.getPagou() == 'S') {
					JOptionPane.showMessageDialog(this, "Este dia j� cont�m  ! Exclua o pagamento primeiro, caso deseje alterar este registro.", "Alterar Registro",JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					diaOutDao.alterar(diaOutBO);
					int linha = consDiaOut.tabela.getSelectedRow();
					consDiaOut.modelo.setValueAt(df.format(consDiaOut.diaOutBO.data.toDate()),linha,1);
					consDiaOut.modelo.setValueAt(diaOutBO.adoBO.getNome(),linha,2);
					consDiaOut.modelo.setValueAt(diaOutBO.adoBO.funcaoBO.getNome(),linha,3);
					consDiaOut.modelo.setValueAt(diaOutBO.getValorTotal(),linha,4);
					JOptionPane.showMessageDialog(this, "Registro alterado com sucesso!", "Registro Alterado", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
					doDefaultCloseAction();
				}
			}
		} else if (origem == btnCancelar) {
			doDefaultCloseAction();
		}
	}
}
