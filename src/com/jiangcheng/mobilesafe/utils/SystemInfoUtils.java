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
 * ϵͳ��Ϣ�Ĺ�����
 */
public class SystemInfoUtils {

	/*
	 * ��ȡ�������еĽ��̵�����
	 */
	//PackageManager  �����������൱�ڳ������������̬������
	//ActivityManager ���̹����� �����ֻ�������Ϣ����̬������
	public static int getRunningProcessCount(Context context){
		
		ActivityManager am=(ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> infos=am.getRunningAppProcesses();
		return infos.size();
		
	}
	/*
	 * �õ�ϵͳ�Ŀ����ڴ�
	 */
	
	public static long getAvailMem(Context context){
		ActivityManager am=(ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
	    MemoryInfo outInfo=new MemoryInfo();
	    am.getMemoryInfo(outInfo);
		return outInfo.availMem;
	}
	
	
	/*
	 * �õ�ϵͳ��ȫ���ڴ�
	 */
	
	public static long getTotalMem(Context context){
	/*ֻ����4.0�����ϰ汾����
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
