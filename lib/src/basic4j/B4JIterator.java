package basic4j;

// Immutable pseudo-stateless version of an Iterator
public class B4JIterator<T> {
	public final T elem;
	public final boolean hasNext;
	
	private final java.util.Iterator<T> it;
	
	public B4JIterator(java.util.Iterator<T> it) {
		this(null, it);
	}
	
	private B4JIterator(T elem, java.util.Iterator<T> it) {
		this.elem = elem;
		this.hasNext = it.hasNext();
		this.it = it;
	}
	
	public B4JIterator<T> next() {
		T elem = it.next();
		return new  B4JIterator<T>(elem, it);
	}

	@Override
	public String toString() {
		return "B4JIterator [elem=" + elem + ", hasNext=" + hasNext + "]";
	}
}
