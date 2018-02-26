package pragas.vo.filtrorelatorios;

import java.awt.Cursor;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import gerais.vo.FrmRelatorioPai;
import impressos.RelatoriosPragas;

public class FrmRelatorioPorPragaGeral extends FrmRelatorioPai {
	private static final long serialVersionUID = -7676481647522868643L;
	
	JLabel lblGrapholita, lblBonagota, lblMosca;
	JRadioButton rbGrapholita, rbBonagota, rbMosca;
	ButtonGroup grupo;
	
	public FrmRelatorioPorPragaGeral() {
		setTitle("Relatório:");
		setSize(400, 200);
		
		lblTitulo.setText("Relatório de índice médio geral");
		lblImg.setIcon(new ImageIcon(getClass().getResource("/icons/icon_grafico.gif")));
		
		lblGrapholita = new JLabel("Grapholita");
		constraints.gridx = 0;
		constraints.gridy = 0;
		painelMeio.add(lblGrapholita, constraints);
		
		constraints.insets = new Insets(2, 0, 2, 2);
		
		rbGrapholita = new JRadioButton("", true);
		constraints.gridx = 1;
		constraints.gridy = 0;
		painelMeio.add(rbGrapholita, constraints);
		
		constraints.insets = new Insets(2, 16, 2, 2);
		
		lblBonagota = new JLabel("Bonagota");
		constraints.gridx = 2;
		constraints.gridy = 0;
		painelMeio.add(lblBonagota, constraints);
		
		constraints.insets = new Insets(2, 0, 2, 2);
		
		rbBonagota = new JRadioButton("", false);
		constraints.gridx = 3;
		constraints.gridy = 0;
		painelMeio.add(rbBonagota, constraints);
		
		constraints.insets = new Insets(2, 16, 2, 2);
		
		lblMosca = new JLabel("Mosca");
		constraints.gridx = 4;
		constraints.gridy = 0;
		painelMeio.add(lblMosca, constraints);

		constraints.insets = new Insets(2, 0, 2, 2);
		
		rbMosca = new JRadioButton("", false);
		constraints.gridx = 5;
		constraints.gridy = 0;
		painelMeio.add(rbMosca, constraints);
		
		grupo = new ButtonGroup();
		grupo.add(rbGrapholita);
		grupo.add(rbBonagota);
		grupo.add(rbMosca);
	}


	@Override
	public void confirmar() {
		char tipoPraga = 'M';
		
		if (rbGrapholita.isSelected())
			tipoPraga = 'G';
		else if (rbBonagota.isSelected())
			tipoPraga = 'B';
		
		RelatoriosPragas rel = new RelatoriosPragas();
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		rel.RelatorioIndiceGeral(tipoPraga);
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

}
