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
import safristas.bo.EmpregadoBO;
import safristas.dao.EmpregadoDao;
import safristas.vo.FrmMenuGeralMaca;
import safristas.vo.filtrorelatorios.FrmRelatorioDiasTrabalhados;
import safristas.vo.lancamentos.FrmDiaTrabalhoOutros;
import safristas.vo.lancamentos.FrmLancaAdiantamentos;
import util.ModeloTabela;

public class FrmConsultaEmpregado extends FrmConsultaPai {
	private static final long serialVersionUID = -3820891566678975761L;
	
	JTable tabela;
	ModeloTabela modelo;
	EmpregadoBO adoBO;
	EmpregadoDao adoDao = new EmpregadoDao();
	FrmDiaTrabalhoOutros pagOutros = null;
	FrmLancaAdiantamentos lancaAdiant = null;
	FrmCadastraVeiculo cadVeic = null;
	FrmRelatorioDiasTrabalhados relDiasTrab = null;

	public FrmConsultaEmpregado(FrmCadastraVeiculo cadVeic) {
		this();
		this.cadVeic = cadVeic;
		btnAlterar.setVisible(false);
		btnExcluir.setVisible(false);
		btnIncluir.setText("Selecionar");
		btnIncluir.setIcon(new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
	}

	public FrmConsultaEmpregado(FrmLancaAdiantamentos lancaAdiant) {
		this();
		this.lancaAdiant = lancaAdiant;
		btnAlterar.setVisible(false);
		btnExcluir.setVisible(false);
		btnIncluir.setText("Selecionar");
		btnIncluir.setIcon(new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
	}

	public FrmConsultaEmpregado (FrmDiaTrabalhoOutros pagOutros) {	
		this();
		this.pagOutros = pagOutros;
		cbConsulta.setSelectedItem("Nome Função");
		if (pagOutros.rBtnCaminhao.isSelected())
			txtDadoConsulta.setText("Motorista");
		else if (pagOutros.rBtnTrator.isSelected())
			txtDadoConsulta.setText("Tratorista");
		else
			txtDadoConsulta.setText("");

		btnConsultar.doClick();
		btnAlterar.setVisible(false);
		btnExcluir.setVisible(false);
		btnIncluir.setText("Selecionar");
		btnIncluir.setIcon(new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
	}
	

	public FrmConsultaEmpregado(FrmRelatorioDiasTrabalhados relDiasTrab) {
		this();
		this.relDiasTrab = relDiasTrab;
		btnAlterar.setVisible(false);
		btnExcluir.setVisible(false);
		btnIncluir.setText("Selecionar");
		btnIncluir.setIcon(new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
	}

	public FrmConsultaEmpregado() {

		setTitle("Consulta Empregado");
		setSize(getWidth()+100, getHeight()+40);

		cbConsulta.addItem("Apelido");
		cbConsulta.addItem("Nome Empreiteiro");
		cbConsulta.addItem("Nome Função");
		cbConsulta.addItem("Nome Equipe");
		cbConsulta.addItem("CPF");
		cbConsulta.addItem("Cód. Empreiteiro");
		cbConsulta.addItem("Cód Função");
		cbConsulta.addItem("Cód. Equipe");

		ArrayList<Object> dados = new ArrayList<Object>();

		String[] colunas = new String[] {"Código", "Nome", "Apelido", "Empreiteiro", "Equipe", "CPF", "Função"};

		boolean[] edicao = {false, false, false, false, false, false, false};

		modelo = new ModeloTabela(dados, colunas, edicao);
		tabela = new JTable(modelo);
		tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
		tabela.getColumnModel().getColumn(0).setResizable(false);
		tabela.getColumnModel().getColumn(1).setPreferredWidth(280);
		tabela.getColumnModel().getColumn(1).setResizable(true);
		tabela.getColumnModel().getColumn(2).setPreferredWidth(100);
		tabela.getColumnModel().getColumn(2).setResizable(true);
		tabela.getColumnModel().getColumn(3).setPreferredWidth(200);
		tabela.getColumnModel().getColumn(3).setResizable(true);
		tabela.getColumnModel().getColumn(4).setPreferredWidth(80);
		tabela.getColumnModel().getColumn(4).setResizable(true);
		tabela.getColumnModel().getColumn(5).setPreferredWidth(100);
		tabela.getColumnModel().getColumn(5).setResizable(true);
		tabela.getColumnModel().getColumn(6).setPreferredWidth(200);
		tabela.getColumnModel().getColumn(6).setResizable(true);
		tabela.getTableHeader().setReorderingAllowed(false); 
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane rolagemTabela = new JScrollPane(tabela);
		painelGeral.add(rolagemTabela, BorderLayout.CENTER);
		tabela.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2){
					if (pagOutros != null || lancaAdiant != null || cadVeic != null || relDiasTrab != null)
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
		ArrayList <EmpregadoBO> adoBO = null;

		// apaga todas as linhas da tabela
		for (int i = modelo.getRowCount() - 1; i >= 0; i--)
			modelo.removeRow(i);

		if (super.cbConsulta.getSelectedItem().equals("código")) {
			try{
				adoBO = adoDao.consultaPorCodigo(Integer.parseInt(super.txtDadoConsulta.getText()));
			}catch(NumberFormatException e1){
				JOptionPane.showMessageDialog(this, "O Código deve ser numérico", "Erro", JOptionPane.ERROR_MESSAGE);
				super.txtDadoConsulta.selectAll();
				super.txtDadoConsulta.requestFocus();
				return;
			}
		} else if (super.cbConsulta.getSelectedItem().equals("Nome")) {
			adoBO = adoDao.consultaPorNome(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("Apelido")) {
			adoBO = adoDao.consultaPorApelido(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("Nome Empreiteiro")) {
			adoBO = adoDao.consultaPorNomeEmpreiteiro(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("Nome Função")) {
			adoBO = adoDao.consultaPorNomeFuncao(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("Nome Equipe")) {
			adoBO = adoDao.consultaPorNomeEquipe(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("Cód. Empreiteiro")) {
			try{
				adoBO = adoDao.consultaPorCodEmpreiteiro(Integer.parseInt(super.txtDadoConsulta.getText()));
			}catch(NumberFormatException e1){
				JOptionPane.showMessageDialog(this, "O código deve ser numérico", "Erro", JOptionPane.ERROR_MESSAGE);
				super.txtDadoConsulta.selectAll();
				super.txtDadoConsulta.requestFocus();
				return;
			}
		} else if (super.cbConsulta.getSelectedItem().equals("Cód. Função")) {
			try{
				adoBO = adoDao.consultaPorCodFuncao(Integer.parseInt(super.txtDadoConsulta.getText()));
			}catch(NumberFormatException e1){
				JOptionPane.showMessageDialog(this, "O código deve ser numérico", "Erro", JOptionPane.ERROR_MESSAGE);
				super.txtDadoConsulta.selectAll();
				super.txtDadoConsulta.requestFocus();
				return;
			}
		} else if (super.cbConsulta.getSelectedItem().equals("CPF")) {
			adoBO = adoDao.consultaPorCPF(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("Cód. Equipe")) {
			try{
				adoBO = adoDao.consultaPorCodEquipe(Integer.parseInt(super.txtDadoConsulta.getText()));
			}catch(NumberFormatException e1){
				JOptionPane.showMessageDialog(this, "O código deve ser numérico", "Erro", JOptionPane.ERROR_MESSAGE);
				super.txtDadoConsulta.selectAll();
				super.txtDadoConsulta.requestFocus();
				return;
			}
		}

		int indice = 0;
		do {
			modelo.addRow(new Object[] {
					new Integer(adoBO.get(indice).getCodigo()),
					adoBO.get(indice).getNome(),
					adoBO.get(indice).getApelido(),
					adoBO.get(indice).equipeBO.iroBO.getNome(),
					adoBO.get(indice).equipeBO.getNome(),
					adoBO.get(indice).getCpf(),
					adoBO.get(indice).funcaoBO.getNome()
			});
			indice++;
		} while (indice < adoBO.size());
	}

	@Override
	public void incluir() {
		if (pagOutros == null && lancaAdiant == null && cadVeic == null && relDiasTrab == null) {

			FrmCadastraEmpregado fr = new FrmCadastraEmpregado();
			fr.setVisible(true);
			getDesktopPane().add(fr);   
			try {
				fr.setSelected(true);
			}
			catch (PropertyVetoException exc) {}
			FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
		} else {
			selecionar();
		}
	}

	@Override
	public void alterar() {
		if (tabela.getSelectedRow()>=0) {
			adoBO = new EmpregadoBO();   // cria�ao do objeto EmpregadoBO

			adoBO = adoDao.consultaPorCodigo(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString())).get(0);
			FrmCadastraEmpregado fr = new FrmCadastraEmpregado(this);
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
		if (adoDao.excluir(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString())) == true)
			modelo.removeRow(tabela.getSelectedRow());
	}

	private void selecionar() {
		try {
			if (pagOutros != null) {
				pagOutros.txtCodEmpregado.setText(modelo.getValueAt(tabela.getSelectedRow(),0).toString());
				pagOutros.txtMostraEmpregado.setText(modelo.getValueAt(tabela.getSelectedRow(),1).toString());
			} else if (lancaAdiant != null) {
				lancaAdiant.txtCodEmpregado.setText(modelo.getValueAt(tabela.getSelectedRow(),0).toString());
				lancaAdiant.txtMostraEmpregado.setText(modelo.getValueAt(tabela.getSelectedRow(),1).toString());
			} else if (cadVeic != null) {
				cadVeic.txtCodProp.setText(modelo.getValueAt(tabela.getSelectedRow(),0).toString());
				cadVeic.txtMostraProp.setText(modelo.getValueAt(tabela.getSelectedRow(),1).toString());
			} else if (relDiasTrab != null) {
				relDiasTrab.txtCodEmpregado.setText(modelo.getValueAt(tabela.getSelectedRow(),0).toString());
				relDiasTrab.txtMostraEmpregado.setText(modelo.getValueAt(tabela.getSelectedRow(),0).toString());
			}
		} catch (ArrayIndexOutOfBoundsException erro) {
			JOptionPane.showMessageDialog(this, "Selecione um registro primeiro!", "Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}
		doDefaultCloseAction();
	}
}
