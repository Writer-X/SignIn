package com.equipment;

import com.socket.ClientSocketThread;
import com.socket.ClientSocketTools;

public class Motor
{
	private ClientSocketThread clientSocketThread;
	private byte[]buffer={(byte)0xFE,(byte)0xE0,0x08,0x32,0x72,0x00,0x02,0x0A};
	/**
	 * 步电机左转函数
	 */
	public void motorLeft()
	{
		new Thread(new Runnable(){
			public void run() {
				clientSocketThread=ClientSocketThread.getClientSocket(ClientSocketTools.getLocalIpAddress(),6109);				
			}	
		}).start();
		buffer[6]=0x03;
		try {
			clientSocketThread.getOutputStream().write(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 步电机右转函数
	 */
	public void motorRight()
	{
		new Thread(new Runnable(){
			public void run() {
				clientSocketThread=ClientSocketThread.getClientSocket(ClientSocketTools.getLocalIpAddress(),6109);				
			}	
		}).start();
		buffer[6]=0x02;
		try {
			clientSocketThread.getOutputStream().write(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 步电机停止函数
	 */
	public void motorStop()
	{
		new Thread(new Runnable(){
			public void run() {
				clientSocketThread=ClientSocketThread.getClientSocket(ClientSocketTools.getLocalIpAddress(),6109);				
			}	
		}).start();
		buffer[6]=0x01;
		try {
			clientSocketThread.getOutputStream().write(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}