package basic4j.lang.view;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import basic4j.B4JInteger;
import basic4j.B4JMap;
import basic4j.B4JString;
import basic4j.Basic4JOperator;
import basic4j.Basic4JType;
import basic4j.ValueSource;
import basic4j.lang.ConstructorCall;
import basic4j.lang.FieldRead;
import basic4j.lang.IntLiteralConstructorCall;
import basic4j.lang.MethodCall;
import basic4j.lang.OperatorCall;
import basic4j.lang.StringLiteralConstructorCall;
import basic4j.lang.Type;
import basic4j.lang.VariableRead;
import basic4j.lang.view.paint.SelectionPanel;
import basic4j.lang.view.paint.SourceCode;

public class ValueSourceView extends SelectionPanel<SelectionPanel.Item> {
	public ValueSourceView(Map<String, Type> declaredVars) {
		this.addItem(EMPTY_ITEM);
		this.setSelectedItem(EMPTY_ITEM);
		
		this.intLitItem = new IntLiteralConstructorCallItem(declaredVars);
		this.strLitItem = new StringLiteralConstructorCallItem(declaredVars);
	}
	
	public void addAllChilds(Map<String, Type> declaredVars) {
		
		
		// Variables:
		for (Map.Entry<String, Type> each : declaredVars.entrySet()) {
			VariableReadItem an = new VariableReadItem(each.getValue(), declaredVars, each.getKey());
			this.addItem(an);
			//addChilds(declaredVars, an, each.getValue(), depth);
		}
		
		// ConstructorCalls:
		
		// Empty Map:
		this.addItem(new ConstructorCallItem(B4JMap.class, declaredVars, Optional.of(2), new Class<?>[0]));
				
		// Literals:
		this.addItem(this.intLitItem);
		//addChilds(declaredVars, intLitItem, new Type(B4JInteger.class), depth);
		this.addItem(this.strLitItem);
		//addChilds(declaredVars, strLitItem, new Type(B4JString.class), depth);
	
		//this.setSelectedItem(EMPTY_ITEM);
	}
	
	private final IntLiteralConstructorCallItem intLitItem;
	private final StringLiteralConstructorCallItem strLitItem;
	
	private final static HashSet<String> EXCLUDE_METHODS = new HashSet<String>(Arrays.asList("hashCode", "equals", "toString", "compareTo"));
		
	public void setValueSource(Map<String, Type> declaredVars, ValueSource vs) {
		if (vs != null) {
			List<ValueSource> path = path(vs);
			if (path.get(0) instanceof VariableRead) {
				this.setSelectedItem(new VariableReadItem(null, null, ((VariableRead)path.get(0)).varName));
			}  else if (path.get(0) instanceof IntLiteralConstructorCall) {
				IntLiteralConstructorCall ilcc = (IntLiteralConstructorCall) path.get(0);
				
				this.setSelectedItem(this.intLitItem);
				this.intLitItem.value.setText("" + ilcc.literalVal);
			} else if (path.get(0) instanceof StringLiteralConstructorCall) {
				StringLiteralConstructorCall slcc = (StringLiteralConstructorCall) path.get(0);
				
				this.setSelectedItem(this.strLitItem);
				this.strLitItem.content.setText(slcc.literalVal);
			} else if (path.get(0) instanceof ConstructorCall) {
				ConstructorCall cc = (ConstructorCall) path.get(0);
				
				this.setSelectedItem(new ConstructorCallItem(cc.type, null, Optional.empty(), cc.constrParams));
				ConstructorCallItem item = (ConstructorCallItem) this.getSelectedItem();
				item.setParamCalcs(declaredVars, cc.paramCalcs);
				item.setMaybeTemplateTypes(cc.maybeTemplateTypes);
			} else {
				new RuntimeException(vs.getClass().getSimpleName() + "?");
			}
			
			Item item = (Item) this.getSelectedItem();
			//item.addAllChilds(declaredVars); we now calling it on select event, calling in JComboBox.setSelectedItem
			
			if (path.size() > 1) {
				item.setValueSource(declaredVars, path.subList(1, path.size()));
			}
		}
	}
	
