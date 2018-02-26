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

import util.ModeloTabela;
import exceptions.StringVaziaException;
import gerais.vo.FrmConsultaPai;
import safristas.bo.CidadeBO;
import safristas.dao.CidadeDao;
import safristas.vo.FrmMenuGeralMaca;

public class FrmConsultaCidade extends FrmConsultaPai {
	private static final long serialVersionUID = -1252741409133300333L;

	CidadeDao cidDao = new CidadeDao();

	JTable tabela;
	ModeloTabela modelo;
	CidadeBO cidBO;   // declaracao de um objeto da classe de negocio CidadeBO
	FrmCadastraAtrativo cadEmpregador = null;
	FrmCadastraEmpreiteiro cadEmpreiteiro = null;

	public FrmConsultaCidade(FrmCadastraAtrativo cadEmpregador) {
		this();
		this.cadEmpregador = cadEmpregador;
		btnAlterar.setVisible(false);
		btnExcluir.setVisible(false);
		btnIncluir.setText("Selecionar");
		btnIncluir.setIcon(new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
	}
	
	public FrmConsultaCidade (FrmCadastraEmpreiteiro cadEmpreiteiro) {
		this();
		this.cadEmpreiteiro = cadEmpreiteiro;
		btnAlterar.setVisible(false);
		btnExcluir.setVisible(false);
		btnIncluir.setText("Selecionar");
		btnIncluir.setIcon(new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
	}

	public FrmConsultaCidade() {

		setTitle("Consulta Cidades");
		setSize(getWidth()-50, getHeight()-40);

		cbConsulta.addItem("UF");

		ArrayList<Object> dados = new ArrayList<Object>();

		String[] colunas = new String[] {"Código", "Nome", "UF"};

		boolean[] edicao = {false, false, false};

		// criação da tabela baseada no modelo ModeloTabela
		modelo = new ModeloTabela(dados, colunas, edicao);
		tabela = new JTable(modelo);
		tabela.getColumnModel().getColumn(0).setResizable(false);
		tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
		tabela.getColumnModel().getColumn(1).setPreferredWidth(480);
		tabela.getColumnModel().getColumn(1).setResizable(true);
		tabela.getColumnModel().getColumn(2).setPreferredWidth(200);
		tabela.getColumnModel().getColumn(2).setResizable(true);
		tabela.getTableHeader().setReorderingAllowed(false); 
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane rolagemTabela = new JScrollPane(tabela);
		painelGeral.add(rolagemTabela, BorderLayout.CENTER);
		
		tabela.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2){
					if ( cadEmpregador != null || cadEmpreiteiro != null)
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
	public void incluir() {
		if (cadEmpregador == null && cadEmpreiteiro == null) { // botao incluir
			FrmCadastraCidade fr = new FrmCadastraCidade();
			fr.setVisible(true);
			getDesktopPane().add(fr); 
			try {
				fr.setSelected(true);
			}
			catch (PropertyVetoException exc) { }
			FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
		}else{  // botao selecionar
			selecionar();
		}
	}

	public void alterar() {
		if (tabela.getSelectedRow()>=0) {
			cidBO = new CidadeBO();   // criaçao do objeto cidade
			cidBO.setCodigo(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString()));
			try {
				cidBO.setNome(modelo.getValueAt(tabela.getSelectedRow(),1).toString());
			} catch (StringVaziaException e1) {}
			cidBO.setUf(modelo.getValueAt(tabela.getSelectedRow(),2).toString());

			FrmCadastraCidade fr = new FrmCadastraCidade(this);  // envia essa consulta para o cadastro
			fr.setVisible(true);  
			getDesktopPane().add(fr);
			try {
				fr.setSelected(true);
			}catch (PropertyVetoException exc) { }
			FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
		} else
			JOptionPane.showMessageDialog(this, "Selecione um registro primeiro!", "Selecionar Registro",JOptionPane.INFORMATION_MESSAGE);
	}
	
	@Override
	public void consultar() {
		ArrayList <CidadeBO> cidBO = null;

		// apaga todas as linhas da tabela
		for (int i = modelo.getRowCount() - 1; i >= 0; i--)
			modelo.removeRow(i);

		if (super.cbConsulta.getSelectedItem().equals("Código")) {
			try{
				cidBO = cidDao.consultaPorCodigo(Integer.parseInt(super.txtDadoConsulta.getText()));
			}catch(NumberFormatException e1){
				JOptionPane.showMessageDialog(this, "O código deve ser numérico", "Erro", JOptionPane.ERROR_MESSAGE);
				super.txtDadoConsulta.selectAll();
				super.txtDadoConsulta.requestFocus();
				return;
			}
		} else if (super.cbConsulta.getSelectedItem().equals("Nome")) {
			cidBO = cidDao.consultaPorNome(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("UF")) {
			cidBO = cidDao.consultaPorUF(super.txtDadoConsulta.getText());
		} 

		int indice = 0;
		do {
			modelo.addRow(new Object[] {
					new Integer(cidBO.get(indice).getCodigo()),
					cidBO.get(indice).getNome(),
					cidBO.get(indice).getUf()
			});
			indice++;
		} while (indice < cidBO.size());
	}
	
	@Override
	public void excluir() {
		if (cidDao.excluir(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString())) == true)
			modelo.removeRow(tabela.getSelectedRow());
	}
	
	public void selecionar(){
		if (cadEmpregador != null) {
		cadEmpregador.txtCodCidade.setText(modelo.getValueAt(tabela.getSelectedRow(),0).toString());
		cadEmpregador.txtMostraCidade.setText(modelo.getValueAt(tabela.getSelectedRow(),1).toString() +
				" - " + modelo.getValueAt(tabela.getSelectedRow(),2).toString());
		} else if (cadEmpreiteiro != null) {
			cadEmpreiteiro.txtCodCidade.setText(modelo.getValueAt(tabela.getSelectedRow(),0).toString());
			cadEmpreiteiro.txtMostraCidade.setText(modelo.getValueAt(tabela.getSelectedRow(),1).toString() +
						" - " + modelo.getValueAt(tabela.getSelectedRow(),2).toString());
		}
		doDefaultCloseAction();
	}

}