package com.raynigon.lib.events.handling;

/**
 * Generated: 26.03.2017 by Simon Schneider Project: RayCommons
 * right reserved by Raynigon.de
 * 
 * @author Simon Schneider The IEventManager is the Interface for the {@link EventManager}
 */
public interface IEventManager{

    /**
     * Fires a Event and call the registered EventListeners in a new Thread
     * 
     * @param event
     *            The Event which should be fired
     */
    public void fireEvent(final Event event);

    /**
     * Fires a Event and synchronously calls the registered EventListeners
     * 
     * @param event
     *            The Event which should be fired
     */
    public void fireSyncEvent(Event event);

    /**
     * Registers a new EventListener, the methods in the EventListener are
     * analyzed, every Event receiving Method has to have a {@link EventHandler}
     * Annotation otherwise it will not receive a fired Event. All matching
     * methods will be saved
     * 
     * @param inEventListener
     *            The Event Listener Object which will be analyzed
     */
    public void registerListener(EventListener inEventListener);

    /**
     * Removes all Methods of this EventListener from this EventManager
     * 
     * @param inEventListener
     *            The Event Listener from which all methods should be deleted
     */
    public void removeListener(EventListener inEventListener);

    /**
     * Registers an Event Executor, which can be used to execute an Event in a
     * special Thread.
     * 
     * @param id
     *            The id of the EventExecutor
     * @param inExecutor
     *            The EventExecutor Object
     */
    public void registerExecutor(int id, EventExecutor inExecutor);

    /**
     * Removed an EventExecutor from the registered Event Executors
     * 
     * @param id
     *            The Id of the EventExecutor which should be unrgistered
     */
    public void unregisterExecutor(int id);

    /**
     * Removed an EventExecutor from the registered Event Executors
     * 
     * @param inExecutor
     *            The Event Executor object which was registered
     */
    public void unregisterExecutor(EventExecutor inExecutor);
}
