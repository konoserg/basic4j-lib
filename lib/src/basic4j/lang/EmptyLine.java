package basic4j.lang;

import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import basic4j.ValueCalc;
import basic4j.lang.view.LineView;

public class EmptyLine extends Step {

	public EmptyLine(Step parent, int nr, Step next) {
		super(parent, nr, new HashSet<>(), new HashSet<>(), next);
	}

	@Override
	public ValueCalc execute(int t, Map<java.lang.String, ValueCalc> vars, Map<String, ValueCalc> _) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
		System.out.println(t + this.toBasic4JLine("\t", ""));
		return nextStep.execute(t + 1, vars, _);
	}

	@Override
	public java.lang.String printJavaString(java.lang.String prefix) {
		return prefix +
				(maybeNextStep_Print.isPresent() ? "\r\n" + maybeNextStep_Print.get().printJavaString(prefix) : "");
	}

	@Override
	public java.lang.String printBasic4JString(String allDesc, String codeDesc) throws NoSuchMethodException, SecurityException {
		return allDesc + nr() + "\t" + codeDesc + 
				(maybeNextStep_Print.isPresent() ? "\r\n" + maybeNextStep_Print.get().printBasic4JString(allDesc, codeDesc) : "");
	}

	@Override
	public java.lang.String toBasic4JLine(String allDesc, String codeDesc) {
		return allDesc + nr() + "\t" + codeDesc;
	}
	
	@Override
	public Component newView(Map<String, Type> declaredVarNames) throws NoSuchMethodException, SecurityException {
		return new LineView("", "", this);
	}
	
	@Override
	public void addStepViews(String prefix, Map<String, Type> declaredVarNames, Container into) throws NoSuchMethodException, SecurityException {
		into.add(this.newView(declaredVarNames));
		if (maybeNextStep_Print.isPresent()) {
			maybeNextStep_Print.get().addStepViews(prefix, declaredVarNames, into);
		}
	}
}
