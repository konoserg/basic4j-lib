package basic4j.lang;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import basic4j.Basic4JOperator;
import basic4j.ValueSource;

public class MethodCall extends ValueSource {
	public final ValueSource on;
	public final Method method;
	public final ValueSource[] paramCalcs;
	
	public MethodCall(ValueSource on, Method method, ValueSource[] paramCalcs) {
		super(addAll(on.usedVars, allUsedVars(paramCalcs)), addAll(on.usedOuterVars, allUsedOuterVars(paramCalcs)));
		
		this.on = on;
		this.method = method;
		this.paramCalcs = paramCalcs;
	}

	@Override
	public Object execute(Map vars) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException {		
		Object[] paramVals = new Object[paramCalcs.length];
		for (int i = 0; i < paramCalcs.length; i ++) {
			paramVals[i] = paramCalcs[i].execute(vars);
		}
		
		return method.invoke(on.execute(vars), paramVals);
	}

	@Override
	public String toJavaString() {
		String paramsList = "";
		for (int i = 0; i < paramCalcs.length; i ++) {
			if (!paramsList.isEmpty()) {
				paramsList += ", ";
			}
			
			paramsList += paramCalcs[i].toJavaString();
		}
		
		return on.toJavaString() + "." + method.getName() + "(" + paramsList + ")";
	}

	@Override
	public String toString()  {
		String paramsList = "";
		for (int i = 0; i < paramCalcs.length; i ++) {
			if (!paramsList.isEmpty()) {
				paramsList += ", ";
			}
			
			paramsList += paramCalcs[i].toString();
		}
		
		String methodName = method.getName();
		if (method.isAnnotationPresent(Basic4JOperator.class)) {
			methodName = method.getAnnotation(Basic4JOperator.class).name();
		}
		
		return on.toString() + "." + methodName
				+ (paramCalcs.length == 0 ? "" : "(" + paramsList + ")");
	}
}
