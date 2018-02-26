package safristas.vo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import gerais.vo.FrmLogin;
import gerais.vo.FrmMenuGeralPai;
import safristas.vo.consultcadast.FrmCadastraCidade;
import safristas.vo.consultcadast.FrmCadastraEmpregado;
import safristas.vo.consultcadast.FrmCadastraAtrativo;
import safristas.vo.consultcadast.FrmCadastraEmpreiteiro;
import safristas.vo.consultcadast.FrmCadastraEquipe;
import safristas.vo.consultcadast.FrmCadastraFuncao;
import safristas.vo.consultcadast.FrmCadastraVeiculo;
import safristas.vo.consultcadast.FrmConsultaCidade;
import safristas.vo.consultcadast.FrmConsultaDia;
import safristas.vo.consultcadast.FrmConsultaDiaOutros;
import safristas.vo.consultcadast.FrmConsultaEmpregado;
import safristas.vo.consultcadast.FrmConsultaEmpregador;
import safristas.vo.consultcadast.FrmConsultaEmpreiteiro;
import safristas.vo.consultcadast.FrmConsultaEquipe;
import safristas.vo.consultcadast.FrmConsultaFuncao;
import safristas.vo.consultcadast.FrmConsultaPagamento;
import safristas.vo.consultcadast.FrmConsultaPagamentoOutros;
import safristas.vo.consultcadast.FrmConsultaQuadra;
import safristas.vo.consultcadast.FrmConsultaVeiculo;
import safristas.vo.especiais.FrmVisaoGeral;
import safristas.vo.filtrorelatorios.FrmPlanilhaColetaDias;
import safristas.vo.filtrorelatorios.FrmRelatorioDiasTrabalhados;
import safristas.vo.filtrorelatorios.FrmRelatorioEmpregadores;
import safristas.vo.filtrorelatorios.FrmRelatorioEmpregados;
import safristas.vo.filtrorelatorios.FrmRelatorioEmpregadosVisaoGeral;
import safristas.vo.filtrorelatorios.FrmRelatorioEmpreiteiros;
import safristas.vo.filtrorelatorios.FrmRelatorioPagamentos;
import safristas.vo.lancamentos.FrmConsultaAdiantamentos;

public class FrmMenuGeralMaca extends FrmMenuGeralPai implements ActionListener {
	private static final long serialVersionUID = 5982691896222331578L;
	
	private JButton btnEmpregadores, btnEmpreiteiros, btnEmpregados, btnEquipes, btnFuncoes, btnVeiculos, btnCidades, btnQuadras,
	btnLancaDia, btnLancaDiaOut, btnPagamento, btnPagamentoOut, btnTrocaModulo;

	private JMenu menuCadastros = new JMenu("Cadastros");
	private JMenu menuConsultas = new JMenu("Consultas");
	private JMenu menuPag = new JMenu("Lançamentos");
	private JMenu menuDiaTrabalho = new JMenu("Dia de trabalho");
	private JMenu menuPagamento = new JMenu("Pagamento/Fechamento");
	private JMenu menuRelatorio = new JMenu("Relatórios");
	private JMenu menuEspeciais = new JMenu("Especiais");
	private JMenu menuPlanilhas = new JMenu("Planilhas");
	private JMenu menuRelEmpregados = new JMenu("Empregados");
	private JMenu menuRelDiasTrabalhados = new JMenu("Dias trabalho");
	private JMenu menuSobre = new JMenu("Sobre");
	
	private JMenuItem itemEmpregadorCad, itemEmpreiteiroCad, itemEmpregadoCad, itemEquipeCad, itemCidadeCad, itemFuncaoCad, itemVeiculoCad, itemQuadraCad,
	itemEmpregadorCon, itemEmpreiteiroCon, itemEmpregadoCon, itemEquipeCon, itemCidadeCon, itemFuncaoCon, itemVeiculoCon, itemQuadraCon,
	itemLancaDia, itemLançaDiaOut, itemFechamento, itemFechamentoOut, itemAdiantamentos, itemRelEmpregadores, itemRelEmpreiteiros, itemRelEmpregadosSimples,
	itemRelEmpregadosVisaoGeral, itemRelPagamentos, itemPlanilhaColetaDias, itemVisaoGeral, itemRelDiasTrabalho, itemDetalhes;
	
