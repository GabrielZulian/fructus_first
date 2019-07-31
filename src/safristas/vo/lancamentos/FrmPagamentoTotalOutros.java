package safristas.vo.lancamentos;

import impressos.Cheques;
import impressos.ReciboOutros;
import safristas.bo.DadosChequeBO;
import safristas.bo.EmpregadoBO;
import safristas.bo.EmpregadorBO;
import safristas.bo.outros.LancaDiaOutrosBO;
import safristas.bo.outros.PagamentoTotalOutrosBO;
import safristas.bo.outros.PagamentoUnitarioOutrosBO;
import safristas.bo.outros.VeiculoBO;
import safristas.dao.EmpregadoDao;
import safristas.dao.EmpregadorDao;
import safristas.dao.outros.LancaDiaOutrosDao;
import safristas.dao.outros.PagamentoTotalOutrosDao;
import safristas.dao.outros.PagamentoUnitarioOutrosDao;
import safristas.dao.outros.VeiculoDao;
import safristas.vo.FrmMenuGeralMaca;
import safristas.vo.consultcadast.FrmConsultaEmpregador;
import safristas.vo.consultcadast.FrmConsultaPagamentoOutros;

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
import exceptions.QuantidadeErradaException;
import exceptions.StringVaziaException;
import exceptions.ValorErradoException;

public class FrmPagamentoTotalOutros extends JInternalFrame implements ActionListener {
	private static final long serialVersionUID = -6163106319333647690L;

	private MaskFormatter mascaraData;
	protected DecimalFormat decimal = new DecimalFormat("#,##0.00");
	double valorTotal = 0.0;
	private ArrayList<Double> valoresEmpregados = new ArrayList<Double>();
	int qntdEmpregado = 0;
	boolean testaAlterar = false;
	boolean auxSair = true;

	private GridBagConstraints constraints = new GridBagConstraints();
	private JLabel lblCodigo, lblData, lblDataInicio, lblDataFinal, lblCodEmpregador, lblQntdEmpreg, lblValorTotalPgto;
	private JTextField txtCodigo, txtQntdEmpreg, txtValorTotalPgto;
	public JTextField txtCodigoEmpregador, txtMostraEmpregador;
	private JButton btnProcuraEmpregador, btnSelecionarTodos, btnDeselecionarTodos, btnGeraRecibos, btnConfirmar, btnLimpar, btnCancelar;
	private JFormattedTextField txtData, txtDatainicial, txtDataFinal;
	protected String nome, codigosEmpregados = "";
	public EmpregadorBO dorBO;
	public EmpregadorDao dorDao = new EmpregadorDao();
	public EmpregadoBO adoBO;
	
	public EmpregadoDao adoDao = new EmpregadoDao();
	private Locale brasil = new Locale("pt", "BR");
	protected DateTimeZone zona = DateTimeZone.forID("Etc/GMT+3");
	protected DateTime data = new DateTime(zona);
	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy", brasil);
	private JCheckBox cbCaminhoes, cbTratores, cbOutros;

	public PagamentoTotalOutrosBO pgtoTotalOutBO;
	public PagamentoTotalOutrosDao pgtoTotalOutDao = new PagamentoTotalOutrosDao();
	public ArrayList<PagamentoUnitarioOutrosBO> pgtoUnitOutBO = new ArrayList<PagamentoUnitarioOutrosBO>();
	public PagamentoUnitarioOutrosDao pgtoUnitOutDao = new PagamentoUnitarioOutrosDao();
	public LancaDiaOutrosDao diaOutrosDao = new LancaDiaOutrosDao();
	public VeiculoDao veicDao = new VeiculoDao();

	protected JTable tabela;
	protected ModeloTabela modelo;
	protected FrmConsultaPagamentoOutros consPagOut = null;

