package gerais.vo;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public class FrmCadastraPai extends JInternalFrame implements ActionListener {
	private static final long serialVersionUID = -2106139847006516110L;

	protected GridBagConstraints constraints = new GridBagConstraints();

	protected JLabel lblTitulo, lblImg, lblCodigo;
	protected JButton btnConfirmar, btnCancelar;

	protected JPanel painelMeio = new JPanel();

	protected Font f = new Font("Arial", Font.BOLD, 14);
	protected Font f2 = new Font("Tahoma", Font.PLAIN, 14);

	public FrmCadastraPai() {
		super("Cadastro",false,true,false,true);
		
		setFrameIcon(new ImageIcon(getClass().getResource("/icons/icon_logo_varaschin.gif")));
		setResizable(false);
		setSize(750, 600);

		JPanel painelGeral = new JPanel();
		painelGeral.setLayout(new BorderLayout());

		JPanel painelCima = new JPanel();
		painelCima.setLayout(new GridBagLayout());

		painelMeio.setLayout(new GridBagLayout());

		JPanel painelBaixo = new JPanel();
		painelBaixo.setLayout(new GridBagLayout());

		constraints.insets = new Insets(3, 4, 3, 4);

		lblImg = new JLabel(new ImageIcon(getClass().getResource("/icons/icon_empregador.gif")));
		constraints.gridx = 0;
		constraints.gridy = 0;
		painelCima.add(lblImg, constraints);

		lblTitulo = new JLabel("Cadastro");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		painelCima.add(lblTitulo, constraints);

		painelGeral.add(painelCima, BorderLayout.NORTH);

		//-----------------------------------------------

		painelMeio.setBorder(BorderFactory.createEtchedBorder());
		painelGeral.add(painelMeio, BorderLayout.CENTER);

		//-----------------------------------------------

		constraints.ipadx = 20;

		btnConfirmar = new JButton("Confirmar (F1)", new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelBaixo.add(btnConfirmar, constraints);
		btnConfirmar.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F1"), "KEY_F1");
		btnConfirmar.getActionMap().put("KEY_F1", new AbstractAction() {
			private static final long serialVersionUID = -2764963705858862697L;

			public void actionPerformed(ActionEvent evt) {
				btnConfirmar.doClick();
		    }
		});

		btnCancelar = new JButton("Cancelar (F4)", new ImageIcon(getClass().getResource("/icons/icon_cancelar.gif")));
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelBaixo.add(btnCancelar, constraints);
		btnCancelar.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F4"), "KEY_F4");
		btnCancelar.getActionMap().put("KEY_F4", new AbstractAction() {
			private static final long serialVersionUID = -3110907542651221016L;

			public void actionPerformed(ActionEvent evt) {
				btnCancelar.doClick();
		    }
		});

		painelGeral.add(painelBaixo, BorderLayout.SOUTH);

		constraints.ipadx = 0;

		Container p = getContentPane();
		p.add(painelGeral);

		btnConfirmar.addActionListener(this);
		btnCancelar.addActionListener(this);

		KeyboardFocusManager.getCurrentKeyboardFocusManager()
		.addPropertyChangeListener("permanentFocusOwner", new PropertyChangeListener()
		{
			public void propertyChange(final PropertyChangeEvent e)
			{
				if (e.getNewValue() instanceof JTextField)
				{
					SwingUtilities.invokeLater(new Runnable()
					{
						public void run()
						{
							JTextField textField = (JTextField)e.getNewValue();
							textField.selectAll();
						}
					});
				}
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object origem = e.getSource();

		if (origem == btnCancelar)
			doDefaultCloseAction();
	}
}
