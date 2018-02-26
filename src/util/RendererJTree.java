package util;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class RendererJTree extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = -6321652970855576136L;
	Icon iconVaraschin, iconEmpreiteiros, iconEquipes, iconEmpregados;

	public RendererJTree() {
		iconVaraschin = new ImageIcon(getClass().getResource("/icons/icon_logo_varaschin.gif"));
		iconEmpreiteiros = new ImageIcon(getClass().getResource("/icons/icon_empreiteirop.gif"));
		iconEquipes = new ImageIcon(getClass().getResource("/icons/icon_equipep.gif"));
		iconEmpregados = new ImageIcon(getClass().getResource("/icons/icon_empregadop.gif"));
	}

	public Component getTreeCellRendererComponent(
			JTree tree,
			Object value,
			boolean sel,
			boolean expanded,
			boolean leaf,
			int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(
				tree, value, sel,
				expanded, leaf, row,
				hasFocus);

		int nivel = ((DefaultMutableTreeNode) value).getLevel();

		if (nivel == 0) {
			setIcon(iconVaraschin);
		} else if (nivel == 1) {
			setIcon(iconEmpreiteiros);
		} else if (nivel == 2) {
			setIcon(iconEquipes);
		} else if (nivel == 3) {
			setIcon(iconEmpregados);
		}  

		return this;
	}
}
