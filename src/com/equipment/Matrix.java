package com.equipment;

import com.socket.ClientSocketThread;
import com.socket.ClientSocketTools;

public class Matrix
{
	private ClientSocketThread clientSocketThread;
	private byte[]buffer={(byte)0xFE,(byte)0xE0,0x08,0x12,0x72,0x00,0x00,0x0A};
	
	/**
	 * ������ʾ�����Ƽ�
	 */
	public void MatrixOpen()
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
	/**
	 * ������ʾ��������
	 */
	public void MatrixOpen2()
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
	/**
	 * �رյ���
	 */
	public void MatrixClose()
	{
		new Thread(new Runnable(){
			public void run() {
				clientSocketThread=ClientSocketThread.getClientSocket(ClientSocketTools.getLocalIpAddress(),6109);				
			}	
		}).start();
		buffer[3]=0x13;
		try {
			clientSocketThread.getOutputStream().write(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}