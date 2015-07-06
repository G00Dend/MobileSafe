package com.jiangcheng.mobilesafe.receiver;

import com.jiangcheng.mobilesafe.db.dao.NumberAddressQueryUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class OutCallReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		//这就是我们拿到的拨出去的电话号码
		String phone=getResultData();
		//查询数据库
		String address=NumberAddressQueryUtils.queryNumber(phone);
		
		Toast.makeText(context, address, 1).show();
	
	}

}