	public FrmMenuGeralMaca() {
		super();
		
		setTitle("Área de trabalho - Colheita maçã");
		
		itemEmpregadorCad = new JMenuItem("Empregadores", new ImageIcon(getClass().getResource("/icons/icon_empregadorp.gif")));
		itemEmpreiteiroCad = new JMenuItem("Empreiteiros", new ImageIcon(getClass().getResource("/icons/icon_empreiteirop.gif")));
		itemEmpregadoCad = new JMenuItem("Empregados", new ImageIcon(getClass().getResource("/icons/icon_empregadop.gif")));
		itemEquipeCad = new JMenuItem("Equipes", new ImageIcon(getClass().getResource("/icons/icon_equipep.gif")));
		itemCidadeCad = new JMenuItem("Cidades", new ImageIcon(getClass().getResource("/icons/icon_cidadep.gif")));
		itemFuncaoCad = new JMenuItem("Funções", new ImageIcon(getClass().getResource("/icons/icon_funcaop.gif")));
		itemVeiculoCad = new JMenuItem("Veículos", new ImageIcon(getClass().getResource("/icons/icon_veiculop.gif")));
		itemQuadraCad = new JMenuItem("Quadras", new ImageIcon(getClass().getResource("/icons/icon_quadrap.gif")));

		menuCadastros.add(itemEmpregadorCad);
		menuCadastros.add(itemEmpreiteiroCad);
		menuCadastros.add(itemEmpregadoCad);
		menuCadastros.addSeparator();
		menuCadastros.add(itemEquipeCad);
		menuCadastros.add(itemFuncaoCad);
		menuCadastros.add(itemVeiculoCad);
		menuCadastros.add(itemQuadraCad);
		menuCadastros.add(itemCidadeCad);
		menuCadastros.setMnemonic(KeyEvent.VK_C);

		barraMenu.add(menuCadastros);

		itemEmpregadorCon = new JMenuItem("Empregadores", new ImageIcon(getClass().getResource("/icons/icon_empregadorp.gif")));
		itemEmpreiteiroCon = new JMenuItem("Empreiteiros", new ImageIcon(getClass().getResource("/icons/icon_empreiteirop.gif")));
		itemEmpregadoCon = new JMenuItem("Empregados", new ImageIcon(getClass().getResource("/icons/icon_empregadop.gif")));
		itemEquipeCon = new JMenuItem("Equipes", new ImageIcon(getClass().getResource("/icons/icon_equipep.gif")));
		itemCidadeCon = new JMenuItem("Cidades", new ImageIcon(getClass().getResource("/icons/icon_cidadep.gif")));
		itemFuncaoCon = new JMenuItem("Funções", new ImageIcon(getClass().getResource("/icons/icon_funcaop.gif")));
		itemVeiculoCon = new JMenuItem("Veículos", new ImageIcon(getClass().getResource("/icons/icon_veiculop.gif")));
		itemQuadraCon = new JMenuItem("Quadras", new ImageIcon(getClass().getResource("/icons/icon_quadrap.gif")));
		
		menuConsultas.add(itemEmpregadorCon);
		menuConsultas.add(itemEmpreiteiroCon);
		menuConsultas.add(itemEmpregadoCon);
		menuConsultas.addSeparator();
		menuConsultas.add(itemEquipeCon);
		menuConsultas.add(itemFuncaoCon);
		menuConsultas.add(itemVeiculoCon);
		menuConsultas.add(itemQuadraCon);
		menuConsultas.add(itemCidadeCon);
		menuConsultas.setMnemonic(KeyEvent.VK_O);

		barraMenu.add(menuConsultas);

		itemLancaDia = new JMenuItem("Safristas", new ImageIcon(getClass().getResource("/icons/icon_lanca_dia_frutp.gif")));
		itemLançaDiaOut = new JMenuItem("Outros", new ImageIcon(getClass().getResource("/icons/icon_lanca_dia_motp.gif")));
		itemFechamento = new JMenuItem("Safristas", new ImageIcon(getClass().getResource("/icons/icon_pagamento_maca2p.gif")));
		itemFechamentoOut = new JMenuItem("Outros", new ImageIcon(getClass().getResource("/icons/icon_pagamento_motorista2p.gif")));
		itemAdiantamentos = new JMenuItem("Adiantamentos", new ImageIcon(getClass().getResource("/icons/icon_adiantamentop.gif")));
		
		menuDiaTrabalho.add(itemLancaDia);
		menuDiaTrabalho.add(itemLançaDiaOut);
		menuPagamento.add(itemFechamento);
		menuPagamento.add(itemFechamentoOut);
		
		menuPag.add(menuDiaTrabalho);
		menuPag.add(menuPagamento);
		menuPag.add(itemAdiantamentos);
		menuPag.setMnemonic(KeyEvent.VK_L);

		barraMenu.add(menuPag);

		itemRelEmpregadores = new JMenuItem("Empregadores", new ImageIcon(getClass().getResource("/icons/icon_empregadorp.gif")));
		itemRelEmpreiteiros = new JMenuItem("Empreiteiros", new ImageIcon(getClass().getResource("/icons/icon_empreiteirop.gif")));
		itemRelEmpregadosSimples = new JMenuItem("Simples", new ImageIcon(getClass().getResource("/icons/icon_empregadop.gif")));
		itemRelEmpregadosVisaoGeral = new JMenuItem("Visão geral", new ImageIcon(getClass().getResource("/icons/icon_equipep.gif")));
		itemRelPagamentos = new JMenuItem("Pagamentos", new ImageIcon(getClass().getResource("/icons/icon_pagamento.gif")));
		itemRelDiasTrabalho = new JMenuItem("Dias trabalhados");
		itemPlanilhaColetaDias = new JMenuItem("Planilha p/ coleta de dias", new ImageIcon(getClass().getResource("/icons/icon_relatoriop.gif")));
		
		menuPlanilhas.add(itemPlanilhaColetaDias);
		menuRelEmpregados.add(itemRelEmpregadosSimples);
		menuRelEmpregados.add(itemRelEmpregadosVisaoGeral);
		
		menuRelatorio.add(itemRelEmpregadores);
		menuRelatorio.add(itemRelEmpreiteiros);
		menuRelatorio.add(menuRelEmpregados);
		menuRelatorio.add(itemRelPagamentos);
		menuRelatorio.add(itemRelDiasTrabalho);
		menuRelatorio.addSeparator();
		menuRelatorio.add(menuPlanilhas);
		menuRelatorio.setMnemonic(KeyEvent.VK_R);
		
		barraMenu.add(menuRelatorio);
		
		itemVisaoGeral = new JMenuItem("Visão Geral");
		menuEspeciais.add(itemVisaoGeral);
		menuEspeciais.setMnemonic(KeyEvent.VK_E);
		barraMenu.add(menuEspeciais);
		
		itemDetalhes = new JMenuItem("Detalhes...");
		menuSobre.add(itemDetalhes);
		menuSobre.setMnemonic(KeyEvent.VK_S);
		barraMenu.add(menuSobre);
		
		//-----------------------------------------------------------

		btnEmpregadores = new JButton(new ImageIcon(getClass().getResource("/icons/icon_empregador.gif")));
		btnEmpregadores.setToolTipText("Consulta de Empregadores");
		btnEmpreiteiros = new JButton(new ImageIcon(getClass().getResource("/icons/icon_empreiteiro.gif")));
		btnEmpreiteiros.setToolTipText("Consulta de Empreiteiros");
		btnEmpregados = new JButton(new ImageIcon(getClass().getResource("/icons/icon_empregado.gif")));
		btnEmpregados.setToolTipText("Consulta de Empregados");
		btnEquipes = new JButton(new ImageIcon(getClass().getResource("/icons/icon_equipe.gif")));
		btnEquipes.setToolTipText("Consulta de Equipes");
		btnFuncoes = new JButton(new ImageIcon(getClass().getResource("/icons/icon_funcao.gif")));
		btnFuncoes.setToolTipText("Consulta de Funções");
		btnVeiculos = new JButton(new ImageIcon(getClass().getResource("/icons/icon_veiculo.gif")));
		btnVeiculos.setToolTipText("Consulta de Veículos");
		btnQuadras = new JButton(new ImageIcon(getClass().getResource("/icons/icon_quadra.gif")));
		btnQuadras.setToolTipText("Consulta de Quadras");
		btnCidades = new JButton(new ImageIcon(getClass().getResource("/icons/icon_cidade.gif")));
		btnCidades.setToolTipText("Consulta de Cidades");

		btnLancaDia = new JButton(new ImageIcon(getClass().getResource("/icons/icon_lanca_dia_frut.gif")));
		btnLancaDia.setToolTipText("Lançar dia trabalho Safristas");
		btnLancaDiaOut = new JButton(new ImageIcon(getClass().getResource("/icons/icon_lanca_dia_mot.gif")));
		btnLancaDiaOut.setToolTipText("Lançar dia trabalho Outros");
		btnPagamento = new JButton(new ImageIcon(getClass().getResource("/icons/icon_pagamento_maca2.gif")));
		btnPagamento.setToolTipText("Lançar pagamento Safristas");
		btnPagamentoOut = new JButton(new ImageIcon(getClass().getResource("/icons/icon_pagamento_motorista2.gif")));
		btnPagamentoOut.setToolTipText("Lançar pagamento Outros");

		barra.add(btnEmpregadores);
		barra.add(btnEmpreiteiros);
		barra.add(btnEmpregados);
		barra.addSeparator();
		barra.add(btnEquipes);
		barra.add(btnFuncoes);
		barra.add(btnVeiculos);
		barra.add(btnQuadras);
		barra.add(btnCidades);
		barra.addSeparator();
		barra.add(btnLancaDia);
		barra.add(btnLancaDiaOut);
		barra.addSeparator();
		barra.add(btnPagamento);
		barra.add(btnPagamentoOut);
		
		btnTrocaModulo = new JButton(new ImageIcon(getClass().getResource("/icons/icon_troca_modulo.gif")));
		btnTrocaModulo.setToolTipText("Trocar Módulo");
		
		barra.add(Box.createHorizontalGlue());
		barra.add(btnTrocaModulo);

		itemEmpregadorCad.addActionListener(this);
		itemEmpreiteiroCad.addActionListener(this);
		itemEmpregadoCad.addActionListener(this);
		itemEquipeCad.addActionListener(this);
		itemCidadeCad.addActionListener(this);
		itemFuncaoCad.addActionListener(this);
		itemVeiculoCad.addActionListener(this);
		itemQuadraCad.addActionListener(this);
		itemEmpregadorCon.addActionListener(this);
		itemEmpreiteiroCon.addActionListener(this);
		itemEmpregadoCon.addActionListener(this);
		itemEquipeCon.addActionListener(this);
		itemCidadeCon.addActionListener(this);
		itemFuncaoCon.addActionListener(this);
		itemVeiculoCon.addActionListener(this);
		itemQuadraCon.addActionListener(this);
		itemLancaDia.addActionListener(this);
		itemLançaDiaOut.addActionListener(this);
		itemFechamento.addActionListener(this);
		itemFechamentoOut.addActionListener(this);
		itemAdiantamentos.addActionListener(this);
		itemRelEmpregadores.addActionListener(this);
		itemRelEmpreiteiros.addActionListener(this);
		itemRelPagamentos.addActionListener(this);
		itemRelDiasTrabalho.addActionListener(this);
		itemRelEmpregadosSimples.addActionListener(this);
		itemPlanilhaColetaDias.addActionListener(this);
		itemRelEmpregadosVisaoGeral.addActionListener(this);
		itemVisaoGeral.addActionListener(this);
		itemDetalhes.addActionListener(this);

		btnEmpregadores.addActionListener(this);
		btnEmpreiteiros.addActionListener(this);
		btnEmpregados.addActionListener(this);
		btnEquipes.addActionListener(this);
		btnCidades.addActionListener(this);
		btnFuncoes.addActionListener(this);
		btnVeiculos.addActionListener(this);
		btnQuadras.addActionListener(this);
		btnLancaDia.addActionListener(this);
		btnLancaDiaOut.addActionListener(this);
		btnPagamento.addActionListener(this);
		btnPagamentoOut.addActionListener(this);
		btnTrocaModulo.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object origem = e.getSource();

			if (origem == itemEmpregadorCad) {
				FrmCadastraAtrativo fr = new FrmCadastraAtrativo();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { }
				centralizaInternalFrame(fr, dPane);

			} else if (origem == itemEmpreiteiroCad) {
				FrmCadastraEmpreiteiro fr = new FrmCadastraEmpreiteiro();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { }
				centralizaInternalFrame(fr, dPane);

			} else if (origem == itemEmpregadoCad) {
				FrmCadastraEmpregado fr = new FrmCadastraEmpregado();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { }
				centralizaInternalFrame(fr, dPane);
				
			} else if (origem == itemEquipeCad) {
				FrmCadastraEquipe fr = new FrmCadastraEquipe();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { }
				centralizaInternalFrame(fr, dPane);

			} else if (origem == itemCidadeCad) {
				FrmCadastraCidade fr = new FrmCadastraCidade();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { }
				centralizaInternalFrame(fr, dPane);

			} else if (origem == itemQuadraCad) {
				FrmConsultaQuadra fr = new FrmConsultaQuadra();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { }
				centralizaInternalFrame(fr, dPane);

			} else if (origem == itemFuncaoCad) {
				FrmCadastraFuncao fr = new FrmCadastraFuncao();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { }
				centralizaInternalFrame(fr, dPane);

			} else if (origem == itemVeiculoCad) {
				FrmCadastraVeiculo fr = new FrmCadastraVeiculo();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { } 
				centralizaInternalFrame(fr, dPane);

			} else if (origem == itemEmpregadorCon || origem == btnEmpregadores) {
				FrmConsultaEmpregador fr = new FrmConsultaEmpregador();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { }
				centralizaInternalFrame(fr, dPane);

			} else if (origem == itemEmpreiteiroCon || origem == btnEmpreiteiros) {
				FrmConsultaEmpreiteiro fr = new FrmConsultaEmpreiteiro();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { }
				centralizaInternalFrame(fr, dPane);

			} else if (origem == itemEmpregadoCon || origem == btnEmpregados) {
				FrmConsultaEmpregado fr = new FrmConsultaEmpregado();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { }
				centralizaInternalFrame(fr, dPane);

			} else if (origem == itemEquipeCon || origem == btnEquipes) {
				FrmConsultaEquipe fr = new FrmConsultaEquipe();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { }
				centralizaInternalFrame(fr, dPane);

			} else if (origem == itemCidadeCon || origem == btnCidades) {
				FrmConsultaCidade fr = new FrmConsultaCidade();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { } 
				centralizaInternalFrame(fr, dPane);
			} else if (origem == itemQuadraCon || origem == btnQuadras) {
				FrmConsultaQuadra fr = new FrmConsultaQuadra();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { } 
				centralizaInternalFrame(fr, dPane);
			} else if (origem == itemFuncaoCon || origem == btnFuncoes) {
				FrmConsultaFuncao fr = new FrmConsultaFuncao();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { } 
				centralizaInternalFrame(fr, dPane);

			} else if (origem == itemVeiculoCon || origem == btnVeiculos) {
				FrmConsultaVeiculo fr = new FrmConsultaVeiculo();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { } 
				centralizaInternalFrame(fr, dPane);

			} else if (origem == itemLancaDia || origem == btnLancaDia) {
				FrmConsultaDia fr = new FrmConsultaDia();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { }
				centralizaInternalFrame(fr, dPane);
			} else if (origem == itemLançaDiaOut || origem == btnLancaDiaOut) {
				FrmConsultaDiaOutros fr = new FrmConsultaDiaOutros();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { }
				centralizaInternalFrame(fr, dPane);
			} else if (origem == itemFechamento || origem == btnPagamento) {
				FrmConsultaPagamento fr = new FrmConsultaPagamento();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { }
				centralizaInternalFrame(fr, dPane);
			} else if (origem == itemFechamentoOut || origem == btnPagamentoOut) {
				FrmConsultaPagamentoOutros fr = new FrmConsultaPagamentoOutros();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { }
				centralizaInternalFrame(fr, dPane);
			} else if (origem == btnTrocaModulo) {
				FrmLogin fr = new FrmLogin();
				fr.cbModulo.removeItem("Controle colheita Maçã");
				fr.fecha = false;
				fr.setVisible(true);
			} else if (origem == itemAdiantamentos) {
				FrmConsultaAdiantamentos fr = new FrmConsultaAdiantamentos();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { }
				centralizaInternalFrame(fr, dPane);
			} else if (origem == itemRelEmpregadores) {
				FrmRelatorioEmpregadores fr = new FrmRelatorioEmpregadores();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { }
				centralizaInternalFrame(fr, dPane);
			} else if (origem == itemRelEmpreiteiros) {
				FrmRelatorioEmpreiteiros fr = new FrmRelatorioEmpreiteiros();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { }
				centralizaInternalFrame(fr, dPane);
			} else if (origem == itemRelEmpregadosSimples) {
				FrmRelatorioEmpregados fr = new FrmRelatorioEmpregados();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { }
				centralizaInternalFrame(fr, dPane);
			} else if (origem == itemRelEmpregadosVisaoGeral) {
				FrmRelatorioEmpregadosVisaoGeral fr = new FrmRelatorioEmpregadosVisaoGeral();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { }
				centralizaInternalFrame(fr, dPane);
			} else if (origem == itemRelPagamentos) {
				FrmRelatorioPagamentos fr = new FrmRelatorioPagamentos();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { }
				centralizaInternalFrame(fr, dPane);
			} else if (origem == itemRelDiasTrabalho) {
				FrmRelatorioDiasTrabalhados fr = new FrmRelatorioDiasTrabalhados();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { }
				centralizaInternalFrame(fr, dPane);
			} else if (origem == itemPlanilhaColetaDias) {
				FrmPlanilhaColetaDias fr = new FrmPlanilhaColetaDias();
				fr.setVisible(true);
				dPane.add(fr);  
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { }
				centralizaInternalFrame(fr, dPane);
			} else if (origem == itemVisaoGeral) {
				FrmVisaoGeral fr = new FrmVisaoGeral();
				fr.setVisible(true);
				dPane.add(fr);
				try {
					fr.setSelected(true);
				} catch (PropertyVetoException exc) { }
				centralizaInternalFrame(fr, dPane);
				fr.setLocation(4, fr.getLocation().y);
			} else if (origem == itemDetalhes) {
				FrmSobreMaca fr = new FrmSobreMaca();
				fr.setVisible(true);
			}
	}
}
