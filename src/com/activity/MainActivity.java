package com.activity;
import com.zxing.activity.CaptureActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private Button mButton_nfc,mButton_decode;
	private TextView[] table_id = new TextView[5];
	private TextView[] table_result =  new TextView[5];
	private TextView[] table_date = new TextView[5];
	
	private String id,data,content;
	private SQLiteDatabase db;
	static ResultDBConnection helper;
	
	public interface ResultData
	{
		String TABLE_NAME = "Results";
		String ID = "_id";
		String CONTENT = "content";
		String DATA = "data";
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
        display();
    }
    
    private void init()
    {
    	 mButton_nfc = (Button)findViewById(R.id.button_nfc); 	 
         mButton_nfc.setOnClickListener(new View.OnClickListener() {
 			 public void onClick(View v) {
 				 Intent intent = new Intent();
 				 intent.setClass(MainActivity.this, NfcActivity.class); 
 				 startActivity(intent);
 			 }
 		 });  
    	 
    	 mButton_decode = (Button)findViewById(R.id.button_decode);
         mButton_decode.setOnClickListener(new View.OnClickListener() {
 			 public void onClick(View v) {
 				 Intent intent = new Intent();
 				 intent.setClass(MainActivity.this, CaptureActivity.class); 
 				 startActivity(intent);
 			 }
 		 });  
         
         table_id[0] = (TextView) findViewById(R.id.id1);
         table_id[1] = (TextView) findViewById(R.id.id2);
         table_id[2] = (TextView) findViewById(R.id.id3);
         table_id[3] = (TextView) findViewById(R.id.id4);
         table_id[4] = (TextView) findViewById(R.id.id5);
         
         table_result[0] = (TextView) findViewById(R.id.result1);
         table_result[1] = (TextView) findViewById(R.id.result2);
         table_result[2] = (TextView) findViewById(R.id.result3);
         table_result[3] = (TextView) findViewById(R.id.result4);
         table_result[4] = (TextView) findViewById(R.id.result5);
         
         table_date[0] = (TextView) findViewById(R.id.date1);
         table_date[1] = (TextView) findViewById(R.id.date2);
         table_date[2] = (TextView) findViewById(R.id.date3);
         table_date[3] = (TextView) findViewById(R.id.date4);
         table_date[4] = (TextView) findViewById(R.id.date5);
         
     	 helper = new ResultDBConnection(this);
         db = helper.getWritableDatabase();
    }
    /**
     * 刷新页面，在Activity跳转返回后主动调用
     */
    @Override
    protected void onResume() {
    	super.onResume();
    	onCreate(null);	
    }
    
    /**
     * 获取数据库的最新五条数据并显示
     */
    private void display()
    {
    	 /*按照时间顺序对数据库进行排序*/
    	 String sql = "select * from "+ ResultData.TABLE_NAME + " order by data desc";
         Cursor c = db.rawQuery(sql,null);
         
         for(int i = 0; i < 5 && c.moveToNext(); i++)
         {
 			 id = c.getString(0);
 			 id = String.format("%-15s", id);
        	 content = c.getString(1);
        	 content = String.format("%-15s", content);
        	 data = c.getString(2);
 			 data = String.format("%-15s", data);
 			 
 			 table_id[i].setText(id);
 			 table_result[i].setText(content);
 			 table_date[i].setText(data);

         }
    }
    
	public static class ResultDBConnection extends SQLiteOpenHelper
	{
		private static final String DATABASE_NAME = "ResultsDB";
		private static final int DATABASE_VERSION = 1;
		public ResultDBConnection(Context context) {
			super(context,DATABASE_NAME, null, DATABASE_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = "CREATE TABLE " + ResultData.TABLE_NAME + " ("
					+ ResultData.ID + " INTEGER primary key autoincrement, "
					+ ResultData.CONTENT + " text not null, "
					+ ResultData.DATA + " text not null "+ ");";
			db.execSQL(sql);
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
	}
}