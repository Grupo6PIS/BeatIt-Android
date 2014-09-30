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
		String fbId = params[0];
		String name = params[1] + " " + params[2];
		String userId = "";
		
		URL url;
		try {
			url = new URL("http://beatit-udelar.rhcloud.com/user/addOrUpdateUser/");
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
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		} catch (JSONException e) {
		}
	    
		
		return userId;
	}

}
