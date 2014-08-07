package hm.hm;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import hm.hm.NotifiedActivity;


import edu.nwpu.msn.nio.codec.CodeMSRRP;

import hm.hm.R;
import hm.hm.R.array;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PublishActivity extends Activity implements OnClickListener{
	
	Button buttons =  null;
	
	TextView textView;
	ProgressDialog myDialog = null;
	ProgressDialog myDialog2 = null;
	final int LIST_DIALOG_SINGLE= 3;
	ProgressDialog myDialog1 = null;
	String uno=null;
	String unotwo=null;
	String gold=null;
	
	String strIp="192.9.200.103";
	int iPort=5555;
	
	private static final int PORT=9988;
	private DatagramSocket dataSocket;
	private DatagramPacket dataPacket;
	private byte sendDataByte[];
	private String sendStr;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publishtask);
        
        Intent intent = getIntent();		//���������Activity��Intent
		unotwo = intent.getStringExtra("uno");
		String[] spp=unotwo.split("\\|");
		uno=spp[0];strIp=spp[1];
        buttons = (Button) this.findViewById(R.id.buttonmap);
        buttons.setOnClickListener(this);
        Button btnDiary = (Button)findViewById(R.id.ol_btnDiary);			//��÷�������ť
        btnDiary.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			showDialog(LIST_DIALOG_SINGLE);
    		}
    	});
        Button btnDiaryBack = (Button)findViewById(R.id.ol_btnDiaryBack);   //���ȡ������ť
        btnDiaryBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        
        //����ظ�����Ϣ��ʾ
        Intent i = new Intent(PublishActivity.this, NotifiedActivity.class);
		PendingIntent pi = PendingIntent.getActivity(PublishActivity.this, 0, i, 0);
		Notification myNotification = new Notification();	//����һ��Notification����
		myNotification.icon=R.drawable.header;				//Notification��ͼ��
		myNotification.tickerText=getResources().getString(R.string.notification);			//
		myNotification.defaults=Notification.DEFAULT_SOUND;
		myNotification.setLatestEventInfo(PublishActivity.this, "Reply From Jim�task title", "time", pi);
		NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		Intent i2 = new Intent(PublishActivity.this, NotifiedActivity.class);
		PendingIntent pi2 = PendingIntent.getActivity(PublishActivity.this, 0, i2, 0);
		Notification myNotification2 = new Notification();	//����һ��Notification����
		myNotification2.icon=R.drawable.header;				//Notification��ͼ��
		myNotification2.tickerText=getResources().getString(R.string.notification);			//
		myNotification2.defaults=Notification.DEFAULT_SOUND;
		myNotification2.setLatestEventInfo(PublishActivity.this, "Reply From Mary�task title", "time", pi2);
		NotificationManager notificationManager2 = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		notificationManager.notify(0, myNotification);		//����Notification
		notificationManager2.notify(1, myNotification2);		//����Notification
        
    }
    protected Dialog onCreateDialog(int id) {			//��дonCreateDialog����
		Dialog dialog = null;					//����һ��Dialog�������ڷ���
		switch(id){			//��id�����ж�
		case LIST_DIALOG_SINGLE:
			Builder b = new AlertDialog.Builder(this);	//����Builder����
			b.setIcon(R.drawable.coins);				//����ͼ��	
			b.setTitle(R.string.coins_title);					//���ñ���
			b.setSingleChoiceItems(						//���õ�ѡ�б�ѡ��
					R.array.msa, 
					0, 
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							gold = getResources().getStringArray(R.array.msa)[which];
							//intentmap.setClass(this,MyMapActivity.class);
							//startActivity(intentmap);
			
									//��д��������ĺ���
									
									EditText etTitle=(EditText)findViewById(R.id.ol_etTitle);
									EditText etDairy=(EditText)findViewById(R.id.ol_etDiary);
									String title= etTitle.getEditableText().toString().trim();
									String diary= etDairy.getEditableText().toString().trim();
									//myDialog1 = ProgressDialog.show(PublishActivity.this, "����", "���ڷ���...",true);
									try{
										String message=title+"|"+diary+"|"+"+"+gold;
										
										// ָ���˿ںţ�����������Ӧ�ó�������ͻ

							            dataSocket = new DatagramSocket(PORT);
							            sendDataByte = new byte[1024];
							            sendStr = uno+","+message;
							            CodeMSRRP code=new CodeMSRRP();
							    		code.setHead("40004",uno, 0, message.length());
							    		code.setContent(message);
							            sendDataByte = code.getResult();//sendStr.getBytes();
							            dataPacket = new DatagramPacket(sendDataByte, code.getFullLength(),
							                    InetAddress.getByName(strIp),iPort);//InetAddress.getByName("localhost"), PORT);
							            dataSocket.send(dataPacket);
//							            
										DatagramPacket dp_receive = new DatagramPacket(new byte[512], 512);

										dataSocket.setSoTimeout(3000);
										dataSocket.receive(dp_receive);
										System.out.println("received!" + dp_receive.getLength());
										byte[] bb = dp_receive.getData();
										String str = new String(bb, 0, dp_receive.getLength());
										dataSocket.close();
										
									} catch (SocketException se) {
							        	se.printStackTrace();
							        	dataSocket.close();
							        	
							        } catch (IOException ie) {
							            ie.printStackTrace();
										dataSocket.close();
										
							        }

									//myDialog1.dismiss();
								
							
						}
					});
			b.setPositiveButton(R.string.normal_task_yes,
					new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which){}});
			dialog = b.create();						//����Dialog����
			dialog.dismiss();
			break;
		default:
			break;
		}
		return dialog;									//�������ɵ�Dialog����
	}
	@Override
	public void onClick(View v) {//ʵ���¼���������
		if(v == buttons){//���µ�һ����ťʱ
			
			
			//intentmap.setClass(this,MyMapActivity.class);
			//startActivity(intentmap);
			myDialog = ProgressDialog.show(PublishActivity.this, "����", "���ڼ���...",true);
			new Thread(){
				public void run(){
					Intent intentmap = new Intent();
					intentmap.setClass(PublishActivity.this, PublishActivity.class);
					startActivityForResult(intentmap, 1);		

					myDialog.dismiss();
				}
			}.start();	
		}
		
		else{//����������ťʱ
			textView.setText("��������" + ((Button)v).getText());
		}
	}
}
