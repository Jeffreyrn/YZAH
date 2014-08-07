package hm.hm;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.nwpu.msn.nio.codec.CodeMSRRP;

import hm.hm.MyTaskActivity;
import hm.hm.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class MyTaskActivity extends Activity{
	String [] items = {			//´æ·ÅListViewÖÐµÄÑ¡ÏîÄÚÈÝ
			"篮球场场地使用情况","数字化机位占用情况","排球场场地使用情况"	
		};
	String [] rewards={"+10","+5","+7"};
	String [] normal_taskstring={"篮球场是否有空场地？","数字化大楼是否有机位？","排球场是否有空场地？"};
	
	String[] f_name={"","","","",""};
	String[] f_level={"","","","",""};
	
	String strIp="192.9.200.103";
	int iPort=5555;
	
	int len=3;
	
	private static final int PORT=9983;
	private DatagramSocket dataSocket;
	private DatagramPacket dataPacket;
	private byte sendDataByte[];
	private String sendStr;
	
	
	String myposition="1";
	String unotwo = null;
	String uno = null;
	View dialog_view = null;
	ProgressDialog pd = null;	//ProgressDialog¶ÔÏóÒýÓÃ
	public List<? extends Map<String, ?>> generateDataList(){
		ArrayList<Map<String,Object>> list= new ArrayList<Map<String,Object>>();;
		
		for(int i=0;i<len;i++){
			HashMap<String,Object> hmap= new HashMap<String,Object>();
			hmap.put("col2",items[i]);
			hmap.put("col3",rewards[i]);
			list.add(hmap);
		}
		return list;
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();		//»ñµÃÆô¶¯¸ÃActivityµÄIntent
		unotwo = intent.getStringExtra("uno");
		String [] spp=unotwo.split("\\|");
		uno=spp[0];strIp=spp[1];
		String message="friendlist";
		/*
		// Ö¸¶¨¶Ë¿ÚºÅ£¬±ÜÃâÓëÆäËûÓ¦ÓÃ³ÌÐò·¢Éú³åÍ»
		try{
        dataSocket = new DatagramSocket(PORT);
        sendDataByte = new byte[1024];
        sendStr = uno+","+message;
        CodeMSRRP code=new CodeMSRRP();
		code.setHead("40005",uno, 0, message.length());
		code.setContent(message);
        sendDataByte = code.getResult();//sendStr.getBytes();
        dataPacket = new DatagramPacket(sendDataByte, code.getFullLength(),
                InetAddress.getByName(strIp),iPort);//InetAddress.getByName("localhost"), PORT);
        dataSocket.send(dataPacket);
        
        
		DatagramPacket dp_receive = new DatagramPacket(new byte[512], 512);
		dataSocket.setSoTimeout(3000);
		dataSocket.receive(dp_receive);
		System.out.println("received!" + dp_receive.getLength());
		byte[] bb = dp_receive.getData();
		String str = new String(bb, 0, dp_receive.getLength());
		String [] sa = str.split("\\*");
		len=Integer.parseInt(sa[sa.length-1]);
		for(int i=0;i<len;i++){
			String [] sc =sa[i].split("\\|");
		f_name[i]=sc[0];
		f_level[i]=sc[1];
		
		}
        dataSocket.close();
	} catch (SocketException se) {
    	se.printStackTrace();
    	dataSocket.close();
    	
    } catch (IOException ie) {
        ie.printStackTrace();
		dataSocket.close();
		
    }
		*/
		setContentView(R.layout.tasklist);			//ÉèÖÃµ±Ç°ÆÁÄ»
		ListView gvPublish = (ListView)findViewById(R.id.gvPublish);		//»ñµÃListView¶ÔÏóÒýÓÃ
		SimpleAdapter ba=new SimpleAdapter(
				this,
				generateDataList(),
				R.layout.gridrow,
				new String[]{"col2","col3"},
				new int[] {R.id.text1grid,R.id.text2grid}
				);
		gvPublish.setAdapter(ba);
		gvPublish.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				switch(position){			//ÅÐ¶Ïµã»÷µÄÊÇÄÄÒ»¸öÑ¡Ïî
				case 0:				//¸üÐÂÐÄÇé
					LayoutInflater li = LayoutInflater.from(MyTaskActivity.this);
					dialog_view = li.inflate(R.layout.task_normal, null);
					new AlertDialog.Builder(MyTaskActivity.this)
						.setTitle("任务内容")
						.setMessage(normal_taskstring[0])
						.setIcon(R.drawable.normal_task)
						//.setView(dialog_view)
						.setPositiveButton(
							"yes",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									pd = ProgressDialog.show(MyTaskActivity.this, "ÇëÉÔºò", "ÕýÔÚ·¢²¼ÈÎÎñ...",true,true);
									//updateStatus();
								}
							})
						.setNegativeButton(
							"no",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {}
							})
						.show();
					break;
				case 1:				//·¢±íÈÕÖ¾
					LayoutInflater li2 = LayoutInflater.from(MyTaskActivity.this);
					dialog_view = li2.inflate(R.layout.task_normal, null);
					new AlertDialog.Builder(MyTaskActivity.this)
						.setTitle("任务内容")
						.setMessage(normal_taskstring[1])
						.setIcon(R.drawable.normal_task)
						.setView(dialog_view)
						.setPositiveButton(
							"yes",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									pd = ProgressDialog.show(MyTaskActivity.this, "ÇëÉÔºò", "ÕýÔÚ·¢²¼ÈÎÎñ...",true,true);
									//updateStatus();
								}
							})
						.setNegativeButton(
							"no",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {}
							})
						.show();
					break;
				case 2:				//ÅÄÕÕÉÏ´«
					//Intent intent2 = new Intent(MyTaskActivity.this,ShootActivity.class);//´´½¨Intent
					//intent2.putExtra("uno", uno);		//ÉèÖÃIntentµÄExtra×Ö¶Î
					//startActivity(intent2);
					LayoutInflater li3 = LayoutInflater.from(MyTaskActivity.this);
					dialog_view = li3.inflate(R.layout.task_normal, null);
					new AlertDialog.Builder(MyTaskActivity.this)
						.setTitle("任务内容")
						.setMessage(normal_taskstring[2])
						.setIcon(R.drawable.normal_task)
						.setView(dialog_view)
						.setPositiveButton(
							"yes",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									pd = ProgressDialog.show(MyTaskActivity.this, "ÇëÉÔºò", "ÕýÔÚ·¢²¼ÈÎÎñ...",true,true);
									//updateStatus();
								}
							})
						.setNegativeButton(
							"no",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {}
							})
						.show();
					break;
				case 3:				//ÅÄÕÕÉÏ´«
					//Intent intent2 = new Intent(MyTaskActivity.this,ShootActivity.class);//´´½¨Intent
					//intent2.putExtra("uno", uno);		//ÉèÖÃIntentµÄExtra×Ö¶Î
					//startActivity(intent2);
					LayoutInflater li4 = LayoutInflater.from(MyTaskActivity.this);
					dialog_view = li4.inflate(R.layout.task_normal, null);
					new AlertDialog.Builder(MyTaskActivity.this)
						.setTitle("ÈÎÎñÄÚÈÝ")
						.setMessage("test")
						.setIcon(R.drawable.normal_task)
						.setView(dialog_view)
						.setPositiveButton(
							"Íê³É",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									pd = ProgressDialog.show(MyTaskActivity.this, "ÇëÉÔºò", "ÕýÔÚ·¢²¼ÈÎÎñ...",true,true);
									//updateStatus();
								}
							})
						.setNegativeButton(
							"È¡Ïû",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {}
							})
						.show();
					break;
				case 4:				//ÅÄÕÕÉÏ´«
					//Intent intent2 = new Intent(MyTaskActivity.this,ShootActivity.class);//´´½¨Intent
					//intent2.putExtra("uno", uno);		//ÉèÖÃIntentµÄExtra×Ö¶Î
					//startActivity(intent2);
					LayoutInflater li5 = LayoutInflater.from(MyTaskActivity.this);
					dialog_view = li5.inflate(R.layout.task_normal, null);
					new AlertDialog.Builder(MyTaskActivity.this)
						.setTitle("ÈÎÎñÄÚÈÝ")
						.setMessage("test")
						.setIcon(R.drawable.normal_task)
						.setView(dialog_view)
						.setPositiveButton(
							"Íê³É",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									pd = ProgressDialog.show(MyTaskActivity.this, "ÇëÉÔºò", "ÕýÔÚ·¢²¼ÈÎÎñ...",true,true);
									//updateStatus();
								}
							})
						.setNegativeButton(
							"È¡Ïû",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {}
							})
						.show();
					break;
				}
			}
		});
	}
}