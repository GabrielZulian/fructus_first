package insumos.vo;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyVetoException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import exceptions.CodigoErradoException;
import exceptions.StringVaziaException;
import gerais.vo.FrmCadastraPai;
import insumos.bo.AplicacaoBO;
import insumos.bo.InsumoBO;
import insumos.bo.TalhaoBO;
import insumos.dao.AplicacaoDao;
import insumos.dao.InsumoDao;
import insumos.dao.TalhaoDao;
import safristas.vo.FrmMenuGeralMaca;

public class FrmLancaAplicacao extends FrmCadastraPai {
	private static final long serialVersionUID = 7491052336081296067L;

	private MaskFormatter mascaraData;
	protected DecimalFormat decimal = new DecimalFormat( "#,##0.00" );
	private JLabel lblCodigo, lblData, lblCodTalhao, lblInsumo, lblQuantidade, lblHistorico;
	private JFormattedTextField txtData;
	public JTextField txtCodigo, txtCodInsumo, txtMostraInsumo, txtCodigoTalhao, txtMostraTalhao, txtQuantidade, txtMostraUnidade;
	private JTextArea txtHistorico;
	private JButton btnProcuraInsumo, btnProcuraLavoura;
	
	private Locale brasil = new Locale("pt", "BR");
	protected DateTimeZone zona = DateTimeZone.forID("Etc/GMT+3");
	protected DateTime data = new DateTime(zona);
	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy", brasil);

	FrmConsultaAplicacao consAplicacao = null;

	public FrmLancaAplicacao(FrmConsultaAplicacao consAplicacao) {
		this();
		this.consAplicacao = consAplicacao;

		txtCodigo.setText(String.valueOf(consAplicacao.aplicacaoBO.getCodigo()));
		txtData.setText(df.format(consAplicacao.aplicacaoBO.getData().toDate()));
		txtCodigoTalhao.setText(String.valueOf(consAplicacao.aplicacaoBO.talhaoBO.getCodigo()));
		txtMostraTalhao.setText("Nro: " + consAplicacao.aplicacaoBO.talhaoBO.getNumero() + " - Tipo: " + consAplicacao.aplicacaoBO.talhaoBO.getTipo());
		txtCodInsumo.setText(String.valueOf(consAplicacao.aplicacaoBO.insumoBO.getCodigo()));
		txtMostraInsumo.setText(consAplicacao.aplicacaoBO.insumoBO.getDescricao());
		txtQuantidade.setText(String.valueOf(consAplicacao.aplicacaoBO.getQuantidade()).replace(",", "").replace(".", ","));
		txtMostraUnidade.setText(consAplicacao.aplicacaoBO.insumoBO.getUnidade());
		txtHistorico.setText(consAplicacao.aplicacaoBO.getHistorico());

		btnConfirmar.setText("Alterar");
	}

	public FrmLancaAplicacao() {
		
		setSize(getWidth(), getHeight()+20);
		setTitle("Lançar aplicação");
		lblTitulo.setText("Lançamento de aplicação");
		lblImg.setIcon(new ImageIcon(getClass().getResource("/icons/icon_lavoura.gif")));
		
		setSize(getWidth()-160, getHeight()-250);
		
		lblCodigo = new JLabel("Código");
		lblCodigo.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblCodigo, constraints);

