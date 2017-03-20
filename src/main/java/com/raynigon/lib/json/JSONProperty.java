package com.raynigon.lib.json;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class JSONProperty{
    public static final int TYPE_FIELD  = 0x100;
    public static final int TYPE_METHOD = 0x200;

    private int             type        = 0;
    private String          name;
    private Method          get_method;
    private Method          set_method;
    private Field           field;
    private Object          obj;

    public JSONProperty(String inName, Object inObj, Field inField){
        type = TYPE_FIELD;
        name = inName;
        obj = inObj;
        field = inField;
    }

    public JSONProperty(String inName, Object inObj, Method inMethod){
        type = TYPE_METHOD;
        name = inName;
        obj = inObj;
        addMethod(inMethod);
    }

    public void addMethod(Method inMethod){
        if(inMethod.getParameterTypes().length == 0 && get_method == null)
            get_method = inMethod;
        else if(inMethod.getParameterTypes().length == 1 && set_method == null)
            set_method = inMethod;
        return;
    }

    public int getType(){
        return type;
    }

    public Object getValue(){
        try{
            if(type == TYPE_FIELD){
                boolean access = field.isAccessible();
                Object value;
                field.setAccessible(true);
                value = field.get(obj);
                field.setAccessible(access);
                return value;
            }else if(type == TYPE_METHOD && get_method != null){
                boolean access = get_method.isAccessible();
                Object value;
                get_method.setAccessible(true);
                value = get_method.invoke(obj);
                get_method.setAccessible(access);
                return value;
            }
        }catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException e){
            throw new JSONParseException("Error during pulling value from " + name, e);
        }
        return null;
    }

    public void setValue(Object value){
        try{
            if(type == TYPE_FIELD){
                boolean access = field.isAccessible();
                field.setAccessible(true);
                field.set(obj, value);
                field.setAccessible(access);
            }else if(type == TYPE_METHOD && set_method != null){
                boolean access = set_method.isAccessible();
                set_method.setAccessible(true);
                set_method.invoke(obj, value);
                set_method.setAccessible(access);
            }
        }catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException e){
            throw new JSONParseException("Error during pushing value " + value.getClass().getSimpleName() + " into " + name, e);
        }
    }

    public Class<?> getDataType(){
        if(type == TYPE_FIELD){
            return field.getType();
        }else if(type == TYPE_METHOD && get_method != null){
            return get_method.getReturnType();
        }else if(type == TYPE_METHOD && set_method != null){
            return set_method.getParameterTypes()[0];
        }
        return null;//TODO throw exception or so
    }

    public Class<?> getValueType(){
        try{
            if(type == TYPE_FIELD){
                return field.get(obj).getClass();
            }else if(type == TYPE_METHOD && get_method != null){
                return get_method.invoke(obj).getClass();
            }
        }catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException e){
            throw new JSONParseException("Error during retreiving value type " + name, e);
        }
        return null;//TODO throw exception or so
    }
}
