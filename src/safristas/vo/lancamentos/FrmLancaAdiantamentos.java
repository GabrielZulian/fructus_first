package safristas.vo.lancamentos;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyVetoException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import exceptions.StringVaziaException;
import exceptions.ValorErradoException;
import gerais.vo.FrmCadastraPai;
import safristas.bo.AdiantamentoBO;
import safristas.bo.EmpregadoBO;
import safristas.bo.safristas.EmpreiteiroBO;
import safristas.dao.AdiantamentoDao;
import safristas.dao.EmpregadoDao;
import safristas.dao.safristas.EmpreiteiroDao;
import safristas.vo.FrmMenuGeralMaca;
import safristas.vo.consultcadast.FrmConsultaEmpregado;
import safristas.vo.consultcadast.FrmConsultaEmpreiteiro;

public class FrmLancaAdiantamentos extends FrmCadastraPai implements ActionListener {
	private static final long serialVersionUID = -7978682064878813758L;
	
	protected DecimalFormat decimal = new DecimalFormat( "#,##0.00" );
	protected JLabel lblData, lblTipoEmpregado, lblCodigo, lblCodEmpregado, lblValor, lblHistorico, lblAdiantamento;
	public JTextField txtCodigo, txtCodEmpregado, txtMostraEmpregado, txtValorAdiantamento;
	protected JFormattedTextField txtData;
	private MaskFormatter mascaraData;
	protected JRadioButton rBtnEmpregado, rBtnEmpreiteiro;
	protected ButtonGroup grupo;
	protected JButton btnProcuraEmpregado;
	private Locale brasil = new Locale("pt", "BR");
	protected DateTimeZone zona = DateTimeZone.forID("Etc/GMT+3");
	protected DateTime data = new DateTime(zona);
	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy", brasil);

	public AdiantamentoBO adiantBO = new AdiantamentoBO(); 
	protected AdiantamentoDao adiantDao = new AdiantamentoDao();

	protected FrmConsultaAdiantamentos consAdiant = null;

	public FrmLancaAdiantamentos(FrmConsultaAdiantamentos consAdiant) {
		this();
		this.consAdiant = consAdiant;
		txtCodigo.setText(String.valueOf(consAdiant.adiantBO.getCodigo()));
		txtData.setValue(df.format(consAdiant.adiantBO.data.toDate()));
		if (consAdiant.adiantBO.getTipo() == 'A') {
			rBtnEmpregado.setSelected(true);
			txtCodEmpregado.setText(String.valueOf(consAdiant.adiantBO.adoBO.getCodigo()));
			txtMostraEmpregado.setText(consAdiant.adiantBO.adoBO.getNome());
		} else {
			rBtnEmpreiteiro.setSelected(true);
			txtCodEmpregado.setText(String.valueOf(consAdiant.adiantBO.iroBO.getCodigo()));
			txtMostraEmpregado.setText(consAdiant.adiantBO.iroBO.getNome());
		}
		txtValorAdiantamento.setText(decimal.format(consAdiant.adiantBO.getValor()));
		lblTitulo.setText("Alterar Adiantamento");
		btnConfirmar.setText("Alterar (F1)");
	}

