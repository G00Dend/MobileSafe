package com.jiangcheng.mobilesafe;

import com.jiangcheng.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

public class LostFindActivity extends Activity {
	
	private SharedPreferences sp; 
	private TextView  mSafeNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sp=getSharedPreferences("config", MODE_PRIVATE);
		//�ж�һ���Ƿ����������򵼣����û������������ת��������ҳ�棬���û�о����ڵ�ǰҳ��
		
		boolean configed=sp.getBoolean("configed", false);
		String  safeNumber = sp.getString("safenumber", "****");
		if(configed){
			setContentView(R.layout.activity_lost_find);
			mSafeNumber = (TextView) findViewById(R.id.safe_number);
			mSafeNumber.setText(safeNumber);
		}else{
			//��û������������
			Intent intent=new Intent(this,Setup1Activity.class);
			startActivity(intent);
			//�رյ�ǰҳ��
			finish();
		}
	}
	/*
	 * ���½����ֻ�����������ҳ��
	 */
	public void reEnterSetup(View view){
		//��û������������
		Intent intent=new Intent(this,Setup1Activity.class);
		startActivity(intent);
		//�رյ�ǰҳ��
		finish();
	}
	
	
}
