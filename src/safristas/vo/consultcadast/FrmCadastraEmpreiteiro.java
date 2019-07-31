package safristas.vo.consultcadast;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyVetoException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import exceptions.CpfInvalidoException;
import exceptions.StringVaziaException;
import gerais.vo.FrmCadastraPai;
import safristas.bo.CidadeBO;
import safristas.bo.safristas.EmpreiteiroBO;
import safristas.dao.CidadeDao;
import safristas.dao.safristas.EmpreiteiroDao;
import safristas.vo.FrmMenuGeralMaca;

public class FrmCadastraEmpreiteiro extends FrmCadastraPai{
	private static final long serialVersionUID = 3102039187941054043L;
	
	private JLabel lblCodigo, lblNome, lblApelido, lblCPF, lblCidade, lblTelefone;
	protected JTextField txtCodigo, txtNome, txtApelido, txtCodCidade, txtMostraCidade, txtTelefone;
	private JFormattedTextField txtCPF;
	private MaskFormatter mascaraCPF;
	private JButton btnProcurar;
	EmpreiteiroDao iroDao = new EmpreiteiroDao();
	FrmConsultaEmpreiteiro consEmpreiteiro = null;

	public FrmCadastraEmpreiteiro (FrmConsultaEmpreiteiro consEmpreiteiro) {
		this();
		this.consEmpreiteiro = consEmpreiteiro;
		txtCodigo.setText(String.valueOf(consEmpreiteiro.iroBO.getCodigo()));
		txtCodigo.setEditable(false);
		txtNome.setText(consEmpreiteiro.iroBO.getNome());
		txtApelido.setText(consEmpreiteiro.iroBO.getApelido());
		txtCPF.setValue(consEmpreiteiro.iroBO.getCpf());
		txtCodCidade.setText(String.valueOf(consEmpreiteiro.iroBO.cidBO.getCodigo()));
		txtMostraCidade.setText(consEmpreiteiro.iroBO.cidBO.getNome() + " - " + consEmpreiteiro.iroBO.cidBO.getUf());
		txtTelefone.setText(consEmpreiteiro.iroBO.getTelefone());

		btnConfirmar.setText("Alterar (F1)");
	}

	public FrmCadastraEmpreiteiro() {

		setTitle("Cadastro de Empreiteiros");
		setSize(getWidth()-150, getHeight()-240);
		lblTitulo.setText(super.lblTitulo.getText() + " de Empreiteiros");
		lblImg.setIcon(new ImageIcon(getClass().getResource("/icons/icon_empreiteiro.gif")));

		lblCodigo = new JLabel("Código");
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

		lblNome = new JLabel("Nome");
		lblNome.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblNome, constraints);

