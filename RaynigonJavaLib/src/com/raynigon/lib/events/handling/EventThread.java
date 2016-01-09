package com.raynigon.lib.events.handling;

import java.util.List;

/**Generated on 14.09.2015 by Simon Schneider in Project <b>RaynigonJavaLib</b><p>
 * Just an internal class for calling EventMethods
 * @author Simon Schneider
 */
public class EventThread extends Thread{

	private List<EventMethod> eventMethods;
	private Event event;

	/**Creates a new Thread
	 * @param inEvent			the Event which is given
	 * @param inEventMethods	a sorted list with all matching EventMethods
	 */
	public EventThread(Event inEvent,List<EventMethod> inEventMethods){
		event = inEvent;
		eventMethods = inEventMethods;
	}

	@Override
	public void run(){
		for(EventMethod em : eventMethods){
			em.callMethod(event);
		}
	}
}
