package com.ndk.ledjni;

import android.app.Activity;
import android.os.Bundle;

public class LedJniActivity extends Activity
{
	private static int fd;
	
	public void OnCreated(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}  
	
    /*添加函数声明,告诉编译和链接器该方法在本地代码中实现 */
    public native static int LedDeviceOpen(String path);
    public native static void LedDeviceIoctl(int cmd, int arg);
    public native void LedDeviceClose();
    
    /*加载JNI代码编译生成的共享库*/
	static {
		System.loadLibrary("led-jni");
	}
	
	/**控制led灯开启*/
	public void ledOpen()  
	{
        String path = new String("/dev/ledtest");
		fd = LedDeviceOpen(path);	
		LedDeviceIoctl(1, 0);   
		LedDeviceIoctl(1, 1);
		LedDeviceIoctl(1, 2);
		LedDeviceIoctl(1, 3);		
	}
	/**控制led灯关闭*/
	public void ledDown()  
	{
        String path = new String("/dev/ledtest");
		fd = LedDeviceOpen(path);
		LedDeviceIoctl(0, 0);
		LedDeviceIoctl(0, 1);
		LedDeviceIoctl(0, 2);
		LedDeviceIoctl(0, 3); 	
	}
	/**控制蜂鸣器响*/
	public void buzzerOpen() 
	{
        String path = new String("/dev/ledtest");
		fd = LedDeviceOpen(path);	
		LedDeviceIoctl(0, 4);  
	}
	/**控制蜂鸣器关闭*/
	public void buzzerDown()
	{
        String path = new String("/dev/ledtest");
		fd = LedDeviceOpen(path);	
		LedDeviceIoctl(1, 4);  
	}
}