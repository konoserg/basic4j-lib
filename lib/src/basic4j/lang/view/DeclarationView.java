package basic4j.lang.view;

import java.util.Map;

import basic4j.lang.Declaration;
import basic4j.lang.Type;
import basic4j.lang.view.paint.Line;
import basic4j.lang.view.paint.SourceCode;
import basic4j.lang.view.paint.SourceCode.Punctuation;

public class DeclarationView extends Line {
	private final TypeView varType;
	private final ValueSourceView init;
	private final SourceCode.Variable varName;
	
	public DeclarationView(String prefix, Map<String, Type> declaredVars) {
		// from Declaration.toBasic4JLine();
//		return prefix + type.getAnnotation(Basic4JType.class).name() + template + " " + varName + " = " + init.toBasic4JString("") + ";";
		
		this.varType = new TypeView();
		varType.addAllTypes();
		
		this.init = new ValueSourceView(declaredVars);
		init.addAllChilds(declaredVars);
		
		this.varName = new SourceCode.Variable("");
		
		this.add(varType).add(new Punctuation(" ")).add(varName).add(new Punctuation(" := ")) // ≔ (colon equals) here?
				.add(init)/* .add(new Punctuation(";")) */;
	}
	
	public void setDeclaration(Map<String, Type> declaredVars, Declaration model) {
		this.varType.setType(model.type);
		this.init.setValueSource(declaredVars, model.initVal);
		this.varName.setText(model.varName);
	}
}
