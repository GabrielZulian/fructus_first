package pragas.vo;

import javax.swing.ImageIcon;

import gerais.bo.VersaoBO;
import gerais.vo.FrmSobrePai;

public class FrmSobrePragas extends FrmSobrePai {
	private static final long serialVersionUID = 3186416340642022815L;

	public FrmSobrePragas() {

		lblImg.setIcon(new ImageIcon(getClass().getResource("/icons/img_login_pragas.gif")));

		lblSoftware.setText("Software de controle de pragas pomar - " + VersaoBO.getVersao());
	}

}