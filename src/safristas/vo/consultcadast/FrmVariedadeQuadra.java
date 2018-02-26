package safristas.vo.consultcadast;

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
import java.math.BigDecimal;

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

import safristas.bo.QuadVariedBO;

public class FrmVariedadeQuadra extends JInternalFrame implements ActionListener {
	private static final long serialVersionUID = 2760239445601121218L;

	protected GridBagConstraints constraints = new GridBagConstraints();
	protected JLabel lblTitulo, lblImg, lblVariedade, lblArea, lblCheckBox,lblValorInicial, lblValorDesconto,
	lblValorDescontoHAT, lblValorDecimoFerias, lblHistorico, lblCodAdiantamento, lblValorFinal;
	protected JTextField txtCodigo, txtArea, txtValorInicial, txtValorDesconto, txtValorDescontoHAT,
	txtValorDecimoFerias, txtCodAdiantamento, txtMostraAdiantamento, txtValorFinal;
	protected JButton btnConfirmar, btnCancelar, btnProcurarAdiantamento;
	protected String[] variedades = {"Maxi Gala", "Royal Gala", "Fuji Comum", "Fuji Suprema", "Fuji Kiku"};
	protected int[] codVariedades = {1,2,3,4,5};
	protected QuadVariedBO variquadBO;
	protected JComboBox<String> cbVariedade;

	protected JPanel painelMeio = new JPanel();
	protected Font f = new Font("Arial", Font.BOLD, 14);
	protected Font f2 = new Font("Tahoma", Font.PLAIN, 14);
	
	FrmCadastraQuadra cadQuadra = null;
	
	public FrmVariedadeQuadra(FrmCadastraQuadra frmCadastraQuadra) {
		this();
		cadQuadra = frmCadastraQuadra;
	}

	public FrmVariedadeQuadra() {
		super("Adicionar Variedade",false,true,false,true);

		setFrameIcon(new ImageIcon(getClass().getResource("/icons/icon_logo_varaschin.gif")));
		setResizable(false);
		setSize(350, 200);

		JPanel painelGeral = new JPanel();
		painelGeral.setLayout(new BorderLayout());

		JPanel painelCima = new JPanel();
		painelCima.setLayout(new GridBagLayout());

		painelMeio.setLayout(new GridBagLayout());

		JPanel painelBaixo = new JPanel();
		painelBaixo.setLayout(new GridBagLayout());

		constraints.insets = new Insets(3, 3, 3, 3);

		lblTitulo = new JLabel("Adicionar Variedade à Quadra");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		painelCima.add(lblTitulo, constraints);

		painelGeral.add(painelCima, BorderLayout.NORTH);

		//-----------------------------------------------
		lblVariedade = new JLabel("Variedade");
		lblVariedade.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblVariedade, constraints);

		cbVariedade = new JComboBox<String>(variedades);
		cbVariedade.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(cbVariedade, constraints);

		lblArea = new JLabel("Área");
		lblArea.setFont(f2);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		painelMeio.add(lblArea, constraints);

		txtArea = new JTextField(9);
		txtArea.setFont(f2);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.WEST;
		painelMeio.add(txtArea, constraints);

		painelGeral.add(painelMeio, BorderLayout.CENTER);

		//-----------------------------------------------

		constraints.ipadx = 12;

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

		if (origem == btnConfirmar) {

			BigDecimal area = new BigDecimal("0.00");

			try {
				area = new BigDecimal(txtArea.getText().replace(',', '.'));
			} catch (NumberFormatException erro) {
				JOptionPane.showMessageDialog(this, "Área incorreta ou inválida!", "ERRO", JOptionPane.ERROR_MESSAGE);
				txtArea.requestFocus();
				txtArea.selectAll();
				return;
			}

			cadQuadra.addVariedadeTabela(codVariedades[cbVariedade.getSelectedIndex()], cbVariedade.getSelectedItem().toString(), area);

			doDefaultCloseAction();
		} else if (origem == btnCancelar) {
			doDefaultCloseAction();
		}
	}
}