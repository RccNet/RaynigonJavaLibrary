package com.raynigon.lib.json;

import java.io.IOException;
import java.lang.reflect.Field;

public class JSONtoJava {

	public static Object createFromJson(JSONArray element, Class<?> inClazz) throws Exception{
		return insertArray(inClazz, element);
	}
	
	public static Object createFromJson(JSONObject element, Class<?> inClazz) throws Exception{
		Object obj = inClazz.newInstance();
		
		for(Field field : inClazz.getFields()){
			parse(field, element, obj);
		}
		
		return obj;
	}

	private static void parse(Field field, JSONObject element, Object obj) throws Exception {
		for(String key : element.keySet()){
			if(field.getName().equals(key)){
				setValue(field, element.get(key), obj);
			}
		}
	}

	private static void setValue(Field field, Object value, Object obj) throws Exception{
		if(field.getType().isArray() && value instanceof JSONArray){			
			field.set(obj, insertArray(getArrayType(field), (JSONArray) value));
		}else if(value instanceof JSONObject){
			field.set(obj, createFromJson((JSONObject) value, field.getType()));
		}else{
			field.set(obj, insertElement(value, field.getType()));
		}
		
	}

	private static Object insertArray(Class<?> arrType, JSONArray value) throws Exception {
		int length = value.length();
		Object[] resArr = new Object[length];
		
		for(int i=0;i<length;i++)
			resArr[i] = insertElement(value.get(i), arrType);
		
		return resArr;
	}

	private static Object insertElement(Object object, Class<?> arrType) throws Exception {
		if(object instanceof JSONObject){
			return createFromJson((JSONObject) object, arrType);
		}else if(object instanceof JSONArray){			
			throw new IOException("Unable to create an multi dimensional Array");
		}
		return object;
	}
	
	private static Class<?> getArrayType(Field field) {
		return field.getType().getComponentType();
	}
}
