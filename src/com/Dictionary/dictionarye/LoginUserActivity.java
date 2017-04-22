package com.Dictionary.dictionarye;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class LoginUserActivity extends Activity implements OnClickListener{
	private TextView login_user;
	private Button back_homepage;
	private Button logout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_user);
		login_user = (TextView) findViewById(R.id.login_user);
		back_homepage = (Button) findViewById(R.id.back_homepage);
		logout = (Button) findViewById(R.id.logout);
		login_user.setText(getIntent().getStringExtra("loginUser"));
		back_homepage.setOnClickListener(this);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.back_homepage:
			changePage(LoginUserActivity.this, "com.Dictionary.dictionarye.MainActivity");
			break;

		case R.id.logout:
			logout();
			break;
		}
	}
	private void changePage(Context context,String page){
		Intent in = new Intent();  
        in.setClassName( context, page);  
        startActivity( in );
	}
	public void logout(){
		//TODO
	}
}
