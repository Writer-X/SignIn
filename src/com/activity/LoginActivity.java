package com.activity;

import com.activity.RegisterActivity.UserSchema;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
    /** Called when the activity is first created. */
	private Button mButton_login, mButton_register;
	private EditText username_login,password_login;
	private SQLiteDatabase db;
	private TextView usr,password,welcome;
	
	static DBConnection helper;
	
    public String[] FROM = 
        {
        		UserSchema.ID,
        		UserSchema.USER_NAME,
        		UserSchema.PASSWORD
        };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        init();
        
        mButton_login.setOnClickListener(new View.OnClickListener() {  
			public void onClick(View v) {			
				username_login = (EditText) findViewById(R.id.username_login);
				password_login =(EditText) findViewById(R.id.password_login);
				String username_input = username_login.getText().toString();
				String password_input = password_login.getText().toString();
				
				Cursor cursor = db.query("Users",FROM, "user_name='" + username_input + "'", null, null, null, null);
				
				if(cursor.moveToFirst() == true)
				{
					String query_password = cursor.getString(1);
					if(query_password.equals(password_input))
					{
						cursor.close();
			     	    Intent intent = new Intent();
	   	   	    	    intent.setClass(LoginActivity.this, MainActivity.class); 
	   	                startActivity(intent);
					}
					else
					{
						cursor.close();
						Toast.makeText(LoginActivity.this, "√‹¬Î¥ÌŒÛ", Toast.LENGTH_SHORT).show();
					}
				}
				else 
				{
					 cursor.close();
					 Toast.makeText(LoginActivity.this, "’À∫≈Œ¥◊¢≤·", Toast.LENGTH_SHORT).show();
				}
			}
        });
	    mButton_register.setOnClickListener(new View.OnClickListener() {  
				public void onClick(View v) {				
					Intent intent = new Intent();
		   	   		intent.setClass(LoginActivity.this, RegisterActivity.class); 
		   	        startActivity(intent); 
				}
        });
    }
    
    
    private void init()
    {
    	usr = (TextView)findViewById(R.id.usr);
    	password = (TextView)findViewById(R.id.password);
    	welcome = (TextView)findViewById(R.id.welcome);
    	
    	welcome.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/MeltFuji.ttf"));
    	usr.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/MeltFuji.ttf"));
    	password.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/MeltFuji.ttf"));
    	
        mButton_login = (Button)findViewById(R.id.button_login);
        mButton_register = (Button)findViewById(R.id.button_register);
        
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