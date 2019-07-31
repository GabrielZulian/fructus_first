package pragas.vo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import exceptions.StringVaziaException;
import gerais.vo.FrmConsultaPai;
import pragas.bo.AtrativoBO;
import pragas.dao.AtrativoDao;
import safristas.vo.FrmMenuGeralMaca;
import util.ModeloTabela;
 
public class FrmConsultaAtrativo extends FrmConsultaPai {
	private static final long serialVersionUID = -1252741409133300333L;

	AtrativoDao atratDao = new AtrativoDao();

	JTable tabela;
	ModeloTabela modelo;
	AtrativoBO atratBO;
	FrmCadastraAtrativo cadAtrativo = null;

	public FrmConsultaAtrativo() {

		setTitle("Consulta Atrativos");
		setSize(getWidth()-50, getHeight()-40);

		cbConsulta.removeItem("Nome");
		cbConsulta.addItem("Descrição");
		cbConsulta.setSelectedItem("Descrição");

		ArrayList<Object> dados = new ArrayList<Object>();

		String[] colunas = new String[] {"Código", "Descrição", "Dias p/ troca"};

		boolean[] edicao = {false, false, false};

		// cria��o da tabela baseada no modelo ModeloTabela
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
				if (e.getClickCount() == 2) {
					if (cadAtrativo != null)
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
		if (cadAtrativo == null) {
			FrmCadastraAtrativo fr = new FrmCadastraAtrativo();
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
			atratBO = new AtrativoBO();
			atratBO.setCodigo(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString()));
			try {
				atratBO.setDescricao(modelo.getValueAt(tabela.getSelectedRow(),1).toString());
			} catch (StringVaziaException e1) {}
			atratBO.setDiasTroca(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),2).toString()));

			FrmCadastraAtrativo fr = new FrmCadastraAtrativo(this);  // envia essa consulta para o cadastro
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
		ArrayList <AtrativoBO> atratBO = null;

		// apaga todas as linhas da tabela
		for (int i = modelo.getRowCount() - 1; i >= 0; i--)
			modelo.removeRow(i);

		if (super.cbConsulta.getSelectedItem().equals("Código")) {
			try {
				atratBO = atratDao.consultaPorCodigo(Integer.parseInt(super.txtDadoConsulta.getText()));
			} catch(NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "O c�digo deve ser numérico", "Erro", JOptionPane.ERROR_MESSAGE);
				super.txtDadoConsulta.selectAll();
				super.txtDadoConsulta.requestFocus();
				return;
			}
		} else if (super.cbConsulta.getSelectedItem().equals("Descrição")) {
			atratBO = atratDao.consultaPorNome(super.txtDadoConsulta.getText());
		}

		int indice = 0;
		do {
			modelo.addRow(new Object[] {
					new Integer(atratBO.get(indice).getCodigo()),
					atratBO.get(indice).getDescricao(),
					atratBO.get(indice).getDiasTroca()
			});
			indice++;
		} while (indice < atratBO.size());
	}
	
	@Override
	public void excluir() {
		if (atratDao.excluir(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString())) == true)
			modelo.removeRow(tabela.getSelectedRow());
	}
	
	public void selecionar() {
	
	}

}