	private static List<ValueSource> path(ValueSource vs) {
		if (vs instanceof VariableRead || vs instanceof IntLiteralConstructorCall || vs instanceof StringLiteralConstructorCall || vs instanceof ConstructorCall) {
			return Arrays.asList(vs);
		} else if (vs instanceof MethodCall) {
			MethodCall mc = (MethodCall)vs;
			List<ValueSource> result = new ArrayList(path(mc.on));
			result.add(mc);
			return result;
		} else if (vs instanceof OperatorCall) {
			OperatorCall oc = (OperatorCall) vs;
			List<ValueSource> result = new ArrayList(path(oc.on));
			result.add(oc);
			return result;
		} else if (vs instanceof FieldRead) {
			FieldRead fr = (FieldRead) vs;
			List<ValueSource> result = new ArrayList(path(fr.on));
			result.add(fr);
			return result;
		} else {
			new RuntimeException(vs.getClass().getSimpleName() + "?");
		}
		return null;
	}
	
	private static final SelectionPanel.Item EMPTY_ITEM = new SelectionPanel.Item(new SourceCode.Punctuation("")) {

		@Override protected void youWasSelected(SelectionPanel<basic4j.lang.view.paint.SelectionPanel.Item> parent) {
			// do not collapse
			super.youWasSelected(parent);
		}
	};
	
	public static class Item extends SelectionPanel.Item {
		private final Type type;
		protected final Map<String, Type> declaredVars;
		protected final SelectionPanel<SelectionPanel.Item> childs = new SelectionPanel();
		
		public Item(Type type, Map<String, Type> declaredVars) {
			this.type =  type;
			this.declaredVars = declaredVars;
			
			this.childs.addItem(EMPTY_ITEM);
			this.childs.setSelectedItem(EMPTY_ITEM);
		}
		
		@Override protected void youWasSelected(SelectionPanel<SelectionPanel.Item> parent) {
			// collapse
			super.youWasSelected(parent);
			this.addAllChilds(this.declaredVars);
		}

		public void addAllChilds(Map<String, Type> declaredVars) {
			
			
			//if (depth > 0) {
				//System.out.println(this + ":");
				
				// Fields:
				for (int i = type.javaClass.getDeclaredFields().length - 1; i >= 0; i --) if (Modifier.isPublic(type.javaClass.getDeclaredFields()[i].getModifiers())) {
					Field each = type.javaClass.getDeclaredFields()[i];
					Class<?> javaClass;
					if (type.maybeTemplateTypes.isPresent()) {
						javaClass = type.maybeTemplateTypes.get()[i];
					} else {
						javaClass = each.getType();
					}
					
					//System.out.println(" - " + javaClass.getSimpleName() + " " + each.getName());
					
					FieldReadItem an = new FieldReadItem(new Type(javaClass), declaredVars, type.javaClass, each.getName());
					this.childs.addItem(an);
					//addChilds(declaredVars, an, new Type(each.getType()), depth - 1);
				}
				
				for (Method each: type.javaClass.getDeclaredMethods()) if (Modifier.isPublic(each.getModifiers()) && !EXCLUDE_METHODS.contains(each.getName())) {
					if (each.getParameterTypes().length == 0) {
						//System.out.println(" - " + each);
						FieldReadItem an = new FieldReadItem(new Type(each.getReturnType()), declaredVars, type.javaClass, each.getName());
						this.childs.addItem(an);
						//addChilds(declaredVars, an, new Type(each.getReturnType()), depth - 1);
					}
				}
				
				// Methods:
				for (Method each: type.javaClass.getDeclaredMethods()) if (Modifier.isPublic(each.getModifiers()) && !EXCLUDE_METHODS.contains(each.getName())) {
					if (each.getParameterTypes().length > 0) { /* all other are FieldReadItems now */
						//System.out.println(" - " + each);
						MethodCallItem an = new MethodCallItem(new Type(each.getReturnType()), declaredVars, each);
						this.childs.addItem(an);
						//addChilds(declaredVars, an, new Type(each.getReturnType()), depth - 1);
					}
				}
				
				// Operators:
				for (Method each: type.javaClass.getDeclaredMethods()) if (Modifier.isPublic(each.getModifiers()) && !EXCLUDE_METHODS.contains(each.getName())) {
					if (each.getParameterTypes().length == 1 && each.isAnnotationPresent(Basic4JOperator.class)) {
						//System.out.println(" - " + each);
						OperatorCallItem an = new OperatorCallItem(new Type(each.getReturnType()), declaredVars, each);
						this.childs.addItem(an);
						//addChilds(declaredVars, an, new Type(each.getReturnType()), depth - 1);
					}
				}
			//}
			
			
		}

