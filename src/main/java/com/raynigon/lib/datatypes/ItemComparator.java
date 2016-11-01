package com.raynigon.lib.datatypes;

public interface ItemComparator<T>{

    public static <U> ItemComparator<U> getDefault(){
      return (U item0, U item1)->{return item0!=null ? item0.equals(item1) : item1==null;};  
    } 
    
    public boolean equals(T item0, T item1);
}
