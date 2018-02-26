package insumos.vo;

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

import exceptions.StringVaziaException;
import gerais.vo.FrmConsultaPai;
import insumos.bo.InsumoBO;
import insumos.dao.InsumoDao;
import safristas.vo.FrmMenuGeralMaca;
import util.ModeloTabela;
 
public class FrmConsultaInsumo extends FrmConsultaPai {
	private static final long serialVersionUID = -1252741409133300333L;

	InsumoDao insumoDao = new InsumoDao();

	JTable tabela;
	ModeloTabela modelo;
	InsumoBO insumoBO;
	FrmLancaAplicacao lancaApl = null;
	
	public FrmConsultaInsumo(FrmLancaAplicacao lancaApl) {
		this();
		this.lancaApl = lancaApl;
		
		btnAlterar.setVisible(false);
		btnExcluir.setVisible(false);
		btnIncluir.setText("Selecionar");
		btnIncluir.setIcon(new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
	}

	public FrmConsultaInsumo() {

		setTitle("Consulta Insumos");
		setSize(getWidth()-50, getHeight()-40);

		cbConsulta.removeItem("Nome");
		cbConsulta.addItem("Descrição");
		cbConsulta.setSelectedItem("Descrição");

		ArrayList<Object> dados = new ArrayList<Object>();

		String[] colunas = new String[] {"Código", "Descrição", "Residual (Dias)", "Unidade"};

		boolean[] edicao = {false, false, false, false};

		// criação da tabela baseada no modelo ModeloTabela
		modelo = new ModeloTabela(dados, colunas, edicao);
		tabela = new JTable(modelo);
		tabela.getColumnModel().getColumn(0).setResizable(false);
		tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
		tabela.getColumnModel().getColumn(1).setPreferredWidth(430);
		tabela.getColumnModel().getColumn(1).setResizable(true);
		tabela.getColumnModel().getColumn(2).setPreferredWidth(200);
		tabela.getColumnModel().getColumn(2).setResizable(true);
		tabela.getColumnModel().getColumn(3).setPreferredWidth(56);
		tabela.getColumnModel().getColumn(3).setResizable(true);
		tabela.getTableHeader().setReorderingAllowed(false); 
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane rolagemTabela = new JScrollPane(tabela);
		painelGeral.add(rolagemTabela, BorderLayout.CENTER);
		
		tabela.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (lancaApl != null)
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
		if (lancaApl == null) {
			FrmCadastraInsumo fr = new FrmCadastraInsumo();
			fr.setVisible(true);
			getDesktopPane().add(fr); 
			try {
				fr.setSelected(true);
			}
			catch (PropertyVetoException exc) { }
			FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
		} else {  // botao selecionar
			selecionar();
		}
	}

	public void alterar() {
		if (tabela.getSelectedRow()>=0) {
			insumoBO = new InsumoBO();
			insumoBO.setCodigo(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString()));
			try {
				insumoBO.setDescricao(modelo.getValueAt(tabela.getSelectedRow(),1).toString());
			} catch (StringVaziaException e1) { }
			insumoBO.setDiasResidual(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),2).toString()));
			insumoBO.setUnidade(modelo.getValueAt(tabela.getSelectedRow(),2).toString());

			FrmCadastraInsumo fr = new FrmCadastraInsumo(this);  // envia essa consulta para o cadastro
			fr.setVisible(true);  
			getDesktopPane().add(fr);
			try {
				fr.setSelected(true);
			} catch (PropertyVetoException exc) { }
			FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
		} else
			JOptionPane.showMessageDialog(this, "Selecione um registro primeiro!", "Selecionar Registro",JOptionPane.INFORMATION_MESSAGE);
	}
	
	@Override
	public void consultar() {
		ArrayList <InsumoBO> insumoBO = null;
		// apaga todas as linhas da tabela
		for (int i = modelo.getRowCount() - 1; i >= 0; i--)
			modelo.removeRow(i);

		if (super.cbConsulta.getSelectedItem().equals("Código")) {
			try {
				insumoBO = insumoDao.consultaPorCodigo(Integer.parseInt(super.txtDadoConsulta.getText()));
			} catch(NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "O código deve ser numérico", "Erro", JOptionPane.ERROR_MESSAGE);
				super.txtDadoConsulta.selectAll();
				super.txtDadoConsulta.requestFocus();
				return;
			}
		} else if (super.cbConsulta.getSelectedItem().equals("Descrição")) {
			insumoBO = insumoDao.consultaPorNome(super.txtDadoConsulta.getText());
		}

		int indice = 0;
		do {
			modelo.addRow(new Object[] {
					new Integer(insumoBO.get(indice).getCodigo()),
					insumoBO.get(indice).getDescricao(),
					insumoBO.get(indice).getDiasResidual(),
					insumoBO.get(indice).getUnidade()
			});
			indice++;
		} while (indice < insumoBO.size());
	}
	
	@Override
	public void excluir() {
		if (insumoDao.excluir(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString())) == true)
			modelo.removeRow(tabela.getSelectedRow());
	}
	
	public void selecionar() {
		if (lancaApl != null) {
			lancaApl.txtMostraInsumo.setText(modelo.getValueAt(tabela.getSelectedRow(),1).toString() + " - " +  modelo.getValueAt(tabela.getSelectedRow(),3).toString());
		}
		doDefaultCloseAction();
	}

}