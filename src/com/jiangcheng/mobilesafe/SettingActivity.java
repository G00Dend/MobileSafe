package com.jiangcheng.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.jiangcheng.mobilesafe.R;
import com.jiangcheng.mobilesafe.service.AddressService;
import com.jiangcheng.mobilesafe.service.CallSmsSafeService;
import com.jiangcheng.mobilesafe.service.WatchDogService;
import com.jiangcheng.mobilesafe.utils.ServiceUtils;
import com.jiangcheng.mobilesafe.widget.SettingClickView;
import com.jiangcheng.mobilesafe.widget.SettingItemView;

public class SettingActivity extends Activity {
 
 //���ÿ����Զ�����
 private SettingItemView siv_update;
 private SharedPreferences sp;
 
 
 //�����Ƿ�����ʾ������
 private SettingItemView siv_show_address;
 private Intent showAddress;
 
 //��������������
 private SettingItemView siv_callsms_safe;
 private Intent callSmsSafeIntent; 
 
 //���ù�������ʾ�򱳾�
 private SettingClickView scv_changebg;
private Editor editor;
 
 @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		showAddress=new Intent(this,AddressService.class);
	    boolean isServiceRunning=ServiceUtils.isServiceRunning(SettingActivity.this, "com.jiangcheng.mobilesafe.service.AddressService");
	    if(isServiceRunning){
	    	//������������Ƿ�����
	    	siv_show_address.setChecked(true);
	    	
	    }else{
	    	siv_show_address.setChecked(false);
	    }
	    
	    
	    
	    boolean iscallSmsServiceRunning = ServiceUtils.isServiceRunning(
				SettingActivity.this,
				"com.jiangcheng.mobilesafe.service.CallSmsSafeService");
		siv_callsms_safe.setChecked(iscallSmsServiceRunning);
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_setting);
	    sp=getSharedPreferences("config", MODE_PRIVATE);
	    editor = sp.edit();
	    initView(); 
	    //�����Ƿ����Զ�����
	    siv_update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
              //�ж��Ƿ�ѡ��
				//�Ѿ����Զ�����
				if(siv_update.isChecked()){
					siv_update.setChecked(false);
					editor.putBoolean("update", false);
				}else{
					//û�д��Զ�����
					siv_update.setChecked(true);
					editor.putBoolean("update", true);
				}
				editor.commit();
			}
		});
	    
	    //���ù�������ʾ�ؼ�   
	    showAddress=new Intent(this,AddressService.class);
	    boolean isServiceRunning=ServiceUtils.isServiceRunning(SettingActivity.this, 
	    		"com.jiangcheng.mobilesafe.service.AddressService");
	    if(isServiceRunning){
	    	//������������Ƿ�����
	    	siv_show_address.setChecked(true);
	    	
	    }else{
	    	siv_show_address.setChecked(false);
	    }
	    
	    
	    siv_show_address.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(siv_show_address.isChecked()){
					//��Ϊ��ѡ��״̬
					siv_show_address.setChecked(false);				
					stopService(showAddress);
					editor.putBoolean("showAddress", false);
				}else{
					//ѡ��״̬
					siv_show_address.setChecked(true);
					startService(showAddress);
					editor.putBoolean("showAddress", true);
				}
				editor.commit();
				
			}
		});
	    
	    /** ��������������  
	  	 */
	    
	    callSmsSafeIntent = new Intent(this, CallSmsSafeService.class);
	    siv_callsms_safe.setOnClickListener(new OnClickListener() {
	  					@Override
	  					public void onClick(View v) {
	  						if (siv_callsms_safe.isChecked()) {
	  							// ��Ϊ��ѡ��״̬
	  							siv_callsms_safe.setChecked(false);
	  							stopService(callSmsSafeIntent);
	  							editor.putBoolean("blankNum", false);
	  						} else {
	  							// ѡ��״̬
	  							siv_callsms_safe.setChecked(true);
	  							startService(callSmsSafeIntent);
	  							editor.putBoolean("blankNum", true);
	  						}
	  						editor.commit();
	  					}
	  				});
	    
	    //���ú�������صı���
	    scv_changebg=(SettingClickView) findViewById(R.id.scv_changebg);
	    scv_changebg.setTitle("��������ʾ����");
	    final String[] items={"��͸��","������","������","������","ƻ����"};
	    int which=sp.getInt("which",0);
		scv_changebg.setDesc(items[which]);
	    scv_changebg.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int dd=sp.getInt("which",0);
				//����һ���Ի���
				AlertDialog.Builder builder=new Builder(SettingActivity.this);
				builder.setTitle("��������ʾ����");
				builder.setSingleChoiceItems(items, dd, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//����ѡ�����
						Editor editor=sp.edit();
						editor.putInt("which", which);
						editor.commit();
						scv_changebg.setDesc(items[which]);
						
						
						//ȡ���Ի���
						dialog.dismiss();
					}
				});
				builder.setNegativeButton("ȡ��", null);
				builder.show();
			}
		});
	    
	}
	
	/**
	 * ��ʼ�����ؼ���״̬
	 */
	private void initView() {
		 siv_update=(SettingItemView) findViewById(R.id.siv_update);
		 siv_callsms_safe = (SettingItemView) findViewById(R.id.siv_callsms_safe);
		 siv_show_address=(SettingItemView) findViewById(R.id.siv_show_address);
		 boolean update = sp.getBoolean("update", false);
		 boolean blankNum = sp.getBoolean("blankNum",false);
		 boolean showAddress = sp.getBoolean("showAddress", false);
		 boolean watchDog = sp.getBoolean("watchDog", false);
		 siv_update.setChecked(update);
		 siv_callsms_safe.setChecked(blankNum);
		 siv_show_address.setChecked(showAddress);
	}	
	
}
