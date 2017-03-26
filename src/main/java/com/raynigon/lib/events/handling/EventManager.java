package com.raynigon.lib.events.handling;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Generated: 09.09.2015 by Simon Schneider Project: KalaidokosmosProtocol All
 * right reserved by Raynigon.de
 * 
 * @author Simon Schneider The EventManager has a List of EventListeners on
 *         which the fired Events will received
 */
public class EventManager implements IEventManager{

    /**
     * A Map of Methods referred by the Class of an Event
     */
    private Map<Class<? extends Event>, List<EventMethod>> cached_methods;

    /**
     * A Map of Methods referred by their Object in which their are inherit
     */
    private Map<EventListener, EventMethod[]>              method_map;

    /**
     * A List of Methods, just all Methods in a List, i don`t know what you
     * expected here...
     */
    private List<EventMethod>                              method_list;

    /**
     * A Map of Event Executors, all registered Exeutors are saved here
     */
    private Map<Integer, EventExecutor>                    executor_map;

    /**
     * Creates a new EventManager Object There should be only one Object per JVM
     * Instance! If you need two have more EventManagers you`re free to create
     * more, but be carefully if you use more than one!
     */
    public EventManager(){
        cached_methods = new HashMap<Class<? extends Event>, List<EventMethod>>();
        method_list = new LinkedList<EventMethod>();
        method_map = new HashMap<EventListener, EventMethod[]>();
        executor_map = new HashMap<>();
    }

    @Override
    public synchronized void fireEvent(final Event event){
        if(event == null){
            throw new NullPointerException("The Event mustn't be null");
        }
        List<EventMethod> ems = getCallingMethods(event);
        // Use the JVM Executer pool for parallel executing
        ems.stream().parallel().forEach((EventMethod em)->em.callMethod(event));
    }

    @Override
    public synchronized void fireSyncEvent(Event event){
        if(event == null){
            throw new NullPointerException("The Event mustn't be null");
        }
        List<EventMethod> ems = getCallingMethods(event);

        // Simple for loop to make it clear,
        // that this events will be executed synchronously
        for(EventMethod em : ems){
            em.callMethod(event);
        }
    }

    @Override
    public synchronized void registerListener(EventListener inEventListener){
        if(inEventListener == null)
            throw new NullPointerException("The EventListener mustn't be null");
        List<EventMethod> eventMethods = new ArrayList<EventMethod>();
        List<Method> methods = findMethodsInSuperclasses(inEventListener.getClass());
        for(Method method : methods){
            EventMethod em = createEventMethod(inEventListener, method);
            if(em == null)
                continue;
            eventMethods.add(em);
        }
        EventMethod[] evms = new EventMethod[eventMethods.size()];
        evms = eventMethods.toArray(evms);
        method_map.put(inEventListener, evms);
    }

