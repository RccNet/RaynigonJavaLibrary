package com.raynigon.lib.json;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;

/**Generated on 28.11.2015 by Simon Schneider in Project <b>RaynigonJavaLib</b><p>
 * @author Simon Schneider
 */
public class JavaJSONConverter {

	/*
	 *
	 * 
	 * 				JAVA TO JSON
	 * 
	 * 
	 * 
	 */
	
	public static JSONArray fromJava(Object obj, Class<?> arrayType) throws IOException{
		JSONArray array = new JSONArray();
		Object[] arr = (Object[]) obj;
		for(Object element : arr){
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
				if(f.getType().isPrimitive() || f.getType()==String.class || 
						Number.class.isAssignableFrom(f.getType())){
					jsonobj.put(fieldName, getValue(f, obj));
				}else if(f.getType().isArray()){
					jsonobj.put(f.getName(), fromJava(getValue(f, obj), f.getType().getComponentType()));
				}else{
					jsonobj.put(fieldName, fromJava(getValue(f, obj)));
				}
			}
			f.setAccessible(accessFlag);
		}
		
		for(Method m : clazz.getMethods()){
			JsonMethodAttribute jma = m.getAnnotation(JsonMethodAttribute.class);
			if(jma==null)
				continue;
			Class<?> type = m.getReturnType();
			Object value = getValue(m, obj);
			if(type.isPrimitive() || type==String.class || Number.class.isAssignableFrom(type)){
				jsonobj.put(jma.Name(), value);
			}else if(type.isArray()){
				jsonobj.put(jma.Name(), fromJava(getValue(m, obj), type.getComponentType()));
			}else{
				jsonobj.put(jma.Name(), fromJava(value));
			}
		}
		
		return jsonobj;
	}
	
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

	private static Object getValue(Field f, Object o) throws IOException{
		try{
			return f.get(o);
		} catch(IllegalArgumentException e){
			throw new IOException(e);
		} catch(IllegalAccessException e){
			throw new IOException(e);
		}
	}
	
	private static String getFieldName(Field f) {
		JsonFieldAttribute attr = f.getAnnotation(JsonFieldAttribute.class);
		if(attr==null || attr.Name().equalsIgnoreCase(""))
			return f.getName();
		
		return attr.Name();
	}

	private static boolean isAffected(Field f) {
		JsonFieldAttribute attr = f.getAnnotation(JsonFieldAttribute.class);
		if(attr==null)
			return true;
		
		return attr.Affect();
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
	
	public static Object[] fromJson(JSONArray element, Class<?> inClazz) throws IOException{
		return insertArray(inClazz, element);
	}
	
	public static Object fromJson(JSONObject element, Class<?> inClazz) throws IOException{
		Object obj;
		try {
			obj = inClazz.newInstance();
		} catch (InstantiationException e) {
			throw new IOException(e);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
		
		for(Field field : inClazz.getDeclaredFields()){
			if(!Modifier.isStatic(field.getModifiers()))
				parse(field, element, obj);
		}
		
		return obj;
	}

	private static void parse(Field field, JSONObject element, Object obj) throws IOException {
		for(String key : element.keySet()){
			if(field.getName().equals(key)){
				setValue(field, element.get(key), obj);
			}
		}
	}

	private static void setValue(Field field, Object value, Object obj) throws IOException{
		boolean accessFlag = field.isAccessible();
		try{
			field.setAccessible(true);
			if(field.getType().isArray() && value instanceof JSONArray){			
				field.set(obj, insertArray(getArrayType(field), (JSONArray) value));
			}else if(value instanceof JSONObject){
				field.set(obj, fromJson((JSONObject) value, field.getType()));
			}else{
				field.set(obj, insertElement(value, field.getType()));
			}
		}catch(IllegalArgumentException e){
			throw new IOException(e);
		}catch(IllegalAccessException e){
			throw new IOException(e);
		}finally {
			field.setAccessible(accessFlag);
		}
	}

	private static Object[] insertArray(Class<?> arrType, JSONArray value) throws IOException {
		int length = value.length();
		Object[] resArr = new Object[length];
		
		for(int i=0;i<length;i++)
			resArr[i] = insertElement(value.get(i), arrType);
		
		return resArr;
	}

	private static Object insertElement(Object object, Class<?> arrType) throws IOException {
		if(object instanceof JSONObject){
			return fromJson((JSONObject) object, arrType);
		}else if(object instanceof JSONArray){			
			throw new IOException("Unable to create an multi dimensional Array");
		}
		return object;
	}
	
	private static Class<?> getArrayType(Field field) {
		return field.getType().getComponentType();
	}
}
