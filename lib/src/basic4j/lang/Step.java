package basic4j.lang;

import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import basic4j.ValueCalc;

abstract public class Step {
	public final Step parent;
	private final int nr;
	public final Set<String> usedVars;
	private final Set<String> usedOuterVars;
	private final Set<String> assigningOuterVars;
	protected Step nextStep;
	protected Optional<Step> maybeNextStep_Print;
	
	protected String nr() {
		if (parent != null) {
			return parent.nr() + "." + nr;
		}
		else {
			return Integer.toString(nr);
		}
	}
	
	public Set<String> usedOuterVars() {
		HashSet<String> result = new HashSet<>(this.usedOuterVars);
		if (this.maybeNextStep_Print.isPresent()) {
			result.addAll(this.maybeNextStep_Print.get().usedOuterVars());
		}
		
		return result;
	}
	
	public Set<String> assigningOuterVars() {
		HashSet<String> result = new HashSet<>(this.assigningOuterVars);
		if (this.maybeNextStep_Print.isPresent()) {
			result.addAll(this.maybeNextStep_Print.get().assigningOuterVars());
		}
		
		return result;
	}
	
	abstract public String toBasic4JLine(String allDesc, String codeDesc) throws NoSuchMethodException, SecurityException;
	abstract public Component newView(Map<String, Type> declaredVarNames) throws NoSuchMethodException, SecurityException;
	abstract public void addStepViews(String prefix, Map<String, Type> declaredVarNames, Container into) throws NoSuchMethodException, SecurityException;
	
	public Step(Step parent, int nr, Set<String> usedVars, Set<String> usedOuterVars, Step next) {
		this(parent, nr, usedVars, usedOuterVars, new HashSet<>() /* no assigningOuterVars */, next);
	}
	
	public Step(Step parent, int nr, Set<String> usedVars, Set<String> usedOuterVars, Set<String> assigningOuterVars, Step next) {
		this.parent = parent;
		this.nr = nr;
		this.usedVars = usedVars;
		this.usedOuterVars = usedOuterVars;
		this.assigningOuterVars = assigningOuterVars;
		this.nextStep = next;
		this.maybeNextStep_Print = Optional.ofNullable(next);
	}
	
	public void setNext(Step next) {
		setNext(next, next);
	}
	
	public void setNext(Step nextRun, Step nextPrint) {
		this.nextStep = nextRun;
		this.maybeNextStep_Print = Optional.ofNullable(nextPrint);
	}
	
	abstract public ValueCalc execute(int t, java.util.Map<String, ValueCalc> vars, java.util.Map<String, ValueCalc> outerVarsAssignedInside) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException;
	abstract public String printJavaString(String prefix);
	abstract public String printBasic4JString(String allDesc, String codeDesc) throws NoSuchMethodException, SecurityException;
	
	@Override
	public String toString() {
		try {
			return this.printBasic4JString("", "");
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			return e.toString();
		}
	}

	public static Map<String, ValueCalc> retainAll(Map<String, ValueCalc> varVals, Set<String> usedVars) {
		HashMap<String, ValueCalc> result = new HashMap<>(varVals);
		result.keySet().retainAll(usedVars);
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + nr;
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Step other = (Step) obj;
		if (nr != other.nr)
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		return true;
	}
	
	//public String toJavaString(String prefix, Step until) {
		
	//}
}
