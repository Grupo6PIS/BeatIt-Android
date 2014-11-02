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

public class ScoreConnection extends AsyncTask<String, Void, JSONObject>{

	@Override
	protected JSONObject doInBackground(String... params) {

		String userId = params[0];
		Double score = Double.parseDouble(params[1]);
		Integer roundId = Integer.parseInt(params[2]);

		URL url;
		JSONObject json = new JSONObject();
		try {
				url = new URL("http://beatit-udelar.rhcloud.com/round/sendScore/"); 
				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);
				OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

				writer.write("userID="+userId+"&score="+score+"&roundID="+roundId);
				writer.flush();
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String response = reader.readLine();
				json = new JSONObject(response);
		} catch (MalformedURLException e) {
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
		}
		return json;
	}


}
