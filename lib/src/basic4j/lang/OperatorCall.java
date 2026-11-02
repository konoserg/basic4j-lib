package basic4j.lang;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;

import basic4j.Basic4JOperator;
import basic4j.ValueSource;

public class OperatorCall extends ValueSource {
	public final ValueSource on;
	public final Method method;
	public final ValueSource argCalc;
	
	public OperatorCall(ValueSource on, Method method, ValueSource argCalc) {
		super(addAll(on.usedVars, allUsedVars(argCalc)), addAll(on.usedOuterVars, allUsedOuterVars(argCalc)));
		
		this.on = on;
		this.method = method;
		this.argCalc = argCalc;
	}

	@Override
	public Object execute(Map vars) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException {		
		Object argVal = argCalc.execute(vars);
		return method.invoke(on.execute(vars), new Object[] {argVal});
	}

	@Override
	public String toJavaString() {
		return on.toJavaString() + "." + method.getName() + "(" + argCalc.toJavaString() + ")";
	}

	@Override
	public String toString(){
		String methodName = method.getName();
		if (method.isAnnotationPresent(Basic4JOperator.class)) {
			methodName = method.getAnnotation(Basic4JOperator.class).name();
		}
		
		return on.toString() + " " + methodName + " " + argCalc.toString();
	}
}
