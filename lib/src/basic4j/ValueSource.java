package basic4j;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

abstract public class ValueSource {
	public final Set<String> usedVars;
	public final Set<String> usedOuterVars;
	
	public ValueSource(Set<String> usedVars, Set<String> usedOuterVars) {
		this.usedVars = usedVars;
		this.usedOuterVars = usedOuterVars;
	}
	
	public static Set<String> allUsedVars(ValueSource... of) {
		HashSet<String> result = new HashSet<>();
		for (ValueSource each: of) {
			result.addAll(each.usedVars);
		}
		
		return result;
	}
	
	public static Set<String> allUsedOuterVars(ValueSource... of) {
		HashSet<String> result = new HashSet<>();
		for (ValueSource each: of) {
			result.addAll(each.usedOuterVars);
		}
		
		return result;
	}
	
	public static Set<String> addAll(Set<String> to, Set<String> from) {
		HashSet<String> result = new HashSet<>(to);
		result.addAll(from);
		return result;
	}
	
	abstract public Object execute(java.util.Map<String, Object> varVals) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException;
	public String toJavaString() { return this.toString(); };
	//public String toString() { return this.toString();};
	
//	@Override
//	public String toString() {
//		try {
//			return this.toBasic4JString();
//		} catch (NoSuchMethodException | SecurityException e) {
//			return e.toString();
//		}
//	}

	public static class Const extends ValueSource {
		public final Object val;
		
		public Const(Object val) {
			super(Collections.EMPTY_SET, Collections.EMPTY_SET);
			this.val = val;
		}

		@Override
		public Object execute(Map<String, Object> varVals)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
				NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
			return val;
		}

		@Override
		public String toJavaString() {
			return val.toString();
		}

		@Override
		public String toString() {
			return val.toString();
		}
		
	}
}
