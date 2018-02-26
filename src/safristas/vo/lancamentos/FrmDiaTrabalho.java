package safristas.vo.lancamentos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.DefaultCellEditor;
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
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.text.MaskFormatter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import exceptions.QuantidadeErradaException;
import exceptions.StringVaziaException;
import exceptions.ValorErradoException;
import safristas.bo.EmpregadoBO;
import safristas.bo.safristas.DiaEmpregadoBO;
import safristas.bo.safristas.EmpreiteiroBO;
import safristas.bo.safristas.EquipeBO;
import safristas.bo.safristas.LancaDiaBO;
import safristas.dao.EmpregadoDao;
import safristas.dao.safristas.DiaEmpregadoDao;
import safristas.dao.safristas.EmpreiteiroDao;
import safristas.dao.safristas.EquipeDao;
import safristas.dao.safristas.LancaDiaDao;
import safristas.vo.FrmMenuGeralMaca;
import safristas.vo.consultcadast.FrmConsultaDia;
import safristas.vo.consultcadast.FrmConsultaEmpreiteiro;
import util.ModeloTabela;
import util.NumberRenderer;

public class FrmDiaTrabalho extends JInternalFrame implements ActionListener{
	private static final long serialVersionUID = 5583500305476262791L;

	private MaskFormatter mascaraData;
	private DecimalFormat decimal = new DecimalFormat( "#,##0.00" );

	private GridBagConstraints constraints = new GridBagConstraints();

	private double totalResto = 0, totalGeral = 0, totalComissao = 0, valorAnterior = 0;
	private boolean controlelistener = true, controleCB = true;

	private String[] presenca = {"S", "N", "M/T"};
	private JComboBox<String> cbPresenca = new JComboBox<String>(presenca);

	private JLabel lblCodigoDia, lblQntdEmpregados, lblData, lblCodEmpreiteiro, lblBinsOuDia, lblClasificador, lblValorClassificador,
	lblQntdBinsClassif, lblValorComissaoIroClassif, lblQntdBinsDia, lblValorBinsDia, lblValorOutrosIro,
	lblValorDia, lblValorTotalRestoEquipe, lblValorTotal, lblVlrComissao, lblVlrTotalComissao, lblHistorico;
	public JTextField txtCodigoDia, txtCodEmpreiteiro, txtMostraEmpreiteiro, txtValorClassif, txtQntdBinsClassif,
	txtValorComissaoIroClassif, txtQntdBinsDia, txtValorBinsDia, txtValorOutrosIro, txtValorTotal, 
	txtValorDia, txtValorTotalRestoEquipe, txtVlrComissao, txtVlrTotalComissao;
	private JButton btnProcuraEmpreiteiro, btnConfirmar, btnCancelar;
	private JFormattedTextField txtData;
	private JTextArea txtAreaObserv;
	private JRadioButton rBtnPorBins, rBtnPorDia;
	private ButtonGroup grupo;
	private JTabbedPane abas = new JTabbedPane();
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

	protected LancaDiaBO diaBO;
	protected LancaDiaDao diaDao = new LancaDiaDao();
	protected ArrayList<EmpreiteiroBO> iroBO = new ArrayList<EmpreiteiroBO>();
	protected EmpreiteiroDao iroDao = new EmpreiteiroDao();
	protected ArrayList<EquipeBO> equiBO = new ArrayList<EquipeBO>();
	protected EquipeDao equiDao = new EquipeDao();
	protected ArrayList<EmpregadoBO> adoBO = new ArrayList<EmpregadoBO>();
	protected EmpregadoDao adoDao = new EmpregadoDao();
	protected ArrayList<DiaEmpregadoBO> diaAdoBO;
	protected DiaEmpregadoDao diaAdoDao = new DiaEmpregadoDao();

	JTable tabela, tabelaEquipe;
	ModeloTabela modelo, modeloTabEquipe;
	//	ModeloTabelaDiaSafrista modelo;

	FrmConsultaDia consDia = null;

	static final int COLUNA_PRESENCA = 0;
	//	static final int COLUNA_MEIOTURNO = 1;
	static final int COLUNA_CLASSIF = 1;
	static final int COLUNA_CODIGO = 2;
	static final int COLUNA_NOME = 3;
	static final int COLUNA_FUNCAO = 4;
	static final int COLUNA_VALOR = 5;