	public FrmPagamentoTotalOutros(FrmConsultaPagamentoOutros consPagOut) {
		this();
		testaAlterar = true;
		this.consPagOut = consPagOut;
		txtCodigo.setText(String.valueOf(consPagOut.pgTotalOutBO.getCodigo()));
		txtData.setValue(df.format(consPagOut.pgTotalOutBO.data.toDate()));
		txtDatainicial.setValue(df.format(consPagOut.pgTotalOutBO.dataInicial.toDate()));
		txtDataFinal.setValue(df.format(consPagOut.pgTotalOutBO.dataFinal.toDate()));
		txtDatainicial.setEditable(false);
		txtDatainicial.setFocusable(false);
		txtDataFinal.setEditable(false);
		txtDataFinal.setFocusable(false);
		txtCodigoEmpregador.setText(String.valueOf(consPagOut.pgTotalOutBO.dorBO.getCodigo()));
		txtMostraEmpregador.setText(consPagOut.pgTotalOutBO.dorBO.getNome());
		for (int i = modelo.getRowCount() - 1; i >= 0; i--)
			modelo.removeRow(i);

		ArrayList<LancaDiaOutrosBO> diaOutBO = diaOutrosDao.consultaVeiculoPagamento(Integer.parseInt(txtCodigo.getText()));
		System.out.println("TAMANHO= " + diaOutBO.size());
		int i = 0;
		do {
			VeiculoBO veicBO = veicDao.consultaPorCodigo(diaOutBO.get(i).veicBO.getCodigo()).get(0);
			System.out.println(diaOutBO.get(i).veicBO.getCodigo());
			modelo.addRow(new Object[] {
					new Boolean(true),
					consPagOut.pgtoUnitOutBO.get(i).diaoutBO.adoBO.getCodigo(),
					consPagOut.pgtoUnitOutBO.get(i).diaoutBO.adoBO.getNome(),
					veicBO.getPlaca() + (veicBO.getDescricao().equals("")?"":" | " + veicBO.getDescricao()),
					consPagOut.pgtoUnitOutBO.get(i).getValor(),
					consPagOut.pgtoUnitOutBO.get(i).getDesconto(),
					consPagOut.pgtoUnitOutBO.get(i).getValorTotal(),
					veicBO.getNomeProprietario(),
					consPagOut.pgtoUnitOutBO.get(i).getQntdDias(),
					consPagOut.pgtoUnitOutBO.get(i).diaoutBO.adoBO.funcaoBO.getNome(),
					consPagOut.pgtoUnitOutBO.get(i).diaoutBO.adoBO.getCpf(),
					consPagOut.pgtoUnitOutBO.get(i).histDesconto.getText(),
					consPagOut.pgtoUnitOutBO.get(i).adiantBO.getCodigo()
			});
			i++;
		} while (i < consPagOut.pgtoUnitOutBO.size());

		geraStringCodigos();

		valorTotal = consPagOut.pgTotalOutBO.getValorTotal();
		txtValorTotalPgto.setText(decimal.format(valorTotal));

		qntdEmpregado = consPagOut.pgTotalOutBO.getQntdEmpregado();
		txtQntdEmpreg.setText(String.valueOf(consPagOut.pgTotalOutBO.getQntdEmpregado()));

		cbCaminhoes.setEnabled(false);
		cbTratores.setEnabled(false);
		cbOutros.setEnabled(false);

		btnConfirmar.setText("Alterar (F1)");
		btnLimpar.setVisible(false);
		btnGeraRecibos.setEnabled(true);
		auxSair = false;
	}

	public FrmPagamentoTotalOutros() {

		super("Pagamento - Fechamento outras funções",true,true,false,true);

		setSize(1000, 600);
		setResizable(false);
		setTitle("Pagamentos - Fechamento outras funções");
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
		} catch (ParseException e) {}

		lblCodigo = new JLabel("C�digo");
		lblCodigo.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelCima.add(lblCodigo, constraints);

