package safristas.vo.lancamentos;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import safristas.bo.safristas.EquipeBO;
import safristas.dao.EmpregadoDao;
import safristas.dao.safristas.EquipeDao;
import safristas.vo.FrmMenuGeralMaca;
import safristas.vo.consultcadast.FrmConsultaEquipe;

public class FrmTrocarEquipePagos extends JInternalFrame implements ActionListener {
	private static final long serialVersionUID = 6373737040899714397L;

	protected GridBagConstraints constraints = new GridBagConstraints();
	protected JLabel lblTitulo, lblImg, lblCodigoEquipe;
	public JTextField txtCodigoEquipe, txtMostraEquipe;
	protected JButton btnConfirmar, btnCancelar, btnProcurarEquipe;

	protected JPanel painelMeio = new JPanel();
	protected Font f = new Font("Arial", Font.BOLD, 14);
	protected Font f2 = new Font("Tahoma", Font.PLAIN, 14);

	protected EquipeDao equipeDao = new EquipeDao();

	protected List<Integer> codPessoal;

	public FrmTrocarEquipePagos (List<Integer> codPessoal) {
		this();
		this.codPessoal = codPessoal;
	}

	private FrmTrocarEquipePagos() {
		super("Trocar Equipe",false,true,false,false);

		setFrameIcon(new ImageIcon(getClass().getResource("/icons/icon_logo_varaschin.gif")));
		setResizable(false);
		setSize(600, 200);

		JPanel painelGeral = new JPanel();
		painelGeral.setLayout(new BorderLayout());

		JPanel painelCima = new JPanel();
		painelCima.setLayout(new GridBagLayout());

		painelMeio.setLayout(new GridBagLayout());

		JPanel painelBaixo = new JPanel();
		painelBaixo.setLayout(new GridBagLayout());

		constraints.insets = new Insets(3, 3, 3, 3);

		lblTitulo = new JLabel("Trocar pessoal de equipe");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		painelCima.add(lblTitulo, constraints);

		painelGeral.add(painelCima, BorderLayout.NORTH);

		//-----------------------------------------------
		lblCodigoEquipe = new JLabel("Código Equipe");
		lblCodigoEquipe.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblCodigoEquipe, constraints);

		txtCodigoEquipe = new JTextField(4);
		txtCodigoEquipe.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtCodigoEquipe, constraints);

		txtMostraEquipe = new JTextField(18);
		txtMostraEquipe.setFont(f2);
		txtMostraEquipe.setFocusable(false);
		txtMostraEquipe.setEditable(false);
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtMostraEquipe, constraints);

		txtCodigoEquipe.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				int codEquipe = Integer.parseInt(txtCodigoEquipe.getText());
				EquipeBO equipeBO = new EquipeBO();
				equipeBO = equipeDao.consultaPorCodigo(codEquipe).get(0);

				txtMostraEquipe.setText(equipeBO.getNome());
			}
		});

		btnProcurarEquipe = new JButton("Procurar", new ImageIcon(getClass().getResource("/icons/icon_procurar.gif")));
		constraints.gridx = 3;
		constraints.gridy = 0;
		constraints.ipady = -5;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(btnProcurarEquipe, constraints);
		constraints.ipady = 0;

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
			private static final long serialVersionUID = 5779522274432064950L;

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
			private static final long serialVersionUID = -7547882810266193631L;

			public void actionPerformed(ActionEvent evt) {
				btnCancelar.doClick();
			}
		});

		painelGeral.add(painelBaixo, BorderLayout.SOUTH);

		constraints.ipadx = 0;

		Container p = getContentPane();
		p.add(painelGeral);

		btnProcurarEquipe.addActionListener(this);
		btnConfirmar.addActionListener(this);
		btnCancelar.addActionListener(this);

		addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameActivated(InternalFrameEvent e) {
				txtCodigoEquipe.requestFocusInWindow();
			}
		});

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
		if (origem == btnConfirmar) {
			int qntd = codPessoal.size();

			if (JOptionPane.showConfirmDialog(this, "Deseja mover " + qntd + " empregados para a equipe " 
					+ txtMostraEquipe.getText() +  " ?", "Confirmar troca", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)  {
				
				EmpregadoDao empregadoDao = new EmpregadoDao();
				
				empregadoDao.moverEmpregadosParaEquipe(codPessoal, Integer.parseInt(txtCodigoEquipe.getText()));
				
				JOptionPane.showMessageDialog(this, "Troca relizada com sucesso!", "Troca de equipe", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/icons/icon_ok.gif")));
				doDefaultCloseAction();
			}
		} else if (origem == btnCancelar) {
			doDefaultCloseAction();
		} else if (origem == btnProcurarEquipe) {
			FrmConsultaEquipe frame = new FrmConsultaEquipe(this);
			frame.setVisible(true);
			getDesktopPane().add(frame);
			try {
				frame.setSelected(true);
			}
			catch (PropertyVetoException exc) { }
			FrmMenuGeralMaca.centralizaInternalFrame(frame, getDesktopPane());
		}
	}
}