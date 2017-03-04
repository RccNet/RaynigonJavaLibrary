package com.raynigon.lib.utils;

public class Pair<T,U>{

    public T first;
    public U second;
    
    public Pair(){
        this(null, null);
    }
    
    public Pair(T inFirst, U inSecond){
        first = inFirst;
        second = inSecond;
    }
}
