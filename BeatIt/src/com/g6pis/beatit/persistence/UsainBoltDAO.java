package com.g6pis.beatit.persistence;

import java.util.LinkedList;
import java.util.List;

import com.g6pis.beatit.challenges.UsainBolt;
import com.g6pis.beatit.entities.Challenge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UsainBoltDAO extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "BeatIt";

	public UsainBoltDAO(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_USAINBOLT_TABLE = "CREATE TABLE UsainBolt ( "
				+ "longitude INTEGER, " + "latitude INTEGER, "
				+ "speed FLOAT, " + "score REAL, " + "challengeId INTEGER, "
				+ "FOREIGN KEY(challengeId) REFERENCES Challenge(challengeId)"
				+ ");";

		// Emable foreign keys
		String ENABLE_FOREIGN_KEYS = "PRAGMA foreign_keys=ON";
		db.execSQL(ENABLE_FOREIGN_KEYS);

		db.execSQL(CREATE_USAINBOLT_TABLE);

	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS UsainBolt");

		// create fresh books table
		this.onCreate(db);
	}

	public void addUsainBolt(UsainBolt usainBolt) {

		Log.d("addUsainBolt", usainBolt.toString());

		// Get Reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// Create ContentValues to add key column/value
		ContentValues values = new ContentValues();
		values.put("challengeId", usainBolt.getChallengeId());
		values.put("longitude", usainBolt.getLongitude());
		values.put("latitude", usainBolt.getLatitude());
		values.put("speed", usainBolt.getSpeed());
		values.put("score", usainBolt.getScore());

		// Insert
		db.insert("UsainBolt", null, values);

		// Close
		db.close();

	}

	public Challenge getUsainBolt(int challengeId) {

		// Get Reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// String[] COLUMNS = {"challengeId","name","description", "duration",
		// "level"};
		String[] COLUMNS = { "challengeId", "longitude", "latitude", "speed",
				"score" };
		// 2. build query
		Cursor cursor = db.query("UsainBolt", // a. table
				COLUMNS, // b. column names
				" challengeId = ?", // c. selections
				new String[] { String.valueOf(challengeId) }, // d. selections
																// args
				null, // e. group by
				null, // f. having
				null, // g. order by
				null); // h. limit

		// 3. if we got results get the first one
		if (cursor != null)
			cursor.moveToFirst();

		// 4. build usianBolt object
		UsainBolt challenge = new UsainBolt();
		challenge.setChallengeId(Integer.parseInt(cursor.getString(0)));
		challenge.setLongitude(Integer.parseInt(cursor.getString(1)));
		challenge.setLatitude(Integer.parseInt(cursor.getString(2)));
		challenge.setSpeed(Float.parseFloat(cursor.getString(3)));
		challenge.setScore(Integer.parseInt(cursor.getString(4)));

		Log.d("getUsainBolt(" + challengeId + ")", challenge.toString());

		return challenge;

	}
	
	

	public List<UsainBolt> getAllUsainBolt() {

		List<UsainBolt> challenges = new LinkedList<UsainBolt>();

		// 1. build the query
		String query = "SELECT  * FROM " + "UsainBolt";

		// 2. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		// 3. if we got results get the first one
		if (cursor.moveToFirst()) {
			do {

				// 4. build challenge object
				UsainBolt challenge = new UsainBolt();
				challenge.setChallengeId(Integer.parseInt(cursor.getString(0)));
				challenge.setLongitude(Integer.parseInt(cursor.getString(1)));
				challenge.setLatitude(Integer.parseInt(cursor.getString(2)));
				challenge.setSpeed(Float.parseFloat(cursor.getString(3)));
				challenge.setScore(Integer.parseInt(cursor.getString(4)));

				challenges.add(challenge);

			} while (cursor.moveToNext());
		}
		Log.d("getAllUsainBolts()", challenges.toString());

		return challenges;
	}

	public int updateChallenge(UsainBolt challenge) {

		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		//values.put("challengeId", challenge.getChallengeId());
		values.put("longitude", challenge.getLongitude());
		values.put("latitude", challenge.getLatitude());
		values.put("speed", challenge.getSpeed());
		values.put("score", challenge.getScore());

		// 3. updating row
		int i = db.update("UsainBolt", // table
				values, // column/value
				"challengeId" + " = ?", // selections
				new String[] { String.valueOf(challenge.getChallengeId()) }); // selection
																// args

        // 4. close
        db.close();
 
        return i;
	}
	
	// Deleting challenge
    public void deleteUsainBolt(Challenge challenge) {
 
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. delete
        db.delete("UsainBolt",
                "challengeId"+" = ?",
                new String[] { String.valueOf(challenge.getChallengeId()) });
 
        // 3. close
        db.close();
 
        Log.d("deleteUsainBolt", challenge.toString());
 
    }
}
