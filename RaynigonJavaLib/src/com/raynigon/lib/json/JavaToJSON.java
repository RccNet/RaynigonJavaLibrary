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
			if(!f.isAccessible())
				continue;
			if(f.getType().isPrimitive()){
				jsonobj.append(f.getName(), f.get(obj));
			}else if(f.getType()==String.class || 
					Number.class.isAssignableFrom(f.getType())){
				jsonobj.append(f.getName(), f.get(obj));
			}else if(f.getType().isArray()){
				//TODO here make this available for array fields
				//jsonobj.append(f.getName(), fromJava());
			}else{
				jsonobj.append(f.getName(), fromJava(f.get(obj)));
			}
		}
		return jsonobj;
	}
	
	public JSONArray fromJava(Object[] obj){
		JSONArray array = new JSONArray();
		//TODO MISSING
		return array;
	}
}
