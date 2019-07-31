package safristas.vo.consultcadast;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import exceptions.StringVaziaException;
import gerais.vo.FrmCadastraPai;
import safristas.bo.ProvaBO;
import safristas.dao.FuncaoDao;

public class FrmCadastraFuncao  extends FrmCadastraPai {
	private static final long serialVersionUID = -8236692021765873894L;
	
	private JLabel lblCodigo, lblNome;
	private JTextField txtCodigo, txtNome;
	
	FrmConsultaFuncao consFuncao= null;
	
	public FrmCadastraFuncao(FrmConsultaFuncao consFuncao) {
		this();
		this.consFuncao = consFuncao;
		txtCodigo.setText(String.valueOf(consFuncao.funBO.getCodigo()));
		txtNome.setText(consFuncao.funBO.getNome());
		btnConfirmar.setText("Alterar (F1)");
	}

	public FrmCadastraFuncao() {
		
		setTitle("Cadastro de Funções");
		setSize(570, 220);
		lblTitulo.setText(super.lblTitulo.getText() + " de Funções");
		lblImg.setIcon(new ImageIcon(getClass().getResource("/icons/icon_funcao.gif")));
		
		lblCodigo = new JLabel("Codigo");
		lblCodigo.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblCodigo, constraints);

		txtCodigo = new JTextField(5);
		txtCodigo.setFont(f2);
		txtCodigo.setEditable(false);
		txtCodigo.setText("-");
		txtCodigo.setFocusable(false);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtCodigo, constraints);

		lblNome = new JLabel("Descrição");
		lblNome.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblNome, constraints);

		txtNome = new JTextField(30);
		txtNome.setFont(f2);
		txtNome.requestFocusInWindow();
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtNome, constraints);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object origem = e.getSource();
		ProvaBO funBO = new ProvaBO();
		FuncaoDao funDao = new FuncaoDao();

		if (origem == btnConfirmar){
			if (txtCodigo.getText().equals("-"))
				funBO.setCodigo(0);
			else
				funBO.setCodigo(Integer.parseInt(txtCodigo.getText()));

			try {
				funBO.setNome(txtNome.getText());
			} catch (StringVaziaException erro) {
				JOptionPane.showMessageDialog(this, "Descrição incorreta!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtNome.requestFocus();
				return;
			}

			if (consFuncao == null){  // veio da inclus�o
				funDao.incluir(funBO);
				JOptionPane.showMessageDialog(this, "Registro salvo com sucesso!", "Registro Salvo", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
			}else{
				funDao.alterar(funBO);
				int linha = consFuncao.tabela.getSelectedRow();
				consFuncao.modelo.setValueAt(funBO.getNome(),linha,1);
				JOptionPane.showMessageDialog(this, "Registro alterado com sucesso!", "Registro Alterado", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
			}
			doDefaultCloseAction();
		}

	}
	
}
