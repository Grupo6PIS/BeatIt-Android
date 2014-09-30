package com.g6pis.beatit.persistence;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.g6pis.beatit.challenges.usainbolt.UsainBolt;
import com.g6pis.beatit.datatypes.DTState;
import com.g6pis.beatit.entities.Challenge;

public class StateDAO extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "BeatIt";

	public StateDAO(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_USAINBOLT_TABLE = "CREATE TABLE State ( "
				+ "challengeId STRING, " + "roundId STRING, "
				+ "score DOUBLE, " + "currentAttempt INTEGER, "
				+ "isFinished BOOLEAN "
				+ "FOREIGN KEY(challengeId) REFERENCES Challenge(challengeId)"
				+ ");";

		// Emable foreign keys
		String ENABLE_FOREIGN_KEYS = "PRAGMA foreign_keys=ON";
		db.execSQL(ENABLE_FOREIGN_KEYS);

		db.execSQL(CREATE_USAINBOLT_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS State");

		// create fresh books table
		this.onCreate(db);
	}

	public void addState(DTState state) {

		// Get Reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// Create ContentValues to add key column/value
		ContentValues values = new ContentValues();
		values.put("challengeId", state.getChallengeId());
		values.put("roundId", state.getRoundId());
		values.put("score", state.getScore());
		values.put("currentAttempt", state.getCurrentAttempt());
		values.put("isFinished", state.isFinished());

		// Insert
		db.insert("State", null, values);

		// Close
		db.close();

	}

	public DTState getState(int challengeId) {

		// Get Reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// String[] COLUMNS = {"challengeId","name","description", "duration",
		// "level"};
		String[] COLUMNS = { "challengeId", "roundId", "score",
				"currentAttempt", "isFinished" };
		// 2. build query
		Cursor cursor = db.query("State", // a. table
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
		DTState state = new DTState(cursor.getString(0), cursor.getString(1),
				Double.parseDouble(cursor.getString(2)),
				Integer.parseInt(cursor.getString(3)),
				Boolean.getBoolean(cursor.getString(4)));

		return state;

	}

	public List<DTState> getAllStates() {

		List<DTState> states = new LinkedList<DTState>();

		// 1. build the query
		String query = "SELECT  * FROM " + "UsainBolt";

		// 2. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		// 3. if we got results get the first one
		if (cursor.moveToFirst()) {
			do {

				// 4. build challenge object
				DTState state = new DTState(cursor.getString(0), cursor.getString(1),
						Double.parseDouble(cursor.getString(2)),
						Integer.parseInt(cursor.getString(3)),
						Boolean.getBoolean(cursor.getString(4)));

				states.add(state);

			} while (cursor.moveToNext());
		}
		

		return states;
	}

	public int updateState(DTState state) {

		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put("challengeId", state.getChallengeId());
		values.put("roundId", state.getRoundId());
		values.put("score", state.getScore());
		values.put("currentAttempt", state.getCurrentAttempt());
		values.put("isFinished", state.isFinished());

		// 3. updating row
		int i = db.update("State", // table
				values, // column/value
				"challengeId" + " = ?", // selections
				new String[] { String.valueOf(state.getChallengeId()) }); // selection
		// args

		// 4. close
		db.close();

		return i;
	}

	// Deleting state
	public void deleteState(DTState state) {

		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. delete
		db.delete("UsainBolt", "challengeId" + " = ?",
				new String[] { String.valueOf(state.getChallengeId()) });

		// 3. close
		db.close();

	}
}
