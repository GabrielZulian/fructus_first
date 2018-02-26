package insumos.vo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import gerais.vo.FrmLogin;
import gerais.vo.FrmMenuGeralPai;
import insumos.vo.filtrorelatorios.FrmRelatorioAplicacao;
import insumos.vo.filtrorelatorios.FrmRelatorioVisaoGeralLavouras;
public class FrmMenuGeralInsumos extends FrmMenuGeralPai implements ActionListener {
	private static final long serialVersionUID = 8626908390766881464L;
	private JButton btnConLavoura, btnConInsumo, btnConAplicacao, btnVisaoGeral, btnTrocaModulo;

	private JMenu menuCadastros = new JMenu("Cadastros");
	private JMenu menuConsultas = new JMenu("Consultas");
	private JMenu menuLancamentos = new JMenu("Lançamentos");
	private JMenu menuRelatorios = new JMenu("Relatorios");
	private JMenu menuEspeciais = new JMenu("Especiais");
	private JMenu menuSobre = new JMenu("Sobre");

	private JMenuItem itemCadTalhao, itemCadInsumo, itemConTalhoes, itemConInsumo, itemAplicacao, itemRelAplicacao,
	itemRelVisaoGeralTalhoes, itemDetalhes;

	public FrmMenuGeralInsumos() {
		super();
		setTitle("Área de trabalho - Insumos");
		menuCadastros.setMnemonic('C');
		menuLancamentos.setMnemonic('L');
		menuRelatorios.setMnemonic('R');
		menuEspeciais.setMnemonic('E');
		
		itemCadTalhao = new JMenuItem("Talhões", new ImageIcon(getClass().getResource("/icons/icon_lavourap.gif")));
		itemCadTalhao.setMnemonic('L');
		
		itemCadInsumo = new JMenuItem("Insumos", new ImageIcon(getClass().getResource("/icons/icon_insumop.gif")));
		itemCadInsumo.setMnemonic('I');
		
		menuCadastros.add(itemCadTalhao);
		menuCadastros.add(itemCadInsumo);
		barraMenu.add(menuCadastros);
		
		itemConTalhoes = new JMenuItem("Talhões", new ImageIcon(getClass().getResource("/icons/icon_lavourap.gif")));
		itemConTalhoes.setMnemonic('L');
		
		itemConInsumo = new JMenuItem("Insumos", new ImageIcon(getClass().getResource("/icons/icon_insumop.gif")));
		itemConInsumo.setMnemonic('I');
		
		menuConsultas.add(itemConTalhoes);
		menuConsultas.add(itemConInsumo);
		
		barraMenu.add(menuConsultas);
		
		itemAplicacao = new JMenuItem("Aplicação");
		itemAplicacao.setMnemonic('p');
		
		menuLancamentos.add(itemAplicacao);
		barraMenu.add(menuLancamentos);
		
		itemRelAplicacao = new JMenuItem("Aplicações");
		itemRelAplicacao.setMnemonic('p');
		
		itemRelVisaoGeralTalhoes = new JMenuItem("Visão Geral Talhões - Residual");
		itemRelVisaoGeralTalhoes.setMnemonic('V');
		
		menuRelatorios.add(itemRelAplicacao);	
		menuRelatorios.add(itemRelVisaoGeralTalhoes);
		barraMenu.add(menuRelatorios);
		
		itemDetalhes = new JMenuItem("Detalhes...");
		
		menuSobre.add(itemDetalhes);
		barraMenu.add(menuSobre);

		//-----------------------------------------------------------
		
		btnConLavoura = new JButton(new ImageIcon(getClass().getResource("/icons/icon_lavoura.gif")));
		btnConLavoura.setToolTipText("Consulta Talhões");
		
		btnConInsumo = new JButton(new ImageIcon(getClass().getResource("/icons/icon_insumo.gif")));
		btnConInsumo.setToolTipText("Consulta Insumos");
		
		btnConAplicacao = new JButton(new ImageIcon(getClass().getResource("/icons/icon_aplicacao.gif")));
		btnConAplicacao.setToolTipText("Consulta Aplicações");
		
		barra.add(btnConLavoura);
		barra.add(btnConInsumo);
		barra.addSeparator();
		barra.add(btnConAplicacao);
		btnVisaoGeral = new JButton(new ImageIcon(getClass().getResource("/icons/icon_visao_lavouras.gif")));
		btnVisaoGeral.setToolTipText("Visão geral talhões");
		barra.add(btnVisaoGeral);
		btnVisaoGeral.addActionListener(this);
		
		barra.addSeparator();
		
		btnTrocaModulo = new JButton(new ImageIcon(getClass().getResource("/icons/icon_troca_modulo.gif")));
		btnTrocaModulo.setToolTipText("Trocar Módulo");
		
		barra.add(Box.createHorizontalGlue());
		barra.add(btnTrocaModulo);

		btnConLavoura.addActionListener(this);
		btnConInsumo.addActionListener(this);
		btnConAplicacao.addActionListener(this);
		btnTrocaModulo.addActionListener(this);
		itemCadTalhao.addActionListener(this);
		itemCadInsumo.addActionListener(this);
		itemConTalhoes.addActionListener(this);
		itemConInsumo.addActionListener(this);
		itemAplicacao.addActionListener(this);
		itemRelAplicacao.addActionListener(this);
		itemRelVisaoGeralTalhoes.addActionListener(this);
		itemDetalhes.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object origem = e.getSource();
		if (origem == btnConLavoura || origem == itemConTalhoes) {
			FrmConsultaTalhao fr = new FrmConsultaTalhao();
			fr.setVisible(true);
			dPane.add(fr);  
			try {
				fr.setSelected(true);
			} catch (PropertyVetoException exc) { }
			centralizaInternalFrame(fr, dPane);
		} else if (origem == btnConInsumo || origem == itemConInsumo) {
			FrmConsultaInsumo fr = new FrmConsultaInsumo();
			fr.setVisible(true);
			dPane.add(fr);  
			try {
				fr.setSelected(true);
			} catch (PropertyVetoException exc) { }
			centralizaInternalFrame(fr, dPane);
		} else if (origem == itemCadInsumo) {
			FrmCadastraInsumo fr = new FrmCadastraInsumo();
			fr.setVisible(true);
			dPane.add(fr);  
			try {
				fr.setSelected(true);
			} catch (PropertyVetoException exc) { }
			centralizaInternalFrame(fr, dPane);
		} else if (origem == itemCadTalhao) {
			FrmCadastraTalhao fr = new FrmCadastraTalhao();
			fr.setVisible(true);
			dPane.add(fr);  
			try {
				fr.setSelected(true);
			} catch (PropertyVetoException exc) { }
			centralizaInternalFrame(fr, dPane);
		} else if (origem == btnConAplicacao) {
			FrmConsultaAplicacao fr = new FrmConsultaAplicacao();
			fr.setVisible(true);
			dPane.add(fr);
			try {
				fr.setSelected(true);
			} catch (PropertyVetoException exc) { }
			centralizaInternalFrame(fr, dPane);
		} else if (origem == btnVisaoGeral) {
			FrmVisaoGeralTalhoes fr = new FrmVisaoGeralTalhoes();
			fr.setVisible(true);
			dPane.add(fr);
			try {
				fr.setMaximum(true);
			} catch (PropertyVetoException e1) {
				JOptionPane.showMessageDialog(null, "Erro ao abrir a tela", "ERRO", JOptionPane.ERROR_MESSAGE);
				}
			try {
				fr.setSelected(true);
			} catch (PropertyVetoException exc) { }
			centralizaInternalFrame(fr, dPane);
		} else if (origem == itemAplicacao) {
			FrmLancaAplicacao fr = new FrmLancaAplicacao();
			fr.setVisible(true);
			dPane.add(fr);  
			try {
				fr.setSelected(true);
			} catch (PropertyVetoException exc) { }
			centralizaInternalFrame(fr, dPane);
		} else if (origem == itemRelAplicacao) {
			FrmRelatorioAplicacao fr = new FrmRelatorioAplicacao();
			fr.setVisible(true);
			dPane.add(fr);
			try {
				fr.setSelected(true);
			} catch (PropertyVetoException exc) { }
			centralizaInternalFrame(fr, dPane);
		} else if (origem == itemRelVisaoGeralTalhoes) {
			FrmRelatorioVisaoGeralLavouras fr = new FrmRelatorioVisaoGeralLavouras();
			fr.setVisible(true);
			dPane.add(fr);
			try {
				fr.setSelected(true);
			} catch (PropertyVetoException exc) { }
			centralizaInternalFrame(fr, dPane);
		} else if (origem == btnTrocaModulo) {
			FrmLogin fr = new FrmLogin();
			fr.setVisible(true);
			fr.fecha = false;
			fr.cbModulo.removeItem("Controle de Insumos");
		} else if (origem == itemDetalhes) {
			FrmSobreInsumos fr = new FrmSobreInsumos();
			fr.setVisible(true);
		}
	}
}