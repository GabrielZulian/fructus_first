package gerais.vo;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class FrmSobrePai extends JFrame implements ActionListener {
	private static final long serialVersionUID = -9116255614514103612L;

	protected GridBagConstraints constraints = new GridBagConstraints();

	private Font f2 = new Font("Tahoma", Font.BOLD, 14);
	
	protected JLabel lblImg, lblSoftware;
	private JLabel lblDesenvolvido, lblCPF, lblLinha, lblLicenca, lblVaraschin;
	private JButton btnCancelar;

	public FrmSobrePai() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image img = tk.getImage(getClass().getResource("/icons/icon_logo_varaschin.gif"));
		setIconImage(img);
		
		setTitle("Sobre");
		setSize(380, 340);
		setLocationRelativeTo(null);
		setResizable(false);

		JPanel painelGeral = new JPanel();
		painelGeral.setLayout(new BorderLayout());

		JPanel painelCima = new JPanel();
		painelCima.setLayout(new GridBagLayout());

		JPanel painelMeio = new JPanel();
		painelMeio.setLayout(new GridBagLayout());

		JPanel painelBaixo = new JPanel();
		painelBaixo.setLayout(new GridBagLayout());

		constraints.insets = new Insets(0, 0, 0, 0);

		lblImg = new JLabel();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		painelCima.add(lblImg, constraints);
		painelCima.setBorder(BorderFactory.createEtchedBorder());

		painelGeral.add(painelCima, BorderLayout.NORTH);
		
		constraints.insets = new Insets(2, 2, 2, 2);
		
		lblSoftware = new JLabel("Software de ... - v0");
		lblSoftware.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		painelMeio.add(lblSoftware, constraints);
		
		lblLinha = new JLabel(" ");
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		painelMeio.add(lblLinha, constraints);
		
		lblDesenvolvido = new JLabel("Desenvolvido por");
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		painelMeio.add(lblDesenvolvido, constraints);
		
		lblCPF = new JLabel("Gabriel Silva Zulian - CPF 013.519.950-65");
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.CENTER;
		painelMeio.add(lblCPF, constraints);
		
		lblLinha = new JLabel(" ");
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.CENTER;
		painelMeio.add(lblLinha, constraints);
		
		lblLicenca = new JLabel("Licença especial e 100% personalizada para");
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.anchor = GridBagConstraints.CENTER;
		painelMeio.add(lblLicenca, constraints);
		
		lblVaraschin = new JLabel("Varaschin Agroflorestal Ltda - CNPJ 16.542.245/0001-80 ");
		constraints.gridx = 0;
		constraints.gridy = 6;
		constraints.anchor = GridBagConstraints.CENTER;
		painelMeio.add(lblVaraschin, constraints);

		painelGeral.add(painelMeio, BorderLayout.CENTER);

		constraints.ipadx = 13;

		btnCancelar = new JButton("Cancelar", new ImageIcon(getClass().getResource("/icons/icon_cancelar.gif")));
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelBaixo.add(btnCancelar, constraints);
		painelBaixo.setBorder(BorderFactory.createEtchedBorder());

		painelGeral.add(painelBaixo, BorderLayout.SOUTH);

		constraints.ipadx = 0;

		Container p = getContentPane();
		p.add(painelGeral);

		btnCancelar.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object origem = e.getSource();

		if (origem == btnCancelar){
			dispose();
		}
	}
}