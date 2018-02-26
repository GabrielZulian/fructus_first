package safristas.vo;

import javax.swing.ImageIcon;

import gerais.bo.VersaoBO;
import gerais.vo.FrmSobrePai;

public class FrmSobreMaca extends FrmSobrePai {
	private static final long serialVersionUID = 3186416340642022815L;

	public FrmSobreMaca() {

		lblImg.setIcon(new ImageIcon(getClass().getResource("/icons/img_login.gif")));

		lblSoftware.setText("Software de controle de colheita - " + VersaoBO.getVersao());
	}

}