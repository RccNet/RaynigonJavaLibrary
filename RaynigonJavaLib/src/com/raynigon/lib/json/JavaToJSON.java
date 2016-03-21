package com.raynigon.lib.json;

import java.lang.reflect.Field;

/**Generated on 28.11.2015 by Simon Schneider in Project <b>RaynigonJavaLib</b><p>
 * @author Simon Schneider
 */
public class JavaToJSON {

	public JSONObject fromJava(Object obj) throws JSONException, IllegalArgumentException, IllegalAccessException{
		JSONObject jsonobj = new JSONObject();
		Class<?> clazz = obj.getClass();
		for(Field f : clazz.getFields()){
			boolean accessFlag = f.isAccessible();
			f.setAccessible(true);
			if(!isAffected(f)){
				String fieldName = getFieldName(f);
				if(f.getType().isPrimitive()){
					jsonobj.append(fieldName, f.get(obj));
				}else if(f.getType()==String.class || 
						Number.class.isAssignableFrom(f.getType())){
					jsonobj.append(fieldName, f.get(obj));
				}else if(f.getType().isArray()){
					jsonobj.append(f.getName(), fromJava(f.get(obj), f.getType().getComponentType()));
				}else{
					jsonobj.append(fieldName, fromJava(f.get(obj)));
				}
			}
			f.setAccessible(accessFlag);
		}
		return jsonobj;
	}
	
	public JSONArray fromJava(Object obj, Class<?> arrayType){
		JSONArray array = new JSONArray();
		throw new IllegalArgumentException("Not Implemented yet");
		//return array;
	}
	
	private String getFieldName(Field f) {
		JsonFieldAttribute attr = f.getAnnotation(JsonFieldAttribute.class);
		if(attr==null || attr.Name().equalsIgnoreCase(""))
			return f.getName();
		
		return attr.Name();
	}

	private boolean isAffected(Field f) {
		JsonFieldAttribute attr = f.getAnnotation(JsonFieldAttribute.class);
		if(attr==null)
			return true;
		
		return attr.Affect();
	}
}
