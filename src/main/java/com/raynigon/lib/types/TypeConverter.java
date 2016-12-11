package com.raynigon.lib.types;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class TypeConverter{

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static<T> T toType(String value, Class<T> type){
        if(type==String.class){
            return (T) value;
        }else if(type.isPrimitive()){
            return toPrimitiveType(value, type);
        }else if(type.isEnum()){
            Class<? extends Enum> enumType = type.asSubclass(Enum.class);
            return (T) Enum.valueOf(enumType, value);
        }else if(getMatchingConstructor(type)!=null){
            Constructor<T> con = getMatchingConstructor(type);
            try{
                return con.newInstance(value);
            }catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
            {e.printStackTrace();}
        }else{
            throw new RuntimeException("Unknown Type: "+type.getName());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static <T> T toPrimitiveType(String value, Class<T> type){
        if(type==Long.TYPE)
            return (T) new Long(value);
        else if(type==Integer.TYPE)
            return (T) new Integer(value);
        else if(type==Byte.TYPE)
            return (T) new Byte(value);
        else
            throw new RuntimeException("Unknown Primitive Type: "+type.getSimpleName());
    }

    @SuppressWarnings("unchecked")
    private static <T> Constructor<T> getMatchingConstructor(Class<T> type){
        for(Constructor<?> con : type.getDeclaredConstructors()){
            Class<?>[] params = con.getParameterTypes();
            if(params.length!=1)
                continue;
            if(params[0]!=String.class)
                continue;
            return (Constructor<T>) con;
        }
        return null;
    }
}
