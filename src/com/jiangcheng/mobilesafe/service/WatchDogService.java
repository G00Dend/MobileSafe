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
	//�Զ���һ���ڲ��࣬�ڲ��㲥�����������ճ������������Ĺ㲥
	private class InnerReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
		//	System.out.println("���յ��������ȷ�����¼��㲥");
			tempStopProtectPackname=intent.getStringExtra("packname");
		}
	}
	
	private class DataChangeReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("�㲥ִ��DataChangeReceiver");
			protectPacknames=dao.findAll();
			
		}
		
	}
	
	@Override
	public void onCreate() {
		innerReceiver=new InnerReceiver();
		//ע��һ���㲥
		 registerReceiver(innerReceiver,new IntentFilter("com.jiangcheng.mobilesafe.tempstop"));
		
		 dataChangeReceiver=new DataChangeReceiver();
		 registerReceiver(dataChangeReceiver,new IntentFilter("com.wfc.mobilesafe.applockchange"));
		 
		 dao=new ApplockDao(this);
         am=(ActivityManager) getSystemService(ACTIVITY_SERVICE);
         
         protectPacknames=dao.findAll();
         flag=true;
         
         //��ǰӦ����Ҫ�������ĳ���һ�������������������������
	      intent=new Intent(getApplicationContext(),EnterPwdActivity.class);
	     //������û������ջ��Ϣ�ģ��ڷ�����activity��Ҫָ�����activity���е�����ջ
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
					 //�ж����Ӧ�ó����Ƿ���Ҫ��ʱֹͣ����
						 if(packname.equals(tempStopProtectPackname)){
							 
						 }else{
							//����Ҫ��������İ���
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
