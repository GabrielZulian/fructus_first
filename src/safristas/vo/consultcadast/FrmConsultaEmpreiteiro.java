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
import safristas.bo.safristas.EmpreiteiroBO;
import safristas.dao.safristas.EmpreiteiroDao;
import safristas.vo.FrmMenuGeralMaca;
import safristas.vo.filtrorelatorios.FrmPlanilhaColetaDias;
import safristas.vo.filtrorelatorios.FrmRelatorioEmpregados;
import safristas.vo.lancamentos.FrmDiaTrabalho;
import safristas.vo.lancamentos.FrmLancaAdiantamentos;
import safristas.vo.lancamentos.FrmPagamentoTotal;
import util.ModeloTabela;

public class FrmConsultaEmpreiteiro extends FrmConsultaPai{
	private static final long serialVersionUID = 4182560824415003202L;
	
	JTable tabela;
	ModeloTabela modelo;
	EmpreiteiroBO iroBO;
	EmpreiteiroDao iroDao = new EmpreiteiroDao();
	FrmCadastraEquipe cadEquipe = null;
	FrmDiaTrabalho pagDia = null;
	FrmLancaAdiantamentos lancaAdiant = null;
	FrmPlanilhaColetaDias planColeta = null;
	FrmPagamentoTotal pgtoTotal = null;
	FrmCadastraVeiculo cadVeic = null;
	FrmRelatorioEmpregados relEmpregados = null;

	public FrmConsultaEmpreiteiro(FrmRelatorioEmpregados relEmpregados) {
		this();
		this.relEmpregados = relEmpregados;
		configuraBotoes();
	}

	public FrmConsultaEmpreiteiro(FrmCadastraVeiculo cadVeic) {
		this();
		this.cadVeic = cadVeic;
		configuraBotoes();
	}
	
	public FrmConsultaEmpreiteiro(FrmPlanilhaColetaDias planColeta) {
		this();
		this.planColeta = planColeta;
		configuraBotoes();
	}

	public FrmConsultaEmpreiteiro(FrmLancaAdiantamentos lancaAdiant) {
		this();
		this.lancaAdiant = lancaAdiant;
		configuraBotoes();
	}

	public FrmConsultaEmpreiteiro(FrmPagamentoTotal pgtoTotal) {
		this();
		this.pgtoTotal = pgtoTotal;
		configuraBotoes();
	}

	public FrmConsultaEmpreiteiro(FrmDiaTrabalho pagDia) {
		this();
		this.pagDia = pagDia;
		configuraBotoes();
	}

	public FrmConsultaEmpreiteiro(FrmCadastraEquipe cadEquipe) {
		this();
		this.cadEquipe = cadEquipe;
		configuraBotoes();
	}

