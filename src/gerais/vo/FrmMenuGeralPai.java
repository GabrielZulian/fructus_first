package gerais.vo;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import gerais.bo.VersaoBO;
import gerais.dao.VersaoDao;

public class FrmMenuGeralPai extends JFrame implements ActionListener {
	private static final long serialVersionUID = 8626908390766881464L;

	public JDesktopPane dPane;
	public JMenuBar barraMenu;
	public JToolBar barra;
	public VersaoDao versaoDao = new VersaoDao();

	public FrmMenuGeralPai() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (JOptionPane.showConfirmDialog(null, "Deseja sair do sistema?", "Sair", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					System.exit(0);
				} else {}
			}
		});

		Toolkit tk = Toolkit.getDefaultToolkit();
		Image img = tk.getImage(getClass().getResource("/icons/icon_logo_varaschin.gif"));
		setIconImage(img);

		setSize(tk.getScreenSize());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setTitle("Área de Trabalho");
		setExtendedState(MAXIMIZED_BOTH);

		barraMenu = new JMenuBar();
		setJMenuBar(barraMenu);
		barra = new JToolBar();

		//-----------------------------------------------------------
		final JPanel painelGeral = new JPanel();
		painelGeral.setLayout(new BorderLayout(2,2));

		dPane = new JDesktopPane() {
			private static final long serialVersionUID = 6147037532024474817L;
			
			ImageIcon icon = new ImageIcon(getClass().getResource("/icons/fundo_area.gif"));
		    Image imagem = icon.getImage();
		    @Override
		    protected void paintComponent(Graphics g) {
		        super.paintComponent(g);
		        g.drawImage(imagem, 0, 0, getWidth(), getHeight(), this);
		    }
		};
		
		painelGeral.add(dPane, BorderLayout.CENTER);
		dPane.putClientProperty("JDesktopPane.dragMode", "outline");
		//----------------------------------------------------------
		
		barra = new JToolBar();

		painelGeral.add(barra,BorderLayout.NORTH);

		Container p = getContentPane();
		p.add(painelGeral);
		
		VersaoBO versaoBO = new VersaoBO();
		versaoBO = versaoDao.consultaVersao();
		
		if (!versaoBO.isUltimaVersao()) {
			JOptionPane.showMessageDialog(this, "Há uma nova versão do sistema disponível! Favor utilizá-la.\n"
					+ "Sua versão: " + VersaoBO.getVersao() + "\nÚltima versão: " + versaoBO.getUltimaVersao(), "Versão Desatualizada", 1);
		}	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

	public static void centralizaInternalFrame (JInternalFrame frame, JDesktopPane pane) {
		Dimension dimensionDesk = pane.getSize();
		Dimension dimensionIFrame = frame.getSize();

		int width = (int)(dimensionDesk.getWidth() - dimensionIFrame.getWidth()) / 2;
		int height = (int)(dimensionDesk.getHeight() - dimensionIFrame.getHeight()) / 2;

		frame.setLocation(width, height);
	}
}
