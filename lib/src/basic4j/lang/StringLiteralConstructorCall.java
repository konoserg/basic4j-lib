package basic4j.lang;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;

import basic4j.B4JString;
import basic4j.ValueSource;

public class StringLiteralConstructorCall extends ValueSource {
	public final String literalVal;
	
	public StringLiteralConstructorCall(String literalVal) {
		super(new HashSet<>() /* no usedVars */, new HashSet<>() /* no usedOuterVars */);
		this.literalVal = literalVal;
	}
	
	@Override
	public Object execute(Map<String, Object> varVals) throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
		return new B4JString(literalVal);
	}

	@Override
	public String toJavaString() {
		return "\"" + literalVal + "\"";
	}

	@Override
	public String toString() {
		return "'"+ literalVal + "'";
	}
}