	public FrmDiaTrabalho (FrmConsultaDia consDia) {
		this();
		this.consDia = consDia;
		txtCodigoDia.setText(String.valueOf(consDia.diaBO.getCodigo()));
		txtData.setText(df.format(consDia.diaBO.data.toDate()));
		txtCodEmpreiteiro.setText(String.valueOf(consDia.diaBO.iroBO.getCodigo()));
		txtCodEmpreiteiro.setEditable(false);
		txtCodEmpreiteiro.setFocusable(false);
		txtMostraEmpreiteiro.setText(consDia.diaBO.iroBO.getNome());
		modeloTabEquipe.addRow(new Object[] {
				consDia.diaBO.equipeBO.getCodigo(),
				consDia.diaBO.equipeBO.getNome()
		});
		btnProcuraEmpreiteiro.setEnabled(false);
		tabelaEquipe.setFocusable(true);
		tabelaEquipe.setRowSelectionInterval(0,0);
		tabelaEquipe.setColumnSelectionAllowed(false);
		int i=0;

		do {
			String presenca = "S";
			boolean isClassif = false;

			adoDao.consultaPorCodigo(consDia.diaAdoBO.get(i).adoBO.getCodigo());

			if (consDia.diaAdoBO.get(i).getPresenca() == 'N')
				presenca = "N";
			else if (consDia.diaAdoBO.get(i).getPresenca() == 'M')
				presenca = "M/T";

			if (consDia.diaAdoBO.get(i).getClassificador() == 'S')
				isClassif = true;

			modelo.addRow(new Object[] {
					presenca,
					isClassif,
					consDia.diaAdoBO.get(i).adoBO.getCodigo(),
					consDia.diaAdoBO.get(i).adoBO.getNome(),
					consDia.diaAdoBO.get(i).adoBO.funcaoBO.getNome(),
					consDia.diaAdoBO.get(i).getValor()
			});
			i++;
		} while (i < consDia.diaAdoBO.size());

		if (consDia.diaBO.getMetodo() == 'B') {
			configInterfaceBins();
		} else {
			configInterfaceDia();
		}

		txtValorClassif.setText(decimal.format(consDia.diaBO.getValorClassif()));
		lblQntdEmpregados.setText(lblQntdEmpregados.getText() + modelo.getRowCount());
		txtValorDia.setText(decimal.format(consDia.diaBO.getValorDia()));
		txtQntdBinsDia.setText(String.valueOf(consDia.diaBO.getQntBinsEquipe()));
		txtQntdBinsClassif.setText(String.valueOf(consDia.diaBO.getQntdBinsClassif()));
		txtValorBinsDia.setText(decimal.format(consDia.diaBO.getValorBins()));
		totalResto = consDia.diaBO.getValorTotalResto();
		totalGeral = consDia.diaBO.getValorTotal();
		txtValorTotalRestoEquipe.setText(decimal.format(consDia.diaBO.getValorTotalResto()));
		txtValorTotal.setText(decimal.format(consDia.diaBO.getValorTotal()));
		txtValorComissaoIroClassif.setText(decimal.format(consDia.diaBO.getValorComissIroClassif()));
		txtValorOutrosIro.setText(decimal.format(consDia.diaBO.getValorOutroIro()));
		txtVlrComissao.setText(decimal.format(consDia.diaBO.getValorComissao()));
		totalComissao = consDia.diaBO.getValorTotalComissao();
		txtVlrTotalComissao.setText(decimal.format(totalComissao));
		txtAreaObserv.setText(consDia.diaBO.observacao.getText());

		if (consDia.diaBO.pgtoBO.getCodigo() != 0)
			btnConfirmar.setEnabled(false);
		btnConfirmar.setText("Alterar turma (F1)");
	}

