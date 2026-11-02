package basic4j.lang;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import basic4j.Basic4JField;
import basic4j.Basic4JOperator;
import basic4j.ValueSource;

public class FieldRead extends ValueSource {
	public final ValueSource on;
	public final java.lang.Class clazz;
	/**
	 * So that we can replace every time size() by size, entries() by entries und umgekehrt.
	 */
	public final String fieldOrMethodName;
	
	public final Type fieldOrMethodType;
	
	public FieldRead(ValueSource on, java.lang.Class clazz, String fieldOrMethodName, Type fieldOrMethodType) {
		super(on.usedVars, on.usedOuterVars);
		
		this.on = on;
		this.clazz = clazz;
		this.fieldOrMethodName = fieldOrMethodName;
		this.fieldOrMethodType = fieldOrMethodType;
	}

	@Override
	public Object execute(Map vars) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {		
		Field maybeField;
		try {
			maybeField = clazz.getDeclaredField(fieldOrMethodName);
		} catch (NoSuchFieldException | SecurityException e) {
			maybeField = null;
		}
		
		if (maybeField != null) {
			return maybeField.get(on.execute(vars));
		} else {
			Method method;
			try {
				method = clazz.getMethod(fieldOrMethodName, new Class[0]);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(fieldOrMethodName + " could not be found wheter as field or as method without arguments");
			}
			
			return method.invoke(on.execute(vars), new Object[0]);
		}
	}

	@Override
	public String toJavaString() {
		String call;
		Field maybeField;
		try {
			maybeField = clazz.getDeclaredField(fieldOrMethodName);
		} catch (NoSuchFieldException | SecurityException e) {
			maybeField = null;
		}
		
		if (maybeField != null) {
			call = fieldOrMethodName;
		} else {
			call = fieldOrMethodName + "()";
		}
		
		return on.toJavaString() + "." + call;
	}

	@Override
	public String toString() {
		Field maybeField;
		try {
			maybeField = clazz.getDeclaredField(fieldOrMethodName);
		} catch (NoSuchFieldException | SecurityException e) {
			maybeField = null;
		}
		
		String string;
		if (maybeField != null) {
			String fieldName = maybeField.getName();
			if (maybeField.isAnnotationPresent(Basic4JField.class)) {
				fieldName = maybeField.getAnnotation(Basic4JField.class).name();
			}
			
			string = fieldName;
		} else {
			try {
				Method method = clazz.getMethod(fieldOrMethodName, new Class[0]);
				String methodName = method.getName();
				if (method.isAnnotationPresent(Basic4JOperator.class)) {
					methodName = method.getAnnotation(Basic4JOperator.class).name();
				}
				
				string = methodName;
			} catch (NoSuchMethodException | SecurityException e) {
				string = "\"" + fieldOrMethodName + "\"";
			}
		}
		
		return on.toString() + "." + string;
	}
}
