package insumos.vo.filtrorelatorios;

import impressos.RelatorioInsumos;
import impressos.RelatoriosSafristas;
import safristas.bo.safristas.EmpreiteiroBO;
import safristas.dao.safristas.EmpreiteiroDao;
import safristas.vo.FrmMenuGeralMaca;
import safristas.vo.consultcadast.FrmConsultaEmpreiteiro;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyVetoException;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import gerais.vo.FrmRelatorioPai;

public class FrmRelatorioVisaoGeralLavouras extends FrmRelatorioPai {
	private static final long serialVersionUID = 8184591208670585466L;

	protected GridBagConstraints constraints = new GridBagConstraints();

	protected JLabel lblTipo, lblCodEmpreiteiro;
	public JTextField txtCodEmpreiteiro, txtMostraEmpreiteiro;
	protected JRadioButton rBtnLavoura, rBtnDataAplicacao, rBtnInsumo, rBtnDataFinal;
	protected JButton btnProcuraEmpreiteiro;
	protected ButtonGroup grupo;
	public EmpreiteiroBO iroBO;
	protected EmpreiteiroDao iroDao = new EmpreiteiroDao();

	public FrmRelatorioVisaoGeralLavouras() {
		setTitle("Relatório - Visão Geral Lavouras - Residual");
		setSize(getWidth()+60, getHeight());
		lblTitulo.setText("Relatório Visão Geral Lavouras");
		constraints.insets = new Insets(2, 2, 2, 2);
		
		lblTipo = new JLabel("Ordenar por:");
		lblTipo.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblTipo, constraints);
		
		rBtnLavoura = new JRadioButton("Lavoura", true);
		rBtnLavoura.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(rBtnLavoura, constraints);
		constraints.gridwidth = 1;
		
		rBtnDataAplicacao = new JRadioButton("Data aplicação", false);
		rBtnDataAplicacao.setFont(f2);
		constraints.gridx = 3;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(rBtnDataAplicacao, constraints);
		
		rBtnInsumo = new JRadioButton("Insumo", false);
		rBtnInsumo.setFont(f2);
		constraints.gridx = 4;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(rBtnInsumo, constraints);
		
		rBtnDataFinal = new JRadioButton("Data Final", false);
		rBtnDataFinal.setFont(f2);
		constraints.gridx = 5;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(rBtnDataFinal, constraints);
		
		grupo = new ButtonGroup();
		grupo.add(rBtnLavoura);
		grupo.add(rBtnDataAplicacao);
		grupo.add(rBtnInsumo);
		grupo.add(rBtnDataFinal);
		
	}

	@Override
	public void confirmar() {
		RelatorioInsumos rel = new RelatorioInsumos();
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		if (rBtnLavoura.isSelected()) {
			rel.relatorioVisaoGeralLavouras("apl_nrolavoura");
		} else if (rBtnDataAplicacao.isSelected()) {
			rel.relatorioVisaoGeralLavouras("data_aplicacao");
		} else if (rBtnInsumo.isSelected()) {
			rel.relatorioVisaoGeralLavouras("ins_descricao");
		} else if (rBtnDataFinal.isSelected()) {
			rel.relatorioVisaoGeralLavouras("data_final");
		}
			
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
}
