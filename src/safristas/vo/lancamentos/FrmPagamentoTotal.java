package safristas.vo.lancamentos;

import impressos.Cheques;
import impressos.ReciboEmpreiteiro;
import impressos.ReciboSafristas;
import safristas.bo.DadosChequeBO;
import safristas.bo.EmpregadoBO;
import safristas.bo.EmpregadorBO;
import safristas.bo.safristas.EmpreiteiroBO;
import safristas.bo.safristas.PagamentoTotalBO;
import safristas.bo.safristas.PagamentoUnitarioBO;
import safristas.dao.AdiantamentoDao;
import safristas.dao.EmpregadoDao;
import safristas.dao.EmpregadorDao;
import safristas.dao.safristas.EmpreiteiroDao;
import safristas.dao.safristas.PagamentoTotalDao;
import safristas.dao.safristas.PagamentoUnitarioDao;
import safristas.vo.FrmMenuGeralMaca;
import safristas.vo.consultcadast.FrmConsultaEmpregador;
import safristas.vo.consultcadast.FrmConsultaEmpreiteiro;
import safristas.vo.consultcadast.FrmConsultaPagamento;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.text.MaskFormatter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import util.NumberRenderer;
import util.Numerals;
import util.ModeloTabela;
import exceptions.StringVaziaException;
import exceptions.ValorErradoException;

public class FrmPagamentoTotal extends JInternalFrame implements ActionListener {
	private static final long serialVersionUID = -7454262646712060816L;

	private MaskFormatter mascaraData;
	protected DecimalFormat decimal = new DecimalFormat("#,##0.00");

	private GridBagConstraints constraints = new GridBagConstraints();
	private int qntdEmpr = 0, qntdBins = 0;
	protected int codAdiantIro = 0;
	protected double valorTotal = 0.0, valorEmpreiteiro, valorTotalEmpreiteiro = 0.0, valorPorEmpr = 0.0, valorBins = 0.0, valorEmpregados = 0.0,
	valorDescIro = 0.0, valorDescontoChq = 0.0;
	boolean auxSair = true;
	boolean[] edicao = {true, false, false, false, false, false, false, false, false, false, false};
	private ArrayList<Double> valoresEmpregados = new ArrayList<Double>();
	private JLabel lblCodigo, lblData, lblDataInicio, lblDataFinal, lblCodEmpregador, lblCodEmpreiteiro, lblValorEmpregados, lblPagaEmpreiteiro,
	lblQntdEmpregados, lblValorTotalEmpreiteiro, lblTotalBins, lblHistoricoDescontoIro,
	lblValorDescontoIro, lblValorTotalPgto, lblInstrucaoDesconto;
	protected JTextField txtCodigo, txtValorEmpregados, txtQntdBins, txtValorTotalPgto, txtValorPorEmpregado, txtQntdEmpregados,
	txtValorTotalEmpreiteiro, txtMostraDescontoIro, txtValorDescontoIro, txtValorPorBins;
	public JTextField txtCodigoEmpregador, txtMostraEmpregador, txtCodEmpreiteiro, txtMostraEmpreiteiro;
	private JButton btnProcuraEmpregador, btnProcuraEmpreiteiro, btnGeraRecibos, btnSelecionarTodos, btnDeselecionarTodos,
	btnDescontoCheque, btnTrocarDeEquipe, btnConfirmar, btnLimpar, btnCancelar;
	private JFormattedTextField txtData, txtDatainicial, txtDataFinal;
	private JCheckBox cbPagarEmpreiteiro;
	protected String nome, codigosEmpregados = "";
	public EmpregadorBO dorBO;
	public EmpregadorDao dorDao = new EmpregadorDao();
	public EmpreiteiroBO iroBO;
	public EmpreiteiroDao iroDao = new EmpreiteiroDao();
	public EmpregadoBO adoBO;
	public EmpregadoDao adoDao = new EmpregadoDao();
	public ArrayList<PagamentoUnitarioBO> pgtoUnitBO = new ArrayList<PagamentoUnitarioBO>();
	public PagamentoUnitarioDao pgtoUnitDao = new PagamentoUnitarioDao();
	public PagamentoTotalBO pgtoTotalBO;
	public PagamentoTotalDao pgtoTotalDao = new PagamentoTotalDao();
	public AdiantamentoDao adiantDao = new AdiantamentoDao();
	private Locale brasil = new Locale("pt", "BR");
	protected DateTimeZone zona = DateTimeZone.forID("Etc/GMT+3");
	protected DateTime data = new DateTime(zona);
	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy", brasil);

	protected JTable tabela;
	protected ModeloTabela modelo;
	protected FrmConsultaPagamento consPag = null;

	public static final int COLUNA_PRESENCA = 0;
	public static final int COLUNA_CODIGO = 1;
	public static final int COLUNA_NOME = 2;
	public static final int COLUNA_VALOR = 3;
	public static final int COLUNA_ACRESCIMO = 4;
	public static final int COLUNA_DESCONTO = 5;
	public static final int COLUNA_DESCHAT = 6;
	public static final int COLUNA_VALORTOTAL = 7;
	public static final int COLUNA_DIASTRAB = 8;
	public static final int COLUNA_FUNCAO = 9;
	public static final int COLUNA_CPF = 10;
	public static final int COLUNA_OBDESCONTO = 11;
	public static final int COLUNA_CODADIANTAMENTO = 12;

