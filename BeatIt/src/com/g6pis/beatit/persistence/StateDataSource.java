package com.g6pis.beatit.persistence;


import java.util.HashMap;
import java.util.Map;

import com.g6pis.beatit.datatypes.DTDateTime;
import com.g6pis.beatit.datatypes.DTState;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class StateDataSource {

	// Database fields
	private SQLiteDatabase dataBase;
	private MySQLiteHelper dbHelper;

	private String[] allColumns = { MySQLiteHelper.TABLE_STATE_ID,
			MySQLiteHelper.TABLE_STATE_ROUND_ID,
			MySQLiteHelper.TABLE_STATE_MAXSCORE,
			MySQLiteHelper.TABLE_STATE_LASTSCORE,
			MySQLiteHelper.TABLE_STATE_CURRENTATTEMPT,
			MySQLiteHelper.TABLE_STATE_ISFINISHED, 
			MySQLiteHelper.TABLE_STATE_LASTFINISHDATETIME};

	public StateDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public SQLiteDatabase open() throws SQLException {
		dataBase = dbHelper.getWritableDatabase();
		return dataBase;
	}

	public void close() {
		dbHelper.close();
	}

	private DTState cursorToState(Cursor cursor) {

		DTState state = new DTState();
		
//		state.setChallengeDescription(challengeDescription);
//		state.setChallengeDuration(challengeDuration);
		state.setChallengeId(cursor.getString(0));
//		state.setChallengeLevel(challengeLevel);
//		state.setChallengeName(challengeName);
		state.setCurrentAttempt(cursor.getInt(4));
//		state.setDateTimeStart(dateTimeStart);
		
		state.setFinished(Boolean.getBoolean(cursor.getString(5)));
		state.setLastScore(cursor.getDouble(3));
		state.setMaxScore(cursor.getDouble(2));
		state.setRoundId(cursor.getString(1));
		
		DTDateTime lastFinishDateTime = null;
		if(!cursor.getString(6).isEmpty())
			lastFinishDateTime = new DTDateTime(cursor.getString(6));
		state.setLastFinishDateTime(lastFinishDateTime);
		
		
		
		
		return state;
	}

	public DTState addState(DTState state) {

		// Create ContentValues to add key column/value
		ContentValues values = new ContentValues();
		values.put("challengeId", state.getChallengeId()); // 0
		values.put("roundId", state.getRoundId()); // 1
		values.put("maxScore", state.getMaxScore()); // 2
		values.put("lastScore", state.getLastScore()); // 3
		values.put("currentAttempt", state.getCurrentAttempt()); // 4
		values.put("isFinished", state.isFinished()); // 5
		if(state.getLastFinishDateTime() != null) //6
			values.put("lastFinishDateTime", state.getLastFinishDateTime().toString());
		else
			values.put("lastFinishDateTime", "");

		// Insert
		long insertId = dataBase.insert(MySQLiteHelper.TABLE_STATE, null,
				values);
System.out.println("insertedId "  + insertId);
System.out.println("challengeId " +  state.getChallengeId());
		Cursor cursor = dataBase.query(MySQLiteHelper.TABLE_STATE, allColumns,
				MySQLiteHelper.TABLE_STATE_ID + " = " + state.getChallengeId(), null, null,
				null, null);
		cursor.moveToFirst();
		DTState newState = cursorToState(cursor);
		cursor.close();

		return newState;

	}

	public int updateState(DTState state) {

		// Create ContentValues to add key column/value
		ContentValues values = new ContentValues();
		
		values.put("challengeId", state.getChallengeId());
		values.put("roundId", state.getRoundId());
		values.put("maxScore", state.getMaxScore());
		values.put("lastScore", state.getLastScore());
		values.put("currentAttempt", state.getCurrentAttempt());
		values.put("isFinished", state.isFinished());
		values.put("lastFinishDateTime", state.getLastFinishDateTime().toString());

		
		return  dataBase.update(MySQLiteHelper.TABLE_STATE, values, MySQLiteHelper.TABLE_STATE_ID + "=" + state.getChallengeId(),new String[] { String.valueOf(state.getChallengeId()) });
		
	}
	
	
	// Deleting state
	public void deleteState(DTState state) {

		String id = state.getChallengeId();

		dataBase.delete(MySQLiteHelper.TABLE_STATE,
				MySQLiteHelper.TABLE_STATE_ID + " = " + id, null);

	}

	public Map<String,DTState>  getAllStates() {

		Map<String,DTState> states = new HashMap<String,DTState>();

		Cursor cursor = dataBase.query(MySQLiteHelper.TABLE_STATE, allColumns,
				null, null, null, null, null);

		// 3. if we got results get the first one
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {

			// 4. build challenge object
			DTState state = cursorToState(cursor);

			states.put(state.getChallengeId(),state);

			cursor.moveToNext();

		}

		cursor.close();
		return states;
	}
	
	public void addStates(Map<String,DTState> states){
		for (Map.Entry<String, DTState> entry : states.entrySet()) {
			this.addState(entry.getValue());
		}
	}

	public SQLiteDatabase getDataBase() {
		return dataBase;
	}

	public void setDataBase(SQLiteDatabase dataBase) {
		this.dataBase = dataBase;
	}

	public MySQLiteHelper getDbHelper() {
		return dbHelper;
	}

	public void setDbHelper(MySQLiteHelper dbHelper) {
		this.dbHelper = dbHelper;
	}
	
	

}
