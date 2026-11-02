package basic4j.lang;

import java.lang.reflect.InvocationTargetException;
import java.awt.Component;
import java.lang.String;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import basic4j.ValueSource;

public class FunctionCall extends ValueSource {
	
	public FunctionCall(ValueSource[] paramCalcs) {
		super(allUsedVars(paramCalcs), allUsedOuterVars(paramCalcs));
	}

	@Override
	public Object execute(Map vars) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toJavaString() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
}