		public void setValueSource(Map<String, Type> declaredVars, List<ValueSource> path) {
			Item selected = null;
			if (path.get(0) instanceof MethodCall) {
				MethodCall mc = (MethodCall)path.get(0);
				this.childs.setSelectedItem(new MethodCallItem(null, null, mc.method));
				MethodCallItem item = (MethodCallItem)this.childs.getSelectedItem();
				item.setParamCalcs(declaredVars, mc.paramCalcs);
				selected = item;
			} else if (path.get(0) instanceof OperatorCall) {
				OperatorCall oc = (OperatorCall) path.get(0);
				this.childs.setSelectedItem(new OperatorCallItem(null, null, oc.method));
				if (this.childs.getSelectedItem() instanceof OperatorCallItem) {
					OperatorCallItem item = (OperatorCallItem) this.childs.getSelectedItem();
					item.setArgCalc(declaredVars, oc.argCalc);
					selected = item;
				}
			} else if (path.get(0) instanceof FieldRead) {
				FieldRead fr = (FieldRead) path.get(0);
				this.childs.setSelectedItem(new FieldReadItem(null, null, fr.clazz, fr.fieldOrMethodName));
				selected = (Item) this.childs.getSelectedItem();
			}
			
			//selected.addAllChilds(declaredVars); we now calling it on select event, calling in JComboBox.setSelectedItem
			if (path.size() > 1) {
				selected.setValueSource(declaredVars, path.subList(1, path.size()));
			}
		}
	}
	
	public static class VariableReadItem extends Item {
		private final String varName;
		
		public VariableReadItem(Type type, Map<String, Type> declaredVars, String varName) {
			super(type, declaredVars);
			this.varName = varName;
			
			this.add(new SourceCode.Variable(varName)).add(childs);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + varName.hashCode();
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
			VariableReadItem other = (VariableReadItem) obj;
			return varName.equals(other.varName);
		}
		
		public String toString() {
			return varName;
		}
	}
	
	public static class IntLiteralConstructorCallItem extends Item {
		private SourceCode.Literal value;
		
		private IntLiteralConstructorCallItem(Map<String, Type> declaredVars) {
			super(new Type(B4JInteger.class), declaredVars);
			value = new SourceCode.Literal("");
			
			this.add(value).add(childs);
		}
	}
	
	public static class StringLiteralConstructorCallItem extends Item {
		private SourceCode.Literal content;

		private StringLiteralConstructorCallItem(Map<String, Type> declaredVars) {
			super(new Type(B4JString.class), declaredVars);
			
			content = new SourceCode.Literal("");
			
			this.add(new SourceCode.Literal("'")).add(content).add(new SourceCode.Literal("'")).add(childs);
		}
	}
	
	public static class ConstructorCallItem extends Item {
		public final Class<?> type;
		private final Optional<TypeView[]> maybeTemplateTypes;
		public final Class<?>[] constrParams;
		private final ValueSourceView[] paramCalcs;

