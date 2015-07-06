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
	 * ϵͳ�ṩ���񶯷���
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
			 * �ı������仯ʱ �ص�
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.length()>=3){
					//��ѯ���ݿ�
					String address=NumberAddressQueryUtils.queryNumber(s.toString());
					result.setText(address);
				}
			}
			/*
			 * �ı��l��׃��֮ǰ׃��(non-Javadoc)
			 * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence, int, int, int)
			 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			/*
			 * �ı��l��׃��֮����{(non-Javadoc)
			 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
			 */
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});	
		 
		  
	}
	
	/**
	 * ��ѯ���������
	 * @param view
	 */
	public void numberAddressQuery(View view){
		String phone = ed_phone.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(this, "����Ϊ��", 0).show();
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			ed_phone.startAnimation(shake);
			//���绰����Ϊ�յ�ʱ���ȥ���ֻ������û�
			vibrator.vibrate(1000);
		/*	long[] pattern={200,200,300,300,1000,2000};
			vibrator.vibrate(pattern, -1);*/
			
			return;
		}else{
			Toast.makeText(this, "��Ҫ��ѯ�ĵ绰���룺 "+phone, 0).show();
			String address=NumberAddressQueryUtils.queryNumber(phone);
			result.setText(address);
			
			//ȥ���ݿ��в�ѯ
			//1.�����ѯ  2.�������ݿ�
			//дһ�������࣬��ѯ���ݿ�
			Log.i(TAG,"��Ҫ��ѯ�ĵ绰���룺 "+phone);
		}
		
	}

}
