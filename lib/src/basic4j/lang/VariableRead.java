package basic4j.lang;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import basic4j.ValueSource;

public class VariableRead extends ValueSource {
	public final String varName;
	
	public VariableRead(String varName) {
		this(varName, null);
	}
	
	public VariableRead(String varName, String maybeOuterVarName) {
		super(new HashSet<>(Arrays.asList(varName)), maybeOuterVarName != null ? new HashSet<>(Arrays.asList(maybeOuterVarName)) : new HashSet<>());
		this.varName = varName;
	}
	
	@Override
	public Object execute(Map<String, Object> vars) {
		return vars.get(varName);
	}

	@Override
	public String toJavaString() {
		return varName;
	}
	
	@Override
	public String toString() {
		return varName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((varName == null) ? 0 : varName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VariableRead other = (VariableRead) obj;
		if (varName == null) {
			if (other.varName != null)
				return false;
		} else if (!varName.equals(other.varName))
			return false;
		return true;
	}
}