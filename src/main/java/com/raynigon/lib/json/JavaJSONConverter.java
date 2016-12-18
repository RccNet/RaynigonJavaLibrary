package com.raynigon.lib.json;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.raynigon.lib.annotations.Future;

/**
 * Generated on 28.11.2015 by Simon Schneider in Project <b>RaynigonJavaLib</b>
 * <p>
 * 
 * @author Simon Schneider
 */
public class JavaJSONConverter{

    private static JSONObject compMapperFJO(Object inO){
        try{
            return fromJava(inO);
        }catch(IOException e){
            throw new JSONParseException("compatibility Exception", e);
        }
    }

    private static <T> JSONArray compMapperFJC(Collection<T> inColl){
        JSONArray result = new JSONArray();
        if(inColl == null)
            return null;
        try{
            for(T object : inColl){
                if(object==null){
                    result.put((String) null);
                    continue;
                }
                Class<?> type = object.getClass();
                if(Collection.class.isAssignableFrom(type))
                    result.put(fromJavaCollection((Collection<?>) object));
                else if(type.isArray())
                    result.put(fromJavaArray((Object[]) object));
                else
                    result.put(fromJava(object));
                continue;
            }
        }catch(IOException e){
            throw new JSONParseException("compatibility Exception", e);
        }
        return result;
    }

    @Future(Version = "0.0.5")
    public static JSONObject fromJavaObject(Object inObject){
        return compMapperFJO(inObject);
    }

    @Future(Version = "0.0.5")
    public static <T> JSONArray fromJavaArray(T[] inArray){
        return fromJavaCollection(Arrays.asList(inArray));
    }

    @Future(Version = "0.0.5")
    public static <T> JSONArray fromJavaCollection(Collection<T> inList){
        return compMapperFJC(inList);
    }

