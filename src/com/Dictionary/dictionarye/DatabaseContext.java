package com.Dictionary.dictionarye;

import java.io.File;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class DatabaseContext extends ContextWrapper {
	private Context context;
	public DatabaseContext(Context base) {
		super(base);
		// TODO Auto-generated constructor stub
		context = base;
	}
	   @Override  
	    public File getDatabasePath(String name) {  
	        String path = android.os.Environment
	    			.getExternalStorageDirectory().getAbsolutePath()
	    			+"/dictionary";  
	        String dbfile = path + "/" + name;  
	        if (!dbfile.endsWith(".db")) {  
	            dbfile += ".db";  
	        }  
	  
	        File result = new File(dbfile);  
	  
	        if (!result.getParentFile().exists()) {  
	            result.getParentFile().mkdirs();  
	        }  
	  
	        return result;  
	    }  
	  
	    /* 
	     * this version is called for android devices >= api-11. thank to @damccull 
	     * for fixing this. 
	     */  
	    @Override  
	    public SQLiteDatabase openOrCreateDatabase(String name, int mode,  
	            SQLiteDatabase.CursorFactory factory,  
	            DatabaseErrorHandler errorHandler) {  
	        return openOrCreateDatabase(name, mode, factory);  
	    }  
	  
	    /* this version is called for android devices < api-11 */  
	    @Override  
	    public SQLiteDatabase openOrCreateDatabase(String name, int mode,  
	            SQLiteDatabase.CursorFactory factory) {  
	        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(  
	                getDatabasePath(name), null);  
	  
	        return result;  
	    }
}
