package safristas.vo.consultcadast;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import exceptions.StringVaziaException;
import gerais.vo.FrmCadastraPai;
import safristas.bo.CidadeBO;
import safristas.dao.CidadeDao;

public class FrmCadastraCidade extends FrmCadastraPai{
	private static final long serialVersionUID = 6892773482368052539L;
	
	private JLabel lblCodigo, lblNome, lblUF;
	private JTextField txtCodigo, txtNome;
	private JComboBox cbUF;
	private String[] opCbUF = {"AC","AL","AP","AM","BA","CE","DF","ES","GO","MA","MT","MS","MG","PA","PB","PR",
			"PE","PI","RJ","RN","RS","RO","RR","SC","SP","SE","TO"};

	FrmConsultaCidade consCidade= null;

	public FrmCadastraCidade(FrmConsultaCidade consCidade) {
		this();
		this.consCidade = consCidade;
		txtCodigo.setText(String.valueOf(consCidade.cidBO.getCodigo()));
		txtNome.setText(consCidade.cidBO.getNome());
		cbUF.setSelectedItem(consCidade.cidBO.getUf());
		btnConfirmar.setText("Alterar (F1)");
	}

	public FrmCadastraCidade() {

		setTitle("Cadastro de Cidades");
		setSize(540, 260);
		lblTitulo.setText(super.lblTitulo.getText() + " de Cidades");
		lblImg.setIcon(new ImageIcon(getClass().getResource("/icons/icon_cidade.gif")));

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

		lblNome = new JLabel("Nome");
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

		lblUF = new JLabel("UF");
		lblUF.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblUF, constraints);

		cbUF = new JComboBox(opCbUF);
		cbUF.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(cbUF, constraints);
		
		cbUF.setSelectedItem("RS");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object origem = e.getSource();
		CidadeBO cidade = new CidadeBO();
		CidadeDao cidDao = new CidadeDao();

		if (origem == btnConfirmar){
			if (txtCodigo.getText().equals("-"))
				cidade.setCodigo(0);
			else
				cidade.setCodigo(Integer.parseInt(txtCodigo.getText()));

			try {
				cidade.setNome(txtNome.getText());
			} catch (StringVaziaException erro) {
				JOptionPane.showMessageDialog(this, "Nome incorreto", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtNome.requestFocus();
				return;
			}

			cidade.setUf(cbUF.getSelectedItem().toString());

			if (consCidade == null){  // veio da inclusão
				cidDao.incluir(cidade);
				JOptionPane.showMessageDialog(this, "Registro salvo com sucesso!", "Registro Salvo", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
			}else{
				cidDao.alterar(cidade);
				int linha = consCidade.tabela.getSelectedRow();
				consCidade.modelo.setValueAt(cidade.getNome(),linha,1);
				consCidade.modelo.setValueAt(cidade.getUf(),linha,2);
				JOptionPane.showMessageDialog(this, "Registro alterado com sucesso!", "Registro Alterado", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
			}
			doDefaultCloseAction();
		}

	}

}
