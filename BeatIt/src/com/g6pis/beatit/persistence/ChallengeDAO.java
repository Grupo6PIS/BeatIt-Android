//package com.g6pis.beatit.persistence;
//
//import java.util.LinkedList;
//import java.util.List;
//
//import com.g6pis.beatit.entities.Challenge;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
//
//public class ChallengeDAO extends SQLiteOpenHelper {
//
//	// Database Version
//	private static final int DATABASE_VERSION = 1;
//	// Database Name
//	private static final String DATABASE_NAME = "BeatIt";
//
//	public ChallengeDAO(Context context) {
//		super(context, DATABASE_NAME, null, DATABASE_VERSION);
//	}
//
//	@Override
//	public void onCreate(SQLiteDatabase db) {
//		// SQL statement to create book table
//		String CREATE_CHALLENGE_TABLE = "CREATE TABLE Challenge ( "
//				+ "challengeId INTEGER PRIMARY KEY AUTOINCREMENT, "
//				+ "name TEXT, " + "description TEXT, " + "duration INTEGER, "
//				+ "level INTEGER" + ")";
//
//		// create table
//		db.execSQL(CREATE_CHALLENGE_TABLE);
//
//	}
//
//	@Override
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		// Drop older table if existed
//		db.execSQL("DROP TABLE IF EXISTS Challenge");
//
//		// create fresh books table
//		this.onCreate(db);
//	}
//
//	public void addChallenge(Challenge challenge) {
//
//		Log.d("addChallenge", challenge.toString());
//
//		// Get Reference to writable DB
//		SQLiteDatabase db = this.getWritableDatabase();
//
//		// Create ContentValues to add key column/value
//		ContentValues values = new ContentValues();
//		values.put("challengeId", challenge.getChallengeId());
//		values.put("name", challenge.getName());
//		values.put("description", challenge.getDescription());
//		values.put("duration", challenge.getDuration());
//		values.put("level", challenge.getLevel());
//
//		// Insert
//		db.insert("Challenge", null, values);
//
//		// Close
//		db.close();
//	}
//
//	public Challenge getChallenge(int challengeId) {
//
//		// Get Reference to writable DB
//		SQLiteDatabase db = this.getWritableDatabase();
//
//		String[] COLUMNS = { "challengeId", "name", "description", "duration",
//				"level" };
//
//		Cursor cursor = db.query("Challenge", // a. table
//				COLUMNS, // b. column names
//				" challengeId = ?", // c. selections
//				new String[] { String.valueOf(challengeId) }, // d. selections
//																// args
//				null, // e. group by
//				null, // f. having
//				null, // g. order by
//				null); // h. limit
//
//		// 3. if we got results get the first one
//		if (cursor != null)
//			cursor.moveToFirst();
//
//		// 4. build challenge object
//		Challenge challenge = new Challenge();
//		challenge.setChallengeId(Integer.parseInt(cursor.getString(0)));
//		challenge.setName(cursor.getString(1));
//		challenge.setDescription(cursor.getString(2));
//		challenge.setDuration(Integer.parseInt(cursor.getString(3)));
//		challenge.setLevel(Integer.parseInt(cursor.getString(4)));
//
//		Log.d("getChallenge(" + challengeId + ")", challenge.toString());
//
//		return challenge;
//
//	}
//
//	public List<Challenge> getAllChallenges() {
//
//		List<Challenge> challenges = new LinkedList<Challenge>();
//
//		// 1. build the query
//		String query = "SELECT  * FROM " + "Challenge";
//
//		// 2. get reference to writable DB
//		SQLiteDatabase db = this.getWritableDatabase();
//		Cursor cursor = db.rawQuery(query, null);
//
//		// 3. if we got results get the first one
//		if (cursor.moveToFirst()) {
//			do {
//
//				// 4. build challenge object
//				Challenge challenge = new Challenge();
//				challenge.setChallengeId(Integer.parseInt(cursor.getString(0)));
//				challenge.setName(cursor.getString(1));
//				challenge.setDescription(cursor.getString(2));
//				challenge.setDuration(Integer.parseInt(cursor.getString(3)));
//				challenge.setLevel(Integer.parseInt(cursor.getString(4)));
//
//				challenges.add(challenge);
//
//			} while (cursor.moveToNext());
//		}
//		Log.d("getAllChallenges()", challenges.toString());
//
//		return challenges;
//	}
//
//	public int updateChallenge(Challenge challenge) {
//
//		// 1. get reference to writable DB
//		SQLiteDatabase db = this.getWritableDatabase();
//
//		// 2. create ContentValues to add key "column"/value
//		ContentValues values = new ContentValues();
//		// values.put("challengeId", challenge.getChallengeId());
//		values.put("name", challenge.getName());
//		values.put("description", challenge.getDescription());
//		values.put("duration", challenge.getDuration());
//		values.put("level", challenge.getLevel());
//
//		// 3. updating row
//		int i = db.update("Challenge", // table
//				values, // column/value
//				"challengeId" + " = ?", // selections
//				new String[] { String.valueOf(challenge.getChallengeId()) }); // selection
//		// args
//
//		// 4. close
//		db.close();
//
//		return i;
//	}
//
//	// Deleting challenge
//	public void deleteChallenge(Challenge challenge) {
//
//		// 1. get reference to writable DB
//		SQLiteDatabase db = this.getWritableDatabase();
//
//		// 2. delete
//		db.delete("Challenge", "challengeId" + " = ?",
//				new String[] { String.valueOf(challenge.getChallengeId()) });
//
//		// 3. close
//		db.close();
//
//		Log.d("deleteChallenge", challenge.toString());
//
//	}
//
//}
