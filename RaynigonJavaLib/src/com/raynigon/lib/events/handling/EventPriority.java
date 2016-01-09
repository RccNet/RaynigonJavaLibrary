package com.raynigon.lib.events.handling;

/**Generated on 14.09.2015 by Simon Schneider in Project <b>RaynigonJavaLib</b><p>
 * The EventPriority defines in  which order Events will be called
 * @author Simon Schneider
 */
public enum EventPriority {

	/** Latest called method
	 */
	HIGHEST(3),
	/** Later called method
	 */
	HIGHER(2),
	/** little bit later called method
	 */
	HIGH(1),
	/** Default priority
	 */
	NEUTREAL(0),
	/** little bit earlier called method
	 */
	LOW(-1),
	/** earlier called method
	 */
	LOWER(-2),
	/** Earliest called method
	 */
	LOWEST(-3);
	
	private int priority;

	private EventPriority(int inPriority) {
		priority = inPriority;
	}
	
	/**Compares this EventPriority with a given EventPriority
	 * @param prio	the given EventPriority
	 * @return	the difference as an integer
	 */
	public int compare(EventPriority prio){
		return prio.priority-priority;
	}

	/**Returns the number representation of this Enum
	 * @return	an integer as number representation
	 */
	public int getNumber() {
		return priority;
	}
}

