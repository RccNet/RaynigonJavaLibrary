package com.raynigon.lib.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use {@link Expose} and {@link SerializedName}
 * @author Simon Schneider
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Deprecated
public @interface JSONAttribute {

	public boolean Affect() default true;
	public String Name() default "";
}
