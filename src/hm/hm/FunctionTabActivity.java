package hm.hm;

import hm.hm.R;
import hm.hm.SearchActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;
import android.widget.TabHost;

public class FunctionTabActivity extends TabActivity {
	
	
	final int LIST_DIALOG_SINGLE=3;
	static final int MENU_SEARCH=0;
    static final int MENU_EXIT=1;
	String uno = null;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final TabHost myTabhost = getTabHost();
        
        Intent intent = getIntent();		//获得启动该Activity的Intent
		uno = intent.getStringExtra("uno");
		
		Intent intentPublish = new Intent(this,hm.hm.MyTaskActivity.class);
		intentPublish.putExtra("uno", uno);
		
		Intent olcampus = new Intent(this,hm.hm.PublishActivity.class);
		olcampus.putExtra("uno", uno);
		
		Intent friendlist = new Intent(this,hm.hm.FriendListActivity.class);
		friendlist.putExtra("uno", uno);		//为将用户id设置为Intent的Extra
		
		Intent myinfointent = new Intent(this,hm.hm.MyInfoActivity.class);
		myinfointent.putExtra("uno", uno);
        
        
        
        
        myTabhost.addTab(
        		myTabhost.newTabSpec("TASK")
        		.setIndicator("TASK",getResources().getDrawable(R.drawable.recieve_task))
        		.setContent(intentPublish)
        		);
        myTabhost.addTab(
        		myTabhost.newTabSpec("CAMPUS")
        		.setIndicator("CAMPUS",getResources().getDrawable(R.drawable.publish_task))
        		.setContent(olcampus)
        		);
        myTabhost.addTab(
        		myTabhost.newTabSpec("PERSONAL")
        		.setIndicator("PERSONAL",getResources().getDrawable(R.drawable.user_info))
        		.setContent(myinfointent)
        		);
        myTabhost.addTab(
        		myTabhost.newTabSpec("FRIENDS")
        		.setIndicator("FRIENDS",getResources().getDrawable(R.drawable.friends_ranking))
        		.setContent(friendlist)
        		);
        
        String tab = intent.getStringExtra("tab");
		if(tab != null){
			myTabhost.setCurrentTabByTag(tab);
		}
	}
	public boolean onCreateOptionsMenu(Menu menu){
    	menu.add(0,MENU_SEARCH,0,"search").setIcon(R.drawable.search);
    	menu.add(0,MENU_EXIT,0,"exit").setIcon(R.drawable.exit);
    	return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    	case MENU_SEARCH:
    		//Intent intent = new Intent(this,SearchActivity.class);
    		//intent.putExtra("vistor", uno);
    		//startActivity(intent);
    		new AlertDialog.Builder(this).setTitle(R.string.classify)
    		.setIcon(R.drawable.header)
    		.setSingleChoiceItems(						//设置单选列表选项
					R.array.msb, 
					0, 
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//EditText et = (EditText)findViewById(R.id.EditText01);
							//et.setText("您选择了："
									//+ getResources().getStringArray(R.array.msa)[which]);
						}
					})
			.setPositiveButton(						//添加一个按钮
			R.string.ok,								//按钮显示的文本
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which){}
			}).show();
    		break;
    	case MENU_EXIT:
    		new AlertDialog.Builder(this).setTitle("exit?")
    		.setMessage("exit?")
    		.setIcon(R.drawable.alert)
    		.setInverseBackgroundForced(false)
    		.setPositiveButton("ok",
    				new DialogInterface.OnClickListener(){
    					public void onClick(DialogInterface dialog, int which){
    						android.os.Process.killProcess(android.os.Process.myPid());
    					}
    				})
    		.setNegativeButton("no",
    				new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int which){}
    				}).show();
    		break;
    	}
    	
    	
    	
    	return super.onOptionsItemSelected(item);
    }
    
    
 
}