	public FrmConsultaEmpreiteiro() {

		setTitle("Consulta Empreiteiro");

		cbConsulta.addItem("CPF");
		cbConsulta.addItem("Apelido");

		ArrayList<Object> dados = new ArrayList<Object>();

		String[] colunas = new String[] {"Código", "Nome", "Apelido", "CPF", "Cidade", "Telefone"};

		boolean[] edicao = {false, false, false, false, false, false};

		modelo = new ModeloTabela(dados, colunas, edicao);
		tabela = new JTable(modelo);
		tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
		tabela.getColumnModel().getColumn(0).setResizable(false);
		tabela.getColumnModel().getColumn(1).setPreferredWidth(215);
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
					if (cadEquipe != null || pagDia != null || lancaAdiant != null || planColeta != null 
							|| pgtoTotal != null || cadVeic != null || relEmpregados != null)
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
		ArrayList <EmpreiteiroBO> iroBO = null;

		// apaga todas as linhas da tabela
		for (int i = modelo.getRowCount() - 1; i >= 0; i--)
			modelo.removeRow(i);

		if (super.cbConsulta.getSelectedItem().equals("Código")) {
			try{
				iroBO = iroDao.consultaPorCodigo(Integer.parseInt(super.txtDadoConsulta.getText()));
			}catch(NumberFormatException e1){
				JOptionPane.showMessageDialog(this, "O código deve ser numérico", "Erro",JOptionPane.ERROR_MESSAGE);
				super.txtDadoConsulta.selectAll();
				super.txtDadoConsulta.requestFocus();
				return;
			}
		} else if (super.cbConsulta.getSelectedItem().equals("Nome")) {
			iroBO = iroDao.consultaPorNome(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("Apelido")) {
			iroBO = iroDao.consultaPorApelido(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("CPF")) {
			iroBO = iroDao.consultaPorCPF(super.txtDadoConsulta.getText());
		}

		int indice = 0;
		do {
			modelo.addRow(new Object[] {
					new Integer(iroBO.get(indice).getCodigo()),
					iroBO.get(indice).getNome(),
					iroBO.get(indice).getApelido(),
					iroBO.get(indice).getCpf(),
					iroBO.get(indice).cidBO.getNome(),
					iroBO.get(indice).getTelefone()
			});
			indice++;
		} while (indice < iroBO.size());

	}

	@Override
	public void incluir() {
		if (cadEquipe == null && pagDia == null && lancaAdiant == null && planColeta == null && pgtoTotal == null && cadVeic == null && relEmpregados == null) {
			FrmCadastraEmpreiteiro fr = new FrmCadastraEmpreiteiro();
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
			iroBO = new EmpreiteiroBO();   // criaçao do objeto EmpreiteiroBO

			iroBO = iroDao.consultaPorCodigo(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString())).get(0);
			FrmCadastraEmpreiteiro fr = new FrmCadastraEmpreiteiro(this);
			fr.setVisible(true);
			getDesktopPane().add(fr);   
			try {
				fr.setSelected(true);
			}
			catch (PropertyVetoException exc) {}
			FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
		} else
			JOptionPane.showMessageDialog(this, "Selecione um registro primeiro!", "Selecionar Registro",JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void excluir() {
		if (iroDao.excluir(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString())))
			modelo.removeRow(tabela.getSelectedRow());
	}

	private void selecionar() {
		if (cadEquipe != null) {
			cadEquipe.txtCodEmpreiteiro.setText(modelo.getValueAt(tabela.getSelectedRow(),0).toString());
			cadEquipe.txtMostraEmpreiteiro.setText(modelo.getValueAt(tabela.getSelectedRow(),1).toString());
		} else if (pagDia != null) {
			pagDia.txtCodEmpreiteiro.setText(modelo.getValueAt(tabela.getSelectedRow(), 0).toString());
			pagDia.txtMostraEmpreiteiro.setText(modelo.getValueAt(tabela.getSelectedRow(),1).toString());
		} else if (lancaAdiant != null) {
			lancaAdiant.txtCodEmpregado.setText(modelo.getValueAt(tabela.getSelectedRow(),0).toString());
			lancaAdiant.txtMostraEmpregado.setText(modelo.getValueAt(tabela.getSelectedRow(),1).toString());
		} else if (planColeta != null) {
			planColeta.txtCodEmpreiteiro.setText(modelo.getValueAt(tabela.getSelectedRow(),0).toString());
			planColeta.txtMostraEmpreiteiro.setText(modelo.getValueAt(tabela.getSelectedRow(),1).toString());
		} else if (pgtoTotal != null) {
			pgtoTotal.txtCodEmpreiteiro.setText(modelo.getValueAt(tabela.getSelectedRow(),0).toString());
			pgtoTotal.txtMostraEmpreiteiro.setText(modelo.getValueAt(tabela.getSelectedRow(),1).toString());
		} else if (cadVeic != null) {
			cadVeic.txtCodProp.setText(modelo.getValueAt(tabela.getSelectedRow(),0).toString());
			cadVeic.txtMostraProp.setText(modelo.getValueAt(tabela.getSelectedRow(),1).toString());
		} else if (relEmpregados != null) {
			relEmpregados.txtCodEmpreiteiro.setText(modelo.getValueAt(tabela.getSelectedRow(),0).toString());
			relEmpregados.txtMostraEmpreiteiro.setText(modelo.getValueAt(tabela.getSelectedRow(),1).toString());
		}
		doDefaultCloseAction();
	}
	
	public void configuraBotoes() {
		btnAlterar.setVisible(false);
		btnExcluir.setVisible(false);
		btnIncluir.setText("Selecionar");
		btnIncluir.setIcon(new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
	}
}