    @Override
    public synchronized void removeListener(EventListener inEventListener){
        if(inEventListener == null)
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

    @Override
    public void registerExecutor(int id, EventExecutor inExecutor){
        synchronized(executor_map){
            if(executor_map.containsKey(id))
                throw new IllegalArgumentException("Executor Id already exists");
            executor_map.put(id, inExecutor);
        }
    }

    @Override
    public void unregisterExecutor(int id){
        synchronized(executor_map){
            executor_map.remove(id);
        }
    }

    @Override
    public void unregisterExecutor(EventExecutor inExecutor){
        int id = 0;
        boolean found = false;
        for(Entry<Integer, EventExecutor> entry : executor_map.entrySet()){
            if(entry.getValue() == inExecutor){
                found = true;
                id = entry.getKey();
            }
        }
        if(!found)
            return;
        executor_map.remove(id);
    }

    /**
     * Searches for all Methods which are defined in the base and all its super
     * classes
     * 
     * @param clazz
     *            The Base Class
     * @return a list of all Methods of the specified Classes
     */
    private List<Method> findMethodsInSuperclasses(Class<?> clazz){
        List<Method> methods = new ArrayList<>();
        while (clazz != null && !clazz.equals(Object.class)){
            methods.addAll(Arrays.asList(clazz.getMethods()));
            clazz = clazz.getSuperclass();
        }
        return methods;
    }

    /**
     * Internal Method for Processing Event Methods
     * 
     * @param inEventListener
     *            The Event Listener for which the Method should be processed
     * @param method
     *            The Method which should be processed
     * @return The created EventMethod, or null if no Event Method was created
     */
    private EventMethod createEventMethod(EventListener inEventListener, Method method){
        EventHandler ev = method.getAnnotation(EventHandler.class);
        ContentEventHandler cev = method.getAnnotation(ContentEventHandler.class);
        if(ev == null)
            return null;
        EventExecutor executor = null;
        if(ev.executorId() != -1)
            executor = executor_map.get(ev.executorId());
        //TODO see if its needed to log an error here, when no executor was found
        Class<?>[] params = method.getParameterTypes();
        if(params.length == 1 && ev != null){
            if(Event.class.isAssignableFrom(params[0])){
                Class<? extends Event> param = params[0].asSubclass(Event.class);
                EventMethod evm = new EventMethod(inEventListener, method, param, ev, cev, executor);
                addEventMethod(evm);
                return evm;
            }
        }
        return null;
    }

    /**
     * Adds a EventMethod to the matching events in the
     * {@link EventManager#cached_methods}
     * 
     * @param eventMethod
     *            The {@link EventMethod} which should be added
     */
    private void addEventMethod(EventMethod eventMethod){
        method_list.add(eventMethod);
        for(Class<? extends Event> eventClazz : cached_methods.keySet()){
            if(eventClazz.isAssignableFrom(eventMethod.getParameterClass())){//Checks if the given class is a superclass of the params class
                cached_methods.get(eventClazz).add(eventMethod);
            }
        }
    }

    /**
     * Creates a List of all Methods which should be called when this Event is
     * fired. If the Event is not in the {@link EventManager#cached_methods} it
     * will be added to it
     * 
     * @param event
     *            The event for which the Methods should be searched
     * @return A list of {@link EventMethod}s
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

    /**
     * Resolves all Event Methods which should be called for the Combination of
     * EventClass and ContentId
     * 
     * @param calling_class
     *            The Class of the Event which should be processed
     * @param contentBased
     *            Flag which states if is its a content based Event
     * @param contendId
     *            The Content Id of the processed Event
     * @return list of Event Methods which match the given Event and ContentId
     */
    private List<EventMethod> resolveCallingMethod(Class<? extends Event> calling_class, boolean contentBased,
            int contendId){
        Stream<EventMethod> stream = method_list.stream().parallel();
        stream = stream.filter((EventMethod em)->em.getParameterClass().isAssignableFrom(calling_class));
        stream = stream.sorted(EventManager::compareEventMethods);
        List<EventMethod> cacheList = stream.collect(Collectors.toList());
        cached_methods.put(calling_class, cacheList);
        if(contentBased){
            stream = cacheList.stream();
            stream = stream.filter((EventMethod method)->checkContentId(method, contendId));
            return stream.collect(Collectors.toList());
        }
        return cacheList;
    }
    
    /**
     * Returns a list of Event Methods filtered by Content Id.
     * 
     * @param calling_class
     *            The Class of the processed Event
     * @param contentBased
     *            Flag which states if is its a content based Event
     * @param contendId
     *            The Content Id of the processed Event
     * @return list of Event Methods which match the given Event and ContentId
     * @throws {@link
     *             NullPointerException} thrown if no Event Method was found in
     *             the cached Methods Map
     */
    private List<EventMethod> getCachedMethods(Class<? extends Event> calling_class, boolean contentBased,
            final int contendId){
        List<EventMethod> methods = cached_methods.get(calling_class);
        if(methods == null)
            throw new NullPointerException("Cached Methods does not contain a method of the given Event Class");
        if(contentBased){
            Stream<EventMethod> stream = methods.stream();
            stream = stream.filter((EventMethod method)->checkContentId(method, contendId));
            methods = stream.collect(Collectors.toList());
        }
        return methods;
    }

    /**compares the EventMethods by their Priority. 
     * When this is used to sort Lists, the List will sorted from low to high
     * @param arg0  The first EventMethod to compare
     * @param arg1  The second EventMethod to compare
     * @return
     */
    private static int compareEventMethods(EventMethod arg0, EventMethod arg1){
                // arg1 first => low to high sorting
                // arg0 first => high to low sorting
                // Change Javadoc if args were changed
                return arg1.getPriority().compare(arg0.getPriority());
    }
    
    /**
     * Checks if the given Method has a content id, if true, it checks if the
     * given contentId and the content id of the given method are the same. if
     * the method does not have a content id it returns true
     * 
     * @param method
     *            The method which should be checked
     * @param contendId
     *            The content id against the method should be checked
     * @return true if the content if matches or the method does not have a
     *         content id, false if the method has a content id and it does not
     *         match with the given content id
     */
    private boolean checkContentId(EventMethod method, int contendId){
        if(!method.hasContentId())
            return true;
        return method.getContentId() == contendId;
    }

}
