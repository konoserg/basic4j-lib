package basic4j.lang.view;

import java.util.Map;

import basic4j.lang.If;
import basic4j.lang.Type;
import basic4j.lang.view.paint.Block;
import basic4j.lang.view.paint.Line;
import basic4j.lang.view.paint.SourceCode.Keyword;
import basic4j.lang.view.paint.SourceCode.Punctuation;

public class IfView extends Block {
	public IfView(String prefixBefore, Map<String, Type> declaredVars, If model) throws NoSuchMethodException, SecurityException {
		// line to line copy of this from If.toBasic4JString()
		//		return prefixBefore + "if(" + condition.toBasic4JString("") + ") {" + "\r\n" + 
		//		then.toBasic4JString(prefixBefore + "\t") + "\r\n" +
		//   prefixBefore + "}" + "\r\n" +
		//	(maybeElse.isPresent() ? 
		//   prefixBefore + "else {" + "\r\n" + 
		//   		maybeElse.get().toBasic4JString(prefixBefore + "\t") + "\r\n" + 
		//   prefixBefore + "}" : "") +
		//   (maybeNextPrint.isPresent() ? "\r\n" + maybeNextPrint.get().toBasic4JString(prefixBefore) : "");
		ValueSourceView condition = new ValueSourceView(declaredVars);
		condition.addAllChilds(declaredVars);
		condition.setValueSource(declaredVars, model.condition);
		
		Line header = this.addNewNonfocusableLine();
		header.add(new Keyword("if")).add(new Punctuation(" (")).add(condition).add(new Punctuation(") {"));
		{
			Block body = this.addNewBlock("    ");
			model.thenFirstStep.addStepViews("", declaredVars, body);
		}
		this.addNewNonfocusableLine().add(new Punctuation("}"));
		if (model.maybeElseFirstStep.isPresent()) {
			this.addNewNonfocusableLine().add(new Keyword("else")).add(new Punctuation(" {"));
			{
				Block body = this.addNewBlock("    ");
				model.maybeElseFirstStep.get().addStepViews("", declaredVars, body);
			}
			this.addNewNonfocusableLine().add(new Punctuation("}"));
		}
		// and then replaced all toBasic4JString() by addStepViews()
	}
}
