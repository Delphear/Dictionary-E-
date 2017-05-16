package com.Dictionary.dictionarye;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class NotFoundActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_not_found);
		
	}
	public void onClick(View view){
		Intent in = new Intent();  
        in.setClassName( NotFoundActivity.this, "com.Dictionary.dictionarye.MainActivity");  
        startActivityForResult(in, 0);
        NotFoundActivity.this.finish();
	}
}
