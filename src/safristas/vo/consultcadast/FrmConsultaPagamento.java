package safristas.vo.consultcadast;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import gerais.vo.FrmConsultaPai;
import safristas.bo.safristas.PagamentoTotalBO;
import safristas.bo.safristas.PagamentoUnitarioBO;
import safristas.dao.AdiantamentoDao;
import safristas.dao.safristas.PagamentoTotalDao;
import safristas.dao.safristas.PagamentoUnitarioDao;
import safristas.vo.FrmMenuGeralMaca;
import safristas.vo.filtrorelatorios.FrmRelatorioPagamentos;
import safristas.vo.lancamentos.FrmPagamentoTotal;
import util.ModeloTabela;
import util.NumberRenderer;

public class FrmConsultaPagamento extends FrmConsultaPai {
	private static final long serialVersionUID = -7689769648771238486L;
	protected DecimalFormat decimal = new DecimalFormat("#,##0.00");
	protected SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	public PagamentoTotalBO pgTotalBO;
	public ArrayList<PagamentoUnitarioBO> pgUnitBO;
	public PagamentoTotalDao pgtotalDao = new PagamentoTotalDao();
	public PagamentoUnitarioDao pgUnitDao = new PagamentoUnitarioDao();
	public JTable tabela;
	public ModeloTabela modelo;
	public FrmRelatorioPagamentos relPgto = null;

