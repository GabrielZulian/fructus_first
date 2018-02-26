package safristas.vo.consultcadast;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import exceptions.StringVaziaException;
import gerais.vo.FrmCadastraPai;
import safristas.bo.safristas.EmpreiteiroBO;
import safristas.bo.safristas.EquipeBO;
import safristas.dao.safristas.EmpreiteiroDao;
import safristas.dao.safristas.EquipeDao;
import safristas.vo.FrmMenuGeralMaca;

public class FrmCadastraEquipe extends FrmCadastraPai{
	private static final long serialVersionUID = 7047848303845095004L;
	
	private JLabel lblCodigo, lblNome, lblCodEmpreiteiro;
	protected JTextField txtCodigo, txtNome, txtCodEmpreiteiro, txtMostraEmpreiteiro;
	private JButton btnprocuraEmpreiteiro;
	private EquipeDao equiDao = new EquipeDao();

	FrmConsultaEquipe consEquipe = null;

	public FrmCadastraEquipe(FrmConsultaEquipe consEquipe) {
		this();
		this.consEquipe = consEquipe;
		txtCodigo.setText(String.valueOf(consEquipe.equiBO.getCodigo()));
		txtNome.setText(consEquipe.equiBO.getNome());
		txtCodEmpreiteiro.setText(String.valueOf(consEquipe.equiBO.iroBO.getCodigo()));
		txtMostraEmpreiteiro.setText(consEquipe.equiBO.iroBO.getNome());

		btnConfirmar.setText("Alterar (F1)");
	}


	public FrmCadastraEquipe() {

		setTitle("Cadastro de Equipes");
		setSize(600,290);

		lblTitulo.setText(super.lblTitulo.getText() + " de Equipes");
		lblImg.setIcon(new ImageIcon(getClass().getResource("/icons/icon_equipe.gif")));

		lblCodigo = new JLabel("Código Equipe");
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

		lblNome = new JLabel("Nome Equipe");
		lblNome.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblNome, constraints);

		txtNome = new JTextField(18);
		txtNome.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtNome, constraints);
		constraints.gridwidth = 1;

		lblCodEmpreiteiro = new JLabel("Código Empreiteiro");
		lblCodEmpreiteiro.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblCodEmpreiteiro, constraints);

		txtCodEmpreiteiro = new JTextField(5);
		txtCodEmpreiteiro.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtCodEmpreiteiro, constraints);

		txtMostraEmpreiteiro = new JTextField(18);
		txtMostraEmpreiteiro.setFont(f2);
		txtMostraEmpreiteiro.setEditable(false);
		constraints.gridx = 2;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtMostraEmpreiteiro, constraints);
		constraints.gridwidth = 1;

		txtCodEmpreiteiro.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				EmpreiteiroDao iroDao = new EmpreiteiroDao();
				ArrayList<EmpreiteiroBO> iroBO = iroDao.consultaPorCodigo(Integer.parseInt(txtCodEmpreiteiro.getText()));
				if (iroBO == null){
					txtCodEmpreiteiro.selectAll();
					txtCodEmpreiteiro.requestFocus();
					txtMostraEmpreiteiro.setText("");
				} else
					txtMostraEmpreiteiro.setText(iroBO.get(0).getNome());
			}
		});

		btnprocuraEmpreiteiro = new JButton("Procurar", new ImageIcon(getClass().getResource("/icons/icon_procurar.gif")));
		constraints.gridx = 4;
		constraints.gridy = 2;
		constraints.ipady = -5;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(btnprocuraEmpreiteiro, constraints);

		btnprocuraEmpreiteiro.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object origem = e.getSource();
		EquipeBO equipe = new EquipeBO();
		EmpreiteiroDao iroDao = new EmpreiteiroDao();
		if (origem == btnprocuraEmpreiteiro) {
			FrmConsultaEmpreiteiro frame = new FrmConsultaEmpreiteiro(this);
			frame.setVisible(true);
			getDesktopPane().add(frame);
			try {
				frame.setSelected(true);
			}
			catch (PropertyVetoException exc) { }
			FrmMenuGeralMaca.centralizaInternalFrame(frame, getDesktopPane());
		} else if (origem == btnConfirmar) {
			if (txtCodigo.getText().equals("-"))
				equipe.setCodigo(0);
			else
				equipe.setCodigo(Integer.parseInt(txtCodigo.getText()));

			try {
				equipe.setNome(txtNome.getText());
			} catch (StringVaziaException e1) {
				JOptionPane.showMessageDialog(this, "Nome incorreto!", "ERRO", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (!txtCodEmpreiteiro.getText().trim().equals("")) {
				equipe.iroBO.setCodigo(Integer.parseInt(txtCodEmpreiteiro.getText()));
			}
			else{
				JOptionPane.showMessageDialog(this, "Codigo empreiteiro deve ser preenchido!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtCodEmpreiteiro.requestFocus();
				return;
			}

			equipe.iroBO = iroDao.consultaPorCodigo(Integer.parseInt(txtCodEmpreiteiro.getText())).get(0);

			if (consEquipe == null) {  // veio da inclusão
				equiDao.incluir(equipe);
				JOptionPane.showMessageDialog(this, "Registro salvo com sucesso!", "Registro Salvo", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
				txtNome.setText("");
				txtCodEmpreiteiro.setText("");
				txtMostraEmpreiteiro.setText("");
				txtNome.requestFocus();
			} else {
				equiDao.alterar(equipe);
				int linha = consEquipe.tabela.getSelectedRow();
				consEquipe.modelo.setValueAt(equipe.getNome(),linha,1);
				consEquipe.modelo.setValueAt(equipe.iroBO.getNome(),linha,2);
				JOptionPane.showMessageDialog(this, "Registro alterado com sucesso!", "Registro Alterado", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
				doDefaultCloseAction();
			}
		}
	}
}
