package com.jiangcheng.mobilesafe;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.jiangcheng.mobilesafe.R;
import com.jiangcheng.mobilesafe.utils.StreamTools;

public class SplashActivity extends Activity {
	protected static final String TAG = "SplashActivity";
	protected static final int ENTR_HOME = 1;
	protected static final int SHOW_UPDATE_DIALOG = 0;
	protected static final int NETWORK_ERROR = 3;
	protected static final int JSON_ERROR =4 ;
	protected static final int URL_ERROR = 2;	
	private TextView tv_splash_version;
	private String description;
	private TextView tv_update_info;
	/*
	 * ��Ӧ�°汾�����ص�ַ
	 */
	private String apkurl;
	private SharedPreferences sp;
 	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//ȥ����Ϣ��
		setContentView(R.layout.activity_splash);
		sp=getSharedPreferences("config", MODE_PRIVATE);
		tv_splash_version=(TextView) findViewById(R.id.tv_splash_version);
		tv_splash_version.setText("�汾��"+getVersionName());
		tv_update_info=(TextView) findViewById(R.id.tv_update_info);
		boolean update=sp.getBoolean("dupdate", false);
		
		installShortCut();
		
		//�������ݿ�
		copyDb("address.db");
		copyDb("antivirus.db");
		if(update){
			//�������
			checkUpdate();
		}else{
			//�Զ������Ѿ��ر���
			handler.postDelayed(new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					enterHome();
				}
			}, 2000);
		}
		AlphaAnimation aa=new AlphaAnimation(0.3f, 1.0f);
	    aa.setDuration(500);
	    findViewById(R.id.rl_root_splash).startAnimation(aa);
	}
 	private void installShortCut() {
		// TODO Auto-generated method stub
 		boolean shortcut=sp.getBoolean("shortcut", false);
 		if(shortcut)
 			return ;
 		Editor editor=sp.edit();
 		//���͹㲥����ͼ�����һ���������棬Ҫ�������ͼ����
 		Intent intent=new Intent();
 		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		//��ݷ�ʽ  Ҫ����3����Ҫ����Ϣ 1.����            2.ͼ��       3.��ʲô��
 		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "�ֻ���ʿ");
 		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));

 		//������ͼ���Ӧ����ͼ
 		Intent shortcutIntent=new Intent();
 		shortcutIntent.setAction("android.intent.action.MAIN");
 		shortcutIntent.addCategory("android.intent.category.LAUNCHER");
 		shortcutIntent.setClassName(getPackageName(), "com.jiangcheng.mobilesafe.SplashActivity");
 		
 		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
 		
 		sendBroadcast(intent);
 		
 		editor.putBoolean("shortcut", true);
 		editor.commit();
	}
	/*
 	 * //path  ��address.db������ݿ⿽����data/data/����/files/address.db
 	 */
	private void copyDb(String filename) {
//ֻҪ�㿽����һ�Σ��ҾͲ�Ҫ���ٿ�����
		try {
			//��ָ����Ŀ¼������ database.db�ļ�
			File file=new File(getFilesDir(), filename);
			if(file.exists()&&file.length()>0){
				//�����ˣ�����Ҫ������
			    Log.i(TAG,"�����ˣ�����Ҫ������");
			}else{
				InputStream is=getAssets().open(filename);
				
				FileOutputStream fos=new FileOutputStream(file);
				byte[] buffer=new byte[1024];
				int len=0;
				len=is.read(buffer);
				while(len!=-1){
					fos.write(buffer,0,len);
					len=is.read(buffer);
				}
				is.close();
				fos.close();
				} 
			}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case SHOW_UPDATE_DIALOG://��ʾ�����ĶԻ���
				Log.i(TAG,"��ʾ�����ĶԻ���");
				showUpdateDialot();
				break;
			case ENTR_HOME:   //������ҳ��
				enterHome();
				break;
			case URL_ERROR:   //URL ����
				enterHome();
				Toast.makeText(getApplicationContext(), "URL����", 0).show();
				break;
			case NETWORK_ERROR: //�����쳣
				Toast.makeText(getApplicationContext(), "�����쳣", 0).show();
				enterHome();
				break;
			case JSON_ERROR:   //JSON��������
				Toast.makeText(SplashActivity.this, "JSON��������", 0).show();
				enterHome();
				break;
			default:
				break;
			}
		}
	};
	
   /*
    * ����Ƿ��а汾����������о�����
    */
	private void checkUpdate() {
 
		new Thread(){
			public void run(){
				Message mes=Message.obtain();
				long startTime=System.currentTimeMillis();

				//URLhttp://192.168.1.105:8080/MobileSafe.apk
				   try{
					URL url=new URL(getString(R.string.serverurl));
					//����
					HttpURLConnection conn=(HttpURLConnection) url.openConnection();
				    conn.setRequestMethod("GET");
				    conn.setConnectTimeout(5000);
				    int code=conn.getResponseCode();
				    if(code==200){
				    	//�����ɹ�
				    InputStream is=	conn.getInputStream();
				    //����ת��String
				    String result=StreamTools.readFromStream(is);
				    System.out.println("�����ɹ�");
				    Log.i(TAG,"�����ɹ����ء���"+result);
				    //json����
				    JSONObject obj=new JSONObject(result);
				    //�õ��������İ汾��Ϣ
				    String version=(String) obj.get("verson");
				    description=(String) obj.get("description");
				    apkurl=(String) obj.get("apkurl");
				    
				    //У���Ƿ����°汾
				    if(getVersionName().equals(version)){
				    	//�汾һֱ��û���°汾��������ҳ��
				    	mes.what=ENTR_HOME;
				    	
				    }else{
				    	//���°汾���������Ի���
				    	mes.what=SHOW_UPDATE_DIALOG;
				    }
				  }
			 }catch(MalformedURLException e){
				 mes.what=URL_ERROR;
				 e.printStackTrace();
			 } catch (IOException e) {
                mes.what=NETWORK_ERROR;
				e.printStackTrace();
			} catch (JSONException e) {
				mes.what=JSON_ERROR;
 				e.printStackTrace();
			}finally{
				long endTime=System.currentTimeMillis();
				//���ǻ��˶���ʱ��
				long dTime=endTime-startTime;
				//����ͣ��2s��
				if(dTime<2000){
					try {
						Thread.sleep(2000-dTime);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				handler.sendMessage(mes);
			}
			};
		}.start();
	}
	
	/*
	 * ���������Ի���
	 */
	private void showUpdateDialot() {
          //this=activity.class
		AlertDialog.Builder builder=new Builder(this);
		builder.setTitle("��ʾ����");
		//���ô����ԣ��öԻ�����ĵ���¼����ɴ���
		builder.setCancelable(false);   //ǿ������
		//������ؼ�ʱ����
		builder.setOnCancelListener(new OnCancelListener(){
			@Override
			public void onCancel(DialogInterface dialog) {
				//������ҳ��
				enterHome();
				dialog.dismiss();
			}
		});
		builder.setMessage(description);
		builder.setPositiveButton("��������", new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//����APK,bignqie tihuan 
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					//sd������
					FinalHttp finalhttp=new FinalHttp();
					finalhttp.download(apkurl, Environment.getExternalStorageDirectory().getAbsolutePath()+"/mobilesafe2.0.apk", new AjaxCallBack<File>(){
						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
                            t.printStackTrace();
                            Toast.makeText(getApplicationContext(), "����ʧ��", 1).show();
							super.onFailure(t, errorNo, strMsg);
						}

						@Override
						public void onLoading(long count, long current) {
							super.onLoading(count, current);
							tv_update_info.setVisibility(View.VISIBLE);
							//��ǰ���ؽ���
						    int progress=(int)(current*100/count);
						 	tv_update_info.setText("���ؽ���:"+progress+"%");
						}

						@Override
						public void onSuccess(File t) {
							// TODO Auto-generated method stub
							super.onSuccess(t);
							installAPK(t);
						}

						/*
						 * ��װAPK
						 */
						private void installAPK(File t) {

							Intent intent=new Intent();
							intent.setAction("android.intent.action.VIEW");
							intent.addCategory("android.intent.category.DEFAULT");
							intent.setDataAndType(Uri.fromFile(t), "application/vnd.android.package-archive");
						    startActivity(intent);
						}
						
					});
					
				}else{
					Toast.makeText(getApplicationContext(), "û��sdcard���밲װ������", 0).show();
					return ;
				}
			}
			
		});
		builder.setNegativeButton("�´���˵", new  OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();//�Ի�������
				enterHome();//������ҳ��
			}
		});
		builder.show();
		}

	
	protected void enterHome() {
			Intent intent=new Intent(this,HomeActivity.class);
		    startActivity(intent);
		    //�رյ�ǰҳ��
		    finish();
	}
	
	/*
	 * �õ�Ӧ�ó���İ汾����
	 */
	private String getVersionName(){
		//���������ֻ���APK
		PackageManager pm=getPackageManager();
		//�õ�ָ��apk�Ĺ��ܵ��嵥�ļ�
		try {
		PackageInfo info=pm.getPackageInfo(getPackageName(), 0);
		return info.versionName;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
}
