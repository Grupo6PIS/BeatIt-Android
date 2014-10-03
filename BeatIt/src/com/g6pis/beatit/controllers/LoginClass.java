package com.g6pis.beatit.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class LoginClass extends AsyncTask<String, Void, String>{

	@Override
	protected String doInBackground(String... params) {
		//http://beatit-udelar.rhcloud.com/user/addOrUpdateUser/
		String userId = params[0];
		String fbId = params[1];
		String name = params[2] + " " + params[3];
		
		URL url;
		try {
			if(userId.isEmpty()){
				url = new URL("http://beatit-udelar.rhcloud.com/user/updateUser/");
				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);
				OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

				writer.write("facebookID="+fbId+"&name="+name+"&imageURL=");
				writer.flush();
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String response = reader.readLine();
				JSONObject json = new JSONObject(response);
				//TODO comprobar que error sea false
				JSONObject user = json.getJSONObject("user");
				userId = user.getString("_id");
				writer.close();
				reader.close();
			}else{
				url = new URL("http://beatit-udelar.rhcloud.com/user/login/");
				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);
				OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

				writer.write("userID="+userId);
				writer.flush();
				writer.close();
				
			}
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		} catch (JSONException e) {
		}
	    
		
		return userId;
	}

}
