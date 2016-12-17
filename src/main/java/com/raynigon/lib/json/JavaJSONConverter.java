package com.raynigon.lib.json;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

/**Generated on 28.11.2015 by Simon Schneider in Project <b>RaynigonJavaLib</b><p>
 * @author Simon Schneider
 */
public class JavaJSONConverter {

	
	private static JSONObject compMapperFJO(Object inO){
		try {
			return fromJava(inO);
		} catch (IOException e) {
			throw new JSONParseException("compatibility Exception",e);
		}
	}
	
	private static <T> JSONArray compMapperFJA(T[] inA){
		try {
			return fromJava(inA, inA.getClass().getComponentType());
		} catch (IOException e) {
			throw new JSONParseException("compatibility Exception",e);
		}
	}
	
    private static <T> JSONArray compMapperFJC(Collection<T> inColl){
        JSONArray result = new JSONArray();
        if(inColl==null)
            return null;
        try {
            for(T object : inColl){
                result.put(fromJava(object));
            }
        } catch (IOException e) {
            throw new JSONParseException("compatibility Exception",e);
        }
        return result;
    }	
	
	private static Object compMapperFJO(JSONObject inO, Class<?> clazz){
		try {
			return fromJson(inO, clazz);
		} catch (IOException e) {
			throw new JSONParseException("compatibility Exception",e);
		}
	}
	
	private static List<?> compMapperFJA(JSONArray inO, Class<?> clazz){
		try {
			return Arrays.asList(fromJson(inO, clazz));
		} catch (IOException e) {
			throw new JSONParseException("compatibility Exception",e);
		}
	}
	
	
	@Future(Version="0.0.5")
	public static JSONObject 	fromJavaObject(Object inObject){return compMapperFJO(inObject);}
	@Future(Version="0.0.5")
	public static <T> JSONArray fromJavaArray(T[] inArray){return compMapperFJA(inArray);}
    @Future(Version="0.0.5")
    public static <T> JSONArray fromJavaCollection(Collection<T> inList){return compMapperFJC(inList);}	
	
	@SuppressWarnings("unchecked")
    @Future(Version="0.0.5")
	public static <T> T		  fromJSONObject(JSONObject inObject, Class<T> inClazz){return (T) compMapperFJO(inObject, inClazz);}
	
	@SuppressWarnings("unchecked")
    @Future(Version="0.0.5")
	public static <T> List<T> fromJSONArray(JSONArray inArray, Class<T> inClazz){return (List<T>) compMapperFJA(inArray, inClazz);}
	
	protected static String getNameFromField(Field inField){
	    JSONAttribute jsonAttr = inField.getAnnotation(JSONAttribute.class);
	    if(jsonAttr==null)
	        return inField.getName();
	    if(!jsonAttr.Affect())
	        return null;
	    if(jsonAttr.Name().isEmpty())
	        return inField.getName();
	    return jsonAttr.Name();
	}
	
	protected static String getNameFromMethod(Method inMethod){
	    JSONMethod jsonMethod = inMethod.getAnnotation(JSONMethod.class);
	    if(jsonMethod==null)
	        return null;
	    return jsonMethod.Name();
	}
	
	protected static boolean isAffected(Field inField){
		JSONAttribute ja = inField.getAnnotation(JSONAttribute.class);
		return ja==null ? true : ja.Affect();
	}
	
	protected static boolean isAffected(Method inMethod){
		return inMethod.isAnnotationPresent(JSONMethod.class);
	}
	
	protected static void addFieldsToValueMap(Field[] fields, Object obj, Map<String, AttributeValue> map){
		for(Field field : fields){
			if(Modifier.isStatic(field.getModifiers()))
				continue;
			if(!isAffected(field))
				continue;
			String name = getNameFromField(field);
			map.put(name, new AttributeValue(name, obj, field));
		}
	}
	
	protected static void addMethodsToValueMap(Method[] methods, Object obj, Map<String, AttributeValue> map){
		for(Method method : methods){
			if(Modifier.isStatic(method.getModifiers()))
				continue;
			if(!isAffected(method))
				continue;
			String name = getNameFromMethod(method);
			if(!method.getReturnType().equals(Void.TYPE) && method.getParameterTypes().length==0)
				addMethodToValueMap(map, name, obj, method);
			else if(method.getReturnType().equals(Void.TYPE) && method.getParameterTypes().length==1)
				addMethodToValueMap(map, name, obj, method);	
		}
	}
	
	protected static void addMethodToValueMap(Map<String, AttributeValue> inMap, String name, Object obj, Method method) {
		if(inMap.containsKey(name))
			inMap.get(name).addMethod(method);
		else
			inMap.put(name, new AttributeValue(name, obj, method));
	}

	protected static Map<String, AttributeValue> findAttributeValues(Object obj){
		Class<?> clazz = obj.getClass();
		Map<String, AttributeValue> attrValues = new HashMap<>();

		addFieldsToValueMap(clazz.getDeclaredFields(), obj, attrValues);
		addMethodsToValueMap(clazz.getDeclaredMethods(), obj, attrValues);
		return attrValues;
	}
	
