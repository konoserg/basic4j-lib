package basic4j;

@Basic4JType(name="Integer")
@Literalable
public class B4JInteger extends B4JFloat {
	//@Basic4JField(name="null")
	//public final B4JInteger NULL = null;
	
	private final int val;
	
	public B4JInteger(int val) {
		super(val);
		this.val = val;
	}
	
	@Override
	public B4JFloat plus(B4JFloat incr) {
		if (incr instanceof B4JInteger) {
			return new B4JInteger(this.val + ((B4JInteger)incr).val);
		}
		else {
			return super.plus(incr);
		}
	}
	
	@Override
	public B4JFloat minus(B4JFloat decr) {
		if (decr instanceof B4JInteger) {
			return new B4JInteger(this.val - ((B4JInteger)decr).val);
		}
		else {
			return super.minus(decr);
		}
	}
	
	@Override
	public B4JFloat mult(B4JFloat by) {
		if (by instanceof B4JInteger) {
			return new B4JInteger(this.val * ((B4JInteger)by).val);
		}
		else {
			return super.mult(by);
		}
	}

	@Override
	public String toString() {
		return java.lang.Integer.toString(val);
	}
}
