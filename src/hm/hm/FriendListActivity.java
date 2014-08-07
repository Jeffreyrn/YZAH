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

import hm.hm.FriendListActivity;
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

public class FriendListActivity extends Activity{
	String [] friendlist_name = {			//好友列表名字
			"James","David","Jack  ","Paul  ","Mark  "
		};
	int [] fl_imgIds = {			//头像图片资源
			R.drawable.fl_1,
			R.drawable.fl_2,
			R.drawable.fl_3,
			R.drawable.fl_4,
			R.drawable.fl_5
		};
	int [] fl_leveliconIds = {		//等级图片资源
			R.drawable.l1,
			R.drawable.l2,
			R.drawable.l3,
			R.drawable.l4,
			R.drawable.l5,
			R.drawable.l6,
			R.drawable.l7
		};
	int [] friendlist_level={5,12,35,21,19};//等级
	
	String[] f_name={"James","David","Jack  ","Paul  ","Mark  "};//将在服务器获取，好友列表名字
	String[] f_level={"5","12","35","21","19"};;//将在服务器获取，等级
	
	String [] normal_taskstring={"error"};
	
	int flag,i,j,tem;
	String tempstring,temp;
	int rowCounter=friendlist_name.length;//列表行数
	
	String strIp="192.9.200.103";
	int iPort=5555;
	
	private static final int PORT=9987;
	private DatagramSocket dataSocket;
	private DatagramPacket dataPacket;
	private byte sendDataByte[];
	private String sendStr;
	
	String uno = null;
	String unotwo = null;
	View dialog_view = null;
	ProgressDialog pd = null;	//ProgressDialog对象引用
	public List<? extends Map<String, ?>> generateDataList(){
		ArrayList<Map<String,Object>> list= new ArrayList<Map<String,Object>>();;
		for(i=0;i<rowCounter-1;i++){//根据等级冒泡排序
			flag=1;
			for(j = 0; j < rowCounter-i-1; j++)
			{
				if(Integer.parseInt(f_level[j]) >Integer.parseInt( f_level[j+1]))
				{
					temp = f_level[j];
					f_level[j] = f_level[j+1];
					f_level[j+1] = temp;
					/*tem = fl_imgIds[j];
					fl_imgIds[j] = fl_imgIds[j+1];
					fl_imgIds[j+1] = tem;*/
					tempstring = f_name[j];
					f_name[j] = f_name[j+1];
					f_name[j+1] = tempstring;
					flag = 0;
				}
			}
			if(1 == flag)
			break;
		}
		for(int i=rowCounter-1;i>-1;i--){//
			HashMap<String,Object> hmap= new HashMap<String,Object>();
			hmap.put("col1",fl_leveliconIds[rowCounter-1-i]);
			hmap.put("col2",fl_imgIds[i]);
			hmap.put("col3",f_name[i]);//riendlist
			hmap.put("col4", f_level[i]);//riendlist
			list.add(hmap);
		}
		return list;
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();		//获得启动该Activity的Intent
		/*unotwo = intent.getStringExtra("uno");
		String [] spp=unotwo.split("\\|");
		uno=spp[0];strIp=spp[1];
		
		try{
			String message="friendlist";
			
			//与服务端通信 指定端口号，避免与其他应用程序发生冲突

            dataSocket = new DatagramSocket(PORT);
            sendDataByte = new byte[1024];
            sendStr = uno+","+message;
            CodeMSRRP code=new CodeMSRRP();
    		code.setHead("40003",uno, 0, message.length());
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
			
			for(int i=0;i<5;i++){
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
			
        }*/
		
		setContentView(R.layout.myfriendlist);			//设置当前屏幕
		ListView gvPublish = (ListView)findViewById(R.id.gv2);		//获得ListView对象引用
		SimpleAdapter ba=new SimpleAdapter(
				this,
				generateDataList(),
				R.layout.friendlistrow,
				new String[]{"col1","col2","col3","col4"},
				new int[] {R.id.imagegrid2,R.id.image2grid2,R.id.text1grid2,R.id.text2grid2}
				);
		gvPublish.setAdapter(ba);
		
	}
}