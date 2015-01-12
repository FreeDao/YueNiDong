package com.yuenidong.util;

import org.json.JSONException;
import org.json.JSONObject;

public class Commvert {
	JSONObject obj;

	public Commvert(JSONObject obj) {
		this.obj = obj;
	}

	//value值为String类型
	public String getString (String key){

		try {
			if(obj.getString(key)==null){
				return "";	
			}else{
				return obj.getString(key);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}

	//value值为Integer类型	
	public Integer getInt (String key){

		try {
			if(obj.getString(key)==null){
				return 0;	
			}else{
				return obj.getInt(key);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return 0;
		}


	}
	//value值为Long类型
	public Long getLong (String key){

		try {
			if(obj.getString(key)==null){
				return (long) 0;	
			}else{
				return obj.getLong(key);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return (long) 0;
		}

	}
	//value值为Double类型
	public Double getDouble (String key){

		try {
			if(obj.getString(key)==null){
				return  0.0;	
			}else{
				return obj.getDouble(key);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return 0.0;
		}

	}
	//value值为Boolean类型
	public Boolean getBoolean (String key){

		try {
			if(obj.getString(key)==null){
				return  false;	
			}else{
				return obj.getBoolean(key);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

	}



}
