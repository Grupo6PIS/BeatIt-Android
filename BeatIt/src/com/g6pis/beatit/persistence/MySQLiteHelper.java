package com.g6pis.beatit.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MySQLiteHelper extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "BeatIt";
	
	public static final String TABLE_STATE = "State";
	public static final String TABLE_STATE_ID = "challengeId";
	public static final String TABLE_STATE_ROUND_ID = "roundId";
	public static final String TABLE_STATE_MAXSCORE = "maxScore";
	public static final String TABLE_STATE_LASTSCORE = "lastScore";
	public static final String TABLE_STATE_CURRENTATTEMPT = "currentAttempt";
	public static final String TABLE_STATE_ISFINISHED = "isFinished";
	public static final String TABLE_STATE_LASTFINISHDATETIME = "lastFinishDateTime";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String CREATE_USAINBOLT_TABLE = "CREATE TABLE" + TABLE_STATE +" ( "
				+ TABLE_STATE_ID + " STRING PRIMARY KEY, " + TABLE_STATE_ROUND_ID +" STRING, "
				+ TABLE_STATE_MAXSCORE +" DOUBLE, " + TABLE_STATE_LASTSCORE +" DOUBLE, " + TABLE_STATE_CURRENTATTEMPT +" INTEGER, "
				+  TABLE_STATE_ISFINISHED +" BOOLEAN, " + TABLE_STATE_LASTFINISHDATETIME + " STRING "
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

}
