package basic4j;



@Basic4JType(name="Entry")
public class B4JEntry<K, V> {
	public final K key;
	public final V val;
	
	public B4JEntry(K key, V val) {
		this.key = key;
		this.val = val;
	}

	@Override
	public java.lang.String toString() {
		return key + " = " + val;
	}
}
