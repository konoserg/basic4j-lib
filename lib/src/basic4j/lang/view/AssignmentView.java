package basic4j.lang.view;

import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;

import basic4j.lang.Assignment;
import basic4j.lang.Type;
import basic4j.lang.view.paint.Line;
import basic4j.lang.view.paint.SelectionPanel;
import basic4j.lang.view.paint.SourceCode;
import basic4j.lang.view.paint.SourceCode.Punctuation;

public class AssignmentView extends Line {
	private final VariableView var;
	private final ValueSourceView to;
	
	public AssignmentView(String prefix, Map<String, Type> declaredVars) {
		// from Assignement.toBasic4JLine():
		// prefix + varName + " = " + to.toBasic4JString("") + ";"
		var = new VariableView(declaredVars);
		to = new ValueSourceView(declaredVars);
		to.addAllChilds(declaredVars);
		
		this.add(var).add(new Punctuation(" ← ")).add(to)/*.add(new Punctuation(";"))*/;
		
		{
		JButton newBtn = new JButton("A");
		//this.add(newBtn);
		newBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				AssignmentView a = new AssignmentView("", declaredVars);
				AssignmentView.this.getParent().add(a);
				AssignmentView.this.getParent().revalidate();
				AssignmentView.this.getParent().repaint();
				
				KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
			}
		});
		}
		
		{
		JButton newBtn = new JButton("D");
		//this.add(newBtn);
		newBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				DeclarationView a = new DeclarationView("", declaredVars);
				AssignmentView.this.getParent().add(a);
				AssignmentView.this.getParent().revalidate();
				AssignmentView.this.getParent().repaint();
				
				KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
			}
		});
		}
	}
	
	public void setAssignment(Map<String, Type> declaredVars, Assignment model) {
		var.setVariable(model.varName);
		to.setValueSource(declaredVars, model.val);
	}
	
	public static class VariableView extends SelectionPanel<VariableView.Item> {
		public VariableView(Map<String, Type> declaredVarNames) {
			this.addItem(new Item(null));
			this.setSelectedItem(new Item(null));
			for (Map.Entry<String, Type> each: declaredVarNames.entrySet()) {
				this.addItem(new Item(each.getKey()));
			}
		}
		
		public void setVariable(String var) {
			this.setSelectedItem(new Item(var));
		}
		
		public class Item extends SelectionPanel.Item {
			private final String var;
			
			public Item(String var) {
				this.var = var;
				this.add(new SourceCode.Variable(var != null ? var.toString() : ""));
			}
			
			@Override
			protected void youWasSelected(SelectionPanel<SelectionPanel.Item> parent) {
				if (var != null) {
					// collapse
					super.youWasSelected(parent);
				}
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + ((var == null) ? 0 : var.hashCode());
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				Item other = (Item) obj;
				if (var == null) {
					if (other.var != null)
						return false;
				} else if (!var.equals(other.var))
					return false;
				return true;
			}
			
			public String toString() {
				return var != null ? var.toString() : "";
			}
		}
	}
}
