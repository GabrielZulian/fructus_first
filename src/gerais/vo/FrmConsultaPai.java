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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public abstract class FrmConsultaPai extends JInternalFrame implements ActionListener {
	private static final long serialVersionUID = 364335289647644146L;
	
	protected JLabel lblConsultaPor;
	protected GridBagConstraints constraints = new GridBagConstraints();
	protected JComboBox cbConsulta;

	protected String opCbConsulta[] = {"Nome", "Código"};

	protected JTextField txtDadoConsulta;
	protected JButton btnConsultar, btnIncluir, btnAlterar, btnExcluir, btnSair;

	protected JPanel painelGeral = new JPanel();
	protected JPanel painelCima = new JPanel();
	protected JPanel painelMeio = new JPanel();
	protected JPanel painelBaixo = new JPanel();

	protected Font f = new Font("Arial", Font.BOLD, 14);
	protected Font f2 = new Font("Arial", Font.PLAIN, 14);

	protected Object origem = new Object();

	public FrmConsultaPai() {

		super("Consulta",true,true,true,true);
		setSize(800, 600);
		setResizable(false);
		setFrameIcon(new ImageIcon(getClass().getResource("/icons/icon_logo_varaschin.gif")));

		constraints.insets = new Insets(3, 7, 3, 7);

		painelGeral.setLayout(new BorderLayout());

		painelCima.setLayout(new GridBagLayout());

		painelMeio.setLayout(new GridBagLayout());

		painelBaixo.setLayout(new GridBagLayout());

		lblConsultaPor = new JLabel("Consulta por:");
		lblConsultaPor.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.ipadx = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelCima.add(lblConsultaPor, constraints);

		constraints.ipadx = 30;
		constraints.ipady = 5;

		cbConsulta = new JComboBox(opCbConsulta);
		cbConsulta.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelCima.add(cbConsulta, constraints);

		txtDadoConsulta = new JTextField(20);
		txtDadoConsulta.setFont(f2);
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.ipady = 12;
		constraints.anchor = GridBagConstraints.EAST;
		painelCima.add(txtDadoConsulta, constraints);
		
		constraints.gridx = -15;
		constraints.ipady = -6;

		btnConsultar = new JButton("Consultar (F5)", new ImageIcon(getClass().getResource("/icons/icon_procurarconsulta.gif")));
		btnConsultar.setFont(f);
		constraints.gridx = 3;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		painelCima.add(btnConsultar, constraints);
		btnConsultar.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F5"), "KEY_F5");
		btnConsultar.getActionMap().put("KEY_F5", new AbstractAction() {
			private static final long serialVersionUID = -5678183948846030223L;

			public void actionPerformed(ActionEvent evt) {
				btnConsultar.doClick();
		    }
		});

		constraints.ipadx = 15;
		constraints.ipady = -2;

		painelGeral.add(painelCima, BorderLayout.NORTH);

		btnIncluir = new JButton("Incluir (F1)" , new ImageIcon(getClass().getResource("/icons/icon_add.gif")));
		btnIncluir.setFont(f);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		painelBaixo.add(btnIncluir, constraints);
		btnIncluir.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F1"), "KEY_F1");
		btnIncluir.getActionMap().put("KEY_F1", new AbstractAction() {
			private static final long serialVersionUID = 3080928089591814405L;

			public void actionPerformed(ActionEvent evt) {
		        btnIncluir.doClick();
		    }
		});
		
		constraints.ipadx = 20;
		constraints.ipady = -2;

		btnAlterar = new JButton("Alterar (F2)", new ImageIcon(getClass().getResource("/icons/icon_editar.gif")));
		btnAlterar.setFont(f);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		painelBaixo.add(btnAlterar, constraints);
		btnAlterar.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F2"), "KEY_F2");
		btnAlterar.getActionMap().put("KEY_F2", new AbstractAction() {
			private static final long serialVersionUID = 5337762781892565180L;

			public void actionPerformed(ActionEvent evt) {
		        btnAlterar.doClick();
		    }
		});

		btnExcluir = new JButton("Excluir (F3)", new ImageIcon(getClass().getResource("/icons/icon_excluir.gif")));
		btnExcluir.setFont(f);
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		painelBaixo.add(btnExcluir, constraints);
		btnExcluir.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F3"), "KEY_F3");
		btnExcluir.getActionMap().put("KEY_F3", new AbstractAction() {
			private static final long serialVersionUID = 7232134275195855793L;

			public void actionPerformed(ActionEvent evt) {
		    	btnExcluir.doClick();
		    }
		});

		constraints.ipadx = 38;

		btnSair = new JButton("Sair (F4)", new ImageIcon(getClass().getResource("/icons/icon_sair.gif")));
		btnSair.setFont(f);
		constraints.gridx = 3;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		painelBaixo.add(btnSair, constraints);
		btnExcluir.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F4"), "KEY_F4");
		btnExcluir.getActionMap().put("KEY_F4", new AbstractAction() {
			private static final long serialVersionUID = 775119795940118861L;

			public void actionPerformed(ActionEvent evt) {
				btnSair.doClick();
		    }
		});

		constraints.ipadx = 0;
		constraints.ipady = 0;

		painelGeral.add(painelBaixo, BorderLayout.SOUTH);

		Container p = getContentPane();
		p.add(painelGeral);

		btnConsultar.addActionListener(this);
		btnIncluir.addActionListener(this);
		btnAlterar.addActionListener(this);
		btnExcluir.addActionListener(this);
		btnSair.addActionListener(this);

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
		origem = e.getSource();

		if (origem == btnSair) {
			doDefaultCloseAction();
		} else if (origem == btnExcluir) {
			try {
			if (JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir esse registro?", "Confirmar Exclus�o", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
				excluir();
			}
			} catch (ArrayIndexOutOfBoundsException erro) {
				JOptionPane.showMessageDialog(null, "Selecione um registro primeiro!", "Selecionar Registro", JOptionPane.INFORMATION_MESSAGE);
			}
		} else if (origem == btnConsultar) {
			consultar();
		} else if (origem == btnIncluir) {
			incluir();
		} else if (origem == btnAlterar) {
			alterar();
		}
	}

	public abstract void consultar();

	public abstract void incluir();

	public abstract void alterar();

	public abstract void excluir();
}