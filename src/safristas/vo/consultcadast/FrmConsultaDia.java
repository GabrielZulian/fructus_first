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
import safristas.bo.safristas.DiaEmpregadoBO;
import safristas.bo.safristas.LancaDiaBO;
import safristas.dao.safristas.DiaEmpregadoDao;
import safristas.dao.safristas.LancaDiaDao;
import safristas.vo.FrmMenuGeralMaca;
import safristas.vo.lancamentos.FrmDiaTrabalho;
import util.NumberRenderer;
import util.ModeloTabela;

public class FrmConsultaDia extends FrmConsultaPai {
	private static final long serialVersionUID = 385481407191159707L;

	public JTable tabela;
	public ModeloTabela modelo;
	public LancaDiaBO diaBO; 
	LancaDiaDao diaDao = new LancaDiaDao();
	DiaEmpregadoDao diaAdoDao = new DiaEmpregadoDao();
	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	public ArrayList<DiaEmpregadoBO> diaAdoBO;

	public FrmConsultaDia() {
		setTitle("Consulta Dias Trabalhados - Safristas");
		setSize(getWidth()+80, getHeight());

		cbConsulta.removeItem("Nome");
		cbConsulta.addItem("Nome Equipe");
		cbConsulta.addItem("Nome Empreiteiro");
		cbConsulta.addItem("Data");
		cbConsulta.addItem("Valor");
		cbConsulta.addItem("Cód Equipe");
		cbConsulta.addItem("Cód. Empreiteiro");
		cbConsulta.setSelectedItem("Data");

		ArrayList<Object> dados = new ArrayList<Object>();

		String[] colunas = new String[] {"Código", "Data", "Equipe", "Empreiteiro", "Valor Total Equipe", "Qntd. Bins", "Histórico", "Situação"};

		boolean[] edicao = {false, false, false, false, false, false, false};

		modelo = new ModeloTabela(dados, colunas, edicao);
		tabela = new JTable(modelo);
		tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
		tabela.getColumnModel().getColumn(0).setResizable(false);
		tabela.getColumnModel().getColumn(1).setPreferredWidth(90);
		tabela.getColumnModel().getColumn(1).setResizable(true);
		tabela.getColumnModel().getColumn(2).setPreferredWidth(180);
		tabela.getColumnModel().getColumn(2).setResizable(true);
		tabela.getColumnModel().getColumn(3).setPreferredWidth(220);
		tabela.getColumnModel().getColumn(3).setResizable(true);
		tabela.getColumnModel().getColumn(4).setPreferredWidth(140);
		tabela.getColumnModel().getColumn(4).setResizable(true);
		tabela.getColumnModel().getColumn(4).setCellRenderer(NumberRenderer.getCurrencyRenderer());
		tabela.getColumnModel().getColumn(5).setPreferredWidth(64);
		tabela.getColumnModel().getColumn(5).setResizable(true);
		tabela.getColumnModel().getColumn(5).setCellRenderer(NumberRenderer.getIntegerRenderer());
		tabela.getColumnModel().getColumn(6).setPreferredWidth(290);
		tabela.getColumnModel().getColumn(6).setResizable(true);
		tabela.getColumnModel().getColumn(7).setPreferredWidth(75);
		tabela.getColumnModel().getColumn(7).setResizable(true);
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
		super.btnIncluir.setText("Lançar Dia (F1)");
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
	}

	@Override
	public void consultar() {
		ArrayList <LancaDiaBO> diaBO = null;

		// apaga todas as linhas da tabela
		for (int i = modelo.getRowCount() - 1; i >= 0; i--)
			modelo.removeRow(i);

		if (super.cbConsulta.getSelectedItem().equals("Código")) {
			diaBO = diaDao.consultaPorCodigo(Integer.parseInt(super.txtDadoConsulta.getText()));
		} else if (super.cbConsulta.getSelectedItem().equals("Nome Equipe")) {
			diaBO = diaDao.consultaPorNomeEquipe(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("Nome Empreiteiro")) {
			diaBO = diaDao.consultaPorNomeEmpreiteiro(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("Data")) {
			diaBO = diaDao.consultaPorData(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("Valor")) {
			diaBO = diaDao.consultaPorValor(Double.parseDouble(super.txtDadoConsulta.getText()));
		} else if (super.cbConsulta.getSelectedItem().equals("Cód. Equipe")) {
			try {
				diaBO = diaDao.consultaPorCodEquipe(Integer.parseInt(super.txtDadoConsulta.getText()));
			} catch(NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "O c�digo deve ser num�rico", "Erro",JOptionPane.ERROR_MESSAGE);
				super.txtDadoConsulta.selectAll();
				super.txtDadoConsulta.requestFocus();
				return;
			}
		} else if (super.cbConsulta.getSelectedItem().equals("C�d. Empreiteiro")) {
			try {
				diaBO = diaDao.consultaPorCodEmpreiteiro(Integer.parseInt(super.txtDadoConsulta.getText()));
			} catch(NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "O c�digo deve ser num�rico", "Erro",JOptionPane.ERROR_MESSAGE);
				super.txtDadoConsulta.selectAll();
				super.txtDadoConsulta.requestFocus();
				return;
			}
		}

		int indice = 0;
		try {
			do {
				modelo.addRow(new Object[] {
						new Integer(diaBO.get(indice).getCodigo()),
						df.format(diaBO.get(indice).data.toDate()),
						diaBO.get(indice).equipeBO.getNome(),
						diaBO.get(indice).iroBO.getNome(),
						diaBO.get(indice).getValorTotal(),
						diaBO.get(indice).getQntBinsEquipe(),
						diaBO.get(indice).observacao.getText(),
						(diaBO.get(indice).pgtoBO.getCodigo() == 0?"Aberto":"Pago")
				});
				indice++;
			} while (indice < diaBO.size());
		} catch (NullPointerException e) {}
		
	}

	@Override
	public void incluir() {
		FrmDiaTrabalho fr = new FrmDiaTrabalho();
		fr.setVisible(true);
		getDesktopPane().add(fr); 
		try {
			fr.setSelected(true);
		}
		catch (PropertyVetoException exc) {}
		FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
	}

	@Override
	public void alterar() {
		if (tabela.getSelectedRow()>=0) {
			diaBO = new LancaDiaBO();
			diaAdoBO = new ArrayList<DiaEmpregadoBO>();
			diaBO = diaDao.consultaPorCodigo(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(), 0).toString())).get(0);
			diaAdoBO = diaAdoDao.consultaPorCodigoDia(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(), 0).toString()));
			FrmDiaTrabalho fr = new FrmDiaTrabalho(this);
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
		if (modelo.getValueAt(tabela.getSelectedRow(),7).toString() == "Pago") {
			JOptionPane.showMessageDialog(this, "Impossível excluir o registro, o mesmo já possui pagamento!", "Excluir Registro",JOptionPane.ERROR_MESSAGE);
			return;
		} else {
			if (diaAdoDao.excluir(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString())) == true) {
				if (diaDao.excluir(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString())) == true) {
					modelo.removeRow(tabela.getSelectedRow());
				}
			}
		}
	}
}