package com.jiangcheng.mobilesafe;
import com.jiangcheng.mobilesafe.R;
import com.jiangcheng.mobilesafe.utils.MD5Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
public class HomeActivity extends Activity {
	protected static final String TAG = "HomeActivity";
	private GridView list_home;
	private MyAdapter adapter;
	private SharedPreferences sp;
	private long mBackTime;
	private static String[] names={
		"手机防盗","防骚扰","软件管理",
		"进程管理","手机杀毒",
		"缓存清理","工具箱","设置"
	};
	private static int[] ids={
		R.drawable.safe,R.drawable.callmsgsafe,R.drawable.app,
		R.drawable.taskmanager,R.drawable.trojan,
		R.drawable.sysoptimize,R.drawable.atools,R.drawable.settings
	};
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		sp=getSharedPreferences("config", MODE_PRIVATE);
		list_home=(GridView) findViewById(R.id.list_home);
		adapter=new MyAdapter();
		list_home.setAdapter(adapter);
		list_home.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent;
				switch(position){
				
				case 0:   //进入手机防盗页面
					showLostFindDialog();
					break;
					default:
						break;
				
					case 1: //加载黑名单拦截界面
						intent=new Intent(HomeActivity.this,CallSmsSafeActivity.class);
						startActivity(intent);
						break;
					case 2: //软件管理
						intent=new Intent(HomeActivity.this,AppManagerActivity.class);
						startActivity(intent);
						break;
					case 3: //进程管理
						intent=new Intent(HomeActivity.this,TaskManagerActivity.class);
						startActivity(intent);
						break; 
					case 4: //手机杀毒
						intent=new Intent(HomeActivity.this,AntiVirusActivity.class);
						startActivity(intent);
						break; 					
					case 5://缓存清理
					 intent=new Intent(HomeActivity.this,CleanCacheActivity.class);
					startActivity(intent);
					break;
					case 6://进入高级工具
						intent=new Intent(HomeActivity.this,AtoolsActivity.class);
						startActivity(intent);
						break;				
				case 7://进入设置中心
				   intent=new Intent(HomeActivity.this,SettingActivity.class);
					startActivity(intent);
					break;
			
				}
			}

		
		});
	}
	
	private void showLostFindDialog() {
        //判断是否设置过密码
		if(isSetupPwd()){
			//已经设置密码了，弹出的事输入对话框
			Log.i(TAG, "设置过密码进入输入密码框");
			showEnterDialog();
		}else{
			//没有设置密码,弹出的就是设置密码的对话框
			Log.i(TAG, "还没有设置过密码进入设置密码框");
			showSetupPwdDialog();
		}
	}
	private EditText et_setup_pwd;
	private EditText et_setup_confirm;
	private Button ok;
	private Button cancel;
	private AlertDialog dialog;
	
	/**
	 * 设置密码对话框
	 */
	private void showSetupPwdDialog() {
		// TODO Auto-generated method stub
	   AlertDialog.Builder builder=new Builder(HomeActivity.this);
		//自定义一个布局文件
	   View view=View.inflate(HomeActivity.this, R.layout.dialog_setup_password, null);
	   et_setup_pwd = (EditText) view.findViewById(R.id.et_setup_pwd);
	   et_setup_confirm = (EditText) view.findViewById(R.id.et_setup_confirm);
	   ok = (Button) view.findViewById(R.id.ok);
	   cancel = (Button) view.findViewById(R.id.cancel);
	   cancel.setOnClickListener(new OnClickListener() {
		
		public void onClick(View v) {

			dialog.dismiss();
		
		}
	});
	   ok.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			//取出密码
          String password=et_setup_pwd.getText().toString().trim();
          String password_confirm=et_setup_confirm.getText().toString().trim();
          if(TextUtils.isEmpty(password)||TextUtils.isEmpty(password_confirm)){
        	  Toast.makeText(HomeActivity.this, "密码为空", 0).show();
              return ;
          }
          //保存密码，判断是否一致采取保存
          if(password.equals(password_confirm)){
        	  //一致的话就保存密码，吧对话框消掉，还要进入手机防盗页面
        	  Editor editor=sp.edit();
        	  editor.putString("password", MD5Utils.md5Password(password));
        	  editor.commit();
        	  dialog.dismiss();
        	  Log.i(TAG, "一致的话就保存密码，吧对话框消掉，还要进入手机防盗页面");
        	  Intent intent=new Intent(HomeActivity.this,LostFindActivity.class);
			  startActivity(intent);
          }else{
        	  Toast.makeText(HomeActivity.this, "密码不一致", 0).show();
        	  return ;
          }
			
		}
	});
	   
	   
	   dialog=builder.create();
	   dialog.setView(view, 0, 0, 0, 0);
	   dialog.show();
	}
/*
 * 输入密码对话框
 */
	private void showEnterDialog() {
		// TODO Auto-generated method stub
		   AlertDialog.Builder builder=new Builder(HomeActivity.this);
			//自定义一个布局文件
		   View view=View.inflate(HomeActivity.this, R.layout.dialog_enter_password, null);
		   et_setup_pwd=(EditText) view.findViewById(R.id.et_setup_pwd);
		   ok=(Button) view.findViewById(R.id.ok);
		   cancel=(Button) view.findViewById(R.id.cancel);
		   cancel.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {

				dialog.dismiss();
			
			}
		});
		   ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { 
		    //取出密码
			String password=et_setup_pwd.getText().toString().trim();
			String savePassword=sp.getString("password", "");
			if(TextUtils.isEmpty(password)){
				Toast.makeText(HomeActivity.this, "密码为空", 1).show();
			}
			
			if(MD5Utils.md5Password(password).equals(savePassword)){
				//输入密码为之前我们这是的密码
				//把对话框消掉，进入主页面
				dialog.dismiss();
				Log.i(TAG, "把对话框消掉，进入手机防盗页面");
				Intent intent=new Intent(HomeActivity.this,LostFindActivity.class);
				startActivity(intent);
			}else{
				Toast.makeText(HomeActivity.this, "密码错误", 1).show();
				return;
			}
			}
		});
		   
		   
		   dialog=builder.create();
		   dialog.setView(view, 0, 0, 0, 0);
		   dialog.show();
		}

	/**
	 * 判断是否设置过密码
	 */
	private boolean isSetupPwd(){
		String password = sp.getString("password", null);
		return !TextUtils.isEmpty(password);
		
	}

	
	private class MyAdapter extends BaseAdapter{
		//屏幕要显示多少个
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return names.length;
		}

		//点击的时候返回某个对象
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view=View.inflate(HomeActivity.this, R.layout.list_item_home, null);
			ImageView iv_item=(ImageView) view.findViewById(R.id.iv_item);
			TextView tv_item=(TextView) view.findViewById(R.id.tv_item);
			tv_item.setText(names[position]);
			iv_item.setImageResource(ids[position]);
			return view;
		}
	}
	  
	@Override
	public void onBackPressed() {
		if (System.currentTimeMillis() - mBackTime > 2000) {
			mBackTime = System.currentTimeMillis();
			Toast.makeText(HomeActivity.this," 在按一次退出", Toast.LENGTH_SHORT).show();
		} else {
			finish();
			super.onBackPressed();
		}		
	}
}
