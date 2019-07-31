package pragas.vo;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import exceptions.StringVaziaException;
import gerais.vo.FrmCadastraPai;
import pragas.bo.AtrativoBO;
import pragas.dao.AtrativoDao;

public class FrmLancaTrocaAplicacao extends FrmCadastraPai{
	private static final long serialVersionUID = 6892773482368052539L;
	
	private JLabel lblCodigo, lblData, lblCodAtrativo, lblHistorico;
	private JTextField txtCodigo, txtCodAtrativo, txtMostraAtrativo;
	private JFormattedTextField txtData;
	private MaskFormatter mascaraData;
	private JTextArea txtHistorico;
	private JButton btnProcuraAtrativo;
	private JSpinner spDiasTroca;
	private Locale brasil = new Locale("pt", "BR");
	protected DateTimeZone zona = DateTimeZone.forID("Etc/GMT+3");
	protected DateTime data = new DateTime(zona);
	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy", brasil);
	
	FrmConsultaAtrativo consAtrativo = null;
	
	public FrmLancaTrocaAplicacao(FrmConsultaAtrativo consAtrativo) {
		this();
		this.consAtrativo = consAtrativo;
		
		txtCodigo.setText(String.valueOf(consAtrativo.atratBO.getCodigo()));
		spDiasTroca.setValue(consAtrativo.atratBO.getDiasTroca());
		
		btnConfirmar.setText("Alterar");
	}

	public FrmLancaTrocaAplicacao() {

		setTitle("Lança troca de Atrativo");
		setSize(600, 340);
		lblTitulo.setText("Troca de Atrativo");
		lblImg.setIcon(new ImageIcon(getClass().getResource("/icons/icon_atrativo.gif")));

		lblCodigo = new JLabel("C�digo");
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
		
		lblData = new JLabel("Data");
		lblData.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblData, constraints);
		
		txtData = new JFormattedTextField(mascaraData);
		txtData.setColumns(8);
		txtData.setText(df.format(data.toDate()));
		txtData.setFont(f2);
		txtData.requestFocusInWindow();
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtData, constraints);
		constraints.gridwidth = 1;

		lblCodAtrativo = new JLabel("Cód. Atrativo");
		lblCodAtrativo.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblCodAtrativo, constraints);

		txtCodAtrativo = new JTextField(5);
		txtCodAtrativo.setFont(f2);
		txtCodAtrativo.requestFocusInWindow();
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtCodAtrativo, constraints);
		
		txtMostraAtrativo = new JTextField(20);
		txtMostraAtrativo.setFont(f2);
		constraints.gridx = 2;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(txtMostraAtrativo, constraints);
		
		btnProcuraAtrativo = new JButton("Procurar", new ImageIcon(getClass().getResource("/icons/icon_procurar.gif")));
		btnProcuraAtrativo.setFocusable(false);
		constraints.ipady = -5;
		constraints.gridx = 3;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(btnProcuraAtrativo, constraints);
		constraints.ipady = 0;
		
		lblHistorico = new JLabel("Histórico");
		lblHistorico.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.NORTHEAST;
		painelMeio.add(lblHistorico, constraints);
		
		txtHistorico = new JTextArea(3, 20);
		txtHistorico.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		JScrollPane rolagemHist= new JScrollPane(txtHistorico);
		painelMeio.add(rolagemHist, constraints);
		constraints.gridwidth = 1;
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object origem = e.getSource();
		AtrativoBO atratBO = new AtrativoBO();
		AtrativoDao atratDao = new AtrativoDao();

		if (origem == btnConfirmar) {
			if (txtCodigo.getText().equals("-"))
				atratBO.setCodigo(0);
			else
				atratBO.setCodigo(Integer.parseInt(txtCodigo.getText()));

			try {
				atratBO.setDescricao(txtCodAtrativo.getText());
			} catch (StringVaziaException erro) {
				JOptionPane.showMessageDialog(this, "Descrição incorreto", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtCodAtrativo.requestFocus();
				return;
			}

			atratBO.setDiasTroca(Integer.parseInt(spDiasTroca.getValue().toString()));

			if (consAtrativo == null) {  // veio da inclus�o
				atratDao.incluir(atratBO);
				JOptionPane.showMessageDialog(this, "Registro salvo com sucesso!", "Registro Salvo", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
			} else {
				atratDao.alterar(atratBO);
				int linha = consAtrativo.tabela.getSelectedRow();
				consAtrativo.modelo.setValueAt(atratBO.getDescricao(),linha,1);
				consAtrativo.modelo.setValueAt(atratBO.getDiasTroca(),linha,2);
				JOptionPane.showMessageDialog(this, "Registro alterado com sucesso!", "Registro Alterado", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
			}
			doDefaultCloseAction();
		}

	}

}
