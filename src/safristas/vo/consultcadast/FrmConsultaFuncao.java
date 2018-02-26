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
import safristas.bo.ProvaBO;
import safristas.dao.FuncaoDao;
import safristas.vo.FrmMenuGeralMaca;

public class FrmConsultaFuncao extends FrmConsultaPai {
	private static final long serialVersionUID = 4988918377344609504L;

	FuncaoDao funDao = new FuncaoDao();
	JTable tabela;
	ModeloTabela modelo;
	ProvaBO funBO;

	FrmCadastraEmpregado cadEmpregado = null;

	public FrmConsultaFuncao(FrmCadastraEmpregado cadEmpregado) {
		this();
		this.cadEmpregado = cadEmpregado;
		btnAlterar.setVisible(false);
		btnExcluir.setVisible(false);
		btnIncluir.setText("Selecionar");
		btnIncluir.setIcon(new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
	}

	public FrmConsultaFuncao() {
		cbConsulta.addItem("Descrição");
		cbConsulta.removeItem("Nome");
		cbConsulta.setSelectedItem("Descrição");
		
		setTitle("Consulta Funções");
		setSize(getWidth()-70, getHeight()-100);

		ArrayList<Object> dados = new ArrayList<Object>();

		String[] colunas = new String[] {"Código", "Descrição"};

		boolean[] edicao = {false, false};

		// criação da tabela baseada no modelo ModeloTabela
		modelo = new ModeloTabela(dados, colunas, edicao);
		tabela = new JTable(modelo);
		tabela.getColumnModel().getColumn(0).setResizable(false);
		tabela.getColumnModel().getColumn(0).setPreferredWidth(80);
		tabela.getColumnModel().getColumn(1).setPreferredWidth(500);
		tabela.getColumnModel().getColumn(1).setResizable(true);
		tabela.getTableHeader().setReorderingAllowed(false); 
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
		ArrayList <ProvaBO> funBO = null;

		// apaga todas as linhas da tabela
		for (int i = modelo.getRowCount() - 1; i >= 0; i--)
			modelo.removeRow(i);

		if (super.cbConsulta.getSelectedItem().equals("Código")) {
			try {
				funBO = funDao.consultaPorCodigo(Integer.parseInt(super.txtDadoConsulta.getText()));
			} catch(NumberFormatException e1){
				JOptionPane.showMessageDialog(this, "O código deve ser numérico", "Erro",JOptionPane.ERROR_MESSAGE);
				super.txtDadoConsulta.selectAll();
				super.txtDadoConsulta.requestFocus();
				return;
			}
		} else if (super.cbConsulta.getSelectedItem().equals("Descrição")) {
			funBO = funDao.consultaPorNome(super.txtDadoConsulta.getText());
		}

		int indice = 0;
		do {
			modelo.addRow(new Object[] {
					new Integer(funBO.get(indice).getCodigo()),
					funBO.get(indice).getNome()
			});
			indice++;
		} while (indice < funBO.size());
	}

	@Override
	public void incluir() {
		if (cadEmpregado == null) { // botao incluir
			FrmCadastraFuncao fr = new FrmCadastraFuncao();
			fr.setVisible(true);
			getDesktopPane().add(fr); 
			try {
				fr.setSelected(true);
			}
			catch (PropertyVetoException exc) { }
			FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
		} else // botao selecionar
			selecionar();
	}

	@Override
	public void alterar() {
		if (tabela.getSelectedRow()>=0) {
			if (Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(), 0).toString()) == 1 
					|| Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(), 0).toString()) == 2 
					|| Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(), 0).toString()) == 3) {
				JOptionPane.showMessageDialog(this, "Registro bloqueado por padrão!", "Registro Bloqueado",JOptionPane.ERROR_MESSAGE);
			}else {
				funBO = new ProvaBO();   // criaçao do objeto função
				funBO.setCodigo(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString()));
				try {
					funBO.setNome(modelo.getValueAt(tabela.getSelectedRow(),1).toString());
				} catch (StringVaziaException e1) {}

				FrmCadastraFuncao fr = new FrmCadastraFuncao(this);  // envia essa consulta para o cadastro
				fr.setVisible(true);  
				getDesktopPane().add(fr);
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) {}
				FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
			}
		} else
			JOptionPane.showMessageDialog(this, "Selecione um registro primeiro!", "Selecionar Registro",JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void excluir() {
		if (Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(), 0).toString()) == 1 
				|| Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(), 0).toString()) == 2
				|| Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(), 0).toString()) == 3) {
			JOptionPane.showMessageDialog(this, "Registro bloqueado por padrão!", "Registro Bloqueado",JOptionPane.ERROR_MESSAGE);
		} else if (funDao.excluir(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString())))
			modelo.removeRow(tabela.getSelectedRow());
	}

	private void selecionar(){
		if (cadEmpregado != null) {
			cadEmpregado.txtCodFuncao.setText(modelo.getValueAt(tabela.getSelectedRow(),0).toString());
			cadEmpregado.txtMostraFuncao.setText(modelo.getValueAt(tabela.getSelectedRow(),1).toString());
		}
		doDefaultCloseAction();
	}

}