		txtCodigo = new JTextField(4);
		txtCodigo.setFocusable(false);
		txtCodigo.setEditable(false);
		txtCodigo.setText("-");
		txtCodigo.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtCodigo, constraints);
		
		lblData = new JLabel("Data aplicação");
		lblData.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblData, constraints);

		try {
			mascaraData = new MaskFormatter("##'/##'/####");
		} catch (ParseException e) {}

		txtData = new JFormattedTextField(mascaraData);
		txtData.setColumns(7);
		txtData.setFont(f2);
		txtData.setText(df.format(data.toDate()));
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtData, constraints);
		constraints.gridwidth = 1;

		lblCodTalhao = new JLabel("Código talhão");
		lblCodTalhao.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblCodTalhao, constraints);

		txtCodigoTalhao = new JTextField(4);
		txtCodigoTalhao.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtCodigoTalhao, constraints);
		txtCodigoTalhao.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				TalhaoDao talhaoDAO = new TalhaoDao();
				TalhaoBO talhaoBO = new TalhaoBO();
				
				talhaoBO = talhaoDAO.consultaPorCodigo(Integer.parseInt(txtCodigoTalhao.getText())).get(0);
				
				if (talhaoBO == null) {
					JOptionPane.showMessageDialog(null, "Talhão não existente", "ERRO", JOptionPane.ERROR_MESSAGE);
					txtCodigoTalhao.requestFocus();
					txtCodigoTalhao.selectAll();
					return;
				}
				
				txtCodigoTalhao.setText(String.valueOf(talhaoBO.getCodigo()));
				
				txtMostraTalhao.setText("Nro: " + talhaoBO.getNumero() + " - Tipo: " + talhaoBO.getTipo());
			}
		});
		
		txtMostraTalhao = new JTextField(14);
		txtMostraTalhao.setFont(f2);
		txtMostraTalhao.setFocusable(false);
		txtMostraTalhao.setEditable(false);
		constraints.gridx = 2;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtMostraTalhao, constraints);
		
		btnProcuraLavoura = new JButton("Procurar", new ImageIcon(getClass().getResource("/icons/icon_procurar.gif")));
		btnProcuraLavoura.setFocusable(false);
		constraints.gridx = 3;
		constraints.gridy = 2;
		constraints.ipady = -5;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(btnProcuraLavoura, constraints);
		constraints.ipady = 0;

		lblInsumo = new JLabel("Cód. insumo");
		lblInsumo.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblInsumo, constraints);

		txtCodInsumo = new JTextField(4);
		txtCodInsumo.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtCodInsumo, constraints);
		txtCodInsumo.addFocusListener(new FocusAdapter() {
			
			@Override
			public void focusLost(FocusEvent e) {
				InsumoDao insumoDao = new InsumoDao();
				InsumoBO insumoBO = new InsumoBO();
				
				insumoBO = insumoDao.consultaPorCodigo(Integer.parseInt(txtCodInsumo.getText())).get(0);
				
				txtMostraInsumo.setText(insumoBO.getDescricao());
				txtMostraUnidade.setText(insumoBO.getUnidade());
			}
		});

		txtMostraInsumo = new JTextField(14);
		txtMostraInsumo.setFocusable(false);
		txtMostraInsumo.setEditable(false);
		txtMostraInsumo.setFont(f2);
		constraints.gridx = 2;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtMostraInsumo, constraints);
		
		btnProcuraInsumo = new JButton("Procurar", new ImageIcon(getClass().getResource("/icons/icon_procurar.gif")));
		btnProcuraInsumo.setFocusable(false);
		constraints.gridx = 3;
		constraints.gridy = 3;
		constraints.ipady = -5;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(btnProcuraInsumo, constraints);
		constraints.ipady = 0;
		
		lblQuantidade = new JLabel("Quantidade");
		lblQuantidade.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblQuantidade, constraints);
		
		txtQuantidade = new JTextField(4);
		txtQuantidade.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtQuantidade, constraints);
		
		txtMostraUnidade = new JTextField(3);
		txtMostraUnidade.setFocusable(false);
		txtMostraUnidade.setEditable(false);
		txtMostraUnidade.setFont(f2);
		constraints.gridx = 2;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtMostraUnidade, constraints);
		
		lblHistorico = new JLabel("Histórico");
		lblHistorico.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.anchor = GridBagConstraints.NORTHEAST;
		painelMeio.add(lblHistorico, constraints);
		
		txtHistorico = new JTextArea(3, 25);
		txtHistorico.setLineWrap(true);
		txtHistorico.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridwidth = 3;
		JScrollPane jspane = new JScrollPane(txtHistorico);
		painelMeio.add(jspane, constraints);
		constraints.gridwidth = 1;

		btnProcuraLavoura.addActionListener(this);
		btnProcuraInsumo.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object origem = e.getSource();
		AplicacaoBO apliBO = new AplicacaoBO();
		AplicacaoDao aplicacaoDao = new AplicacaoDao();

		if (origem == btnConfirmar) {

			try {
				if (txtCodigo.getText().equals("-"))
					apliBO.setCodigo(0);
				else
					apliBO.setCodigo(Integer.parseInt(txtCodigo.getText()));
			} catch (CodigoErradoException erro) {
				JOptionPane.showMessageDialog(this, "Código incorreto", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtCodigoTalhao.requestFocus();
				txtCodigoTalhao.selectAll();
				return;
			}

			try {
				apliBO.setData(new DateTime(df.parse(txtData.getText())));
			} catch (ParseException e1) {
				JOptionPane.showMessageDialog(this, "Data incorreta", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtData.requestFocus();
				txtData.selectAll();
				return;
			}

			try {
				apliBO.talhaoBO.setCodigo(Integer.parseInt(txtCodigoTalhao.getText()));
			} catch (NumberFormatException erro) {
				JOptionPane.showMessageDialog(this, "Código talhão incorreto", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtCodigoTalhao.requestFocus();
				txtCodigoTalhao.selectAll();
				return;
			}

			try {
				apliBO.insumoBO.setCodigo(Integer.parseInt(txtCodInsumo.getText()));
			} catch (NumberFormatException erro) {
				JOptionPane.showMessageDialog(this, "Código insumo incorreto", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtCodInsumo.requestFocus();
				txtCodInsumo.selectAll();
				return;
			}
			
			try {
				apliBO.setQuantidade(Double.parseDouble(txtQuantidade.getText().trim().replace(',', '.')));
			} catch (NumberFormatException erro) {
				JOptionPane.showMessageDialog(this, "Quantidade incorreta", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtCodInsumo.requestFocus();
				txtCodInsumo.selectAll();
				return;
			}
			
			try {
				apliBO.setHistorico(txtHistorico.getText());
			} catch (StringVaziaException e1) {
				JOptionPane.showMessageDialog(this, "Histórico incorreto", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtHistorico.requestFocus();
				txtHistorico.selectAll();
				return;
			}

			if (consAplicacao == null) {   // veio da inclusão
				aplicacaoDao.incluir(apliBO);
				JOptionPane.showMessageDialog(this, "Registro salvo com sucesso!", "Registro Salvo", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
			} else {
				aplicacaoDao.alterar(apliBO);
				AplicacaoBO apliBOAlterada = aplicacaoDao.consultaPorCodigo(apliBO.getCodigo()).get(0);
				int linha = consAplicacao.tabela.getSelectedRow();
				consAplicacao.modelo.setValueAt(df.format(apliBOAlterada.getData().toDate()),linha,1);
				consAplicacao.modelo.setValueAt(apliBOAlterada.talhaoBO.getNumero(),linha,2);
				consAplicacao.modelo.setValueAt(apliBOAlterada.talhaoBO.getTipo(),linha,3);
				consAplicacao.modelo.setValueAt(apliBOAlterada.insumoBO.getDescricao(),linha,4);
				consAplicacao.modelo.setValueAt(apliBOAlterada.getHistorico(),linha,5);
				JOptionPane.showMessageDialog(this, "Registro alterado com sucesso!", "Registro Alterado", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
			}
			doDefaultCloseAction();
		} else if (origem == btnProcuraInsumo) {
			FrmConsultaInsumo fr = new FrmConsultaInsumo(this);
			fr.setVisible(true);
			getDesktopPane().add(fr);
			try {
				fr.setSelected(true);
			}
			catch (PropertyVetoException exc) {  }
			FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
		} else if (origem == btnProcuraLavoura) {
			FrmConsultaTalhao fr = new FrmConsultaTalhao(this);
			fr.setVisible(true);
			getDesktopPane().add(fr);
			try {
				fr.setSelected(true);
			}
			catch (PropertyVetoException exc) { }
			FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
		}
	}

}
