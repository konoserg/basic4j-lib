package basic4j.lang;

import basic4j.ValueCalc;

public class CalcError extends Exception {
	public final ValueCalc source;
	
	public CalcError(ValueCalc source) {
		this.source = source;
	}
}
