package pragas.vo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;

import util.ModeloTabela;
import pragas.bo.ContagemBO;
import pragas.dao.ContagemDao;

public class FrmDados extends JInternalFrame implements ActionListener {
	private static final long serialVersionUID = 3387969633911321736L;

	private GridBagConstraints constraints = new GridBagConstraints();
	
	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	JLabel lblNroFrascos, lblIndice;
	JTextField txtNroFrascos, txtIndice;
	JButton btnCancelar;
	char tipo;

	protected JTable tabela;
	protected ModeloTabela modeloTab;
	protected ContagemDao contDao = new ContagemDao();
	protected ArrayList<ContagemBO> contBO;
	
	public FrmDados(int numeroQuadra, char tipo, BigDecimal indice) {
		this();
		setTitle("Quadra " + numeroQuadra);
		Color vermelho = new Color (252,125,125);
		Color amarelo = new Color (255,255,125);
		this.tipo = tipo;
		System.out.println("Indice =" + indice);
		contBO = contDao.consultaPorNumeroETipo(numeroQuadra, tipo);

		int aux = 0;
		try {
		do {
			modeloTab.addRow(new Object[] {
				df.format(contBO.get(aux).data.toDate()),
				contBO.get(aux).getQntdInsetos(),
				contBO.get(aux).getIndiceFinal()
			});
			aux++;
		} while (aux<4);
		} catch (IndexOutOfBoundsException erro) {}
		
		txtIndice.setText(modeloTab.getValueAt(0, 2).toString());
		txtNroFrascos.setText(String.valueOf(contBO.get(0).frasco.getQntdFrascos()));
		
		BigDecimal indiceBig = new BigDecimal(txtIndice.getText());
		
		switch (tipo) {
		case 'G': setTitle(getTitle() + " - Grapholita");
		if (indiceBig.compareTo(new BigDecimal("20.0")) >= 0) {
			txtIndice.setBackground(vermelho);
		} else if (indiceBig.compareTo(new BigDecimal("15.0")) > 0 && indiceBig.compareTo(new BigDecimal("20.0")) < 0) {
			txtIndice.setBackground(amarelo);
		}
		break;
		case 'B': setTitle(getTitle() + " - Bonagota");
		if (indiceBig.compareTo(new BigDecimal("20.0")) >= 0) {
			txtIndice.setBackground(vermelho);
		} else if (indiceBig.compareTo(new BigDecimal("15.0")) > 0 && indiceBig.compareTo(new BigDecimal("20.0")) < 0) {
			txtIndice.setBackground(amarelo);
		}
		break;
		case 'M': setTitle(getTitle() + " - Mosca da fruta");
		if (indiceBig.compareTo(new BigDecimal("0.5")) >= 0) {
			txtIndice.setBackground(vermelho);
		} else if (indiceBig.compareTo(new BigDecimal("0.4")) >= 0 && indiceBig.compareTo(new BigDecimal("0.5")) < 0) {
			txtIndice.setBackground(amarelo);
		}
		break;
		}
	}
	
	public FrmDados() {

		super("Dados",true,true,false,true);
		
		setFrameIcon(new ImageIcon(getClass().getResource("/icons/icon_logo_varaschin.gif")));

		setSize(300, 300);
		setResizable(false);

		JPanel painelCima = new JPanel();
		painelCima.setLayout(new GridBagLayout());
		
		JPanel painelMeio = new JPanel();
		painelMeio.setLayout(new GridBagLayout());
		
		JPanel painelBaixo = new JPanel();
		painelBaixo.setLayout(new GridBagLayout());
		
		JPanel painelGeral = new JPanel();
		painelGeral.setLayout(new BorderLayout(2, 4));
		
		constraints.insets = new Insets(2, 2, 2, 2);
		
		lblIndice = new JLabel("Índice:");
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelCima.add(lblIndice, constraints);
		
		txtIndice = new JTextField(6);		
		lblNroFrascos = new JLabel("Frascos na área:");
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelCima.add(lblNroFrascos, constraints);
		
		txtNroFrascos = new JTextField(4);
		txtNroFrascos.setEditable(false);
		txtNroFrascos.setFocusable(false);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.WEST;
		painelCima.add(txtNroFrascos, constraints);
		
		painelGeral.add(painelCima, BorderLayout.NORTH);
		
		ArrayList<Object> dados2 = new ArrayList<Object>();

		String[] colunas2 = new String[] {"Data", "Nro. Insetos", "Índice"};

		boolean[] edicao2 = {false, false, false};

		modeloTab = new ModeloTabela(dados2, colunas2, edicao2);
		tabela = new JTable(modeloTab);
		tabela.setRowHeight(22);
		tabela.getColumnModel().getColumn(0).setPreferredWidth(90);
		tabela.getColumnModel().getColumn(0).setResizable(true);
		tabela.getColumnModel().getColumn(1).setPreferredWidth(80);
		tabela.getColumnModel().getColumn(1).setResizable(true);
		tabela.getColumnModel().getColumn(2).setPreferredWidth(87);
		tabela.getColumnModel().getColumn(2).setResizable(true);
		tabela.getTableHeader().setReorderingAllowed(false); 
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane rolagemTabelaEquipe = new JScrollPane(tabela);
		rolagemTabelaEquipe.setPreferredSize(new Dimension(260, 120));
		painelMeio.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Últimas Atualizações"));
		painelMeio.add(rolagemTabelaEquipe);
		tabela.setFocusable(false);
		
		painelGeral.add(painelMeio, BorderLayout.CENTER);

		btnCancelar = new JButton("Voltar (F4)", new ImageIcon(getClass().getResource("/icons/icon_sair.gif")));
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelBaixo.add(btnCancelar, constraints);
		btnCancelar.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F4"), "KEY_F4");
		btnCancelar.getActionMap().put("KEY_F4", new AbstractAction() {
			private static final long serialVersionUID = -6147085484460330907L;

			public void actionPerformed(ActionEvent evt) {
				btnCancelar.doClick();
		    }
		});
		painelGeral.add(painelBaixo, BorderLayout.SOUTH);
		
		btnCancelar.addActionListener(this);
		
		Container p = getContentPane();
		p.add(painelGeral);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object origem = e.getSource();
		
		if (origem == btnCancelar) {
			doDefaultCloseAction();
		}
	}

}
