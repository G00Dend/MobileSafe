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
		//判断一下是否做过设置向导，如果没有做过，就跳转到设置向导页面，如果没有就留在当前页面
		
		boolean configed=sp.getBoolean("configed", false);
		String  safeNumber = sp.getString("safenumber", "****");
		if(configed){
			setContentView(R.layout.activity_lost_find);
			mSafeNumber = (TextView) findViewById(R.id.safe_number);
			mSafeNumber.setText(safeNumber);
		}else{
			//还没有做过设置向导
			Intent intent=new Intent(this,Setup1Activity.class);
			startActivity(intent);
			//关闭当前页面
			finish();
		}
	}
	/*
	 * 重新进入手机防盗设置向导页面
	 */
	public void reEnterSetup(View view){
		//还没有做过设置向导
		Intent intent=new Intent(this,Setup1Activity.class);
		startActivity(intent);
		//关闭当前页面
		finish();
	}
	
	
}
