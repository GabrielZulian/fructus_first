package safristas.vo.filtrorelatorios;

import impressos.RelatoriosSafristas;
import safristas.bo.outros.PagamentoTotalOutrosBO;
import safristas.bo.safristas.PagamentoTotalBO;
import safristas.dao.outros.PagamentoTotalOutrosDao;
import safristas.dao.safristas.PagamentoTotalDao;
import safristas.vo.FrmMenuGeralMaca;
import safristas.vo.consultcadast.FrmConsultaPagamento;
import safristas.vo.consultcadast.FrmConsultaPagamentoOutros;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyVetoException;
import java.text.DecimalFormat;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import gerais.vo.FrmRelatorioPai;

public class FrmRelatorioPagamentos extends FrmRelatorioPai {
	private static final long serialVersionUID = -9181747584561580375L;

	protected DecimalFormat decimal = new DecimalFormat("#,##0.00");
	protected JLabel lblTipoPgto, lblCodigoPagamento;
	public JTextField txtCodPagamento, txtMostraPagamento;
	protected JFormattedTextField txtDataInicial, txtDataFinal;
	protected JRadioButton rBtnSafristas, rBtnOutros;
	protected ButtonGroup grupo;
	protected JButton btnProcuraPagamento;
	protected PagamentoTotalDao PgtoTotalDao = new PagamentoTotalDao();
	protected PagamentoTotalOutrosDao PgtoTotalOutDao = new PagamentoTotalOutrosDao();

	protected PagamentoTotalBO pgTotalBO;
	protected PagamentoTotalOutrosBO pgTotalOutBO;

	public FrmRelatorioPagamentos() {
		setTitle("Relatório de Pagamentos");
		lblTitulo.setText("Pagamentos");
		setSize(getWidth()+40, getHeight()-50);

		lblTipoPgto = new JLabel("Tipo Pagamento");
		lblTipoPgto.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblTipoPgto, constraints);

		rBtnSafristas = new JRadioButton("Safristas", true);
		rBtnSafristas.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(rBtnSafristas, constraints);
		constraints.gridwidth = 1;

		rBtnOutros = new JRadioButton("Outros", false);
		rBtnOutros.setFont(f2);
		constraints.gridx = 3;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(rBtnOutros, constraints);

		grupo = new ButtonGroup();
		grupo.add(rBtnSafristas);
		grupo.add(rBtnOutros);

		lblCodigoPagamento = new JLabel("Código pagamento");
		lblCodigoPagamento.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblCodigoPagamento, constraints);

		txtCodPagamento = new JTextField(4);
		txtCodPagamento.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtCodPagamento, constraints);
		txtCodPagamento.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				if (rBtnSafristas.isSelected()) {
					pgTotalBO = new PagamentoTotalBO();
					pgTotalBO = PgtoTotalDao.consultaPorCodigo(Integer.parseInt(txtCodPagamento.getText())).get(0);

					txtMostraPagamento.setText(pgTotalBO.data.toString("dd/MM/yyyy") + " - " + decimal.format(pgTotalBO.getValorTotal()));
				} else if (rBtnOutros.isSelected()) {
					pgTotalOutBO = new PagamentoTotalOutrosBO();
					pgTotalOutBO = PgtoTotalOutDao.consultaPorCodigo(Integer.parseInt(txtCodPagamento.getText())).get(0);
					
					txtMostraPagamento.setText(pgTotalOutBO.data.toString("dd/MM/yyyy") + " - " + decimal.format(pgTotalOutBO.getValorTotal()));
				}
			}
		});

		txtMostraPagamento = new JTextField(15);
		txtMostraPagamento.setFont(f2);
		txtMostraPagamento.setEditable(false);
		txtMostraPagamento.setFocusable(false);
		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtMostraPagamento, constraints);
		constraints.gridwidth = 1;

		btnProcuraPagamento = new JButton("Procurar", new ImageIcon(getClass().getResource("/icons/icon_procurar.gif")));
		btnProcuraPagamento.setFocusable(false);
		constraints.ipady = -5;
		constraints.gridx = 4;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(btnProcuraPagamento, constraints);
		constraints.ipady = 0;

		btnProcuraPagamento.addActionListener(this);
	}

	@Override
	public void confirmar() {
		int codigoPgto = Integer.parseInt(txtCodPagamento.getText());
		RelatoriosSafristas rel = new RelatoriosSafristas();
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		if (rBtnSafristas.isSelected()) {
			rel.RelatorioPagamentoSafristas(codigoPgto);
		} else if (rBtnOutros.isSelected()) {
			rel.RelatorioPagamentoOutros(codigoPgto);
		}
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	public void actionPerformed(ActionEvent e) {
		Object origem = e.getSource();
		super.actionPerformed(e);
		if (origem == btnProcuraPagamento) {
			if (rBtnSafristas.isSelected()) {
				FrmConsultaPagamento fr = new FrmConsultaPagamento(this);
				fr.setVisible(true);
				getDesktopPane().add(fr);
				try {
					fr.setSelected(true);
				}
				catch (PropertyVetoException exc) { }
				FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
			} else if (rBtnOutros.isSelected()) {
				FrmConsultaPagamentoOutros fr = new FrmConsultaPagamentoOutros(this);
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
}
