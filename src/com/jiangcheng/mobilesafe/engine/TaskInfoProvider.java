package com.jiangcheng.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;

import com.jiangcheng.mobilesafe.R;
import com.jiangcheng.mobilesafe.domain.TaskInfo;

/*
 * 提供手机里面的进程信息
 */
public class TaskInfoProvider {
/*
 * 获取所有进程的信息
 */
	public static List<TaskInfo> getTaskInfos(Context context){
		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm=context.getPackageManager();
		//得到进程信息
		List<RunningAppProcessInfo> processInfos=am.getRunningAppProcesses();
	    List<TaskInfo> taskInfs=new ArrayList<TaskInfo>();
		for(RunningAppProcessInfo processInfo:processInfos){
			TaskInfo taskInfo=new TaskInfo();
			//应用程序的包名
			String packname=processInfo.processName;
			//设置包名
			taskInfo.setPackname(packname);
			//内存的相关信息
			MemoryInfo[] memoryInfos=am.getProcessMemoryInfo(new int[]{processInfo.pid});
			long memsize=memoryInfos[0].getTotalPrivateDirty()*1024;
			taskInfo.setMemsize(memsize);
			try {
				ApplicationInfo applicationInfo=pm.getApplicationInfo(packname, 0);
		        Drawable icon=applicationInfo.loadIcon(pm);
		        //设置图标
		        taskInfo.setIcon(icon);
		        String name=applicationInfo.loadLabel(pm).toString();
		        //设置名字
		        taskInfo.setName(name);
		        if((applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0){
		        	//用户进程
		        	taskInfo.setUserTask(true);
		        }else{
		        	//系统进程
		        	taskInfo.setUserTask(false);
		        }
				
			
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				taskInfo.setIcon(context.getResources().getDrawable(R.drawable.ic_default));
				taskInfo.setName(packname);
				
			}
			taskInfs.add(taskInfo);
		}
		return taskInfs;
	}
}
