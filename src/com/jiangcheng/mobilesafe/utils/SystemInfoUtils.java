package com.jiangcheng.mobilesafe.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

/*
 * 系统信息的工具类
 */
public class SystemInfoUtils {

	/*
	 * 获取正在运行的进程的数量
	 */
	//PackageManager  包管理器，相当于程序管理器：静态的内容
	//ActivityManager 进程管理器 管理手机里面活动信息，动态的内容
	public static int getRunningProcessCount(Context context){
		
		ActivityManager am=(ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> infos=am.getRunningAppProcesses();
		return infos.size();
		
	}
	/*
	 * 得到系统的可用内存
	 */
	
	public static long getAvailMem(Context context){
		ActivityManager am=(ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
	    MemoryInfo outInfo=new MemoryInfo();
	    am.getMemoryInfo(outInfo);
		return outInfo.availMem;
	}
	
	
	/*
	 * 得到系统的全部内存
	 */
	
	public static long getTotalMem(Context context){
	/*只能在4.0及以上版本可用
		ActivityManager am=(ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
	    MemoryInfo outInfo=new MemoryInfo();
	    am.getMemoryInfo(outInfo);
		return outInfo.totalMem;
	*/
		
		try {
			File file=new File("/proc/meminfo");
			FileInputStream fis=new FileInputStream(file);
			BufferedReader br=new BufferedReader(new InputStreamReader(fis));
			String line=br.readLine();
			
			StringBuilder sb=new StringBuilder();
			for(char c:line.toCharArray()){
				if(c>='0'&&c<='9'){
					sb.append(c);
				}
			}
			return Long.parseLong(sb.toString())*1024;
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	
	}
	
	
	
}
