package insumos.vo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PainelVisaoTalhao extends JPanel {
	private JLabel lblProduto, lblDataAplicacao, lblDataFinal;
	public JLabel lblNroTalhao;
	protected JTextField txtProduto, txtDataAplicacao, txtDataFinal;
	private GridBagConstraints constraints = new GridBagConstraints();
	
	public JPanel painelGeral;
	
	private Font f2 = new Font("Tahoma", Font.BOLD, 16);
	
	public PainelVisaoTalhao() {
		
		setPreferredSize(new Dimension(276, 276));
		setLayout(new GridBagLayout());
		
		painelGeral = new JPanel(new GridBagLayout());
		
		constraints.insets = new Insets(2, 2, 2, 2);
		
		lblNroTalhao = new JLabel("Lavoura 00");
		lblNroTalhao.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		painelGeral.add(lblNroTalhao, constraints);
		constraints.gridwidth = 1;
		
		lblProduto = new JLabel("Produto:");
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelGeral.add(lblProduto, constraints);
		
		txtProduto = new JTextField(12);
		txtProduto.setFocusable(false);
		txtProduto.setEditable(false);
		txtProduto.setBackground(Color.white);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.WEST;
		painelGeral.add(txtProduto, constraints);
		
		lblDataAplicacao = new JLabel("Data Aplicação:");
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		painelGeral.add(lblDataAplicacao, constraints);
		
		txtDataAplicacao = new JTextField(8);
		txtDataAplicacao.setFocusable(false);
		txtDataAplicacao.setEditable(false);
		txtDataAplicacao.setBackground(Color.white);
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelGeral.add(txtDataAplicacao, constraints);
		
		lblDataFinal = new JLabel("Data Final:");
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.EAST;
		painelGeral.add(lblDataFinal, constraints);
		
		txtDataFinal = new JTextField(8);
		txtDataFinal.setFocusable(false);
		txtDataFinal.setEditable(false);
		txtDataFinal.setBackground(Color.white);
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.WEST;
		
		painelGeral.add(txtDataFinal, constraints);
		
		painelGeral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		add(painelGeral);
		setBorder(BorderFactory.createLineBorder(Color.gray, 1));
	}
	
	public void setBackgroundColor(Color color) {
		painelGeral.setBackground(color);
		setBackground(color);
	}
	
}
