package com.raynigon.old_lib.logging;

/**This Managers is made to manage all Frames in cause an Exception causes a Pause or a Shutdown
 * of the Software
 * @author Simon Schneider
 */
public class RaynigonFrameManager {

	/**Stops all Threads/Processes after printing a new Error Frame
	 * @param id		The Error id
	 * @param message	The Error Message
	 */
	public void stopAll(int id, String message) {
		// TODO Auto-generated method stub
		System.exit(id);
	}

	/**Pauses all Windows
	 * @param id		The Error id
	 * @param message	The Error Message
	 */
	public void pauseAll(int id, String message) {
		// TODO Auto-generated method stub
		
	}

}
