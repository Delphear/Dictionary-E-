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
import android.widget.ListView;
import android.widget.Spinner;

public class NoteBookActivity extends Activity implements OnClickListener {
	
	private Spinner word_type;
	private ListView words_show;
	private Button Homepage;
	private Button practice;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_book);
		word_type=(Spinner) findViewById(R.id.word_type);
		words_show= (ListView) findViewById(R.id.words_show);
		Homepage = (Button) findViewById(R.id.btn_homepage);
		practice = (Button) findViewById(R.id.btn_prectise);
		Homepage.setOnClickListener(this);
		practice.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_homepage:
			String page = "com.Dictionary.dictionarye.MainActivity";
			changePage(NoteBookActivity.this, page);
			break;

		case R.id.btn_prectise:
			String page1 = "com.Dictionary.dictionarye.PracticeActivity";
			changePage(NoteBookActivity.this, page1);
			break;
		}
		
	}
	private void changePage(Context context,String page){
		Intent in = new Intent();  
        in.setClassName( context, page);  
        startActivity( in );
	}
}
