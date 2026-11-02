package basic4j;

@Basic4JType(name="Number")
public class B4JFloat {
	private final double val;
	
	public B4JFloat(double val) {
		this.val = val;
	}
	
	@Basic4JOperator(name="+")
	public B4JFloat plus(B4JFloat incr) {
		return new B4JFloat(this.val + incr.val);
	}
	
	@Basic4JOperator(name="-")
	public B4JFloat minus(B4JFloat decr) {
		return new B4JFloat(this.val - decr.val);
	}
	
	@Basic4JOperator(name="*")
	public B4JFloat mult(B4JFloat by) {
		return new B4JFloat(this.val * by.val);
	}
	
	@Basic4JOperator(name="/")
	public B4JFloat devide(B4JFloat by) {
		return new B4JFloat((double)this.val / by.val);
	}
	
//	public B4JInteger min(B4JInteger other) {
//	return new B4JInteger(Math.min(this.val, other.val));
//}
//
//public B4JInteger max(B4JInteger other) {
//	return new B4JInteger(Math.max(this.val, other.val));
//}

@Basic4JOperator(name=">")
public Boolean higher(B4JFloat then) {
	return this.val > then.val;
}

//@Basic4JOperator(name="==")
//public Boolean equal(B4JInteger to) {
//	return this.val == to.val;
//}
	
	@Override
	public String toString() {
		return java.lang.Double.toString(val);
	}
}
