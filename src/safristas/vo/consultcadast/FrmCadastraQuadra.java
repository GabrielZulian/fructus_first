package safristas.vo.consultcadast;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import exceptions.CodigoErradoException;
import exceptions.QuantidadeErradaException;
import gerais.bo.QuadraBO;
import gerais.dao.QuadraDao;
import gerais.vo.FrmCadastraPai;
import safristas.bo.QuadVariedBO;
import safristas.dao.QuadVariDao;
import safristas.vo.FrmMenuGeralMaca;
import util.ModeloTabela;
import util.NumberRenderer;

public class FrmCadastraQuadra extends FrmCadastraPai {
	private static final long serialVersionUID = 6892773482368052539L;

	private JLabel lblNumero, lblAnoPlantio, lblNroPlantas, lblAreaTotal;
	private JTextField txtNumero, txtAnoPlantio, txtNroPlantas, txtAreaTotal;
	private JTable tabela;
	public ModeloTabela modelo;
	private JButton btnAddVariedade, btnExcluiVariedade;
	private DecimalFormat decimal = new DecimalFormat( "#,##0.00" );

	FrmConsultaQuadra consQuadra= null;
	

	public FrmCadastraQuadra(FrmConsultaQuadra consQuadra) {
		this();
		this.consQuadra = consQuadra;
		txtNumero.setText(String.valueOf(consQuadra.quadBO.getNumero()));
		txtAnoPlantio.setText(String.valueOf(consQuadra.quadBO.getAnoPlantio()));
		txtNroPlantas.setText(String.valueOf(consQuadra.quadBO.getNroPlantasHectare()));
		txtAreaTotal.setText(decimal.format(consQuadra.quadBO.getArea()));
		
		QuadVariDao quadVariDao = new QuadVariDao();
		ArrayList<QuadVariedBO> quadVariBO = new ArrayList<QuadVariedBO>();
		
		quadVariBO = quadVariDao.consultaPorNroQuadra(consQuadra.quadBO.getNumero());
		
		int aux = 0;
		
		do {
			modelo.addRow(new Object[] {
					quadVariBO.get(aux).getCodigo(),
					quadVariBO.get(aux).variBO.getDescricao(),
					quadVariBO.get(aux).getArea()
			});
			
			aux++;
		} while (aux < quadVariBO.size());
		
		btnConfirmar.setText("Alterar");
	}

	public FrmCadastraQuadra() {

		setTitle("Cadastro de Quadras");
		setSize(520, 380);
		setResizable(true);
		lblTitulo.setText(super.lblTitulo.getText() + " de Quadras");
		lblImg.setIcon(new ImageIcon(getClass().getResource("/icons/icon_quadra.gif")));

		lblNumero = new JLabel("Número");
		lblNumero.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblNumero, constraints);

