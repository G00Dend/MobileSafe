package com.jiangcheng.mobilesafe;
import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jiangcheng.mobilesafe.R;
import com.jiangcheng.mobilesafe.db.dao.NumberAddressQueryUtils;
public class NumberAddressQueryActivity extends Activity {
	private static final String TAG = "NumberAddressQueryActivity";
	private EditText ed_phone;
	private TextView result;
	
	/**
	 * 系统提供的振动服务
	 */
	private Vibrator vibrator;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 		setContentView(R.layout.activity_number_addres_query); 
		ed_phone = (EditText) findViewById(R.id.ed_phone);
		result = (TextView) findViewById(R.id.result);
		
		vibrator=(Vibrator) getSystemService(VIBRATOR_SERVICE);
		ed_phone.addTextChangedListener(new TextWatcher() {
			/*
			 * 文本发生变化时 回调
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.length()>=3){
					//查询数据库
					String address=NumberAddressQueryUtils.queryNumber(s.toString());
					result.setText(address);
				}
			}
			/*
			 * 文本發生變化之前變化(non-Javadoc)
			 * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence, int, int, int)
			 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			/*
			 * 文本發生變化之後回調(non-Javadoc)
			 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
			 */
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});	
		 
		  
	}
	
	/**
	 * 查询号码归属地
	 * @param view
	 */
	public void numberAddressQuery(View view){
		String phone = ed_phone.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(this, "号码为空", 0).show();
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			ed_phone.startAnimation(shake);
			//当电话号码为空的时候就去震动手机提醒用户
			vibrator.vibrate(1000);
		/*	long[] pattern={200,200,300,300,1000,2000};
			vibrator.vibrate(pattern, -1);*/
			
			return;
		}else{
			Toast.makeText(this, "你要查询的电话号码： "+phone, 0).show();
			String address=NumberAddressQueryUtils.queryNumber(phone);
			result.setText(address);
			
			//去数据库中查询
			//1.网络查询  2.本地数据库
			//写一个工具类，查询数据库
			Log.i(TAG,"你要查询的电话号码： "+phone);
		}
		
	}

}
