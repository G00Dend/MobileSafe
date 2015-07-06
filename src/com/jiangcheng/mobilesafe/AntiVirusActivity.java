package com.jiangcheng.mobilesafe;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.List;

import android.R.color;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.transition.Visibility;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jiangcheng.mobilesafe.R;
import com.jiangcheng.mobilesafe.db.dao.AntivirsuDao;
public class AntiVirusActivity extends Activity implements View.OnClickListener{

	protected static final int SCANING=0;
	protected static final int FINISH=2;
	private ImageView iv_scan;
	private ProgressBar progressBar1;
    private PackageManager pm;
    private TextView tv_scan_status;
    private LinearLayout ll_container;
    private Button mBtnStart;
    private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			//super.handleMessage(msg);
			switch(msg.what){
			case SCANING:
				ScanInfo scanInfo=(ScanInfo) msg.obj;
				tv_scan_status.setVisibility(View.VISIBLE);
				tv_scan_status.setText("正在扫描:"+scanInfo.name);
				TextView tv=new TextView(getApplicationContext());
			    if(scanInfo.isvirus){
			    	tv.setTextColor(Color.RED);
			    	tv.setText("发现病毒:"+scanInfo.name);
			    }else{
			    	tv.setTextColor(Color.BLACK);
			    	tv.setText("扫描安全:"+scanInfo.name);
			    }
				ll_container.addView(tv,0);
				break;
				
			case FINISH:
				tv_scan_status.setText("扫描完毕");
				mBtnStart.setClickable(true);
				//把一个控件上的动画清除掉
				iv_scan.clearAnimation();
				break;
			}
		}
    	
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_anti_virus);
	    iv_scan=(ImageView) findViewById(R.id.iv_scan);
	    tv_scan_status=(TextView) findViewById(R.id.tv_scan_status);
	    ll_container=(LinearLayout) findViewById(R.id.ll_container);
	    mBtnStart = (Button) findViewById(R.id.btn_start);
	    mBtnStart.setOnClickListener(this);
	    
	    
	    progressBar1=(ProgressBar) findViewById(R.id.progressBar1);
	    
	    
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_start:
			RotateAnimation ra=new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		    ra.setDuration(2000);
		    ra.setRepeatCount(Animation.INFINITE);
			iv_scan.startAnimation(ra);
			scanVirus();
			mBtnStart.setClickable(false);
			break;
		default:
			break;
		}	
	}
    /**
     * 扫描病毒
     */
	private void scanVirus() {
		pm=getPackageManager();
		tv_scan_status.setText("正在初始化杀毒引擎.....");
	    new Thread(){
			public void run(){
	    		 List<PackageInfo> infos=pm.getInstalledPackages(0);
	    		 try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		 progressBar1.setMax(infos.size());
	    		 int progress=0;
	    		 for(PackageInfo info:infos){
	    			 //apk文件的完整的路径
	    		    String sourcedir=info.applicationInfo.sourceDir;
	    			String md5=getFileMd5(sourcedir);
	    			ScanInfo scaninfo=new ScanInfo();
	    			scaninfo.name=info.applicationInfo.loadLabel(pm).toString();
	    			scaninfo.packname=info.packageName;
	    		    //查询md5信息，是否在病毒数据库里面存着
	    			if(AntivirsuDao.isVirus(md5)){
	    				 //发现病毒
	    				scaninfo.isvirus=true;
	    			}else{
	    				//扫描安全
	    				scaninfo.isvirus=false;
	    			}
	    			
	    			Message msg=Message.obtain();
	    			msg.obj=scaninfo;
					msg.what=SCANING;
	    			handler.sendMessage(msg);
	    			
	    			try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			progress++;
	    			progressBar1.setProgress(progress);
	    			
	    		 }
	    		 Message msg=Message.obtain();
	    		 msg.what=FINISH;
	    		 handler.sendMessage(msg);
	    	}
	    }.start();
	}
	/**
	 * 扫描信息的内部类
	 */
	class ScanInfo{
		String packname;
		String name;
		boolean isvirus;
	}
	
	/*
	 * 获取文件md5的值
	 * @param path 文件的全路径
	 */
	private String getFileMd5(String path){
		//获取一个文件的特征信息，签名信息
		File file=new File(path);
		//md5
		try {
			MessageDigest digest=MessageDigest.getInstance("sha1");
		    FileInputStream fis=new FileInputStream(file);
		    byte[] buffer=new byte[1024];
		    int len=-1;
		    while((len=fis.read(buffer))!=-1){
		    	digest.update(buffer, 0, len);
		    }
		  byte[] result=digest.digest();
		  StringBuffer sb=new StringBuffer();
		  for(byte b:result){
			  //与运算
			  int number=b & 0xff;  //加盐
			  String str=Integer.toHexString(number);
			  if(str.length()==1){
				  sb.append("0");
			  }
			  sb.append(str);
		  }
		  return sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		
	}
	
	
}
