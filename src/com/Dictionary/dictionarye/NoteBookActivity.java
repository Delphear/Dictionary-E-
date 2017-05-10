package com.Dictionary.dictionarye;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NoteBookActivity extends Activity implements OnClickListener {
	private static final String ACTIVITY_TAG="LogDemoNotebook";
	private Spinner word_type;
	private ListView words_show;
	private Button Homepage;
	private Button practice;
	private SQLiteDatabase database;
	private String [] edata;
	private Map<String, String> data;
	private Bundle savedInstanceState;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.savedInstanceState = savedInstanceState;
		setContentView(R.layout.activity_note_book);
		word_type=(Spinner) findViewById(R.id.word_type);
		words_show= (ListView) findViewById(R.id.words_show);
		Homepage = (Button) findViewById(R.id.btn_homepage);
		practice = (Button) findViewById(R.id.btn_prectise);
		Words.setWords();
		database = Words.getDatabase();
		data = Words.getData();
		edata = Words.getEdata();	 
		words_show.setAdapter(new MyListviewAdapter(this,Words.getCursor(),true));
		Homepage.setOnClickListener(this);
		practice.setOnClickListener(this);

		words_show.setOnItemClickListener(/**
		 * @author minqi
		 *当点击单词时进行的操作
		 */
		new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				String key = edata[position];
				new AlertDialog.Builder(NoteBookActivity.this).setTitle(key)
				.setMessage(data.get(key)).setPositiveButton("确定", null).show();
			}
		});
		words_show.setOnItemLongClickListener(new OnItemLongClickListener() {

			/* (non-Javadoc)
			 * @see android.widget.AdapterView.OnItemLongClickListener#onItemLongClick(android.widget.AdapterView, android.view.View, int, long)
			 * 生词本单词被长按时执行的操作
			 */
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				final String key = edata[position];
				final ContentValues values = new ContentValues();
				try {
						values.put("collected",0);
						new AlertDialog.Builder(NoteBookActivity.this)
						.setMessage("确定删除？")
						.setPositiveButton("删除",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface di, int i) {
										database.update("t_words", values, "english=?", new String[]{key});
						            	NoteBookActivity.this.onCreate(NoteBookActivity.this.savedInstanceState);
						            	Toast.makeText(NoteBookActivity.this, "单词"+key+"已移生词本", 1).show();
						            	
										//由于只生成了一个view组件 所以刷新没用
									};
								}).setNegativeButton("取消", null).show();
						return true;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				return false;
			}
		});
	}
 
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 * 当界面上的button被点击后进行相关操作
	 */
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
	/**
	 * @param context
	 * @param page
	 * 跳转界面
	 */
	private void changePage(Context context,String page){
		Intent in = new Intent();  
        in.setClassName( context, page);  
        startActivityForResult(in, 0);
	}
	public class MyListviewAdapter extends CursorAdapter{
		
		public MyListviewAdapter(Context context, Cursor c, boolean autoRequery) {
			super(context, c, autoRequery);
			// TODO Auto-generated constructor stub
			layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		private LayoutInflater layoutInflater;
		@Override
		public CharSequence convertToString(Cursor cursor){
			return cursor==null?"":cursor.getString(cursor.getColumnIndex("_id"));
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
