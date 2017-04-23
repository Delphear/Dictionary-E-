package com.Dictionary.dictionarye;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.R.layout;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

	private Spinner word_type;
	private AutoCompleteTextView actvWord;
	private Button searchWord;
	private Button btn_notebook;
	private Button btn_mine;
	private Button btn_home;
	private TextView result_show;
	private static TextView username_show;
	private final String DATABASE_PATH = android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+"/dictionary";
	private final String DATABASE_FILENAME = "dictionary.db";
	private SQLiteDatabase database;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		database = openDatabase();
		word_type = (Spinner) findViewById(R.id.spinner_language_type);
		searchWord = (Button)findViewById(R.id.button_search);
		actvWord = (AutoCompleteTextView)findViewById(R.id.actv_word);
		result_show = (TextView)findViewById(R.id.result_show);
		username_show = (TextView) findViewById(R.id.username_show);
		btn_home = (Button) findViewById(R.id.btn_home);
		btn_notebook = (Button) findViewById(R.id.btn_notebook);
		btn_mine = (Button) findViewById(R.id.btn_mine);
		username_show.setText(getIntent().getStringExtra("username_show"));
		actvWord.addTextChangedListener(this);
		searchWord.setOnClickListener(this);
		btn_home.setOnClickListener(this);
		btn_notebook.setOnClickListener(this);
		btn_mine.setOnClickListener(this);
	}
/*	public static void setUsername_show(String username_show) {
		MainActivity.username_show.setText(username_show);
	}*/
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
	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		result_show.setText("");
		Cursor cursor= database.rawQuery("select english as _id from t_words where english like ?",
				new String[]{s.toString()+"%"});
		DictionaryAdapter adapter = new DictionaryAdapter(this, cursor, true);
		actvWord.setAdapter(adapter);
		
		
	}
	@Override
	public void onClick(View view){
		switch (view.getId()) {
		case R.id.button_search:
			String sqlString = "select chinese from t_words where english=?";
			Cursor cursor = database.rawQuery(sqlString, new String[]{actvWord.getText().toString().trim()});
			String result = "";
			if(cursor.getCount()>0){
				cursor.moveToFirst();
				result = cursor.getString(cursor.getColumnIndex("chinese"));
			}
			//new AlertDialog.Builder(this).setTitle("The result is").
			//setMessage(result).setPositiveButton("close", null).show();
			if("".equals(result)){
				Toast toast = Toast.makeText(this,"Input a word please!",Toast.LENGTH_LONG);
				LinearLayout linearLayout = (LinearLayout) toast.getView();  
				TextView messageTextView = (TextView) linearLayout.getChildAt(0);  
				messageTextView.setTextSize(30);  
				toast.show();
			}
			result_show.setText(result);
			break;
		case R.id.btn_home:
			break;
		case R.id.btn_notebook:
			String page1 = "com.Dictionary.dictionarye.NoteBookActivity";
			changePage(MainActivity.this, page1);
			break;
		case R.id.btn_mine:
			Intent intent = new Intent();
			intent.putExtra("loginUser",username_show.getText().toString());
			intent.setClass(MainActivity.this, LoginActivity.class);
			startActivityForResult(intent, 0);
			break;
		}
		
		
	}
	private void changePage(Context context,String page){
		Intent in = new Intent();  
        in.setClassName( context, page);  
        startActivity( in );
	}
	private SQLiteDatabase openDatabase() {
		
			String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
			File file = new File(DATABASE_PATH);
			if(!file.exists()){file.mkdir();}
			try {
				if(!(new File(databaseFilename)).exists()){
				InputStream iStream = getResources().openRawResource(R.raw.dictionary);
				FileOutputStream fos = new FileOutputStream(databaseFilename);
				byte[] buffer = new byte[8192];
				int count = 0;
				while((count=iStream.read(buffer))>0){fos.write(buffer, 0, count);}
				fos.close();
				iStream.close();
			}
			SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
			return database;
		}catch (FileNotFoundException e) {
			e.getStackTrace();
			// TODO: handle exception
		}catch (Exception e) {
			// TODO: handle exception
			e.getStackTrace();
		}
		return null;
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
