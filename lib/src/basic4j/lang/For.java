package basic4j.lang;

import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import basic4j.B4JIterator;
import basic4j.B4JList;
import basic4j.Basic4JType;
import basic4j.ValueCalc;
import basic4j.ValueSource;
import basic4j.lang.view.ForView;

public class For extends Step {
	public final ValueSource elemsSrc;
	public final Type elemType;
	public final String elemName;
	public Step bodyFirstStep;
	
	public For(Step parent, int nr, Step next, ValueSource elemsSrc, Type elementType, String elementName, Step bodyFirst) {
		super(parent, nr, elemsSrc.usedVars, elemsSrc.usedOuterVars, next);
		this.elemsSrc = elemsSrc;
		this.elemType = elementType;
		this.elemName = elementName;
		this.bodyFirstStep = bodyFirst;
	}
	
	//private Iterator<? extends Object> iterator = null;
	private HashMap<String, ValueCalc> outerVars;
	private HashMap<String, ValueCalc> outerOuterVarsAssignedInside;
	private int t;
	
	@Override
	// this step is unter anderen the nextStep of the last step of the body, so it is executed every time
	// we achieved the end of the body
	public ValueCalc execute(int t, Map<String, ValueCalc> varsBefore, Map<String, ValueCalc> outerVarsAssignedInside) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
		System.out.println(t + this.toBasic4JLine("\t", ""));
		
		ValueCalc it = varsBefore.get("it");
		if (it == null) {
			ValueSource itFunc = new ValueSource(null, null) { @Override public Object execute(Map<String, Object> varVals) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
				B4JList<? extends Object> elems = (B4JList<? extends Object>)elemsSrc.execute(varVals);
				B4JIterator<? extends Object> it = elems.iterator();
				return it;
			}};
			
			it = new ValueCalc(this, t, itFunc, retainAll(varsBefore, elemsSrc.usedVars));
			
			this.outerVars = new HashMap<>(varsBefore);
			this.t = t;
			outerOuterVarsAssignedInside = new HashMap<>(outerVarsAssignedInside);
			outerVarsAssignedInside = new HashMap<>();
			//t = -1;
			
			//varsBefore = retainAll(varsBefore, ValueSource.addAll(elemsSrc.usedVars, bodyFirstStep.usedOuterVars()));
			varsBefore.put("it", it);
		}
		
		ValueSource hasNextFunc = new ValueSource(null, null) { @Override public Object execute(Map<String, Object> varVals) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
			return ((B4JIterator<? extends Object>)varVals.get("it")).hasNext;
		}};
		ValueCalc hasNext = new ValueCalc(this, t, hasNextFunc, "it", it);
		if((boolean) hasNext.perform()) {
			ValueSource nextFunc = new ValueSource(null, null) { @Override public Object execute(Map<String, Object> varVals) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
				return ((B4JIterator<? extends Object>)varVals.get("it")).next();
			}};
			
			it = new ValueCalc(this, t, nextFunc, "it", it);
			
			ValueSource elemFunc = new ValueSource(null, null) { @Override public Object execute(Map<String, Object> varVals) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
				return ((B4JIterator<? extends Object>)varVals.get("it")).elem;
			}};
			
			ValueCalc elem = new ValueCalc(this, t, elemFunc, "it", it);
			
			HashMap<String, ValueCalc> varsAfter = new HashMap<>(varsBefore);
			varsAfter.put("it", it);
			varsAfter.put(elemName, elem);
			return bodyFirstStep.execute(t + 1, varsAfter, outerVarsAssignedInside);
		}
		else {
			varsBefore.remove("it");
			HashMap<String, ValueCalc> varsNew = new HashMap<>(this.outerVars);
			for (Map.Entry<String, ValueCalc> each: outerVarsAssignedInside.entrySet()) {
				String varName = each.getKey();
				ValueCalc valInside = each.getValue();
				//varsNew.put(varName, new Value(this, this.t, valInside, valInside.val));
				varsNew.put(varName, valInside);
			}

			return nextStep.execute(t + 1, varsNew, this.outerOuterVarsAssignedInside);
		}
	}

	@Override
	public String printJavaString(String prefix) {
		String template = "";
		if (elemType.maybeTemplateTypes.isPresent()) {
			for (java.lang.Class<?> each: elemType.maybeTemplateTypes.get()) {
				if (!template.isEmpty()) {
					template += ", ";
				}
				
				template += each.getSimpleName();
			}
			
			template = "<" + template + ">";
		}
		
		return prefix + "for(" + elemType.javaClass.getSimpleName() + template + " each: " + elemsSrc.toJavaString() + ") {" + "\r\n" +
					bodyFirstStep.printJavaString(prefix + "\t") + "\r\n" + 
			   prefix + "}" +
			   (maybeNextStep_Print.isPresent() ? "\r\n" + maybeNextStep_Print.get().printJavaString(prefix) : "");
	}
	
	@Override
	public String printBasic4JString(String allDesc, String codeDesc) throws NoSuchMethodException, SecurityException {
		String template = "";
		if (elemType.maybeTemplateTypes.isPresent()) {
			for (java.lang.Class<?> each: elemType.maybeTemplateTypes.get()) {
				if (!template.isEmpty()) {
					template += ", ";
				}
				
				template += each.getAnnotation(Basic4JType.class).name();
			}
			
			template = "<" + template + ">";
		}
		
		return allDesc + nr() + "\t" + codeDesc + "for(" + elemType.javaClass.getAnnotation(Basic4JType.class).name() + template + " each: " + elemsSrc.toString() + ") {" + "\r\n" +
					bodyFirstStep.printBasic4JString(allDesc, codeDesc + "\t") + "\r\n" + 
					allDesc + "\t" + codeDesc + "}" +
			   (maybeNextStep_Print.isPresent() ? "\r\n" + maybeNextStep_Print.get().printBasic4JString(allDesc, codeDesc) : "");
	}
	
	@Override
	public String toBasic4JLine(String allDesc, String codeDesc) throws NoSuchMethodException, SecurityException {
		String template = "";
		if (elemType.maybeTemplateTypes.isPresent()) {
			for (java.lang.Class<?> each: elemType.maybeTemplateTypes.get()) {
				if (!template.isEmpty()) {
					template += ", ";
				}
				
				template += each.getAnnotation(Basic4JType.class).name();
			}
			
			template = "<" + template + ">";
		}
		
		return allDesc + nr() + "\t" + codeDesc + "for(" + elemType.javaClass.getAnnotation(Basic4JType.class).name() + template + " each: " + elemsSrc.toString() + ") {" + "\r\n" +
					bodyFirstStep.printBasic4JString(allDesc, codeDesc + "\t") + "\r\n" + 
					allDesc + "\t" + codeDesc + "}";
	}
	
	@Override
	public Component newView(Map<String, Type> declaredVarsBefore) throws NoSuchMethodException, SecurityException {
		HashMap<String, Type> declaredVarsInside = new HashMap<>(declaredVarsBefore);
		declaredVarsInside.put("each", elemType);
		
		return new ForView("", declaredVarsInside, this);
	}
	
	@Override
	public void addStepViews(String prefix, Map<String, Type> declaredVars, Container into) throws NoSuchMethodException, SecurityException {
		into.add(this.newView(declaredVars));
		if (maybeNextStep_Print.isPresent()) {

			maybeNextStep_Print.get().addStepViews(prefix, declaredVars, into);
		}
	}
}
