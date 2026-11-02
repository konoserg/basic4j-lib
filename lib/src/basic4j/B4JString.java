package basic4j;

@Basic4JType(name="Text")
@Literalable
public class B4JString implements Comparable<B4JString> {
	private final java.lang.String val;
	
	public B4JString(java.lang.String val) {
		this.val = val;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof B4JString)) {
			return false;
		}
		
		return val.equals(((B4JString)obj).val);
	}

	@Override
	public int hashCode() {
		return val.hashCode();
	}

	@Override
	public java.lang.String toString() {
		return "'" + val.toString() + "'";
	}

	@Override
	public int compareTo(B4JString other) {
		return this.val.compareTo(other.val);
	}
}
