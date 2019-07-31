package pragas.vo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import gerais.vo.FrmLogin;
import gerais.vo.FrmMenuGeralPai;
import pragas.vo.especiais.FrmMandaEmailIndice;
import pragas.vo.filtrorelatorios.FrmRelatorioPorPragaGeral;
import pragas.vo.filtrorelatorios.FrmRelatorioPorPragaQuadra;
public class FrmMenuGeralPragas extends FrmMenuGeralPai implements ActionListener{
	private static final long serialVersionUID = 8626908390766881464L;
	private JButton btnConAtrativo, btnGrapholita, btnBonagota, btnMosca, btnLancaContagem, btnLancaTroca, btnTrocaModulo;

	private JMenu menuCadastros = new JMenu("Cadastros");
	private JMenu menuConsultas = new JMenu("Consultas");
	private JMenu menuMapas = new JMenu("Mapas de controle");
	private JMenu menuLancamentos = new JMenu("Lançamentos");
	private JMenu menuRelatorios = new JMenu("Relatorios");
	private JMenu menuEspeciais = new JMenu("Especiais");
	private JMenu menuSobre = new JMenu("Sobre");

	private JMenuItem itemCadAtrativo, itemConAtrativo, itemMapaGrapholita, itemMapaBonagota, itemMapaMosca, itemLancaContagem, itemLancaTroca,
	itemMandaEmail, itemRelGraficoIndiceMedio, itemRelGrafQuadra, itemDetalhes;