	public FrmDiaTrabalho() {

		super("Lan�amento Dia",false,true,false,true);

		setSize(916, 716);
		setResizable(true);
		setTitle("Lan�ar Dia - Safristas");
		setFrameIcon(new ImageIcon(getClass().getResource("/icons/icon_logo_varaschin.gif")));

		painelGeral.setLayout(new BorderLayout(2, 2));

		painelCima.setLayout(new GridBagLayout());

		painelEsq.setLayout(new GridBagLayout());

		painelMeioBorder.setLayout(new BorderLayout(2, 2));

		painelMeio.setLayout(new GridBagLayout());

		painelBaixo.setLayout(new GridBagLayout());

		Font f = new Font("Arial", Font.PLAIN, 14);

		ArrayList<Object> dados2 = new ArrayList<Object>();

		String[] colunas2 = new String[] {"C�d.", "Nome"};

		boolean[] edicao2 = {false, false};

		modeloTabEquipe = new ModeloTabela(dados2, colunas2, edicao2);
		tabelaEquipe = new JTable(modeloTabEquipe);
		tabelaEquipe.setFont(f);
		tabelaEquipe.setRowHeight(22);
		tabelaEquipe.getColumnModel().getColumn(0).setPreferredWidth(30);
		tabelaEquipe.getColumnModel().getColumn(0).setResizable(true);
		tabelaEquipe.getColumnModel().getColumn(1).setPreferredWidth(100);
		tabelaEquipe.getColumnModel().getColumn(1).setResizable(true);
		tabelaEquipe.getTableHeader().setReorderingAllowed(false); 
		tabelaEquipe.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabelaEquipe.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabelaEquipe.setFocusable(false);
		JScrollPane rolagemTabelaEquipe = new JScrollPane(tabelaEquipe);
		rolagemTabelaEquipe.setPreferredSize(new Dimension(154, 500));
		painelGeral.add(painelEsq, BorderLayout.WEST);
		painelEsq.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Equipes"));
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.anchor = GridBagConstraints.NORTH;
		painelEsq.add(rolagemTabelaEquipe, constraints);

		lblQntdEmpregados = new JLabel("Qntd. Empregados: ");
		lblQntdEmpregados.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.insets = new Insets(16, 0, 0, 0);
		constraints.anchor = GridBagConstraints.SOUTH;
		painelEsq.add(lblQntdEmpregados, constraints);

		//------------------------------------------------------

		constraints.insets = new Insets(4, 4, 4, 4);

		ArrayList<Object> dados = new ArrayList<Object>();

		String[] colunas = new String[] {"Presen�a", "Classif.", "C�digo", "Nome", "Fun��o", "Valor"};

		boolean[] edicao = {true, true, false, false, false, false, false};

		modelo = new ModeloTabela(dados, colunas, edicao);
		tabela = new JTable(modelo);
		tabela.setFont(f);
		tabela.setRowHeight(22);
		tabela.getColumnModel().getColumn(COLUNA_PRESENCA).setPreferredWidth(62);
		tabela.getColumnModel().getColumn(COLUNA_PRESENCA).setResizable(true);
		tabela.getColumnModel().getColumn(COLUNA_PRESENCA).setCellEditor(new DefaultCellEditor(cbPresenca));
		tabela.getColumnModel().getColumn(COLUNA_CLASSIF).setPreferredWidth(66);
		tabela.getColumnModel().getColumn(COLUNA_CLASSIF).setResizable(true);
		tabela.getColumnModel().getColumn(COLUNA_CODIGO).setPreferredWidth(50);
		tabela.getColumnModel().getColumn(COLUNA_CODIGO).setResizable(true);
		tabela.getColumnModel().getColumn(COLUNA_NOME).setPreferredWidth(264);
		tabela.getColumnModel().getColumn(COLUNA_NOME).setResizable(true);
		tabela.getColumnModel().getColumn(COLUNA_FUNCAO).setPreferredWidth(180);
		tabela.getColumnModel().getColumn(COLUNA_FUNCAO).setResizable(true);
		tabela.getColumnModel().getColumn(COLUNA_VALOR).setPreferredWidth(84);
		tabela.getColumnModel().getColumn(COLUNA_VALOR).setResizable(true);
		tabela.getColumnModel().getColumn(COLUNA_VALOR).setCellRenderer(NumberRenderer.getCurrencyRenderer());
		tabela.getTableHeader().setReorderingAllowed(false); 
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane rolagemTabela = new JScrollPane(tabela);
		rolagemTabela.setPreferredSize(new Dimension(100, 200));
		constraints.gridwidth = 6;
		constraints.gridx = 0;
		constraints.gridy = 0;
		painelMeioBorder.add(rolagemTabela, BorderLayout.NORTH);
		constraints.gridheight = 1;
		constraints.gridwidth = 1;

		tabela.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				int linha = tabela.rowAtPoint(e.getPoint());
				int coluna = tabela.columnAtPoint(e.getPoint());

				if (linha >= 0 && coluna == COLUNA_CLASSIF) {
					if (modelo.getValueAt(linha, COLUNA_PRESENCA).equals("N")) {
						JOptionPane.showInternalMessageDialog(FrmDiaTrabalho.this, "Empregado n�o est� presente p/ ser classificador", "Erro" , JOptionPane.ERROR_MESSAGE);
						modelo.setValueAt(false, linha, COLUNA_CLASSIF);
					} else {
						distribuiValores();
					}
				}	
			}
		});

		cbPresenca.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				distribuiValores();
			}
		});

		//----------------------------------------------------

		lblCodigoDia = new JLabel("C�digo");
		lblCodigoDia.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		painelCima.add(lblCodigoDia, constraints);

		txtCodigoDia = new JTextField(4);
		txtCodigoDia.setText("-");
		txtCodigoDia.setEditable(false);
		txtCodigoDia.setFocusable(false);
		txtCodigoDia.setFont(f);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		painelCima.add(txtCodigoDia, constraints);

		lblData = new JLabel("Data");
		lblData.setFont(f);
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelCima.add(lblData, constraints);

		try {
			mascaraData = new MaskFormatter("##'/##'/####");
		} catch (ParseException e) {}

		txtData = new JFormattedTextField(mascaraData);
		txtData.setColumns(8);
		txtData.setFont(f);
		txtData.setText(df.format(data.toDate()));
		constraints.gridx = 3;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelCima.add(txtData, constraints);
		constraints.gridwidth = 1;

		lblCodEmpreiteiro = new JLabel("C�d. empreiteiro");
		lblCodEmpreiteiro.setFont(f);
		constraints.gridx = 5;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelCima.add(lblCodEmpreiteiro, constraints);

		txtCodEmpreiteiro = new JTextField(5);
		txtCodEmpreiteiro.setFont(f);
		constraints.gridx = 6;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		painelCima.add(txtCodEmpreiteiro, constraints);

		txtMostraEmpreiteiro = new JTextField(30);
		txtMostraEmpreiteiro.setEditable(false);
		txtMostraEmpreiteiro.setFont(f);
		txtMostraEmpreiteiro.setFocusable(false);
		constraints.gridx = 7;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelCima.add(txtMostraEmpreiteiro, constraints);
		constraints.gridwidth = 1;

		txtCodEmpreiteiro.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				EmpreiteiroDao iroDao = new EmpreiteiroDao();
				ArrayList<EmpreiteiroBO> iroBO = iroDao.consultaPorCodigo(Integer.parseInt(txtCodEmpreiteiro.getText()));
				if (iroBO == null) {
					txtCodEmpreiteiro.selectAll();
					txtCodEmpreiteiro.requestFocus();
					txtMostraEmpreiteiro.setText("");
				} else {
					txtMostraEmpreiteiro.setText(iroBO.get(0).getNome());
					EquipeDao equiDao = new EquipeDao();
					ArrayList<EquipeBO> equiBO = equiDao.consultaPorCodEmpreiteiro(Integer.parseInt(txtCodEmpreiteiro.getText()));

					for (int i = modelo.getRowCount() - 1; i >= 0; i--)
						modelo.removeRow(i);

					for (int i = modeloTabEquipe.getRowCount() - 1; i >= 0; i--)
						modeloTabEquipe.removeRow(i);

					int i = 0;
					do{
						modeloTabEquipe.addRow(new Object[] {
								equiBO.get(i).getCodigo(),
								equiBO.get(i).getNome()
						});
						i++;
					} while (i < equiBO.size());
				}
			}
		});

		btnProcuraEmpreiteiro = new JButton("Procurar", new ImageIcon(getClass().getResource("/icons/icon_procurar.gif")));
		btnProcuraEmpreiteiro.setFocusable(false);
		constraints.gridx = 9;
		constraints.gridy = 0;
		constraints.ipady = -5;
		constraints.anchor = GridBagConstraints.WEST;
		painelCima.add(btnProcuraEmpreiteiro, constraints);
		constraints.ipady = 0;

		tabelaEquipe.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && controlelistener) {
					selecionaEquipe();
				}
			}
		});

		painelGeral.add(painelCima, BorderLayout.NORTH);

		//------------------------------------------------------

		constraints.weightx = 1;
		constraints.weighty = 0;

		lblBinsOuDia = new JLabel("M�todo distribui��o");
		lblBinsOuDia.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblBinsOuDia, constraints);

		JPanel painelrBtns = new JPanel();
		painelrBtns.setLayout(new FlowLayout());

		rBtnPorBins = new JRadioButton("Por bins", true);
		rBtnPorBins.setFont(f);
		painelrBtns.add(rBtnPorBins);

		rBtnPorDia = new JRadioButton("Por dia", false);
		rBtnPorDia.setFont(f);
		painelrBtns.add(rBtnPorDia);

		grupo = new ButtonGroup();
		grupo.add(rBtnPorBins);
		grupo.add(rBtnPorDia);

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(painelrBtns, constraints);

		lblValorClassificador = new JLabel("Valores classificadores");
		lblValorClassificador.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblValorClassificador, constraints);

		txtValorClassif = new JTextField(10);
		txtValorClassif.setFont(f);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtValorClassif, constraints);

		txtValorClassif.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {

				for (int x = 0; x < modelo.getRowCount(); x++)
					modelo.setValueAt(0.0, x, COLUNA_VALOR);

				distribuiValores();
			}
		});

		lblQntdBinsClassif = new JLabel("Qntd bins classificador");
		lblQntdBinsClassif.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblQntdBinsClassif, constraints);

		txtQntdBinsClassif = new JTextField(5);
		txtQntdBinsClassif.setFont(f);
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtQntdBinsClassif, constraints);

		lblValorComissaoIroClassif = new JLabel("Valor comis. empreiteiro classif.");
		lblValorComissaoIroClassif.setFont(f);
		constraints.gridx = 2;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblValorComissaoIroClassif, constraints);
		lblValorComissaoIroClassif.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				calculaComissao();
			}
		});

		txtValorComissaoIroClassif = new JTextField(10);
		txtValorComissaoIroClassif.setFont(f);
		constraints.gridx = 3;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtValorComissaoIroClassif, constraints);
		txtValorComissaoIroClassif.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				calculaComissao();
			}
		});

		lblQntdBinsDia = new JLabel("Quantidade bins equipe");
		lblQntdBinsDia.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblQntdBinsDia, constraints);

		txtQntdBinsDia = new JTextField(5);
		txtQntdBinsDia.setFont(f);
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtQntdBinsDia, constraints);

		lblValorBinsDia = new JLabel("Valor bins");
		lblValorBinsDia.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblValorBinsDia, constraints);

		txtValorBinsDia = new JTextField(10);
		txtValorBinsDia.setFont(f);
		txtValorBinsDia.setText("0,00");
		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtValorBinsDia, constraints);

		txtValorBinsDia.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				distribuiValores();
			}
		});

		lblValorDia = new JLabel("Valor dia");
		lblValorDia.setFont(f);
		lblValorDia.setForeground(Color.lightGray);
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblValorDia, constraints);

		txtValorDia = new JTextField(10);
		txtValorDia.setFont(f);
		txtValorDia.setEnabled(false);
		txtValorDia.setText("0,00");
		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtValorDia, constraints);
		constraints.gridwidth = 1;
		txtValorDia.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				distribuiValores();
			}
		});

		lblValorTotalRestoEquipe = new JLabel("Valor total resto equipe");
		lblValorTotalRestoEquipe.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 6;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblValorTotalRestoEquipe, constraints);

		txtValorTotalRestoEquipe = new JTextField(10);
		txtValorTotalRestoEquipe.setFont(f);
		txtValorTotalRestoEquipe.setEditable(false);
		txtValorTotalRestoEquipe.setFocusable(false);
		constraints.gridx = 1;
		constraints.gridy = 6;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtValorTotalRestoEquipe, constraints);

		lblValorTotal = new JLabel("Valor total equipe");
		lblValorTotal.setFont(f);
		constraints.gridx = 2;
		constraints.gridy = 6;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblValorTotal, constraints);

		txtValorTotal = new JTextField(10);
		txtValorTotal.setFont(f);
		txtValorTotal.setBackground(new Color(187,244,188));
		txtValorTotal.setEditable(false);
		txtValorTotal.setFocusable(false);
		constraints.gridx = 3;
		constraints.gridy = 6;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtValorTotal, constraints);
		constraints.gridwidth = 1;

		lblVlrComissao = new JLabel("Valor comiss�o empreiteiro");
		lblVlrComissao.setFont(f);
		constraints.anchor = GridBagConstraints.EAST;
		constraints.gridx = 0;
		constraints.gridy = 7;
		painelMeio.add(lblVlrComissao, constraints);

		txtVlrComissao = new JTextField(10);
		txtVlrComissao.setFont(f);
		constraints.gridx = 1;
		constraints.gridy = 7;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtVlrComissao, constraints);
		txtVlrComissao.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				calculaComissao();
			}
		});

		lblValorOutrosIro = new JLabel("Outros valores empreiteiro");
		lblValorOutrosIro.setFont(f);
		constraints.anchor = GridBagConstraints.EAST;
		constraints.gridx = 0;
		constraints.gridy = 8;
		painelMeio.add(lblValorOutrosIro, constraints);

		txtValorOutrosIro = new JTextField(10);
		txtValorOutrosIro.setFont(f);
		constraints.gridx = 1;
		constraints.gridy = 8;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtValorOutrosIro, constraints);
		txtValorOutrosIro.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				calculaComissao();
			}
		});

		lblVlrTotalComissao = new JLabel("Valor total comiss�o empreiteiro");
		lblVlrTotalComissao.setFont(f);
		constraints.anchor = GridBagConstraints.EAST;
		constraints.gridx = 2;
		constraints.gridy = 8;
		painelMeio.add(lblVlrTotalComissao, constraints);

		txtVlrTotalComissao = new JTextField(10);
		txtVlrTotalComissao.setFont(f);
		txtVlrTotalComissao.setEditable(false);
		txtVlrTotalComissao.setFocusable(false);
		constraints.gridx = 3;
		constraints.gridy = 8;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtVlrTotalComissao, constraints);

		lblHistorico = new JLabel("Hist�rico");
		lblHistorico.setFont(f);
		constraints.anchor = GridBagConstraints.NORTHEAST;
		constraints.gridx = 0;
		constraints.gridy = 9;
		painelMeio.add(lblHistorico, constraints);

		txtAreaObserv = new JTextArea(3, 32);
		txtAreaObserv.setFont(f);
		txtAreaObserv.setLineWrap(true);
		txtAreaObserv.setMargin(new Insets(4, 4, 4, 4));
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridx = 1;
		constraints.gridy = 9;
		constraints.gridwidth = 3;
		JScrollPane rolagemObserv = new JScrollPane(txtAreaObserv);
		painelMeio.add(rolagemObserv, constraints);

		constraints.gridwidth = 1;

		painelMeio.setBorder(BorderFactory.createEtchedBorder());

		abas.add("Normal", painelMeio);

		painelMeioBorder.add(abas, BorderLayout.CENTER);

		painelGeral.add(painelMeioBorder, BorderLayout.CENTER);

		//----------------------------fim painel por equipe------------------------------

		constraints.weightx = 0;
		constraints.weighty = 0;

		constraints.ipadx = 10;
		constraints.ipady = 4;

		btnConfirmar = new JButton("Confirmar turma (F1)", new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelBaixo.add(btnConfirmar, constraints);
		btnConfirmar.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F1"), "KEY_F1");
		btnConfirmar.getActionMap().put("KEY_F1", new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 893443548623575521L;

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
			/**
			 * 
			 */
			private static final long serialVersionUID = -4073867882628559580L;

			public void actionPerformed(ActionEvent evt) {
				btnCancelar.doClick();
			}
		});

		painelBaixo.setBorder(BorderFactory.createEtchedBorder());
		painelGeral.add(painelBaixo, BorderLayout.SOUTH);

		Container p = getContentPane();
		p.add(painelGeral);

		configInterfaceBins();

		btnConfirmar.addActionListener(this);
		btnCancelar.addActionListener(this);
		btnProcuraEmpreiteiro.addActionListener(this);
		rBtnPorBins.addActionListener(this);
		rBtnPorDia.addActionListener(this);

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

	private void configInterfaceBins() {
		rBtnPorBins.setSelected(true);
		txtValorTotal.setText("");
		txtValorTotalRestoEquipe.setText("");
		txtValorDia.setEnabled(false);
		lblValorDia.setForeground(Color.LIGHT_GRAY);

		txtValorBinsDia.setEnabled(true);
		lblValorBinsDia.setForeground(Color.BLACK);
	}

	private void configInterfaceDia() {
		rBtnPorDia.setSelected(true);
		txtValorTotal.setText("");
		txtValorTotalRestoEquipe.setText("");
		txtValorDia.setEnabled(true);
		lblValorDia.setForeground(Color.BLACK);

		txtValorBinsDia.setEnabled(false);
		txtValorBinsDia.setText("0,00");
		lblValorBinsDia.setForeground(Color.LIGHT_GRAY);
	}

	private void distribuiValoresBins() {

		try {
			int qntdBins = Integer.parseInt(txtQntdBinsDia.getText());
			double valorBins = Double.parseDouble(txtValorBinsDia.getText().replace(',', '.'));
			double valorClassif = Double.parseDouble(txtValorClassif.getText().replace(',', '.'));
			double valorAdicional = 0.0;
			int aux = 0;
			int qntdFaltas = 0;
			int qntdEmpregados = 0;
			int qntdEmpregadosMeios = 0;
			int qntdClassif = 0;
			int qntdClassifMeios = 0;
			int totalEmpregados = modelo.getRowCount();
			int presentes = 0;
			totalGeral = 0;
			totalResto = 0;

			do {

				if (modelo.getValueAt(aux, COLUNA_PRESENCA).toString().equals("N")) {
					qntdFaltas++;
					modelo.setValueAt(false, aux, COLUNA_CLASSIF);
					modelo.setValueAt(0, aux, COLUNA_VALOR);
				}

				if (modelo.getValueAt(aux, COLUNA_PRESENCA).toString().equals("S") && modelo.getValueAt(aux, COLUNA_CLASSIF).toString() == "false") {
					qntdEmpregados++;
				}

				if (modelo.getValueAt(aux, COLUNA_PRESENCA).toString().equals("M/T") && modelo.getValueAt(aux, COLUNA_CLASSIF).toString() == "false") {
					qntdEmpregadosMeios++;
				}

				if (modelo.getValueAt(aux, COLUNA_PRESENCA).toString().equals("S") && modelo.getValueAt(aux, COLUNA_CLASSIF).toString() == "true") {
					qntdClassif++;
				}

				if (modelo.getValueAt(aux, COLUNA_PRESENCA).toString().equals("M/T") && modelo.getValueAt(aux, COLUNA_CLASSIF).toString() == "true") {
					qntdClassifMeios++;
				}

				aux++;
			} while(aux < totalEmpregados);

			totalResto = qntdBins * valorBins;

			presentes = totalEmpregados-(qntdFaltas+qntdClassif+qntdClassifMeios);

			if (qntdEmpregadosMeios <=0)
				valorAdicional = 0;
			else {
				double metade = (totalResto/presentes/2) * qntdEmpregadosMeios;
				valorAdicional = metade/(presentes-qntdEmpregadosMeios);
			}

			aux = 0;

			double valor = 0;
			do {
				if (modelo.getValueAt(aux, COLUNA_PRESENCA).toString().equals("M/T") && modelo.getValueAt(aux, COLUNA_CLASSIF).toString() == "false") {
					valor = ((totalResto/presentes)/2);
					modelo.setValueAt(valor, aux, COLUNA_VALOR);
					totalGeral += valor;
				} else if (modelo.getValueAt(aux, COLUNA_PRESENCA).toString().equals("S") && modelo.getValueAt(aux, COLUNA_CLASSIF).toString() == "false") {
					valor = (totalResto/presentes)+valorAdicional;
					modelo.setValueAt(valor, aux, COLUNA_VALOR);
					totalGeral += valor;
				} else if (modelo.getValueAt(aux, COLUNA_PRESENCA).toString().equals("S") && modelo.getValueAt(aux, COLUNA_CLASSIF).toString() == "true") {
					valor = valorClassif;
					modelo.setValueAt(valor, aux, COLUNA_VALOR);
					totalGeral += valor;
				} else if (modelo.getValueAt(aux, COLUNA_PRESENCA).toString().equals("M/T") && modelo.getValueAt(aux, COLUNA_CLASSIF).toString() == "true") {
					valor = valorClassif/2;
					modelo.setValueAt(valor, aux, COLUNA_VALOR);
					totalGeral += valor;
				}
				aux++;
				valor = 0;
			} while(aux < totalEmpregados);

			txtValorTotalRestoEquipe.setText(decimal.format(totalResto));
			txtValorTotal.setText(decimal.format(totalGeral));

			calculaComissao();

		} catch (NumberFormatException erro) {
			System.out.println("Ainda n�o � poss�vel calcular");
		}
	}

	private void distribuiValoresDia() {
		try {
			double valorDia = Double.parseDouble(txtValorDia.getText().replace(',', '.'));
			double valorClassif = Double.parseDouble(txtValorClassif.getText().replace(',', '.'));
			int aux = 0;
			int qntdFaltas = 0;
			int qntdEmpregados = 0;
			int qntdEmpregadosMeios = 0;
			int qntdClassif = 0;
			int qntdClassifMeios = 0;
			int totalEmpregados = modelo.getRowCount();
			totalGeral = 0;
			totalResto = 0;

			do {

				if (modelo.getValueAt(aux, COLUNA_PRESENCA).toString().equals("N")) {
					qntdFaltas++;
					modelo.setValueAt(false, aux, COLUNA_CLASSIF);
					modelo.setValueAt(0, aux, COLUNA_VALOR);
				}

				if (modelo.getValueAt(aux, COLUNA_PRESENCA).toString().equals("S") && modelo.getValueAt(aux, COLUNA_CLASSIF).toString() == "false") {
					qntdEmpregados++;
				}

				if (modelo.getValueAt(aux, COLUNA_PRESENCA).toString().equals("M/T") && modelo.getValueAt(aux, COLUNA_CLASSIF).toString() == "false") {
					qntdEmpregadosMeios++;
				}

				if (modelo.getValueAt(aux, COLUNA_PRESENCA).toString().equals("S") && modelo.getValueAt(aux, COLUNA_CLASSIF).toString() == "true") {
					qntdClassif++;
				}

				if (modelo.getValueAt(aux, COLUNA_PRESENCA).toString().equals("M/T") && modelo.getValueAt(aux, COLUNA_CLASSIF).toString() == "true") {
					qntdClassifMeios++;
				}

				aux++;
			} while(aux < totalEmpregados);

			System.out.println("Faltas= " + qntdFaltas);
			System.out.println("qntdEmpregados= " + qntdEmpregados);
			System.out.println("qntdEmpregadosMeios= " + qntdEmpregadosMeios);
			System.out.println("qntdClassif= " + qntdClassif);
			System.out.println("qntdClassifMeios= " + qntdClassifMeios);

			totalResto = (qntdEmpregados * valorDia) + ((valorDia/2) * qntdEmpregadosMeios);
			System.out.println(totalResto);

			aux = 0;

			double valor = 0;
			do {
				if (modelo.getValueAt(aux, COLUNA_PRESENCA).toString().equals("M/T") && modelo.getValueAt(aux, COLUNA_CLASSIF).toString() == "false") {
					valor = (valorDia/2);
					modelo.setValueAt(valor, aux, COLUNA_VALOR);
					totalGeral += valor;
				} else if (modelo.getValueAt(aux, COLUNA_PRESENCA).toString().equals("S") && modelo.getValueAt(aux, COLUNA_CLASSIF).toString() == "false") {
					valor = valorDia;
					modelo.setValueAt(valor, aux, COLUNA_VALOR);
					totalGeral += valor;
				} else if (modelo.getValueAt(aux, COLUNA_PRESENCA).toString().equals("S") && modelo.getValueAt(aux, COLUNA_CLASSIF).toString() == "true") {
					valor = valorClassif;
					modelo.setValueAt(valor, aux, COLUNA_VALOR);
					totalGeral += valor;
				} else if (modelo.getValueAt(aux, COLUNA_PRESENCA).toString().equals("M/T") && modelo.getValueAt(aux, COLUNA_CLASSIF).toString() == "true") {
					valor = valorClassif/2;
					modelo.setValueAt(valor, aux, COLUNA_VALOR);
					totalGeral += valor;
				}
				aux++;
				valor = 0;
			} while(aux < totalEmpregados);

			txtValorTotalRestoEquipe.setText(decimal.format(totalResto));
			txtValorTotal.setText(decimal.format(totalGeral));
		} catch (NumberFormatException e) {
			System.out.println("Ainda n�o � poss�vel calcular (dia)");
		}
		calculaComissao();
	}

	public void selecionaEquipe() {
		for (int i = modelo.getRowCount() - 1; i >= 0; i--)
			modelo.removeRow(i);

		adoBO = adoDao.consultaPorCodEquipe(Integer.parseInt(modeloTabEquipe.getValueAt(tabelaEquipe.getSelectedRow(), 0).toString()));
		int index = 0;

		try {
			do {
				modelo.addRow(new Object[] {
						"S",
						new Boolean(false),
						new Integer(adoBO.get(index).getCodigo()),
						adoBO.get(index).getNome(),
						adoBO.get(index).funcaoBO.getNome(),
						new Double(0.0)
				});
				index++;
			} while (index < adoBO.size());

			lblQntdEmpregados.setText("Qntd. Empregados: " + modelo.getRowCount());

		} catch (NullPointerException e1) {}
	}


	private void calculaComissao() {
		try {
			double vlrComissao = Double.parseDouble(txtVlrComissao.getText().trim().replace(',', '.'));
			double valorComissaoIroClassif = Double.parseDouble(txtValorComissaoIroClassif.getText().trim().replace(',', '.'));
			double outrosValoresIro = Double.parseDouble(txtValorOutrosIro.getText().trim().replace(',', '.'));
			int qntdBins = Integer.parseInt(txtQntdBinsDia.getText());
			int qntdEmpregados = 0, qntdEmpregadosMeios = 0, qntdClassif = 0, qntdClassifMeios = 0, x = 0;
			totalComissao = 0;

			do {

				if (modelo.getValueAt(x, COLUNA_PRESENCA).toString().equals("S") && modelo.getValueAt(x, COLUNA_CLASSIF).toString() == "false") {
					qntdEmpregados++;
				}

				if (modelo.getValueAt(x, COLUNA_PRESENCA).toString().equals("M/T") && modelo.getValueAt(x, COLUNA_CLASSIF).toString() == "false") {
					qntdEmpregadosMeios++;
				}

				if (modelo.getValueAt(x, COLUNA_PRESENCA).toString().equals("S") && modelo.getValueAt(x, COLUNA_CLASSIF).toString() == "true") {
					qntdClassif++;
				}

				if (modelo.getValueAt(x, COLUNA_PRESENCA).toString().equals("M/T") && modelo.getValueAt(x, COLUNA_CLASSIF).toString() == "true") {
					qntdClassifMeios++;
				}

				x++;
			} while (x<modelo.getRowCount());

			if (rBtnPorBins.isSelected())
				totalComissao = vlrComissao*qntdBins;
			else if (rBtnPorDia.isSelected())
				totalComissao = (vlrComissao*qntdEmpregados) + ((vlrComissao/2)*qntdEmpregadosMeios);

			totalComissao = totalComissao + (valorComissaoIroClassif*qntdClassif) + ((valorComissaoIroClassif/2)*qntdClassifMeios) + outrosValoresIro;

			txtVlrTotalComissao.setText(decimal.format(totalComissao));
		} catch (NumberFormatException e) {
			System.out.println("ERRO COMISSAO");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object origem = e.getSource();

		if (origem == btnProcuraEmpreiteiro) {
			FrmConsultaEmpreiteiro frame = new FrmConsultaEmpreiteiro(this);
			frame.setVisible(true);
			getDesktopPane().add(frame);
			try {
				frame.setSelected(true);
			}
			catch (PropertyVetoException exc) { }
			FrmMenuGeralMaca.centralizaInternalFrame(frame, getDesktopPane());
		} else if (origem == btnConfirmar) {
			diaBO = new LancaDiaBO();
			diaAdoBO = new ArrayList<DiaEmpregadoBO>();

			if (txtCodigoDia.getText().equals("-"))
				diaBO.setCodigo(diaDao.getUltimoCod()+1);
			else
				diaBO.setCodigo(Integer.parseInt(txtCodigoDia.getText()));

			try {
				diaBO.data = new DateTime(df.parse(txtData.getText()));
			} catch (ParseException e2) {
				JOptionPane.showMessageDialog(this, "Data inv�lida!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtCodigoDia.requestFocus();
				txtCodigoDia.selectAll();
				return;
			}

			if (!txtCodEmpreiteiro.getText().trim().equals("")) {
				diaBO.iroBO.setCodigo(Integer.parseInt(txtCodEmpreiteiro.getText()));
			} else {
				JOptionPane.showMessageDialog(this, "Codigo empreiteiro deve ser preenchido!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtCodEmpreiteiro.requestFocus();
				txtCodEmpreiteiro.selectAll();
				return;
			}

			try {
				diaBO.iroBO.setNome(txtMostraEmpreiteiro.getText());
			} catch (StringVaziaException e3) {}

			if (rBtnPorBins.isSelected())
				diaBO.setMetodo('B');
			else 
				diaBO.setMetodo('D');

			try {
				diaBO.setValorClassif(Double.parseDouble(txtValorClassif.getText().trim().replace(',', '.')));
			} catch (NumberFormatException e4) {
				JOptionPane.showMessageDialog(this, "Valor classificador deve ser num�rico!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtValorClassif.requestFocus();
				txtValorClassif.selectAll();
			} catch (ValorErradoException e4) {
				JOptionPane.showMessageDialog(this, "Valor classificador inv�lido!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtValorClassif.requestFocus();
				txtValorClassif.selectAll();
				return;
			}

			try {
				diaBO.setQntdBinsClassif(Integer.parseInt(txtQntdBinsClassif.getText()));
			} catch (NumberFormatException e3) {
				JOptionPane.showMessageDialog(this, "Quantidade de deve ser num�rico!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtQntdBinsDia.requestFocus();
				txtQntdBinsDia.selectAll();
			} catch (QuantidadeErradaException e3) {
				JOptionPane.showMessageDialog(this, "Quantidade de bins incorreta!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtQntdBinsDia.requestFocus();
				txtQntdBinsDia.selectAll();
				txtQntdBinsDia.getText();
				return;
			}

			try {
				diaBO.setValorComissIroClassif(Double.parseDouble(txtValorComissaoIroClassif.getText().trim().replace(',', '.')));
			} catch (NumberFormatException e3) {
				JOptionPane.showMessageDialog(this, "Valor comiss�o deve ser num�rico!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtValorComissaoIroClassif.requestFocus();
				txtValorComissaoIroClassif.selectAll();
				return;
			} catch (ValorErradoException e3) {
				JOptionPane.showMessageDialog(this, "Valor comiss�o inv�lido!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtValorComissaoIroClassif.requestFocus();
				txtValorComissaoIroClassif.selectAll();
				return;
			}

			try {
				diaBO.setQntBinsEquipe(Integer.parseInt(txtQntdBinsDia.getText()));
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "Quantidade de deve ser num�rico!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtQntdBinsDia.requestFocus();
				txtQntdBinsDia.selectAll();
				return;
			} catch (QuantidadeErradaException e1) {
				JOptionPane.showMessageDialog(this, "Quantidade de bins incorreta!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtQntdBinsDia.requestFocus();
				txtQntdBinsDia.selectAll();
				return;
			}

			try {
				diaBO.setValorBins(Double.parseDouble(txtValorBinsDia.getText().replace(',', '.')));
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "Valor bins deve ser num�rico!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtValorBinsDia.requestFocus();
				txtValorBinsDia.selectAll();
				return;
			} catch (ValorErradoException e1) {
				JOptionPane.showMessageDialog(this, "Valor bins inv�lido!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtValorBinsDia.requestFocus();
				txtValorBinsDia.selectAll();
				return;
			}

			try {
				diaBO.setValorDia(Double.parseDouble(txtValorDia.getText().replace(',', '.')));
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "Valor do dia deve ser num�rico!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtValorDia.requestFocus();
				txtValorDia.selectAll();
				return;
			} catch (ValorErradoException e1) {
				JOptionPane.showMessageDialog(this, "Valor do dia inv�lido!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtValorDia.requestFocus();
				txtValorDia.selectAll();
				return;
			}

			try {
				diaBO.setValorTotalResto(totalResto);
				diaBO.setValorTotal(totalGeral);
				diaBO.setValorComissao(Double.parseDouble(txtVlrComissao.getText().trim().replace(',', '.')));
				diaBO.setValorOutroIro(Double.parseDouble(txtValorOutrosIro.getText().trim().replace(',', '.')));
				diaBO.setValorTotalComissao(totalComissao);
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "Valor deve ser num�rico!", "ERRO", JOptionPane.ERROR_MESSAGE);
				return;
			} 
			catch (ValorErradoException e1) {
				JOptionPane.showMessageDialog(this, "Valor incorreto!", "ERRO", JOptionPane.ERROR_MESSAGE);
				return;
			}

			diaBO.equipeBO.setCodigo(Integer.parseInt(modeloTabEquipe.getValueAt(tabelaEquipe.getSelectedRow(), 0).toString()));
			try {
				diaBO.equipeBO.setNome(modeloTabEquipe.getValueAt(tabelaEquipe.getSelectedRow(), 1).toString());
			} catch (StringVaziaException e2) {}

			diaBO.observacao.setText(txtAreaObserv.getText());

			if (consDia == null)
				diaDao.incluir(diaBO);
			else 
				diaDao.alterar(diaBO);

			try {
				int cont = 0;
				char presenca = 'S';

				if (!txtCodigoDia.getText().equals("-"))
					diaAdoDao.excluir(Integer.parseInt(txtCodigoDia.getText()));

				do {
					diaAdoBO.add(new DiaEmpregadoBO());
					int codigoEmpregado = Integer.parseInt(modelo.getValueAt(cont, COLUNA_CODIGO).toString());
					diaAdoBO.get(cont).adoBO.setCodigo(codigoEmpregado);
					if (modelo.getValueAt(cont, COLUNA_PRESENCA).toString().equals("N"))
						presenca = 'N';
					else if (modelo.getValueAt(cont, COLUNA_PRESENCA).toString().equals("M/T"))
						presenca = 'M';
					else if (modelo.getValueAt(cont, COLUNA_PRESENCA).toString().equals("S"))
						presenca = 'S';

					if (modelo.getValueAt(cont, COLUNA_CLASSIF).toString().equals("true"))
						diaAdoBO.get(cont).setClassificador('S');
					else
						diaAdoBO.get(cont).setClassificador('N');

					diaAdoBO.get(cont).setPresenca(presenca);
					diaAdoBO.get(cont).setValor(Double.parseDouble(modelo.getValueAt(cont, COLUNA_VALOR).toString()));
					diaAdoBO.get(cont).diaBO.setCodigo(diaBO.getCodigo());

					diaAdoDao.incluir(diaAdoBO.get(cont));

					cont++;
				} while (cont < modelo.getRowCount());

				if (consDia == null) {
					txtQntdBinsDia.setText("");
					txtValorClassif.setText("");
					txtValorBinsDia.setText("0,00");
					txtValorTotal.setText("");
					txtValorTotalRestoEquipe.setText("");
					txtValorClassif.requestFocus();
					txtValorDia.setText("0,00");
					txtVlrComissao.setText("");
					txtVlrTotalComissao.setText("");
					txtAreaObserv.setText("");
					JOptionPane.showMessageDialog(this, "Registro salvo com sucesso!", "Registro Salvo", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
					try {
						tabelaEquipe.addRowSelectionInterval(tabelaEquipe.getSelectedRow()+1, tabelaEquipe.getSelectedRow()+1);
					} catch (IllegalArgumentException erro) {
						tabelaEquipe.addRowSelectionInterval(0, 0);
					}
					selecionaEquipe();
				} else {
					int linha = consDia.tabela.getSelectedRow();
					consDia.modelo.setValueAt(df.format(consDia.diaBO.data.toDate()),linha,1);
					consDia.modelo.setValueAt(diaBO.equipeBO.getNome(),linha,2);
					consDia.modelo.setValueAt(diaBO.iroBO.getNome(),linha,3);
					consDia.modelo.setValueAt(diaBO.getValorTotal(),linha,4);
					JOptionPane.showMessageDialog(this, "Registro alterado com sucesso!", "Registro Alterado", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
					doDefaultCloseAction();
				}
			} catch (ValorErradoException e1) {
				
				JOptionPane.showMessageDialog(this, "Valor empregado incorreto!", "ERRO", JOptionPane.ERROR_MESSAGE);
				return;
			}

		} else if (origem == btnCancelar) {
			doDefaultCloseAction();
		} else if (origem == rBtnPorBins) {

			for (int x = 0; x < modelo.getRowCount(); x++)
				modelo.setValueAt(0.0, x, COLUNA_VALOR);

			configInterfaceBins();
			txtQntdBinsDia.setText("0");
			txtValorDia.setText("0,00");

		} else if (origem == rBtnPorDia) {
			
			for (int x = 0; x < modelo.getRowCount(); x++)
				modelo.setValueAt(0.0, x, COLUNA_VALOR);

			configInterfaceDia();
			txtValorBinsDia.setText("0,00");

		}
	}

	private void distribuiValores() {
		if (rBtnPorBins.isSelected())
			distribuiValoresBins();
		else
			distribuiValoresDia();
	}
}