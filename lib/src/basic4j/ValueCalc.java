package basic4j;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import basic4j.lang.Step;

public class ValueCalc {
	
	// trace informations
	public final Step line;
	public final int t;
	
	//public final Object result;
	public final ValueSource formula;
	public final Map<String, ValueCalc> context;
	
	public Object perform() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		return formula.execute(performAll(context));
	}
	
	public static Map<String, Object> performAll(Map<String, ValueCalc> vars) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		HashMap<String, Object> result = new HashMap<>();
		for (Map.Entry<String, ValueCalc> each: vars.entrySet()) {
			result.put(each.getKey(), each.getValue().perform());
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		return "ValueCalc[formula=" + formula + ", context=" + context.keySet() + "]";
	}
	
//	public Map<String, ValueCalc> input(Line line) {
//		if (line == this.line) {
//			return input;
//		}
//		else {
//			return condition.input(line);
//		}
//	}
//	
//	public Map<String, ValueCalc> inputInclParents() {
//		HashMap<String, ValueCalc> result = new HashMap<>();
//		result.putAll(this.input());
//		
//		if (this.line.parent != null) {
//			result.putAll(this.input(this.line.parent));
//		}
//		
//		return result;
//	}
	
	public ValueCalc(Step line, int t, ValueSource formula, Map<String, ValueCalc> input) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		this.line = line;
		this.t = t;
		
		//this.result = formula.execute(Line.valsOnly(input));
		this.formula = formula;
		this.context = input;
	}
	
	public ValueCalc(Step line, int t, ValueSource formula, String inputKey, ValueCalc inputVal) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		this(line, t, formula, newMap(inputKey, inputVal));
	}
	
	public static Map<String, ValueCalc> newMap(String key, ValueCalc val) {
		HashMap<String, ValueCalc> result = new HashMap<>();
		result.put(key, val);
		return result;
	}

//	@Override
//	public String toString() {
//		return result + "";
//	}
}
