package com.jiangcheng.mobilesafe.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class AutoCleanService extends Service {

	private ScreenOffReceiver receiver;
	private ActivityManager am;
	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}
	
	@Override
	public void onCreate() {
		am=(ActivityManager) getSystemService(ACTIVITY_SERVICE);
		receiver=new ScreenOffReceiver();
		//System.out.println("广播注册成功");
		registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		
		unregisterReceiver(receiver);
		//System.out.println("广播销毁成功");
		receiver=null;
		super.onDestroy();
	}
	
	private class ScreenOffReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			//得到所有的进程
			//System.out.println("广播执行成功");
			List<RunningAppProcessInfo> infos=am.getRunningAppProcesses();
			for(RunningAppProcessInfo info:infos){
				am.killBackgroundProcesses(info.processName);
			}
		}
	}
}
