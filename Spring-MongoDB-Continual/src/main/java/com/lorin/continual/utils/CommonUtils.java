package com.lorin.continual.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class CommonUtils {
	
	public static JSONObject toJson(String jsonString) {
		JSONParser parser = new JSONParser();
		JSONObject json= null;
		try {
			json=(JSONObject) parser.parse(jsonString);
		}catch(Throwable t){
			throw new  RuntimeException(t);
			
		}
		return json;
	}
	
	

}
