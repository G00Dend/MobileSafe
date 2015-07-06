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
	 * ҵ�񷽷����ṩ�ֻ����氲װ������Ӧ�ó�����Ϣ
	 * @param context ������
	 * @return 
	 */
	public static List<AppInfo> getAppInfos(Context context){
		PackageManager pm=context.getPackageManager();
		//���еİ�װ��ϵͳ�ϵ�Ӧ�ó������Ϣ
		List<PackageInfo> packInfos=pm.getInstalledPackages(0);
		List<AppInfo> appInfos=new ArrayList<AppInfo>();
		for(PackageInfo packInfo:packInfos){
		AppInfo appInfo=new AppInfo();
		//packInfo  �൱��һ��Ӧ�ó���apk�����嵥�ļ�
		String packname=packInfo.packageName;
		Drawable icon=packInfo.applicationInfo.loadIcon(pm);
		String name=packInfo.applicationInfo.loadLabel(pm).toString();
		int flags=packInfo.applicationInfo.flags; //Ӧ�ó���Ĵ��⿨
		int uid=packInfo.applicationInfo.uid;  //����ϵͳ�����Ӧ��ϵͳһ���̶��ı�ţ�һ��Ӧ�ó���ײ���ֻ���id�͹̶�������
		appInfo.setUid(uid);
		
		if((flags&ApplicationInfo.FLAG_SYSTEM)==0 && !packname.equals("com.jiangcheng.mobilesafe")){
			appInfo.setUserApp(true);
			appInfo.setPackname(packname);
			appInfo.setIcon(icon);
			appInfo.setName(name);
			if((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)==0){
				//�ֻ����ڴ�
				appInfo.setInRom(true);
			}else{
				//�ֻ�����洢�豸
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