	public FrmPagamentoTotal(FrmConsultaPagamento consPag) {
		this();

		for (int i = modelo.getRowCount() - 1; i >= 0; i--)
			modelo.removeRow(i);

		boolean[] edicao = {false, false, false, false, false, false, false, false, false, false, false, false};

		this.consPag = consPag;
		txtCodigo.setText(String.valueOf(consPag.pgTotalBO.getCodigo()));
		txtData.setValue(df.format(consPag.pgTotalBO.data.toDate()));
		txtDatainicial.setValue(df.format(consPag.pgTotalBO.dataInicial.toDate()));
		txtDataFinal.setValue(df.format(consPag.pgTotalBO.dataFinal.toDate()));
		txtCodigoEmpregador.setText(String.valueOf(consPag.pgTotalBO.dorBO.getCodigo()));
		txtMostraEmpregador.setText(consPag.pgTotalBO.dorBO.getNome());
		txtCodEmpreiteiro.setText(String.valueOf(consPag.pgTotalBO.iroBO.getCodigo()));
		txtCodEmpreiteiro.setFocusable(false);
		txtCodEmpreiteiro.setEditable(false);
		txtMostraEmpreiteiro.setText(consPag.pgTotalBO.iroBO.getNome());
		int i = 0;
		do {
			modelo.addRow(new Object[] {
					new Boolean(true),
					consPag.pgUnitBO.get(i).adoBO.getCodigo(),
					consPag.pgUnitBO.get(i).adoBO.getNome(),
					consPag.pgUnitBO.get(i).getValor(),
					consPag.pgUnitBO.get(i).getValorAcrescimo(),
					consPag.pgUnitBO.get(i).getDesconto(),
					consPag.pgUnitBO.get(i).getDescHAT(),
					consPag.pgUnitBO.get(i).getValorTotal(),
					consPag.pgUnitBO.get(i).getQntdDias(),
					consPag.pgUnitBO.get(i).adoBO.funcaoBO.getNome(),
					consPag.pgUnitBO.get(i).adoBO.getCpf(),
					consPag.pgUnitBO.get(i).histDesconto.getText(),
					consPag.pgUnitBO.get(i).adiantBO.getCodigo()
			});

			i++;	
		} while (i < consPag.pgUnitBO.size());

		geraStringCodigos();			

		valorBins = consPag.pgTotalBO.getValorBins();
		valorTotalEmpreiteiro = consPag.pgTotalBO.getValorEmpreiteiro();
		valorPorEmpr = consPag.pgTotalBO.getValorEmpregado();
		valorEmpregados = consPag.pgTotalBO.getValorTotalEmpregados();
		valorTotal = consPag.pgTotalBO.getValorTotal();
		valorDescIro = consPag.pgTotalBO.getValorDesconto();
		codAdiantIro = consPag.pgTotalBO.adiantBO.getCodigo();

		valorEmpreiteiro = valorTotalEmpreiteiro - valorDescIro;

		txtValorTotalEmpreiteiro.setText(decimal.format(valorEmpreiteiro));
		txtValorEmpregados.setText(decimal.format(valorEmpregados));
		txtValorTotalPgto.setText(decimal.format(valorTotal));
		txtValorDescontoIro.setText(decimal.format(valorDescIro));

		txtQntdEmpregados.setText(String.valueOf(consPag.pgTotalBO.getQntdEmpregado()));
		txtQntdBins.setText(String.valueOf(consPag.pgTotalBO.getQntdBins()));
		txtMostraDescontoIro.setText(consPag.pgTotalBO.getHistDesconto());

		if (valorTotalEmpreiteiro == 0.00) {
			cbPagarEmpreiteiro.setSelected(false);
		}

		btnSelecionarTodos.setEnabled(false);
		btnDeselecionarTodos.setEnabled(false);
		btnConfirmar.setText("Alterar (F1)");
		btnLimpar.setVisible(false);
		btnProcuraEmpreiteiro.setEnabled(false);
		btnGeraRecibos.setEnabled(true);
		btnTrocarDeEquipe.setEnabled(true);
		auxSair = false;
	}

