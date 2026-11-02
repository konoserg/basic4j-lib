package basic4j.lang;

import java.util.Arrays;
import java.util.HashSet;

import basic4j.ValueSource;

/**
 * Assignment inside of a block on an outer to this block variable.
 */
public class OuterAssignment extends Assignment {

	public OuterAssignment(Step parent, int nr, Step next, String varName, ValueSource to) {
		super(parent, nr, new HashSet<>(Arrays.asList(varName)) /* assigningOuterVars */, next, varName, to);
	}
}
