package com.raynigon.lib.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Simon Schneider
 * <b>Use</b> {@link SerializedName} <b>instead</b>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Deprecated
public @interface JSONMethod {

	public static final int FUNCTION_GET = 0;
	public static final int FUNCTION_SET = 1;
	
	public String Name();
	public int Function() default FUNCTION_GET;
}
