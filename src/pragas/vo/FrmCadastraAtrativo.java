package pragas.vo;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import exceptions.StringVaziaException;
import gerais.vo.FrmCadastraPai;
import pragas.bo.AtrativoBO;
import pragas.dao.AtrativoDao;

public class FrmCadastraAtrativo extends FrmCadastraPai{
	private static final long serialVersionUID = 6892773482368052539L;
	
	private JLabel lblCodigo, lblDescricao, lblDiasTroca;
	private JTextField txtCodigo, txtDescricao;
	private JSpinner spDiasTroca;
	
	FrmConsultaAtrativo consAtrativo = null;
	
	public FrmCadastraAtrativo(FrmConsultaAtrativo consAtrativo) {
		this();
		this.consAtrativo = consAtrativo;
		
		txtCodigo.setText(String.valueOf(consAtrativo.atratBO.getCodigo()));
		txtDescricao.setText(consAtrativo.atratBO.getDescricao());
		spDiasTroca.setValue(consAtrativo.atratBO.getDiasTroca());
		
		btnConfirmar.setText("Alterar");
	}

	public FrmCadastraAtrativo() {

		setTitle("Cadastro de Atrativos");
		setSize(540, 260);
		lblTitulo.setText(super.lblTitulo.getText() + " de Atrativos");
		lblImg.setIcon(new ImageIcon(getClass().getResource("/icons/icon_atrativo.gif")));

		lblCodigo = new JLabel("Código");
		lblCodigo.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblCodigo, constraints);

		txtCodigo = new JTextField(5);
		txtCodigo.setFont(f2);
		txtCodigo.setEditable(false);
		txtCodigo.setFocusable(false);
		txtCodigo.setText("-");
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtCodigo, constraints);

		lblDescricao = new JLabel("Descrição");
		lblDescricao.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblDescricao, constraints);

		txtDescricao = new JTextField(30);
		txtDescricao.setFont(f2);
		txtDescricao.requestFocusInWindow();
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtDescricao, constraints);
		
		lblDiasTroca = new JLabel("Dias p/ troca");
		lblDiasTroca.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblDiasTroca, constraints);
		
		spDiasTroca = new JSpinner();
		spDiasTroca.setFont(f2);
		spDiasTroca.setPreferredSize(new Dimension(50, 22));
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(spDiasTroca, constraints);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object origem = e.getSource();
		AtrativoBO atratBO = new AtrativoBO();
		AtrativoDao atratDao = new AtrativoDao();

		if (origem == btnConfirmar){
			if (txtCodigo.getText().equals("-"))
				atratBO.setCodigo(0);
			else
				atratBO.setCodigo(Integer.parseInt(txtCodigo.getText()));

			try {
				atratBO.setDescricao(txtDescricao.getText());
			} catch (StringVaziaException erro) {
				JOptionPane.showMessageDialog(this, "Descrição incorreto", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtDescricao.requestFocus();
				return;
			}

			atratBO.setDiasTroca(Integer.parseInt(spDiasTroca.getValue().toString()));

			if (consAtrativo == null){  // veio da inclus�o
				atratDao.incluir(atratBO);
				JOptionPane.showMessageDialog(this, "Registro salvo com sucesso!", "Registro Salvo", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
			}else{
				atratDao.alterar(atratBO);
				int linha = consAtrativo.tabela.getSelectedRow();
				consAtrativo.modelo.setValueAt(atratBO.getDescricao(),linha,1);
				consAtrativo.modelo.setValueAt(atratBO.getDiasTroca(),linha,2);
				JOptionPane.showMessageDialog(this, "Registro alterado com sucesso!", "Registro Alterado", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
			}
			doDefaultCloseAction();
		}

	}

}
