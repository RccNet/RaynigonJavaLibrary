package com.raynigon.lib.datatypes;

import java.lang.reflect.Array;

public class ArrayUtils{
    
    public static <T> int findElement(T[] array, T element, ItemComparator<T> comp){
        int position = -1;
        for(int i=0;i<array.length;i++){
            if(!comp.equals(array[i],element))
                continue;
            position = i;
            break;
        }
        return position;
    }
    
    public static <T> int findElement(T[] array, T element){
        return findElement(array, element, ItemComparator.getDefault());
    }
    
    public static int findElement(byte[] array, byte element){
        int position = -1;
        for(int i=0;i<array.length;i++){
            if(array[i]!=element)
                continue;
            position = i;
            break;
        }
        return position;
    }
    
    public static int findElement(char[] array, char element){
        int position = -1;
        for(int i=0;i<array.length;i++){
            if(array[i]!=element)
                continue;
            position = i;
            break;
        }
        return position;
    }
    
    public static int findElement(short[] array, short element){
        int position = -1;
        for(int i=0;i<array.length;i++){
            if(array[i]!=element)
                continue;
            position = i;
            break;
        }
        return position;
    }
    
    public static int findElement(int[] array, int element){
        int position = -1;
        for(int i=0;i<array.length;i++){
            if(array[i]!=element)
                continue;
            position = i;
            break;
        }
        return position;
    }
    
    public static int findElement(long[] array, long element){
        int position = -1;
        for(int i=0;i<array.length;i++){
            if(array[i]!=element)
                continue;
            position = i;
            break;
        }
        return position;
    }
    
    public static <T> T[] removeElement(T[] array, T element){
        int pos = findElement(array, element);
        if(pos==-1)
            return array;
        @SuppressWarnings("unchecked")
        T[] result = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length-1);
        int length0 = pos;
        int length1 = array.length-(pos+1);
        if(length0>0)
            System.arraycopy(array, 0, result, 0, length0);
        if(length1>0)
            System.arraycopy(array, pos+1, result, length0, length1);
        return result;
    }

    public static byte[] removeElement(byte[] array, byte element){
        int pos = findElement(array, element);
        if(pos==-1)
            return array;
        byte[] result = new byte[array.length-1];
        int length0 = pos;
        int length1 = array.length-(pos+1);
        if(length0>0)
            System.arraycopy(array, 0, result, 0, length0);
        if(length1>0)
            System.arraycopy(array, pos+1, result, length0, length1);
        return result;
    }
    
    public static char[] removeElement(char[] array, char element){
        int pos = findElement(array, element);
        if(pos==-1)
            return array;
        char[] result = new char[array.length-1];
        int length0 = pos;
        int length1 = array.length-(pos+1);
        if(length0>0)
            System.arraycopy(array, 0, result, 0, length0);
        if(length1>0)
            System.arraycopy(array, pos+1, result, length0, length1);
        return result;
    }
    
    public static short[] removeElement(short[] array, short element){
        int pos = findElement(array, element);
        if(pos==-1)
            return array;
        short[] result = new short[array.length-1];
        int length0 = pos;
        int length1 = array.length-(pos+1);
        if(length0>0)
            System.arraycopy(array, 0, result, 0, length0);
        if(length1>0)
            System.arraycopy(array, pos+1, result, length0, length1);
        return result;
    }
    
    public static int[] removeElement(int[] array, int element){
        int pos = findElement(array, element);
        if(pos==-1)
            return array;
        int[] result = new int[array.length-1];
        int length0 = pos;
        int length1 = array.length-(pos+1);
        if(length0>0)
            System.arraycopy(array, 0, result, 0, length0);
        if(length1>0)
            System.arraycopy(array, pos+1, result, length0, length1);
        return result;
    }    
    
    public static long[] removeElement(long[] array, long element){
        int pos = findElement(array, element);
        if(pos==-1)
            return array;
        long[] result = new long[array.length-1];
        int length0 = pos;
        int length1 = array.length-(pos+1);
        if(length0>0)
            System.arraycopy(array, 0, result, 0, length0);
        if(length1>0)
            System.arraycopy(array, pos+1, result, length0, length1);
        return result;
    }

    public static <T> T[] addElement(T[] array, T element){
        @SuppressWarnings("unchecked")
        T[] result = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length+1);
        System.arraycopy(array, 0, result, 0, array.length);
        result[array.length] = element;
        return result;
    }
    
    public static byte[] addElement(byte[] array, byte element){
        byte[] result = new byte[array.length+1];
        System.arraycopy(array, 0, result, 0, array.length);
        result[array.length] = element;
        return result;
    }
    
    public static char[] addElement(char[] array, char element){
        char[] result = new char[array.length+1];
        System.arraycopy(array, 0, result, 0, array.length);
        result[array.length] = element;
        return result;
    }
    
    public static short[] addElement(short[] array, short element){
        short[] result = new short[array.length+1];
        System.arraycopy(array, 0, result, 0, array.length);
        result[array.length] = element;
        return result;
    }
    
    public static int[] addElement(int[] array, int element){
        int[] result = new int[array.length+1];
        System.arraycopy(array, 0, result, 0, array.length);
        result[array.length] = element;
        return result;
    }
    
    public static long[] addElement(long[] array, long element){
        long[] result = new long[array.length+1];
        System.arraycopy(array, 0, result, 0, array.length);
        result[array.length] = element;
        return result;
    }
}
