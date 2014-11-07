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

public class AllStatesConnection extends AsyncTask<String, Void, JSONObject> {
	
	@Override
	protected JSONObject doInBackground(String... params) {

		String data = params[0];
		
		


		URL url;
		JSONObject json = new JSONObject();
		try {
				url = new URL("http://beatit-udelar.rhcloud.com/user/sendAllStates/"); 
				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);
				OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

				writer.write("data="+data);
				writer.flush();
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String response = reader.readLine();
				json = new JSONObject(response);
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		} catch (JSONException e) {
		}
		return json;
	}
}