package pragas.vo;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import pragas.bo.ContagemBO;
import pragas.dao.ContagemDao;

public abstract class FrmMapaPai extends JInternalFrame implements ActionListener {
	private static final long serialVersionUID = -3963504949842249226L;
	public JLabel lblMapa;
	protected List<JButton> btnsPontosPomar1 = new ArrayList<JButton>();
	protected JButton btnAtualizar, btnFechar;

	protected static int ESTADO_OK = 1, ESTADO_ATENCAO = 2, QNTD_QUADRAS = 33;

	private GridBagConstraints constraints = new GridBagConstraints();
	protected ContagemDao contDao = new ContagemDao();
	protected ArrayList<ContagemBO> contBO;
	protected BigDecimal indice;

	public FrmMapaPai() {
		super("Dados",true,true,false,true);

		setResizable(true);
		setSize(600, 680);
		setFrameIcon(new ImageIcon(getClass().getResource("/icons/icon_logo_varaschin.gif")));
		setTitle("Controle de Pragas");

		JPanel painelGeral = new JPanel();
		painelGeral.setLayout(new BorderLayout(2,2));
		
		JPanel painelMeio = new JPanel();
		painelMeio.setLayout(new GridBagLayout());
		
		JPanel painelBaixo = new JPanel();
		painelMeio.setLayout(new GridBagLayout());

		constraints.ipadx = -30;
		constraints.ipady = -10;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 20;
		constraints.gridheight = 20;

		for (int i=0;i<QNTD_QUADRAS;i++) {
			btnsPontosPomar1.add(new JButton());
			configuraBotao(btnsPontosPomar1.get(i), String.valueOf(i+1));
		}
		
		constraints.insets = new Insets(-270, -170, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(0), constraints);

		constraints.insets = new Insets(-295, -105, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(1), constraints);

		constraints.insets = new Insets(-320, -190, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(2), constraints);

		constraints.insets = new Insets(-340, -130, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(3), constraints);

		constraints.insets = new Insets(-330, -60, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(4), constraints);

		constraints.insets = new Insets(-390, -35, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(5), constraints);

		constraints.insets = new Insets(-265, -75, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(6), constraints);

		constraints.insets = new Insets(-305, 0, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(7), constraints);

		constraints.insets = new Insets(-230, -20, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(8), constraints);

		constraints.insets = new Insets(-300, 150, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(9), constraints);

		constraints.insets = new Insets(-220, 140, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(10), constraints);

		constraints.insets = new Insets(-180, 100, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(11), constraints);

		constraints.insets = new Insets(-200, -75, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(12), constraints);

		constraints.insets = new Insets(-130, 60, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(13), constraints);

		constraints.insets = new Insets(-110, 0, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(14), constraints);

		constraints.insets = new Insets(-110, -110, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(15), constraints);

		constraints.insets = new Insets(-460, -110, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(16), constraints);

		constraints.insets = new Insets(-440, -300, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(17), constraints);

		constraints.insets = new Insets(-340, -260, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(18), constraints);

		constraints.insets = new Insets(40, -20, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(19), constraints);

		constraints.insets = new Insets(0, 130, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(20), constraints);
		
		constraints.insets = new Insets(65, 160, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(21), constraints);

		constraints.insets = new Insets(130, 110, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(22), constraints);

		constraints.insets = new Insets(225, 40, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(23), constraints);

		constraints.insets = new Insets(370, 70, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(24), constraints);
		
		constraints.insets = new Insets(240, 170, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(25), constraints);
		
		constraints.insets = new Insets(-460, 50, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(26), constraints);
		
		constraints.insets = new Insets(-516, 180, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(27), constraints);
		
		constraints.insets = new Insets(110, -190, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(28), constraints);
		
		constraints.insets = new Insets(54, -155, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(29), constraints);
		
		constraints.insets = new Insets(-30, -200, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(30), constraints);
		
		constraints.insets = new Insets(-110, -220, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(31), constraints);
		
		constraints.insets = new Insets(-40, -300, 0, 0);
		painelMeio.add(btnsPontosPomar1.get(32), constraints);
		
		lblMapa = new JLabel(new ImageIcon(getClass().getResource("/icons/mapa.jpg")));
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 20;
		constraints.gridheight = 20;
		painelMeio.add(lblMapa, constraints);
		
		JScrollPane painelScroll = new JScrollPane(painelMeio);
		
		painelGeral.add(painelScroll, BorderLayout.CENTER);
		
		constraints.ipadx = 100;
		constraints.ipady = 100;
		
		btnAtualizar = new JButton("Atualizar (F1)", new ImageIcon(getClass().getResource("/icons/icon_atualizar.gif")));
		constraints.gridx = 0;
		constraints.gridy = 0;
		painelBaixo.add(btnAtualizar, constraints);
		btnAtualizar.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F1"), "KEY_F1");
		btnAtualizar.getActionMap().put("KEY_F1", new AbstractAction() {
			private static final long serialVersionUID = -6600295826327650590L;

			public void actionPerformed(ActionEvent evt) {
				btnAtualizar.doClick();
		    }
		});
		
		btnFechar = new JButton("Fechar (F4)", new ImageIcon(getClass().getResource("/icons/icon_sair.gif")));
		constraints.gridx = 1;
		constraints.gridy = 0;
		painelBaixo.add(btnFechar, constraints);
		btnFechar.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F4"), "KEY_F4");
		btnFechar.getActionMap().put("KEY_F4", new AbstractAction() {
			private static final long serialVersionUID = 4473114401830474222L;

			public void actionPerformed(ActionEvent evt) {
				btnFechar.doClick();
		    }
		});
		
		constraints.ipadx = 0;
		constraints.ipady = 0;
		
		painelGeral.add(painelBaixo, BorderLayout.SOUTH);
		
		for (int i=0;i<btnsPontosPomar1.size();i++)
			btnsPontosPomar1.get(i).addActionListener(this);
		
		btnAtualizar.addActionListener(this);
		btnFechar.addActionListener(this);
		
		Container p = getContentPane();
		p.add(painelGeral);
	}

	public void configuraBotao(JButton botao, String numero) {
		botao.setContentAreaFilled(false);
		botao.setBorderPainted(false);
		botao.setFocusPainted(false);
		botao.setName(numero);
		botao.setToolTipText("Quadra " + numero);
		botao.setIcon(new ImageIcon(getClass().getResource("/icons/icon_ponto_verde.gif")));
		botao.setRolloverIcon(new ImageIcon(getClass().getResource("/icons/icon_ponto_verde_hover.gif")));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object origem = e.getSource();
		
		if (origem == btnAtualizar) {
			atualizaBotoes();
		} else if (origem == btnFechar) {
			doDefaultCloseAction();
		}
		
	}
	
	public abstract void atualizaBotoes();
	
	public abstract void calculaIndice();
}