	public FrmPagamentoTotal() {

		super("Pagamento - Fechamento",true,true,false,true);

		setSize(980, 660);
		setResizable(false);
		setTitle("Pagamentos - Safristas");
		setFrameIcon(new ImageIcon(getClass().getResource("/icons/icon_logo_varaschin.gif")));

		Font f = new Font("Arial", Font.PLAIN, 14);

		JPanel painelGeral = new JPanel();
		painelGeral.setLayout(new BorderLayout(2, 2));

		JPanel painelCima = new JPanel();
		painelCima.setLayout(new GridBagLayout());

		JPanel painelEsq = new JPanel();
		painelEsq.setLayout(new GridBagLayout());

		JPanel painelMeioGeral = new JPanel();
		painelMeioGeral.setLayout(new GridBagLayout());

		JPanel painelBaixo = new JPanel();
		painelBaixo.setLayout(new GridBagLayout());

		constraints.insets = new Insets(2, 4, 2, 4);

		try {
			mascaraData = new MaskFormatter("##/##/####");
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(this, "Data incorreta ou inválida!", "ERRO", JOptionPane.ERROR_MESSAGE);
			return;
		}

		lblCodigo = new JLabel("Código");
		lblCodigo.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelCima.add(lblCodigo, constraints);

		txtCodigo = new JTextField(5);
		txtCodigo.setEditable(false);
		txtCodigo.setFocusable(false);
		txtCodigo.setFont(f);
		txtCodigo.setText(String.valueOf(pgtoTotalDao.getUltimoCod()+1));
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		painelCima.add(txtCodigo, constraints);

		lblData = new JLabel("Data");
		lblData.setFont(f);
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelCima.add(lblData, constraints);

		txtData = new JFormattedTextField(mascaraData);
		txtData.setColumns(8);
		txtData.setFont(f);
		txtData.setValue(df.format(data.toDate()));
		constraints.gridx = 3;
		constraints.gridy = 0;
		constraints.gridwidth = 3;
		constraints.anchor = GridBagConstraints.WEST;
		painelCima.add(txtData, constraints);
		constraints.gridwidth = 1;

		lblDataInicio = new JLabel("Data inicial");
		lblDataInicio.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelCima.add(lblDataInicio, constraints);

		txtDatainicial = new JFormattedTextField(mascaraData);
		txtDatainicial.setColumns(8);
		txtDatainicial.setFont(f);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelCima.add(txtDatainicial, constraints);
		constraints.gridwidth = 1;

		lblDataFinal = new JLabel("Data final");
		lblDataFinal.setFont(f);
		constraints.gridx = 3;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelCima.add(lblDataFinal, constraints);

		txtDataFinal = new JFormattedTextField(mascaraData);
		txtDataFinal.setFont(f);
		txtDataFinal.setColumns(8);
		constraints.gridx = 5;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.WEST;
		painelCima.add(txtDataFinal, constraints);
		constraints.gridwidth = 1;

		lblCodEmpregador = new JLabel("Cód. Empregador");
		lblCodEmpregador.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		painelCima.add(lblCodEmpregador, constraints);

		txtCodigoEmpregador = new JTextField(5);
		txtCodigoEmpregador.setFont(f);
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelCima.add(txtCodigoEmpregador, constraints);
		txtCodigoEmpregador.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				dorBO = new EmpregadorBO();
				try {
					dorBO = dorDao.consultaPorCodigo(Integer.parseInt(txtCodigoEmpregador.getText())).get(0);
				} catch (NumberFormatException erro) {

				}
				txtMostraEmpregador.setText(dorBO.getNome());
			}
		});

		txtMostraEmpregador = new JTextField(30);
		txtMostraEmpregador.setFont(f);
		txtMostraEmpregador.setEditable(false);
		txtMostraEmpregador.setFocusable(false);
		constraints.gridx = 2;
		constraints.gridy = 2;
		constraints.gridwidth = 6;
		constraints.anchor = GridBagConstraints.WEST;
		painelCima.add(txtMostraEmpregador, constraints);
		constraints.gridwidth = 1;

		btnProcuraEmpregador = new JButton("Procurar", new ImageIcon(getClass().getResource("/icons/icon_procurar.gif")));
		btnProcuraEmpregador.setFocusable(false);
		constraints.gridx = 8;
		constraints.gridy = 2;
		constraints.ipady = -5;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelCima.add(btnProcuraEmpregador, constraints);
		constraints.gridwidth = 1;
		constraints.ipady = 0;

		//------------------------------------------------------------

		JPanel painelInterno = new JPanel();
		painelInterno.setLayout(new BorderLayout(2, 2));

		JPanel painelInternoCima = new JPanel();
		painelInternoCima.setLayout(new GridBagLayout());

		JPanel painelInternoBaixo = new JPanel();
		painelInternoBaixo.setLayout(new GridBagLayout());

		ArrayList<Object> dados = new ArrayList<Object>();

		String[] colunas = new String[] {" ", "Código", "Nome", "Valor", "Acrescimos", "Descontos", "Desc. Hab./Ali./Tra.", "Valor Final", "Nº dias trab.", "Função", "CPF", 
				"Observação Desconto", "Cód. Adiant."};

		modelo = new ModeloTabela(dados, colunas, edicao);
		tabela = new JTable(modelo);
		tabela.getColumnModel().getColumn(0).setPreferredWidth(30);
		tabela.getColumnModel().getColumn(0).setResizable(false);
		tabela.getColumnModel().getColumn(1).setPreferredWidth(50);
		tabela.getColumnModel().getColumn(1).setResizable(false);
		tabela.getColumnModel().getColumn(2).setPreferredWidth(300);
		tabela.getColumnModel().getColumn(2).setResizable(true);
		tabela.getColumnModel().getColumn(3).setPreferredWidth(90);
		tabela.getColumnModel().getColumn(3).setResizable(true);
		tabela.getColumnModel().getColumn(3).setCellRenderer(NumberRenderer.getCurrencyRenderer());
		tabela.getColumnModel().getColumn(4).setPreferredWidth(90);
		tabela.getColumnModel().getColumn(4).setResizable(true);
		tabela.getColumnModel().getColumn(4).setCellRenderer(NumberRenderer.getCurrencyRenderer());
		tabela.getColumnModel().getColumn(5).setPreferredWidth(90);
		tabela.getColumnModel().getColumn(5).setResizable(false);
		tabela.getColumnModel().getColumn(5).setCellRenderer(NumberRenderer.getCurrencyRenderer());
		tabela.getColumnModel().getColumn(6).setPreferredWidth(90);
		tabela.getColumnModel().getColumn(6).setResizable(true);
		tabela.getColumnModel().getColumn(6).setCellRenderer(NumberRenderer.getCurrencyRenderer());
		tabela.getColumnModel().getColumn(7).setPreferredWidth(90);
		tabela.getColumnModel().getColumn(7).setResizable(true);
		tabela.getColumnModel().getColumn(7).setCellRenderer(NumberRenderer.getCurrencyRenderer());
		tabela.getColumnModel().getColumn(8).setPreferredWidth(70);
		tabela.getColumnModel().getColumn(8).setResizable(true);
		tabela.getColumnModel().getColumn(8).setCellRenderer(NumberRenderer.getIntegerRenderer());
		tabela.getColumnModel().getColumn(9).setPreferredWidth(180);
		tabela.getColumnModel().getColumn(9).setResizable(false);
		tabela.getColumnModel().getColumn(10).setPreferredWidth(150);
		tabela.getColumnModel().getColumn(10).setResizable(true);
		tabela.getColumnModel().getColumn(11).setPreferredWidth(270);
		tabela.getColumnModel().getColumn(11).setResizable(true);
		tabela.getColumnModel().getColumn(12).setPreferredWidth(80);
		tabela.getColumnModel().getColumn(12).setResizable(true);
		tabela.getColumnModel().getColumn(12).setCellRenderer(NumberRenderer.getIntegerRenderer());
		tabela.getTableHeader().setReorderingAllowed(false);  
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane rolagemTabela = new JScrollPane(tabela);
		painelInterno.add(rolagemTabela, BorderLayout.CENTER);

		tabela.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				int linha = tabela.rowAtPoint(e.getPoint());
				int coluna = tabela.columnAtPoint(e.getPoint());
				int qntdEmpr = Integer.parseInt(txtQntdEmpregados.getText());

				if (consPag == null) {

					if (linha >= 0 && coluna == 0) {
						if ((Boolean)modelo.getValueAt(linha, coluna) == false) {
							modelo.setValueAt(new Double(0.00), linha, COLUNA_VALOR);
							modelo.setValueAt(new Double(0.00), linha, COLUNA_ACRESCIMO);
							modelo.setValueAt(new Double(0.00), linha, COLUNA_DESCONTO);
							modelo.setValueAt(new Double(0.00), linha, COLUNA_VALORTOTAL);
							modelo.setValueAt("", linha, COLUNA_OBDESCONTO);
							modelo.setValueAt(0, linha, COLUNA_CODADIANTAMENTO);
							calculaTotalEmpreiteiro();
							txtQntdEmpregados.setText(String.valueOf(qntdEmpr-1));
						} else {
							modelo.setValueAt(valoresEmpregados.get(linha), linha, COLUNA_VALOR);
							modelo.setValueAt(valoresEmpregados.get(linha), linha, COLUNA_VALORTOTAL);
							calculaTotalEmpreiteiro();
							txtQntdEmpregados.setText(String.valueOf(qntdEmpr+1));
						}
						if (!cbPagarEmpreiteiro.isSelected())
							cbPagarEmpreiteiro.doClick();
					}
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				int coluna = tabela.columnAtPoint(e.getPoint());

				if (btnConfirmar.isEnabled()) {
					if (e.getClickCount() == 2) {
						if (coluna >= COLUNA_CODIGO) {
							FrmDescAcrescPagamentos fr = new FrmDescAcrescPagamentos(FrmPagamentoTotal.this, 'A');
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
			}
		});

		tabela.setFocusable(false);

		lblCodEmpreiteiro = new JLabel("Cód. Empreiteiro");
		lblCodEmpreiteiro.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.EAST;
		painelCima.add(lblCodEmpreiteiro, constraints);

		txtCodEmpreiteiro = new JTextField(5);
		txtCodEmpreiteiro.setFont(f);
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.WEST;
		painelCima.add(txtCodEmpreiteiro, constraints);
		txtCodEmpreiteiro.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				preencheTabela();
			}
		});

		txtMostraEmpreiteiro = new JTextField(30);
		txtMostraEmpreiteiro.setEditable(false);
		txtMostraEmpreiteiro.setFont(f);
		txtMostraEmpreiteiro.setFocusable(false);
		constraints.gridx = 2;
		constraints.gridy = 3;
		constraints.gridwidth = 6;
		constraints.anchor = GridBagConstraints.WEST;
		painelCima.add(txtMostraEmpreiteiro, constraints);
		constraints.gridwidth = 1;

		btnProcuraEmpreiteiro = new JButton("Procurar", new ImageIcon(getClass().getResource("/icons/icon_procurar.gif")));
		btnProcuraEmpreiteiro.setFocusable(false);
		constraints.gridx = 8;
		constraints.gridy = 3;
		constraints.ipady = -5;
		constraints.anchor = GridBagConstraints.WEST;
		painelCima.add(btnProcuraEmpreiteiro, constraints);
		constraints.ipady = 0;

		//----------------------------------------------

		btnSelecionarTodos = new JButton(new ImageIcon(getClass().getResource("/icons/icon_cbok.gif")));
		btnSelecionarTodos.setToolTipText("Selecionar todos");
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(2, -200, 2, 4);
		painelInternoBaixo.add(btnSelecionarTodos, constraints);

		btnDeselecionarTodos = new JButton(new ImageIcon(getClass().getResource("/icons/icon_cbempty.gif")));
		btnDeselecionarTodos.setToolTipText("Deselecionar todos");
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(2, -150, 2, 4);
		painelInternoBaixo.add(btnDeselecionarTodos, constraints);

		lblValorEmpregados = new JLabel("Valor total empregados");
		lblValorEmpregados.setFont(f);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.insets = new Insets(4, 6, 2, 4);
		painelInternoBaixo.add(lblValorEmpregados, constraints);

		txtValorEmpregados = new JTextField(8);
		txtValorEmpregados.setFont(f);
		txtValorEmpregados.setEditable(false);
		txtValorEmpregados.setFocusable(false);
		txtValorEmpregados.setText("0,00");
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(4, 2, 2, 4);
		painelInternoBaixo.add(txtValorEmpregados, constraints);
		
		btnDescontoCheque = new JButton("Desconto Cheques");
		btnDescontoCheque.setFont(f);
		constraints.gridx = 3;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.gridwidth = 6;
		painelInternoBaixo.add(btnDescontoCheque, constraints);
		constraints.gridwidth = 1;
		
		JPanel painelEmpreiteiro = new JPanel();
		painelEmpreiteiro.setLayout(new GridBagLayout());

		lblPagaEmpreiteiro = new JLabel("Pagar empreiteiro?");
		lblPagaEmpreiteiro.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelEmpreiteiro.add(lblPagaEmpreiteiro, constraints);

		cbPagarEmpreiteiro = new JCheckBox("", true);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(2, 0, 2, 4);
		painelEmpreiteiro.add(cbPagarEmpreiteiro, constraints);
		constraints.insets = new Insets(2, 4, 2, 4);

		lblQntdEmpregados = new JLabel("Qntd de empregados");
		lblQntdEmpregados.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelEmpreiteiro.add(lblQntdEmpregados, constraints);

		txtQntdEmpregados = new JTextField(4);
		txtQntdEmpregados.setFont(f);
		txtQntdEmpregados.setEditable(false);
		txtQntdEmpregados.setFocusable(false);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.WEST;
		painelEmpreiteiro.add(txtQntdEmpregados, constraints);

		lblTotalBins = new JLabel("Qntd total de Bins");
		lblTotalBins.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		painelEmpreiteiro.add(lblTotalBins, constraints);

		txtQntdBins = new JTextField(4);
		txtQntdBins.setFont(f);
		txtQntdBins.setEditable(false);
		txtQntdBins.setFocusable(false);
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelEmpreiteiro.add(txtQntdBins, constraints);

		lblHistoricoDescontoIro = new JLabel("Hist. Desconto");
		lblHistoricoDescontoIro.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.EAST;
		painelEmpreiteiro.add(lblHistoricoDescontoIro, constraints);

		txtMostraDescontoIro = new JTextField(17);
		txtMostraDescontoIro.setFont(f);
		txtMostraDescontoIro.setEditable(false);
		txtMostraDescontoIro.setFocusable(false);
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelEmpreiteiro.add(txtMostraDescontoIro, constraints);
		constraints.gridwidth = 1;

		lblValorDescontoIro = new JLabel("Valor Desconto");
		lblValorDescontoIro.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.EAST;
		painelEmpreiteiro.add(lblValorDescontoIro, constraints);

		txtValorDescontoIro = new JTextField(8);
		txtValorDescontoIro.setFont(f);
		txtValorDescontoIro.setEditable(false);
		txtValorDescontoIro.setFocusable(false);
		txtValorDescontoIro.setText("0,00");
		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelEmpreiteiro.add(txtValorDescontoIro, constraints);
		constraints.gridwidth = 1;
		txtValorDescontoIro.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					FrmDescAcrescPagamentos fr = new FrmDescAcrescPagamentos(FrmPagamentoTotal.this, 'I');
					fr.setVisible(true);
					getDesktopPane().add(fr);
					try {
						fr.setSelected(true);
					}
					catch (PropertyVetoException exc) { }
					FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
				}
			}
		});

		lblValorTotalEmpreiteiro = new JLabel("Valor total empreiteiro");
		lblValorTotalEmpreiteiro.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.anchor = GridBagConstraints.EAST;
		painelEmpreiteiro.add(lblValorTotalEmpreiteiro, constraints);

		txtValorTotalEmpreiteiro = new JTextField(8);
		txtValorTotalEmpreiteiro.setFont(f);
		txtValorTotalEmpreiteiro.setFocusable(false);
		txtValorTotalEmpreiteiro.setEditable(false);
		txtValorTotalEmpreiteiro.setText("0,00");
		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelEmpreiteiro.add(txtValorTotalEmpreiteiro, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridheight = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		painelEmpreiteiro.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Valores Empreiteiro"));
		painelInternoBaixo.add(painelEmpreiteiro, constraints);
		constraints.gridheight = 1;
		constraints.weightx = 0;

		lblInstrucaoDesconto = new JLabel("Clique 2 vezes no empregado e/ou no valor desconto empreiteiro p/ lançar os descontos.");
		lblInstrucaoDesconto.setFont(new Font("Arial", Font.PLAIN, 10));
		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.gridwidth = 3;
		constraints.insets = new Insets(0, 6, 4, 4);
		constraints.anchor = GridBagConstraints.WEST;
		painelInternoBaixo.add(lblInstrucaoDesconto, constraints);
		constraints.insets = new Insets(2, 4, 2, 4);
		constraints.gridwidth = 1;

		lblValorTotalPgto = new JLabel("Valor Total");
		lblValorTotalPgto.setFont(f);
		constraints.gridx = 3;
		constraints.gridy = 1;
		constraints.gridheight = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(2, 4, 5, 4);
		painelInternoBaixo.add(lblValorTotalPgto, constraints);

		txtValorTotalPgto = new JTextField(8);
		txtValorTotalPgto.setFont(f);
		txtValorTotalPgto.setBackground(new Color(187,244,188));
		txtValorTotalPgto.setEditable(false);
		txtValorTotalPgto.setFocusable(false);
		txtValorTotalPgto.setText("0,00");
		constraints.gridx = 4;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		painelInternoBaixo.add(txtValorTotalPgto, constraints);
		constraints.gridheight = 1;
		
		btnGeraRecibos = new JButton("<html><center>Gerar Recibos<br> e Cheques</center></html>", new ImageIcon(getClass().getResource("/icons/icon_recibo.gif")));
		btnGeraRecibos.setIconTextGap(8);
		btnGeraRecibos.setEnabled(false);
		constraints.gridx = 3;
		constraints.gridy = 2;
		constraints.ipadx = 0;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.SOUTH;
		painelInternoBaixo.add(btnGeraRecibos, constraints);
		constraints.gridwidth = 1;
		constraints.insets = new Insets(2, 4, 2, 4);
		
		btnTrocarDeEquipe = new JButton("Trocar equipe");
		btnTrocarDeEquipe.setFont(f);
		btnTrocarDeEquipe.setEnabled(false);
		constraints.gridx = 3;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		painelInternoBaixo.add(btnTrocarDeEquipe, constraints);
		constraints.gridwidth = 1;
		constraints.ipady = 0;

		painelCima.setBorder(BorderFactory.createEtchedBorder());
		painelInternoBaixo.setBorder(BorderFactory.createEtchedBorder());
		painelGeral.add(painelCima, BorderLayout.NORTH);
		painelInterno.add(painelInternoCima, BorderLayout.NORTH);
		painelInterno.add(painelInternoBaixo, BorderLayout.SOUTH);
		painelGeral.add(painelInterno, BorderLayout.CENTER);

		//----------------------------------------------

		constraints.ipadx = 20;

		btnConfirmar = new JButton("Confirmar (F1)", new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelBaixo.add(btnConfirmar, constraints);
		btnConfirmar.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F1"), "KEY_F1");
		btnConfirmar.getActionMap().put("KEY_F1", new AbstractAction() {
			private static final long serialVersionUID = -3116644525429488236L;

			public void actionPerformed(ActionEvent evt) {
				btnConfirmar.doClick();
			}
		});

		btnLimpar = new JButton("Limpar (F2)", new ImageIcon(getClass().getResource("/icons/icon_limpar.gif")));
		constraints.ipady = -2;
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelBaixo.add(btnLimpar, constraints);
		constraints.ipady = 0;
		btnLimpar.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F2"), "KEY_F2");
		btnLimpar.getActionMap().put("KEY_F2", new AbstractAction() {
			private static final long serialVersionUID = -4028184566520528639L;

			public void actionPerformed(ActionEvent evt) {
				btnLimpar.doClick();
			}
		});

		btnCancelar = new JButton("Cancelar (F4)", new ImageIcon(getClass().getResource("/icons/icon_cancelar.gif")));
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelBaixo.add(btnCancelar, constraints);
		btnCancelar.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F4"), "KEY_F4");
		btnCancelar.getActionMap().put("KEY_F4", new AbstractAction() {
			private static final long serialVersionUID = 6444953566905530865L;

			public void actionPerformed(ActionEvent evt) {
				btnCancelar.doClick();
			}
		});

		painelBaixo.setBorder(BorderFactory.createEtchedBorder());
		painelGeral.add(painelBaixo, BorderLayout.SOUTH);

		Container p = getContentPane();
		p.add(painelGeral);

		btnGeraRecibos.addActionListener(this);
		btnTrocarDeEquipe.addActionListener(this);
		btnProcuraEmpregador.addActionListener(this);
		btnProcuraEmpreiteiro.addActionListener(this);
		btnSelecionarTodos.addActionListener(this);
		btnDeselecionarTodos.addActionListener(this);
		btnDescontoCheque.addActionListener(this);
		cbPagarEmpreiteiro.addActionListener(this);
		btnConfirmar.addActionListener(this);
		btnLimpar.addActionListener(this);
		btnCancelar.addActionListener(this);
	}

	public void preencheTabela() {
		txtValorTotalPgto.setText("");
		txtValorTotalEmpreiteiro.setText("");
		txtQntdEmpregados.setText("");
		txtQntdBins.setText("");
		valoresEmpregados.clear();

		iroBO = new EmpreiteiroBO();
		int codigoEmpreiteiro = Integer.parseInt(txtCodEmpreiteiro.getText());
		iroBO = iroDao.consultaPorCodigo(codigoEmpreiteiro).get(0);
		txtMostraEmpreiteiro.setText(iroBO.getNome());
		DateTime dataInicial = new DateTime(zona);
		DateTime dataFinal = new DateTime(zona);

		try {
			dataInicial = new DateTime(df.parse(txtDatainicial.getValue().toString()));
			dataFinal = new DateTime(df.parse(txtDataFinal.getValue().toString()));
		} catch (ParseException e1) {}

		qntdBins = pgtoTotalDao.consultaQntdBins(codigoEmpreiteiro, dataInicial, dataFinal);
		valorTotalEmpreiteiro = pgtoTotalDao.consultaValorEmpreiteiro(codigoEmpreiteiro, dataInicial, dataFinal);
		valorEmpreiteiro = valorTotalEmpreiteiro;

		for (int i = modelo.getRowCount() - 1; i >= 0; i--)
			modelo.removeRow(i);

		pgtoUnitDao = new PagamentoUnitarioDao();
		ArrayList<PagamentoUnitarioBO> pgUnitBO = pgtoUnitDao.consultaPorPeriodoECod(dataInicial, dataFinal, codigoEmpreiteiro);

		int i = 0;
		do {
			if (pgUnitBO.get(i).getQntdDias() != 0) {
				modelo.addRow(new Object[] {
						new Boolean(true),
						pgUnitBO.get(i).adoBO.getCodigo(),
						pgUnitBO.get(i).adoBO.getNome(),
						pgUnitBO.get(i).getValor(),
						new Double(0.00),
						new Double(0.00),
						new Double(0.00),
						pgUnitBO.get(i).getValor(),
						pgUnitBO.get(i).getQntdDias(),
						pgUnitBO.get(i).adoBO.funcaoBO.getNome(),
						pgUnitBO.get(i).adoBO.getCpf(),
						"",
						new Integer(0)
				});
				valoresEmpregados.add(pgUnitBO.get(i).getValor());
			}

			i++;
		} while (i < pgUnitBO.size());
		calculaTotal();
		txtQntdEmpregados.setText(String.valueOf(modelo.getRowCount()));
		txtQntdBins.setText(String.valueOf(qntdBins));
		qntdEmpr = Integer.parseInt(txtQntdEmpregados.getText());
		txtValorTotalEmpreiteiro.setText(decimal.format(valorEmpreiteiro));
	}

	public void calculaTotal() {
		int i = 0;
		double soma = 0.0;
		do {
			soma += (Double)modelo.getValueAt(i, COLUNA_VALORTOTAL);
			i++;
		} while (i < tabela.getRowCount());

		txtValorEmpregados.setText(decimal.format(soma));

		valorTotal = soma + valorEmpreiteiro;

		txtValorTotalPgto.setText(decimal.format(valorTotal));
	}

	public void calculaTotalEmpreiteiro() {

		valorEmpreiteiro = valorTotalEmpreiteiro - valorDescIro;

		if (valorEmpreiteiro < 0) {
			valorEmpreiteiro = valorTotalEmpreiteiro;
			txtMostraDescontoIro.setText("");
			txtValorDescontoIro.setText("");
			JOptionPane.showMessageDialog(this, "Valor final do empreiteiro não pode ser negativo!", "Saldo Negativo", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_cancelar.gif")));
		}
		txtValorTotalEmpreiteiro.setText(decimal.format(valorEmpreiteiro));

		calculaTotal();
	}

	public void geraCheques() {
		Numerals e = new Numerals(2);
		List<DadosChequeBO> chequeBO = new ArrayList<DadosChequeBO>();
		int aux = 0;

		do {
			chequeBO.add(new DadosChequeBO());
			double valor = Double.parseDouble(modelo.getValueAt(aux, COLUNA_VALORTOTAL).toString());
			chequeBO.get(aux).setNome(modelo.getValueAt(aux, COLUNA_NOME).toString());
			chequeBO.get(aux).setValor(decimal.format(valor-valorDescontoChq));
			String extenso = e.toString(BigDecimal.valueOf(valor-valorDescontoChq));
			chequeBO.get(aux).setValorExtenso(extenso.toUpperCase());
			aux++;
		} while (aux < tabela.getRowCount());

		if (cbPagarEmpreiteiro.isSelected()) {
			chequeBO.add(new DadosChequeBO());
			double valorEmpreiteiroChq = valorEmpreiteiro;
			String extensoIro = e.toString(BigDecimal.valueOf(valorEmpreiteiroChq));

			chequeBO.get(aux).setValor(decimal.format(valorEmpreiteiroChq));
			chequeBO.get(aux).setValorExtenso(extensoIro.toUpperCase());
			chequeBO.get(aux).setNome(txtMostraEmpreiteiro.getText());
		}

		Cheques relCheque = new Cheques();
		relCheque.geraCheques(chequeBO);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object origem = e.getSource();
		valorEmpregados = valorTotal - valorEmpreiteiro;

		if (origem == btnConfirmar) {
			int x = tabela.getRowCount();

			if (consPag == null) {
				do {
					Boolean selecionado = (Boolean) modelo.getValueAt(x-1, COLUNA_PRESENCA);
					if (!selecionado)
						modelo.removeRow(x-1);
					x--;
				} while (x>0);
			}

			pgtoTotalBO = new PagamentoTotalBO();

			pgtoTotalBO.setCodigo(Integer.parseInt(txtCodigo.getText()));

			try {
				pgtoTotalBO.data = new DateTime(df.parse(txtData.getText()));
			} catch (ParseException e1) {
				JOptionPane.showMessageDialog(this, "Data incorreta ou inv�lida!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtData.requestFocus();
				txtData.selectAll();
				return;
			}

			try {
				pgtoTotalBO.dataInicial = new DateTime(df.parse(txtDatainicial.getText()));
			} catch (ParseException e1) {
				JOptionPane.showMessageDialog(this, "Data incorreta ou inv�lida!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtDatainicial.requestFocus();
				txtDatainicial.selectAll();
				return;
			}
			try {
				pgtoTotalBO.dataFinal = new DateTime(df.parse(txtDataFinal.getText()));
			} catch (ParseException e1) {
				JOptionPane.showMessageDialog(this, "Data incorreta ou inv�lida!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtDataFinal.requestFocus();
				txtDataFinal.selectAll();
				return;
			}

			pgtoTotalBO.dorBO.setCodigo(Integer.parseInt(txtCodigoEmpregador.getText()));
			pgtoTotalBO.iroBO.setCodigo(Integer.parseInt(txtCodEmpreiteiro.getText()));

			try {
				pgtoTotalBO.iroBO.setNome(txtMostraEmpreiteiro.getText());
				pgtoTotalBO.dorBO.setNome(txtMostraEmpregador.getText());
			} catch (StringVaziaException e1) {}

			pgtoTotalBO.setQntdBins(qntdBins);

			try {
				pgtoTotalBO.setValorBins(valorBins);
			} catch (ValorErradoException e2) {
				JOptionPane.showMessageDialog(this, "Valor por Bins incorreto!", "Valor incorreto", JOptionPane.ERROR_MESSAGE);
				txtValorPorBins.requestFocus();
				txtValorPorBins.selectAll();
				return;
			}

			qntdEmpr = Integer.parseInt(txtQntdEmpregados.getText());
			pgtoTotalBO.setQntdEmpregado(qntdEmpr);

			try {
				pgtoTotalBO.setValorEmpregado(valorPorEmpr);
			} catch (ValorErradoException e2) {
				JOptionPane.showMessageDialog(this, "Valor por empregado incorreto!", "Valor Incorreto", JOptionPane.ERROR_MESSAGE);
				txtValorPorEmpregado.requestFocus();
				txtValorPorEmpregado.selectAll();
				return;
			}

			pgtoTotalBO.setValorTotalEmpregados(valorEmpregados);
			System.out.println(valorEmpregados);
			try {
				pgtoTotalBO.setValorEmpreiteiro(valorEmpreiteiro);
			} catch (ValorErradoException e3) {
				JOptionPane.showMessageDialog(this, "Valor empreiteiro incorreto!", "Valor Incorreto", JOptionPane.ERROR_MESSAGE);
				return;
			}

			pgtoTotalBO.setValorTotal(valorTotal);

			try {
				pgtoTotalBO.setValorDesconto(valorDescIro);
			} catch (ValorErradoException e2) {
				JOptionPane.showMessageDialog(this, "Valor desconto incorreto!", "Valor Incorreto", JOptionPane.ERROR_MESSAGE);
				return;
			}

			pgtoTotalBO.adiantBO.setCodigo(codAdiantIro);

			pgtoTotalBO.setHistDesconto(txtMostraDescontoIro.getText());

			if (consPag == null)
				pgtoTotalDao.incluir(pgtoTotalBO);
			else
				pgtoTotalDao.alterar(pgtoTotalBO);

			//----------------------------------------------------------------------------

			int i = 0;

			do {
				pgtoUnitBO.add(new PagamentoUnitarioBO());
				pgtoUnitBO.get(i).pgtoTotalBO.setCodigo(pgtoTotalBO.getCodigo());
				pgtoUnitBO.get(i).adoBO.setCodigo(Integer.parseInt(modelo.getValueAt(i, COLUNA_CODIGO).toString()));
				pgtoUnitBO.get(i).setValor(Double.parseDouble(modelo.getValueAt(i, COLUNA_VALOR).toString()));
				try {
					pgtoUnitBO.get(i).setValorAcrescimo(Double.parseDouble(modelo.getValueAt(i, COLUNA_ACRESCIMO).toString()));
					pgtoUnitBO.get(i).setDesconto(Double.parseDouble(modelo.getValueAt(i, COLUNA_DESCONTO).toString()));
					pgtoUnitBO.get(i).setDescHAT(Double.parseDouble(modelo.getValueAt(i, COLUNA_DESCHAT).toString()));
					pgtoUnitBO.get(i).setValorTotal(Double.parseDouble(modelo.getValueAt(i, COLUNA_VALORTOTAL).toString()));
				} catch (NumberFormatException e1) {
				} catch (ValorErradoException e1) {}
				pgtoUnitBO.get(i).setQntdDias(Integer.parseInt(modelo.getValueAt(i, COLUNA_DIASTRAB).toString()));
				pgtoUnitBO.get(i).histDesconto.setText(modelo.getValueAt(i, COLUNA_OBDESCONTO).toString());
				pgtoUnitBO.get(i).adiantBO.setCodigo(Integer.parseInt(modelo.getValueAt(i, COLUNA_CODADIANTAMENTO).toString()));
				if (consPag == null)
					pgtoUnitDao.incluir(pgtoUnitBO.get(i));
				else
					pgtoUnitDao.alterar(pgtoUnitBO.get(i));
				i++;
			} while (i < modelo.getRowCount());

			geraStringCodigos();

			if (consPag == null) {
				JOptionPane.showMessageDialog(this, "Pagamento registrado com sucesso!", "Registro Salvo", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
			} else {
				JOptionPane.showMessageDialog(this, "Pagamento alterado com sucesso!", "Registro Alterado", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
				int linha = consPag.tabela.getSelectedRow();
				consPag.modelo.setValueAt(pgtoTotalBO.data.toDate(),linha,1);
				consPag.modelo.setValueAt(pgtoTotalBO.iroBO.getNome(),linha,2);
				consPag.modelo.setValueAt(pgtoTotalBO.dataInicial.toDate(),linha,3);
				consPag.modelo.setValueAt(pgtoTotalBO.dataFinal.toDate(),linha,4);
				consPag.modelo.setValueAt(pgtoTotalBO.dorBO.getNome(),linha,5);
				consPag.modelo.setValueAt(pgtoTotalBO.getValorTotal(),linha,6);
				consPag.modelo.setValueAt(pgtoTotalBO.getValorTotalEmpregados(),linha,7);
				consPag.modelo.setValueAt(pgtoTotalBO.getValorEmpreiteiro(),linha,8);
			}
			pgtoTotalDao.atualizaPagos(pgtoTotalBO.dataInicial, pgtoTotalBO.dataFinal, pgtoTotalBO.iroBO.getCodigo(), Integer.parseInt(txtCodigo.getText()), codigosEmpregados);

			if (cbPagarEmpreiteiro.isSelected())
				pgtoTotalDao.atualizaPagosDia(pgtoTotalBO.dataInicial, pgtoTotalBO.dataFinal, pgtoTotalBO.iroBO.getCodigo(), Integer.parseInt(txtCodigo.getText()));
			else
				pgtoTotalDao.desatualizaPagosDia(pgtoTotalBO.dataInicial, pgtoTotalBO.dataFinal, pgtoTotalBO.iroBO.getCodigo(), Integer.parseInt(txtCodigo.getText()));

			btnGeraRecibos.setEnabled(true);
			btnTrocarDeEquipe.setEnabled(true);
			btnConfirmar.setEnabled(false);
			btnProcuraEmpregador.setEnabled(false);
			btnProcuraEmpreiteiro.setEnabled(false);
			txtCodEmpreiteiro.setEditable(false);
			txtCodEmpreiteiro.setFocusable(false);
			txtCodigoEmpregador.setEditable(false);
			txtCodigoEmpregador.setFocusable(false);
			txtDatainicial.setEditable(false);
			txtDatainicial.setFocusable(false);
			txtDataFinal.setEditable(false);
			txtDataFinal.setFocusable(false);
			txtData.setEditable(false);
			txtData.setFocusable(false);
			btnSelecionarTodos.setEnabled(false);
			btnDeselecionarTodos.setEnabled(false);
			cbPagarEmpreiteiro.setEnabled(false);
			btnCancelar.setText("Sair (F4)");
			auxSair = false;
		} else if (origem == btnGeraRecibos) {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			geraCheques();
			ReciboSafristas recibo = new ReciboSafristas(Integer.parseInt(txtCodigo.getText()));
			ReciboEmpreiteiro reciboEmpreiteiro = new ReciboEmpreiteiro(Integer.parseInt(txtCodigo.getText()));

			if (cbPagarEmpreiteiro.isSelected())
				reciboEmpreiteiro.geraReciboEmpreiteiro();
			
			recibo.geraReciboSafristasSacola(codigosEmpregados);

			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		} else if (origem == btnSelecionarTodos) {
			int aux = 0;

			do {
				modelo.setValueAt(true, aux, 0);
				modelo.setValueAt(valoresEmpregados.get(aux), aux, COLUNA_VALOR);
				modelo.setValueAt(valoresEmpregados.get(aux), aux, COLUNA_VALORTOTAL);
				aux++;
			} while (aux < modelo.getRowCount());

			modelo.fireTableDataChanged();
			txtQntdEmpregados.setText(String.valueOf(modelo.getRowCount()));
			calculaTotalEmpreiteiro();
		} else if (origem == btnDeselecionarTodos) {
			int aux = 0;

			do {
				modelo.setValueAt(false, aux, 0);
				modelo.setValueAt(new Double(0.00), aux, COLUNA_VALOR);
				modelo.setValueAt(new Double(0.00), aux, COLUNA_DESCONTO);
				modelo.setValueAt(new Double(0.00), aux, COLUNA_VALORTOTAL);
				modelo.setValueAt("", aux, COLUNA_OBDESCONTO);
				modelo.setValueAt(0, aux, COLUNA_CODADIANTAMENTO);
				aux++;
			} while (aux < modelo.getRowCount());

			modelo.fireTableDataChanged();
			txtQntdEmpregados.setText("0");
			calculaTotalEmpreiteiro();
		} else if (origem == btnDescontoCheque) {
			String valor = JOptionPane.showInputDialog(this, "Informe o valor à descontar nos cheques", "Desconto Cheque", JOptionPane.INFORMATION_MESSAGE);
			valorDescontoChq = Double.parseDouble(valor.replace(',', '.'));
		} else if (origem == cbPagarEmpreiteiro) {
			if (!cbPagarEmpreiteiro.isSelected()) {
				zeraValoresEmpreiteiro();
			} else {
				try {
					valorTotalEmpreiteiro = pgtoTotalDao.consultaValorEmpreiteiro(Integer.parseInt(txtCodEmpreiteiro.getText()),
							new DateTime(df.parse(txtDatainicial.getValue().toString())), new DateTime(df.parse(txtDataFinal.getValue().toString())));
				} catch (NumberFormatException e1) {} catch (ParseException e1) {}
				
				valorEmpreiteiro = valorTotalEmpreiteiro;
				txtValorTotalEmpreiteiro.setText(decimal.format(valorEmpreiteiro));
			}
			calculaTotal();
		} else if (origem == btnLimpar) {
			qntdEmpr = 0;
			qntdBins = 0;
			valorTotal = 0.0;
			valorTotalEmpreiteiro = 0.0;
			valorPorEmpr = 0.0;
			valorBins = 0.0;
			valorEmpregados = 0.0;
			auxSair = true;
			codigosEmpregados = "";
			btnGeraRecibos.setEnabled(false);
			btnConfirmar.setEnabled(true);
			btnProcuraEmpregador.setEnabled(true);
			btnProcuraEmpreiteiro.setEnabled(true);
			btnSelecionarTodos.setEnabled(true);
			btnDeselecionarTodos.setEnabled(true);
			cbPagarEmpreiteiro.setEnabled(true);
			txtCodigo.setText(String.valueOf(pgtoTotalDao.getUltimoCod()+1));
			txtCodEmpreiteiro.setEditable(true);
			txtCodEmpreiteiro.setFocusable(true);
			txtCodEmpreiteiro.setText("");
			txtCodigoEmpregador.setEditable(true);
			txtCodigoEmpregador.setFocusable(true);
			txtCodigoEmpregador.setText("");
			txtData.setEditable(true);
			txtData.setFocusable(true);
			txtData.selectAll();
			txtData.requestFocus();
			txtDatainicial.setEditable(true);
			txtDatainicial.setFocusable(true);
			txtDatainicial.setValue("");
			txtDataFinal.setEditable(true);
			txtDataFinal.setFocusable(true);
			txtDataFinal.setValue("");
			txtMostraEmpregador.setText("");
			txtMostraEmpreiteiro.setText("");
			txtCodigoEmpregador.setText("");
			txtCodEmpreiteiro.setText("");
			txtValorEmpregados.setText("");
			txtQntdEmpregados.setText("");
			txtQntdBins.setText("");
			txtValorTotalEmpreiteiro.setText("");
			txtValorTotalPgto.setText("");
			btnCancelar.setText("Cancelar (F4)");
			for (int i = modelo.getRowCount() - 1; i >= 0; i--)
				modelo.removeRow(i);
		} else if (origem == btnProcuraEmpregador) {
			FrmConsultaEmpregador fr = new FrmConsultaEmpregador(this);
			fr.setVisible(true);
			getDesktopPane().add(fr);
			try {
				fr.setSelected(true);
			}
			catch (PropertyVetoException exc) { }
			FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
		} else if (origem == btnProcuraEmpreiteiro) {
			FrmConsultaEmpreiteiro fr = new FrmConsultaEmpreiteiro(this);
			fr.setVisible(true);
			getDesktopPane().add(fr);
			try {
				fr.setSelected(true);
			}
			catch (PropertyVetoException exc) { }
			FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
		} else if (origem == btnTrocarDeEquipe) {
			List<Integer> codigos = new ArrayList<Integer>();
			
			for (int aux=0; aux<modelo.getRowCount(); aux++) {
				codigos.add(Integer.parseInt(modelo.getValueAt(aux, COLUNA_CODIGO).toString()));
			}
			
			FrmTrocarEquipePagos fr = new FrmTrocarEquipePagos(codigos);
			fr.setVisible(true);
			getDesktopPane().add(fr);
			try {
				fr.setSelected(true);
			}
			catch (PropertyVetoException exc) { }
			FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
		} else if (origem == btnCancelar) {
			if (auxSair) {
				if (JOptionPane.showConfirmDialog(this, "Deseja cancelar a operação?", "Cancelar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
					doDefaultCloseAction();
			} else {
				doDefaultCloseAction();
			}
		}
	}

	private String geraStringCodigos() {
		int i = 0;

		do {
			codigosEmpregados = codigosEmpregados + modelo.getValueAt(i, COLUNA_CODIGO).toString() + ",";
			i++;
		} while (i < modelo.getRowCount());

		if (codigosEmpregados.length() > 0) {
			codigosEmpregados = codigosEmpregados.substring(0, codigosEmpregados.length()-1);
		}
		
		System.out.println(codigosEmpregados);

		return codigosEmpregados;
	}

	private void zeraValoresEmpreiteiro() {
		valorEmpreiteiro = 0.00;
		valorDescIro = 0.00;
		txtValorTotalEmpreiteiro.setText("0,00");
		txtMostraDescontoIro.setText("");
		txtValorDescontoIro.setText("");
	}
}