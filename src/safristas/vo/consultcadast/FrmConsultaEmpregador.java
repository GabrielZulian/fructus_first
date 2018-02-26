package safristas.vo.consultcadast;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import gerais.vo.FrmConsultaPai;
import safristas.bo.EmpregadorBO;
import safristas.dao.EmpregadorDao;
import safristas.vo.FrmMenuGeralMaca;
import safristas.vo.lancamentos.FrmPagamentoTotal;
import safristas.vo.lancamentos.FrmPagamentoTotalOutros;
import util.ModeloTabela;

public class FrmConsultaEmpregador extends FrmConsultaPai{
	private static final long serialVersionUID = 5057633229892811649L;

	JTable tabela;
	ModeloTabela modelo;
	EmpregadorBO dorBO;
	EmpregadorDao dorDao = new EmpregadorDao();
	FrmPagamentoTotal pgtoTotal = null;
	FrmPagamentoTotalOutros pgtoTotalOut = null;
	FrmCadastraVeiculo cadVeic = null;

	public FrmConsultaEmpregador(FrmCadastraVeiculo cadVeic) {
		this();
		this.cadVeic = cadVeic;
		btnAlterar.setVisible(false);
		btnExcluir.setVisible(false);
		btnIncluir.setText("Selecionar");
		btnIncluir.setIcon(new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
	}
	
	public FrmConsultaEmpregador(FrmPagamentoTotalOutros pgtoTotalOut) {
		this();
		this.pgtoTotalOut = pgtoTotalOut;
		btnAlterar.setVisible(false);
		btnExcluir.setVisible(false);
		btnIncluir.setText("Selecionar");
		btnIncluir.setIcon(new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
	}

	public FrmConsultaEmpregador(FrmPagamentoTotal pgtoTotal) {
		this();
		this.pgtoTotal = pgtoTotal;
		btnAlterar.setVisible(false);
		btnExcluir.setVisible(false);
		btnIncluir.setText("Selecionar");
		btnIncluir.setIcon(new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
	}

	public FrmConsultaEmpregador() {

		setTitle("Consulta Empregador");

		cbConsulta.addItem("CPF");
		cbConsulta.addItem("IE");

		ArrayList<Object> dados = new ArrayList<Object>();

		String[] colunas = new String[] {"Código", "Nome", "CPF", "Inscrição Estadual", "Cidade", "Telefone"};

		boolean[] edicao = {false, false, false, false, false, false};

		modelo = new ModeloTabela(dados, colunas, edicao);
		tabela = new JTable(modelo);
		tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
		tabela.getColumnModel().getColumn(0).setResizable(false);
		tabela.getColumnModel().getColumn(1).setPreferredWidth(240);
		tabela.getColumnModel().getColumn(1).setResizable(true);
		tabela.getColumnModel().getColumn(2).setPreferredWidth(120);
		tabela.getColumnModel().getColumn(2).setResizable(true);
		tabela.getColumnModel().getColumn(3).setPreferredWidth(150);
		tabela.getColumnModel().getColumn(3).setResizable(true);
		tabela.getColumnModel().getColumn(4).setPreferredWidth(150);
		tabela.getColumnModel().getColumn(4).setResizable(true);
		tabela.getColumnModel().getColumn(5).setPreferredWidth(100);
		tabela.getColumnModel().getColumn(5).setResizable(true);
		tabela.getTableHeader().setReorderingAllowed(false); 
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane rolagemTabela = new JScrollPane(tabela);
		painelGeral.add(rolagemTabela, BorderLayout.CENTER);

		tabela.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2){
					if (pgtoTotal != null || pgtoTotalOut != null || cadVeic != null)
						selecionar();
					else
						alterar();
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
		ArrayList <EmpregadorBO> dorBO = null;

		// apaga todas as linhas da tabela
		for (int i = modelo.getRowCount() - 1; i >= 0; i--)
			modelo.removeRow(i);

		if (super.cbConsulta.getSelectedItem().equals("Código")) {
			try{
				dorBO = dorDao.consultaPorCodigo(Integer.parseInt(super.txtDadoConsulta.getText()));
			}catch(NumberFormatException e1){
				JOptionPane.showMessageDialog(this, "O código deve ser numérico", "Erro",JOptionPane.ERROR_MESSAGE);
				super.txtDadoConsulta.selectAll();
				super.txtDadoConsulta.requestFocus();
				return;
			}
		} else if (super.cbConsulta.getSelectedItem().equals("Nome")) {
			dorBO = dorDao.consultaPorNome(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("CPF")) {
			dorBO = dorDao.consultaPorCPF(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("IE")) {
			dorBO = dorDao.consultaPorIE(super.txtDadoConsulta.getText());
		}

		int indice = 0;
		do {
			modelo.addRow(new Object[] {
					new Integer(dorBO.get(indice).getCodigo()),
					dorBO.get(indice).getNome(),
					dorBO.get(indice).getCpf(),
					dorBO.get(indice).getIe(),
					dorBO.get(indice).cidBO.getNome(),
					dorBO.get(indice).getTelefone()
			});
			indice++;
		} while (indice < dorBO.size());
	}

	@Override
	public void incluir() {
		if (pgtoTotal == null && pgtoTotalOut == null && cadVeic == null) {
			FrmCadastraAtrativo fr = new FrmCadastraAtrativo();
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
			dorBO = new EmpregadorBO();   // criaçao do objeto EmpregadorBO

			dorBO = dorDao.consultaPorCodigo(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString())).get(0);

			FrmCadastraAtrativo fr = new FrmCadastraAtrativo(this);
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
		if (dorDao.excluir(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString())) == true)
			modelo.removeRow(tabela.getSelectedRow());
	}

	private void selecionar() {
		if (pgtoTotal != null) {
			pgtoTotal.txtCodigoEmpregador.setText(modelo.getValueAt(tabela.getSelectedRow(),0).toString());
			pgtoTotal.txtMostraEmpregador.setText(modelo.getValueAt(tabela.getSelectedRow(),1).toString());
		} else if (pgtoTotalOut != null) {
			pgtoTotalOut.txtCodigoEmpregador.setText(modelo.getValueAt(tabela.getSelectedRow(),0).toString());
			pgtoTotalOut.txtMostraEmpregador.setText(modelo.getValueAt(tabela.getSelectedRow(),1).toString());
		} else if (cadVeic != null) {
			cadVeic.txtCodProp.setText(modelo.getValueAt(tabela.getSelectedRow(),0).toString());
			cadVeic.txtMostraProp.setText(modelo.getValueAt(tabela.getSelectedRow(),1).toString());
		}
		doDefaultCloseAction();
	}
}
