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
import safristas.bo.EmpregadoBO;
import safristas.bo.ProvaBO;
import safristas.bo.safristas.EquipeBO;
import safristas.dao.CidadeDao;
import safristas.dao.EmpregadoDao;
import safristas.dao.FuncaoDao;
import safristas.dao.safristas.EquipeDao;
import safristas.vo.FrmMenuGeralMaca;
import safristas.vo.especiais.FrmVisaoGeral;

public class FrmCadastraEmpregado extends FrmCadastraPai{
	private static final long serialVersionUID = -1764961556184621999L;
	private JLabel lblCodigo, lblNome, lblApelido, lblCpf, lblCodFuncao, lblCodEquipe;
	protected JTextField txtCodigo, txtNome, txtApelido, txtCodFuncao, txtMostraFuncao, 
	txtCodEquipe, txtMostraEquipe;
	protected JFormattedTextField txtCPF;
	protected JButton btnProcuraFuncao, btnProcuraEquipe;
	private MaskFormatter mascaraCPF;

	private EmpregadoDao adoDao = new EmpregadoDao();
	protected CidadeDao cidDao = new CidadeDao();
	protected EquipeDao equiDao = new EquipeDao();
	protected FuncaoDao funDao = new FuncaoDao();
	public FrmConsultaEmpregado consEmpregado = null;
	public FrmVisaoGeral visaoGeral = null;

	public FrmCadastraEmpregado(FrmConsultaEmpregado consEmpregado) {
		this();
		lblTitulo.setText("Alterar Empregado");
		this.consEmpregado = consEmpregado;
		txtCodigo.setText(String.valueOf(consEmpregado.adoBO.getCodigo()));
		txtCodigo.setEditable(false);
		txtNome.setText(consEmpregado.adoBO.getNome());
		txtApelido.setText(consEmpregado.adoBO.getApelido());
		txtCPF.setValue(consEmpregado.adoBO.getCpf());
		txtCodFuncao.setText(String.valueOf(consEmpregado.adoBO.funcaoBO.getCodigo()));
		txtCodEquipe.setText(String.valueOf(consEmpregado.adoBO.equipeBO.getCodigo()));
		txtMostraFuncao.setText(consEmpregado.adoBO.funcaoBO.getNome());
		txtMostraEquipe.setText(consEmpregado.adoBO.equipeBO.getNome());

		btnConfirmar.setText("Alterar (F1)");
	}

	public FrmCadastraEmpregado(FrmVisaoGeral visaoGeral) {
		this();
		lblTitulo.setText("Alterar Empregado");
		this.visaoGeral = visaoGeral;
		txtCodigo.setText(String.valueOf(visaoGeral.adoBO.getCodigo()));
		txtCodigo.setEditable(false);
		txtNome.setText(visaoGeral.adoBO.getNome());
		txtApelido.setText(visaoGeral.adoBO.getApelido());
		txtCPF.setValue(visaoGeral.adoBO.getCpf());
		txtCodFuncao.setText(String.valueOf(visaoGeral.adoBO.funcaoBO.getCodigo()));
		txtCodEquipe.setText(String.valueOf(visaoGeral.adoBO.equipeBO.getCodigo()));
		txtMostraFuncao.setText(visaoGeral.adoBO.funcaoBO.getNome());
		txtMostraEquipe.setText(visaoGeral.adoBO.equipeBO.getNome());

		btnConfirmar.setText("Alterar (F1)");
	}

	public FrmCadastraEmpregado() {

		setTitle("Cadastro de Empregados");

		lblTitulo.setText(super.lblTitulo.getText() + " de Empregados");
		lblImg.setIcon(new ImageIcon(getClass().getResource("/icons/icon_empregado.gif")));
		setResizable(false);
		setSize(getWidth()-150, getHeight()-240);

		lblCodigo = new JLabel("Código");
		lblCodigo.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblCodigo, constraints);

