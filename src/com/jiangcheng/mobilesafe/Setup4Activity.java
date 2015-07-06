package com.jiangcheng.mobilesafe;
import com.jiangcheng.mobilesafe.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
public class Setup4Activity extends BaseSetupActivity {
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		
		sp=getSharedPreferences("config", MODE_PRIVATE);
		
	}
	@Override
	public void showNext() {
		Editor editor=sp.edit();
		editor.putBoolean("configed", true);
		editor.commit();
		Intent intent=new Intent(this,LostFindActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
			
	}

	@Override
	public void showPre() {
		Intent intent=new Intent(this,Setup3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
		
	}

}
