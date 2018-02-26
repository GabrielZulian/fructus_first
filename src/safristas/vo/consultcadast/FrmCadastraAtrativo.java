package safristas.vo.consultcadast;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyVetoException;
import java.text.ParseException;

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
import safristas.bo.EmpregadorBO;
import safristas.dao.CidadeDao;
import safristas.dao.EmpregadorDao;
import safristas.vo.FrmMenuGeralMaca;

public class FrmCadastraAtrativo extends FrmCadastraPai{
	private static final long serialVersionUID = 6321414954080834053L;
	
	private JLabel lblCodigo, lblNome, lblCPF, lblTelefone, lblIE, lblCidade;
	protected JTextField txtCodigo, txtNome, txtTelefone, txtIE, txtCodCidade, txtMostraCidade;
	private JButton btnprocurar;
	protected JFormattedTextField txtCPF;
	private MaskFormatter mascaraCPF;
	EmpregadorDao dorDao = new EmpregadorDao();

	FrmConsultaEmpregador consEmpregador = null;

	public FrmCadastraAtrativo(FrmConsultaEmpregador consEmpregador) {
		this();
		this.consEmpregador = consEmpregador;
		txtCodigo.setText(String.valueOf(consEmpregador.dorBO.getCodigo()));
		txtCodigo.setEditable(false);
		txtNome.setText(consEmpregador.dorBO.getNome());
		txtCPF.setValue(consEmpregador.dorBO.getCpf());
		txtIE.setText(consEmpregador.dorBO.getIe());
		txtCodCidade.setText(String.valueOf(consEmpregador.dorBO.cidBO.getCodigo()));
		txtMostraCidade.setText(consEmpregador.dorBO.cidBO.getNome() + " - " + consEmpregador.dorBO.cidBO.getUf());
		txtTelefone.setText(consEmpregador.dorBO.getTelefone());

		btnConfirmar.setText("Alterar (F1)");
	}

	public FrmCadastraAtrativo() {

		setTitle("Cadastro de Empregadores");
		setSize(getWidth()-150, getHeight()-240);

		lblTitulo.setText(super.lblTitulo.getText() + " de Empregadores");
		lblImg.setIcon(new ImageIcon(getClass().getResource("/icons/icon_empregador.gif")));

		constraints.ipadx = 0;
		constraints.ipady = 0;

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

		lblNome = new JLabel("Nome Completo");
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
		constraints.gridwidth = 1;

		lblCPF = new JLabel("CPF");
		lblCPF.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 2;
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
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtCPF, constraints);
		constraints.gridwidth = 1;

		lblIE = new JLabel("Inscrição Estadual");
		lblIE.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblIE, constraints);

		txtIE = new JTextField(15);
		txtIE.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtIE, constraints);
		constraints.gridwidth = 1;

		lblCidade = new JLabel("Código Cidade");
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
				try {
					CidadeDao cidDao = new CidadeDao();
					CidadeBO cidBO = cidDao.consultaPorCodigo(Integer.parseInt(txtCodCidade.getText())).get(0);
					txtMostraCidade.setText(cidBO.getNome() + " - " + cidBO.getUf());
				}catch (NullPointerException e1) { 
					txtCodCidade.requestFocus();
					txtCodCidade.selectAll();
					txtMostraCidade.setText("");
				}
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

		btnprocurar = new JButton("Procurar", new ImageIcon(getClass().getResource("/icons/icon_procurar.gif")));
		btnprocurar.setFocusable(false);
		constraints.gridx = 3;
		constraints.gridy = 4;
		constraints.ipadx = 0;
		constraints.ipady = -5;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(btnprocurar, constraints);

		constraints.ipadx = 0;
		constraints.ipady = 0;

		lblTelefone = new JLabel("Telefone");
		lblTelefone.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblTelefone, constraints);

		txtTelefone = new JTextField(15);
		txtTelefone.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtTelefone, constraints);
		constraints.gridwidth = 1;

		btnprocurar.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object origem = e.getSource();
		EmpregadorBO empregador = new EmpregadorBO();
		CidadeDao cidDao = new CidadeDao();
		if (origem == btnprocurar){
			FrmConsultaCidade frame = new FrmConsultaCidade(this);
			frame.setVisible(true);
			getDesktopPane().add(frame);
			try {
				frame.setSelected(true);
			}
			catch (PropertyVetoException exc) { }
			FrmMenuGeralMaca.centralizaInternalFrame(frame, getDesktopPane());

		} else if (origem == btnConfirmar) {
			if (txtCodigo.getText().equals("-"))
				empregador.setCodigo(0);
			else
				empregador.setCodigo(Integer.parseInt(txtCodigo.getText()));

			try {
				empregador.setNome(txtNome.getText());
			} catch (StringVaziaException e1) {
				JOptionPane.showMessageDialog(this, "Nome incorreto!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtNome.selectAll();
				txtNome.requestFocus();
				return;
			}

			try {
				empregador.setCpf((String)txtCPF.getValue());
			} catch (CpfInvalidoException e1) {
				JOptionPane.showMessageDialog(this, "CPF incorreto!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtCPF.selectAll();
				txtCPF.requestFocus();
				return;
			}

			try {
				empregador.setIe(txtIE.getText());
			} catch (StringVaziaException e1) {
				JOptionPane.showMessageDialog(this, "Inscrição estadual incorreta!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtIE.selectAll();
				txtIE.requestFocus();
				return;
			}

			if (!txtCodCidade.getText().trim().equals("")) {
				empregador.cidBO.setCodigo(Integer.parseInt(txtCodCidade.getText()));
			}
			else{
				JOptionPane.showMessageDialog(this, "Codigo cidade deve ser preenchido!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtCodCidade.requestFocus();
				return;
			}
			
			empregador.cidBO = cidDao.consultaPorCodigo(Integer.parseInt(txtCodCidade.getText())).get(0);

			try {
				empregador.setTelefone(txtTelefone.getText());
			} catch (StringVaziaException e1) {
				JOptionPane.showMessageDialog(this, "Telefone incorreto!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtTelefone.selectAll();
				txtTelefone.requestFocus();
				return;
			}

			if (consEmpregador == null) {  // veio da inclusão
				dorDao.incluir(empregador);
				JOptionPane.showMessageDialog(this, "Registro salvo com sucesso!", "Registro Salvo", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
				txtNome.setText("");
				txtCPF.setText("");
				txtIE.setText("");
				txtTelefone.setText("");
				txtCodCidade.setText("");
				txtMostraCidade.setText("");
				txtNome.requestFocus();
			} else {
				dorDao.alterar(empregador);
				int linha = consEmpregador.tabela.getSelectedRow();
				consEmpregador.modelo.setValueAt(empregador.getNome(),linha,1);
				consEmpregador.modelo.setValueAt(empregador.getCpf(),linha,2);
				consEmpregador.modelo.setValueAt(empregador.getIe(),linha,3);
				consEmpregador.modelo.setValueAt(empregador.cidBO.getNome(),linha,4);
				consEmpregador.modelo.setValueAt(empregador.getTelefone(),linha,5);
				JOptionPane.showMessageDialog(this, "Registro alterado com sucesso!", "Registro Alterado", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
				doDefaultCloseAction();
			}
		}
	}
}