    public static <T> T fromJSONObject(JSONObject inObject, Class<T> inClazz){
        T obj = createInstance(inClazz);

        Map<String, JSONProperty> valueMap = new HashMap<>();
        addAttributeValuesRecursive(inClazz, obj, valueMap);

        try{
            parse(valueMap, inObject, obj);
        }catch(IOException e){
            throw new JSONParseException("compatibility Exception", e);
        }

        return obj;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> fromJSONArray(JSONArray inArray, Class<T> inClazz){
        return (List<T>) Arrays.asList(insertArray(inClazz, inArray));
    }

    private static <T> void addAttributeValuesRecursive(Class<T> inClazz, T instance,
            Map<String, JSONProperty> valueMap){
        
        addFieldsToValueMap(inClazz.getDeclaredFields(), instance, valueMap);
        addMethodsToValueMap(inClazz.getDeclaredMethods(), instance, valueMap);
        if(inClazz.isAnnotationPresent(JSONClass.class) || inClazz.getSuperclass() == Object.class)
            return;
        addAttributeValuesRecursive(inClazz.getSuperclass(), instance, valueMap);
    }

    private static <T> T createInstance(Class<T> inClazz){
        try{
            return inClazz.newInstance();
        }catch(InstantiationException | IllegalAccessException e){
            return null;
        }
    }

    protected static String getNameFromField(Field inField){
        SerializedName name = inField.getAnnotation(SerializedName.class);
        if(name == null)
            return inField.getName();
        return name.name();
    }

    protected static String getNameFromMethod(Method inMethod){
        SerializedName name = inMethod.getAnnotation(SerializedName.class);
        if(name == null)
            return null;
        return name.name();
    }

    protected static boolean isAffected(Field inField){
        return !inField.isAnnotationPresent(Expose.class);
    }

    protected static boolean isAffected(Method inMethod){
        return inMethod.isAnnotationPresent(SerializedName.class);
    }

    protected static <T> void addFieldsToValueMap(Field[] fields, T obj, Map<String, JSONProperty> map){
        for(Field field : fields){
            if(Modifier.isStatic(field.getModifiers()))
                continue;
            if(!isAffected(field))
                continue;
            String name = getNameFromField(field);
            if(map.containsKey(name))
                return;
            map.put(name, new JSONProperty(name, obj, field));
        }
    }

    protected static <T> void addMethodsToValueMap(Method[] methods, T obj, Map<String, JSONProperty> map){
        for(Method method : methods){
            if(Modifier.isStatic(method.getModifiers()))
                continue;
            if(!isAffected(method))
                continue;
            String name = getNameFromMethod(method);
            if(!method.getReturnType().equals(Void.TYPE) && method.getParameterTypes().length == 0)
                addMethodToValueMap(map, name, obj, method);
            else if(method.getReturnType().equals(Void.TYPE) && method.getParameterTypes().length == 1)
                addMethodToValueMap(map, name, obj, method);
        }
    }

    protected static void addMethodToValueMap(Map<String, JSONProperty> inMap, String name, Object obj,
            Method method){
        if(inMap.containsKey(name))
            inMap.get(name).addMethod(method);
        else
            inMap.put(name, new JSONProperty(name, obj, method));
    }

    protected static Map<String, JSONProperty> findAttributeValues(Object obj){
        Class<?> clazz = obj.getClass();
        Map<String, JSONProperty> attrValues = new HashMap<>();

        addFieldsToValueMap(clazz.getDeclaredFields(), obj, attrValues);
        addMethodsToValueMap(clazz.getDeclaredMethods(), obj, attrValues);
        return attrValues;
    }

    /*
     * #####################################################################
     * #####################################################################
     * #####################################################################
     * JAVA TO JSON - DEPRECATED
     * #####################################################################
     * #####################################################################
     * #####################################################################
     */

    @Deprecated
    public static JSONArray fromJava(Object obj, Class<?> arrayType) throws IOException{
        JSONArray array = new JSONArray();
        Object[] arr = (Object[]) obj;
        for(Object element : arr){
            if(element == null)
                continue;
            Class<?> type = element.getClass();
            if(type.isArray())
                array.put(fromJava(element, type.getComponentType()));
            else if(type.isPrimitive() || type == String.class || Number.class.isAssignableFrom(type))
                array.put(element);
            else
                array.put(fromJava(element));
        }
        return array;
    }

    @Deprecated
    public static <T> JSONObject fromJava(T obj) throws IOException{
        if(obj == null)
            return null;
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) obj.getClass();
        JSONObject jsonobj = new JSONObject();

        Map<String, JSONProperty> valueMap = new HashMap<>();
        addAttributeValuesRecursive(clazz, obj, valueMap);

        for(String propertyName : valueMap.keySet()){
            JSONProperty property = valueMap.get(propertyName);
            Object propertyValue = property.getValue();
            if(propertyValue == null){
                jsonobj.put(propertyName, (String) null);
                continue;
            }
            Class<?> type = property.getDataType();
            if(type.isPrimitive() || String.class.isAssignableFrom(type)){
                jsonobj.put(propertyName, propertyValue);
            }else if(Double.class.isAssignableFrom(type) || Float.class.isAssignableFrom(type)){
                jsonobj.put(propertyName, ((Number) propertyValue).doubleValue());
            }else if(Number.class.isAssignableFrom(type)){
                jsonobj.put(propertyName, ((Number) propertyValue).longValue());
            }else if(Boolean.class.isAssignableFrom(type)){
                jsonobj.put(propertyName, ((Boolean) propertyValue).booleanValue());
            }else if(type.isArray()){
                jsonobj.put(propertyName, fromJava(propertyValue, type.getComponentType()));
            }else if(type == JSONObject.class || type == JSONArray.class){
                jsonobj.put(propertyName, propertyValue);
            }else if(Collection.class.isAssignableFrom(type)){
                JSONArray jArr = new JSONArray();
                Collection<?> coll = (Collection<?>) propertyValue;
                for(Object item : coll){
                    Class<?> itemType = item.getClass();
                    if(type.isPrimitive() || String.class.isAssignableFrom(type)){
                        jArr.put(item);
                    }else if(Number.class.isAssignableFrom(itemType)){
                        jArr.put(((Number) item).longValue());
                    }else if(itemType.isArray()){
                        jArr.put(fromJavaArray((Object[]) item));
                    }else if(Collection.class.isAssignableFrom(itemType)){
                        jArr.put(fromJavaCollection((Collection<?>) item));
                    }else if(itemType == JSONObject.class || itemType == JSONArray.class){
                        jArr.put(item);
                    }else{
                        jArr.put(fromJava(item));
                    }
                }
                jsonobj.put(propertyName, jArr);
            }else{
                jsonobj.put(propertyName, fromJava(propertyValue));
            }
        }
        return jsonobj;
    }


    /*
     * 
     * 
     * 
     * JSON TO JAVA
     * 
     * 
     * 
     * 
     */

    @Deprecated
    private static void parse(Map<String, JSONProperty> valueMap, JSONObject element, Object instance)
            throws IOException{
        for(String key : element.keySet()){
            JSONProperty attr = valueMap.get(key);
            if(attr == null)
                continue;
            Object value = element.get(key);
            if(attr.getDataType().isArray() && value instanceof JSONArray){
                Class<?> arrType = attr.getDataType();
                Object[] array = insertArray(attr.getDataType().getComponentType(), (JSONArray) value);
                attr.setValue(arrType.cast(array));
            }else if(value instanceof JSONObject){
                attr.setValue(fromJSONObject((JSONObject) instance, attr.getDataType()));
            }else{
                attr.setValue(insertElement(value, attr.getDataType()));
            }
        }
    }

    @Deprecated
    private static Object[] insertArray(Class<?> arrType, JSONArray value){
        int length = value.length();
        Object[] resArr = (Object[]) Array.newInstance(arrType, length);//new Object[length];

        for(int i = 0; i < length; i++)
            resArr[i] = insertElement(value.get(i), arrType);

        return resArr;
    }

    @Deprecated
    private static Object insertElement(Object object, Class<?> arrType){
        if(object instanceof JSONObject){
            return fromJSONObject((JSONObject) object, arrType);
        }else if(object instanceof JSONArray){
            return insertArray(arrType, (JSONArray) object);
        }
        return object;
    }

}
