package com.Dictionary.dictionarye;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.database.sqlite.SQLiteDatabase;

public class CreateDatabase {
	private Context myContext;
	public String getDatabaseFilename() {
		return databaseFilename;
	}

	public CreateDatabase(Context context){
		myContext = context;
	}
	private final String DATABASE_PATH = android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+"/dictionary";
	private final String DATABASE_FILENAME = "dictionary.db";
	String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;

		public  SQLiteDatabase createDatabase() {
		File file = new File(DATABASE_PATH);
		if(!file.exists()){file.mkdir();}
		try {
			if(!(new File(databaseFilename)).exists()){
			InputStream iStream =myContext.getResources().openRawResource(R.raw.dictionary);
			FileOutputStream fos = new FileOutputStream(databaseFilename);
			byte[] buffer = new byte[8192];
			int count = 0;
			while((count=iStream.read(buffer))>0){fos.write(buffer, 0, count);}
			fos.close();
			iStream.close();
		}
			SQLiteDatabase db =SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
			return db;
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return null;
}
}
