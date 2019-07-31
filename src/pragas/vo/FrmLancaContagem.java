package pragas.vo;

import java.awt.BorderLayout;
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
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.MaskFormatter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;

import exceptions.QuantidadeErradaException;
import gerais.bo.QuadraBO;
import gerais.dao.QuadraDao;
import pragas.bo.ContagemBO;
import pragas.bo.FrascosBO;
import pragas.dao.ContagemDao;
import pragas.dao.FrascosDao;
import util.ModeloTabela;
public class FrmLancaContagem extends JInternalFrame implements ActionListener {
	private static final long serialVersionUID = -2497625136173141494L;
	private MaskFormatter mascaraData;
	int nroQuadra = 0;

	private GridBagConstraints constraints = new GridBagConstraints();

	private JLabel lblCodigoDia, lblTipoPraga, lblNroQuadra, lblQntdFrascos, lblData, lblQntdInsetos;
	public JTextField txtCodigoCont, txtNroQuadra, txtQntdFrascos, txtQntdInsetos;
	private JButton btnConfirmar, btnCancelar;
	private JFormattedTextField txtData;
	private JRadioButton rBtnGrapholita, rBtnBonagota, rBtnMosca;
	private ButtonGroup grupo;
	private JPanel painelGeral = new JPanel();
	private JPanel painelCima = new JPanel();
	private JPanel painelEsq = new JPanel();
	private JPanel painelMeioBorder = new JPanel();
	private JPanel painelMeio = new JPanel();
	private JPanel painelBaixo = new JPanel();
	private Locale brasil = new Locale("pt", "BR");
	protected DateTimeZone zona = DateTimeZone.forID("Etc/GMT+3");
	protected DateTime data = new DateTime(zona);
	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy", brasil);

	private QuadraDao quadDao = new QuadraDao();
	protected ContagemBO contBO;
	private ContagemDao contDao = new ContagemDao();
	private FrascosDao frascosDao = new FrascosDao();
	private FrascosBO frascosBO = null;

	JTable tabela;
	ModeloTabela modeloTab;

	public FrmLancaContagem() {

		super("Lançamento Dia",false,true,false,true);

		setSize(480, 300);
		setResizable(true);
		setTitle("Lançar Contagem");
		setFrameIcon(new ImageIcon(getClass().getResource("/icons/icon_logo_varaschin.gif")));

		painelGeral.setLayout(new BorderLayout(2, 2));

		painelCima.setLayout(new GridBagLayout());

		painelEsq.setLayout(new FlowLayout());

		painelMeioBorder.setLayout(new BorderLayout(2, 2));

		painelMeio.setLayout(new GridBagLayout());

		painelBaixo.setLayout(new GridBagLayout());

		Font f = new Font("Arial", Font.PLAIN, 14);

		//------------------------------------------------------

		constraints.insets = new Insets(4, 4, 4, 4);

		lblCodigoDia = new JLabel("Código");
		lblCodigoDia.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		painelCima.add(lblCodigoDia, constraints);

		txtCodigoCont = new JTextField(4);
		txtCodigoCont.setText(String.valueOf(contDao.getUltimoCod()+1));
		txtCodigoCont.setEditable(false);
		txtCodigoCont.setFocusable(false);
		txtCodigoCont.setFont(f);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		painelCima.add(txtCodigoCont, constraints);
		JPanel painelEspecies = new JPanel(new FlowLayout());

		rBtnGrapholita = new JRadioButton("Grapholita", true);
		rBtnGrapholita.setFont(f);
		painelEspecies.add(rBtnGrapholita);

		rBtnBonagota = new JRadioButton("Bonagota", false);
		rBtnBonagota.setFont(f);
		painelEspecies.add(rBtnBonagota);

		rBtnMosca = new JRadioButton("Mosca", false);
		rBtnMosca.setFont(f);
		painelEspecies.add(rBtnMosca);

		constraints.gridx = 1;
		constraints.gridy = 1;
		painelEspecies.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		painelCima.add(painelEspecies, constraints);

		grupo = new ButtonGroup();
		grupo.add(rBtnGrapholita);
		grupo.add(rBtnBonagota);
		grupo.add(rBtnMosca);

		painelGeral.add(painelCima, BorderLayout.NORTH);

		//------------------------------------------------------

		constraints.weightx = 1;
		constraints.weighty = 0;

		lblData = new JLabel("Data:");
		lblData.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 0;
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
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtData, constraints);
		constraints.gridwidth = 1;

