package basic4j.lang;

import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import basic4j.ValueSource;
import basic4j.ValueCalc;
import basic4j.lang.view.DeclarationView;

public class Declaration extends Step {
	public final Type type;
	public final String varName;
	public final ValueSource initVal; // always there
	
	public Declaration(Step parent, int nr, Step next, Type type, String varName, ValueSource init) {
		super(parent, nr, init.usedVars, init.usedOuterVars, next);
		this.type = type;
		this.varName = varName;
		this.initVal = init;
	}
	
	public Declaration(Step parent, int nr, Step next, java.lang.Class<?> javaClass, Optional<java.lang.Class<?>[]> maybeTemplateTypes, String varName, ValueSource init) {
		super(parent, nr, init.usedVars, init.usedOuterVars, next);
		this.type = new Type(javaClass, maybeTemplateTypes);
		this.varName = varName;
		this.initVal = init;
	}
	
	@Override
	public ValueCalc execute(int t, Map<String, ValueCalc> varsBefore, Map<String, ValueCalc> _) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
		System.out.print(t + toBasic4JLine("\t", ""));
		
		//Object initValResult = initVal.execute(valsOnly(retainAll(varsBefore, initVal.usedVars)));
		ValueCalc val = new ValueCalc(this, t, initVal, retainAll(varsBefore, initVal.usedVars));
		
		System.out.println(" " + val.perform());
		
		HashMap<String, ValueCalc> varsAfter = new HashMap<>(varsBefore);
		varsAfter.put(varName, val);
		return nextStep.execute(t + 1, varsAfter, _);
	}

	@Override
	public String printJavaString(String prefix) {
		return prefix + type.toJavaString() + " " + varName + " = " + initVal.toJavaString() + ";" +
				(maybeNextStep_Print.isPresent() ? "\r\n" + maybeNextStep_Print.get().printJavaString(prefix) : "");
	}
	
	@Override
	public String printBasic4JString(String allDesc, String codeDesc) throws NoSuchMethodException, SecurityException {
		return allDesc + nr() + "\t" + codeDesc + type.toBasic4JString() + " " + varName + " = " + initVal.toString() + ";" +
				(maybeNextStep_Print.isPresent() ? "\r\n" + maybeNextStep_Print.get().printBasic4JString(allDesc, codeDesc) : "");
	}

	@Override
	public String toBasic4JLine(String allDesc, String codeDesc) throws NoSuchMethodException, SecurityException {
		return allDesc + nr() + "\t" + codeDesc + type.toBasic4JString() + " " + varName + " = " + initVal.toString() + ";";
	}
	
	@Override
	public Component newView(Map<String, Type> declaredVars) {
		DeclarationView result = new DeclarationView("", declaredVars);
		result.setDeclaration(declaredVars, this);
		return result;
	}
	
	@Override
	public void addStepViews(String prefix, Map<String, Type> declaredVarsBefore, Container into) throws NoSuchMethodException, SecurityException {
		into.add(this.newView(declaredVarsBefore));
		if (maybeNextStep_Print.isPresent()) {
			HashMap<String, Type> declaredVarsAfter = new HashMap<>(declaredVarsBefore);
			declaredVarsAfter.put(varName, type);
			maybeNextStep_Print.get().addStepViews(prefix, declaredVarsAfter, into);
		}
	}
}
