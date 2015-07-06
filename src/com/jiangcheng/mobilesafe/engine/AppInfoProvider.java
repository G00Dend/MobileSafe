package com.jiangcheng.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.jiangcheng.mobilesafe.domain.AppInfo;

public class AppInfoProvider {
	/*
	 * 业务方法，提供手机里面安装的所有应用程序信息
	 * @param context 上下文
	 * @return 
	 */
	public static List<AppInfo> getAppInfos(Context context){
		PackageManager pm=context.getPackageManager();
		//所有的安装在系统上的应用程序包信息
		List<PackageInfo> packInfos=pm.getInstalledPackages(0);
		List<AppInfo> appInfos=new ArrayList<AppInfo>();
		for(PackageInfo packInfo:packInfos){
		AppInfo appInfo=new AppInfo();
		//packInfo  相当于一个应用程序apk包的清单文件
		String packname=packInfo.packageName;
		Drawable icon=packInfo.applicationInfo.loadIcon(pm);
		String name=packInfo.applicationInfo.loadLabel(pm).toString();
		int flags=packInfo.applicationInfo.flags; //应用程序的答题卡
		int uid=packInfo.applicationInfo.uid;  //操作系统分配给应用系统一个固定的编号，一旦应用程序被撞倒手机，id就固定不变了
		appInfo.setUid(uid);
		
		if((flags&ApplicationInfo.FLAG_SYSTEM)==0 && !packname.equals("com.jiangcheng.mobilesafe")){
			appInfo.setUserApp(true);
			appInfo.setPackname(packname);
			appInfo.setIcon(icon);
			appInfo.setName(name);
			if((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)==0){
				//手机的内存
				appInfo.setInRom(true);
			}else{
				//手机的外存储设备
				appInfo.setInRom(false);
			}
			appInfos.add(appInfo);
		}else{
			appInfo.setUserApp(false);
		  }	
		}
		return appInfos;
	}
}
