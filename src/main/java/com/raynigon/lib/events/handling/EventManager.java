package com.raynigon.lib.events.handling;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

/**Generated: 09.09.2015 by Simon Schneider
 * Project: KalaidokosmosProtocol
 * All right reserved by Raynigon.de
 * @author Simon Schneider
 * The EventManager has a List of EventListeners on which the fired Events will received
 */
public class EventManager {
	
	/**Generated: 09.09.2015 by Simon Schneider
	 * Project: KalaidokosmosProtocol
	 * All right reserved by Raynigon.de
	 * @author sschneid
	 * The EventMethodComparator compares the EventMethods by their Priority.
	 * When this is used to sort Lists, the List will sorted from low to high
	 */
	class EventMethodComparator implements Comparator<EventMethod>{
		@Override
		public int compare(EventMethod arg0, EventMethod arg1) {
			// arg1 first => low to high sorting
			// arg0 first => high to low sorting
			// Change Javadoc if args were changed
			return arg1.getPriority().compare(arg0.getPriority());
		}	
	}
	
	/** A Map of Methods referred by the Class of an Event
	 */
	private Map<Class<? extends Event>, List<EventMethod>> cached_methods;
	
	/** A Map of Methods referred by their Object in which their are inherit
	 */
	private Map<EventListener, EventMethod[]> method_map;
	
	/** A List of Methods, just all Methods in a List, i don`t know what you expected here...
	 */
	private List<EventMethod> method_list;
	
	/** A Map of Event Executors, all registered Exeutors are saved here
	 */
	private Map<Integer, EventExecutor> executor_map;
	
	
	/**Creates a new EventManager Object
	 * There should be only one Object per JVM Instance!
	 * If you need two have more EventManagers you`re free to create more,
	 * but be carefully if you use more than one!
	 */
	public EventManager() {
		cached_methods = new HashMap<Class<? extends Event>, List<EventMethod>>();
		method_list = new LinkedList<EventMethod>();
		method_map = new HashMap<EventListener, EventMethod[]>();
		executor_map = new HashMap<>();
	}
	
	/**Fires a Event and call the registered EventListeners in a new Thread
	 * @param event		The Event which should be fired
	 */
	public synchronized void fireEvent(final Event event) {
		if(event==null){
		    throw new NullPointerException("The Event mustn't be null");
		}
		List<EventMethod> ems = getCallingMethods(event);
		ems.stream().parallel().forEach((EventMethod em)->em.callMethod(event));
	}

	/**Fires a Event and synchronously calls the registered EventListeners
	 * @param event		The Event which should be fired
	 */
	public synchronized void fireSyncEvent(Event event) {
		if(event==null){
		    throw new NullPointerException("The Event mustn't be null");
		}
		List<EventMethod> ems = getCallingMethods(event);
		
		for(EventMethod em : ems){
			em.callMethod(event);
		}
	}
	
	
	/**Registers a new EventListener, the methods in the EventListener are analyzed,
	 * every Event receiving Method has to have a {@link EventHandler} Annotation
	 * otherwise it will not receive a fired Event. All matching methods will be saved
	 * @param inEventListener		The Event Listener Object which will be analyzed
	 */
	public synchronized void registerListener(EventListener inEventListener){
	    if(inEventListener==null)
	        throw new NullPointerException("The EventListener mustn't be null");
		List<EventMethod> eventMethods = new LinkedList<EventMethod>();
		Method[] methods = inEventListener.getClass().getMethods();
		for(Method method : methods){
			EventMethod em = createEventMethod(inEventListener, method);
			if(em==null)
				continue;
			eventMethods.add(em);
		}
		EventMethod[] evms = new EventMethod[eventMethods.size()];
		evms = eventMethods.toArray(evms);
		method_map.put(inEventListener, evms);
	}

	/** Internal Method for Processing Event Methods
	 * @param inEventListener	The Event Listener for which the Method should be processed 
	 * @param method			The Method which should be processed
	 * @return					The created EventMethod, or null if no Event Method was created
	 */
	private EventMethod createEventMethod(EventListener inEventListener, Method method) {
		EventHandler ev = method.getAnnotation(EventHandler.class);
		EventExecutor executor = null;
		if(ev.executorId()!=-1)
			executor = executor_map.get(ev.executorId());
		//TODO see if its needed to log an error here, when no executor was found
		Class<?>[] params = method.getParameterTypes();
		if(params.length==1 && ev!=null){
			if(Event.class.isAssignableFrom(params[0])){
				Class<? extends Event> param = params[0].asSubclass(Event.class);
				EventMethod evm = new EventMethod(inEventListener, method, ev, param, executor);
				addEventMethod(evm);
				return evm;
			}
		}
		return null;
	}
	
