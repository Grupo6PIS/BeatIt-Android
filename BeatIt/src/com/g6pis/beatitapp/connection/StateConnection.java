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

public class StateConnection extends AsyncTask<String, Void, JSONObject> {
	
	@Override
	protected JSONObject doInBackground(String... params) {

		String userId = params[0];
		Integer roundId = Integer.parseInt(params[1]);
		Integer challengeId = Integer.parseInt(params[2]);
		Integer attempts = Integer.parseInt(params[3]);
		Boolean finished = Boolean.getBoolean(params[4]);
		Double bestScore = Double.parseDouble(params[5]);
		Double lastScore = Double.parseDouble(params[6]);
		


		URL url;
		JSONObject json = new JSONObject();
		try {
				url = new URL("http://beatit-udelar.rhcloud.com/user/sendState/"); 
				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);
				OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

				writer.write("userId="+userId+"&roundId="+roundId+"&challengeId="+challengeId+"&attemps="+attempts+"&finished"+finished+
							"&stat_date=&bestScore="+bestScore+"&lastScore="+lastScore);
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