		txtCodigo = new JTextField(5);
		txtCodigo.setFont(f2);
		txtCodigo.setEditable(false);
		txtCodigo.setFocusable(false);
		txtCodigo.setText("-");
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtCodigo, constraints);
		constraints.gridwidth = 1;

		lblNome = new JLabel("Nome");
		lblNome.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblNome, constraints);

		txtNome = new JTextField(24);
		txtNome.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 3;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtNome, constraints);
		constraints.gridwidth = 1;

		lblApelido = new JLabel("Apelido");
		lblApelido.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblApelido, constraints);

		txtApelido = new JTextField(14);
		txtApelido.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtApelido, constraints);
		constraints.gridwidth = 1;

		lblCpf = new JLabel("CPF");
		lblCpf.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblCpf, constraints);

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
		constraints.gridy = 4;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtCPF, constraints);
		constraints.gridwidth = 1;

		lblCodEquipe = new JLabel("Código Equipe");
		lblCodEquipe.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 6;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblCodEquipe, constraints);

		txtCodEquipe = new JTextField(5);
		txtCodEquipe.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 6;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtCodEquipe, constraints);

		txtCodEquipe.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				EquipeDao equiDao = new EquipeDao();
				EquipeBO equiBo = equiDao.consultaPorCodigo(Integer.parseInt(txtCodEquipe.getText())).get(0);
				if (equiBo == null){
					txtCodEquipe.selectAll();
					txtCodEquipe.requestFocus();
					txtMostraEquipe.setText("");
				} else
					txtMostraEquipe.setText(equiBo.getNome());
			}
		});

		txtMostraEquipe = new JTextField(18);
		txtMostraEquipe.setFont(f2);
		txtMostraEquipe.setEditable(false);
		txtMostraEquipe.setFocusable(false);
		constraints.gridwidth = 3;
		constraints.gridx = 2;
		constraints.gridy = 6;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtMostraEquipe, constraints);
		constraints.gridwidth = 1;

		btnProcuraEquipe = new JButton("Procurar", new ImageIcon(getClass().getResource("/icons/icon_procurar.gif")));
		btnProcuraEquipe.setFocusable(false);
		constraints.gridx = 4;
		constraints.gridy = 6;
		constraints.ipady = -5;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(btnProcuraEquipe, constraints);
		constraints.gridwidth = 1;
		constraints.ipady = 1;

		lblCodFuncao = new JLabel("Código Função");
		lblCodFuncao.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 7;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblCodFuncao, constraints);

		txtCodFuncao = new JTextField(5);
		txtCodFuncao.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 7;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtCodFuncao, constraints);

		txtCodFuncao.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				ArrayList<ProvaBO> funBo = funDao.consultaPorCodigo(Integer.parseInt(txtCodFuncao.getText()));
				if (funBo == null) {
					txtMostraFuncao.setText("");
					txtCodFuncao.selectAll();
					txtCodFuncao.requestFocus();
				} else
					txtMostraFuncao.setText(funBo.get(0).getNome());
			}
		});

		txtMostraFuncao = new JTextField(18);
		txtMostraFuncao.setFont(f2);
		txtMostraFuncao.setFocusable(false);
		constraints.gridx = 2;
		constraints.gridy = 7;
		constraints.gridwidth = 3;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtMostraFuncao, constraints);
		txtMostraFuncao.setEditable(false);
		constraints.gridwidth = 1;

		btnProcuraFuncao = new JButton("Procurar", new ImageIcon(getClass().getResource("/icons/icon_procurar.gif")));
		btnProcuraFuncao.setFocusable(false);
		constraints.gridx = 4;
		constraints.gridy = 7;
		constraints.ipady = -5;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(btnProcuraFuncao, constraints);
		constraints.gridwidth = 1;
		constraints.ipady = 1;

		btnProcuraEquipe.addActionListener(this);
		btnProcuraFuncao.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object origem = e.getSource();

		EmpregadoBO empregado = new EmpregadoBO();

		if (origem == btnProcuraFuncao) {
			FrmConsultaFuncao frame = new FrmConsultaFuncao(this);
			frame.setVisible(true);
			getDesktopPane().add(frame);
			try {
				frame.setSelected(true);
			}
			catch (PropertyVetoException exc) { }
			FrmMenuGeralMaca.centralizaInternalFrame(frame, getDesktopPane());

		} else if (origem == btnProcuraEquipe) {
			FrmConsultaEquipe frame = new FrmConsultaEquipe(this);
			frame.setVisible(true);
			getDesktopPane().add(frame);
			try {
				frame.setSelected(true);
			}
			catch (PropertyVetoException exc) { }
			FrmMenuGeralMaca.centralizaInternalFrame(frame, getDesktopPane());

		} else if (origem == btnConfirmar) {
			if (txtCodigo.getText().equals("-"))
				empregado.setCodigo(0);
			else
				empregado.setCodigo(Integer.parseInt(txtCodigo.getText()));


			try {
				empregado.setNome(txtNome.getText());
			} catch (StringVaziaException e1) {
				JOptionPane.showMessageDialog(this, "Nome incorreto!", "ERRO", JOptionPane.ERROR_MESSAGE);
				return;
			}

			empregado.setApelido(txtApelido.getText());

			try {
				empregado.setCpf(txtCPF.getValue().toString());
			} catch (CpfInvalidoException e1) {
				JOptionPane.showMessageDialog(this, "CPF incorreto ou inválido!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtCPF.selectAll();
				txtCPF.requestFocus();
				return;
			}

			if (txtCodFuncao.getText() != "") {
				empregado.funcaoBO.setCodigo(Integer.parseInt(txtCodFuncao.getText()));
			}
			else {
				JOptionPane.showMessageDialog(this, "Código da Função incorreto!", "ERRO", JOptionPane.ERROR_MESSAGE);
				return;
			}
			empregado.funcaoBO = funDao.consultaPorCodigo(Integer.parseInt(txtCodFuncao.getText())).get(0);

			if (txtCodEquipe.getText() != "") {
				empregado.equipeBO.setCodigo(Integer.parseInt(txtCodEquipe.getText()));
			}
			else {
				JOptionPane.showMessageDialog(this, "Código da Equipe incorreto!", "ERRO", JOptionPane.ERROR_MESSAGE);
				return;
			}
			empregado.equipeBO = equiDao.consultaPorCodigo(Integer.parseInt(txtCodEquipe.getText())).get(0);

			if (consEmpregado == null && visaoGeral == null) {  // veio da inclusão
				adoDao.incluir(empregado);
				JOptionPane.showMessageDialog(this, "Registro salvo com sucesso!", "Registro Salvo", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
				txtNome.setText("");
				txtApelido.setText("");
				txtCPF.setText("");
				txtCodEquipe.setText("");
				txtMostraEquipe.setText("");
				txtCodFuncao.setText("");
				txtMostraFuncao.setText("");
				txtNome.requestFocus();
			} else {
				adoDao.alterar(empregado);
				if (visaoGeral == null) {
					int linha = consEmpregado.tabela.getSelectedRow();
					consEmpregado.modelo.setValueAt(empregado.getNome(),linha,1);
					consEmpregado.modelo.setValueAt(empregado.getApelido(),linha,2);
					consEmpregado.modelo.setValueAt(empregado.equipeBO.iroBO.getNome(),linha,3);
					consEmpregado.modelo.setValueAt(empregado.equipeBO.getNome(),linha,4);
					consEmpregado.modelo.setValueAt(empregado.getCpf(),linha,5);
					consEmpregado.modelo.setValueAt(empregado.funcaoBO.getNome(),linha,6);
				} else
					visaoGeral.confirmar();
				
				JOptionPane.showMessageDialog(this, "Registro alterado com sucesso!", "Registro Alterado", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
				doDefaultCloseAction();
			}
		}
	}
}