		txtNome = new JTextField(30);
		txtNome.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 3;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtNome, constraints);

		lblApelido = new JLabel("Apelido");
		lblApelido.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblApelido, constraints);

		txtApelido = new JTextField(15);
		txtApelido.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtApelido, constraints);
		constraints.gridwidth = 1;

		lblCPF = new JLabel("CPF");
		lblCPF.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblCPF, constraints);

		try {
			mascaraCPF = new MaskFormatter("###.###.###-##");
		} catch (ParseException e1) {}
		mascaraCPF.setPlaceholderCharacter('_');
		mascaraCPF.setValidCharacters("0123456789");
		mascaraCPF.setValueContainsLiteralCharacters(false);

		txtCPF = new JFormattedTextField(mascaraCPF);
		txtCPF.setColumns(11);
		txtCPF.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtCPF, constraints);
		constraints.gridwidth = 1;

		lblCidade = new JLabel("Cód Cidade");
		lblCidade.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblCidade, constraints);

		txtCodCidade = new JTextField(5);
		txtCodCidade.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtCodCidade, constraints);

		txtCodCidade.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				CidadeDao cidDao = new CidadeDao();
				ArrayList<CidadeBO> cidBo = cidDao.consultaPorCodigo(Integer.parseInt(txtCodCidade.getText()));
				if (cidBo == null){
					txtCodCidade.selectAll();
					txtCodCidade.requestFocus();
					txtMostraCidade.setText("");
				} else
					txtMostraCidade.setText(cidBo.get(0).getNome() + " - " + cidBo.get(0).getUf());
			}
		});

		txtMostraCidade = new JTextField(15);
		txtMostraCidade.setFont(f2);
		txtMostraCidade.setEditable(false);
		txtMostraCidade.setFocusable(false);
		constraints.gridx = 2;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtMostraCidade, constraints);

		btnProcurar = new JButton("Procurar", new ImageIcon(getClass().getResource("/icons/icon_procurar.gif")));
		btnProcurar.setFocusable(false);
		constraints.gridx = 3;
		constraints.gridy = 4;
		constraints.ipady = -5;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(btnProcurar, constraints);
		constraints.ipady = 1;

		lblTelefone = new JLabel("Telefone");
		lblTelefone.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblTelefone, constraints);

		txtTelefone = new JTextField(10);
		txtTelefone.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtTelefone, constraints);
		constraints.gridwidth = 1;

		btnProcurar.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object origem = e.getSource();
		CidadeDao cidDao = new CidadeDao();
		EmpreiteiroBO empreiteiro = new EmpreiteiroBO();

		if (origem == btnProcurar){
			FrmConsultaCidade frame = new FrmConsultaCidade(this);
			frame.setVisible(true);
			getDesktopPane().add(frame);
			try {
				frame.setSelected(true);
			}
			catch (PropertyVetoException exc) { }
			FrmMenuGeralMaca.centralizaInternalFrame(frame, getDesktopPane());
		} else if (origem == btnConfirmar){
			if (txtCodigo.getText().equals("-"))
				empreiteiro.setCodigo(0);
			else
				empreiteiro.setCodigo(Integer.parseInt(txtCodigo.getText()));

			try {
				empreiteiro.setNome(txtNome.getText());
			} catch (StringVaziaException e1) {
				JOptionPane.showMessageDialog(this, "Nome incorreto!", "ERRO", JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				empreiteiro.setApelido(txtApelido.getText());
			} catch (StringVaziaException e1) {
				JOptionPane.showMessageDialog(this, "Apelido incorreto!", "ERRO", JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				empreiteiro.setCpf(txtCPF.getValue().toString());
			} catch (CpfInvalidoException e1) {
				JOptionPane.showMessageDialog(this, "CPF incorreto!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtCPF.selectAll();
				txtCPF.requestFocus();
				return;
			}

			if (!txtCodCidade.getText().trim().equals("")) {
				empreiteiro.cidBO.setCodigo(Integer.parseInt(txtCodCidade.getText()));
			}
			else{
				JOptionPane.showMessageDialog(this, "Codigo cidade deve ser preenchido!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtCodCidade.requestFocus();
				return;
			}

			empreiteiro.cidBO = cidDao.consultaPorCodigo(Integer.parseInt(txtCodCidade.getText())).get(0);

			try {
				empreiteiro.setTelefone(txtTelefone.getText());
			} catch (StringVaziaException e1) {
				JOptionPane.showMessageDialog(this, "Telefone incorreto!", "ERRO", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (consEmpreiteiro == null) { // veio inclus�o
				iroDao.incluir(empreiteiro);
				JOptionPane.showMessageDialog(this, "Registro salvo com sucesso!", "Registro Salvo", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
				txtNome.setText("");
				txtApelido.setText("");
				txtCPF.setText("");
				txtTelefone.setText("");
				txtCodCidade.setText("");
				txtMostraCidade.setText("");
				txtNome.requestFocus();
			} else {
				iroDao.alterar(empreiteiro);
				int linha = consEmpreiteiro.tabela.getSelectedRow();
				consEmpreiteiro.modelo.setValueAt(empreiteiro.getNome(),linha,1);
				consEmpreiteiro.modelo.setValueAt(empreiteiro.getApelido(),linha,2);
				consEmpreiteiro.modelo.setValueAt(empreiteiro.getCpf(),linha,3);
				consEmpreiteiro.modelo.setValueAt(empreiteiro.cidBO.getNome(),linha,4);
				consEmpreiteiro.modelo.setValueAt(empreiteiro.getTelefone(),linha,5);
				JOptionPane.showMessageDialog(this, "Registro alterado com sucesso!", "Registro Alterado", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
				doDefaultCloseAction();
			}
		}
	}
}
