package com.raynigon.lib.events.handling;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**Generated on 24.03.2017 by Simon Schneider in Project <b>RaynigonJavaLib</b><p>
 * The ContentEventHandler Annotation is used for declaring content based event methods to be called by the {@link EventManager}
 * @author Simon Schneider
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ContentEventHandler {

	/**The {@link EventHandler#contentId()} described the content of an called Event,
	 * if the EventContentId and this content id matches, the method will be called.
	 * This will only wok for {@link ContentBasedEvent}s. Usually this method will be called
	 * without checking the content id
	 * @return	the content id of the listening Event 
	 */
	int contentId();
}
