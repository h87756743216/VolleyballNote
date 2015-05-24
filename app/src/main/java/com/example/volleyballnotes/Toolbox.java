package com.example.volleyballnotes;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class Toolbox {

	public static String[] JSONConvertStringArray(String json,String[] column) {
		try {
			JSONArray jsonArray = new JSONArray(json);
			JSONObject jsonData = null;
			//parse the jsonArray to get each jsonObject
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonData = jsonArray.getJSONObject(i);
			}
			//convert the jsonObject to string
			for ( int x=0;x<column.length;x++ ){
				column[x]=jsonData.getString(column[x]);
			}
			for ( int x=0;x<column.length;x++ ) LogCenter("JSONConvert","JSConvertValue", column[x]);
		} catch (Exception e) {
			Log.e("JSONConvertStringArray", e.toString());
		}
		return column;
	}
	public static void LogCenter(String group,String tag,String msg) {
		//Sign in
//		if ( group == "btn_sign_in") Log.i(tag, msg);
		
//		if ( group == "JSONConvert" ) Log.i(tag, msg); 
		
//		if ( group=="btn_register") Log.i(tag, msg);
	
//		if ( group=="DBConnector") Log.i(tag, msg);

//		if ( group=="main") Log.i(tag, msg);
		
//		if ( group=="btn_yes") Log.i(tag, msg);
		
//		if ( group=="basic" ) Log.i(tag, msg);
		
		if ( group=="from_to" ) Log.i(tag, msg);
		
//		if ( group=="DBConnector" ) Log.i(tag, msg);
		
		if ( group=="Specialty" ) Log.i(tag, msg);
		
	}
	//Toolbox.LogCenter("basic","","");
	public static void LogCenter(boolean from,String group,String tag,String msg) {
		if ( group == "btn_sign_in"){
			Log.i(tag, msg);
		}
	}

}