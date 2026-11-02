package basic4j.lang.view.paint;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ComponentComboBox<E extends Component> extends JComboBox<E> {
	public ComponentComboBox() {
		this.setRenderer(new ListCellRenderer<E>() { @Override public Component getListCellRendererComponent(
					JList<? extends E> list,
                    E item,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus) {
				if (item != null) {
					return item;
				} else {
					return new JLabel();
				}
			}
		});
	}
}
