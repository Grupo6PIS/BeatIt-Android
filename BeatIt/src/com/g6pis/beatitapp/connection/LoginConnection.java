package com.g6pis.beatitapp.connection;

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

public class LoginConnection extends AsyncTask<String, Void, JSONObject>{

	@Override
	protected JSONObject doInBackground(String... params) {
		//http://beatit-udelar.rhcloud.com/user/addOrUpdateUser/
		String userId = params[0];
		String fbId = params[1];
		String name = params[2] + " " + params[3];
		String imageURL = params[4];
		
		URL url;
		JSONObject user = new JSONObject();
		try { 
				url = new URL("http://beatit-udelar.rhcloud.com/user/login/");
				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);
				OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

				writer.write("userID="+fbId);
				writer.flush();
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String response = reader.readLine();
				JSONObject json = new JSONObject(response);
				boolean error = json.getBoolean("error");
				writer.close();
				if(error){ 
					url = new URL("http://beatit-udelar.rhcloud.com/user/updateUser/");
					URLConnection connection = url.openConnection();
					connection.setDoOutput(true);
					writer = new OutputStreamWriter(connection.getOutputStream());

					writer.write("userID="+fbId+"&name="+name+"&imageURL="+imageURL);
					writer.flush();
					reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					response = reader.readLine();
					json = new JSONObject(response);
					//TODO comprobar que error sea false
					user = json.getJSONObject("user");
					writer.close();
					reader.close();
				}else{
					user = json.getJSONObject("user");
				}
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		} catch (JSONException e) {
		}
		
		return user;
	}

}
