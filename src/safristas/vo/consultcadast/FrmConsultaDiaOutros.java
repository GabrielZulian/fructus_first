package safristas.vo.consultcadast;

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
import safristas.bo.outros.LancaDiaOutrosBO;
import safristas.dao.outros.LancaDiaOutrosDao;
import safristas.vo.FrmMenuGeralMaca;
import safristas.vo.lancamentos.FrmDiaTrabalhoOutros;
import util.NumberRenderer;
import util.ModeloTabela;

public class FrmConsultaDiaOutros extends FrmConsultaPai {
	private static final long serialVersionUID = 1453706300582230467L;

	public JTable tabela;
	public ModeloTabela modelo;
	public LancaDiaOutrosBO diaOutBO; 
	LancaDiaOutrosDao diaOutDao = new LancaDiaOutrosDao();
	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

	public FrmConsultaDiaOutros() {

		setTitle("Consulta dias trabalhados - Outros");
		setSize(getWidth()+90, getHeight());

		cbConsulta.addItem("Data");
		cbConsulta.addItem("Fun��o");
		cbConsulta.addItem("C�d. Fun��o");
		cbConsulta.addItem("Valor");
		cbConsulta.setSelectedItem("Data");

		ArrayList<Object> dados = new ArrayList<Object>();

		String[] colunas = new String[] {"C�digo", "Data", "Nome", "Fun��o", "Valor Total", "Hist�rico", "Situa��o"};

		boolean[] edicao = {false, false, false, false, false, false, false};

		modelo = new ModeloTabela(dados, colunas, edicao);
		tabela = new JTable(modelo);
		tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
		tabela.getColumnModel().getColumn(0).setResizable(false);
		tabela.getColumnModel().getColumn(1).setPreferredWidth(90);
		tabela.getColumnModel().getColumn(1).setResizable(true);
		tabela.getColumnModel().getColumn(2).setPreferredWidth(240);
		tabela.getColumnModel().getColumn(2).setResizable(true);
		tabela.getColumnModel().getColumn(3).setPreferredWidth(160);
		tabela.getColumnModel().getColumn(3).setResizable(true);
		tabela.getColumnModel().getColumn(4).setPreferredWidth(120);
		tabela.getColumnModel().getColumn(4).setResizable(true);
		tabela.getColumnModel().getColumn(4).setCellRenderer(NumberRenderer.getCurrencyRenderer());
		tabela.getColumnModel().getColumn(5).setPreferredWidth(280);
		tabela.getColumnModel().getColumn(5).setResizable(true);
		tabela.getColumnModel().getColumn(6).setPreferredWidth(60);
		tabela.getColumnModel().getColumn(6).setResizable(true);
		tabela.getTableHeader().setReorderingAllowed(false); 
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane rolagemTabela = new JScrollPane(tabela);
		painelGeral.add(rolagemTabela, BorderLayout.CENTER);
		tabela.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					alterar();
				}
			}
		});
		tabela.setFocusable(false);
		super.btnIncluir.setText("Lan�ar Dia (F1)");
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
	}

	@Override
	public void consultar() {
		ArrayList <LancaDiaOutrosBO> diaOutBO = null;

		// apaga todas as linhas da tabela
		for (int i = modelo.getRowCount() - 1; i >= 0; i--)
			modelo.removeRow(i);

		if (super.cbConsulta.getSelectedItem().equals("C�digo")) {
			diaOutBO = diaOutDao.consultaPorCodigo(Integer.parseInt(super.txtDadoConsulta.getText()));
		} else if (super.cbConsulta.getSelectedItem().equals("Nome")) {
			diaOutBO = diaOutDao.consultaPorNomeEmpregado(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("C�d. Fun��o")) {
			diaOutBO = diaOutDao.consultaPorCodFuncao(Integer.parseInt(super.txtDadoConsulta.getText()));
		} else if (super.cbConsulta.getSelectedItem().equals("Fun��o")) {
			diaOutBO = diaOutDao.consultaPorNomeFuncao(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("Data")) {
			diaOutBO = diaOutDao.consultaPorData(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("Valor")) {
			diaOutBO = diaOutDao.consultaPorValor(Double.parseDouble(super.txtDadoConsulta.getText().trim().replace(',', '.')));
		}

		int indice = 0;
		try {
			do {
				modelo.addRow(new Object[] {
						new Integer(diaOutBO.get(indice).getCodigo()),
						df.format(diaOutBO.get(indice).data.toDate()),
						diaOutBO.get(indice).adoBO.getNome(),
						diaOutBO.get(indice).adoBO.funcaoBO.getNome(),
						diaOutBO.get(indice).getValorTotal(),
						diaOutBO.get(indice).observacao.getText(),
						(diaOutBO.get(indice).getPagou() == 'N'? "Aberto" : "Pago")
				});
				indice++;
			} while (indice < diaOutBO.size());
		} catch (NullPointerException e) {}
	}

	@Override
	public void incluir() {
		FrmDiaTrabalhoOutros fr = new FrmDiaTrabalhoOutros();
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

			diaOutBO = new LancaDiaOutrosBO();
			diaOutBO = diaOutDao.consultaPorCodigo(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(), 0).toString())).get(0);
			FrmDiaTrabalhoOutros fr = new FrmDiaTrabalhoOutros(this);
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
		if (modelo.getValueAt(tabela.getSelectedRow(),6).toString() == "Pago") {
			JOptionPane.showMessageDialog(this, "Este dia j� cont�m pagamento! Excluir pagamento primeiro, caso deseje excluir este registro.", "Excluir Registro",JOptionPane.ERROR_MESSAGE);
		} else if (diaOutDao.excluir(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString())) == true) {
			modelo.removeRow(tabela.getSelectedRow());
		}
	}

}
