package com.raynigon.lib.events.handling;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
			//arg1 first => low to high sorting
			//arg0 first => high to low sorting
			//Change Javadoc if args were changed
			return arg1.getPriority().compare(arg0.getPriority());
		}	
	}
	
	/** A Map of Methods referred by the Class of an Event
	 */
	private Map<Class<? extends Event>, List<EventMethod>> cached_methods;
	
	/** A Map of Methods referred by their Object in which their are inherit
	 */
	private Map<EventListener, EventMethod[]> method_map;
	
	/**A List of Methods, just all Methods in a List, i don`t know what you expected here...
	 */
	private List<EventMethod> method_list;
	
	
	/**Creates a new EventManager Object
	 * There should be only one Object per JVM Instance!
	 * If you need two have more EventManagers you`re free to create more,
	 * but be carefully if you use more than one!
	 */
	public EventManager() {
		cached_methods = new HashMap<Class<? extends Event>, List<EventMethod>>();
		method_list = new LinkedList<EventMethod>();
		method_map = new HashMap<EventListener, EventMethod[]>();
	}
	
	/**Fires a Event and call the registered EventListeners in a new Thread
	 * @param event		The Event which should be fired
	 */
	public synchronized void fireEvent(Event event) {
		if(event==null){
		    throw new NullPointerException("The Event mustn't be null");
		}
		List<EventMethod> ems = getCallingMethods(event);
		
		EventThread evt = new EventThread(event, ems);
		evt.start();
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
		List<EventMethod> ev_methods = new LinkedList<EventMethod>();
		List<Method> methods = findMethodsInSuperclasses(inEventListener.getClass());
		EventMethod evm = null;
		for(Method method : methods){
			EventHandler ev = method.getAnnotation(EventHandler.class);
			Class<?>[] params = method.getParameterTypes();
			if(params.length==1 && ev!=null){
				if(Event.class.isAssignableFrom(params[0])){
					Class<? extends Event> param = params[0].asSubclass(Event.class);
					evm = new EventMethod(inEventListener, method, ev, param);
					addEventMethod(evm);
					ev_methods.add(evm);
				}
			}
		}
		EventMethod[] evms = new EventMethod[ev_methods.size()];
		evms = ev_methods.toArray(evms);
		method_map.put(inEventListener, evms);
	}

	/** Searches for all Methods which are defined in the base and all its super classes
	 * @param clazz	The Base Class
	 * @return a list of all Methods of the specified Classes
	 */
	private List<Method> findMethodsInSuperclasses(Class<?> clazz) {
		List<Method> methods = new ArrayList<>();
		while(clazz!=null && !clazz.equals(Object.class)){
			methods.addAll(Arrays.asList(clazz.getMethods()));
			clazz = clazz.getSuperclass();
		}
		return methods;
	}
	
	/**Removes all Methods of this EventListener from this EventManager
	 * @param inEventListener		The Event Listener from which all methods should be deleted
	 */
	public synchronized void removeListener(EventListener inEventListener){
	    if(inEventListener==null)
            throw new NullPointerException("The EventListener mustn't be null");
		EventMethod[] ems = method_map.get(inEventListener);
		if(ems==null)
			return;
		List<EventMethod> methods;
		for(EventMethod em : ems){
			method_list.remove(em);
			for(Object key : cached_methods.keySet()){
				methods = cached_methods.get(key);
				methods.remove(em);
			}
		}
		method_map.remove(inEventListener);
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
		List<EventMethod> cacheList = new ArrayList<EventMethod>();
		if(cached_methods.containsKey(calling_class)){
			if(contentBased){
				for(EventMethod em : cached_methods.get(calling_class)){
					if(em.getContentId()!=contendId)
						continue;
					cacheList.add(em);
				}
			}else{
				return new ArrayList<>(cached_methods.get(calling_class));
			}
		}else{
			for(EventMethod method : method_list){
				if(method.getParameterClass().isAssignableFrom(event.getClass())){
					cacheList.add(method);
				}
			}
			Collections.sort(cacheList, new EventMethodComparator());
			cached_methods.put(calling_class, cacheList);
			if(contentBased){
				List<EventMethod> filteredList = new ArrayList<>();
				for(EventMethod em : cacheList){
					if(em.getContentId()!=contendId)
						continue;
					filteredList.add(em);
				}
				cacheList = filteredList;
			}
		}
		return cacheList;
	}

}
