package com.jiangcheng.mobilesafe;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public abstract class BaseSetupActivity extends Activity {

	
	//����һ������ʶ����
		private GestureDetector detector;
	    protected SharedPreferences sp;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			sp=getSharedPreferences("config",MODE_PRIVATE);
			//2.ʵ��������ʶ����
			/*detector = new GestureDetector(this, new SimpleOnGestureListener(){
				
			
			});*/
			detector=new GestureDetector(this, new SimpleOnGestureListener(){

				/*
				 * �����ǵ���ָ�����滬����ʱ�򣬻ص�
				 */
				@Override
				public boolean onFling(MotionEvent e1, MotionEvent e2,
						float speedX, float speedY) {

				        float XFrom = e1.getX();  //�������� 
				        float XTo = e2.getX(); 
				        float YFrom = e1.getY(); 
				        float YTo = e2.getY(); 
				        // ���һ�����X����ȴ���100������X�᷽����ٶȴ���100 
				        if (Math.abs(XFrom - XTo) > 100.0f && Math.abs(speedX) > 100.0f) { 
				            // X����ȴ���Y��ķ��� 
				            if (Math.abs(XFrom - XTo) >= Math.abs(YFrom - YTo)) { 
				                if (XFrom > XTo) { 
				                    // ��һ�� 
				                	showNext();
				                } else { 
				                    // ��һ�� 
				                   showPre();
				                } 
				            } 
				        } else { 
				            return false; 
				        } 
				        return true; 
				}

				
			 
			});
			
		}

		public abstract void showNext();
		public abstract void showPre();
		/*
		 * ��һ������¼�
		 */
		public void next(View view){
			showNext();
		}
		
		/*
		 * ��һ��һ��
		 */
		public void pre(View view){
			 showPre();
		}
		
		//ʹ������ʶ����
		@Override
		public boolean onTouchEvent(MotionEvent event) {

			//���յ�����
			detector.onTouchEvent(event);
			return super.onTouchEvent(event);
		}
}
