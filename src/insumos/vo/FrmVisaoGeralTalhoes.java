package insumos.vo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.joda.time.DateTime;

import insumos.bo.AplicacaoBO;
import insumos.dao.AplicacaoDao;
import util.WrapLayout;

public class FrmVisaoGeralTalhoes extends JInternalFrame implements ActionListener {
	private static final long serialVersionUID = -1501662160449268607L;

	protected GridBagConstraints constraints = new GridBagConstraints();

	protected JLabel lblOrdenarPor, lblInfo, lblImg;
	protected JTextField txtOrdenarPor;
	protected JComboBox<String> cbOrdenar;
	protected String[] filtros = {"Número", "Data final", "Data aplicação", "Insumo"};
	protected JButton btnOrdenar, btnCancelar;
	
	private ArrayList<PainelVisaoTalhao> paineis = new ArrayList<PainelVisaoTalhao>();
	private AplicacaoDao aplicacaoDao = new AplicacaoDao();
	private ArrayList<AplicacaoBO> aplicacaoBO = new ArrayList<AplicacaoBO>();
	
	private JPanel painelAntigos, painelAtuais;
	JTabbedPane tabbedPane;
	
	private Color vermelho = new Color (252,100,100);
	private Color amarelo = new Color (255,255,110);
	private Color verde = new Color (120,255,120);

	protected Font f2 = new Font("Tahoma", Font.PLAIN, 14);

	public FrmVisaoGeralTalhoes() {
		super("Visão geral talhões", false,true,true,false);
		
		setFrameIcon(new ImageIcon(getClass().getResource("/icons/icon_logo_varaschin.gif")));
		setResizable(false);
		
		JPanel painelGeral = new JPanel();
		painelGeral.setLayout(new BorderLayout());

		JPanel painelCima = new JPanel();
		painelCima.setLayout(new GridBagLayout());

		JPanel painelBaixo = new JPanel();
		painelBaixo.setLayout(new GridBagLayout());
		
		painelAntigos = new JPanel();
		painelAntigos.setLayout(new WrapLayout(WrapLayout.LEFT));
		
		painelAtuais = new JPanel();
		painelAtuais.setLayout(new WrapLayout(WrapLayout.LEFT));
		
		constraints.insets = new Insets(1, 1, 1, 1);
		
		lblOrdenarPor = new JLabel("Ordenar Por:");
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		painelCima.add(lblOrdenarPor, constraints);
		
		cbOrdenar = new JComboBox<String>(filtros);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		painelCima.add(cbOrdenar, constraints);
		
		btnOrdenar = new JButton("Ordenar");
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		painelCima.add(btnOrdenar, constraints);

		painelCima.setBorder(BorderFactory.createEtchedBorder());
		
		constraints.insets = new Insets(3, 4, 3, 4);

		painelGeral.add(painelCima, BorderLayout.NORTH);
		
		//-----------------------------------------------
		
		aplicacaoBO = aplicacaoDao.consultaGeralPorNro();
		inserePaineis(aplicacaoBO);
		
		tabbedPane = new JTabbedPane();
		
		painelAntigos.setBackground(Color.white);
		
		painelAtuais.setBackground(Color.white);
		
		tabbedPane.addTab("Antigos", painelAntigos);
		tabbedPane.addTab("Atuais", painelAtuais);
		
		tabbedPane.setSelectedIndex(1);
		
		JScrollPane scrollAtuais = new JScrollPane(tabbedPane);
		painelGeral.add(scrollAtuais, BorderLayout.CENTER);
		
		//-----------------------------------------------
		
		painelGeral.add(painelBaixo, BorderLayout.SOUTH);
		constraints.ipadx = 0;

		Container p = getContentPane();
		p.add(painelGeral);

		btnOrdenar.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object origem = e.getSource();
		if (origem == btnOrdenar) {
			if (cbOrdenar.getSelectedItem().toString().equals("Número")) {
				aplicacaoBO = aplicacaoDao.consultaGeralPorNro();
				inserePaineis(aplicacaoBO);
			} else if (cbOrdenar.getSelectedItem().toString().equals("Data final")) {
				aplicacaoBO = aplicacaoDao.consultaGeralPorDataFinal();
				inserePaineis(aplicacaoBO);
			} else if (cbOrdenar.getSelectedItem().toString().equals("Data aplicação")) {
				aplicacaoBO = aplicacaoDao.consultaGeralPorDataApl();
				inserePaineis(aplicacaoBO);
			} else if (cbOrdenar.getSelectedItem().toString().equals("Insumo")) {
				aplicacaoBO = aplicacaoDao.consultaGeralPorInsumo();
				inserePaineis(aplicacaoBO);
			}
		} else if (origem == btnCancelar)
			doDefaultCloseAction();
	}
	
	private void inserePaineis(ArrayList<AplicacaoBO> aplicacaoBO) {
		painelAtuais.removeAll();
		painelAntigos.removeAll();
		int cont = 0;
		
		do {
			DateTime dataFinal = aplicacaoBO.get(cont).getDataFinal();
			PainelVisaoTalhao painelLav = new PainelVisaoTalhao();
			
			painelLav.lblNroTalhao.setText(aplicacaoBO.get(cont).talhaoBO.getTipo() + " " + aplicacaoBO.get(cont).talhaoBO.getNumero());
			painelLav.txtProduto.setText(aplicacaoBO.get(cont).insumoBO.getDescricao());
			painelLav.txtDataAplicacao.setText(aplicacaoBO.get(cont).getData().toString("dd/MM/yyyy"));
			painelLav.txtDataFinal.setText(dataFinal.toString("dd/MM/yyyy"));
			
			if (DateTime.now().isEqual(dataFinal) || DateTime.now().isAfter(dataFinal)) {
				painelLav.setBackgroundColor(vermelho);
			} else if ((DateTime.now().isAfter(dataFinal.minusDays(5)) && DateTime.now().isBefore(dataFinal))) {
				painelLav.setBackgroundColor(amarelo);
			} else if (DateTime.now().isBefore(dataFinal.minusDays(5))) {
				painelLav.setBackgroundColor(verde);
			}
			
			if (dataFinal.isBefore(DateTime.now().minusDays(30)))
				painelAntigos.add(painelLav);
			else
				painelAtuais.add(painelLav);
			
			cont++;
		} while (cont < aplicacaoBO.size());
		painelAtuais.validate();
		painelAntigos.validate();
	}
}
