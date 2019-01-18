package util;

import javax.swing.ImageIcon;

import net.sf.jasperreports.view.JasperViewer;

public class JasperViewerModelo {
	
	public void JasperViewermodelo(JasperViewer view) {
		view.setTitle("Varaschin Software - Relatórios");
		ImageIcon icon = new ImageIcon(getClass().getResource("/icons/icon_logo_varaschin.gif"));
		view.setIconImage(icon.getImage());
	}

}
