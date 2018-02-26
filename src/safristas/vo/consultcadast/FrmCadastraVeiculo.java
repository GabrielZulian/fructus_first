package safristas.vo.consultcadast;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyVetoException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import exceptions.StringVaziaException;
import gerais.vo.FrmCadastraPai;
import safristas.bo.EmpregadoBO;
import safristas.bo.EmpregadorBO;
import safristas.bo.outros.VeiculoBO;
import safristas.bo.safristas.EmpreiteiroBO;
import safristas.dao.EmpregadoDao;
import safristas.dao.EmpregadorDao;
import safristas.dao.outros.VeiculoDao;
import safristas.dao.safristas.EmpreiteiroDao;
import safristas.vo.FrmMenuGeralMaca;

public class FrmCadastraVeiculo extends FrmCadastraPai{
	private static final long serialVersionUID = -8731838749878188826L;

	private JLabel lblCodigo, lblPlaca, lblTipoVeiculo, lblTipoProprietario, lblProprietario, lblDescricao;
	protected JTextField txtCodigo, txtPlaca, txtCodProp, txtMostraProp;
	private JTextArea txtDescricao;
	private JButton btnProcuraProp;
	private JRadioButton rBtnTrator, rBtnCaminhao, rBtnEmpregado, rBtnEmpreiteiro, rBtnEmpregador;
	private ButtonGroup grupoRBtn, grupoRBtnProp;
	protected char tipoProp = 'I';

	public VeiculoBO veicBO = new VeiculoBO();

	protected FrmConsultaVeiculo consVeiculo= null;

	public FrmCadastraVeiculo(FrmConsultaVeiculo consVeiculo) {
		this();
		this.consVeiculo = consVeiculo;
		txtCodigo.setText(String.valueOf(consVeiculo.veicBO.getCodigo()));
		if (consVeiculo.veicBO.getTipoVeiculo() == 'C')
			rBtnCaminhao.setSelected(true);
		if (consVeiculo.veicBO.getTipoEmpregado() == 'I'){
			this.tipoProp = 'I';
			txtCodProp.setText(String.valueOf(consVeiculo.veicBO.proprietarioIro.getCodigo()));
			txtMostraProp.setText(consVeiculo.veicBO.proprietarioIro.getNome());
			rBtnEmpreiteiro.setSelected(true);
		} else if (consVeiculo.veicBO.getTipoEmpregado() == 'A'){
			this.tipoProp = 'A';
			txtCodProp.setText(String.valueOf(consVeiculo.veicBO.proprietarioAdo.getCodigo()));
			txtMostraProp.setText(consVeiculo.veicBO.proprietarioAdo.getNome());
			rBtnEmpregado.setSelected(true);
		} else if (consVeiculo.veicBO.getTipoEmpregado() == 'D'){
			this.tipoProp = 'D';
			txtCodProp.setText(String.valueOf(consVeiculo.veicBO.proprietarioDor.getCodigo()));
			txtMostraProp.setText(consVeiculo.veicBO.proprietarioDor.getNome());
			rBtnEmpregador.setSelected(true);
		}

		txtPlaca.setText(consVeiculo.veicBO.getPlaca());
		txtDescricao.setText(consVeiculo.veicBO.getDescricao());
		lblTitulo.setText("Alterar Veículo");
		btnConfirmar.setText("Alterar (F1)");
	}

	public FrmCadastraVeiculo() {

		setTitle("Cadastro de Veículos");
		setSize(580, 420);
		setResizable(false);
		lblTitulo.setText(super.lblTitulo.getText() + " de Veículo");
		lblImg.setIcon(new ImageIcon(getClass().getResource("/icons/icon_veiculo.gif")));

		JPanel painelRBtn = new JPanel();
		painelRBtn.setLayout(new FlowLayout());

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

		lblTipoVeiculo = new JLabel("Tipo Veículo");
		lblTipoVeiculo.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblTipoVeiculo, constraints);

		rBtnTrator = new JRadioButton("Trator", true);
		rBtnCaminhao = new JRadioButton("Caminhão", false);
		grupoRBtn = new ButtonGroup();
		grupoRBtn.add(rBtnTrator);
		grupoRBtn.add(rBtnCaminhao);

