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

public class LoginConnection extends AsyncTask<String, Void, JSONObject> {

	@Override
	protected JSONObject doInBackground(String... params) {
		String fbId = params[1];
		String name = params[2] + " " + params[3];
		String imageURL = params[4];

		URL url;
		JSONObject user = new JSONObject();
		try {
			url = new URL("http://beatit-udelar.rhcloud.com/user/updateUser/");
			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			OutputStreamWriter writer = new OutputStreamWriter(
					connection.getOutputStream());

			writer.write("userID=" + fbId + "&name=" + name + "&imageURL="
					+ imageURL);
			writer.flush();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String response = reader.readLine();
			user = new JSONObject(response);
			writer.close();
			reader.close();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		} catch (JSONException e) {
		}

		return user;
	}

}
