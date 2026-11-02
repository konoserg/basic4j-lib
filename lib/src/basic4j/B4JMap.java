package basic4j;



@Collection
@Basic4JType(name="Map")
public class B4JMap<K, V> {
	private final java.util.TreeMap<K, V> val;
	
	public B4JMap() {
		this(new B4JEntry[0]);
	}
	
	public B4JMap(B4JEntry<K, V>[] init) {
		this.val = new java.util.TreeMap<>();
		for (B4JEntry<K, V> each: init) {
			val.put(each.key, each.val);
		}
	}
	
	public B4JMap(java.util.TreeMap<K, V> val) {
		this.val = val;
	}
	
	public B4JMap<K, V> set(K key, V value) {
		java.util.TreeMap<K, V> newVal = new java.util.TreeMap<>(this.val);
		newVal.put(key, value);
		return new B4JMap<>(newVal);
	}
	
	public V get(K key) {
		return val.get(key);
	}
	
	public Boolean isEmpty() {
		return val.isEmpty();
	}
	
	public B4JInteger size() {
		return new B4JInteger(val.size());
	}
	
	public B4JList<B4JEntry<K, V>> entries() {
		B4JList<B4JEntry<K, V>> result = new B4JList<B4JEntry<K, V>>();
		for (java.util.HashMap.Entry<K, V> each: val.entrySet()) {
			result = result.add(new B4JEntry<>(each.getKey(), each.getValue()));
		}
			
		return result;
	}

	@Override
	public String toString() {
		return val.toString();
	}
}
