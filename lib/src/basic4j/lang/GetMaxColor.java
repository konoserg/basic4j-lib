package basic4j.lang;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;

import basic4j.B4JEntry;
import basic4j.B4JFloat;
import basic4j.B4JList;
import basic4j.B4JMap;
import basic4j.B4JString;
import basic4j.Basic4JType;
import basic4j.ValueCalc;
import basic4j.ValueSource;

public class GetMaxColor extends Function {

	public GetMaxColor() throws NoSuchMethodException, SecurityException, NoSuchFieldException {
		super(new Type(B4JString.class), "getMaxColor", 
				new Type[] {new Type(B4JList.class, new Class[] {Ball.class})}, 
				new Optional[0],// {Optional.of(new Class[] {Ball.class})}, 
				new java.lang.String[] {"of"}, 
				create(null));
	}
	
	public static Step create(Step parent) throws NoSuchMethodException, SecurityException, NoSuchFieldException {
		Declaration declareColorCountsHashMap = new Declaration(parent, 0 /* nr */, null/*:next*/, B4JMap.class/*:type*/, Optional.of(new Class<?>[] {B4JString.class, B4JFloat.class}), "colorCounts" /*:varName*/, 
				/*init:*/newColorCountPut(
				newColorCountPut(
					newColorCountPut(
						new ConstructorCall(B4JMap.class /*:type*/, Optional.of(new Class<?>[] {B4JString.class, B4JFloat.class}) /* :template */, new Class[0] /*:constrParams*/, new ValueSource[0] /*:paramCalcs*/),
						"Red"),
							"Green"),
								"Blue")
		);
		
//		If ifEmpty = new If(null/*:next*/, 
//				/*condition:*/new MethodCall("colorCounts", "isEmpty", new Class[0], new Value[0]), null/*:then*/);
		
		For forEachInOf = new For(parent, 1 /* nr */, null /* :next */, new VariableRead("of") /* on */, new Type(Ball.class), "each",
				null /* body */);
		
		Declaration declarePrevEachCountsGet = new Declaration(forEachInOf /* parent */, 0 /* nr */, null /* next */, B4JFloat.class /* type */, Optional.empty() /* maybeTemplateTypes */, "prev" /* varName */, 
				/*init:*/new MethodCall(new OuterVariableRead("colorCounts")/*:on*/, B4JMap.class.getMethod("get"/*:methodName*/, new Class[] {Object.class}/*:methodParams*/), 
						/*paramCalcs:*/new ValueSource[] {
								/* [0]: */new FieldRead(new VariableRead("each") /* on */, Ball.class, "color", new Type(B4JString.class) /* field */)
						}
				)
		);
		
		OuterAssignment assignEachCountsPut = new OuterAssignment(forEachInOf /* parent */, 1 /* nr */, null/*:next*/, "colorCounts"/*:varName*/, 
				/*to:*/new MethodCall(new OuterVariableRead("colorCounts")/*:on*/, B4JMap.class.getMethod("set"/*:methodName*/, new Class[] {Object.class, Object.class}/*:methodParams*/), 
						/*paramCalcs:*/new ValueSource[] {
								/*[0]:*/new FieldRead(new VariableRead("each") /* on */, Ball.class, "color", new Type(B4JString.class) /* field */),
								/*[1]:*/new OperatorCall(new VariableRead("prev") /*on*/, B4JFloat.class.getMethod("plus" /*methodName*/, new Class<?>[] {B4JFloat.class} /*methodParams*/), new IntLiteralConstructorCall(1) /*argCalc*/)
						}
				)
		);
		
		Declaration maxCountVal = new Declaration(parent, 3 /* nr */, null /* next */, B4JFloat.class /* type */,
				Optional.empty() /* maybeTemplateTypes */, "maxCountVal" /* varName */, 
				/* init: */new IntLiteralConstructorCall(-1));
		
		Declaration maxCountColor = new Declaration(parent, 4 /* nr */, null /* next */, B4JString.class /* type */,
				Optional.empty() /* maybeTemplateTypes */, "maxCountColor" /* varName */, 
				/* init: */new StringLiteralConstructorCall(""));
		
		For forEachInColorCountsEntries = new For(parent, 5 /* nr */, null /* :next */, 
				/* on: */new FieldRead(new VariableRead("colorCounts") /* on */, B4JMap.class, "entries", new Type(B4JEntry.class, new Class[] {B4JString.class, B4JFloat.class}) /* method() */), 
				new Type(B4JEntry.class, Optional.of(new Class[] {B4JString.class, B4JFloat.class})), "each", null /* body */);
		
		If ifValHigherMaxCountVal = new If(forEachInColorCountsEntries /* parent */, 0 /* nr */,
				null /* next */, 
				/* condition */new OperatorCall(
						/* on */new FieldRead(new VariableRead("each") /* on */, B4JEntry.class, "val", new Type(B4JFloat.class) /* field */), 
						B4JFloat.class.getMethod("higher", new Class[] {B4JFloat.class}) /*method*/, new OuterVariableRead("maxCountVal") /*argCalc*/), 
				null /*then*/);
		
		OuterAssignment setMaxCountVal = new OuterAssignment(ifValHigherMaxCountVal /* parent */, 0 /* nr */, null /* next */, "maxCountVal" /* varName */, new FieldRead(new OuterVariableRead("each"), B4JEntry.class, "val", new Type(B4JFloat.class) /* to */));
		OuterAssignment setMaxCountColor = new OuterAssignment(ifValHigherMaxCountVal /* parent */, 1 /* nr */, null /* next */, "maxCountColor" /* varName */, new FieldRead(new OuterVariableRead("each"), B4JEntry.class, "key", new Type(B4JString.class) /* to */));
		
		Return returnColorCounts = new Return(parent, 7, new VariableRead("maxCountColor"));
		
		Step first = declareColorCountsHashMap;
		declareColorCountsHashMap.setNext(forEachInOf);
		forEachInOf.bodyFirstStep = declarePrevEachCountsGet;
		declarePrevEachCountsGet.setNext(assignEachCountsPut);
		assignEachCountsPut.setNext(forEachInOf /* :run */, null /* :print */);
		forEachInOf.setNext(new EmptyLine(parent, 2 /* nr */, maxCountVal));
		maxCountVal.setNext(maxCountColor);
		maxCountColor.setNext(forEachInColorCountsEntries);
		forEachInColorCountsEntries.bodyFirstStep = ifValHigherMaxCountVal;
		ifValHigherMaxCountVal.thenFirstStep = setMaxCountVal;
		setMaxCountVal.setNext(setMaxCountColor);
		setMaxCountColor.setNext(ifValHigherMaxCountVal /* :run */, null /* :print */);
		ifValHigherMaxCountVal.setNext(forEachInColorCountsEntries /* :run */, null /* :print */);
		forEachInColorCountsEntries.setNext(new EmptyLine(parent, 6 /* nr */, returnColorCounts));
		
		return first;
	}
	