	protected static class AttributeValue{
		public static final int TYPE_FIELD = 0x100;
		public static final int TYPE_METHOD = 0x200;
		
		private int type = 0;
		private String name;
		private Method get_method;
		private Method set_method;
		private Field field;
		private Object obj;
		
		public AttributeValue(String inName, Object inObj, Field inField) {
			type 	= TYPE_FIELD;
			name 	= inName;
			obj 	= inObj;
			field 	= inField;
		}
		
		public AttributeValue(String inName, Object inObj, Method inMethod) {
			type 	= TYPE_METHOD;
			name 	= inName;
			obj 	= inObj;
			addMethod(inMethod);
		}

		public void addMethod(Method inMethod){
		    if(inMethod.getParameterCount()==0)
                get_method  = inMethod;
            else if(inMethod.getParameterCount()==1)
                set_method = inMethod;
            else
                throw new IllegalArgumentException("Setter methods must have only one Parameter");
		}
		
		public int getType() {
			return type;
		}
		
		public Object getValue(){
			try{
				if(type==TYPE_FIELD){
					boolean access = field.isAccessible();
					Object value;
					field.setAccessible(true);
					value = field.get(obj);
					field.setAccessible(access);
					return value;
				}else if(type==TYPE_METHOD && get_method!=null){
					boolean access = get_method.isAccessible();
					Object value;
					get_method.setAccessible(true);
					value = get_method.invoke(obj);
					get_method.setAccessible(access);
					return value;
				}
			}catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException e){
				throw new JSONParseException("Error during pulling value from "+name, e);
			}
			return null;
		}
		
