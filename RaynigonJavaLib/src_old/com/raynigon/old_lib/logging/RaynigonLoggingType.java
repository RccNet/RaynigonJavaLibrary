package com.raynigon.old_lib.logging;

import static com.raynigon.old_lib.logging.RaynigonConsoleColor.*;

/**A Enum with Typed which are used for Logging some stuff
 * @author Simon Schneider
 *
 */
public enum RaynigonLoggingType {

	/**A Normal Logging Task
	 * 
	 */
	Standard(0),
	/**Error occurred but it isn't important,
	 * 
	 */
	Warning(1),
	/**Error which is really important
	 * 
	 */
	Error(2),
	/**Just an Info, for debugging Plugins and etc.
	 * 
	 */
	Info(3), 
	/**This is the real Debug Mode it shows everything which could be needed
	 * to debug the Software. Do never use this in production! In some Projects
	 * does the debug make more than 10GB per hour, log files
	 */
	Debug(4);
	
	private int art;
	
	private RaynigonLoggingType(int a){
		art = a;
	}
	
	/**Returns the Prefix
	 * @return the Prefix
	 */
	public String getText(){
		String text = "";
		switch(art){
		case 0:
			text = "[Message]";
		break;
		case 1:
			text ="[Warning]";
		break;
		case 2:
			text = "[ERROR]";
		break;
		case 3:
			text = "[INFO]";
		break;
		case 4:
			text = "[DEBUG]";
		break;
		}
		return text;
	}
	
	
	/**Returns the Colored Prefix
	 * @return the Colored Prefix
	 */
	public String getColoredText(){
		String text = "";
		switch(art){
		case 0:
			text = WHITE+"[Message]"+RESET;
		break;
		case 1:
			text = YELLOW+"[Warning]"+RESET;
		break;
		case 2:
			text = RED+"[ERROR]"+RESET;
		break;
		case 3:
			text = CYAN+"[INFO]"+RESET;
		break;
		case 4:
			text = LIGHT_BLUE+"[Debug]"+RESET;
		break;
		}
		return text;
	}
	
	/**Return the type as integer
	 * @return	the type as integer
	 */
	public int getType(){
		return art;
	}
}
