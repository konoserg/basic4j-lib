package basic4j.lang;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;

import basic4j.B4JInteger;
import basic4j.ValueSource;

public class IntLiteralConstructorCall extends ValueSource {
	public final int literalVal;
	
	public IntLiteralConstructorCall(int literalVal) {
		super(new HashSet<>() /* no usedVars */, new HashSet<>() /* no usedOuterVars */);
		this.literalVal = literalVal;
	}
	
	@Override
	public Object execute(Map<String, Object> varVals) throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
		return new B4JInteger(literalVal);
	}

	@Override
	public String toJavaString() {
		return "" + literalVal;
	}

	@Override
	public String toString() {
		return "" + literalVal;
	}
}
