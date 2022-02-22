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
	
    /*��Ӻ�������,���߱�����������÷����ڱ��ش�����ʵ�� */
    public native static int LedDeviceOpen(String path);
    public native static void LedDeviceIoctl(int cmd, int arg);
    public native void LedDeviceClose();
    
    /*����JNI����������ɵĹ����*/
	static {
		System.loadLibrary("led-jni");
	}
	
	/**����led�ƿ���*/
	public void ledOpen()  
	{
        String path = new String("/dev/ledtest");
		fd = LedDeviceOpen(path);	
		LedDeviceIoctl(1, 0);   
		LedDeviceIoctl(1, 1);
		LedDeviceIoctl(1, 2);
		LedDeviceIoctl(1, 3);		
	}
	/**����led�ƹر�*/
	public void ledDown()  
	{
        String path = new String("/dev/ledtest");
		fd = LedDeviceOpen(path);
		LedDeviceIoctl(0, 0);
		LedDeviceIoctl(0, 1);
		LedDeviceIoctl(0, 2);
		LedDeviceIoctl(0, 3); 	
	}
	/**���Ʒ�������*/
	public void buzzerOpen() 
	{
        String path = new String("/dev/ledtest");
		fd = LedDeviceOpen(path);	
		LedDeviceIoctl(0, 4);  
	}
	/**���Ʒ������ر�*/
	public void buzzerDown()
	{
        String path = new String("/dev/ledtest");
		fd = LedDeviceOpen(path);	
		LedDeviceIoctl(1, 4);  
	}
}