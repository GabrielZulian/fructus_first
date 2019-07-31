package insumos.vo;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import exceptions.CodigoErradoException;
import gerais.vo.FrmCadastraPai;
import insumos.bo.TalhaoBO;
import insumos.dao.TalhaoDao;

public class FrmCadastraTalhao extends FrmCadastraPai{
	private static final long serialVersionUID = 7491052336081296067L;

	private JLabel lblCodigo, lblTipo, lblNumero, lblArea;
	private JTextField txtCodigo, txtNumero, txtArea;
	private JComboBox<String> cbTipo;
	private String[] tipos = {"Lavoura", "Pomar"};

	FrmConsultaTalhao consTalhao = null;

	public FrmCadastraTalhao(FrmConsultaTalhao consTalhao) {
		this();
		this.consTalhao = consTalhao;

		txtCodigo.setText(String.valueOf(consTalhao.talhaoBO.getCodigo()));
		cbTipo.setSelectedItem(consTalhao.talhaoBO.getTipo());
		txtNumero.setText(String.valueOf(consTalhao.talhaoBO.getNumero()));
		txtArea.setText(consTalhao.talhaoBO.getArea().toString());

		btnConfirmar.setText("Alterar");
	}

	public FrmCadastraTalhao() {

		setTitle("Cadastro de Talhões");
		setSize(540, 260);
		lblTitulo.setText(super.lblTitulo.getText() + " de Talhões");
		lblImg.setIcon(new ImageIcon(getClass().getResource("/icons/icon_lavoura.gif")));

		lblCodigo = new JLabel("Código");
		lblCodigo.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblCodigo, constraints);

		txtCodigo = new JTextField(4);
		txtCodigo.setFocusable(false);
		txtCodigo.setEditable(false);
		txtCodigo.setText("-");
		txtCodigo.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtCodigo, constraints);

		lblTipo = new JLabel("Tipo");
		lblTipo.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblTipo, constraints);

		cbTipo = new JComboBox<String>(tipos);
		cbTipo.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(cbTipo, constraints);

		lblNumero = new JLabel("Número");
		lblNumero.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblNumero, constraints);

		txtNumero = new JTextField(7);
		txtNumero.setFont(f2);
		txtNumero.requestFocusInWindow();
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtNumero, constraints);

		lblArea = new JLabel("Área (ha)");
		lblArea.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblArea, constraints);

		txtArea = new JTextField(8);
		txtArea.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtArea, constraints);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object origem = e.getSource();
		TalhaoBO talhaoBO = new TalhaoBO();
		TalhaoDao talhaoDAO = new TalhaoDao();

		if (origem == btnConfirmar) {

			if (txtCodigo.getText().equals("-"))
				talhaoBO.setCodigo(0);
			else
				talhaoBO.setCodigo(Integer.parseInt(txtCodigo.getText()));

			talhaoBO.setTipo(cbTipo.getSelectedItem().toString());

			try {
				talhaoBO.setNumero(Integer.parseInt(txtNumero.getText()));
			} catch (NumberFormatException erro) {
				JOptionPane.showMessageDialog(this, "Número incorreto", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtNumero.requestFocus();
				txtNumero.selectAll();
				return;
			}

			try {
				talhaoBO.setArea(new BigDecimal(txtArea.getText().replace(',', '.')));
			} catch (NumberFormatException erro) {
				JOptionPane.showMessageDialog(this, "Área incorreta", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtArea.requestFocus();
				txtArea.selectAll();
				return;
			}

			if (consTalhao == null) {  // veio da inclus�o
				talhaoDAO.incluir(talhaoBO);
				JOptionPane.showMessageDialog(this, "Registro salvo com sucesso!", "Registro Salvo", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
			} else {
				talhaoDAO.alterar(talhaoBO);
				int linha = consTalhao.tabela.getSelectedRow();
				consTalhao.modelo.setValueAt(talhaoBO.getCodigo(),linha,0);
				consTalhao.modelo.setValueAt(talhaoBO.getTipo(),linha,1);
				consTalhao.modelo.setValueAt(talhaoBO.getNumero(),linha,2);
				consTalhao.modelo.setValueAt(talhaoBO.getArea(),linha,3);

				JOptionPane.showMessageDialog(this, "Registro alterado com sucesso!", "Registro Alterado", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
			}
			doDefaultCloseAction();
		}
	}

}
