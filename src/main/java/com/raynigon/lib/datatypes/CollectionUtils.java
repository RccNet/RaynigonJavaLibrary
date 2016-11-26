package com.raynigon.lib.datatypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionUtils{

    public static <T> boolean contains(Collection<T> collection, T element){
        return contains(collection, element, ItemComparator.getDefault());
    }

    public static <T, U> boolean contains(Collection<T> collection, ItemCheck<T> comparator){
        boolean result = false;
        for(T item : collection){
            if(!comparator.check(item))
                continue;
            result = true;
            break;
        }
        return result;
    }
    
    public static <T> boolean contains(Collection<T> collection, T element, ItemComparator<T> comparator){
        boolean result = false;
        for(T item : collection){
            if(!comparator.equals(item, element))
                continue;
            result = true;
            break;
        }
        return result;
    }

    public static <T> int find(List<T> collection, T element){
        return collection.indexOf(element);
    }
    
    public static <T> int find(List<T> collection, ItemCheck<T> comparator){
        int result = -1;
        for(int index = 0;index<collection.size();index++){
            T item = collection.get(index);
            if(!comparator.check(item))
                continue;
            result = index;
            break;
        }
        return result;
    }
    
    public static <T> int find(List<T> collection, T element, ItemComparator<T> comparator){
        int result = -1;
        for(int index = 0;index<collection.size();index++){
            T item = collection.get(index);
            if(!comparator.equals(item, element))
                continue;
            result = index;
            break;
        }
        return result;
    }
    
    public static <T> T findItem(List<T> collection, ItemCheck<T> comparator){
        T result = null;
        for(T item : collection){
            if(!comparator.check(item))
                continue;
            result = item;
            break;
        }
        return result;
    }

    public static <T> Collection<T> copy(Collection<T> input, ItemCheck<T> comparator){
        List<T> result = new ArrayList<>();
        copy(input, result, comparator);
        return result;
    }
    
    public static <T> void copy(Collection<T> input, Collection<T> output, ItemCheck<T> comparator){
        for(T item : input){
            if(!comparator.check(item))
                continue;
            output.add(item);
        }
    }
}
