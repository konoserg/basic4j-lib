package basic4j.lang.view;

import java.util.Map;

import basic4j.lang.For;
import basic4j.lang.Type;
import basic4j.lang.view.paint.Block;
import basic4j.lang.view.paint.Line;
import basic4j.lang.view.paint.SourceCode;
import basic4j.lang.view.paint.SourceCode.Keyword;
import basic4j.lang.view.paint.SourceCode.Punctuation;

public class ForView extends Block {
	public ForView(String prefix, Map<String, Type> declaredVars, For model) throws NoSuchMethodException, SecurityException {
		// line to line copy of this
//		return prefix + "for(" + of.elemType.getAnnotation(Basic4JType.class).name() + template + " each: " + of.on.toBasic4JString("") + ") {" + "\r\n" +
//			of.body.toBasic4JString(prefix + "\t") + "\r\n" + 
//			prefix + "}";
		//this.add(newLine(prefix + "for(" + of.elemType.getAnnotation(Basic4JType.class).name() + template + " each: " + of.on.toBasic4JString("") + ") {"), BorderLayout.PAGE_START);
		{
			Line header = this.addNewNonfocusableLine();

			TypeView elementType = new TypeView();
			elementType.addAllTypes();
			elementType.setType(model.elemType);

			ValueSourceView valueSource = new ValueSourceView(declaredVars);
			valueSource.addAllChilds(declaredVars);
			valueSource.setValueSource(declaredVars, model.elemsSrc);

			header.add(new Keyword("for")).add(new Punctuation(" (")).add(elementType)
					.add(new SourceCode.Variable(" " + model.elemName)).add(new Punctuation(": ")).add(valueSource)
					.add(new Punctuation(") {"));
		}
		
		Block body = this.addNewBlock("    ");
		model.bodyFirstStep.addStepViews("", declaredVars, body);
		
		this.addNewNonfocusableLine().add(new Punctuation("}"));
		// and then replaced all toBasic4JString() by addStepViews()
	}
}
