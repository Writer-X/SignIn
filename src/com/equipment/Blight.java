package com.equipment;

import com.socket.ClientSocketThread;
import com.socket.ClientSocketTools;

public class Blight
{
	private ClientSocketThread clientSocketThread;
	private byte[]buffer={(byte)0xFE,(byte)0xE0,0x08,0x44,0x72,0x00,0x12,0x0A};
	
	/**
	 * ��ͨ�ƻƵ�������
	 */
	public void blightOpen()
	{
		new Thread(new Runnable(){
			public void run() {
				clientSocketThread=ClientSocketThread.getClientSocket(ClientSocketTools.getLocalIpAddress(),6109);				
			}	
		}).start();
		buffer[6]=0x12;
		try {
			clientSocketThread.getOutputStream().write(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ͨ�ƺ��������
	 */
	public void blightRed()
	{
		new Thread(new Runnable(){
			public void run() {
				clientSocketThread=ClientSocketThread.getClientSocket(ClientSocketTools.getLocalIpAddress(),6109);				
			}	
		}).start();
		buffer[6]=0x09;
		try {
			clientSocketThread.getOutputStream().write(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ͨ���̵�������
	 */
	public void blightGreen()
	{
		new Thread(new Runnable(){
			public void run() {
				clientSocketThread=ClientSocketThread.getClientSocket(ClientSocketTools.getLocalIpAddress(),6109);				
			}	
		}).start();
		buffer[6]=0x24;
		try {
			clientSocketThread.getOutputStream().write(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ͨ�ƹغ���
	 */
	public void blightClose()
	{
		new Thread(new Runnable(){
			public void run() {
				clientSocketThread=ClientSocketThread.getClientSocket(ClientSocketTools.getLocalIpAddress(),6109);				
			}	
		}).start();
		buffer[6]=0x00;
		try {
			clientSocketThread.getOutputStream().write(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}