	public FrmLancaAdiantamentos() {
		setResizable(true);
		setSize(690, 315);

		lblImg.setIcon(new ImageIcon(getClass().getResource("/icons/icon_adiantamento.gif")));

		lblTitulo.setText("Lançar Adiantamento");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));

		//-----------------------------------------------

		lblCodigo = new JLabel("Código");
		lblCodigo.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblCodigo, constraints);

		txtCodigo = new JTextField(4);
		txtCodigo.setFont(f2);
		txtCodigo.setFocusable(false);
		txtCodigo.setEditable(false);
		txtCodigo.setText(String.valueOf(adiantDao.getUltimoCod()+1));
		constraints.gridwidth = 2;
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtCodigo, constraints);
		constraints.gridwidth = 1;

		lblData = new JLabel("Data");
		lblData.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblData, constraints);

		try {
			mascaraData = new MaskFormatter("##/##/####");
		} catch (ParseException e) {}

		txtData = new JFormattedTextField(mascaraData);
		txtData.setColumns(7);
		txtData.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtData, constraints);
		constraints.gridwidth = 1;

		lblTipoEmpregado = new JLabel("Tipo empregado");
		lblTipoEmpregado.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblTipoEmpregado, constraints);

		rBtnEmpregado = new JRadioButton("Empregado", true);
		rBtnEmpregado.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(rBtnEmpregado, constraints);
		constraints.gridwidth = 1;

		rBtnEmpreiteiro = new JRadioButton("Empreiteiro", false);
		rBtnEmpreiteiro.setFont(f2);
		constraints.gridx = 3;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(rBtnEmpreiteiro, constraints);

		grupo = new ButtonGroup();
		grupo.add(rBtnEmpregado);
		grupo.add(rBtnEmpreiteiro);

		lblCodEmpregado = new JLabel("Código empregado");
		lblCodEmpregado.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblCodEmpregado, constraints);

		txtCodEmpregado = new JTextField(4);
		txtCodEmpregado.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtCodEmpregado, constraints);
		txtCodEmpregado.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				if (rBtnEmpregado.isSelected()) {
					EmpregadoDao adoDao = new EmpregadoDao();
					EmpregadoBO adoBO = new EmpregadoBO();
					adoBO = adoDao.consultaPorCodigo(Integer.parseInt(txtCodEmpregado.getText())).get(0);
					txtMostraEmpregado.setText(adoBO.getNome());
				} else if (rBtnEmpreiteiro.isSelected()) {
					EmpreiteiroDao iroDao = new EmpreiteiroDao();
					EmpreiteiroBO iroBO = new EmpreiteiroBO();
					iroBO = iroDao.consultaPorCodigo(Integer.parseInt(txtCodEmpregado.getText())).get(0);
					txtMostraEmpregado.setText(iroBO.getNome());
				}
			}
		});

		txtMostraEmpregado = new JTextField(25);
		txtMostraEmpregado.setFont(f2);
		txtMostraEmpregado.setEditable(false);
		txtMostraEmpregado.setFocusable(false);
		constraints.gridx = 2;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtMostraEmpregado, constraints);
		constraints.gridwidth = 1;

		btnProcuraEmpregado = new JButton("Procurar", new ImageIcon(getClass().getResource("/icons/icon_procurar.gif")));
		btnProcuraEmpregado.setFocusable(false);
		constraints.ipady = -5;
		constraints.gridx = 4;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(btnProcuraEmpregado, constraints);
		constraints.ipady = 0;

		lblValor = new JLabel("Valor");
		lblValor.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblValor, constraints);

		txtValorAdiantamento = new JTextField(8);
		txtValorAdiantamento.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtValorAdiantamento, constraints);
		constraints.gridwidth = 1;

		painelMeio.setBorder(BorderFactory.createEtchedBorder());

		//-----------------------------------------------

		btnProcuraEmpregado.addActionListener(this);
		btnCancelar.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object origem = e.getSource();

		if (origem == btnProcuraEmpregado) {
			if (rBtnEmpregado.isSelected()) {
			FrmConsultaEmpregado frame = new FrmConsultaEmpregado(this);
			frame.setVisible(true);
			getDesktopPane().add(frame);
			try {
				frame.setSelected(true);
			}
			catch (PropertyVetoException exc) { }
			FrmMenuGeralMaca.centralizaInternalFrame(frame, getDesktopPane());
			} else {
				FrmConsultaEmpreiteiro frame = new FrmConsultaEmpreiteiro(this);
				frame.setVisible(true);
				getDesktopPane().add(frame);
				try {
					frame.setSelected(true);
				}
				catch (PropertyVetoException exc) { }
				FrmMenuGeralMaca.centralizaInternalFrame(frame, getDesktopPane());
			}
		} else if (origem == btnConfirmar) {
			adiantBO = new AdiantamentoBO();

			adiantBO.setCodigo(Integer.parseInt(txtCodigo.getText()));

			try {
				adiantBO.data = new DateTime(df.parse(txtData.getText()));
			} catch (ParseException e1) {
				JOptionPane.showMessageDialog(this, "Data incorreta!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtData.requestFocus();
				txtData.selectAll();
				return;
			}

			try {
				if (rBtnEmpregado.isSelected()) {
					adiantBO.setTipo('A');
					adiantBO.adoBO.setCodigo(Integer.parseInt(txtCodEmpregado.getText()));
					adiantBO.adoBO.setNome(txtMostraEmpregado.getText());
				} else {
					adiantBO.setTipo('I');
					adiantBO.iroBO.setCodigo(Integer.parseInt(txtCodEmpregado.getText()));
					adiantBO.iroBO.setNome(txtMostraEmpregado.getText());
				}
			} catch (NumberFormatException erro) {
				JOptionPane.showMessageDialog(this, "Valor deve ser num�rico!", "Valor incorreto", JOptionPane.ERROR_MESSAGE);
				txtCodEmpregado.requestFocus();
				txtCodEmpregado.selectAll();
				return;
			} catch (StringVaziaException e1) {}

			try {
				adiantBO.setValor(Double.parseDouble(txtValorAdiantamento.getText().replace(',', '.')));
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "Valor deve ser numérico!", "Valor incorreto", JOptionPane.ERROR_MESSAGE);
				txtValorAdiantamento.requestFocus();
				txtValorAdiantamento.selectAll();
				return;
			} catch (ValorErradoException e1) {
				JOptionPane.showMessageDialog(this, "Valor incorreto!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtValorAdiantamento.requestFocus();
				txtValorAdiantamento.selectAll();
				return;
			}

			if (consAdiant == null) {
				adiantDao.incluir(adiantBO);
				JOptionPane.showMessageDialog(this, "Registro salvo com sucesso!", "Registro Salvo", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
				txtCodigo.setText(String.valueOf(adiantDao.getUltimoCod()+1));
				txtData.setValue("");
				rBtnEmpregado.setSelected(true);
				txtCodEmpregado.setText("");
				txtMostraEmpregado.setText("");
				txtValorAdiantamento.setText("");
				txtData.requestFocus();
			} else {
				adiantDao.alterar(adiantBO);
				int linha = consAdiant.tabela.getSelectedRow();
				consAdiant.modelo.setValueAt(df.format(adiantBO.data.toDate()), linha, 1);
				if (rBtnEmpregado.isSelected()) 
					consAdiant.modelo.setValueAt(adiantBO.adoBO.getNome(), linha, 2);
				else
					consAdiant.modelo.setValueAt(adiantBO.iroBO.getNome(), linha, 2);
				consAdiant.modelo.setValueAt(adiantBO.getValor(), linha, 3);
				consAdiant.modelo.setValueAt(adiantBO.getPagouString(), linha, 4);
				JOptionPane.showMessageDialog(this, "Registro alterado com sucesso!", "Registro Alterado", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
				doDefaultCloseAction();
			}
		} else if (origem == btnCancelar) {
			doDefaultCloseAction();
		}
	}
}
