package com.raynigon.old_lib.tools;

import java.util.Map;

/**
 * @author Simon Schneider
 *
 */
public class Lock<K> {
	
	private K locking_object = null;
	private Object locker = null;
	
	public Lock(K locked) {
		locking_object = locked;
	}
	
	public synchronized void lock(Object tlocker){
		if(!isLocked()){
			locker = tlocker;	
		}
	}
	
	public synchronized void unlock(Object tlocker){
		if(isLocked()){
			if(tlocker==locker){
				locker = null;
			}
		}
	}
	
	public synchronized boolean isLocked(){
		return locker!=null;
	}
	
	public synchronized K getLockingObject(){
		if(!isLocked()){
			return locking_object;
		}
		return null;
	}
	
	public synchronized K getLockingObject(Object tlocker){
		if(locker!=null){
			if(locker == tlocker){
				return locking_object;
			}
		}
		return null;
	}
	
	public void waitUntilOpenLock() {
		try {
			while(isLocked()){
				Thread.sleep(100);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
