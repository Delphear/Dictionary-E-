package com.Dictionary.dictionarye;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class PracticeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practice);
	}
	public void onClick(View view){
		Intent in = new Intent();  
        in.setClassName( PracticeActivity.this, "com.Dictionary.dictionarye.MainActivity");  
        startActivity( in );
	}
}