		txtCodigo = new JTextField(5);
		txtCodigo.setEditable(false);
		txtCodigo.setFocusable(false);
		txtCodigo.setFont(f);
		txtCodigo.setText(String.valueOf(pgtoTotalOutDao.getUltimoCod()+1));
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
					txtCodigoEmpregador.requestFocus();
					txtCodigoEmpregador.selectAll();
					return;
				}
				catch (NullPointerException erro) {}
				txtMostraEmpregador.setText(dorBO.getNome());
				if (!testaAlterar) {
					preencheTabela();
					qntdEmpregado = modelo.getRowCount();
					txtQntdEmpreg.setText(String.valueOf(qntdEmpregado));
				}
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

		cbCaminhoes = new JCheckBox("Caminhões", true);
		cbCaminhoes.setFont(f);
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelCima.add(cbCaminhoes, constraints);

		cbTratores = new JCheckBox("Tratores", true);
		cbTratores.setFont(f);
		constraints.gridx = 3;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.WEST;
		painelCima.add(cbTratores, constraints);

		cbOutros = new JCheckBox("Outros", true);
		cbOutros.setFont(f);
		constraints.gridx = 5;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.WEST;
		painelCima.add(cbOutros, constraints);
		constraints.gridwidth = 1;

		//------------------------------------------------------------

		JPanel painelInterno = new JPanel();
		painelInterno.setLayout(new BorderLayout(2, 2));

		JPanel painelInternoCima = new JPanel();
		painelInternoCima.setLayout(new GridBagLayout());

		JPanel painelInternoBaixo = new JPanel();
		painelInternoBaixo.setLayout(new GridBagLayout());

		ArrayList<Object> dados = new ArrayList<Object>();

		String[] colunas = new String[] {" ", "Código", "Nome", "Veículo", "Valor",  "Descontos", "Valor Final", "Beneficiário",
				"Quantidade Dias/Fretes", "Função", "CPF", "Observação Desconto", "Cód Adiant"};

		boolean[] edicao = {true, false, false, false, false, false, false, false, false, false, false, false, false};

		modelo = new ModeloTabela(dados, colunas, edicao);
		tabela = new JTable(modelo);
		tabela.getColumnModel().getColumn(0).setPreferredWidth(30);
		tabela.getColumnModel().getColumn(0).setResizable(false);
		tabela.getColumnModel().getColumn(1).setPreferredWidth(50);
		tabela.getColumnModel().getColumn(1).setResizable(false);
		tabela.getColumnModel().getColumn(2).setPreferredWidth(250);
		tabela.getColumnModel().getColumn(2).setResizable(true);
		tabela.getColumnModel().getColumn(3).setPreferredWidth(120);
		tabela.getColumnModel().getColumn(3).setResizable(true);
		tabela.getColumnModel().getColumn(4).setPreferredWidth(90);
		tabela.getColumnModel().getColumn(4).setResizable(true);
		tabela.getColumnModel().getColumn(4).setCellRenderer(NumberRenderer.getCurrencyRenderer());
		tabela.getColumnModel().getColumn(5).setPreferredWidth(90);
		tabela.getColumnModel().getColumn(5).setResizable(true);
		tabela.getColumnModel().getColumn(5).setCellRenderer(NumberRenderer.getCurrencyRenderer());
		tabela.getColumnModel().getColumn(6).setPreferredWidth(90);
		tabela.getColumnModel().getColumn(6).setResizable(false);
		tabela.getColumnModel().getColumn(6).setCellRenderer(NumberRenderer.getCurrencyRenderer());
		tabela.getColumnModel().getColumn(7).setPreferredWidth(200);
		tabela.getColumnModel().getColumn(7).setResizable(true);
		tabela.getColumnModel().getColumn(8).setPreferredWidth(136);
		tabela.getColumnModel().getColumn(8).setResizable(true);
		tabela.getColumnModel().getColumn(8).setCellRenderer(NumberRenderer.getIntegerRenderer());
		tabela.getColumnModel().getColumn(9).setPreferredWidth(130);
		tabela.getColumnModel().getColumn(9).setResizable(false);
		tabela.getColumnModel().getColumn(10).setPreferredWidth(140);
		tabela.getColumnModel().getColumn(10).setResizable(true);
		tabela.getColumnModel().getColumn(11).setPreferredWidth(300);
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
				int qntdEmpr = Integer.parseInt(txtQntdEmpreg.getText());

				if (consPagOut == null) {

					if (linha >= 0 && coluna == 0) {
						if ((Boolean)modelo.getValueAt(linha, coluna) == false) {
							modelo.setValueAt(new Double(0.00), linha, 4);
							modelo.setValueAt(new Double(0.00), linha, 5);
							modelo.setValueAt(new Double(0.00), linha, 6);
							modelo.setValueAt("", linha, 10);
							modelo.setValueAt(0, linha, 11);
							txtQntdEmpreg.setText(String.valueOf(qntdEmpr-1));
							calculaTotal();
						} else {
							modelo.setValueAt(valoresEmpregados.get(linha), linha, 4);
							modelo.setValueAt(valoresEmpregados.get(linha), linha, 6);
							txtQntdEmpreg.setText(String.valueOf(qntdEmpr+1));
	
							calculaTotal();
						}
					}
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (btnConfirmar.isEnabled()) {
					if (e.getClickCount() == 2) {
						nome = modelo.getValueAt(tabela.getSelectedRow(), 2).toString();
						FrmDescAcrescPagamentos fr = new FrmDescAcrescPagamentos(FrmPagamentoTotalOutros.this, 'A');
						fr.setVisible(true);
						getDesktopPane().add(fr);   
						try {
							fr.setSelected(true);
						} catch (PropertyVetoException exc) { }
						FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
					}
				}
			}
		});

		tabela.setFocusable(false);

		//----------------------------------------------

		btnSelecionarTodos = new JButton(new ImageIcon(getClass().getResource("/icons/icon_cbok.gif")));
		btnSelecionarTodos.setToolTipText("Selecionar todos");
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(2, -264, 2, 4);
		painelInternoBaixo.add(btnSelecionarTodos, constraints);

		btnDeselecionarTodos = new JButton(new ImageIcon(getClass().getResource("/icons/icon_cbempty.gif")));
		btnDeselecionarTodos.setToolTipText("Deselecionar todos");
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(2, -380, 2, 4);
		painelInternoBaixo.add(btnDeselecionarTodos, constraints);

		lblQntdEmpreg = new JLabel("Quantidade Empregados");
		lblQntdEmpreg.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridheight = 2;
		constraints.insets = new Insets(5, 4, 2, 4);
		constraints.anchor = GridBagConstraints.NORTHEAST;
		painelInternoBaixo.add(lblQntdEmpreg, constraints);

		txtQntdEmpreg = new JTextField(5);
		txtQntdEmpreg.setFont(f);
		txtQntdEmpreg.setEditable(false);
		txtQntdEmpreg.setFocusable(false);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridheight = 2;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		painelInternoBaixo.add(txtQntdEmpreg, constraints);

		lblValorTotalPgto = new JLabel("Valor Total");
		lblValorTotalPgto.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridheight = 2;
		constraints.anchor = GridBagConstraints.SOUTHEAST;
		constraints.insets = new Insets(2, 4, 4, 4);
		painelInternoBaixo.add(lblValorTotalPgto, constraints);

		txtValorTotalPgto = new JTextField(8);
		txtValorTotalPgto.setFont(f);
		txtValorTotalPgto.setBackground(new Color(187,244,188));
		txtValorTotalPgto.setEditable(false);
		txtValorTotalPgto.setFocusable(false);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.SOUTH;
		painelInternoBaixo.add(txtValorTotalPgto, constraints);
		constraints.gridheight = 1;

		btnGeraRecibos = new JButton("<html><center>Gerar Recibos<br> e Cheques</center></html>", new ImageIcon(getClass().getResource("/icons/icon_recibo.gif")));
		btnGeraRecibos.setIconTextGap(8);
		btnGeraRecibos.setEnabled(false);
		constraints.gridx = 3;
		constraints.gridy = 0;
		constraints.ipadx = 0;
		constraints.gridheight = 2;
		constraints.anchor = GridBagConstraints.SOUTH;
		painelInternoBaixo.add(btnGeraRecibos, constraints);
		constraints.ipady = 0;
		constraints.gridheight = 1;
		constraints.insets = new Insets(2, 4, 2, 4);

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
			private static final long serialVersionUID = 6851324674055856899L;

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
			private static final long serialVersionUID = 590619079419821543L;

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
			private static final long serialVersionUID = -1852278878056526843L;

			public void actionPerformed(ActionEvent evt) {
				btnCancelar.doClick();
			}
		});

		painelBaixo.setBorder(BorderFactory.createEtchedBorder());
		painelGeral.add(painelBaixo, BorderLayout.SOUTH);

		Container p = getContentPane();
		p.add(painelGeral);

		btnGeraRecibos.addActionListener(this);
		btnSelecionarTodos.addActionListener(this);
		btnDeselecionarTodos.addActionListener(this);
		btnProcuraEmpregador.addActionListener(this);
		cbCaminhoes.addActionListener(this);
		cbTratores.addActionListener(this);
		cbOutros.addActionListener(this);
		btnConfirmar.addActionListener(this);
		btnLimpar.addActionListener(this);
		btnCancelar.addActionListener(this);
	}

	public void preencheTabela() {
		txtValorTotalPgto.setText("");
		valoresEmpregados.clear();

		DateTime dataInicial = new DateTime(zona);
		DateTime dataFinal = new DateTime(zona);

		try {
			dataInicial = new DateTime(df.parse(txtDatainicial.getValue().toString()));
			dataFinal = new DateTime(df.parse(txtDataFinal.getValue().toString()));
		} catch (ParseException e1) {}

		for (int i = modelo.getRowCount() - 1; i >= 0; i--)
			modelo.removeRow(i);

		ArrayList<PagamentoUnitarioOutrosBO> pgUnitOutBO = pgtoUnitOutDao.consultaPorPeriodo(dataInicial, dataFinal);

		int i = 0;
		do {
			VeiculoBO veiculoBO = veicDao.consultaPorCodigo(pgUnitOutBO.get(i).diaoutBO.veicBO.getCodigo()).get(0);
			if (pgUnitOutBO.get(i).getQntdDias() != 0 && pgUnitOutBO.get(i).getValor() > 0) {
				modelo.addRow(new Object[] {
						new Boolean(true),
						pgUnitOutBO.get(i).diaoutBO.adoBO.getCodigo(),
						pgUnitOutBO.get(i).diaoutBO.adoBO.getNome(),
						veiculoBO.getPlaca() + (veiculoBO.getDescricao().equals("")?"":" | " + veiculoBO.getDescricao()),
						pgUnitOutBO.get(i).getValor(),
						new Double(0.00),
						pgUnitOutBO.get(i).getValor(),
						veiculoBO.getNomeProprietario(),
						pgUnitOutBO.get(i).getQntdDias(),
						pgUnitOutBO.get(i).diaoutBO.adoBO.funcaoBO.getNome(),
						pgUnitOutBO.get(i).diaoutBO.adoBO.getCpf(),
						"",
						new Integer(0)
				});
			}
			valoresEmpregados.add(pgUnitOutBO.get(i).getValor());

			i++;
		} while (i < pgUnitOutBO.size());
		calculaTotal();
		cbCaminhoes.setSelected(true);
		cbTratores.setSelected(true);
		cbOutros.setSelected(true);
	}

	public void calculaTotal() {
		int i = 0;
		valorTotal = 0.00;
		do {
			valorTotal += (Double)modelo.getValueAt(i, 6);
			i++;
		} while (i < tabela.getRowCount());

		txtValorTotalPgto.setText(decimal.format(valorTotal));
	}

	public void geraCheques() {
		int aux = 0;

		Numerals e = new Numerals(2);
		List<DadosChequeBO> chequeBO = new ArrayList<DadosChequeBO>();
		do {
			chequeBO.add(new DadosChequeBO());
			double valor = Double.parseDouble(modelo.getValueAt(aux, 6).toString());
			chequeBO.get(aux).setNome(modelo.getValueAt(aux, 7).toString());
			chequeBO.get(aux).setValor(decimal.format(valor));
			String extenso = e.toString(BigDecimal.valueOf(valor));
			chequeBO.get(aux).setValorExtenso(extenso.toUpperCase());
			aux++;
		} while (aux < tabela.getRowCount());
		Cheques relCheque = new Cheques();
		relCheque.geraCheques(chequeBO);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object origem = e.getSource();
		if (origem == btnConfirmar) {

			int x = tabela.getRowCount();

			if (consPagOut == null) {
				do {
					Boolean selecionado = (Boolean) modelo.getValueAt(x-1, 0);
					if (!selecionado)
						modelo.removeRow(x-1);
					x--;
				} while (x>0);
			}

			pgtoTotalOutBO = new PagamentoTotalOutrosBO();

			pgtoTotalOutBO.setCodigo(Integer.parseInt(txtCodigo.getText()));

			try {
				pgtoTotalOutBO.data = new DateTime(df.parse(txtData.getText()));
			} catch (ParseException e1) {
				JOptionPane.showMessageDialog(this, "Data incorreta ou inválida!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtData.requestFocus();
				txtData.selectAll();
				return;
			}

			try {
				pgtoTotalOutBO.dataInicial = new DateTime(df.parse(txtDatainicial.getText()));
			} catch (ParseException e1) {
				JOptionPane.showMessageDialog(this, "Data incorreta ou inválida!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtDatainicial.requestFocus();
				txtDatainicial.selectAll();
				return;
			}

			try {
				pgtoTotalOutBO.dataFinal = new DateTime(df.parse(txtDataFinal.getText()));
			} catch (ParseException e1) {
				JOptionPane.showMessageDialog(this, "Data incorreta ou inválida!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtDataFinal.requestFocus();
				txtDataFinal.selectAll();
				return;
			}

			pgtoTotalOutBO.dorBO.setCodigo(Integer.parseInt(txtCodigoEmpregador.getText()));

			try {
				pgtoTotalOutBO.dorBO.setNome(txtMostraEmpregador.getText());
			} catch (StringVaziaException e1) {}

			try {
				pgtoTotalOutBO.setQntdEmpregado(qntdEmpregado);
			} catch (QuantidadeErradaException e2) {
				JOptionPane.showMessageDialog(this, "Quantidade empregados errada!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtQntdEmpreg.requestFocus();
				txtQntdEmpreg.selectAll();
				return;
			}
			pgtoTotalOutBO.setValorTotal(valorTotal);

			if (consPagOut == null)
				pgtoTotalOutDao.incluir(pgtoTotalOutBO);
			else
				pgtoTotalOutDao.alterar(pgtoTotalOutBO);

			//----------------------------------------------------------------------------

			int i = 0;
			do {
				pgtoUnitOutBO.add(new PagamentoUnitarioOutrosBO());
				pgtoUnitOutBO.get(i).pgtoTotalOutBO.setCodigo(pgtoTotalOutBO.getCodigo());
				pgtoUnitOutBO.get(i).diaoutBO.adoBO.setCodigo(Integer.parseInt(modelo.getValueAt(i, 1).toString()));
				pgtoUnitOutBO.get(i).setValor(Double.parseDouble(modelo.getValueAt(i, 4).toString()));
				try {
					pgtoUnitOutBO.get(i).setDesconto(Double.parseDouble(modelo.getValueAt(i, 5).toString()));
				} catch (NumberFormatException e1) {
				} catch (ValorErradoException e1) {}
				pgtoUnitOutBO.get(i).setValorTotal(Double.parseDouble(modelo.getValueAt(i, 6).toString()));
				pgtoUnitOutBO.get(i).setQntdDias(Integer.parseInt(modelo.getValueAt(i, 8).toString()));
				pgtoUnitOutBO.get(i).histDesconto.setText(modelo.getValueAt(i, 11).toString());
				pgtoUnitOutBO.get(i).adiantBO.setCodigo(Integer.parseInt(modelo.getValueAt(i, 12).toString()));
				if (consPagOut == null)
					pgtoUnitOutDao.incluir(pgtoUnitOutBO.get(i));
				else
					pgtoUnitOutDao.alterar(pgtoUnitOutBO.get(i));
				i++;
			} while (i < modelo.getRowCount());

			if (consPagOut == null) {
				JOptionPane.showMessageDialog(this, "Pagamento registrado com sucesso!", "Registro Salvo", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
			} else {
				JOptionPane.showMessageDialog(this, "Pagamento alterado com sucesso!", "Registro Alterado", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
				int linha = consPagOut.tabela.getSelectedRow();
				consPagOut.modelo.setValueAt(pgtoTotalOutBO.data.toDate(),linha,1);
				consPagOut.modelo.setValueAt(pgtoTotalOutBO.dataInicial.toDate(),linha,2);
				consPagOut.modelo.setValueAt(pgtoTotalOutBO.dataFinal.toDate(),linha,3);
				consPagOut.modelo.setValueAt(pgtoTotalOutBO.dorBO.getNome(),linha,4);
				consPagOut.modelo.setValueAt(pgtoTotalOutBO.getQntdEmpregado(),linha,5);
				consPagOut.modelo.setValueAt(pgtoTotalOutBO.getValorTotal(),linha,6);
			}
			pgtoTotalOutDao.atualizaPagos(pgtoTotalOutBO.dataInicial, pgtoTotalOutBO.dataFinal, pgtoTotalOutBO.getCodigo(), geraStringCodigos());
			btnGeraRecibos.setEnabled(true);
			btnConfirmar.setEnabled(false);
			btnProcuraEmpregador.setEnabled(false);
			txtCodigoEmpregador.setEditable(false);
			txtCodigoEmpregador.setFocusable(false);
			txtDatainicial.setEditable(false);
			txtDatainicial.setFocusable(false);
			txtDataFinal.setEditable(false);
			txtDataFinal.setFocusable(false);
			btnCancelar.setText("Sair (F4)");
			auxSair = false;
		} else if (origem == btnLimpar) {
			valorTotal = 0.0;
			qntdEmpregado = 0;
			auxSair = true;
			btnGeraRecibos.setEnabled(false);
			btnConfirmar.setEnabled(true);
			btnProcuraEmpregador.setEnabled(true);
			txtCodigoEmpregador.setEditable(true);
			txtCodigoEmpregador.setFocusable(true);
			txtCodigoEmpregador.setText("");
			txtDatainicial.setEditable(true);
			txtDatainicial.setFocusable(true);
			txtDatainicial.setValue("");
			txtDataFinal.setEditable(true);
			txtDataFinal.setFocusable(true);
			txtDataFinal.setValue("");
			txtMostraEmpregador.setText("");
			txtCodigoEmpregador.setText("");
			txtValorTotalPgto.setText("");
			txtQntdEmpreg.setText("");
			txtData.selectAll();
			txtData.requestFocus();
			btnCancelar.setText("Cancelar (F4)");
			for (int i = modelo.getRowCount() - 1; i >= 0; i--)
				modelo.removeRow(i);
		} else if (origem == cbCaminhoes) {
			int aux = 0;
			do {
				if (modelo.getValueAt(aux, 9).equals("Motorista"))
					if (!cbCaminhoes.isSelected())
						modelo.setValueAt(false, aux, 0);
					else
						modelo.setValueAt(true, aux, 0);
				aux++;
			} while (aux<tabela.getRowCount());

		} else if (origem == cbTratores) {
			int aux = 0;
			do {
				if (modelo.getValueAt(aux, 9).equals("Tratorista"))
					if (!cbTratores.isSelected())
						modelo.setValueAt(false, aux, 0);
					else
						modelo.setValueAt(true, aux, 0);
				aux++;
			} while (aux<tabela.getRowCount());

		} else if (origem == cbOutros) {
			int aux = 0;
			do {
				if (!modelo.getValueAt(aux, 9).equals("Tratorista") && !modelo.getValueAt(aux, 9).equals("Motorista"))
					if (!cbOutros.isSelected())
						modelo.setValueAt(false, aux, 0);
					else
						modelo.setValueAt(true, aux, 0);
				aux++;
			} while (aux<tabela.getRowCount());

		} else if (origem == btnSelecionarTodos) {
			int aux = 0;

			do {
				modelo.setValueAt(true, aux, 0);
				modelo.setValueAt(valoresEmpregados.get(aux), aux, 4);
				modelo.setValueAt(valoresEmpregados.get(aux), aux, 6);
				aux++;
			} while (aux < modelo.getRowCount());

			calculaTotal();
			modelo.fireTableDataChanged();
			txtQntdEmpreg.setText(String.valueOf(modelo.getRowCount()));

		} else if (origem == btnDeselecionarTodos) {
			int aux = 0;

			do {
				modelo.setValueAt(false, aux, 0);
				modelo.setValueAt(new Double(0.00), aux, 4);
				modelo.setValueAt(new Double(0.00), aux, 5);
				modelo.setValueAt(new Double(0.00), aux, 6);
				aux++;
			} while (aux < modelo.getRowCount());

			calculaTotal();
			modelo.fireTableDataChanged();
			txtQntdEmpreg.setText("0");

		} else if (origem == btnGeraRecibos) {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			geraCheques();
			int i = 0;
			ArrayList<Integer> codVeiculos = new ArrayList<Integer>();
			ArrayList<LancaDiaOutrosBO> diaOutBO = diaOutrosDao.consultaVeiculoPagamento(Integer.parseInt(txtCodigo.getText()));
			do {
				codVeiculos.add(diaOutBO.get(i).veicBO.getCodigo());
				i++;
			} while (i < diaOutBO.size());
			ReciboOutros reciboOut = new ReciboOutros(Integer.parseInt(txtCodigo.getText()), codVeiculos);
			reciboOut.geraRecibo();
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		} else if (origem == btnProcuraEmpregador) {
			FrmConsultaEmpregador fr = new FrmConsultaEmpregador(this);
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
			codigosEmpregados = codigosEmpregados + modelo.getValueAt(i, 1).toString() + ",";
			i++;
		} while (i < modelo.getRowCount());

		if (codigosEmpregados.length() > 0) {
			codigosEmpregados = codigosEmpregados.substring(0, codigosEmpregados.length()-1);
		}

		return codigosEmpregados;
	}

}
