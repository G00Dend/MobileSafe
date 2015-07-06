package com.jiangcheng.mobilesafe;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jiangcheng.mobilesafe.R;
import com.jiangcheng.mobilesafe.utils.SmsUtils;
import com.jiangcheng.mobilesafe.utils.SmsUtils.BackUpCallBack;

public class AtoolsActivity extends Activity {
	
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
	}
	
	/*
	 * 点击事件，进入号码查询的页面
	 */
	public void numberQuery(View view){
		Intent intentv = new Intent(this,NumberAddressQueryActivity.class);
		startActivity(intentv);
	
	}
	
	/**
	 * 点击事件，短信备份
	 * @param  context上下文
	 */
	public void smsBackup(View view){
		pd=new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在备份短信...");
		pd.show();
		new Thread(){
			public void run(){
				try {
					SmsUtils.backupSms(AtoolsActivity.this,new BackUpCallBack(){

						@Override
						public void beforeBackup(int max) {
							pd.setMax(max);						
						}

						@Override
						public void onSmsBackup(int progress) {
							pd.setProgress(progress);
							//progressBar1.setProgress(progress);
						}
						
					});
					//Toast.makeText(this, "备份成功", 0).show();
				runOnUiThread(new Runnable(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(AtoolsActivity.this, "备份成功", 0).show();
					}
					
				});
				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();				
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
						Toast.makeText(AtoolsActivity.this, "备份失败", 0).show(); 
						}
						
					});
				}finally{
					pd.dismiss();
				}
			};
			
		}.start();
		
	}
	
	/**
	 * 点击事件，短信还原
	 * @param view
	 */
	public void smsRestore(View view){
		try {
			SmsUtils.restoreSms(this,false);
		} catch (Exception e) {
			Log.e("AtoolsActivity", "短信恢复失败!");
		}
		Toast.makeText(this, "短信恢复成功", Toast.LENGTH_SHORT).show();
	}
	
	

}
