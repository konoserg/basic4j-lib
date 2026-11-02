package basic4j.lang;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import basic4j.Basic4JType;
import basic4j.ValueSource;

public class ConstructorCall extends ValueSource {
	public final java.lang.Class<?> type;
	public final Optional<java.lang.Class<?>[]> maybeTemplateTypes;
	public final java.lang.Class<?>[] constrParams;
	public final ValueSource[] paramCalcs;
	
	public ConstructorCall(java.lang.Class<?> type, java.lang.Class<?>[] constrParams, ValueSource[] paramCalcs) {
		this(type, Optional.empty(), constrParams, paramCalcs);
	}
	
	public ConstructorCall(java.lang.Class<?> type, Optional<java.lang.Class<?>[]> maybeTemplateTypes, java.lang.Class<?>[] constrParams, ValueSource[] paramCalcs) {
		super(allUsedVars(paramCalcs), allUsedOuterVars(paramCalcs));
		
		this.type = type;
		this.maybeTemplateTypes = maybeTemplateTypes;
		this.constrParams = constrParams;
		this.paramCalcs = paramCalcs;
	}

	@Override
	public Object execute(Map vars) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		Object[] paramVals = new Object[paramCalcs.length];
		for (int i = 0; i < paramCalcs.length; i ++) {
			paramVals[i] = paramCalcs[i].execute(vars);
		}
		
		return type.getDeclaredConstructor(constrParams).newInstance(paramVals);
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
		
		return "new " + type.getSimpleName() + (maybeTemplateTypes.isPresent() ? "<>" : "") + "(" + paramsList + ")";
	}
	
	@Override
	public String toString() {
		if (paramCalcs.length == 1) {
			return paramCalcs[0].toString();
		}
		else {
			String paramsList = "";
			for (int i = 0; i < paramCalcs.length; i ++) {
				if (!paramsList.isEmpty()) {
					paramsList += ", ";
				}
				
				paramsList += paramCalcs[i].toString();
			}
			
			String template = "";
			if (maybeTemplateTypes.isPresent()) {
				for (java.lang.Class<?> each: maybeTemplateTypes.get()) {
					if (!template.isEmpty()) {
						template += ", ";
					}
					
					template += each.getAnnotation(Basic4JType.class).name();
				}
				
				template = "<" + template + ">";
			}
			
			return type.getAnnotation(Basic4JType.class).name() + template + "(" + paramsList + ")";
		}
	}
}
