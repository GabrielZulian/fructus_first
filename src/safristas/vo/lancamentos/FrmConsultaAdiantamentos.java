package safristas.vo.lancamentos;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import gerais.vo.FrmConsultaPai;
import safristas.bo.AdiantamentoBO;
import safristas.dao.AdiantamentoDao;
import safristas.vo.FrmMenuGeralMaca;
import util.NumberRenderer;
import util.ModeloTabela;

public class FrmConsultaAdiantamentos extends FrmConsultaPai {
	private static final long serialVersionUID = 3343608749992984374L;
	
	JTable tabela;
	ModeloTabela modelo;
	AdiantamentoBO adiantBO;
	AdiantamentoDao adiantDao = new AdiantamentoDao();
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	DecimalFormat decimal = new DecimalFormat( "#,##0.00" );
	FrmDescAcrescPagamentos frmDesconto = null;
	
	public FrmConsultaAdiantamentos (FrmDescAcrescPagamentos frmDesconto) {
		this();
		this.frmDesconto = frmDesconto;
		btnAlterar.setVisible(false);
		btnExcluir.setVisible(false);
		btnIncluir.setText("Selecionar (F1)");
		btnIncluir.setIcon(new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
		cbConsulta.setSelectedItem("Situação");
		txtDadoConsulta.setText("N");
		txtDadoConsulta.setEditable(false);
		cbConsulta.setEnabled(false);
		btnConsultar.doClick();
	}

	public FrmConsultaAdiantamentos() {

		setTitle("Consulta Adiantamentos");
		setSize(getWidth()-40, getHeight()-40);

		cbConsulta.removeItem("Nome");
		cbConsulta.addItem("Data");
		cbConsulta.addItem("Valor");
		cbConsulta.addItem("Situa��o");
		cbConsulta.setSelectedItem("Data");

		ArrayList<Object> dados = new ArrayList<Object>();

		String[] colunas = new String[] {"C�digo", "Data Lan�amento", "Nome", "Valor", "Situa��o"};

		boolean[] edicao = {false, false, false, false, false};

		modelo = new ModeloTabela(dados, colunas, edicao);
		tabela = new JTable(modelo);
		tabela.getColumnModel().getColumn(0).setResizable(false);
		tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
		tabela.getColumnModel().getColumn(1).setPreferredWidth(120);
		tabela.getColumnModel().getColumn(1).setResizable(true);
		tabela.getColumnModel().getColumn(2).setPreferredWidth(350);
		tabela.getColumnModel().getColumn(2).setResizable(true);
		tabela.getColumnModel().getColumn(3).setPreferredWidth(80);
		tabela.getColumnModel().getColumn(3).setResizable(true);
		tabela.getColumnModel().getColumn(3).setCellRenderer(NumberRenderer.getCurrencyRenderer());
		tabela.getColumnModel().getColumn(4).setPreferredWidth(80);
		tabela.getColumnModel().getColumn(4).setResizable(true);
		tabela.getTableHeader().setReorderingAllowed(false); 
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane rolagemTabela = new JScrollPane(tabela);
		painelGeral.add(rolagemTabela, BorderLayout.CENTER);
		
		tabela.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2){
					if (frmDesconto != null)
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
		if (frmDesconto == null) { // botao incluir
			FrmLancaAdiantamentos fr = new FrmLancaAdiantamentos();
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
			adiantBO = new AdiantamentoBO();
			adiantBO = adiantDao.consultaPorCodigo(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString())).get(0);
			
			FrmLancaAdiantamentos fr = new FrmLancaAdiantamentos(this);
			fr.setVisible(true);  
			getDesktopPane().add(fr);
			try {
				fr.setSelected(true);
			}catch (PropertyVetoException exc) { }
			FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
		}
	}
	
	@Override
	public void consultar() {
		ArrayList <AdiantamentoBO> adiantBO = null;

		// apaga todas as linhas da tabela
		for (int i = modelo.getRowCount() - 1; i >= 0; i--)
			modelo.removeRow(i);

		if (super.cbConsulta.getSelectedItem().equals("C�digo")) {
			try{
				adiantBO = adiantDao.consultaPorCodigo(Integer.parseInt(super.txtDadoConsulta.getText()));
			}catch(NumberFormatException e1){
				JOptionPane.showMessageDialog(this, "O c�digo deve ser num�rico", "Erro",JOptionPane.ERROR_MESSAGE);
				super.txtDadoConsulta.selectAll();
				super.txtDadoConsulta.requestFocus();
				return;
			}
		} else if (super.cbConsulta.getSelectedItem().equals("Data")) {
			adiantBO = adiantDao.consultaPorData(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("Valor")) {
			adiantBO = adiantDao.consultaPorValor(Double.parseDouble(super.txtDadoConsulta.getText().replace(',', '.')));
		} else if (super.cbConsulta.getSelectedItem().equals("Situação")) {
			adiantBO = adiantDao.consultaPorSituacao(super.txtDadoConsulta.getText().charAt(0));
		}

		int indice = 0;
		do {
			modelo.addRow(new Object[] {
					new Integer(adiantBO.get(indice).getCodigo()),
					df.format(adiantBO.get(indice).data.toDate()),
					verifNome(adiantBO.get(indice)),
					adiantBO.get(indice).getValor(),
					adiantBO.get(indice).getPagouString()
			});
			indice++;
		} while (indice < adiantBO.size());
	}
	
	@Override
	public void excluir() {
		if (adiantDao.excluir(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString())) == true)
			modelo.removeRow(tabela.getSelectedRow());
	}
	
	public void selecionar(){
		if (frmDesconto != null) {
			frmDesconto.txtCodAdiantamento.setText(modelo.getValueAt(tabela.getSelectedRow(),0).toString());
			frmDesconto.txtMostraAdiantamento.setText(modelo.getValueAt(tabela.getSelectedRow(),1).toString() +
			" - " + decimal.format(Double.parseDouble(modelo.getValueAt(tabela.getSelectedRow(),3).toString())));
			frmDesconto.txtValorDesconto.setText(decimal.format(Double.parseDouble(modelo.getValueAt(tabela.getSelectedRow(),3).toString())));
			frmDesconto.txtHistorico.setText("Adiantamento " + modelo.getValueAt(tabela.getSelectedRow(),1).toString());
		}
		doDefaultCloseAction();
	}
	
	public String verifNome(AdiantamentoBO adiantBO) {
		if (adiantBO.getTipo() == 'I')
			return adiantBO.iroBO.getNome();
		else if (adiantBO.getTipo() == 'A')
			return adiantBO.adoBO.getNome();

		return "";
	}

}