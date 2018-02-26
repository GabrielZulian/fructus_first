package pragas.vo.especiais;

import java.awt.Cursor;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import gerais.vo.FrmRelatorioPai;
import mail.MailIndice;
import pragas.bo.ContagemBO;
import pragas.dao.ContagemDao;

public class FrmMandaEmailIndice extends FrmRelatorioPai {
	
	JLabel lblGrapholita, lblBonagota, lblMosca;
	JCheckBox cbGrapholita, cbBonagota, cbMosca;
	
	ContagemDao contDao = new ContagemDao();
	MailIndice mail;
	
	public FrmMandaEmailIndice() {
		setTitle("Mandar e-mail índices:");
		setSize(400, 200);
		
		lblTitulo.setText("Mandar e-mail índices");
		lblImg.setIcon(new ImageIcon(getClass().getResource("/icons/icon_email.gif")));
		
		lblGrapholita = new JLabel("Grapholita");
		constraints.gridx = 0;
		constraints.gridy = 0;
		painelMeio.add(lblGrapholita, constraints);
		
		constraints.insets = new Insets(2, 0, 2, 2);
		
		cbGrapholita = new JCheckBox("", false);
		constraints.gridx = 1;
		constraints.gridy = 0;
		painelMeio.add(cbGrapholita, constraints);
		
		constraints.insets = new Insets(2, 16, 2, 2);
		
		lblBonagota = new JLabel("Bonagota");
		constraints.gridx = 2;
		constraints.gridy = 0;
		painelMeio.add(lblBonagota, constraints);
		
		constraints.insets = new Insets(2, 0, 2, 2);
		
		cbBonagota = new JCheckBox("", false);
		constraints.gridx = 3;
		constraints.gridy = 0;
		painelMeio.add(cbBonagota, constraints);
		
		constraints.insets = new Insets(2, 16, 2, 2);
		
		lblMosca = new JLabel("Mosca");
		constraints.gridx = 4;
		constraints.gridy = 0;
		painelMeio.add(lblMosca, constraints);

		constraints.insets = new Insets(2, 0, 2, 2);
		
		cbMosca = new JCheckBox("", false);
		constraints.gridx = 5;
		constraints.gridy = 0;
		painelMeio.add(cbMosca, constraints);
	}

	@Override
	public void confirmar() {
		if (!cbGrapholita.isSelected() && !cbBonagota.isSelected() && !cbMosca.isSelected()) {
			
			JOptionPane.showMessageDialog(this, "Selecione a(s) espécie(s) para serem enviados os índices!", "Erro seleção", JOptionPane.ERROR_MESSAGE);
			return;
			
		} else {
			
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			
			if (cbGrapholita.isSelected()) {
				mail = new MailIndice();
				List<ContagemBO> contBO = new ArrayList<ContagemBO>();
				contBO = contDao.consultaPorTipo('G');
				mail.configuraMsg(contBO, 'G');
			}
			
			if (cbBonagota.isSelected()) {
				mail = new MailIndice();
				List<ContagemBO> contBO = new ArrayList<ContagemBO>();
				contBO = contDao.consultaPorTipo('B');
				mail.configuraMsg(contBO, 'B');
			}
			
			if (cbMosca.isSelected()) {
				mail = new MailIndice();
				List<ContagemBO> contBO = new ArrayList<ContagemBO>();
				contBO = contDao.consultaPorTipo('M');
				mail.configuraMsg(contBO, 'M');
			}
			
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			
			JOptionPane.showMessageDialog(this, "E-mail(s) enviado(s) com sucesso!", "Enviado!", JOptionPane.INFORMATION_MESSAGE);
			
		}
		
	}

}
