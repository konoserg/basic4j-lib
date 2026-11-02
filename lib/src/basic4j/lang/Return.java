package basic4j.lang;

import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import basic4j.ValueSource;
import basic4j.ValueCalc;
import basic4j.lang.view.ReturnView;

public class Return extends Step {
	public final ValueSource val; // always there
	
	public Return(Step parent, int nr, ValueSource val) {
		super(parent, nr, val.usedVars, val.usedOuterVars, null);
		this.val = val;
	}

	@Override
	public ValueCalc execute(int t, Map<String, ValueCalc> vars, Map<String, ValueCalc> _) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
		System.out.print(t + this.toBasic4JLine("\t", ""));
		
		//Object resultVal = val.execute(valsOnly(retainAll(vars, val.usedVars)));
		ValueCalc valCalc = new ValueCalc(this, t, this.val, retainAll(vars, this.val.usedVars));
		
		System.out.println(" " + valCalc.perform());
		
		return valCalc;
	}

	@Override
	public String printJavaString(String prefix) {
		return prefix + "return" + " " + val.toJavaString() + ";";
	}

	@Override
	public String printBasic4JString(String allDesc, String codeDesc) throws NoSuchMethodException, SecurityException {
		return allDesc + nr() + "\t" + codeDesc + "return" + " " + val.toString() + ";";
	}
	
	@Override
	public String toBasic4JLine(String allDesc, String codeDesc) throws NoSuchMethodException, SecurityException {
		return allDesc + nr() + "\t" + codeDesc + "return" + " " + val.toString() + ";";
	}
	
	@Override
	public Component newView(Map<String, Type> declaredVars) {
		return new ReturnView(declaredVars, this);
	}
	
	@Override
	public void addStepViews(String prefix, Map<String, Type> declaredVars, Container into) {
		into.add(this.newView(declaredVars));
	}
}
