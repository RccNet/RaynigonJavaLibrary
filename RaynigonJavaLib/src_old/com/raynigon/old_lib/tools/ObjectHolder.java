package com.raynigon.old_lib.tools;

public class ObjectHolder<T> {

	private T object = null;
	
	public void setObject(T obj){
		object = obj;
	}
	
	public T getObject(){
		return object;
	}
}
