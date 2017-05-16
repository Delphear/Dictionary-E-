package com.Dictionary.dictionarye;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RepeatActivity extends Activity implements OnClickListener{

	private TextView word_show;
	private Button repeat_homepage;
	private Button repeat_next;
	private Button repeat_over;
	private Button unkown_word;
	private Button next_word;
	private Button know_this_word;
	private Button not_know_this_word;
	private String[] this_word;
	private boolean flag;
	private SQLiteDatabase database;
	private ArrayMap<String, Integer> word;
	//private Thread myThread;
	private int count;
	private int index;
	private Handler myHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			word =(ArrayMap<String, Integer>) msg.obj;
			this_word = word.keyAt(index).split("\n");
				word_show.setText(this_word[0]);
				if (word.valueAt(index).equals(new Integer(0))) {
					unkown_word.setVisibility(0);
				}else{
					unkown_word.setVisibility(4);
				}
		//	index++;
			if (index==14) {
				repeat_next.setVisibility(0);
			}	
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_repeat);
		word_show = (TextView) findViewById(R.id.word_show);
		repeat_homepage=(Button) findViewById(R.id.repeat_homepage);
		repeat_next=(Button) findViewById(R.id.repeat_next);
		repeat_over=(Button) findViewById(R.id.repeat_over);
		unkown_word=(Button) findViewById(R.id.unkown_word);
		next_word=(Button) findViewById(R.id.next_word);
		know_this_word=(Button) findViewById(R.id.know_this_word);
		not_know_this_word=(Button) findViewById(R.id.not_know_this_word);
		database=MainActivity.getDatabase();
		if (!database.isOpen()) {
			SQLiteDatabase.openOrCreateDatabase(database.getPath(), null);
		}
		count = 0;
		index = 0;
		flag =true;
		showWord();
		repeat_homepage.setOnClickListener(this);
		repeat_next.setOnClickListener(this);
		repeat_over.setOnClickListener(this);
		unkown_word.setOnClickListener(this);
		next_word.setOnClickListener(this);
		know_this_word.setOnClickListener(this);
		not_know_this_word.setOnClickListener(this);
	}
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.repeat_homepage:
			Intent in = new Intent(); 
	        in.setClassName( RepeatActivity.this, "com.Dictionary.dictionarye.MainActivity");  
	        startActivity(in);
	        RepeatActivity.this.finish();
			break;

		case R.id.repeat_next:
			repeat_next.setVisibility(4);
			next_word.setVisibility(4);
			next_word.setClickable(true);
			know_this_word.setClickable(true);
			showWord();
			break;
		case R.id.repeat_over:
			//myThread.stop();
				//myHandler.wait(200000);
			if (flag) {
//				next_word.setVisibility(0);
				next_word.setClickable(true);
				repeat_next.setClickable(true);
				know_this_word.setClickable(true);
				repeat_over.setBackgroundResource(R.drawable.dict_pause);
				flag = false;
			}else {
				next_word.setVisibility(4);
				next_word.setClickable(false);
				repeat_next.setVisibility(4);
				repeat_next.setClickable(false);
				know_this_word.setClickable(false);
				repeat_over.setBackgroundResource(R.drawable.dict_continue);
				flag =true;
			}				
			break;
		case R.id.unkown_word:
			String[] sourceStrArray = ((String) word_show.getText()).split("\n");
			Log.e("show_word",sourceStrArray[0]);
			ContentValues values = new ContentValues();
			values.put("collected",1);
			try {
				database.update("t_words", values, "english=?", new String[]{sourceStrArray[0]});
				unkown_word.setBackgroundResource(R.drawable.add_note_disable);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.next_word:
			index++;
			next_word.setVisibility(4);
			this_word = word.keyAt(index).split("\n");
			word_show.setText(this_word[0]);
			if (word.valueAt(index).equals(new Integer(0))) {
				unkown_word.setBackgroundResource(R.drawable.add_note);
				unkown_word.setVisibility(0);
			}else{
				unkown_word.setVisibility(4);
			}

			if (index==14) {
				repeat_next.setVisibility(0);
				next_word.setVisibility(4);
				next_word.setClickable(false);
				know_this_word.setClickable(false);
			}
			break;
		case R.id.know_this_word:
			next_word.callOnClick();
			break;
		case R.id.not_know_this_word:
			word_show.setText(word.keyAt(index));
			next_word.setVisibility(0);
			break;
		}
		
	}
	public void showWord(){
		 new Thread(){
			public void run() {
				Random random = new Random();
				int rnum1;
				rnum1 = random.nextInt(1000);
				//String[] word = new String[15];
				ArrayMap word = new ArrayMap();
				String sqlString = "select * from t_words";
				Cursor cursor = database.rawQuery(sqlString, null);
				String result = "";
				Integer collected;
				count=0;
				index = 0;
				if(cursor.getCount()>0){
					while(count<15){
						cursor.moveToPosition(rnum1);
						result = cursor.getString(cursor.getColumnIndex("english"))+"\n\n"+cursor.getString(cursor.getColumnIndex("chinese"));
						collected =Integer.valueOf(cursor.getInt(cursor.getColumnIndex("collected"))) ;
						word.put(result, collected);
						rnum1 = random.nextInt(1000);
						count++;
					}					
				}
				Message msg = new Message();
				msg.obj = word;
				myHandler.sendMessage(msg);	
		};}.start();
	}
	@Override  
	public void onBackPressed() {  
	  
	       Toast.makeText(this, "继续点击一次返回键将返回查询界面", Toast.LENGTH_LONG).show();  
	    new AlertDialog.Builder(this).setTitle("确认返回吗？")  
	    .setPositiveButton("确定", new DialogInterface.OnClickListener() {  
	          
	        @Override  
	        public void onClick(DialogInterface dialog, int which) {  
	              
	            //退出APP  
	        	String page = "com.Dictionary.dictionarye.MainActivity";
	        	Intent in = new Intent(); 
	            in.setClassName(RepeatActivity.this, page);  
	            startActivity(in);
	            RepeatActivity.this.finish();  
	            //异常导致app挂掉,需要发送完数据后，kill掉死掉的APP。  
	            //int myPid=android.os.Process.myPid();  
	            //android.os.Process.killProcess(myPid);  
	        }  
	    })  
	    .setNegativeButton("取消", new DialogInterface.OnClickListener() {  
	          
	        @Override  
	        public void onClick(DialogInterface dialog, int which) {  
	        	
	            //nothing to do   
	        }  
	    })  
	    .show();  
	} 
}
