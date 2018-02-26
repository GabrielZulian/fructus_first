package insumos.vo;

import javax.swing.ImageIcon;

import gerais.bo.VersaoBO;
import gerais.vo.FrmSobrePai;

public class FrmSobreInsumos extends FrmSobrePai {
	private static final long serialVersionUID = 3186416340642022815L;

	public FrmSobreInsumos() {

		lblImg.setIcon(new ImageIcon(getClass().getResource("/icons/img_login_insumos.gif")));

		lblSoftware.setText("Software de controle de insumos - " + VersaoBO.getVersao());
	}

}