	public static MethodCall newColorCountPut(ValueSource on, java.lang.String color) throws NoSuchMethodException, SecurityException {
		return new MethodCall(on, B4JMap.class.getMethod("set"/*:methodName*/, new Class[] {Object.class, Object.class}/*:methodParams*/), 
				/*paramCalcs:*/new ValueSource[] {
						/*[0]:*/new StringLiteralConstructorCall(color),
						/*[1]:*/new IntLiteralConstructorCall(0)
				}
		);
	}
	
	public static void main(java.lang.String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		GetMaxColor inst = new GetMaxColor();
		System.out.println(inst.toJavaString());
		System.out.println("---------------------");
		System.out.println(inst.toBasic4JString(""));
		System.out.println("---------------------");
		Object paramVal = new B4JList(
				new Ball(new B4JString("Red")), 
				new Ball(new B4JString("Green")), 
				new Ball(new B4JString("Green")), 
				new Ball(new B4JString("Blue"))
		);
		ValueSource[] paramVals = new ValueSource[] {
				new ValueSource.Const(
						paramVal
				)
		};
		ValueCalc calc = inst.execute(paramVals);
		System.out.println("inst.execute(" + paramVal + ") = " + calc.perform());
		
		System.out.println("Backtracing:");
		printBackVar("", /* inst, */ calc);
	}
	
	private static void printBackVar(String prefix, ValueCalc it) throws NoSuchMethodException, SecurityException, NoSuchFieldException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {		
		//System.out.print(it.t + it.line.toBasic4JLine(prefix + "\t", "") + " ");
		System.out.print(it.t + prefix + it.formula.toString() + " ");
		Map<String, ValueCalc> used = it.context;// f.varVals(paramVals, varVal.line.usedVars, varVal.t, varVal.line);
		System.out.println("context=" + used);
		
		for (Map.Entry<String, ValueCalc> each: used.entrySet()) {
			if (each.getValue().t != -1) {
				System.out.println(prefix + each.getKey() + ":");
				printBackVar(prefix + "\t", /* inst, */ each.getValue());
			}
		}
		
//		if (varVal.line.parent != null && false) {
//			printBackVar(prefix, /* inst, */ varVal.parent);
//		}
	}
	
	@Basic4JType(name="Ball")
	public static class Ball {
		public final B4JString color;
		
		public Ball(B4JString color) {
			this.color = color;
		}

		@Override
		public java.lang.String toString() {
			return "Ball[color=" + color + "]";
		}
	}
}
