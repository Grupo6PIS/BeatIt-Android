package com.g6pis.beatit.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class RoundClass extends AsyncTask<Void, Void, String>{
	private final String USER_AGENT = "Mozilla/5.0";
	
	@Override
	protected String doInBackground(Void...params) {
		try {
			URL obj = new URL("http://beatit-udelar.rhcloud.com/round/getRound/");
			HttpURLConnection con;
			con = (HttpURLConnection) obj.openConnection();
			
			// optional default is GET
			con.setRequestMethod("GET");
						
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			JSONObject json = new JSONObject(in.readLine());
			boolean error = json.getBoolean("error");
			if(!json.getBoolean("error")){
				//TODO get weekNumber, start_date, end_date, ranking, challengeList
			}
			in.close();

		} catch (IOException e) {
		} catch (JSONException e) {
		}
		return " ";
	}

	

}
