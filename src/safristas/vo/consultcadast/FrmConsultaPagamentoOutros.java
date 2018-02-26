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
import safristas.bo.outros.PagamentoTotalOutrosBO;
import safristas.bo.outros.PagamentoUnitarioOutrosBO;
import safristas.dao.outros.PagamentoTotalOutrosDao;
import safristas.dao.outros.PagamentoUnitarioOutrosDao;
import safristas.vo.FrmMenuGeralMaca;
import safristas.vo.filtrorelatorios.FrmRelatorioPagamentos;
import safristas.vo.lancamentos.FrmPagamentoTotalOutros;
import util.NumberRenderer;
import util.ModeloTabela;

public class FrmConsultaPagamentoOutros extends FrmConsultaPai {
	private static final long serialVersionUID = -8191655337201082296L;
	protected DecimalFormat decimal = new DecimalFormat("#,##0.00");
	protected SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	public PagamentoTotalOutrosBO pgTotalOutBO;
	public ArrayList<PagamentoUnitarioOutrosBO> pgtoUnitOutBO;
	public PagamentoUnitarioOutrosDao pgtoUnitOutDao = new PagamentoUnitarioOutrosDao();
	protected PagamentoTotalOutrosDao pgtotalOutDao = new PagamentoTotalOutrosDao();
	public JTable tabela;
	public ModeloTabela modelo;
	public FrmRelatorioPagamentos relPgto = null;

	public FrmConsultaPagamentoOutros (FrmRelatorioPagamentos relPgto) {
		this();
		this.relPgto = relPgto;
		btnAlterar.setVisible(false);
		btnExcluir.setVisible(false);
		btnIncluir.setText("Selecionar");
		btnIncluir.setIcon(new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
	}


	public FrmConsultaPagamentoOutros() {

		setTitle("Consulta Pagamentos outras funções");
		setSize(getWidth()+50, getHeight());

		cbConsulta.removeItem("Nome");
		cbConsulta.addItem("Valor");

		cbConsulta.addItem("Data");
		cbConsulta.addItem("Data Inicial");
		cbConsulta.addItem("Data Final");
		cbConsulta.addItem("Valor");
		cbConsulta.addItem("Nome Empregador");
		cbConsulta.addItem("Cód. Empregador");
		cbConsulta.setSelectedItem("Data");

		ArrayList<Object> dados = new ArrayList<Object>();

		String[] colunas = new String[] {"Código", "Data", "Data Inicial", "Data Final", "Empregador", "Quant. Empregados", "Valor Total"};

		boolean[] edicao = {false, false, false, false, false, false, false};

		modelo = new ModeloTabela(dados, colunas, edicao);
		tabela = new JTable(modelo);
		tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
		tabela.getColumnModel().getColumn(0).setResizable(false);
		tabela.getColumnModel().getColumn(1).setPreferredWidth(90);
		tabela.getColumnModel().getColumn(1).setResizable(true);
		tabela.getColumnModel().getColumn(2).setPreferredWidth(90);
		tabela.getColumnModel().getColumn(2).setResizable(true);
		tabela.getColumnModel().getColumn(3).setPreferredWidth(90);
		tabela.getColumnModel().getColumn(3).setResizable(true);
		tabela.getColumnModel().getColumn(4).setPreferredWidth(250);
		tabela.getColumnModel().getColumn(4).setResizable(true);
		tabela.getColumnModel().getColumn(5).setPreferredWidth(120);
		tabela.getColumnModel().getColumn(5).setResizable(true);
		tabela.getColumnModel().getColumn(5).setCellRenderer(NumberRenderer.getIntegerRenderer());
		tabela.getColumnModel().getColumn(6).setPreferredWidth(120);
		tabela.getColumnModel().getColumn(6).setResizable(true);
		tabela.getColumnModel().getColumn(6).setCellRenderer(NumberRenderer.getCurrencyRenderer());
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
		ArrayList<PagamentoTotalOutrosBO> pgTotalOutBO = null;

		for (int i = modelo.getRowCount() - 1; i >= 0; i--)
			modelo.removeRow(i);

		if (super.cbConsulta.getSelectedItem().equals("Código")) {
			pgTotalOutBO = pgtotalOutDao.consultaPorCodigo(Integer.parseInt(super.txtDadoConsulta.getText()));
		} else if (super.cbConsulta.getSelectedItem().equals("Data")) {
			pgTotalOutBO = pgtotalOutDao.consultaPorData(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("Data Inicial")) {
			pgTotalOutBO = pgtotalOutDao.consultaPorDataInicial(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("Data Final")) {
			pgTotalOutBO = pgtotalOutDao.consultaPorDataFinal(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("Valor")) {
			pgTotalOutBO = pgtotalOutDao.consultaPorValorTotal(Double.parseDouble(super.txtDadoConsulta.getText().trim().replace(',', '.')));
		}  else if (super.cbConsulta.getSelectedItem().equals("Nome Empregador")) {
			pgTotalOutBO = pgtotalOutDao.consultaPorNomeEmpregador(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("Cód. Empregador")) {
			try {
				pgTotalOutBO = pgtotalOutDao.consultaPorCodEmpregador(Integer.parseInt(super.txtDadoConsulta.getText()));
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
						new Integer(pgTotalOutBO.get(indice).getCodigo()),
						pgTotalOutBO.get(indice).data.toDate(),
						pgTotalOutBO.get(indice).dataInicial.toDate(),
						pgTotalOutBO.get(indice).dataFinal.toDate(),
						pgTotalOutBO.get(indice).dorBO.getNome(),
						pgTotalOutBO.get(indice).getQntdEmpregado(),
						pgTotalOutBO.get(indice).getValorTotal()
				});
				indice++;
			} while (indice < pgTotalOutBO.size());
		} catch (NullPointerException e) {}

	}

	@Override
	public void incluir() {
		if (relPgto == null) {
			FrmPagamentoTotalOutros fr = new FrmPagamentoTotalOutros();
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
			pgTotalOutBO = pgtotalOutDao.consultaPorCodigo(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(), 0).toString())).get(0);
			pgtoUnitOutBO = new ArrayList<PagamentoUnitarioOutrosBO>();
			pgtoUnitOutBO = pgtoUnitOutDao.consultaPorCodPagamento(pgTotalOutBO.getCodigo());
			FrmPagamentoTotalOutros fr = new FrmPagamentoTotalOutros(this);
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
		if (pgtoUnitOutDao.excluir(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString())) == true) {
			int codigo = Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString());
			pgTotalOutBO = new PagamentoTotalOutrosBO();
			pgTotalOutBO = pgtotalOutDao.consultaPorCodigo(codigo).get(0);
			
			pgtotalOutDao.desatualizaPagos(pgTotalOutBO.dataInicial, pgTotalOutBO.dataFinal, codigo);
			if (pgtotalOutDao.excluir(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString())) == true) {
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
