package com.jiangcheng.mobilesafe.service;

import java.util.List;

import com.jiangcheng.mobilesafe.EnterPwdActivity;
import com.jiangcheng.mobilesafe.db.dao.ApplockDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class WatchDogService extends Service {

	private ActivityManager am;
	private boolean flag;
	private ApplockDao dao;
	private InnerReceiver innerReceiver;
	private DataChangeReceiver dataChangeReceiver;
	private String tempStopProtectPackname;
	private List<String> protectPacknames;
	private Intent intent;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	//自定义一个内部类，内部广播接收者来接收程序输入密码后的广播
	private class InnerReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
		//	System.out.println("接收到了密码框确定的事件广播");
			tempStopProtectPackname=intent.getStringExtra("packname");
		}
	}
	
	private class DataChangeReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("广播执行DataChangeReceiver");
			protectPacknames=dao.findAll();
			
		}
		
	}
	
	@Override
	public void onCreate() {
		innerReceiver=new InnerReceiver();
		//注册一个广播
		 registerReceiver(innerReceiver,new IntentFilter("com.jiangcheng.mobilesafe.tempstop"));
		
		 dataChangeReceiver=new DataChangeReceiver();
		 registerReceiver(dataChangeReceiver,new IntentFilter("com.wfc.mobilesafe.applockchange"));
		 
		 dao=new ApplockDao(this);
         am=(ActivityManager) getSystemService(ACTIVITY_SERVICE);
         
         protectPacknames=dao.findAll();
         flag=true;
         
         //当前应用需要保护，蹦出来一个输入密码框界面输入密码界面
	      intent=new Intent(getApplicationContext(),EnterPwdActivity.class);
	     //服务是没有任务栈信息的，在服务开启activity，要指定这个activity运行的任务栈
	     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         
         new Thread(){
			@Override
			public void run() {
				while(flag){
					 List<RunningTaskInfo> infos=am.getRunningTasks(100);
					 String packname=infos.get(0).topActivity.getPackageName();
					 System.out.println("packname:  "+packname);
					// if(dao.find(packname)){
					if(protectPacknames.contains(packname)){
					 //判断这个应用程序是否需要临时停止保护
						 if(packname.equals(tempStopProtectPackname)){
							 
						 }else{
							//设置要保护程序的包名
						     intent.putExtra("packname",packname);
						     startActivity(intent);
						 }
					 }
					 try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		 }.start();
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		flag=false;
		unregisterReceiver(innerReceiver);
		innerReceiver=null;
		unregisterReceiver(dataChangeReceiver);
		dataChangeReceiver=null;
		super.onDestroy();
	}

}