		painelRBtn.add(rBtnTrator);
		painelRBtn.add(rBtnCaminhao);

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelRBtn.setBorder(BorderFactory.createEtchedBorder());
		painelMeio.add(painelRBtn, constraints);

		JPanel painelRBtn2 = new JPanel();
		painelRBtn2.setLayout(new FlowLayout());

		lblTipoProprietario = new JLabel("Tipo Prop.");
		lblTipoProprietario.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblTipoProprietario, constraints);

		rBtnEmpregado = new JRadioButton("Empregado", true);
		rBtnEmpreiteiro = new JRadioButton("Empreiteiro", false);
		rBtnEmpregador = new JRadioButton("Empregador", false);
		grupoRBtnProp = new ButtonGroup();
		grupoRBtnProp.add(rBtnEmpregado);
		grupoRBtnProp.add(rBtnEmpreiteiro);
		grupoRBtnProp.add(rBtnEmpregador);

		painelRBtn2.add(rBtnEmpregado);
		painelRBtn2.add(rBtnEmpreiteiro);
		painelRBtn2.add(rBtnEmpregador);

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelRBtn2.setBorder(BorderFactory.createEtchedBorder());
		painelMeio.add(painelRBtn2, constraints);

		lblProprietario = new JLabel("Código Proprietário");
		lblProprietario.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblProprietario, constraints);

		txtCodProp = new JTextField(5);
		txtCodProp.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtCodProp, constraints);
		txtCodProp.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				if (rBtnEmpregado.isSelected()) {
					EmpregadoDao adoDao = new EmpregadoDao();
					EmpregadoBO adoBO = new EmpregadoBO();
					adoBO = adoDao.consultaPorCodigo(Integer.parseInt(txtCodProp.getText())).get(0);
					txtMostraProp.setText(adoBO.getNome());
				} else if (rBtnEmpreiteiro.isSelected()) {
					EmpreiteiroDao iroDao = new EmpreiteiroDao();
					EmpreiteiroBO iroBO = new EmpreiteiroBO();
					iroBO = iroDao.consultaPorCodigo(Integer.parseInt(txtCodProp.getText())).get(0);
					txtMostraProp.setText(iroBO.getNome());
				} else if (rBtnEmpregador.isSelected()) {
					EmpregadorDao dorDao = new EmpregadorDao();
					EmpregadorBO dorBO = new EmpregadorBO();
					dorBO = dorDao.consultaPorCodigo(Integer.parseInt(txtCodProp.getText())).get(0);
					txtMostraProp.setText(dorBO.getNome());
				}
			}
		});

		txtMostraProp = new JTextField(18);
		txtMostraProp.setFont(f2);
		txtMostraProp.setEditable(false);
		txtMostraProp.setFocusable(false);
		constraints.gridx = 2;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtMostraProp, constraints);

		btnProcuraProp = new JButton("Procurar", new ImageIcon(getClass().getResource("/icons/icon_procurar.gif")));
		btnProcuraProp.setFocusable(false);
		constraints.gridx = 4;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.ipady = -5;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(btnProcuraProp, constraints);

		constraints.ipady = 0;

		lblPlaca = new JLabel("Placa/Identificação");
		lblPlaca.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblPlaca, constraints);

		txtPlaca = new JTextField(8);
		txtPlaca.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtPlaca, constraints);

		lblDescricao = new JLabel("Descrição");
		lblDescricao.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.NORTHEAST;
		painelMeio.add(lblDescricao, constraints);

		txtDescricao = new JTextArea(4,20);
		txtDescricao.setFont(f2);
		txtDescricao.setLineWrap(true);
		txtDescricao.setMargin(new Insets(4, 4, 4, 4));
		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.gridwidth = 4;
		constraints.anchor = GridBagConstraints.WEST;
		JScrollPane rolagemDesc = new JScrollPane(txtDescricao);
		painelMeio.add(rolagemDesc, constraints);

		btnProcuraProp.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object origem = e.getSource();
		VeiculoDao veicDao = new VeiculoDao();

		if (origem == btnConfirmar){
			if (txtCodigo.getText().equals("-"))
				veicBO.setCodigo(0);
			else
				veicBO.setCodigo(Integer.parseInt(txtCodigo.getText()));

			try {
				veicBO.setPlaca(txtPlaca.getText());
			} catch (StringVaziaException e1) {
				JOptionPane.showMessageDialog(this, "Placa ou Identificação incorreta!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtPlaca.requestFocus();
				txtPlaca.selectAll();
				return;
			}

			char tipo;
			if (rBtnTrator.isSelected())
				tipo = 'T';
			else
				tipo = 'C';

			try {
				if (rBtnEmpregado.isSelected()) {
					tipoProp = 'A';
					veicBO.proprietarioAdo.setCodigo(Integer.parseInt(txtCodProp.getText()));
					veicBO.proprietarioAdo.setNome(txtMostraProp.getText());
				} else if (rBtnEmpreiteiro.isSelected()) {
					tipoProp = 'I';
					veicBO.proprietarioIro.setCodigo(Integer.parseInt(txtCodProp.getText()));				
					veicBO.proprietarioIro.setNome(txtMostraProp.getText());
				} else if (rBtnEmpregador.isSelected()) {
					tipoProp = 'D';
					veicBO.proprietarioDor.setCodigo(Integer.parseInt(txtCodProp.getText()));
					veicBO.proprietarioDor.setNome(txtMostraProp.getText());
				}
			} catch (StringVaziaException erro) {
				JOptionPane.showMessageDialog(this, "Código deve ser preenchido!", "ERRO", JOptionPane.ERROR_MESSAGE);
				return;
			} catch (NumberFormatException erro) {
				JOptionPane.showMessageDialog(this, "Código deve se numérico!", "ERRO", JOptionPane.ERROR_MESSAGE);
				return;
			}

			veicBO.setTipoVeiculo(tipo);

			veicBO.setTipoEmpregado(tipoProp);

			veicBO.setDescricao(txtDescricao.getText());

			if (consVeiculo == null){  // veio da inclusão
				veicDao.incluir(veicBO);
				JOptionPane.showMessageDialog(this, "Registro salvo com sucesso!", "Registro Salvo", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
				rBtnTrator.setSelected(true);
				rBtnTrator.requestFocus();
				txtPlaca.setText("");
				txtCodProp.setText("");
				txtMostraProp.setText("");
				txtDescricao.setText("");
			}else{
				veicDao.alterar(veicBO);
				int linha = consVeiculo.tabela.getSelectedRow();
				consVeiculo.modelo.setValueAt(veicBO.getPlaca(),linha,1);
				consVeiculo.modelo.setValueAt(veicBO.getTipoVeiculoString(),linha,2);
				consVeiculo.modelo.setValueAt(veicBO.getDescricao(), linha, 3);
				verifProp(linha);
				consVeiculo.modelo.setValueAt(veicBO.getTipoEmpregadoString(), linha, 5);

				JOptionPane.showMessageDialog(this, "Registro alterado com sucesso!", "Registro Alterado", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
				doDefaultCloseAction();
			}
		} else if (origem == btnProcuraProp) {
			if (rBtnEmpregado.isSelected()) {
				FrmConsultaEmpregado fr = new FrmConsultaEmpregado(this);
				fr.setVisible(true);  
				getDesktopPane().add(fr);
				try {
					fr.setSelected(true);
				}catch (PropertyVetoException exc) { }
				FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
			} else if (rBtnEmpreiteiro.isSelected()) {
				FrmConsultaEmpreiteiro fr = new FrmConsultaEmpreiteiro(this);
				fr.setVisible(true);  
				getDesktopPane().add(fr);
				try {
					fr.setSelected(true);
				}catch (PropertyVetoException exc) { }
				FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
			} else if (rBtnEmpregador.isSelected()) {
				FrmConsultaEmpregador fr = new FrmConsultaEmpregador(this);
				fr.setVisible(true);  
				getDesktopPane().add(fr);
				try {
					fr.setSelected(true);
				}catch (PropertyVetoException exc) { }
				FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
			}
		}
	}

	private void verifProp(int linha) {
		if (tipoProp == 'I') {
			consVeiculo.modelo.setValueAt(veicBO.proprietarioIro.getNome(), linha, 4);
		} else if (tipoProp == 'A') {
			consVeiculo.modelo.setValueAt(veicBO.proprietarioAdo.getNome(), linha, 4);
		} else if (tipoProp == 'D') {
			consVeiculo.modelo.setValueAt(veicBO.proprietarioDor.getNome(), linha, 4);
		}
	}
}
