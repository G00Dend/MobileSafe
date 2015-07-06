package com.jiangcheng.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.jiangcheng.mobilesafe.R;
import com.jiangcheng.mobilesafe.db.dao.NumberAddressQueryUtils;

public class AddressService extends Service {
protected static final String TAG = "AddressService";
	/*
 * ���������
 */
	private WindowManager wm;
	View view;
	/**
	 * �绰���� 
	 */
	private TelephonyManager tm;
	private MyListenerPhone listenerPhone;
	
	private SharedPreferences sp;
	
	private OutCallReceiver receiver;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//�൱�ڷ���������ڲ���
	class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			//����������õ��Ĳ���ȥ�ĵ绰����
			String phone=getResultData();
			//��ѯ���ݿ�
			String address=NumberAddressQueryUtils.queryNumber(phone);
			
			//Toast.makeText(context, address, 1).show();
			myToast(address);
			
		}

	}
	
	
	
	
	private class MyListenerPhone extends PhoneStateListener{

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
		//state:״̬��incomingNumber:�������
			super.onCallStateChanged(state, incomingNumber);
            switch(state){
            case TelephonyManager.CALL_STATE_RINGING:   //������������
            	//��ѯ���ݿ�Ĳ���
            	String address=NumberAddressQueryUtils.queryNumber(incomingNumber);
            	//Toast.makeText(getApplicationContext(), address, 1).show();
            	myToast(address);
            	break;
            case TelephonyManager.CALL_STATE_IDLE: //�绰�Ŀ���״̬
            	//�����View�Ƴ�
            	if(view != null){
            		wm.removeView(view);
            	}
            	
            	
            	break;
            	
            	default:
            		break;
            }
		}
		
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		tm=(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		
		//��������
		listenerPhone=new MyListenerPhone();
		tm.listen(listenerPhone, PhoneStateListener.LISTEN_CALL_STATE);
	    
		
		//�ô���ע��㲥������
		receiver=new OutCallReceiver();
		//��ͼƥ����
		IntentFilter filter=new IntentFilter();
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(receiver, filter);
		
		//ʵ��������
		wm=(WindowManager) getSystemService(WINDOW_SERVICE);
	}
	
	 WindowManager.LayoutParams params;
	 long[] mHits=new long[2];
	 
	/*
	 * �Զ�����˾
	 */
	public void myToast(String address) {
		// TODO Auto-generated method stub
		//items={"��͸��","������","������","������","ƻ����"};  
		view=view.inflate(this, R.layout.address_show, null);
		TextView textview=(TextView) view.findViewById(R.id.tv_address);
		
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
 
				
				 System.arraycopy(mHits, 1, mHits, 0, mHits.length-1);
			      mHits[mHits.length-1] = SystemClock.uptimeMillis();
			      if (mHits[0] >= (SystemClock.uptimeMillis()-500)) {
			    
			    	  //˫�������ˡ�������
			    	  params.x=wm.getDefaultDisplay().getWidth()/2-view.getWidth()/2;
			    	  wm.updateViewLayout(view, params);
			    	  Editor editor=sp.edit();
						editor.putInt("lastx", params.x);
						editor.commit();
			      }
			}
		});
		
		//��view��������һ�������ļ�����
		view.setOnTouchListener(new OnTouchListener() {
			//������ָ�ĳ�ʼλ��
			int startX;
			int startY;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:   //��ָ������Ļ
					startX=(int) event.getRawX();
					startY=(int) event.getRawY();
                    Log.i(TAG,"��ָ�����ؼ���"+startX+"   "+startY);					
					break;
				case MotionEvent.ACTION_MOVE:   //��ָ����Ļ���ƶ�
					int newX=(int) event.getRawX();
					int newY=(int) event.getRawY();
                    				
					
                    int dx=newX-startX;
                    int dy=newY-startY;
                    params.x+=dx;
                    params.y+=dy;
                    //���Ǳ߽�����
                    if(params.x<0){
                    	params.x=0;
                    }
                    if(params.y<0){
                    	params.y=0;
                    }
                    if(params.x>(wm.getDefaultDisplay().getWidth()-view.getWidth())){
                    	params.x=wm.getDefaultDisplay().getWidth()-view.getWidth();
                    }
                    if(params.y>(wm.getDefaultDisplay().getHeight()-view.getHeight())){
                    	params.y=wm.getDefaultDisplay().getHeight()-view.getHeight();
                    }
                    Log.i(TAG,"��ָ�ڿؼ����ƶ���"+newX+"   "+newY);	
                    wm.updateViewLayout(view, params);
					
                    startX=(int) event.getRawX();
					startY=(int) event.getRawY();
                    
                    break;
					
				case MotionEvent.ACTION_UP:     //��ָһ����Ļһ˲��
					//��¼�ؼ�������Ļ���Ͻǵ�����
					Editor editor=sp.edit();
					editor.putInt("lastx", params.x);
					editor.putInt("lasty", params.y);
					editor.commit();
					Log.i(TAG,"��ָ�뿪�ؼ���"+"   ");	
					break;
				
				}
				
				//return true;   //�¼���������ˣ���Ҫ�ø��ؼ���������Ӧ��Ӧ�¼�
			     return false;
			}
		});
		
		
		int[] ids={R.drawable.call_locate_white,R.drawable.call_locate_orange
				,R.drawable.call_locate_blue,R.drawable.call_locate_gray
				,R.drawable.call_locate_green};
		
		  sp=getSharedPreferences("config", MODE_PRIVATE);
		int t=sp.getInt("which",0);
		view.setBackgroundResource(ids[sp.getInt("which",0)]);
		
		
		
		 
		  //view.setBackgroundResource(R.drawable.call_locate_gray);
		  
		  textview.setText(address);
		  
		  //����Ĳ��������ú���
		  params = new WindowManager.LayoutParams();
		 
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity=Gravity.TOP+Gravity.LEFT;
        //ָ�����������ߺ��ϱ߾���
        params.x=sp.getInt("lastx", 0);
        params.y=sp.getInt("lasty", 0);
        
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        //androidϵͳ�о��е绰���ȼ���һ�ִ������ͣ��ǵ����Ȩ��
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		
		wm.addView(view, params);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//ȡ����������
		tm.listen(listenerPhone, PhoneStateListener.LISTEN_NONE);
		listenerPhone=null;
		//�ڷ���������ʱ���ô���ȡ��ע��㲥�Ľ�����
		unregisterReceiver(receiver);
		receiver=null;
		
	}
	 
}
