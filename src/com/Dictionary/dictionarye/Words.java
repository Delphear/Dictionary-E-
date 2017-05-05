package com.Dictionary.dictionarye;

import java.util.HashMap;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Words {
	public Words() {
		super();
		// TODO Auto-generated constructor stub
	}
	private static SQLiteDatabase database= MainActivity.getDatabase();
	private static String [] edata;
	private static Map<String, String> data;
	private static int wordcount;
	private static Cursor cursor;
	public static int getWordcount() {
		return wordcount;
	}
	public static String[] getEdata() {
		return edata;
	}
	public static Map<String, String> getData() {
		return data;
	}
	public static void setWords(){
		if(!database.isOpen()){
			SQLiteDatabase.openOrCreateDatabase(database.getPath(), null);
		}
		String sql = "select english as _id,chinese from t_words where collected = 1";
		cursor = database.rawQuery(sql, null);
		wordcount =cursor.getCount();
		edata =new String[wordcount] ;
		data= new HashMap<String, String>();
		int i=0;
		String english;
		String chinese;
		while(cursor.moveToNext()){
			english = cursor.getString(cursor.getColumnIndex("_id"));
			chinese = cursor.getString(cursor.getColumnIndex("chinese"));
			edata[i++] =english ;
			data.put(english,chinese);
		}
	}
	public static SQLiteDatabase getDatabase() {
		return database;
	}
	public static Cursor getCursor() {
		return cursor;
	}
}
