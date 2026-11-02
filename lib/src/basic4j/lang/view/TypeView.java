package basic4j.lang.view;

import java.util.Map;

import basic4j.B4JEntry;
import basic4j.B4JFloat;
import basic4j.B4JInteger;
import basic4j.B4JList;
import basic4j.B4JMap;
import basic4j.B4JString;
import basic4j.Basic4JType;
import basic4j.lang.Type;
import basic4j.lang.view.paint.SelectionPanel;
import basic4j.lang.view.paint.SourceCode;
import basic4j.lang.view.paint.SourceCode.Punctuation;

public class TypeView extends SelectionPanel<TypeView.Item> {
	/**
	 * Add to this all your custom Basic4JTypes.
	 */
	public static final java.util.HashMap<java.lang.Class<?>, Integer> ALL = new java.util.HashMap<>();
	static {
		ALL.put(B4JFloat.class, 0);
		ALL.put(B4JInteger.class, 0);
		ALL.put(B4JString.class, 0);
		ALL.put(B4JList.class, 1);
		ALL.put(B4JMap.class, 2);
		ALL.put(B4JEntry.class, 2);
	}

	public TypeView() {
		this.addItem(new Item(null, -1));
	}
	
	public void addAllTypes() {
		for (Map.Entry<java.lang.Class<?>, Integer> each: ALL.entrySet()) {
			this.addItem(new Item(each.getKey(), each.getValue()));
		}
	}
	
	public void setType(Type t) {
		this.setSelectedItem(new Item(t.javaClass, -1));
		
		Item item = (Item) this.getSelectedItem();
		if (t.maybeTemplateTypes.isPresent()) {
			for (int i = 0; i < item.templateTypes.length; i ++) {
				//item.templateTypes[i].addAllTypes(); we doing it now on select event in the Item itself in the Item.youWasSelected()
				item.templateTypes[i].setType(new Type(t.maybeTemplateTypes.get()[i]));
			}
		}
	}
	
	public class Item extends SelectionPanel.Item {
		public final java.lang.Class<?> javaClass;
		private final TypeView[] templateTypes;
		
		public Item(java.lang.Class<?> javaClass, int numTemplateTypes) {
			this.javaClass = javaClass;
			this.add(new SourceCode.Type(javaClass != null ? javaClass.getAnnotation(Basic4JType.class).name() : ""));
			
			if (numTemplateTypes >= 0) {
				this.templateTypes = new TypeView[numTemplateTypes];
				if (numTemplateTypes > 0) {
					this.add(new Punctuation("<"));
					for (int i = 0; i < numTemplateTypes; i ++) {
						if (i != 0) {
							this.add(new Punctuation(", "));
						}
						
						TypeView templateTypesOne = new TypeView();
						this.templateTypes[i] = templateTypesOne;
						this.add(templateTypesOne);
					}
					
					this.add(new Punctuation(">"));
				}
			}
			else {
				this.templateTypes = null;
			}
		}

		@Override
		protected void youWasSelected(SelectionPanel<SelectionPanel.Item> parent) {
			if (javaClass != null) {
				// collapse
				super.youWasSelected(parent);
				
				for (int i = 0; i < this.templateTypes.length; i ++) {
					this.templateTypes[i].addAllTypes();
				}
			}
		}

		@Override
		public String toString() {
			return javaClass != null ? javaClass.getSimpleName().toString() : "";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((javaClass == null) ? 0 : javaClass.hashCode());
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
			if (javaClass == null) {
				if (other.javaClass != null)
					return false;
			} else if (!javaClass.equals(other.javaClass))
				return false;
			return true;
		}
	}
}
