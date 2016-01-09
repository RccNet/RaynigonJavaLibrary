package com.raynigon.lib.events.handling;

/**Generated on 09.09.2015 by Simon Schneider in Project <b>RaynigonJavaLib</b><p>
 * This interface should be used for often called Events which have different Content, 
 * the content id affects if a Method is invoked or not.  
 * @author Simon Schneider
 */
public interface ContentBasedEvent {

	/**Returns the Event Id of the current instance of this event
	 * @return	the content id of this Event instance
	 */
	public int getContentId();
}
