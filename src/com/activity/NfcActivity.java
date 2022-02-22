package com.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.equipment.Blight;
import com.equipment.Matrix;
import com.equipment.Motor;
import com.ndk.ledjni.LedJniActivity;
import com.socket.ClientSocketThread;
import com.socket.ClientSocketTools;
import com.socket.MessageListener;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class NfcActivity extends Activity implements OnClickListener{

	private Button nfc_open, nfc_read,nfc_exit;
	private TextView nfc_result;
	private ClientSocketThread clientSocketThread;
	private MyHandler handler;
	private byte[]buffer={(byte)0xFE,(byte)0xE0,0x08,0x32,0x72,0x00,0x02,0x0A};
	private byte[]data={(byte)0xFE,(byte)0xE0,0x08,0x32,0x72,0x00,0x02,0x0A};
	private Motor motor = new Motor();
	private Blight blight = new Blight();
	private Matrix matrix = new Matrix();
	
	public interface ResultData
	{
		String TABLE_NAME = "Results";
		String ID = "_id";
		String CONTENT = "content";
		String DATA = "data";
	}
	private LedJniActivity ledJniActivity = new LedJniActivity();
	static ResultDBConnection helper;
	
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.nfc); 
		init();
	
		handler = new MyHandler();
		/*实例化客户端 server线程*/
		/*利用 ClinetSocketThread类的getClientSocket()方法创建一个线程与 server进行通信*/
		new Thread(new Runnable(){
			public void run() {
				clientSocketThread=ClientSocketThread.getClientSocket(ClientSocketTools.getLocalIpAddress(),6109);		
				clientSocketThread.setListener(new MessageListener(){
						public void Message(byte []message, int message_len){

						System.arraycopy(message, 1, data, 0,4);
						String s = ClientSocketTools.byte2hex(data,4);
						Bundle bundle = new Bundle();
				    	bundle.putString("id",s);
				    	Message msg = handler.obtainMessage();
				    	msg.setData(bundle);
				    	handler.sendMessage(msg);
				}	
			});
		}
		}).start();
	}	
	
	void init()
	{
		/*按钮获取*/
		nfc_open = (Button) findViewById(R.id.nfc_open); 
		nfc_read = (Button) findViewById(R.id.nfc_read); 
		nfc_exit = (Button) findViewById(R.id.nfc_exit);
		nfc_result = (TextView) findViewById(R.id.nfc_result);
		nfc_open.setOnClickListener(this);
		nfc_read.setOnClickListener(this);
		nfc_exit.setOnClickListener(this);
		//数据库连接初始化
   	    helper = new ResultDBConnection(this);		
	}
	
	public void onClick(View v) {
		switch(v.getId()){
		   case R.id.nfc_open:
			  buffer[3]=0x51;
			  nfc_result.setText("打开");
			  motor.motorLeft();
			  blight.blightRed();
			  matrix.MatrixOpen();
		     	try {
			     	clientSocketThread.getOutputStream().write(buffer);
			    } catch (Exception e) {
			    	e.printStackTrace();
		     	}		
			  break;
		   case R.id.nfc_read:
			  buffer[3]=0x55;
			  nfc_result.setText("读取");
			  motor.motorRight();
			  blight.blightGreen();
		     	try {
			     	clientSocketThread.getOutputStream().write(buffer);
			    } catch (Exception e) {
			    	e.printStackTrace();
		     	}		
			  break;
		   case R.id.nfc_exit:
			   buffer[3]=0x53;
			   motor.motorStop();
			   blight.blightClose();
			   matrix.MatrixClose();
			   try {
				     clientSocketThread.getOutputStream().write(buffer);
			   } catch (Exception e) {
				     e.printStackTrace();
			   }
		       /*返回主界面*/
			   Intent intent = new Intent();
	   	   	   intent.setClass(NfcActivity.this, MainActivity.class); 
	   	       startActivity(intent); 
	   	       break;
    	}
	}
	
	public class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg)
		{
			Bundle bundle = msg.getData();						
			nfc_result.setText(bundle.get("id").toString());

			/*扫码结果*/
	        ContentValues values = new ContentValues();
	        values.put(ResultData.CONTENT,bundle.get("id").toString());
	        
		    /*获取系统时间*/
		    SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日   HH:mm:ss");   
		    Date curDate = new Date(System.currentTimeMillis());
		    String nowData = formatter.format(curDate);
	        values.put(ResultData.DATA,nowData);
	
	        /*写入数据库*/
	        SQLiteDatabase db = helper.getWritableDatabase();
	        db.insert(ResultData.TABLE_NAME, null, values);
	        db.close();
			
	        /*写入一条数据后之间关闭NFC*/
/*			 buffer[3]=0x53;
			 try {
				   clientSocketThread.getOutputStream().write(buffer);
			 } catch (Exception e) {
				   e.printStackTrace();
			 }
*/	        
	        /*控制蜂鸣器和led灯*/
	        ledJniActivity.ledOpen();
	        ledJniActivity.buzzerOpen();
	        for(int i = 0; i < 10000000; i++){}
	        ledJniActivity.ledDown();
	        ledJniActivity.buzzerDown();
		}
	}
	
	/**
	 * 结果数据库
	 * @author xutong
	 *
	 */
	public static class ResultDBConnection extends SQLiteOpenHelper
	{
		private static final String DATABASE_NAME = "ResultsDB";
		private static final int DATABASE_VERSION = 1;
		public ResultDBConnection(Context context) {
			super(context,DATABASE_NAME, null, DATABASE_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = "CREATE TABLE " + ResultData.TABLE_NAME 
		            + " (" + ResultData.ID + " INTEGER primary key autoincrement, "
					+ ResultData.CONTENT + " text not null, "
		            + ResultData.DATA + " text not null, " + ");";
			db.execSQL(sql);
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
	}
}