		txtNumero = new JTextField(5);
		txtNumero.setFont(f2);
		txtNumero.requestFocusInWindow();
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtNumero, constraints);

		lblAnoPlantio = new JLabel("Ano Plantio");
		lblAnoPlantio.setFont(f2);
		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblAnoPlantio, constraints);

		txtAnoPlantio = new JTextField(6);
		txtAnoPlantio.setFont(f2);
		constraints.gridx = 3;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtAnoPlantio, constraints);

		lblNroPlantas = new JLabel("Número Plantas p/ ha");
		lblNroPlantas.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblNroPlantas, constraints);

		txtNroPlantas = new JTextField(5);
		txtNroPlantas.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtNroPlantas, constraints);

		lblAreaTotal = new JLabel("Área Total (ha)");
		lblAreaTotal.setFont(f2);
		constraints.gridx = 2;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblAreaTotal, constraints);

		txtAreaTotal = new JTextField(6);
		txtAreaTotal.setFont(f2);
		constraints.gridx = 3;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtAreaTotal, constraints);
		constraints.gridwidth = 1;

		ArrayList<Object> dados = new ArrayList<Object>();

		String[] colunas = new String[] {"Cód.", "Variedade", "Área(ha)"};

		boolean[] edicao = {false, false, false};

		modelo = new ModeloTabela(dados, colunas, edicao);
		tabela = new JTable(modelo);
		tabela.getColumnModel().getColumn(0).setPreferredWidth(80);
		tabela.getColumnModel().getColumn(0).setResizable(true);
		tabela.getColumnModel().getColumn(0).setCellRenderer(NumberRenderer.getIntegerRenderer());
		tabela.getColumnModel().getColumn(1).setPreferredWidth(200);
		tabela.getColumnModel().getColumn(1).setResizable(true);
		tabela.getColumnModel().getColumn(2).setPreferredWidth(103);
		tabela.getColumnModel().getColumn(2).setResizable(true);
		tabela.getTableHeader().setReorderingAllowed(false);
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 4;
		constraints.gridheight = 2;
		constraints.anchor = GridBagConstraints.WEST;
		JScrollPane rolagemTabela = new JScrollPane(tabela);
		rolagemTabela.setPreferredSize(new Dimension(390, 100));
		painelMeio.add(rolagemTabela, constraints);		
		constraints.gridheight = 1;
		constraints.gridwidth = 1;

		btnAddVariedade = new JButton(new ImageIcon(getClass().getResource("/icons/icon_addp.gif")));
		btnAddVariedade.setToolTipText("Adicionar variedade");
		constraints.gridx = 4;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(btnAddVariedade, constraints);

		btnExcluiVariedade = new JButton(new ImageIcon(getClass().getResource("/icons/icon_delp.gif")));
		btnExcluiVariedade.setToolTipText("Excluir variedade");
		constraints.gridx = 4;
		constraints.gridy = 5;
		constraints.anchor = GridBagConstraints.NORTH;
		painelMeio.add(btnExcluiVariedade, constraints);

		btnAddVariedade.addActionListener(this);
		btnExcluiVariedade.addActionListener(this);
	}

	public void addVariedadeTabela (int cod, String variedade, BigDecimal area) {

		modelo.addRow(new Object[] {
				cod,
				variedade,
				area
		});
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object origem = e.getSource();
		QuadraBO quadraBO = new QuadraBO();
		QuadraDao quadraDao = new QuadraDao();
		QuadVariDao quadVariDao = new QuadVariDao();
		QuadVariedBO quadVariBO = new QuadVariedBO();

		if (origem == btnConfirmar){

			if (tabela.getRowCount() <= 0) {
				JOptionPane.showMessageDialog(this, "Nenhuma Variedade informada!", "Erro ao confirmar registro", JOptionPane.ERROR_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_cancelar.gif")));
				return;
			}

			if (somaArea().compareTo(new BigDecimal(txtAreaTotal.getText().replace(',', '.'))) != 0) {
				JOptionPane.showMessageDialog(this, "Soma das áreas não fecham com a área total informada!", "Erro ao confirmar registro", JOptionPane.ERROR_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_cancelar.gif")));
				txtAreaTotal.requestFocus();
				txtAreaTotal.selectAll();
				return;
			}

			try {
				quadraBO.setNumero(Integer.parseInt(txtNumero.getText()));
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "Número quadra incorreto!", "Erro ao confirmar registro", JOptionPane.ERROR_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_cancelar.gif")));
				txtAreaTotal.requestFocus();
				txtAreaTotal.selectAll();
				return;
			}

			try {
				quadraBO.setArea(new BigDecimal(txtAreaTotal.getText().replace(",", ".")));
			} catch (QuantidadeErradaException e1) {
				JOptionPane.showMessageDialog(this, "Area incorreta!", "Erro ao confirmar registro", JOptionPane.ERROR_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_cancelar.gif")));
				txtAreaTotal.requestFocus();
				txtAreaTotal.selectAll();
				return;
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "Area incorreta!", "Erro ao confirmar registro", JOptionPane.ERROR_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_cancelar.gif")));
				txtAreaTotal.requestFocus();
				txtAreaTotal.selectAll();
				return;
			}

			try {
				quadraBO.setAnoPlantio(Integer.parseInt(txtAnoPlantio.getText()));
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "Ano plantio incorreto!", "Erro ao confirmar registro", JOptionPane.ERROR_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_cancelar.gif")));
				txtAnoPlantio.requestFocus();
				txtAnoPlantio.selectAll();
				return;
			}

			try {
				quadraBO.setNroPlantasHectare((Integer.parseInt(txtNroPlantas.getText())));
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "Número de plantas incorreto!", "Erro ao confirmar registro", JOptionPane.ERROR_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_cancelar.gif")));
				txtNroPlantas.requestFocus();
				txtNroPlantas.selectAll();
				return;
			} catch (QuantidadeErradaException e1) {
				JOptionPane.showMessageDialog(this, "Número de plantas incorreto!", "Erro ao confirmar registro", JOptionPane.ERROR_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_cancelar.gif")));
				txtNroPlantas.requestFocus();
				txtNroPlantas.selectAll();
				return;
			}

			if (consQuadra == null) {
				quadraDao.incluir(quadraBO);

				int x = 0;

				do {

					try {
						quadVariBO.variBO.setCodigo(Integer.parseInt(modelo.getValueAt(x,0).toString()));
						quadVariBO.setArea(new BigDecimal(modelo.getValueAt(x, 2).toString()));
						quadVariDao.incluir(quadVariBO);
					} catch (NumberFormatException e1) {
					} catch (CodigoErradoException e1) {
					} catch (QuantidadeErradaException e1) {
					}
					x++;

				} while (x<modelo.getRowCount());

				JOptionPane.showMessageDialog(this, "Registro salvo com sucesso!", "Registro Salvo", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
			} else {
				quadraDao.alterar(quadraBO);
				int linha = consQuadra.tabela.getSelectedRow();
				consQuadra.modelo.setValueAt(quadraBO.getNumero(),linha,1);
				consQuadra.modelo.setValueAt(quadraBO.getArea(),linha,2);
				JOptionPane.showMessageDialog(this, "Registro alterado com sucesso!", "Registro Alterado", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
			}

			doDefaultCloseAction();
		} else if (origem == btnAddVariedade) {
			FrmVariedadeQuadra fr = new FrmVariedadeQuadra(this);
			fr.setVisible(true);
			getDesktopPane().add(fr);   
			try {
				fr.setSelected(true);
			}
			catch (PropertyVetoException exc) { }
			FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
		} else if (origem == btnExcluiVariedade) {
			if (tabela.getSelectedRow() >= 0) {
				modelo.removeRow(tabela.getSelectedRow());
			} else {
				JOptionPane.showMessageDialog(this, "Selecione uma variedade primeiro!", "Excluir Variedade", JOptionPane.ERROR_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_cancelar.gif")));
			}
		}
	}

	private BigDecimal somaArea() {
		BigDecimal soma = new BigDecimal("0");
		int aux = 0;

		do {
			soma = soma.add(new BigDecimal(modelo.getValueAt(aux, 2).toString()));
			aux++;
		} while (aux < modelo.getRowCount());

		return soma;
	}
}