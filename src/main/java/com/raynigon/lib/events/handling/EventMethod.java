package com.raynigon.lib.events.handling;

import java.lang.reflect.Method;

/**Generated on 14.09.2015 by Simon Schneider in Project <b>RaynigonJavaLib</b><p>
 * This is a internal storage container for every listening method registered in the {@link EventManager}
 * It should only be used by the {@link EventManager}
 * @author Simon Schneider
 */
public final class EventMethod {

	private EventListener call_object;
	private Method call_method;
	private Class<? extends Event> parameter_class;
	private EventHandler eventHandler;
	private ContentEventHandler contentEventHandler;
	private EventExecutor executor;
	
	
	/**
	 * @param inCallObject
	 * @param inCallMethod
	 * @param inParameterClass
	 * @param inEventHandler
	 * @param inContentEventHandler
	 * @param inExecutor
	 */
	protected EventMethod(EventListener inCallObject, Method inCallMethod, 
			Class<? extends Event> inParameterClass, 
			EventHandler inEventHandler, ContentEventHandler inContentEventHandler, 
			EventExecutor inExecutor) {
		call_object = inCallObject;
		call_method = inCallMethod;
		parameter_class = inParameterClass;
	    eventHandler = inEventHandler;
	    contentEventHandler = inContentEventHandler;
		executor = inExecutor;
	}
	
	   /**
     * @param inCallObject
     * @param inCallMethod
     * @param inParameterClass
     * @param inEventHandler
     * @param inContentEventHandler
     * @param inExecutor
     */
    protected EventMethod(EventListener inCallObject, Method inCallMethod, 
            Class<? extends Event> inParameterClass, 
            EventHandler inEventHandler, EventExecutor inExecutor) {
        this(inCallObject, inCallMethod, inParameterClass, inEventHandler, null, inExecutor);
    }
    
    /**
     * @param inCallObject
     * @param inCallMethod
     * @param inParameterClass
     * @param inEventHandler
     * @param inContentEventHandler
     * @param inExecutor
     */
    protected EventMethod(EventListener inCallObject, Method inCallMethod, 
            Class<? extends Event> inParameterClass, 
            EventHandler inEventHandler, ContentEventHandler inContentEventHandler) {
        this(inCallObject, inCallMethod, inParameterClass, inEventHandler, inContentEventHandler, null);
    }
	
	
	/**Calls the referenced Method
	 * @param event	The Event which should be passed as the parameter
	 * @return true if the method call was a success, false if an error occured
	 */
	protected boolean callMethod(final Event event){
		if(executor!=null){
			executor.execute(()->executeMethodCall(event));
			return true;
		}
		return executeMethodCall(event);
	}


	private boolean executeMethodCall(Event event) {
		boolean ret = false;
		try{
			call_method.invoke(call_object, event);
			ret = true;
		}catch(Exception e){
			System.err.println("Event:"+event.getClass().getName());
			System.err.println("Listener"+call_object.getClass().getName());
			System.err.println("Listener Param"+parameter_class.getName());
			e.printStackTrace();
		}
		return ret;
	}

	/**Returns the {@link EventPriority} defined by the methods {@link EventHandler}
	 * @return	the {@link EventPriority} for this method
	 */
	protected EventPriority getPriority() {
		return eventHandler.priority();
	}

	/**Returns the @{link java.lang.Class}  for the parameter of the method
	 * @return	the @{link java.lang.Class} of the parameter
	 */
	protected Class<? extends Event> getParameterClass() {
		return parameter_class;
	}

	/**Returns if a content Id exists
	    * @return   the contentId for this method
	    */
	    public boolean hasContentId() {
	        return contentEventHandler!=null;
	    }
	    
	    /**Returns the contentId defined by the methods {@link EventHandler}
	    * @return   the contentId for this method
	    */
	    public int getContentId() {
	        return contentEventHandler!=null ? contentEventHandler.contentId() : 0;
	    }
}
