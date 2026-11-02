package basic4j.lang.view;

import java.util.Map;

import basic4j.lang.Return;
import basic4j.lang.Type;
import basic4j.lang.view.paint.Line;
import basic4j.lang.view.paint.SourceCode;

public class ReturnView extends Line {
	public ReturnView(Map<String, Type> declaredVars, Return model) {
		ValueSourceView that = new ValueSourceView(declaredVars);
		that.addAllChilds(declaredVars);
		that.setValueSource(declaredVars, model.val);
		
		this.add(new SourceCode.Keyword("return ")).add(that)/* .add(new SourceCode.Punctuation(";")) */;
	}
}
