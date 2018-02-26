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
import safristas.bo.outros.VeiculoBO;
import safristas.dao.outros.VeiculoDao;
import safristas.vo.FrmMenuGeralMaca;
import safristas.vo.lancamentos.FrmDiaTrabalhoOutros;
import util.ModeloTabela;

public class FrmConsultaVeiculo extends FrmConsultaPai {
	private static final long serialVersionUID = -7892087613652361353L;
	
	JTable tabela;
	ModeloTabela modelo;
	VeiculoBO veicBO;
	VeiculoDao veicDao = new VeiculoDao();
	FrmDiaTrabalhoOutros pagOutros = null;

	public FrmConsultaVeiculo(FrmDiaTrabalhoOutros pagOutros) {
		this();
		this.pagOutros = pagOutros;
		cbConsulta.setSelectedItem("Tipo");
		if (pagOutros.rBtnCaminhao.isSelected())
			txtDadoConsulta.setText("C");
		else if (pagOutros.rBtnTrator.isSelected())
			txtDadoConsulta.setText("T");
		else
			cbConsulta.setSelectedItem("Placa");

		btnConsultar.doClick();
		btnAlterar.setVisible(false);
		btnExcluir.setVisible(false);
		btnIncluir.setText("Selecionar");
		btnIncluir.setIcon(new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
	}

	public FrmConsultaVeiculo() {

		setTitle("Consulta Veículos");
		setSize(getWidth()+30, getHeight()-40);

		cbConsulta.removeItem("Nome");
		cbConsulta.addItem("Placa");
		cbConsulta.addItem("Tipo");
		cbConsulta.setSelectedItem("Placa");

		ArrayList<Object> dados = new ArrayList<Object>();

		String[] colunas = new String[] {"Código", "Placa/Identificação", "Tipo", "Descrição", "Nome Proprietário", "Tipo Proprietário"};

		boolean[] edicao = {false, false, false, false, false, false};

		// criação da tabela baseada no modelo ModeloTabela
		modelo = new ModeloTabela(dados, colunas, edicao);
		tabela = new JTable(modelo);
		tabela.getColumnModel().getColumn(0).setResizable(false);
		tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
		tabela.getColumnModel().getColumn(1).setPreferredWidth(120);
		tabela.getColumnModel().getColumn(1).setResizable(true);
		tabela.getColumnModel().getColumn(2).setPreferredWidth(110);
		tabela.getColumnModel().getColumn(2).setResizable(true);
		tabela.getColumnModel().getColumn(3).setPreferredWidth(290);
		tabela.getColumnModel().getColumn(3).setResizable(true);
		tabela.getColumnModel().getColumn(4).setPreferredWidth(220);
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
					if (pagOutros != null)
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
		if (pagOutros == null) { // botao incluir
			FrmCadastraVeiculo fr = new FrmCadastraVeiculo();
			fr.setVisible(true);
			getDesktopPane().add(fr); 
			try {
				fr.setSelected(true);
			}
			catch (PropertyVetoException exc) { }
			FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
		}
		else{  // botao selecionar
			selecionar();
		}
	}

	public void alterar() {
		if (tabela.getSelectedRow()>=0) {
			veicBO = new VeiculoBO(); 
			veicBO = veicDao.consultaPorCodigo(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString())).get(0);

			FrmCadastraVeiculo fr = new FrmCadastraVeiculo(this);
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
		ArrayList <VeiculoBO> veicBO = null;

		// apaga todas as linhas da tabela
		for (int i = modelo.getRowCount() - 1; i >= 0; i--)
			modelo.removeRow(i);

		if (super.cbConsulta.getSelectedItem().equals("Código")) {
			try{
				veicBO = veicDao.consultaPorCodigo(Integer.parseInt(super.txtDadoConsulta.getText()));
			}catch(NumberFormatException e1){
				JOptionPane.showMessageDialog(this, "O código deve ser numérico", "Erro",JOptionPane.ERROR_MESSAGE);
				super.txtDadoConsulta.selectAll();
				super.txtDadoConsulta.requestFocus();
				return;
			}
		} else if (super.cbConsulta.getSelectedItem().equals("Placa")) {
			veicBO = veicDao.consultaPorPlaca(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("Tipo")) {
			veicBO = veicDao.consultaPorTipo(super.txtDadoConsulta.getText());
		} 

		int indice = 0;
		try {
			do {
				modelo.addRow(new Object[] {
						new Integer(veicBO.get(indice).getCodigo()),
						veicBO.get(indice).getPlaca(),
						veicBO.get(indice).getTipoVeiculoString(),
						veicBO.get(indice).getDescricao(),
						verifNomeProp(veicBO.get(indice)),
						veicBO.get(indice).getTipoEmpregadoString()
				});
				indice++;
			} while (indice < veicBO.size());
		} catch (NullPointerException erro) {}
	}

	@Override
	public void excluir() {
		if (veicDao.excluir(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString())) == true)
			modelo.removeRow(tabela.getSelectedRow());
	}

	public void selecionar() {
		if (pagOutros != null) {
			pagOutros.txtCodVeiculo.setText(modelo.getValueAt(tabela.getSelectedRow(),0).toString());
			pagOutros.txtMostraVeiculo.setText(modelo.getValueAt(tabela.getSelectedRow(),1).toString() + " - " +
					modelo.getValueAt(tabela.getSelectedRow(), 2));
		}
		doDefaultCloseAction();
	}

	public String verifNomeProp (VeiculoBO veiculoBO) {
		if (veiculoBO.getTipoEmpregado() == 'I')
			return veiculoBO.proprietarioIro.getNome();
		else if (veiculoBO.getTipoEmpregado() == 'A')
			return veiculoBO.proprietarioAdo.getNome();
		else if (veiculoBO.getTipoEmpregado() == 'D')
			return veiculoBO.proprietarioDor.getNome();

		return "";
	}

}