	public FrmConsultaPagamento(FrmRelatorioPagamentos relPgto) {
		this();
		this.relPgto = relPgto;
		btnAlterar.setVisible(false);
		btnExcluir.setVisible(false);
		btnIncluir.setText("Selecionar");
		btnIncluir.setIcon(new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
	}

	public FrmConsultaPagamento() {

		setTitle("Consulta Pagamentos");
		setSize(getWidth()+280, getHeight());

		cbConsulta.removeItem("Nome");
		cbConsulta.addItem("Valor");

		cbConsulta.addItem("Data Lançamento");
		cbConsulta.addItem("Data Inicial");
		cbConsulta.addItem("Data Final");
		cbConsulta.addItem("Valor");
		cbConsulta.addItem("Nome Empreiteiro");
		cbConsulta.addItem("Nome Empregador");
		cbConsulta.addItem("Cód. Empreiteiro");
		cbConsulta.addItem("Cód. Empregador");
		cbConsulta.setSelectedItem("Data Lançamento");


		ArrayList<Object> dados = new ArrayList<Object>();

		String[] colunas = new String[] {"Código", "Data", "Empreiteiro", "Data Inicial", "Data Final", "Empregador", "Valor Total", "Valor Empregados", "Valor Empreiteiro"};

		boolean[] edicao = {false, false, false, false, false, false, false, false, false};

		modelo = new ModeloTabela(dados, colunas, edicao);
		tabela = new JTable(modelo);
		tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
		tabela.getColumnModel().getColumn(0).setResizable(false);
		tabela.getColumnModel().getColumn(1).setPreferredWidth(90);
		tabela.getColumnModel().getColumn(1).setResizable(true);
		tabela.getColumnModel().getColumn(2).setPreferredWidth(230);
		tabela.getColumnModel().getColumn(2).setResizable(true);
		tabela.getColumnModel().getColumn(3).setPreferredWidth(90);
		tabela.getColumnModel().getColumn(3).setResizable(true);
		tabela.getColumnModel().getColumn(4).setPreferredWidth(90);
		tabela.getColumnModel().getColumn(4).setResizable(true);
		tabela.getColumnModel().getColumn(5).setPreferredWidth(230);
		tabela.getColumnModel().getColumn(5).setResizable(true);
		tabela.getColumnModel().getColumn(6).setPreferredWidth(120);
		tabela.getColumnModel().getColumn(6).setResizable(true);
		tabela.getColumnModel().getColumn(6).setCellRenderer(NumberRenderer.getCurrencyRenderer());
		tabela.getColumnModel().getColumn(7).setPreferredWidth(120);
		tabela.getColumnModel().getColumn(7).setResizable(true);
		tabela.getColumnModel().getColumn(7).setCellRenderer(NumberRenderer.getCurrencyRenderer());
		tabela.getColumnModel().getColumn(8).setPreferredWidth(120);
		tabela.getColumnModel().getColumn(8).setResizable(true);
		tabela.getColumnModel().getColumn(8).setCellRenderer(NumberRenderer.getCurrencyRenderer());
		tabela.getTableHeader().setReorderingAllowed(false); 
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane rolagemTabela = new JScrollPane(tabela);
		painelGeral.add(rolagemTabela, BorderLayout.CENTER);
		tabela.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2){
					if (relPgto == null)
						alterar();
					else
						selecionar();
				}
			}
		});
		tabela.setFocusable(false);
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
	}

	@Override
	public void consultar() {
		ArrayList<PagamentoTotalBO> pgTotalBO = null;

		for (int i = modelo.getRowCount() - 1; i >= 0; i--)
			modelo.removeRow(i);

		if (super.cbConsulta.getSelectedItem().equals("Código")) {
			pgTotalBO = pgtotalDao.consultaPorCodigo(Integer.parseInt(super.txtDadoConsulta.getText()));
		} else if (super.cbConsulta.getSelectedItem().equals("Data Lançamento")) {
			pgTotalBO = pgtotalDao.consultaPorData(super.txtDadoConsulta.getText().replace('/', '.'));
		} else if (super.cbConsulta.getSelectedItem().equals("Data Inicial")) {
			pgTotalBO = pgtotalDao.consultaPorDataInicial(super.txtDadoConsulta.getText().replace('/', '.'));
		} else if (super.cbConsulta.getSelectedItem().equals("Data Final")) {
			pgTotalBO = pgtotalDao.consultaPorDataFinal(super.txtDadoConsulta.getText().replace('/', '.'));
		} else if (super.cbConsulta.getSelectedItem().equals("Valor")) {
			pgTotalBO = pgtotalDao.consultaPorValorTotal(Double.parseDouble(super.txtDadoConsulta.getText().trim().replace(',', '.')));
		} else if (super.cbConsulta.getSelectedItem().equals("Nome Empreiteiro")) {
			pgTotalBO = pgtotalDao.consultaPorNomeEmpreiteiro(super.txtDadoConsulta.getText());
		}  else if (super.cbConsulta.getSelectedItem().equals("Nome Empregador")) {
			pgTotalBO = pgtotalDao.consultaPorNomeEmpregador(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("Cód. Empreiteiro")) {
			try {
				pgTotalBO = pgtotalDao.consultaPorCodEmpreiteiro(Integer.parseInt(super.txtDadoConsulta.getText()));
			} catch(NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "O código deve ser numérico", "Erro",JOptionPane.ERROR_MESSAGE);
				super.txtDadoConsulta.selectAll();
				super.txtDadoConsulta.requestFocus();
				return;
			} 
		} else if (super.cbConsulta.getSelectedItem().equals("Cód. Empregador")) {
			try {
				pgTotalBO = pgtotalDao.consultaPorCodEmpregador(Integer.parseInt(super.txtDadoConsulta.getText()));
			} catch(NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "O código deve ser numérico", "Erro",JOptionPane.ERROR_MESSAGE);
				super.txtDadoConsulta.selectAll();
				super.txtDadoConsulta.requestFocus();
				return;
			} 
		}

		int indice = 0;
		try {
			do {
				modelo.addRow(new Object[] {
						new Integer(pgTotalBO.get(indice).getCodigo()),
						pgTotalBO.get(indice).data.toDate(),
						pgTotalBO.get(indice).iroBO.getNome(),
						pgTotalBO.get(indice).dataInicial.toDate(),
						pgTotalBO.get(indice).dataFinal.toDate(),
						pgTotalBO.get(indice).dorBO.getNome(),
						pgTotalBO.get(indice).getValorTotal(),
						pgTotalBO.get(indice).getValorTotalEmpregados(),
						pgTotalBO.get(indice).getValorEmpreiteiro()
				});
				indice++;
			} while (indice < pgTotalBO.size());
		} catch (NullPointerException e) {
			System.out.println("ERRO");
		}

	}

	@Override
	public void incluir() {
		if (relPgto == null) {
			FrmPagamentoTotal fr = new FrmPagamentoTotal();
			fr.setVisible(true);
			getDesktopPane().add(fr); 
			try {
				fr.setSelected(true);
			}
			catch (PropertyVetoException exc) { }
			FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
		} else
			selecionar();
	}

	@Override
	public void alterar() {
		if (tabela.getSelectedRow()>=0) {
			pgTotalBO = new PagamentoTotalBO();
			pgUnitBO = new ArrayList<PagamentoUnitarioBO>();
			pgTotalBO = pgtotalDao.consultaPorCodigo(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(), 0).toString())).get(0);
			pgUnitBO = pgUnitDao.consultaPorCodPagamento(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(), 0).toString()));
			FrmPagamentoTotal fr = new FrmPagamentoTotal(this);
			fr.setVisible(true);
			getDesktopPane().add(fr);   
			try {
				fr.setSelected(true);
			}
			catch (PropertyVetoException exc) { }
			FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
		} else
			JOptionPane.showMessageDialog(this, "Selecione um registro primeiro!", "Selecionar Registro",JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void excluir() {
		AdiantamentoDao adiDao = new AdiantamentoDao();
		int codPagamento = Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString());
		pgUnitBO = new ArrayList<PagamentoUnitarioBO>();
		pgUnitBO = pgUnitDao.consultaPorCodPagamento(codPagamento);

		if (pgUnitDao.excluir(codPagamento) == true) {
			for (int x=0;x<pgUnitBO.size();x++) {
				if (pgUnitBO.get(x).adiantBO.getCodigo() != 0) {
					adiDao.desatualizaPago(pgUnitBO.get(x).adiantBO.getCodigo());
				}
			}

			pgTotalBO = new PagamentoTotalBO();
			pgTotalBO = pgtotalDao.consultaPorCodigo(codPagamento).get(0);
			
			String codigosEmpregados = "(";
			int aux = 0;
			do {
				codigosEmpregados = codigosEmpregados + pgUnitBO.get(aux).adoBO.getCodigo() + ",";
				aux++;
			} while(aux < pgUnitBO.size());
			
			if (codigosEmpregados.length() > 0) {
				codigosEmpregados = codigosEmpregados.substring(0, codigosEmpregados.length()-1);
			}

			codigosEmpregados = codigosEmpregados + ")";
			
			pgtotalDao.desatualizaPagos(pgTotalBO.dataInicial, pgTotalBO.dataFinal, pgTotalBO.iroBO.getCodigo(), codPagamento, codigosEmpregados);

			if (pgtotalDao.excluir(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString())) == true) {

				modelo.removeRow(tabela.getSelectedRow());
			}
		}
	}

	private void selecionar() {
		if (relPgto != null) {
			relPgto.txtCodPagamento.setText(modelo.getValueAt(tabela.getSelectedRow(),0).toString());
			relPgto.txtMostraPagamento.setText(df.format(modelo.getValueAt(tabela.getSelectedRow(),1)) + " - " + decimal.format(Double.parseDouble(modelo.getValueAt(tabela.getSelectedRow(), 6).toString())));
		}
		doDefaultCloseAction();
	}
}
