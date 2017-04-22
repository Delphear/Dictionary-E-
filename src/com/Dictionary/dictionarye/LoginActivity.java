package com.Dictionary.dictionarye;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
			
		}
		userLogin();
	}
	public void userLogin(){
		String name = username.getText().toString().trim();
		String password = userpassword.getText().toString().trim();
		if(TextUtils.isEmpty(name)||TextUtils.isEmpty(password)){
			Toast.makeText(this, "username and password cannot be empty!", Toast.LENGTH_LONG).show();
		}else{
			//TODO 连接数据库
		/*	MainActivity.setUsername_show("hi,"+username);
			String page = "com.Dictionary.dictionarye.MainActivity";
			changePage(LoginActivity.this, page);*/
			//String username_show = "hi,"+ username;
			Intent intent = new Intent();
			intent.putExtra("username_show", "hi,"+username.getText().toString());
			intent.setClass(LoginActivity.this, MainActivity.class);
			startActivityForResult(intent, 0);
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
