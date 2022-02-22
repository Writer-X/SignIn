package com.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity 
{
	private Button complete;
	private EditText registerUsr,registerPassword,registerTwicePassword;
	private OnClickListener listener_complete = null; 
	private SQLiteDatabase db;
	private TextView usr_register,password_register,password_twice_register,welcome_register;
	
	static DBConnection helper;
	
	public interface UserSchema
	{
		String TABLE_NAME = "Users";
		String ID = "_id";
		String USER_NAME = "user_name";  
		String PASSWORD = "password";   
	}
    public String[] FROM = 
    {
        UserSchema.ID,
        UserSchema.USER_NAME,
        UserSchema.PASSWORD
    };
    public void onCreate(final Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        init();
        
        listener_complete = new OnClickListener()
        {
        	public void onClick(View v)
        	{
            	registerUsr = (EditText) findViewById(R.id.register_usr);
            	registerPassword = (EditText) findViewById(R.id.register_password);
            	registerTwicePassword = (EditText) findViewById(R.id.register_password_twice); 
            	
            	Cursor cursor = db.query("Users",FROM, "user_name='" + registerUsr.getText().toString() + "'", null, null, null, null);
            	
            	if(cursor.moveToFirst() == true)
            	{
            		Toast.makeText(RegisterActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
            	}
            	else
            	{
        		    if(registerPassword.getText().toString().equals(registerTwicePassword.getText().toString() ))
        		    {
        	             /*向数据库中写入数据*/
        		         ContentValues values = new ContentValues();
        		         values.put(UserSchema.USER_NAME,registerUsr.getText().toString());
        		         values.put(UserSchema.PASSWORD,registerPassword.getText().toString());
        		
        		         SQLiteDatabase db = helper.getWritableDatabase();
        		         db.insert(UserSchema.TABLE_NAME, null, values);
        		         db.close();
        		     
             		     /*跳转回登录界面*/
     		     	     Intent intent = new Intent();
        	   	         intent.setClass(RegisterActivity.this,LoginActivity.class); 
        	             startActivity(intent);
        	             
        	    
        	        }
        		    else
        		    {
        		        Toast.makeText(RegisterActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
        	     	}
             	}
            	onCreate(savedInstanceState);
        	}
        };
        complete.setOnClickListener(listener_complete);         
    }
    
    private void init()
    {
    	usr_register = (TextView)findViewById(R.id.usr_register);
    	password_register = (TextView)findViewById(R.id.password_register);
    	password_twice_register = (TextView)findViewById(R.id.password_twice_register);
    	welcome_register = (TextView) findViewById(R.id.welcome_register);
    	
    	welcome_register.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/MeltFuji.ttf"));
    	usr_register.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/MeltFuji.ttf"));
    	password_register.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/MeltFuji.ttf"));
    	password_twice_register.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/MeltFuji.ttf"));
    	
    	complete = (Button) findViewById(R.id.button_complete);
    	
    	helper = new DBConnection(this);
        db = helper.getWritableDatabase();
    }
    
	public static class DBConnection extends SQLiteOpenHelper
	{
		private static final String DATABASE_NAME = "UsersDB";
		private static final int DATABASE_VERSION = 1;
		private DBConnection(Context ctx){
			super(ctx,DATABASE_NAME, null, DATABASE_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = "CREATE TABLE " + UserSchema.TABLE_NAME + " ("
					+ UserSchema.ID + " INTEGER primary key autoincrement, "
					+ UserSchema.USER_NAME + " text not null, "
					+ UserSchema.PASSWORD + " text not null "+ ");";
			db.execSQL(sql);
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}
}