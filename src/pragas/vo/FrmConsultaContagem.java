package pragas.vo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.joda.time.DateTime;

import gerais.vo.FrmConsultaPai;
import util.ModeloTabela;
import pragas.bo.ContagemBO;
import pragas.dao.ContagemDao;

public class FrmConsultaContagem extends FrmConsultaPai {
	private static final long serialVersionUID = -2708643108135526552L;
	public JTable tabela;
	public ModeloTabela modelo;
	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	public ArrayList<ContagemBO> contBO;
	private ContagemDao contDao = new ContagemDao();
	private JRadioButton rbtnCydia, rbtnGrapholita, rbtnBonagota, rbtnMosca;
	private ButtonGroup grupo;
	
	private final int COL_CODCONTAGEM = 0;
	private final int COL_CODQUADRA = 1;
	private final int COL_DATA = 2;
	private final int COL_NROQUADRA = 3;
	private final int COL_QNTDINSETOS = 4;
	private final int COL_ESPECIE = 5;
	private final int COL_INDICE = 6;
	private final int COL_QNTDFRASCOS = 7;

	public FrmConsultaContagem() {

		setTitle("Consulta dias contagem");
		setSize(getWidth()-50, getHeight());

		cbConsulta.addItem("Data");
		cbConsulta.addItem("Espécie");
		cbConsulta.addItem("Qntd. Insetos");
		cbConsulta.addItem("Nro. Quadra");
		cbConsulta.setSelectedItem("Data");

		txtDadoConsulta.setColumns(13);

		ArrayList<Object> dados = new ArrayList<Object>();

		String[] colunas = new String[] {"Código Contagem", "Cod. Quadra", "Data", "Nro Quadra", "Qntd. Insetos", "Esp�cie", "Índice", "Qntd. Frascos"};

		boolean[] edicao = {false, false, false, false, false, false, false, false, false};

		modelo = new ModeloTabela(dados, colunas, edicao);
		tabela = new JTable(modelo);
		tabela.getColumnModel().getColumn(COL_CODCONTAGEM).setPreferredWidth(110);
		tabela.getColumnModel().getColumn(COL_CODCONTAGEM).setResizable(false);
		tabela.getColumnModel().getColumn(COL_DATA).setPreferredWidth(90);
		tabela.getColumnModel().getColumn(COL_DATA).setResizable(true);
		tabela.getColumnModel().getColumn(COL_NROQUADRA).setPreferredWidth(80);
		tabela.getColumnModel().getColumn(COL_NROQUADRA).setResizable(true);
		tabela.getColumnModel().getColumn(COL_QNTDINSETOS).setPreferredWidth(90);
		tabela.getColumnModel().getColumn(COL_QNTDINSETOS).setResizable(true);
		tabela.getColumnModel().getColumn(COL_ESPECIE).setPreferredWidth(116);
		tabela.getColumnModel().getColumn(COL_ESPECIE).setResizable(true);
		tabela.getColumnModel().getColumn(COL_INDICE).setPreferredWidth(90);
		tabela.getColumnModel().getColumn(COL_INDICE).setResizable(true);
		tabela.getColumnModel().getColumn(COL_QNTDFRASCOS).setPreferredWidth(86);
		tabela.getColumnModel().getColumn(COL_QNTDFRASCOS).setResizable(true);
		tabela.getTableHeader().setReorderingAllowed(true); 
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane rolagemTabela = new JScrollPane(tabela);
		painelGeral.add(rolagemTabela, BorderLayout.CENTER);

		tabela.setFocusable(false);
		super.btnIncluir.setText("Lançar (F1)");
		super.btnAlterar.setEnabled(false);
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		contBO = null;
		preencheTabela(contBO);
	}

	@Override
	public void excluir() {
		DateTime dataRegistro = new DateTime();
		DateTime ultimaData = new DateTime();

		try {
			dataRegistro = new DateTime(df.parse(modelo.getValueAt(tabela.getSelectedRow(), COL_DATA).toString()));
		} catch (ParseException e) {}
		ultimaData = contDao.getDataUltimaContagem(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(), COL_CODQUADRA).toString()));

		if (dataRegistro.isBefore(ultimaData)) {
			JOptionPane.showMessageDialog(null, "Data deste registro menor que a ultima data de contagem!", "Erro exclusão", JOptionPane.ERROR_MESSAGE);
			return;
		} else if (contDao.excluir(Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(),0).toString())) == true) {
			modelo.removeRow(tabela.getSelectedRow());
		}
	}

	@Override
	public void consultar() {
		contBO = null;

		// apaga todas as linhas da tabela
		for (int i = modelo.getRowCount() - 1; i >= 0; i--)
			modelo.removeRow(i);

		if (super.cbConsulta.getSelectedItem().equals("Data")) {
				contBO = contDao.consultaPorData(super.txtDadoConsulta.getText());
		} else if (super.cbConsulta.getSelectedItem().equals("Qntd. Insetos")) {
			try {
				contBO = contDao.consultaPorQntdInsetos(Integer.parseInt(super.txtDadoConsulta.getText()));
			} catch (NumberFormatException erro) {
				JOptionPane.showMessageDialog(null, "Quantidade deve ser num�rica!", "Erro consulta", JOptionPane.ERROR_MESSAGE);
			}
		} else if (super.cbConsulta.getSelectedItem().equals("Nro. Quadra")) {
			try {
				contBO = contDao.consultaPorNroQuadra(Integer.parseInt(super.txtDadoConsulta.getText()));
			} catch (NumberFormatException erro) {
				JOptionPane.showMessageDialog(null, "Número Quadra deve ser numérico!", "Erro consulta", JOptionPane.ERROR_MESSAGE);
			}
		} else if (super.cbConsulta.getSelectedItem().equals("Espécie")) {
			contBO = contDao.consultaPorEspecie(super.txtDadoConsulta.getText().charAt(0));
//				if (rbtnCydia.isSelected()) {
//					contBO = contDao.consultaPorEspecie('C');
//				} else if (rbtnGrapholita.isSelected()) {
//					contBO = contDao.consultaPorEspecie('G');
//				} else if (rbtnBonagota.isSelected()) {
//					contBO = contDao.consultaPorEspecie('B');
//				} else if (rbtnMosca.isSelected()) {
//					contBO = contDao.consultaPorEspecie('M');
//			}
		}
		preencheTabela(contBO);
	}
	
	private void preencheTabela(ArrayList<ContagemBO> contBO) {

		int indice = 0;
		try {
			do {
				modelo.addRow(new Object[] {
						new Integer(contBO.get(indice).getCodigo()),
						contBO.get(indice).frasco.getCodigo(),
						df.format(contBO.get(indice).data.toDate()),
						contBO.get(indice).frasco.quadra.getNumero(),
						contBO.get(indice).getQntdInsetos(),
						contBO.get(indice).frasco.getTipoPragaString(),
						contBO.get(indice).getIndiceFinal(),
						contBO.get(indice).frasco.getQntdFrascos()
				});
				indice++;
			} while (indice < contBO.size());
		} catch (NullPointerException e) {}
	}

	@Override
	public void incluir() {
		FrmLancaContagem fr = new FrmLancaContagem();
		fr.setVisible(true);
		getDesktopPane().add(fr); 
		try {
			fr.setSelected(true);
		}
		catch (PropertyVetoException exc) { }
		FrmMenuGeralPragas.centralizaInternalFrame(fr, getDesktopPane());
	}

	@Override
	public void alterar() {
		// TODO Auto-generated method stub
		
	}
}
