package basic4j.lang;

import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import basic4j.ValueSource;
import basic4j.ValueCalc;
import basic4j.lang.view.AssignmentView;

public class Assignment extends Step {
	public final String varName;
	public final ValueSource val;
	
	public Assignment(Step parent, int nr, Step next, String varName, ValueSource to) {
		this(parent, nr, new HashSet<>() /* no assigningOuterVars */, next, varName, to);
	}
	
	protected Assignment(Step parent, int nr, Set<String> assigningOuterVars, Step next, String varName, ValueSource val) {
		super(parent, nr, val.usedVars, assigningOuterVars, next);
		this.varName = varName;
		this.val = val;
	}

	@Override
	public ValueCalc execute(int t, Map<String, ValueCalc> varsBefore, Map<String, ValueCalc> _) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
		System.out.print(t + toBasic4JLine("\t", ""));
		
		//Object valToVal = valTo.execute(valsOnly(retainAll(varsBefore, valTo.usedVars)));
		ValueCalc valCalc = new ValueCalc(this, t, val, retainAll(varsBefore, val.usedVars));
		
		System.out.println(" " + valCalc.perform());
		
		HashMap<String, ValueCalc> varsAfter = new HashMap<>(varsBefore);
		varsAfter.put(varName, valCalc);
		if (this instanceof OuterAssignment) {
			_.put(varName, valCalc);
		}
		
		return nextStep.execute(t + 1, varsAfter, _);
	}
	
	protected void assigning(String varTo, ValueCalc val) {}

	@Override
	public String printJavaString(String prefix) {
		return prefix  + varName + " = " + val.toJavaString() + ";" +
				(maybeNextStep_Print.isPresent() ? "\r\n" + maybeNextStep_Print.get().printJavaString(prefix) : "");
	}

	@Override
	public String printBasic4JString(String allDesc, String codeDesc) throws NoSuchMethodException, SecurityException {
		return allDesc + nr() + "\t" + codeDesc + varName + " = " + val.toString() + ";" +
				(maybeNextStep_Print.isPresent() ? "\r\n" + maybeNextStep_Print.get().printBasic4JString(allDesc, codeDesc) : "");
	}

	@Override
	public String toBasic4JLine(String allDesc, String codeDesc) throws NoSuchMethodException, SecurityException {
		return allDesc + nr() + "\t" + codeDesc + varName + " = " + val.toString() + ";";
	}
	
	@Override
	public Component newView(Map<String, Type> declaredVars) {
		AssignmentView result = new AssignmentView("", declaredVars);
		result.setAssignment(declaredVars, this);
		return result;
	}

	@Override
	public void addStepViews(String prefix, Map<String, Type> declaredVars, Container into) throws NoSuchMethodException, SecurityException {
		into.add(this.newView(declaredVars));
		if (maybeNextStep_Print.isPresent()) {
			maybeNextStep_Print.get().addStepViews(prefix, declaredVars, into);
		}
	}
}
