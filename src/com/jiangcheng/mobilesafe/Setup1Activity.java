package com.jiangcheng.mobilesafe;
import com.jiangcheng.mobilesafe.R;

import android.content.Intent;
import android.os.Bundle;
public class Setup1Activity extends BaseSetupActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
	    
		
 	}

	@Override
	public void showNext() {
		Intent intent=new Intent(this,Setup2Activity.class);
		startActivity(intent);
		finish();
		//要求在finish()或者在startaActivity(intent);后面执行都可以
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
			
	}



	@Override
	public void showPre() {
		// TODO Auto-generated method stub
		
	}
	

}
