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
	 * ����¼�����������ѯ��ҳ��
	 */
	public void numberQuery(View view){
		Intent intentv = new Intent(this,NumberAddressQueryActivity.class);
		startActivity(intentv);
	
	}
	
	/**
	 * ����¼������ű���
	 * @param  context������
	 */
	public void smsBackup(View view){
		pd=new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("���ڱ��ݶ���...");
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
					//Toast.makeText(this, "���ݳɹ�", 0).show();
				runOnUiThread(new Runnable(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(AtoolsActivity.this, "���ݳɹ�", 0).show();
					}
					
				});
				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();				
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
						Toast.makeText(AtoolsActivity.this, "����ʧ��", 0).show(); 
						}
						
					});
				}finally{
					pd.dismiss();
				}
			};
			
		}.start();
		
	}
	
	/**
	 * ����¼������Ż�ԭ
	 * @param view
	 */
	public void smsRestore(View view){
		try {
			SmsUtils.restoreSms(this,false);
		} catch (Exception e) {
			Log.e("AtoolsActivity", "���Żָ�ʧ��!");
		}
		Toast.makeText(this, "���Żָ��ɹ�", Toast.LENGTH_SHORT).show();
	}
	
	

}