	public FrmMenuGeralPragas() {
		super();
		
		setTitle("Área de trabalho - Pragas");
		
		menuCadastros.setMnemonic('C');
		menuMapas.setMnemonic('p');
		menuLancamentos.setMnemonic('I');
		menuRelatorios.setMnemonic('R');
		menuEspeciais.setMnemonic('E');
		
		itemCadAtrativo = new JMenuItem("Atrativos", new ImageIcon(getClass().getResource("/icons/icon_atrativop.gif")));
		itemCadAtrativo.setMnemonic('a');
		menuCadastros.add(itemCadAtrativo);
		
		barraMenu.add(menuCadastros);
		
		itemConAtrativo = new JMenuItem("Atrativos", new ImageIcon(getClass().getResource("/icons/icon_atrativop.gif")));
		itemConAtrativo.setMnemonic('a');
		menuConsultas.add(itemConAtrativo);
		
		barraMenu.add(menuConsultas);
		
		itemMapaGrapholita = new JMenuItem("Grapholita", new ImageIcon(getClass().getResource("/icons/icon_grapholita_pp.gif")));
		itemMapaGrapholita.setMnemonic('G');
		itemMapaBonagota = new JMenuItem("Bonagota", new ImageIcon(getClass().getResource("/icons/icon_bonagota_pp.gif")));
		itemMapaBonagota.setMnemonic('B');
		itemMapaMosca = new JMenuItem("Mosca", new ImageIcon(getClass().getResource("/icons/icon_mosca_pp.gif")));
		itemMapaMosca.setMnemonic('M');
		menuMapas.add(itemMapaGrapholita);
		menuMapas.add(itemMapaBonagota);
		menuMapas.add(itemMapaMosca);
		
		barraMenu.add(menuMapas);
		
		itemLancaContagem = new JMenuItem("Contagem de insetos", new ImageIcon(getClass().getResource("/icons/icon_lancarp.gif")));
		itemLancaContagem.setMnemonic('C');
		itemLancaTroca = new JMenuItem("Troca de atrativo");
		itemLancaTroca.setMnemonic('T');
		menuLancamentos.add(itemLancaContagem);
//		menuLancamentos.add(itemLancaTroca);
		
		barraMenu.add(menuLancamentos);
		
		itemRelGraficoIndiceMedio = new JMenuItem("Gráfico índice médio geral", new ImageIcon(getClass().getResource("/icons/icon_graficop.gif")));
		itemRelGrafQuadra = new JMenuItem("Gráfico índice por quadra", new ImageIcon(getClass().getResource("/icons/icon_graficop.gif")));
		menuRelatorios.add(itemRelGraficoIndiceMedio);
		menuRelatorios.add(itemRelGrafQuadra);
		
		barraMenu.add(menuRelatorios);
		
		itemMandaEmail = new JMenuItem("Enviar e-mail com �ndices", new ImageIcon(getClass().getResource("/icons/icon_emailp.gif")));
		menuEspeciais.add(itemMandaEmail);
		
		barraMenu.add(menuEspeciais);
		
		itemDetalhes = new JMenuItem("Detalhes...");
		menuSobre.add(itemDetalhes);
		
		barraMenu.add(menuSobre);

		//-----------------------------------------------------------
		
		btnConAtrativo = new JButton(new ImageIcon(getClass().getResource("/icons/icon_atrativo.gif")));
		btnConAtrativo.setToolTipText("Consulta de Atrativos");
		
		btnGrapholita = new JButton(new ImageIcon(getClass().getResource("/icons/icon_grapholita.gif")));
		btnGrapholita.setToolTipText("Mapa Grapholita");
		
		btnBonagota = new JButton(new ImageIcon(getClass().getResource("/icons/icon_bonagota.gif")));
		btnBonagota.setToolTipText("Mapa Bonagota");
		
		btnMosca = new JButton(new ImageIcon(getClass().getResource("/icons/icon_mosca.gif")));
		btnMosca.setToolTipText("Mapa Mosca");
		
		btnLancaContagem = new JButton(new ImageIcon(getClass().getResource("/icons/icon_lancar.gif")));
		btnLancaContagem.setToolTipText("Lan�ar Contagem");
		
		btnLancaTroca = new JButton("TESTE");
		btnLancaTroca.setToolTipText("Lançar troca de atrativo");
		
		barra.add(btnConAtrativo);
		barra.addSeparator();
		barra.add(btnGrapholita);
		barra.add(btnBonagota);
		barra.add(btnMosca);
		barra.addSeparator();
		barra.add(btnLancaContagem);
//		barra.add(btnLancaTroca);
		
		btnTrocaModulo = new JButton(new ImageIcon(getClass().getResource("/icons/icon_troca_modulo.gif")));
		btnTrocaModulo.setToolTipText("Trocar Módulo");
		
		barra.add(Box.createHorizontalGlue());
		barra.add(btnTrocaModulo);

		btnConAtrativo.addActionListener(this);
		btnGrapholita.addActionListener(this);
		btnBonagota.addActionListener(this);
		btnMosca.addActionListener(this);
		btnLancaContagem.addActionListener(this);
		btnLancaTroca.addActionListener(this);
		btnTrocaModulo.addActionListener(this);
		itemCadAtrativo.addActionListener(this);
		itemConAtrativo.addActionListener(this);
		itemMapaGrapholita.addActionListener(this);
		itemMapaBonagota.addActionListener(this);
		itemMapaMosca.addActionListener(this);
		itemLancaContagem.addActionListener(this);
		itemMandaEmail.addActionListener(this);
		itemRelGraficoIndiceMedio.addActionListener(this);
		itemRelGrafQuadra.addActionListener(this);
		itemDetalhes.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object origem = e.getSource();
		if (origem == itemCadAtrativo) {
			FrmCadastraAtrativo fr = new FrmCadastraAtrativo();
			fr.setVisible(true);
			dPane.add(fr);  
			try {
				fr.setSelected(true);
			} catch (PropertyVetoException exc) { }
			centralizaInternalFrame(fr, dPane);
		} else if (origem == btnConAtrativo || origem == itemConAtrativo) {
			FrmConsultaAtrativo fr = new FrmConsultaAtrativo();
			fr.setVisible(true);
			dPane.add(fr);
			try {
				fr.setSelected(true);
			} catch (PropertyVetoException exc) { }
			centralizaInternalFrame(fr, dPane);
		} else if (origem == btnGrapholita || origem == itemMapaGrapholita) {
			FrmMapaGrapholita fr = new FrmMapaGrapholita();
			fr.setVisible(true);
			dPane.add(fr);
			try {
				fr.setSelected(true);
			} catch (PropertyVetoException exc) { }
			centralizaInternalFrame(fr, dPane);
		} else if (origem == btnBonagota || origem == itemMapaBonagota) {
			FrmMapaBonagota fr = new FrmMapaBonagota();
			fr.setVisible(true);
			dPane.add(fr);
			try {
				fr.setSelected(true);
			} catch (PropertyVetoException exc) { }
			centralizaInternalFrame(fr, dPane);
		} else if (origem == btnMosca || origem == itemMapaMosca) {
			FrmMapaMosca fr = new FrmMapaMosca();
			fr.setVisible(true);
			dPane.add(fr);
			try {
				fr.setSelected(true);
			} catch (PropertyVetoException exc) { }
			centralizaInternalFrame(fr, dPane);
		} else if (origem == itemLancaContagem || origem == btnLancaContagem) {
			FrmConsultaContagem fr = new FrmConsultaContagem();
			fr.setVisible(true);
			dPane.add(fr);
			try {
				fr.setSelected(true);
			} catch (PropertyVetoException exc) { }
			centralizaInternalFrame(fr, dPane);
		} else if (origem == itemLancaTroca || origem == btnLancaTroca) {
			FrmLancaTrocaAplicacao fr = new FrmLancaTrocaAplicacao();
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
			fr.cbModulo.removeItem("Controle de Pragas");
		} else if (origem == itemMandaEmail) {
			FrmMandaEmailIndice fr = new FrmMandaEmailIndice();
			fr.setVisible(true);
			dPane.add(fr);
			try {
				fr.setSelected(true);
			} catch (PropertyVetoException exc) { }
			centralizaInternalFrame(fr, dPane);
		} else if (origem == itemRelGraficoIndiceMedio) {
			FrmRelatorioPorPragaGeral fr = new FrmRelatorioPorPragaGeral();
			fr.setVisible(true);
			dPane.add(fr);  
			try {
				fr.setSelected(true);
			} catch (PropertyVetoException exc) { }
			centralizaInternalFrame(fr, dPane);
		} else if (origem == itemRelGrafQuadra) {
			FrmRelatorioPorPragaQuadra fr = new FrmRelatorioPorPragaQuadra();
			fr.setVisible(true);
			dPane.add(fr);  
			try {
				fr.setSelected(true);
			} catch (PropertyVetoException exc) { }
			centralizaInternalFrame(fr, dPane);
		} else if (origem == itemDetalhes) {
			FrmSobrePragas fr = new FrmSobrePragas();
			fr.setVisible(true);
		}
	}
}
