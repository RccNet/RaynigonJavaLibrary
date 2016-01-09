package com.raynigon.old_lib.tools;

public class PairHolder<T, U> {
	private T oa = null;
	private U ob = null;
	
	public PairHolder(){
		oa = null;
		ob = null;
	}
	
	public PairHolder(T t_oa, U t_ob){
		oa = t_oa;
		ob = t_ob;
	}
	
	public T getFirst(){
		return oa;
	}
	
	public U getSecond(){
		return ob;
	}
	
	public void setFirst(T t_oa){
		oa = t_oa;
	}
	
	public void setSecond(U t_ob){
		ob = t_ob;
	}
}
