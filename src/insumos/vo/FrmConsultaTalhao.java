package insumos.vo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import gerais.vo.FrmConsultaPai;
import insumos.bo.TalhaoBO;
import insumos.dao.TalhaoDao;
import safristas.vo.FrmMenuGeralMaca;
import util.ModeloTabela;
import util.Parse;

public class FrmConsultaTalhao extends FrmConsultaPai {
	private static final long serialVersionUID = -1252741409133300333L;

	TalhaoDao talhaoDAO = new TalhaoDao();

	JTable tabela;
	ModeloTabela modelo;
	TalhaoBO talhaoBO;
	FrmLancaAplicacao lancaApl = null;
	
	public FrmConsultaTalhao(FrmLancaAplicacao lancaApl) {
		this();
		this.lancaApl = lancaApl;
		
		btnAlterar.setVisible(false);
		btnExcluir.setVisible(false);
		btnIncluir.setText("Selecionar");
		btnIncluir.setIcon(new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
	}

	public FrmConsultaTalhao() {

		setTitle("Consulta Talhões");
		setSize(getWidth()-76, getHeight()-40);

		cbConsulta.addItem("Número");
		cbConsulta.addItem("Área");
//		cbConsulta.addItem("Tipo");
		cbConsulta.setSelectedItem("Número");

		ArrayList<Object> dados = new ArrayList<Object>();

		String[] colunas = new String[] {"Código", "Tipo", "Número", "Área"};

		boolean[] edicao = {false, false, false};

		// criação da tabela baseada no modelo ModeloTabela
		modelo = new ModeloTabela(dados, colunas, edicao);
		tabela = new JTable(modelo);
		tabela.getColumnModel().getColumn(0).setResizable(false);
		tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
		tabela.getColumnModel().getColumn(1).setPreferredWidth(200);
		tabela.getColumnModel().getColumn(1).setResizable(true);
		tabela.getColumnModel().getColumn(2).setPreferredWidth(200);
		tabela.getColumnModel().getColumn(2).setResizable(true);
		tabela.getColumnModel().getColumn(3).setPreferredWidth(200);
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
			FrmCadastraTalhao fr = new FrmCadastraTalhao();
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
			talhaoBO = new TalhaoBO();
			
			talhaoBO.setCodigo(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString()));
			
			talhaoBO.setTipo(modelo.getValueAt(tabela.getSelectedRow(), 1).toString());
			
			talhaoBO.setNumero(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),2).toString()));
			
			try {
				talhaoBO.setArea(Parse.stringToBigDecimalPonto(modelo.getValueAt(tabela.getSelectedRow(),3).toString()));
			} catch (ParseException e) {
				System.out.println("ERRO ALTERAR LAVOURA");
			}

			FrmCadastraTalhao fr = new FrmCadastraTalhao(this);  // envia essa consulta para o cadastro
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
		ArrayList <TalhaoBO> talhaoBO = null;
		// apaga todas as linhas da tabela
		for (int i = modelo.getRowCount() - 1; i >= 0; i--)
			modelo.removeRow(i);

		if (super.txtDadoConsulta.getText().equals("")) {
				talhaoBO = talhaoDAO.consultaTodos();	
		} else if (super.cbConsulta.getSelectedItem().equals("Número")) {
			try {
				talhaoBO = talhaoDAO.consultaPorNumero(Integer.parseInt(super.txtDadoConsulta.getText()));
			} catch(NumberFormatException e1){
				JOptionPane.showMessageDialog(this, "O código deve ser numérico", "Erro", JOptionPane.ERROR_MESSAGE);
				super.txtDadoConsulta.selectAll();
				super.txtDadoConsulta.requestFocus();
				return;
			}
		} else if (super.cbConsulta.getSelectedItem().equals("Área")) {
			try {
				talhaoBO = talhaoDAO.consultaPorArea(new BigDecimal(txtDadoConsulta.getText()));
			} catch(NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "A área deve ser numérica", "Erro", JOptionPane.ERROR_MESSAGE);
				super.txtDadoConsulta.selectAll();
				super.txtDadoConsulta.requestFocus();
				return;
			}
		}

		int indice = 0;
		do {
			modelo.addRow(new Object[] {
					new Integer(talhaoBO.get(indice).getCodigo()),
					talhaoBO.get(indice).getTipo(),
					new Integer(talhaoBO.get(indice).getNumero()),
					talhaoBO.get(indice).getArea(),
			});
			indice++;
		} while (indice < talhaoBO.size());
	}

	@Override
	public void excluir() {
		if (talhaoDAO.excluir(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString())) == true)
			modelo.removeRow(tabela.getSelectedRow());
	}

	public void selecionar() {
		if (lancaApl != null) {
			lancaApl.txtCodigoTalhao.setText(modelo.getValueAt(tabela.getSelectedRow(),0).toString());
		}
		doDefaultCloseAction();
	}
}