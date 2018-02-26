package safristas.vo.filtrorelatorios;

import impressos.RelatoriosSafristas;

import java.awt.Cursor;
import java.awt.GridBagConstraints;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import gerais.vo.FrmRelatorioPai;

public class FrmRelatorioEmpregadosVisaoGeral extends FrmRelatorioPai {
	private static final long serialVersionUID = -6026521613365692984L;

	protected GridBagConstraints constraints = new GridBagConstraints();

	protected JLabel lblTipo, lblValor;
	protected JTextField txtNome;
	protected JRadioButton rBtnGeral, rBtnComFiltros;
	protected ButtonGroup grupo;

	public FrmRelatorioEmpregadosVisaoGeral() {
		setTitle("Relatório de Empregados");
		lblTitulo.setText("Relatório de Empregados - Visão Geral");

		lblTipo = new JLabel("Tipo");
		lblTipo.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblTipo, constraints);
		
		rBtnGeral = new JRadioButton("Geral", true);
		rBtnGeral.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(rBtnGeral, constraints);
		
		rBtnComFiltros = new JRadioButton("Com Filtro", false);
		rBtnComFiltros.setFont(f2);
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(rBtnComFiltros, constraints);
		
		grupo = new ButtonGroup();
		grupo.add(rBtnGeral);
		grupo.add(rBtnComFiltros);
	}

	@Override
	public void confirmar() {
		RelatoriosSafristas rel = new RelatoriosSafristas();
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		rel.RelatorioEmpregadoVisaoGeral();
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
}
