package com.raynigon.lib.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Future Annotation indicates that this is a feature which will be available in the Future.
 * It could be that the method wont work completely as expected, or just wraps some old code
 * @author Simon Schneider
 */
@Retention(RetentionPolicy.SOURCE)
@Target(value={
		ElementType.FIELD,
		ElementType.METHOD, 
		ElementType.LOCAL_VARIABLE, 
		ElementType.CONSTRUCTOR, 
		ElementType.PACKAGE,
		ElementType.TYPE
	})
public @interface Future {

	/**Returns the version Number in which the Feature should be fully implemented
	 * @return version in which the Feature should be fully implemented
	 */
	String Version();

}
