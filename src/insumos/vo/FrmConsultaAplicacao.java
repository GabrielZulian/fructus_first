package insumos.vo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import gerais.vo.FrmConsultaPai;
import insumos.bo.AplicacaoBO;
import insumos.dao.AplicacaoDao;
import safristas.vo.FrmMenuGeralMaca;
import util.ModeloTabela;

public class FrmConsultaAplicacao extends FrmConsultaPai {
	private static final long serialVersionUID = -1252741409133300333L;

	AplicacaoDao aplicacaoDao = new AplicacaoDao();

	JTable tabela;
	ModeloTabela modelo;
	AplicacaoBO aplicacaoBO;
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	FrmLancaAplicacao lancaAplicacao = null;

	static final int CODIGO = 0;
	static final int DATA = 1;
	static final int NRO_TALHAO = 2;
	static final int TIPO_TALHAO = 3;
	static final int INSUMO = 4;
	static final int HISTORICO = 5;

	public FrmConsultaAplicacao() {

		setTitle("Consulta Aplicações");
		setSize(getWidth()-50, getHeight()-40);

		cbConsulta.removeItem("Nome");
		cbConsulta.addItem("Histórico");
		cbConsulta.addItem("Data");
		cbConsulta.addItem("Nro. Talhão");
		cbConsulta.addItem("Insumo");
		cbConsulta.setSelectedItem("Data");

		ArrayList<Object> dados = new ArrayList<Object>();

		String[] colunas = new String[] {"Código", "Data", "Nro. Talhão", "Tipo Talhão", "Insumo", "Histórico"};

		boolean[] edicao = {false, false, false, false, false, false};

		// criação da tabela baseada no modelo ModeloTabela
		modelo = new ModeloTabela(dados, colunas, edicao);
		tabela = new JTable(modelo);
		tabela.getColumnModel().getColumn(CODIGO).setResizable(false);
		tabela.getColumnModel().getColumn(CODIGO).setPreferredWidth(50);
		tabela.getColumnModel().getColumn(DATA).setPreferredWidth(80);
		tabela.getColumnModel().getColumn(DATA).setResizable(true);
		tabela.getColumnModel().getColumn(NRO_TALHAO).setPreferredWidth(80);
		tabela.getColumnModel().getColumn(NRO_TALHAO).setResizable(true);
		tabela.getColumnModel().getColumn(TIPO_TALHAO).setPreferredWidth(100);
		tabela.getColumnModel().getColumn(TIPO_TALHAO).setResizable(true);
		tabela.getColumnModel().getColumn(INSUMO).setPreferredWidth(200);
		tabela.getColumnModel().getColumn(INSUMO).setResizable(true);
		tabela.getColumnModel().getColumn(HISTORICO).setPreferredWidth(220);
		tabela.getColumnModel().getColumn(HISTORICO).setResizable(true);
		tabela.getTableHeader().setReorderingAllowed(false); 
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane rolagemTabela = new JScrollPane(tabela);
		painelGeral.add(rolagemTabela, BorderLayout.CENTER);

		tabela.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2){
					if (lancaAplicacao != null)
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
		if (lancaAplicacao == null) {
			FrmLancaAplicacao fr = new FrmLancaAplicacao();
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
			aplicacaoBO = new AplicacaoBO();
			AplicacaoDao aplicacaoDao = new AplicacaoDao();

			int codigo = Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),CODIGO).toString());

			aplicacaoBO = aplicacaoDao.consultaPorCodigo(codigo).get(0);

			FrmLancaAplicacao fr = new FrmLancaAplicacao(this);  // envia essa consulta para o cadastro
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
		ArrayList <AplicacaoBO> aplicacaoBO = null;
		// apaga todas as linhas da tabela
		for (int i = modelo.getRowCount() - 1; i >= 0; i--)
			modelo.removeRow(i);

		if (super.cbConsulta.getSelectedItem().equals("Código")) {
			try {
				aplicacaoBO = aplicacaoDao.consultaPorCodigo(Integer.parseInt(super.txtDadoConsulta.getText()));
			} catch(NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "O código deve ser numérico", "Erro", JOptionPane.ERROR_MESSAGE);
				super.txtDadoConsulta.selectAll();
				super.txtDadoConsulta.requestFocus();
				return;
			}
		} else if (super.cbConsulta.getSelectedItem().equals("Histórico")) {
			aplicacaoBO = aplicacaoDao.consultaPorHistorico(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("Insumo")) {
			aplicacaoBO = aplicacaoDao.consultaPorInsumo(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("Data")) {
			aplicacaoBO = aplicacaoDao.consultaPorData(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("Tipo Talhão")) {
			aplicacaoBO = aplicacaoDao.consultaPorTipoTalhao(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("Nro. Talhão")) {
			try {
				aplicacaoBO = aplicacaoDao.consultaPorNroTalhao(Integer.parseInt(super.txtDadoConsulta.getText()));
			} catch(NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "O número deve ser numérico", "Erro", JOptionPane.ERROR_MESSAGE);
				super.txtDadoConsulta.selectAll();
				super.txtDadoConsulta.requestFocus();
				return;
			}
		}
		int indice = 0;
		do {
			modelo.addRow(new Object[] {
					new Integer(aplicacaoBO.get(indice).getCodigo()),
					df.format(aplicacaoBO.get(indice).getData().toDate()),
					new Integer(aplicacaoBO.get(indice).talhaoBO.getNumero()),
					aplicacaoBO.get(indice).talhaoBO.getTipo(),
					aplicacaoBO.get(indice).insumoBO.getDescricao(),
					aplicacaoBO.get(indice).getHistorico()
			});

			indice++;
		} while (indice < aplicacaoBO.size());
	}

	@Override
	public void excluir() {
		if (aplicacaoDao.excluir(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),CODIGO).toString())) == true)
			modelo.removeRow(tabela.getSelectedRow());
	}

	public void selecionar() {

	}

}