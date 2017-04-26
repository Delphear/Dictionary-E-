package com.Dictionary.dictionarye;
import java.io.StringWriter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener{
	   private EditText username;
	   private EditText userpassword;
	   private Button register;
	   private Button login;
	   private SQLiteDatabase database;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		String intentText=getIntent().getStringExtra("loginUser");
		if(!"".equals(intentText)){
			Intent intent = new Intent();
			intent.putExtra("loginUser",intentText);
			intent.setClass(LoginActivity.this, LoginUserActivity.class);
			startActivityForResult(intent, 0);
		}
		database = (new MySqliteOpenHelper(getApplicationContext())).getWritableDatabase();
		username = (EditText) findViewById(R.id.username);
		userpassword = (EditText) findViewById(R.id.password);
		register = (Button) findViewById(R.id.register);
		login = (Button) findViewById(R.id.login);
		register.setOnClickListener(this);
		login.setOnClickListener(this);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.register:
			userRegister();
			break;

		case R.id.login:
			// TODO complete content
			userLogin();
			break;
		}
		
	}
	public void userRegister(){
		String name = username.getText().toString().trim();
		String password = userpassword.getText().toString().trim();
		if(TextUtils.isEmpty(name)||TextUtils.isEmpty(password)){
			Toast.makeText(this, "username and password cannot be empty!", Toast.LENGTH_LONG).show();
		}else{
			//TODO 连接数据库
			//String sqlString = "select * from user_info where username =?";
			Cursor cursor = database.query("user_info", null, "username=?",new String[]{name},null, null, null);
			boolean isRegistered=false;
			if(cursor.moveToFirst()){
				isRegistered = true;
			}
			if(isRegistered){
				Toast.makeText(this, "Registered already,please change a name! ", Toast.LENGTH_LONG).show();
				username.setText("");
				userpassword.setText("");
			}else{
				String EnglishNoteTable="E"+name +"table";
				String JapaneseNoteTable="J"+name +"table";
				try {
					ContentValues cv = new ContentValues();
					cv.put("username", name);
					cv.put("password", password);
					cv.put("Enotebook", EnglishNoteTable);
					cv.put("Jnotebook", JapaneseNoteTable);
					database.insertOrThrow("user_info", null, cv);
					System.out.println("insert sucessfully");
//	database.execSQL("insert into user_info values(?,?,?,?)", new String[]{name,password,EnglishNoteTable,JapaneseNoteTable});
					database.execSQL("create table "+EnglishNoteTable+"(english vachar(20))");
					System.out.println("created sucessfully");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("SQL ERROR");
					e.printStackTrace();
				}
			//	database.execSQL("create table ?(japanese vachar(20) primary key foreign key(japanese) references Depart(t_words))",new String[]{JapaneseNoteTable});
				String sqlStringtest = "select * from user_info where username =?";
				Cursor cursortest = database.rawQuery(sqlStringtest, new String[]{name});
				boolean isRegisteredtest=false;
				if(cursortest.moveToFirst()){
					isRegisteredtest = true;
				}
				if(isRegisteredtest){
					Toast.makeText(this, "Registered Successfully!", Toast.LENGTH_LONG).show();
					userLogin();
				}
				else{
					Toast.makeText(this, "Register Failed,please try again!", Toast.LENGTH_LONG).show();
					username.setText("");
					userpassword.setText("");
				}
			}
		}
		
	}
	public void userLogin(){
		String name = username.getText().toString().trim();
		String password = userpassword.getText().toString().trim();
		if(TextUtils.isEmpty(name)||TextUtils.isEmpty(password)){
			Toast.makeText(this, "username and password cannot be empty!", Toast.LENGTH_LONG).show();
		}else{
			//TODO 连接数据库
			String sqlString = "select * from user_info where username = '"+name+"'";
			Cursor cursor = database.rawQuery(sqlString, null);
			boolean isRegistered=false;
			if(cursor.moveToFirst()){
				isRegistered = true;
			}
			if(!isRegistered){
				Toast.makeText(this, "No such user registed!", Toast.LENGTH_LONG).show();
				username.setText("");
				userpassword.setText("");
			}
			else if(!password.equals(cursor.getString(1))){
				Toast.makeText(this, "Wrong password!", Toast.LENGTH_LONG).show();
				username.setText("");
				userpassword.setText("");
			}
			else if(password.equals(cursor.getString(1))){
			Intent intent = new Intent();
			intent.putExtra("username_show", "hi,"+username.getText().toString());
			intent.setClass(LoginActivity.this, MainActivity.class);
			startActivityForResult(intent, 0);
			}
			database.close();
		}

	}
/*		private void saveCheckedUser(String name,String pwd){
		SharedPreferences sp = getSharedPreferences("userInfo",0);
		Editor editor = sp.edit();
		editor.putString("name", name);
		editor.putString("pwd", pwd);
		editor.commit();
	if(result){
			Toast.makeText(this, "Register successfully", Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(this, "Registering Failed", Toast.LENGTH_LONG).show();
		}
	}*/
/*	private void changePage(Context context,String page){
		Intent in = new Intent();  
        in.setClassName( context, page);  
        startActivity( in );
	}*/
}
