package basic4j.lang;

import java.util.Optional;

import basic4j.Basic4JType;

public class Type {
	public final java.lang.Class<?> javaClass;
	public final Optional<java.lang.Class<?>[]> maybeTemplateTypes;
	
	public Type(java.lang.Class<?> javaClass) {
		this(javaClass, Optional.empty());
	}
	
	public Type(java.lang.Class<?> javaClass, java.lang.Class<?>[] templateTypes) {
		this(javaClass, Optional.of(templateTypes));
	}
	
	public Type(java.lang.Class<?> javaClass, Optional<java.lang.Class<?>[]> maybeTemplateTypes) {
		this.javaClass = javaClass;
		this.maybeTemplateTypes = maybeTemplateTypes;
	}
	
	public String toJavaString() {
		String template = "";
		if (this.maybeTemplateTypes.isPresent()) {
			for (java.lang.Class<?> each: this.maybeTemplateTypes.get()) {
				if (!template.isEmpty()) {
					template += ", ";
				}
				
				template += each.getSimpleName();
			}
			
			template = "<" + template + ">";
		}
		
		return this.javaClass.getSimpleName() + template;
	}
	
	public String toBasic4JString() {
		String template = "";
		if (this.maybeTemplateTypes.isPresent()) {
			for (java.lang.Class<?> each: this.maybeTemplateTypes.get()) {
				if (!template.isEmpty()) {
					template += ", ";
				}
				
				template += each.getAnnotation(Basic4JType.class).name();
			}
			
			template = "<" + template + ">";
		}
		
		return this.javaClass.getAnnotation(Basic4JType.class).name() + template;
	}
}