		public void setValue(Object value){
			try{
				if(type==TYPE_FIELD){
					boolean access = field.isAccessible();
					field.setAccessible(true);
					field.set(obj, value);
					field.setAccessible(access);
				}else if(type==TYPE_METHOD && set_method!=null){
					boolean access = set_method.isAccessible();
					set_method.setAccessible(true);
					set_method.invoke(obj, value);
					set_method.setAccessible(access);
				}
			}catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException e){
				throw new JSONParseException("Error during pushing value into "+name, e);
			}
		}

        public Class<?> getDataType(){
            if(type==TYPE_FIELD){
                return field.getType();
            }else if(type==TYPE_METHOD && set_method!=null){
                return set_method.getParameterTypes()[0];
            }else if(type==TYPE_METHOD && get_method!=null){
                return get_method.getReturnType();
            }
            return null;//TODO throw exception or so
        }
	}
	
	
	
	
	/*#####################################################################
	 *#####################################################################
	 *#####################################################################
	 * 				JAVA TO JSON   -   DEPRECATED
	 *##################################################################### 
	 *##################################################################### 
	 *##################################################################### 
	 */
	
	@Deprecated
	public static JSONArray fromJava(Object obj, Class<?> arrayType) throws IOException{
		JSONArray array = new JSONArray();
		Object[] arr = (Object[]) obj;
		for(Object element : arr){
		    if(element==null)
		        continue;
			Class<?> type = element.getClass();
			if(type.isArray())
				array.put(fromJava(element, type.getComponentType()));
			else if(type.isPrimitive() || type==String.class || 
					Number.class.isAssignableFrom(type))
				array.put(element);
			else
				array.put(fromJava(element));
		}
		return array;
	}
	
	
    /*@SuppressWarnings("unchecked")
    public static <T> void AssignValue(JSONObject jobj, String key, T value, ParameterizedType genericType){
	    Class<T> typeClass = (Class<T>) value.getClass();
	    if(typeClass.isPrimitive()){
	        jobj.put(key, value);
	    }else if(Number.class.isAssignableFrom(typeClass)){
	        jobj.put(key, ((Number) value).longValue());
	    }else if(typeClass==JSONObject.class || typeClass==JSONArray.class){
	        jobj.put(key, value);
	    }else if(typeClass.isArray()){
	        
	    }else if(Collection.class.isAssignableFrom(typeClass)){
	        Class<?> collectionType = (Class<?>) genericType.getActualTypeArguments()[0];
	        jobj.put(key, createArray(value, genericType));
	    }
	    
	}
	
	private static <T> JSONArray createArray(Collection<T> value, ParameterizedType genericType){
        // TODO Auto-generated method stub
        return null;
    }*/

    @Deprecated
	public static JSONObject fromJava(Object obj) throws IOException{
		if(obj==null)
			return null;
		JSONObject jsonobj = new JSONObject();
		Class<?> clazz = obj.getClass();
		
		for(Field f : clazz.getDeclaredFields()){
			if(Modifier.isStatic(f.getModifiers()))
				continue;
			
			boolean accessFlag = f.isAccessible();
			f.setAccessible(true);
			if(isAffected(f)){
				String fieldName = getFieldName(f);
				if(f.getType().isPrimitive() || f.getType()==String.class){
					jsonobj.put(fieldName, getValue(f, obj));
				}else if(Number.class.isAssignableFrom(f.getType())){
				    jsonobj.put(fieldName, ((Number) getValue(f, obj)).longValue());
				}else if(f.getType().isArray()){
					jsonobj.put(f.getName(), fromJava(getValue(f, obj), f.getType().getComponentType()));
				}else if(f.getType()==JSONObject.class || f.getType()==JSONArray.class){
				    jsonobj.put(fieldName, getValue(f, obj));
				}else if(Collection.class.isAssignableFrom(f.getType())){
				    JSONArray jArr = new JSONArray();
				    Collection<?> coll = (Collection<?>) getValue(f, obj);
				    for(Object item : coll){
				        Class<?> type = item.getClass();
		                if(type==String.class){
		                    jArr.put(item);
		                }else if(Number.class.isAssignableFrom(type)){
		                    jArr.put(((Number) item).longValue());
		                }else if(type.isArray()){
		                    jArr.put(fromJava(item, type.getComponentType()));
		                }else if(type==JSONObject.class || type==JSONArray.class){
		                    jArr.put(item);
		                }else{
		                    jArr.put(fromJava(item));
		                }
				    }
                    jsonobj.put(fieldName, jArr);
                }else{
					jsonobj.put(fieldName, fromJava(getValue(f, obj)));
				}
			}
			f.setAccessible(accessFlag);
		}
		
		for(Method m : clazz.getMethods()){
			JSONMethod jma = m.getAnnotation(JSONMethod.class);
			if(jma==null)
				continue;
			Class<?> type = m.getReturnType();
			Object value = getValue(m, obj);
			if(type.isPrimitive() || type==String.class || Number.class.isAssignableFrom(type)){
				jsonobj.put(jma.Name(), value);
			}else if(type.isArray()){
				jsonobj.put(jma.Name(), fromJava(getValue(m, obj), type.getComponentType()));
			}else if(type==JSONObject.class || type==JSONArray.class){
                jsonobj.put(jma.Name(), value);
            }else{
				jsonobj.put(jma.Name(), fromJava(value));
			}
		}
		
		return jsonobj;
	}
	
	@Deprecated
	private static Object getValue(Method m, Object obj) throws IOException {
		try {
			return m.invoke(obj);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		} catch (InvocationTargetException e) {
			throw new IOException(e);
		}
	}

	@Deprecated
	private static Object getValue(Field f, Object o) throws IOException{
		try{
			return f.get(o);
		} catch(IllegalArgumentException e){
			throw new IOException(e);
		} catch(IllegalAccessException e){
			throw new IOException(e);
		}
	}
	
	@Deprecated
	private static String getFieldName(Field f) {
		JSONAttribute attr = f.getAnnotation(JSONAttribute.class);
		if(attr==null || attr.Name().equalsIgnoreCase(""))
			return f.getName();
		
		return attr.Name();
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
	public static Object[] fromJson(JSONArray element, Class<?> inClazz) throws IOException{
		return insertArray(inClazz, element);
	}
	
	@Deprecated
	public static Object fromJson(JSONObject element, Class<?> inClazz) throws IOException{
		Object obj;
		try {
			obj = inClazz.newInstance();
		} catch (InstantiationException e) {
			throw new IOException(e);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
		
		Map<String, AttributeValue> valueMap = new HashMap<>();
		addFieldsToValueMap(inClazz.getDeclaredFields(), obj, valueMap);
		addMethodsToValueMap(inClazz.getDeclaredMethods(), obj, valueMap);
		
		parse(valueMap, element, obj);
		
		return obj;
	}

	@Deprecated
	private static void parse(Map<String, AttributeValue> valueMap, JSONObject element, Object obj) throws IOException {
		for(String key : element.keySet()){
			AttributeValue value = valueMap.get(key);
			if(value==null)
				continue;
			setValue(value, element.get(key));
		}
	}	

	@Deprecated
	private static void setValue(AttributeValue attr, Object value) throws IOException{
		if(attr.getDataType().isArray() && value instanceof JSONArray){		
		    Class<?> arrType = attr.getDataType();
		    Object[] array = insertArray(attr.getDataType().getComponentType(), (JSONArray) value);
		    attr.setValue(arrType.cast(array));
		}else if(value instanceof JSONObject){
			attr.setValue(fromJson((JSONObject) value, attr.getDataType()));
		}else{
			attr.setValue(insertElement(value, attr.getDataType()));
		}
	}

	@Deprecated
	private static Object[] insertArray(Class<?> arrType, JSONArray value) throws IOException {
		int length = value.length();
		Object[] resArr = (Object[]) Array.newInstance(arrType, length);//new Object[length];
		
		for(int i=0;i<length;i++)
			resArr[i] = insertElement(value.get(i), arrType);
		
		return resArr;
	}

	@Deprecated
	private static Object insertElement(Object object, Class<?> arrType) throws IOException {
		if(object instanceof JSONObject){
			return fromJson((JSONObject) object, arrType);
		}else if(object instanceof JSONArray){			
			throw new IOException("Unable to create an multi dimensional Array");
		}
		return object;
	}
	
}
