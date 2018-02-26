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
import safristas.bo.safristas.EquipeBO;
import safristas.dao.safristas.EquipeDao;
import safristas.vo.FrmMenuGeralMaca;
import util.ModeloTabela;

public class FrmConsultaEquipe extends FrmConsultaPai {
	private static final long serialVersionUID = -663051264967706403L;
	
	JTable tabela;
	ModeloTabela modelo;
	EquipeBO equiBO;
	EquipeDao equiDao = new EquipeDao();
	FrmCadastraEmpregado cadEmpregado = null;

	public FrmConsultaEquipe(FrmCadastraEmpregado cadEmpregado) {
		this();
		this.cadEmpregado = cadEmpregado;
		btnAlterar.setVisible(false);
		btnExcluir.setVisible(false);
		btnIncluir.setText("Selecionar");
		btnIncluir.setIcon(new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
	}

	public FrmConsultaEquipe() {

		setTitle("Consulta Equipes");
		setSize(800, 600);
		cbConsulta.addItem("Cód. Empreiteiro");
		cbConsulta.addItem("Nome Empreiteiro");

		ArrayList<Object> dados = new ArrayList<Object>();

		String[] colunas = new String[] {"Código", "Nome Equipe", "Empreiteiro"};

		boolean[] edicao = {false, false, false};

		modelo = new ModeloTabela(dados, colunas, edicao);
		tabela = new JTable(modelo);
		tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
		tabela.getColumnModel().getColumn(0).setResizable(false);
		tabela.getColumnModel().getColumn(1).setPreferredWidth(350);
		tabela.getColumnModel().getColumn(1).setResizable(true);
		tabela.getColumnModel().getColumn(2).setPreferredWidth(290);
		tabela.getColumnModel().getColumn(2).setResizable(true);
		tabela.getTableHeader().setReorderingAllowed(true); 
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane rolagemTabela = new JScrollPane(tabela);
		painelGeral.add(rolagemTabela, BorderLayout.CENTER);
		tabela.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2){
					if (cadEmpregado != null)
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
		ArrayList <EquipeBO> equiBO = null;

		// apaga todas as linhas da tabela
		for (int i = modelo.getRowCount() - 1; i >= 0; i--)
			modelo.removeRow(i);

		if (super.cbConsulta.getSelectedItem().equals("Código")) {
			try{
				equiBO = equiDao.consultaPorCodigo(Integer.parseInt(super.txtDadoConsulta.getText()));
			}catch(NumberFormatException e1){
				JOptionPane.showMessageDialog(this, "O código deve ser numérico", "Erro",JOptionPane.ERROR_MESSAGE);
				super.txtDadoConsulta.selectAll();
				super.txtDadoConsulta.requestFocus();
				return;
			}
		} else if (super.cbConsulta.getSelectedItem().equals("Nome")) {
			equiBO = equiDao.consultaPorNome(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("Nome Empreiteiro")) {
			equiBO = equiDao.consultaPorNomeEmpreiteiro(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("Cód. Empreiteiro")) {
			try{
				equiBO = equiDao.consultaPorCodEmpreiteiro(Integer.parseInt(super.txtDadoConsulta.getText()));
			}catch(NumberFormatException e1){
				JOptionPane.showMessageDialog(this, "O código deve ser numérico", "Erro",JOptionPane.ERROR_MESSAGE);
				super.txtDadoConsulta.selectAll();
				super.txtDadoConsulta.requestFocus();
				return;
			}
		} 

		int indice = 0;
		do {
			modelo.addRow(new Object[] {
					new Integer(equiBO.get(indice).getCodigo()),
					equiBO.get(indice).getNome(),
					equiBO.get(indice).iroBO.getNome(),
			});
			indice++;
		} while (indice < equiBO.size());	
	}

	@Override
	public void incluir() {
		if (cadEmpregado == null) {
			FrmCadastraEquipe fr = new FrmCadastraEquipe();
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
			equiBO = new EquipeBO();   // criaçao do objeto EmpregadorBO

			equiBO = equiDao.consultaPorCodigo(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString())).get(0);
			FrmCadastraEquipe fr = new FrmCadastraEquipe(this);
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
		if (equiDao.excluir(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString())) == true)
			modelo.removeRow(tabela.getSelectedRow());
	}

	private void selecionar() {
		try {
			if (cadEmpregado != null) {
				cadEmpregado.txtCodEquipe.setText(modelo.getValueAt(tabela.getSelectedRow(),0).toString());
				cadEmpregado.txtMostraEquipe.setText(modelo.getValueAt(tabela.getSelectedRow(),1).toString());
			}
		} catch (ArrayIndexOutOfBoundsException erro) {
			JOptionPane.showMessageDialog(this, "Selecione um registro primeiro!", "Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}
		doDefaultCloseAction();
	}

}
