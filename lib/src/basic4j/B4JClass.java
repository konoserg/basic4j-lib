package basic4j;



import java.util.Set;

public class B4JClass {
	public final B4JString name;
	public final Set<B4JString> fieldNames;
	
	public B4JClass(B4JString name, Set<B4JString> fieldNames) {
		this.name = name;
		this.fieldNames = fieldNames;
	}
}
