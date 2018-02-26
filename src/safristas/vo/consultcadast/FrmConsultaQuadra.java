package safristas.vo.consultcadast;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import gerais.bo.QuadraBO;
import gerais.dao.QuadraDao;
import gerais.vo.FrmConsultaPai;
import safristas.vo.FrmMenuGeralMaca;
import util.ModeloTabela;

public class FrmConsultaQuadra extends FrmConsultaPai {
	private static final long serialVersionUID = 4182560824415003202L;

	JTable tabela;
	ModeloTabela modelo;
	QuadraBO quadBO;
	QuadraDao quadDao = new QuadraDao();

	public FrmConsultaQuadra() {

		setTitle("Consulta Quadras");
		setResizable(true);
		cbConsulta.removeItem("Nome");
		setSize(getWidth()-80, getHeight());
		super.txtDadoConsulta.setColumns(12);

		cbConsulta.removeItem("Código");
		cbConsulta.addItem("Número");
		cbConsulta.addItem("Área");
		cbConsulta.addItem("Ano plantio");
		cbConsulta.addItem("Número de plantas");

		ArrayList<Object> dados = new ArrayList<Object>();

		String[] colunas = new String[] {"Número", "Área(ha)", "Ano plantio", "Nro Plantas p/ ha"};

		boolean[] edicao = {false, false, false, false, false};

		modelo = new ModeloTabela(dados, colunas, edicao);
		tabela = new JTable(modelo);
		tabela.getColumnModel().getColumn(0).setPreferredWidth(80);
		tabela.getColumnModel().getColumn(0).setResizable(true);
		tabela.getColumnModel().getColumn(1).setPreferredWidth(180);
		tabela.getColumnModel().getColumn(1).setResizable(true);
		tabela.getColumnModel().getColumn(2).setPreferredWidth(80);
		tabela.getColumnModel().getColumn(2).setResizable(true);
		tabela.getColumnModel().getColumn(3).setPreferredWidth(130);
		tabela.getColumnModel().getColumn(3).setResizable(true);
		tabela.getTableHeader().setReorderingAllowed(false);
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane rolagemTabela = new JScrollPane(tabela);
		painelGeral.add(rolagemTabela, BorderLayout.CENTER);

		tabela.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2){
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
		ArrayList <QuadraBO> quadBO = null;

		// apaga todas as linhas da tabela
		for (int i = modelo.getRowCount() - 1; i >= 0; i--)
			modelo.removeRow(i);

		if (super.cbConsulta.getSelectedItem().equals("Número")) {
			quadBO = quadDao.consultaPorNumero(Integer.parseInt(super.txtDadoConsulta.getText()));
		} else if (super.cbConsulta.getSelectedItem().equals("Área")) {
			try{
				String valorString = super.txtDadoConsulta.getText().replaceAll(",","");
				quadBO = quadDao.consultaPorArea(new BigDecimal(valorString));
			} catch(NumberFormatException e1){
				JOptionPane.showMessageDialog(this, "A área deve ser numérica", "Erro",JOptionPane.ERROR_MESSAGE);
				super.txtDadoConsulta.selectAll();
				super.txtDadoConsulta.requestFocus();
				return;
			}
		} else if (super.cbConsulta.getSelectedItem().equals("Ano plantio")) {
			try{
				quadBO = quadDao.consultaPorAnoPlantio(Integer.parseInt(super.txtDadoConsulta.getText()));
			} catch(NumberFormatException e1){
				JOptionPane.showMessageDialog(this, "A área deve ser numérica", "Erro",JOptionPane.ERROR_MESSAGE);
				super.txtDadoConsulta.selectAll();
				super.txtDadoConsulta.requestFocus();
				return;
			}
		} else if (super.cbConsulta.getSelectedItem().equals("Número de plantas")) {
			try{
				quadBO = quadDao.consultaPorNroPlantas(Integer.parseInt(super.txtDadoConsulta.getText()));
			} catch(NumberFormatException e1){
				JOptionPane.showMessageDialog(this, "A área deve ser numérica", "Erro",JOptionPane.ERROR_MESSAGE);
				super.txtDadoConsulta.selectAll();
				super.txtDadoConsulta.requestFocus();
				return;
			}
		}
		
		geraTabela(quadBO);
	}

	private void geraTabela(ArrayList<QuadraBO> quadBO) {
		int indice = 0;
		
		do {
			modelo.addRow(new Object[] {
					quadBO.get(indice).getNumero(),
					quadBO.get(indice).getArea(),
					quadBO.get(indice).getNroPlantasHectare(),
					quadBO.get(indice).getAnoPlantio()
			});
			indice++;
		} while (indice < quadBO.size());
	}

	@Override
	public void incluir() {
		FrmCadastraQuadra fr = new FrmCadastraQuadra();
		fr.setVisible(true);
		getDesktopPane().add(fr);   
		try {
			fr.setSelected(true);
		}
		catch (PropertyVetoException exc) { }
		FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
	}

	@Override
	public void alterar() {
		if (tabela.getSelectedRow()>=0) {
			quadBO = new QuadraBO();

			quadBO = quadDao.consultaPorNumero(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString())).get(0);
			FrmCadastraQuadra fr = new FrmCadastraQuadra(this);
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
		if (quadDao.excluir(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString())))
			modelo.removeRow(tabela.getSelectedRow());
	}

	@SuppressWarnings("unused")
	private void selecionar() {
		doDefaultCloseAction();
	}

	public void configuraBotoes() {
		btnAlterar.setVisible(false);
		btnExcluir.setVisible(false);
		btnIncluir.setText("Selecionar");
		btnIncluir.setIcon(new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
	}
}