		lblNroQuadra = new JLabel("Número Quadra");
		lblNroQuadra.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblNroQuadra, constraints);

		txtNroQuadra = new JTextField(5);
		txtNroQuadra.setFont(f);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtNroQuadra, constraints);
		txtNroQuadra.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				selecionaQuadra(Integer.parseInt(txtNroQuadra.getText()));
			}
		});

		lblQntdFrascos = new JLabel("Qntd. Frascos:");
		lblQntdFrascos.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblQntdFrascos, constraints);

		txtQntdFrascos = new JTextField(5);
		txtQntdFrascos.setFont(f);
		txtQntdFrascos.setEditable(false);
		txtQntdFrascos.setFocusable(false);
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtQntdFrascos, constraints);

		lblQntdInsetos = new JLabel("Quantidade Machos:");
		lblQntdInsetos.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblQntdInsetos, constraints);

		txtQntdInsetos = new JTextField(5);
		txtQntdInsetos.setFont(f);
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtQntdInsetos, constraints);

		painelMeio.setBorder(BorderFactory.createEtchedBorder());
		painelMeioBorder.add(painelMeio, BorderLayout.CENTER);

		painelGeral.add(painelMeioBorder, BorderLayout.CENTER);

		//----------------------------fim painel------------------------------

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
			private static final long serialVersionUID = 4534421141365571896L;

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
			private static final long serialVersionUID = -8237690810461114260L;

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
		rBtnGrapholita.addActionListener(this);
		rBtnBonagota.addActionListener(this);
		rBtnMosca.addActionListener(this);

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

	public void selecionaQuadra(int nroQuadra) {
		QuadraBO quadBO = new QuadraBO();

		quadBO = quadDao.consultaPorNumeroETipo(nroQuadra, getTipo()).get(0);

		char tipo = getTipo();

		if (tipo == 'G') {
			lblQntdInsetos.setText("Quantidade Machos:");
		} else if (tipo == 'B') {
			lblQntdInsetos.setText("Quantidade Machos:");
		} else if (tipo == 'M') {
			lblQntdInsetos.setText("Quantidade Insetos:");
		}

		txtNroQuadra.setText(quadBO.getNumero() + "");
		txtQntdFrascos.setText(String.valueOf(quadBO.getQntdFrascos()));
		nroQuadra = quadBO.getNumero();
	}

	private int getCodQuadra() {
		frascosBO = frascosDao.consultaPorNumeroETipo(Integer.parseInt(txtNroQuadra.getText()), getTipo()).get(0);
		return frascosBO.getCodigo();
	}

	public char getTipo() {
		if (rBtnGrapholita.isSelected()) 
			return 'G';
		else if (rBtnBonagota.isSelected()) 
			return 'B';
		else
			return 'M';
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object origem = e.getSource();

		try {
			int nroQuadra = Integer.parseInt(txtNroQuadra.getText());    
		} catch (NumberFormatException erro) {

		}

		if (origem == btnConfirmar) {
			contBO = new ContagemBO();
			
			contBO.setCodigo(Integer.parseInt(txtCodigoCont.getText()));

			contBO.frasco.quadra.setNumero(Integer.parseInt(txtNroQuadra.getText()));

			contBO.frasco.setCodigo(getCodQuadra());

			try {
				contBO.data = new DateTime(df.parse(txtData.getText()));
			} catch (ParseException e2) {
				JOptionPane.showMessageDialog(this, "Data inválida!", "ERRO", JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				contBO.setQntdInsetos(Integer.parseInt(txtQntdInsetos.getText()));
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "Quantidade de insetos deve ser numérico!", "ERRO", JOptionPane.ERROR_MESSAGE);
				return;
			} catch (QuantidadeErradaException e1) {
				JOptionPane.showMessageDialog(this, "Quantidade de insetos não pode ser negativa!", "ERRO", JOptionPane.ERROR_MESSAGE);
				return;
			}

			int qntdDias = calculaDias();

			contBO.setQntdDias(qntdDias);

			BigDecimal indice = calculaIndice(qntdDias);
			contBO.setIndiceFinal(indice);

			contDao.incluir(contBO);
			//			else
			//				contDao.alterar(contBO);

			txtNroQuadra.requestFocus();
			txtNroQuadra.selectAll();
			JOptionPane.showMessageDialog(this, "Registro salvo com sucesso!", "Registro Salvo", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));

		} else if (origem == btnCancelar) {
			doDefaultCloseAction();
		}
	}

	public int calculaDias() {
		DateTime dataAtual = new DateTime(contBO.data);

		DateTime ultimaData = contDao.getDataUltimaContagem(Integer.parseInt(txtNroQuadra.getText()), getTipo());
		if (ultimaData == null) {
			return 0;
		} else {
			int qntdDias = Days.daysBetween(ultimaData, dataAtual).getDays();
			return qntdDias;
		}
	}

	public BigDecimal calculaIndice(int qntdDias) {
		BigDecimal indice = new BigDecimal("0.00");
		int qntdInsetos = Integer.parseInt(txtQntdInsetos.getText());
		int qntdFrascos = Integer.parseInt(txtQntdFrascos.getText());

		System.out.println("Insetos = " + qntdInsetos);
		System.out.println("Frascos = " + qntdFrascos);
		System.out.println("Dias = " + qntdDias);

		if (qntdDias == 0 || qntdInsetos == 0) {
			return indice;
		} else if (rBtnGrapholita.isSelected()) {
			int aux = ((int)qntdDias/7)==0?1:(int)qntdDias/7;
			double calculo = (double)qntdInsetos /  qntdFrascos / aux;
			indice = BigDecimal.valueOf(calculo);
		} else if (rBtnBonagota.isSelected()) {
			int aux = ((int)qntdDias)/7==0?1:(int)qntdDias/7;
			double calculo = (double)qntdInsetos / qntdFrascos / aux;
			indice = BigDecimal.valueOf(calculo);
		} else if (rBtnMosca.isSelected()) {
			double calculo = (double)qntdInsetos / qntdFrascos / qntdDias;
			indice = BigDecimal.valueOf(calculo);
		}
		return indice;
	}
}
