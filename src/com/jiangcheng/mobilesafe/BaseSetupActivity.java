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

	
	//定义一个手势识别器
		private GestureDetector detector;
	    protected SharedPreferences sp;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			sp=getSharedPreferences("config",MODE_PRIVATE);
			//2.实例化手势识别器
			/*detector = new GestureDetector(this, new SimpleOnGestureListener(){
				
			
			});*/
			detector=new GestureDetector(this, new SimpleOnGestureListener(){

				/*
				 * 当我们的手指在上面滑动的时候，回调
				 */
				@Override
				public boolean onFling(MotionEvent e1, MotionEvent e2,
						float speedX, float speedY) {

				        float XFrom = e1.getX();  //按下坐标 
				        float XTo = e2.getX(); 
				        float YFrom = e1.getY(); 
				        float YTo = e2.getY(); 
				        // 左右滑动的X轴幅度大于100，并且X轴方向的速度大于100 
				        if (Math.abs(XFrom - XTo) > 100.0f && Math.abs(speedX) > 100.0f) { 
				            // X轴幅度大于Y轴的幅度 
				            if (Math.abs(XFrom - XTo) >= Math.abs(YFrom - YTo)) { 
				                if (XFrom > XTo) { 
				                    // 下一个 
				                	showNext();
				                } else { 
				                    // 上一个 
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
		 * 下一步点击事件
		 */
		public void next(View view){
			showNext();
		}
		
		/*
		 * 上一步一步
		 */
		public void pre(View view){
			 showPre();
		}
		
		//使用手势识别器
		@Override
		public boolean onTouchEvent(MotionEvent event) {

			//接收到手势
			detector.onTouchEvent(event);
			return super.onTouchEvent(event);
		}
}
