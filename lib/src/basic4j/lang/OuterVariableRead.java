package basic4j.lang;

public class OuterVariableRead extends VariableRead {

	public OuterVariableRead(String varName) {
		super(varName, varName /* maybeOuterVarName */);
	}
}
