package gerais.bo;

import gerais.dao.VersaoDao;

public class VersaoBO {
	
	private static String versao = "v1.3";
	private String ultimaVersao;
	private VersaoDao versaoDAO = new VersaoDao();
	
	public VersaoBO() {
		this.versao = "v1.3";
		this.ultimaVersao = "";
	}
	
	public static String getVersao() {
		return versao;
	}
	
	public String getUltimaVersao() {
		return ultimaVersao;
	}
	
	public void setUltimaVersao(String ultimaVersao) {
		this.ultimaVersao = ultimaVersao;
	}
	
	public boolean isUltimaVersao() {
		if (ultimaVersao.equals(versao))
			return true;
		else
			return false;
	}
	
}
