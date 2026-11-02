package basic4j.lang.view.paint;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JPanel;

public class SelectionPanel<E extends SelectionPanel.Item> extends Line {
	private final ComponentComboBox<E> combo = new ComponentComboBox();
	
	public SelectionPanel() {
		//this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		//this.setBackground(Area.BACKGROUND_COLOR);
		
		//combo.setFocusable(true);
		this.add(combo);
		
		combo.addItemListener(new ItemListener() { @Override public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {// && !combo.isFocusOwner()) {
		          //Object selItem = e.getItem();
		          //ComponentCollapseBox.this.remove(combo);
		          //Component view = selItem instanceof Component ? ((Component)selItem) : ((ComponentComboBox.Item)selItem).view;
		          //ComponentCollapseBox.this.add(view);
				Item selItem = SelectionPanel.this.getSelectedItem();
		          System.out.println("Selected \"" + selItem + "\"");
		          
//		          if (combo.hasFocus()) {
//		        	  KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
//		          } else {
//			          ComponentCollapseBox.this.remove(combo);
//			          Component view = selItem instanceof Component ? ((Component)selItem) : ((ComponentComboBox.Item)selItem).view;
//			          ComponentCollapseBox.this.add(view);
//		          }
		          
		          selItem.youWasSelected((SelectionPanel<Item>) SelectionPanel.this);
		       }
		}});
		
		combo.addFocusListener(new FocusListener(
				) {
			
			@Override
			public void focusLost(FocusEvent e) {
				//System.out.println("focusLost " + e.getComponent());
				//evtlCollapse();
				
//				Object selItem = combo.getSelectedItem();
//				ComponentCollapseBox.this.remove(combo);
//		        Component view = selItem instanceof Component ? ((Component)selItem) : ((ComponentComboBox.Item)selItem).view;
//		        ComponentCollapseBox.this.add(view);
		          //System.out.println("Selected \"" + item + "\"");
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				//System.out.println("focusGained " + e.getComponent());
				
//				ComponentCollapseBox.this.removeAll();
//				ComponentCollapseBox.this.add(combo);
			}
		});
	}
	
	public void addItem(E item) {
		combo.addItem(item);
	}
	
	public void setSelectedItem(E item) {
		//combo.requestFocus();
		combo.setSelectedItem(item);
		//KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
		//this.evtlCollapse();
	}
	
	public E getSelectedItem() {
		return (E) combo.getSelectedItem();
	}
	
	public int getItemCount() {
		return combo.getItemCount();
	}
	
	public E getItemAt(int i) {
		return combo.getItemAt(i);
	}
	
	public static class Item extends Line {
		public Item() {}
		
		public Item(Component c) {
			super();
			/*Line.*/this.add(c);
		}
		
		protected void youWasSelected(SelectionPanel<Item> parent) {
			parent.remove(parent.combo);
	        parent.add(this);
	          
	        parent.revalidate();
	        parent.repaint();
		}
	}
}