	/**Removes all Methods of this EventListener from this EventManager
	 * @param inEventListener		The Event Listener from which all methods should be deleted
	 */
	public synchronized void removeListener(EventListener inEventListener){
	    if(inEventListener==null)
            throw new NullPointerException("The EventListener mustn't be null");
		EventMethod[] ems = method_map.get(inEventListener);
		List<EventMethod> methods;
		for(EventMethod em : ems){
			method_list.remove(em);
			for(Object key : cached_methods.keySet()){
				methods = cached_methods.get(key);
				methods.remove(em);
			}
		}
	}

	/** Registers an Event Executor,
	 * which can be used to execute an Event in a special Thread.
	 * @param id			The id of the EventExecutor
	 * @param inExecutor	The EventExecutor Object
	 */
	public void registerExecutor(int id, EventExecutor inExecutor){
		synchronized (executor_map) {
			if(executor_map.containsKey(id))
				throw new IllegalArgumentException("Executor Id already exists");
			executor_map.put(id, inExecutor);
		}
	}
	
	/** Removed an EventExecutor from the registered Event Executors
	 * @param id	The Id of the EventExecutor which should be unrgistered
	 */
	public void unregisterExecutor(int id) {
		synchronized (executor_map) {
			executor_map.remove(id);
		}
	}
	
	/** Removed an EventExecutor from the registered Event Executors
	 * @param inExecutor	The Event Executor object which was registered
	 */
	public void unregisterExecutor(EventExecutor inExecutor){
		int id = 0;
		boolean found = false;
		for(Entry<Integer, EventExecutor> entry : executor_map.entrySet()){
			if(entry.getValue()==inExecutor){
				found = true;
				id = entry.getKey();
			}
		}
		if(!found)
			return;
		executor_map.remove(id);
	}
	
	/**Adds a EventMethod to the matching events in the {@link EventManager#cached_methods} 
	 * @param eventMethod	The {@link EventMethod} which should be added
	 */
	private void addEventMethod(EventMethod eventMethod) {
		method_list.add(eventMethod);
		for(Class<? extends Event> eventClazz : cached_methods.keySet()){
			if(eventClazz.isAssignableFrom(eventMethod.getParameterClass())){//Checks if the given class is a superclass of the params class
				cached_methods.get(eventClazz).add(eventMethod);
			}
		}
	}
	
	/** Creates a List of all Methods which should be called when this Event is fired.
	 * If the Event is not in the {@link EventManager#cached_methods} it will be added to it
	 * @param event		The event for which the Methods should be searched
	 * @return	A list of {@link EventMethod}s
	 */
	private List<EventMethod> getCallingMethods(Event event){
		Class<? extends Event> calling_class = event.getClass();
		boolean contentBased = event instanceof ContentBasedEvent;
		int contendId = -1;
		if(contentBased)
			contendId = ((ContentBasedEvent) event).getContentId();
		if(cached_methods.containsKey(calling_class))
			return getCachedMethods(calling_class, contentBased, contendId);
		return resolveCallingMethod(calling_class, contentBased, contendId);
	}

	/** Resolves all Event Methods which should be called for the Combination of EventClass and ContentId
	 * @param calling_class		The Class of the Event which should be processed
	 * @param contentBased		Flag which states if is its a content based Event
	 * @param contendId			The Content Id of the processed Event
	 * @return	list of Event Methods which match the given Event and ContentId
	 */
	private List<EventMethod> resolveCallingMethod(Class<? extends Event> calling_class, boolean contentBased, int contendId) {
		Stream<EventMethod> stream = method_list.stream().parallel();
		stream = stream.filter((EventMethod em)->em.getParameterClass().isAssignableFrom(calling_class));
		stream = stream.sorted(new EventMethodComparator());
		List<EventMethod> cacheList = Arrays.asList((EventMethod[]) stream.toArray());
		cached_methods.put(calling_class, cacheList);
		if(contentBased){
			stream = cacheList.stream();
			stream = stream.filter((EventMethod method)->method.getContentId() == contendId);
			return Arrays.asList((EventMethod[])stream.toArray());
		}
		return cacheList;
	}

	/** Returns a list of Event Methods filtered by Content Id.
	 * @param calling_class		The Class of the processed Event
	 * @param contentBased		Flag which states if is its a content based Event
	 * @param contendId			The Content Id of the processed Event
	 * @return	list of Event Methods which match the given Event and ContentId
	 * @throws {@link NullPointerException} thrown if no Event Method was found in the cached Methods Map
	 */
	private List<EventMethod> getCachedMethods(Class<? extends Event> calling_class, boolean contentBased, final int contendId) {
		List<EventMethod> methods = cached_methods.get(calling_class);
		if(methods==null)
			throw new NullPointerException("Cached Methods does not contain a method of the given Event Class");
		if(contentBased){
			Stream<EventMethod> stream = methods.stream();
			stream = stream.filter((EventMethod method)->method.getContentId() == contendId);
			methods = Arrays.asList((EventMethod[])stream.toArray());
		}
		return methods;
	}

}
