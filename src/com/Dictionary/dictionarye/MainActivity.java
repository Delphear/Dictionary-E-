package com.Dictionary.dictionarye;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


import android.R.layout;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, TextWatcher{
	private static final String ACTIVITY_TAG="LogDemo";
	private Spinner word_type;
	private AutoCompleteTextView actvWord;
	private Button searchWord;
	private Button addnote;
	private Button homepage;
	private Button practice;
	private Button btn_notebook;
	private TextView result_show;
	private int collected;
	private static SQLiteDatabase database;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		database=(new CreateDatabase(this)).createDatabase();
		word_type = (Spinner) findViewById(R.id.spinner_language_type);
		searchWord = (Button)findViewById(R.id.button_search);
		actvWord = (AutoCompleteTextView)findViewById(R.id.actv_word);
		result_show = (TextView)findViewById(R.id.result_show);
		addnote = (Button) findViewById(R.id.addnote);
		homepage = (Button) findViewById(R.id.homepage);
		btn_notebook = (Button) findViewById(R.id.btn_notebook);
		practice = (Button) findViewById(R.id.practice);
		actvWord.addTextChangedListener(this);
		searchWord.setOnClickListener(this);
		addnote.setOnClickListener(this);
		homepage.setOnClickListener(this);
		btn_notebook.setOnClickListener(this);
		practice.setOnClickListener(this);
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub
		//result_show.setText("");
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		//result_show.setText("");
	}
	
	/* (non-Javadoc)
	 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
	 * 输入框内容改变之后执行的操作
	 */
	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		result_show.setText("");
		Cursor cursor= database.rawQuery("select english as _id from t_words where english like ?",
				new String[]{s.toString()+"%"});
		DictionaryAdapter adapter = new DictionaryAdapter(this, cursor, true);
		actvWord.setAdapter(adapter);
		addnote.setBackgroundResource(R.drawable.add_note);
		addnote.setVisibility(View.INVISIBLE);
		homepage.setBackgroundResource(R.drawable.dict_normal);
		
		
	}
	@Override
	public void onClick(View view){
		switch (view.getId()) {
		case R.id.button_search:
			if(actvWord.getText().toString().equals("")){
				Toast toast = Toast.makeText(this,"Input a word please!",Toast.LENGTH_LONG);
				LinearLayout linearLayout = (LinearLayout) toast.getView();  
				TextView messageTextView = (TextView) linearLayout.getChildAt(0);  
				messageTextView.setTextSize(30);  
				toast.show();
			}else{
				String sqlString = "select * from t_words where english=?";
				Cursor cursor = database.rawQuery(sqlString, new String[]{actvWord.getText().toString().trim()});
				String result = "";
				if(cursor.getCount()>0){
					cursor.moveToFirst();
					result = cursor.getString(cursor.getColumnIndex("chinese"));
					collected = cursor.getInt(cursor.getColumnIndex("collected"));

					if(collected==1){
						addnote.setBackgroundResource(R.drawable.add_note_disable);
					}
					result_show.setText(result);
					homepage.setBackgroundResource(R.drawable.dict_selected);
					addnote.setVisibility(view.VISIBLE);
				}else if("".equals(result)){
					changePage(MainActivity.this, "com.Dictionary.dictionarye.NotFoundActivity");
				}
			}
			
			break;
		case R.id.addnote:
			String sqlString1 = "select collected from t_words where english=?";
			Cursor cursor1 = database.rawQuery(sqlString1, new String[]{actvWord.getText().toString().trim()});
			if(cursor1.getCount()>0){
				cursor1.moveToFirst();
				collected = cursor1.getInt(cursor1.getColumnIndex("collected"));
			}
			ContentValues values = new ContentValues();
			try {
				if(collected==1){
					values.put("collected",0);
					database.update("t_words", values, "english=?", new String[]{actvWord.getText().toString().trim()});
					addnote.setBackgroundResource(R.drawable.add_note);
				}else{
					values.put("collected",1);
					database.update("t_words", values, "english=?", new String[]{actvWord.getText().toString().trim()});
					addnote.setBackgroundResource(R.drawable.add_note_disable);	
				}
				Log.v(MainActivity.ACTIVITY_TAG, ""+collected);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.homepage:
			String page = "com.Dictionary.dictionarye.MainActivity";
			changePage(MainActivity.this, page);
			break;
		case R.id.btn_notebook:
			String page1 = "com.Dictionary.dictionarye.NoteBookActivity";
			changePage(MainActivity.this, page1);
			break;
		case R.id.practice:
			String page2 = "com.Dictionary.dictionarye.PracticeActivity";
			changePage(MainActivity.this, page2);
			break;
		}
		
		
	}
	private void changePage(Context context,String page){
		Intent in = new Intent(); 
        in.setClassName( context, page);  
        startActivity(in);
	}
	
	public static SQLiteDatabase getDatabase() {
		return database;
	}
	public static void setDatabase(SQLiteDatabase database) {
		MainActivity.database = database;
	}

	public class DictionaryAdapter extends CursorAdapter{
		
		private LayoutInflater layoutInflater;
		@Override
		public CharSequence convertToString(Cursor cursor){
			return cursor==null?"":cursor.getString(cursor.getColumnIndex("_id"));
		}
		public DictionaryAdapter(Context context, Cursor c, boolean autoRequery) {
			super(context, c, autoRequery);
			// TODO Auto-generated constructor stub
			layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		private void setView(View view,Cursor cursor){
			TextView tvWordItem = (TextView)view;
			tvWordItem.setText(cursor.getString(cursor.getColumnIndex("_id")));
		}
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = layoutInflater.inflate(R.layout.word_list_item, null);
			setView(view, cursor);
			return view;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			// TODO Auto-generated method stub
			setView(view, cursor);
		}
	}	
}
