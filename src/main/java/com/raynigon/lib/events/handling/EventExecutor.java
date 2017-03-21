package com.raynigon.lib.events.handling;

/**Generated on 21.03.2017 by Simon Schneider in Project <b>RaynigonJavaLib</b><p>
 * The EventHandler Annotation is used for declaring methods to be called by the {@link EventManager}
 * @author Simon Schneider
 */
@FunctionalInterface
public interface EventExecutor {

	/** Executes an Runnable 
	 *  which executes the Event Method
	 * @param runner	The Runnable which contains the Event Method
	 */
	public void execute(Runnable runner);
}