		private ConstructorCallItem(Class<?> type, Map<String, Type> declaredVars, Optional<Integer> maybeNumTemplateTypes, Class<?>[] constrParams) {
			super(new Type(type), declaredVars);
			this.type = type;
			this.constrParams = constrParams;
			
			this.add(new SourceCode.Call(type.getAnnotation(Basic4JType.class).name()));
			
			if (maybeNumTemplateTypes.isPresent()) {
				this.maybeTemplateTypes = Optional.of(new TypeView[maybeNumTemplateTypes.get()]);
				if (false) this.add(new SourceCode.Punctuation("<"));
				for (int i = 0; i < maybeNumTemplateTypes.get(); i ++) {
					TypeView templateType = new TypeView();
					templateType.addAllTypes();
					this.maybeTemplateTypes.get()[i] = templateType;
					
					if (i != 0) {
						if (false) this.add(new SourceCode.Punctuation(", "));
					}
					
					if (false) this.add(templateType);
				}
				
				if (false) this.add(new SourceCode.Punctuation(">"));
			} else {
				this.maybeTemplateTypes = Optional.empty();
			}
			
			this.add(new SourceCode.Punctuation("("));
			
			paramCalcs = new ValueSourceView[constrParams.length];
			boolean hasAlready = false;
			for (int i = 0; i < constrParams.length; i ++) {
				if (hasAlready) {
					this.add(new SourceCode.Punctuation(", "));
				}
				
				paramCalcs[i] = new ValueSourceView(declaredVars);
				//paramCalcs[i].addAllChilds(declaredVars); we now doint it on select event (called in the JComboBox.setSelectedItem())
				this.add(paramCalcs[i]);
				hasAlready = true;
			}
			
			this.add(new SourceCode.Punctuation(")"));
			this.add(childs);
		}
		
		
		
		@Override
		protected void youWasSelected(SelectionPanel<SelectionPanel.Item> parent) {
			super.youWasSelected(parent);
			
			for (int i = 0; i < paramCalcs.length; i ++) {
				paramCalcs[i].addAllChilds(this.declaredVars);
			}
		}



		public void setMaybeTemplateTypes(Optional<java.lang.Class<?>[]> maybeTemplateTypes) {
			if (maybeTemplateTypes.isPresent()) {
				for (int i = 0; i < this.maybeTemplateTypes.get().length; i ++) {
					this.maybeTemplateTypes.get()[i].setType(new Type(maybeTemplateTypes.get()[i]));
				}
			}
		}

		public void setParamCalcs(Map<String, Type> declaredVars, ValueSource[] paramCalcs) {
			 for (int i = 0; i < this.paramCalcs.length; i ++) {
				 this.paramCalcs[i].setValueSource(declaredVars, paramCalcs[i]);
			 }
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + type.hashCode();
			result = prime * result + Arrays.hashCode(constrParams);
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
			ConstructorCallItem other = (ConstructorCallItem) obj;
			if (!type.equals(other.type)) {
				return false;
			}
				
			return Arrays.equals(constrParams, other.constrParams);
		}

		@Override
		public String toString() {
			return type.getSimpleName() + "(" + Arrays.toString(constrParams) + ")";
		}
	}
	
	public static class MethodCallItem extends Item {
		private final String signature;
		private final ValueSourceView[] paramCalcs;
		
		public MethodCallItem(Type type, Map<String, Type> declaredVars, Method of) {
			super(type, declaredVars);
			this.signature = signature(of);
			
			this.add(new SourceCode.Punctuation(".")).add(new SourceCode.Call(of.getName())).add(new SourceCode.Punctuation("("));
			
			this.paramCalcs = new ValueSourceView[of.getParameterTypes().length];
			boolean addedAlready = false;
			for (int i = 0; i < of.getParameterTypes().length; i ++) {
				if (addedAlready) {
					this.add(new SourceCode.Punctuation(", "));
				}
				
				//this.add(new SourceCode.Type(each.getSimpleName()));
				this.paramCalcs[i] = new ValueSourceView(declaredVars);
				//this.paramCalcs[i].addAllChilds(declaredVars); we now doing it on select event (called in the JComboBox.setSelectedItem())
				this.add(this.paramCalcs[i]);
				addedAlready = true;
			}
			
			this.add(new SourceCode.Punctuation(")"));
			this.add(childs);
		}
		
