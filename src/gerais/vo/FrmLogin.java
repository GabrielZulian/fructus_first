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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import gerais.bo.UsuarioBO;
import gerais.bo.VersaoBO;
import gerais.dao.UsuarioDao;
import insumos.vo.FrmMenuGeralInsumos;
import pragas.vo.FrmMenuGeralPragas;
import safristas.vo.FrmMenuGeralMaca;

public class FrmLogin extends JFrame implements ActionListener {
	private static final long serialVersionUID = -9116255614514103612L;

	protected GridBagConstraints constraints = new GridBagConstraints();

	private Font f2 = new Font("Tahoma", Font.PLAIN, 14);
	private JLabel lblImg, lblUsuario, lblLogin, lblVersao, lblSenha, lblModulo;
	private JPasswordField txtSenha;
	private JButton btnConfirmar, btnCancelar;
	public JComboBox cbUsuarios, cbModulo;
	public boolean fecha = true;
	private String[] modulos = {"Controle colheita Maçã", "Controle de Pragas", "Controle de Insumos"};
	private static ArrayList<UsuarioBO> usuBO;
	private UsuarioDao usuDao = new UsuarioDao();
	
	public FrmLogin() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (fecha)
					System.exit(0);
				else
					dispose();
			}
		});

		Toolkit tk = Toolkit.getDefaultToolkit();
		Image img = tk.getImage(getClass().getResource("/icons/icon_logo_varaschin.gif"));
		setIconImage(img);

		setTitle("Varaschin Software - Login");
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

		lblImg = new JLabel(new ImageIcon(getClass().getResource("/icons/img_login.gif")));
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		painelCima.add(lblImg, constraints);
		painelCima.setBorder(BorderFactory.createEtchedBorder());
		painelGeral.add(painelCima, BorderLayout.NORTH);

		constraints.insets = new Insets(3, 3, 3, 3);

		lblLogin = new JLabel("Login");
		lblLogin.setFont(new Font("Arial", Font.BOLD, 18));
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(2, 4, 20, 4);
		painelMeio.add(lblLogin, constraints);
		constraints.gridwidth = 1;

		lblUsuario = new JLabel("Usuário");
		lblUsuario.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.insets = new Insets(3, 3, 3, 3);
		painelMeio.add(lblUsuario, constraints);

		cbUsuarios = new JComboBox();
		cbUsuarios.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(cbUsuarios, constraints);

		lblSenha = new JLabel("Senha");
		lblSenha.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblSenha, constraints);

		txtSenha = new JPasswordField(7);
		txtSenha.setFont(f2);
		txtSenha.requestFocus();
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtSenha, constraints);	

		lblModulo = new JLabel("Módulo");
		lblModulo.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblModulo, constraints);

		cbModulo = new JComboBox(modulos);
		cbModulo.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(cbModulo, constraints);
		
		lblVersao = new JLabel(VersaoBO.getVersao());
		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblVersao, constraints);

		painelGeral.add(painelMeio, BorderLayout.CENTER);

		constraints.ipadx = 13;

		btnConfirmar = new JButton("Confirmar", new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelBaixo.add(btnConfirmar, constraints);
		btnConfirmar.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "KEY_ENTER");
		btnConfirmar.getActionMap().put("KEY_ENTER", new AbstractAction() {
			private static final long serialVersionUID = 3975961991102010170L;

			public void actionPerformed(ActionEvent evt) {
				btnConfirmar.doClick();
			}
		});

		constraints.ipadx = 20;

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

		consultaNomes();

		btnConfirmar.addActionListener(this);
		btnCancelar.addActionListener(this);
		cbModulo.addActionListener(this);

		lePropriedades();
	}
	
	private File getArquivoProp() {
		File arquivo = null;
		String diretorio = null;

		diretorio = System.getenv("AppData");
		arquivo = new File(diretorio + "\\varas_config.properties");
		
		return arquivo; 
	}

	private void lePropriedades() {
		Properties prop = new Properties();
		InputStream input = null;
		
		if (getArquivoProp().exists()) {
			
			try {
				input = new FileInputStream(getArquivoProp());

				prop.load(input);

				prop.getProperty("usuario");
				prop.getProperty("modulo");
				
				cbUsuarios.setSelectedItem(prop.getProperty("usuario"));
				cbModulo.setSelectedItem(prop.getProperty("modulo"));

			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else
			escrevePropriedades();
	}

	private void escrevePropriedades() {
		Properties prop = new Properties();
		OutputStream output = null;

		try {
			output = new FileOutputStream(getArquivoProp());

			prop.setProperty("usuario", cbUsuarios.getSelectedItem().toString());
			prop.setProperty("modulo", cbModulo.getSelectedItem().toString());
			
			prop.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	private void consultaNomes() {
		usuBO = new ArrayList<UsuarioBO>();

		usuBO = usuDao.consultaNomes();

		for (int i=0;i<usuBO.size();i++) {
			cbUsuarios.addItem(usuBO.get(i).getNome());
		}

		usuBO.removeAll(usuBO);
	}

	public static void main(String[] args) {
		  SwingUtilities.invokeLater(new Runnable() {
		        public void run() {
		            exibirTela();
		        }
		    });
		
	}

	protected static void exibirTela() {
		FrmLogin fr = new FrmLogin();
		fr.setVisible(true);
	}

	public static UsuarioBO getUsuario() {
		return usuBO.get(0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object origem = e.getSource();

		if (origem == btnConfirmar) {
			usuBO = new ArrayList<UsuarioBO>();
			usuBO = usuDao.consultaPorNomeESenha(cbUsuarios.getSelectedItem().toString(), new String(txtSenha.getPassword()));
			if (usuBO == null) {
				JOptionPane.showMessageDialog(this, "Senha incorreta!", "Erro senha", JOptionPane.ERROR_MESSAGE);
				txtSenha.requestFocus();
				txtSenha.selectAll();
				return;
			} else {
				usuBO.get(0).setSenha("");
				if (cbModulo.getSelectedItem().equals("Controle colheita Maçã")) {
					FrmMenuGeralMaca fr = new FrmMenuGeralMaca();
					fr.setVisible(true);
				} else if (cbModulo.getSelectedItem().equals("Controle de Pragas")) {
					FrmMenuGeralPragas fr = new FrmMenuGeralPragas();
					fr.setVisible(true);
				} else if (cbModulo.getSelectedItem().equals("Controle de Insumos")) {
					FrmMenuGeralInsumos fr = new FrmMenuGeralInsumos();
					fr.setVisible(true);
				} else {}
				escrevePropriedades();
				dispose();
			}
		} else if (origem == btnCancelar) {
			if (fecha)
				System.exit(0);
			else
				dispose();
		} else if (origem == cbModulo) {
			if (cbModulo.getSelectedItem().equals("Controle colheita Maçã"))
				lblImg.setIcon(new ImageIcon(getClass().getResource("/icons/img_login.gif")));
			else if (cbModulo.getSelectedItem().equals("Controle de Pragas"))
				lblImg.setIcon(new ImageIcon(getClass().getResource("/icons/img_login_pragas.gif")));
			else if (cbModulo.getSelectedItem().equals("Controle de Insumos"))
				lblImg.setIcon(new ImageIcon(getClass().getResource("/icons/img_login_insumos.gif")));
		}
	}
}