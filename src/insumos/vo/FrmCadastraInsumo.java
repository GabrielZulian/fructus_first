package insumos.vo;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import exceptions.StringVaziaException;
import gerais.vo.FrmCadastraPai;
import insumos.bo.InsumoBO;
import insumos.dao.InsumoDao;

public class FrmCadastraInsumo extends FrmCadastraPai{
	private static final long serialVersionUID = 7491052336081296067L;
	
	private JLabel lblCodigo, lblDescricao, lblUnidade, lblDiasResidual;
	private JTextField txtCodigo, txtDescricao;
	private String[] unidades = {"Kg", "L"};
	private JComboBox<String> cbUnidade;; 
	private JSpinner spDiasResidual;
	
	FrmConsultaInsumo consInsumo = null;
	
	public FrmCadastraInsumo(FrmConsultaInsumo consInsumo) {
		this();
		this.consInsumo = consInsumo;
		
		txtCodigo.setText(String.valueOf(consInsumo.insumoBO.getCodigo()));
		txtDescricao.setText(consInsumo.insumoBO.getDescricao());
		cbUnidade.setSelectedItem(consInsumo.insumoBO.getUnidade());
		spDiasResidual.setValue(consInsumo.insumoBO.getDiasResidual());
		
		btnConfirmar.setText("Alterar");
	}

	public FrmCadastraInsumo() {

		setTitle("Cadastro de Insumos");
		setSize(540, 260);
		lblTitulo.setText(super.lblTitulo.getText() + " de Insumos");
		lblImg.setIcon(new ImageIcon(getClass().getResource("/icons/icon_insumo.gif")));
		
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
		
		lblUnidade = new JLabel("Unidade");
		lblUnidade.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblUnidade, constraints);
		
		cbUnidade = new JComboBox<String>(unidades);
		cbUnidade.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(cbUnidade, constraints);
		
		lblDiasResidual = new JLabel("Residual (dias)");
		lblDiasResidual.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblDiasResidual, constraints);
		
		spDiasResidual = new JSpinner();
		spDiasResidual.setFont(f2);
		spDiasResidual.setModel(new SpinnerNumberModel(0, 0, 999, 1));
		spDiasResidual.setPreferredSize(new Dimension(50, 22));
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(spDiasResidual, constraints);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object origem = e.getSource();
		InsumoBO insumoBO = new InsumoBO();
		InsumoDao insumoDao = new InsumoDao();

		if (origem == btnConfirmar){
			
			if (txtCodigo.getText().equals("-"))
				insumoBO.setCodigo(0);
			else
				insumoBO.setCodigo(Integer.parseInt(txtCodigo.getText()));

			try {
				insumoBO.setDescricao(txtDescricao.getText());
			} catch (StringVaziaException erro) {
				JOptionPane.showMessageDialog(this, "Descrição incorreta", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtDescricao.requestFocus();
				return;
			}
			
			insumoBO.setUnidade(cbUnidade.getSelectedItem().toString());

			insumoBO.setDiasResidual(Integer.parseInt(spDiasResidual.getValue().toString()));

			if (consInsumo == null){  // veio da inclus�o
			insumoDao.incluir(insumoBO);
				JOptionPane.showMessageDialog(this, "Registro salvo com sucesso!", "Registro Salvo", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
			} else {
				insumoDao.alterar(insumoBO);
				int linha = consInsumo.tabela.getSelectedRow();
				consInsumo.modelo.setValueAt(insumoBO.getDescricao(),linha,1);
				consInsumo.modelo.setValueAt(insumoBO.getDiasResidual(),linha,2);
				consInsumo.modelo.setValueAt(insumoBO.getUnidade(),linha,3);
				JOptionPane.showMessageDialog(this, "Registro alterado com sucesso!", "Registro Alterado", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
			}
			doDefaultCloseAction();
		}

	}

}
