package com.g6pis.beatit.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class RankingConnection extends AsyncTask<Void, Void, JSONObject> {
	@Override
	protected JSONObject doInBackground(Void...params) {
		JSONObject ranking = new JSONObject();
		try {
			URL obj = new URL("http://beatit-udelar.rhcloud.com/round/getRanking/");
			HttpURLConnection con;
			con = (HttpURLConnection) obj.openConnection();
			
			// optional default is GET
			con.setRequestMethod("GET");
						
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			JSONObject json = new JSONObject(in.readLine());
			if(!json.getBoolean("error")){
				ranking = json.getJSONObject("data");
			}
			in.close();

		} catch (IOException e) {
		} catch (JSONException e) {
		}
		return ranking;
	}
}
