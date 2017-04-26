package com.Dictionary.dictionarye;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.NfcAdapter.CreateBeamUrisCallback;
import android.util.Log;

public class MySqliteOpenHelper extends SQLiteOpenHelper {
	private static final String ACTIVITY_TAG="LogDemoooo";
		//private Context Maincontext;
		private final static String dbname = "user.db";
	public MySqliteOpenHelper(Context context) {
		super(context,dbname, null, 1);
		// TODO Auto-generated constructor stub
	//	Maincontext=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub	
		
	db.execSQL("create table user_info(username varchar(20) primary key not null,"
			+ "password varchar(20) not null,"
			+ "Enotebook vachar(20) not null,"
			+ "Jnotebook vachar(20) not null)");
	Log.d(ACTIVITY_TAG, "table created successfully");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
