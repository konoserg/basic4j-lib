package basic4j.lang.view;

import javax.swing.JLabel;

import basic4j.lang.Step;
import basic4j.lang.view.paint.SourceCode;

public class LineView extends basic4j.lang.view.paint.Line {
	public LineView(String allDesc, String codeDesc, Step of) throws NoSuchMethodException, SecurityException {
		SourceCode.Call line = new SourceCode.Call(of.toBasic4JLine(allDesc, codeDesc));
		this.add(line);
	}
}