		@Override protected void youWasSelected(SelectionPanel<SelectionPanel.Item> parent) {
			super.youWasSelected(parent);
			
			for (int i = 0; i < paramCalcs.length; i ++) {
				this.paramCalcs[i].addAllChilds(this.declaredVars);
			}
		}

		public void setParamCalcs(Map<String, Type> declaredVars, ValueSource[] paramCalcs) {
			assert paramCalcs.length == this.paramCalcs.length;
			
			for (int i = 0; i < this.paramCalcs.length; i ++) {
				this.paramCalcs[i].setValueSource(declaredVars, paramCalcs[i]);
			}
		}
		
		private static String signature(Method of) {
			String paramsString = "";
			for (Class<?> each: of.getParameterTypes()) {
				if (!paramsString.isEmpty()) {
					paramsString += ", ";
				}
				
				paramsString += each.getSimpleName();
			}
			
			return "." + of.getName() + "(" + paramsString + ")";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + signature.hashCode();
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
			MethodCallItem other = (MethodCallItem) obj;
			return signature.equals(other.signature);
		}

		@Override
		public String toString() {
			return signature;
		}
	}
	
	public static class FieldReadItem extends Item {
		public final java.lang.Class clazz;
		public final String fieldOrMethodName;
		
		public FieldReadItem(Type type, Map<String, Type> declaredVars, java.lang.Class clazz, String fieldOrMethodName) {
			super(type, declaredVars);
			this.clazz = clazz;
			this.fieldOrMethodName = fieldOrMethodName;
			
			this.add(new SourceCode.Punctuation(".")).add(new SourceCode.Call(fieldOrMethodName)).add(childs);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + clazz.hashCode();
			result = prime * result + fieldOrMethodName.hashCode();
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
			FieldReadItem other = (FieldReadItem) obj;
			if (!clazz.equals(other.clazz)) {
				return false;
			}
				
			return fieldOrMethodName.equals(other.fieldOrMethodName);
		}

		@Override
		public String toString() {
			return "." + (clazz.getAnnotation(Basic4JType.class) != null ? ((Basic4JType)clazz.getAnnotation(Basic4JType.class)).name() : clazz.getSimpleName()) + "::" + fieldOrMethodName;
		}
	}
	
	public static class OperatorCallItem extends Item {
		private final String signature;
		private final ValueSourceView argCalc;
		
		public OperatorCallItem(Type type, Map<String, Type> declaredVars, Method model) {
			super(type, declaredVars);
			this.signature = signature(model);
			
			if (model != null) { // why it can be null?
				this.argCalc = new ValueSourceView(declaredVars);
				//this.argCalc.addAllChilds(declaredVars); // we now doing it on select event (called in the JComboBox.setSelectedItem())
				
				this.add(new SourceCode.Punctuation(" "));
				this.add(new SourceCode.Punctuation(model.getAnnotation(Basic4JOperator.class).name()));
				this.add(new SourceCode.Punctuation(" "));
				this.add(argCalc);
				this.add(childs);
			}
			else {
				this.argCalc = null;
			}
		}
		
		@Override
		protected void youWasSelected(SelectionPanel<SelectionPanel.Item> parent) {
			super.youWasSelected(parent);
			
			if (this.argCalc != null) { // copied from constructor
				this.argCalc.addAllChilds(this.declaredVars);
			}
		}

		public void setArgCalc(Map<String, Type> declaredVars, ValueSource argCalc) {
			this.argCalc.setValueSource(declaredVars, argCalc);
		}
		
		private static String signature(Method of) {
			return of != null ? (" " + of.getName() + " " + ((Basic4JType)of.getParameterTypes()[0].getAnnotation(Basic4JType.class)).name()) : "";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((signature == null) ? 0 : signature.hashCode());
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
			OperatorCallItem other = (OperatorCallItem) obj;
			if (signature == null) {
				if (other.signature != null)
					return false;
			} else if (!signature.equals(other.signature))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return signature != null ? signature : "";
		}
	}
}
