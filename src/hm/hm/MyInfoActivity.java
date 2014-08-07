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

import hm.hm.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MyInfoActivity extends Activity{
	String [] userinfoitem_name = {			//´æ·ÅListViewÖÐµÄÑ¡ÏîÄÚÈÝ
			"LEVEL          ",
			"GOLD           ",
			"TASK           "	
		};
	String [] userinfoitem_nums = {
			"12",
			"45",
			"34"
		};
	
	String [] tasktitle = {			//´æ·ÅListViewÖÐµÄÑ¡ÏîÄÚÈÝ
			"篮球场场地使用",
			"数字化大楼使用",	
		};
	String [] goldget = {
			"+12",
			"+20",
		};
	String [] taskdate = {			//´æ·ÅListViewÖÐµÄÑ¡ÏîÄÚÈÝ
			"2011-05-15",
			"2011-05-20",	
		};
	
	
	int iPort=5555;
	String strIp="192.9.200.103";
	private static final int PORT=9987;
	private DatagramSocket dataSocket;
	private DatagramPacket dataPacket;
	private byte sendDataByte[];
	private String sendStr;
	
	int rowCounter=userinfoitem_name.length;
	int rowCounter2=tasktitle.length;
	View dialog_view = null;
	String uno = null;
	String unotwo = null;
	ProgressDialog pd = null;	//ProgressDialog¶ÔÏóÒýÓÃ
	
	public List<? extends Map<String, ?>> generateDataList(){
		ArrayList<Map<String,Object>> list= new ArrayList<Map<String,Object>>();;
		
		for(int i=0;i<rowCounter;i++){
			HashMap<String,Object> hmap= new HashMap<String,Object>();
			hmap.put("col1",userinfoitem_name[i]);
			hmap.put("col2",userinfoitem_nums[i]);
			list.add(hmap);
		}
		return list;
	}
	
	public List<? extends Map<String, ?>> generateDataList2(){
		ArrayList<Map<String,Object>> list2= new ArrayList<Map<String,Object>>();;
		
		for(int i=0;i<rowCounter2;i++){
			HashMap<String,Object> hmap2= new HashMap<String,Object>();
			hmap2.put("col1",tasktitle[i]);
			hmap2.put("col2",goldget[i]);
			hmap2.put("col3",taskdate[i]);
			list2.add(hmap2);
		}
		return list2;
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();		//»ñµÃÆô¶¯¸ÃActivityµÄIntent
		unotwo = intent.getStringExtra("uno");
		String [] spp=unotwo.split("\\|");
		uno=spp[0];strIp=spp[1];
		/*try{
			String message="friendlist";
			
			// Ö¸¶¨¶Ë¿ÚºÅ£¬±ÜÃâÓëÆäËûÓ¦ÓÃ³ÌÐò·¢Éú³åÍ»

            dataSocket = new DatagramSocket(PORT);
            sendDataByte = new byte[1024];
            sendStr = uno+","+message;
            CodeMSRRP code=new CodeMSRRP();
    		code.setHead("40008",uno, 0, message.length());
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
			String [] sa = str.split("\\|");
			
			for(int i=0;i<3;i++){
				userinfoitem_nums[i]=sa[i];
			
			}
            dataSocket.close();
		} catch (SocketException se) {
        	se.printStackTrace();
        	dataSocket.close();
        	
        } catch (IOException ie) {
            ie.printStackTrace();
			dataSocket.close();
			
        }*/
		
		setContentView(R.layout.myinfoview);			//ÉèÖÃµ±Ç°ÆÁÄ»
		ListView lv_myinf1 = (ListView)findViewById(R.id.userinfo_list1);		//»ñµÃListView¶ÔÏóÒýÓÃ
		SimpleAdapter ba=new SimpleAdapter(
				this,
				generateDataList(),
				R.layout.userinfolistrow1,
				new String[]{"col1","col2"},
				new int[] {R.id.userinfo_text1,R.id.userinfo_text2}
				);
		ImageView mi_tempiv=(ImageView)findViewById(R.id.user_headimage);
		mi_tempiv.setImageResource(R.drawable.defaut_userhead);
		lv_myinf1.setAdapter(ba);
		
		ListView lv_myinf2 = (ListView)findViewById(R.id.userinfo_list2);
		SimpleAdapter ba2=new SimpleAdapter(
				this,
				generateDataList2(),
				R.layout.userinfolistrow2,
				new String[]{"col1","col2","col3"},
				new int[] {R.id.userinfo_text3,R.id.userinfo_text4,R.id.userinfo_text5}
				);
		lv_myinf2.setAdapter(ba2);
	}
}
