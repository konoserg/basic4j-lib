package basic4j.lang;

import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import basic4j.ValueCalc;
import basic4j.ValueSource;
import basic4j.lang.view.IfView;

public class If extends Step {
	public final ValueSource condition;
	public Step thenFirstStep;
	public Optional<Step> maybeElseFirstStep;
	
	public If(Step parent, int nr, Step next, ValueSource condition, Step then) {
		this(parent, nr, next, condition, then, Optional.empty());
	}
	
	public If(Step parent, int nr, Step next, ValueSource condition, Step thenFirst, Optional<Step> maybeElseFirst) {
		super(parent, nr, condition.usedVars, condition.usedOuterVars, next);
		this.condition = condition;
		this.thenFirstStep = thenFirst;
		this.maybeElseFirstStep = maybeElseFirst;
	}
	
	private HashMap<String, ValueCalc> outerVars;
	private HashMap<String, ValueCalc> outerOuterVarsAssignedInside;
	
	@Override
	public ValueCalc execute(int t, Map<String, ValueCalc> varsBefore, Map<String, ValueCalc> outerVarsAssignedInside) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
		outerVars = new HashMap<>(varsBefore);
		outerOuterVarsAssignedInside = new HashMap<>(outerVarsAssignedInside);
		outerVarsAssignedInside = new HashMap<>();
		
		ValueCalc conditionCalc = new ValueCalc(this, t, this.condition, retainAll(varsBefore, this.condition.usedVars));
		if ((boolean) conditionCalc.perform()) {
			HashMap<String, ValueCalc> varsAfter = new HashMap<>(varsBefore);
			return thenFirstStep.execute(t + 1, varsAfter, outerVarsAssignedInside);
		}
		else {
			if (maybeElseFirstStep.isPresent()) {
				HashMap<String, ValueCalc> varsAfter = new HashMap<>(varsBefore);
				return maybeElseFirstStep.get().execute(t + 1, varsAfter, outerVarsAssignedInside);
			}
			else {
				HashMap<String, ValueCalc> varsNew = new HashMap<>(outerVars);
				outerVars = null;
				for (Map.Entry<String, ValueCalc> each: outerVarsAssignedInside.entrySet()) {
					String varName = each.getKey();
					ValueCalc valInside = each.getValue();
					//varsNew.put(varName, new Value(this, t, valInside.val));
					varsNew.put(varName, valInside);
				}
				
				return nextStep.execute(t + 1, varsNew, outerOuterVarsAssignedInside);

			}
		}		
	}

	@Override
	public String printJavaString(String prefixBefore) {
		return prefixBefore + "if(" + condition.toJavaString() + ") {" + "\r\n" + 
					thenFirstStep.printJavaString(prefixBefore + "\t") + "\r\n" +
			   prefixBefore + "}" + "\r\n" +
				(maybeElseFirstStep.isPresent() ? 
			   prefixBefore + "else {" + "\r\n" + 
			   		maybeElseFirstStep.get().printJavaString(prefixBefore + "\t") + "\r\n" + 
			   prefixBefore + "}" : "") +
			   (maybeNextStep_Print.isPresent() ? "\r\n" + maybeNextStep_Print.get().printJavaString(prefixBefore) : "");
	}

	@Override
	public String printBasic4JString(String allDesc, String codeDesc) throws NoSuchMethodException, SecurityException {
		return allDesc + nr() + "\t" + codeDesc + "if(" + condition.toString() + ") {" + "\r\n" + 
					thenFirstStep.printBasic4JString(allDesc, codeDesc + "\t") + "\r\n" +
					allDesc + "\t" + codeDesc + "}" + "\r\n" +
				(maybeElseFirstStep.isPresent() ? 
						allDesc + "\t" + codeDesc + "else {" + "\r\n" + 
			   		maybeElseFirstStep.get().printBasic4JString(allDesc, codeDesc + "\t") + "\r\n" + 
			   		allDesc + "\t" + codeDesc + "}" : "") +
			   (maybeNextStep_Print.isPresent() ? "\r\n" + maybeNextStep_Print.get().printBasic4JString(allDesc, codeDesc) : "");
	}
	
	@Override
	public String toBasic4JLine(String allDesc, String codeDesc) throws NoSuchMethodException, SecurityException {
		return allDesc + nr() + "\t" + codeDesc + "if(" + condition.toString() + ") {" + "\r\n" + 
					thenFirstStep.printBasic4JString(allDesc, codeDesc + "\t") + "\r\n" +
					allDesc + "\t" + codeDesc + "}" + //"\r\n" +
				(maybeElseFirstStep.isPresent() ? 
						allDesc + "\t" + codeDesc + "else {" + "\r\n" + 
			   		maybeElseFirstStep.get().printBasic4JString(allDesc, codeDesc + "\t") + "\r\n" + 
			   		allDesc + "\t" + codeDesc + "}" : "");
	}
	
	@Override
	public Component newView(Map<String, Type> declaredVarNames) throws NoSuchMethodException, SecurityException {
		return new IfView("", declaredVarNames, this);
	}
	
	@Override
	public void addStepViews(String prefix, Map<String, Type> declaredVarNames, Container into) throws NoSuchMethodException, SecurityException {
		into.add(this.newView(declaredVarNames));
		if (maybeNextStep_Print.isPresent()) {
			maybeNextStep_Print.get().addStepViews(prefix, declaredVarNames, into);
		}
	}
}
