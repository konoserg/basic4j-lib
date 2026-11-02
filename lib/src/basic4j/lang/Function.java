package basic4j.lang;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import basic4j.Basic4JType;
import basic4j.ValueCalc;
import basic4j.ValueSource;
import basic4j.lang.view.FunctionView;

abstract public class Function {
	public final Type retType;
	public final String name;
	public final Type[] paramTypes;
	public final Optional<Class<?>[]>[] maybeTemplateTypes;
	public final String[] paramNames;
	public final Step firstStep;
	
	public Function(Type retType, String name, Type[] paramTypes, Optional<Class<?>[]>[] maybeTemplateTypes, String[] paramNames, Step first) {
		this.retType = retType;
		this.name = name;
		this.paramTypes = paramTypes;
		this.maybeTemplateTypes = maybeTemplateTypes;
		this.paramNames = paramNames;
		this.firstStep = first;
	}
	
	public ValueCalc execute(ValueSource[] params) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
		HashMap<String, ValueCalc> vars = new HashMap<>();
		for (int i = 0; i < paramNames.length; i ++) {
			vars.put(paramNames[i], new ValueCalc(null, -1, params[i], Collections.EMPTY_MAP));
		}
		
		return firstStep.execute(0, vars, new HashMap<>());
	}
	
	public String toJavaString() {
		String paramTypesList = "";
		for (int i = 0; i < paramTypes.length; i ++) {
			if (!paramTypesList.isEmpty()) {
				paramTypesList += ", ";
			}
			
			String template = "";
			if (paramTypes[i].maybeTemplateTypes.isPresent()) {
				for (Class each: paramTypes[i].maybeTemplateTypes.get()) {
					if (!template.isEmpty()) {
						template += ", ";
					}
					
					template += each.getSimpleName();
				}
				
				template = "<" + template + ">";
			}
			
			paramTypesList += paramTypes[i].javaClass.getSimpleName() + template + " " + paramNames[i];
		}
		
		return "public static " + retType.javaClass.getSimpleName() + " " + name + "(" + paramTypesList + ") {" + "\r\n" + 
				firstStep.printJavaString("\t") + "\r\n" +
				"}";
	}
	
	public String toBasic4JString(String desc) throws NoSuchMethodException, SecurityException {
		String paramTypesList = "";
		for (int i = 0; i < paramTypes.length; i ++) {
			if (!paramTypesList.isEmpty()) {
				paramTypesList += ", ";
			}
			
			String template = "";
			if (paramTypes[i].maybeTemplateTypes.isPresent()) {
				for (Class<?> each: paramTypes[i].maybeTemplateTypes.get()) {
					if (!template.isEmpty()) {
						template += ", ";
					}
					
					template += each.getAnnotation(Basic4JType.class).name();
				}
				
				template = "<" + template + ">";
			}
			
			paramTypesList += paramTypes[i].javaClass.getAnnotation(Basic4JType.class).name() + template + " " + paramNames[i];
		}
		
		return retType.javaClass.getAnnotation(Basic4JType.class).name() + " " + name + "(" + paramTypesList + ") {" + "\r\n" + 
				firstStep.printBasic4JString(desc, "\t") + "\r\n" +
				"}";
	}
	
	public String title() {
		String paramTypesList = "";
		for (int i = 0; i < paramTypes.length; i ++) {
			if (!paramTypesList.isEmpty()) {
				paramTypesList += ", ";
			}
			
			String template = "";
			if (paramTypes[i].maybeTemplateTypes.isPresent()) {
				for (Class<?> each: paramTypes[i].maybeTemplateTypes.get()) {
					if (!template.isEmpty()) {
						template += ", ";
					}
					
					template += each.getAnnotation(Basic4JType.class).name();
				}
				
				template = "<" + template + ">";
			}
			
			paramTypesList += paramTypes[i].javaClass.getAnnotation(Basic4JType.class).name() + template + " " + paramNames[i];
		}
		
		return retType.javaClass.getAnnotation(Basic4JType.class).name() + " " + name + "(" + paramTypesList + ")";
	}
	
	public FunctionView newView() throws NoSuchMethodException, SecurityException {
		FunctionView result = new FunctionView();
		result.setStaticMethod(this);
		return result;
	}
}
