package basic4j;



import java.util.Arrays;
import java.util.Iterator;

@Collection
@Basic4JType(name="List")
public class B4JList<T> {
	private final java.util.List<T> val;
	
	public B4JList(java.util.List<T> val) {
		this.val = val;
	}
	
	public B4JList(T... elems) {
		this(Arrays.asList(elems));
	}
	
	public B4JList<T> add(T elem) {
		java.util.ArrayList<T> newVal = new java.util.ArrayList<>(this.val);
		newVal.add(elem);
		return new B4JList<>(newVal);
	}

	public B4JIterator<T> iterator() {
		return new B4JIterator<>(this.val.iterator());
	}

	@Override
	public java.lang.String toString() {
		return val.toString();
	}
}
