package safristas.vo.especiais;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import safristas.bo.EmpregadoBO;
import safristas.bo.safristas.EmpreiteiroBO;
import safristas.bo.safristas.EquipeBO;
import safristas.dao.EmpregadoDao;
import safristas.dao.PessoaDao;
import safristas.vo.FrmMenuGeralMaca;
import safristas.vo.consultcadast.FrmCadastraEmpregado;
import util.RendererJTree;

public class FrmVisaoGeral extends JInternalFrame implements ActionListener {
	private static final long serialVersionUID = -1501662160449268607L;

	protected GridBagConstraints constraints = new GridBagConstraints();

	protected JLabel lblTitulo, lblInfo, lblImg;
	protected JButton btnConfirmar, btnCancelar;
	
	private JTree arvore;
	private DefaultMutableTreeNode noRaiz = null;
	private RendererJTree renderer = new RendererJTree();
	private PessoaDao pessDao = new PessoaDao();
	private EmpregadoDao adoDao = new EmpregadoDao();
	public EmpregadoBO adoBO;

	protected JPanel painelMeio = new JPanel();

	protected Font f2 = new Font("Tahoma", Font.PLAIN, 14);

	public FrmVisaoGeral() {
		super("Visão geral pessoas",true,true,false,true);

		setFrameIcon(new ImageIcon(getClass().getResource("/icons/icon_logo_varaschin.gif")));
		setResizable(false);
		setSize(420, 760);

		JPanel painelGeral = new JPanel();
		painelGeral.setLayout(new BorderLayout());

		JPanel painelCima = new JPanel();
		painelCima.setLayout(new GridBagLayout());

		painelMeio.setLayout(new GridBagLayout());

		JPanel painelBaixo = new JPanel();
		painelBaixo.setLayout(new GridBagLayout());

		constraints.insets = new Insets(3, 4, 3, 4);

		lblImg = new JLabel(new ImageIcon(getClass().getResource("/icons/icon_relatorio.gif")));
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		painelCima.add(lblImg, constraints);

		lblTitulo = new JLabel("Visão geral Pessoas");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		painelCima.add(lblTitulo, constraints);
		
		lblInfo = new JLabel("Clique 2x no empregado para abrir seu cadastro.");
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		painelCima.add(lblInfo, constraints);

		painelGeral.add(painelCima, BorderLayout.NORTH);

		//-----------------------------------------------
		noRaiz = new DefaultMutableTreeNode("");
		arvore = new JTree(noRaiz);
		geraArvore();

		arvore.setCellRenderer(renderer);
		arvore.setDragEnabled(true);
		arvore.setTransferHandler(new TransferHandler(){
			@Override
			public boolean canImport(TransferSupport support) {
				support.setDropAction(COPY);
				// TODO Auto-generated method stub
				return super.canImport(support);
			}
			
			@Override
			public boolean importData(TransferSupport support) {
				return super.importData(support);
				
			}
		});

		arvore.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					DefaultMutableTreeNode no = (DefaultMutableTreeNode) arvore.getLastSelectedPathComponent();
					if (no == null) return;

					if (no.getLevel() == 3) {
						Object noSelecionado = no.getUserObject();

						String codigoString = noSelecionado.toString().substring(0, noSelecionado.toString().indexOf("-"));
						int codigoEmpregado = Integer.parseInt(codigoString);

						adoBO = adoDao.consultaPorCodigo(codigoEmpregado).get(0);

						FrmCadastraEmpregado fr = new FrmCadastraEmpregado(FrmVisaoGeral.this);
						fr.setVisible(true);
						getDesktopPane().add(fr);   
						try {
							fr.setSelected(true);
						}
						catch (PropertyVetoException exc) {}
						FrmMenuGeralMaca.centralizaInternalFrame(fr, getDesktopPane());
					}
				}
			}
		});

		JScrollPane scrollArvore = new JScrollPane(arvore);

		painelGeral.add(scrollArvore, BorderLayout.CENTER);
		//-----------------------------------------------

		constraints.ipadx = 0;
		constraints.ipady = -7;

		btnConfirmar = new JButton("Atualizar (F1)", new ImageIcon(getClass().getResource("/icons/icon_atualizar.gif")));
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelBaixo.add(btnConfirmar, constraints);
		btnConfirmar.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F1"), "KEY_F1");
		btnConfirmar.getActionMap().put("KEY_F1", new AbstractAction() {
			private static final long serialVersionUID = 1822556124503002905L;

			public void actionPerformed(ActionEvent evt) {
				btnConfirmar.doClick();
			}
		});

		constraints.ipadx = 20;
		constraints.ipady = 0;

		btnCancelar = new JButton("Sair (F4)", new ImageIcon(getClass().getResource("/icons/icon_cancelar.gif")));
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		painelBaixo.add(btnCancelar, constraints);
		btnCancelar.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F4"), "KEY_F4");
		btnCancelar.getActionMap().put("KEY_F4", new AbstractAction() {
			private static final long serialVersionUID = 5158056804716400309L;

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

	private void geraArvore() {

		noRaiz.removeAllChildren();

		ArrayList<EmpreiteiroBO> iroBO = pessDao.consultaEmpreiteiro();
		ArrayList<EquipeBO> equipeBO = pessDao.consultaEquipes();
		ArrayList<EmpregadoBO> adoBO = pessDao.consultaEmpregado();

		ArrayList<DefaultMutableTreeNode> noEmpreiteiro = new ArrayList<DefaultMutableTreeNode>();
		ArrayList<DefaultMutableTreeNode> noEquipe = new ArrayList<DefaultMutableTreeNode>();
		ArrayList<DefaultMutableTreeNode> noEmpregados = new ArrayList<DefaultMutableTreeNode>();

		int auxIro = 0, auxEquipe = 0, auxAdo = 0;

		do {
			noEmpreiteiro.add(new DefaultMutableTreeNode(iroBO.get(auxIro).getCodigo() + "-" + iroBO.get(auxIro).getNome()));
			noRaiz.add(noEmpreiteiro.get(auxIro));
			do {
				noEquipe.add(new DefaultMutableTreeNode(equipeBO.get(auxEquipe).getCodigo() + "-" + equipeBO.get(auxEquipe).getNome()));
				if (equipeBO.get(auxEquipe).iroBO.getCodigo() == iroBO.get(auxIro).getCodigo()) {
					noEmpreiteiro.get(auxIro).add(noEquipe.get(auxEquipe));
					do {
						noEmpregados.add(new DefaultMutableTreeNode(adoBO.get(auxAdo).getCodigo() + "-" + adoBO.get(auxAdo).getNome()));
						if (adoBO.get(auxAdo).equipeBO.getCodigo() == equipeBO.get(auxEquipe).getCodigo()) {
							noEquipe.get(auxEquipe).add(noEmpregados.get(auxAdo));
						}
						auxAdo++;
					} while (auxAdo < adoBO.size());
					auxAdo = 0;
				}
				auxEquipe++;
			} while (auxEquipe < equipeBO.size());
			auxEquipe = 0;
			auxIro++;
		} while (auxIro < iroBO.size());

		DefaultTreeModel model = (DefaultTreeModel)arvore.getModel();
		model.reload(noRaiz);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object origem = e.getSource();
		if (origem == btnConfirmar) {
			confirmar();
		} else if (origem == btnCancelar)
			doDefaultCloseAction();
	}

	public void confirmar() {
		geraArvore();
	}
}
