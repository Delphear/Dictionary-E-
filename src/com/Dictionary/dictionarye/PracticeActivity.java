package com.Dictionary.dictionarye;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class PracticeActivity extends Activity implements OnClickListener{
	private static final String ACTIVITY_TAG="LogDemoPractice";
	private RadioGroup group;
	private RadioButton btn_a;
	private RadioButton btn_b;
	private RadioButton btn_c;
	private TextView text_a;
	private TextView text_b;
	private TextView text_c;
	private TextView topic;
	private TextView show_answer;
	private Button next_topic;
	private Button end_practice;
	private Button practice_homepage;
	private SQLiteDatabase database;
	private String [] edata;
	private String [] tested_word;
	private Map<String, String> data;
	private int wordcount;
	private StringBuilder errorlog;
	private int practice_count;
	private int error_count;
	private int right_count;
	private String show_word;
	private Thread newThread; 
	private Bundle savedInstanceState;
	private Handler myHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			String[] content = (String[]) msg.obj;
			show_word =content[0];
			String text =content[1];
			String text1 =content[2];
			String text2 =content[3];
			int rnum = msg.what;
			topic.setText(show_word);
			switch (rnum) {
				case 0:
					text_a.setText(text);
					text_b.setText(text1);
					text_c.setText(text2);
					break;
				case 1:	
					text_b.setText(text);
					text_a.setText(text1);
					text_c.setText(text2);
					break;
				case 2:
					text_c.setText(text);
					text_a.setText(text1);
					text_b.setText(text2);
					break;
				}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practice);
		init();
		if(wordcount <= 0){
		//	onCreate(this.savedInstanceState);
			btn_a.setVisibility(4);
			btn_b.setVisibility(4);
			btn_c.setVisibility(4);
			//group.setVisibility(0);
			new AlertDialog.Builder(PracticeActivity.this)
			.setMessage("Error:您的生词本为空").setPositiveButton("确定", null).show();
		/*	Intent in = new Intent(); 
	        in.setClassName( PracticeActivity.this, "com.Dictionary.dictionarye.MainActivity");  
	        startActivity(in);*/
		}else{
		tested_word = new String[wordcount];
		errorlog = new StringBuilder();
		showTopic();
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int radioButtonId = group.getCheckedRadioButtonId();
				String text = data.get(show_word);
				switch (radioButtonId) {
				case R.id.btn_a:
					if(!text_a.getText().equals(text)){
						error_count++;
						show_answer.setText(text);
						next_topic.setVisibility(0);
						Log.e(ACTIVITY_TAG, text+"******"+error_count);
						//errorlog.append(show_word+"\n"+text+"\n");
					}else{
						right_count++;
					next_topic.callOnClick();
					}
					break;
				case R.id.btn_b:
					if(!text_b.getText().equals(text)){
						error_count++;
						show_answer.setText(text);
						next_topic.setVisibility(0);
						Log.e(ACTIVITY_TAG, text+"******"+error_count);
						errorlog.append(show_word+"\n"+text+"\n");
					}else{
						right_count++;
						next_topic.callOnClick();
						}
					break;
				case R.id.btn_c:
					if(!text_c.getText().equals(text)){
						error_count++;
						show_answer.setText(text);
						next_topic.setVisibility(0);
						Log.e(ACTIVITY_TAG, text+"******"+error_count);
						errorlog.append(show_word+"\n"+text+"\n");
					}else{
						right_count++;
						next_topic.callOnClick();
						}
					break;

				}							
			}
		});
		}
		end_practice.setOnClickListener(this);
		practice_homepage.setOnClickListener(this);	
		next_topic.setOnClickListener(this);
	}
	/**
	 * 对组件，数据库，和相关变量进行初始化
	 */
	private void init() {
		group= (RadioGroup) findViewById(R.id.radioGroup1);
		btn_a = (RadioButton) findViewById(R.id.btn_a);
		btn_b = (RadioButton) findViewById(R.id.btn_b);
		btn_c = (RadioButton) findViewById(R.id.btn_c);
		text_a = (TextView) findViewById(R.id.text_a);
		text_b = (TextView) findViewById(R.id.text_b);
		text_c = (TextView) findViewById(R.id.text_c);
		topic =(TextView) findViewById(R.id.topic);
		show_answer = (TextView) findViewById(R.id.show_answer);
		next_topic = (Button) findViewById(R.id.next_topic);
		end_practice = (Button) findViewById(R.id.end_practice);
		practice_homepage = (Button) findViewById(R.id.practice_homepage);
		practice_count = 0;
		error_count = 0;
		right_count=0;
		Words.setWords();
		database = Words.getDatabase();
		data = Words.getData();
		edata = Words.getEdata();
		wordcount =Words.getWordcount();
	}
	/**
	 * 显示题目和选项内容
	 */
	private void showTopic(){
		btn_a.setChecked(false);
		btn_b.setChecked(false);
		btn_c.setChecked(false);
		if (practice_count>=wordcount) {
			new AlertDialog.Builder(PracticeActivity.this)
			.setMessage("您的生词已练完").setPositiveButton("确定", null).show();
			end_practice.callOnClick();
		}
		practice_count++;
		new Thread(){
			public void run() {
				Random random = new Random();
				int rnum1 ,rnum2,rnum3,rnum4;
				boolean tested = false;
				do {
					rnum1 = random.nextInt(wordcount);
					String word = edata[rnum1];
					for(int i=0;i<(practice_count-1);i++){
						if(word.equals(tested_word[i])){
							tested = true;
							break;
						}
					}
					if(!tested){
						tested_word[practice_count-1] = word;
						rnum2=random.nextInt(3);
						rnum3 = (rnum1+1+rnum2)%wordcount;
						rnum4 = (rnum3+1+rnum2)%wordcount;
						if(rnum1==rnum3 && rnum1==rnum4){
							rnum3=(rnum1+1)%wordcount;
							rnum4=(rnum1+2)%wordcount;
						}else if(rnum1==rnum3 && rnum1!=rnum4){
							rnum3=(rnum1+rnum4)%wordcount;
						}else if(rnum1==rnum4 && rnum1!=rnum3){
							rnum4=(rnum1+rnum3)%wordcount;
						}else if(rnum1!=rnum3 && rnum3==rnum4){
							rnum3=(rnum1+rnum4)%wordcount;
						}
						String[] text = new String[]{word,data.get(word),data.get(edata[rnum3]),data.get(edata[rnum4])};
						Message msg = new Message();
						msg.obj = text;
						msg.what = rnum2;
						myHandler.sendMessage(msg);						
					}
				} while (tested);
		};}.start();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.end_practice:
			StringBuilder grade=new StringBuilder();
			int total = practice_count-1;
			DecimalFormat format2 = new DecimalFormat( "0.00 "); 
			double weight=(right_count*1.0/total)*100;
			weight = Double.parseDouble(format2.format(weight));
			//double weight =right_count/total; 
			try {
				grade.append("总题数："+(total)+"\n做对："+(right_count)+"\n正确率："+weight+"%");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//grade.append(errorlog);
			//group.setClickable(false);
			btn_a.setClickable(false);
			btn_b.setClickable(false);
			btn_c.setClickable(false);
			new AlertDialog.Builder(PracticeActivity.this).setTitle("您的成绩为:")
			.setMessage(grade).setPositiveButton("确定", null).show();
			break;
		case R.id.practice_homepage:
			Intent in = new Intent(); 
	        in.setClassName( PracticeActivity.this, "com.Dictionary.dictionarye.MainActivity");  
	        startActivity(in);
	        PracticeActivity.this.finish();
	        break;
		case R.id.next_topic:
			showTopic();
			show_answer.setText("");
			next_topic.setVisibility(4);
			break;
		}
		
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
	            in.setClassName( PracticeActivity.this, page);  
	            startActivity(in);
	            PracticeActivity.this.finish();  
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
