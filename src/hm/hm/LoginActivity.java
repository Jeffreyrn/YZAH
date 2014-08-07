package hm.hm;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import hm.hm.R;
import hm.hm.FunctionTabActivity;
import edu.nwpu.msn.nio.codec.CodeMSRRP;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity implements OnClickListener{
	private TextView textUserName;
	private TextView textPSD;
	private TextView textIp;
	private EditText editUserName;
	private EditText editPSD;
	private EditText editIp;
	private EditText editPort;
	private Button buttonLogin;
	private Button buttonReg;
	
	//udp
	private static final int PORT=9988;
	private DatagramSocket dataSocket;
	private DatagramPacket dataPacket;
	private byte sendDataByte[];
	private String sendStr;
	static String strIp;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		textUserName=(TextView)findViewById(R.id.textUserName);
		textUserName.setTextColor(getResources().getColor(R.color.white));
		textUserName.setTextSize(20);
		
		
		textPSD=(TextView)findViewById(R.id.textPSD);
		textPSD.setTextColor(getResources().getColor(R.color.white));
		textPSD.setTextSize(20);
		
		
		textIp=(TextView)findViewById(R.id.textIp);
		textIp.setTextColor(getResources().getColor(R.color.white));
		textIp.setTextSize(20);
		textIp.setText("IP and Port Config");
		textIp.setTextColor(R.color.black);
		
		editIp=(EditText)findViewById(R.id.editIp);
		editIp.setText("192.9.200.103");
		
		editPort=(EditText)findViewById(R.id.editPort);
		editPort.setText("5555");
		
		editUserName=(EditText)findViewById(R.id.editUserName);
		editUserName.setText("13689240376");
		
		editPSD=(EditText)findViewById(R.id.editPSD);
		editPSD.setText("123");
		
		buttonLogin=(Button)findViewById(R.id.buttonLogin);
		buttonLogin.setTextSize(20);
		buttonLogin.setOnClickListener(this);
		
		buttonReg=(Button)findViewById(R.id.buttonReg);
		buttonReg.setTextSize(20);
		buttonReg.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String strUserName=editUserName.getText().toString();
		String strPsd=editPSD.getText().toString();
		strIp=editIp.getText().toString();
		int iPort=Integer.parseInt(editPort.getText().toString());
		if(strUserName.equals("")){
			textUserName.setTextColor(getResources().getColor(R.color.red));
			textUserName.setText("账号不能为空！");
			return;
		}else{
			if(strUserName.length()!=11){
				textUserName.setTextColor(getResources().getColor(R.color.red));
				textUserName.setText("账号必须是11位号码！");
				return;
			}
			textUserName.setTextColor(getResources().getColor(R.color.white));
			textUserName.setTextSize(20);
			textUserName.setText(" ");
		}
		if(strPsd.equals("")){
			textPSD.setTextColor(getResources().getColor(R.color.red));
			textPSD.setText("密码不能为空！");
			return;
		}else{
			textPSD.setTextColor(getResources().getColor(R.color.white));
			textPSD.setTextSize(20);
			textPSD.setText(" ");
		}
		System.out.println("Click");
		if(v.getId()==R.id.buttonLogin){
			
			if(true){//validateUser(strUserName,strPsd,strIp,iPort)//平时调试时设置为true
				//System.out.println("ok");
				Intent appGui=new Intent(this,FunctionTabActivity.class);
				appGui.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
				appGui.putExtra("uno", strUserName+"|"+strIp);
				startActivity(appGui);				
			}
		}else if(v.getId()==R.id.buttonReg){
			String strReply=registerUser(strUserName,strPsd,strIp,iPort);
			System.out.println(strReply);
			if("Existed".equals(strReply)){
				textUserName.setTextColor(getResources().getColor(R.color.red));
				textUserName.setText("注册失败，已经存在的ID！");
			}
			else if("OK".equals(strReply)){
				textUserName.setTextColor(getResources().getColor(R.color.white));
				textUserName.setText("注册成功，点击登录进入系统！");
			}			
		}
		
	}
	private String registerUser(String strUserName, String strPSD,
			String strIP, int iPort) {
		// TODO Auto-generated method stub
		
		try {
            // 指定端口号，避免与其他应用程序发生冲突

            dataSocket = new DatagramSocket(PORT);
            sendDataByte = new byte[1024];
            sendStr = strUserName+","+strPSD;
            CodeMSRRP code=new CodeMSRRP();
    		code.setHead("40002",strUserName, 0, strPSD.length());
    		code.setContent(strPSD);
            sendDataByte = code.getResult();//sendStr.getBytes();
            dataPacket = new DatagramPacket(sendDataByte, code.getFullLength(),
                    InetAddress.getByName(strIP),iPort);//InetAddress.getByName("localhost"), PORT);
            dataSocket.send(dataPacket);
//            
			DatagramPacket dp_receive = new DatagramPacket(new byte[512], 512);

			dataSocket.setSoTimeout(3000);
			dataSocket.receive(dp_receive);
			System.out.println("received!" + dp_receive.getLength());
			byte[] bb = dp_receive.getData();
			String str = new String(bb, 0, dp_receive.getLength());
			dataSocket.close();
			return str;
			//System.out.println(str);

        } catch (SocketException se) {
        	se.printStackTrace();
        	dataSocket.close();
        	return "error";
        } catch (IOException ie) {
            ie.printStackTrace();
            textUserName.setTextColor(getResources().getColor(R.color.red));
			textUserName.setText("服务器没有反应！");
			dataSocket.close();
			return "timeout";
        }
	}
	private boolean validateUser(String strUserName,String strPSD,String strIP,int iPort) {
		// TODO Auto-generated method stub
		try {
            // 指定端口号，避免与其他应用程序发生冲突

            dataSocket = new DatagramSocket(PORT);
            sendDataByte = new byte[1024];
            sendStr = strUserName+","+strPSD;
            CodeMSRRP code=new CodeMSRRP();
    		code.setHead("40001",strUserName, 0, strPSD.length());
    		code.setContent(strPSD);
            sendDataByte = code.getResult();//sendStr.getBytes();
            dataPacket = new DatagramPacket(sendDataByte, code.getFullLength(),
                    InetAddress.getByName(strIP),iPort);//InetAddress.getByName("localhost"), PORT);
            dataSocket.send(dataPacket);
//            
			DatagramPacket dp_receive = new DatagramPacket(new byte[512], 512);

			dataSocket.setSoTimeout(3000);
			dataSocket.receive(dp_receive);
			System.out.println("received!" + dp_receive.getLength());
			byte[] bb = dp_receive.getData();
			String str = new String(bb, 0, dp_receive.getLength());
			if(str.equals("OK")){
				dataSocket.close();
				return true;
			}
			else{
				textUserName.setTextColor(getResources().getColor(R.color.red));
				textUserName.setText("用户名或密码错误！");
				dataSocket.close();
				 return false;
			}
			//System.out.println(str);

        } catch (SocketException se) {
        	se.printStackTrace();
        	dataSocket.close();
        	return false;
        } catch (IOException ie) {
            ie.printStackTrace();
            textUserName.setTextColor(getResources().getColor(R.color.red));
			textUserName.setText("服务器没有反应！");
			dataSocket.close();
			return false;
        }
	}
	private String getIp() {
		String localip = null;
		String netip = null;
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface
					.getNetworkInterfaces();
			InetAddress ip = null;
			boolean finded = false;
			while (netInterfaces.hasMoreElements() && !finded) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> address = ni.getInetAddresses();
				while (address.hasMoreElements()) {
					ip = address.nextElement();
					if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
							&& ip.getHostAddress().indexOf(":") == -1) {
						netip = ip.getHostAddress();
						finded = true;
						break;
					} else if (ip.isSiteLocalAddress()
							&& !ip.isLoopbackAddress()
							&& ip.getHostAddress().indexOf(":") == -1) {
						localip = ip.getHostAddress();
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		if (netip != null && !"".equals(netip)) {
			return netip;
		} else {
			return localip;
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			new AlertDialog.Builder(this)

			.setIcon(R.drawable.alarm)

			.setTitle("警告")

			.setMessage("是否要退出应用？")

			.setNegativeButton("取消", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

				}

			})

			.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int whichButton) {

					Intent startMain = new Intent(Intent.ACTION_MAIN);
					startMain.addCategory(Intent.CATEGORY_HOME);
					startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(startMain);
					System.exit(0);
					// finish();
					// System.exit(0);
					// android.os.Process.killProcess(android.os.Process.myPid());

				}

			}).show();

			return true;

		} else {

			return super.onKeyDown(keyCode, event);

		}
	}
	
	private boolean IsIpAvailable(String strIP){
		 
		long[] ip = new long[4];
		int position1 = strIP.indexOf(".");
		int position2 = strIP.indexOf(".", position1 + 1);
		int position3 = strIP.indexOf(".", position2 + 1);
		ip[0] = Long.parseLong(strIP.substring(0, position1));
		ip[1] = Long.parseLong(strIP.substring(position1 + 1, position2));
		ip[2] = Long.parseLong(strIP.substring(position2 + 1, position3));
		ip[3] = Long.parseLong(strIP.substring(position3 + 1));
		
		System.out.println(ip[0]+","+ip[1]+","+ip[2]+","+ip[3]);
		
		if(0<=ip[0]&&ip[0]<=255&&0<=ip[1]&&ip[1]<=255&&0<=ip[2]&&ip[2]<=255&&0<=ip[3]&&ip[3]<=255){
			return true;
		}
		else{
			return false;
		}

	}

}