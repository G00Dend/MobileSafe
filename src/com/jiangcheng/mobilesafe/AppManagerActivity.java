package com.jiangcheng.mobilesafe;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.jiangcheng.mobilesafe.R;
import com.jiangcheng.mobilesafe.db.dao.ApplockDao;
import com.jiangcheng.mobilesafe.domain.AppInfo;
import com.jiangcheng.mobilesafe.engine.AppInfoProvider;
import com.jiangcheng.mobilesafe.utils.DensityUtil;
public class AppManagerActivity extends Activity  {
	private TextView tv_total;
	private SwipeMenuListView lv_app_manager;
	/**所有的应用程序包信息  */
	private List<AppInfo> appInfos;
	
	/** 用户应用程序的集和 */
	private List<AppInfo> userAppInfos;
	
	private AppManagerAdapter adapter;
	private LinearLayout mLlLoading;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_manager);
		tv_total=(TextView) findViewById(R.id.tv_avail_rom);	
		mLlLoading = (LinearLayout) findViewById(R.id.ll_loading);
		//布局
		lv_app_manager=(SwipeMenuListView) findViewById(R.id.lv_app_manager);
		userAppInfos = new ArrayList<AppInfo>();
		fillData();
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem openItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
						0xCE)));
				// set item width
				openItem.setWidth(dp2px(90));
				// set item title
				openItem.setTitle("Open");
				// set item title fontsize
				openItem.setTitleSize(18);
				// set item title font color
				openItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(openItem);

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
       lv_app_manager.setMenuCreator(creator);
       
		/**
		 * 设置listview的点击事件
		 */
		lv_app_manager.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
		    AppInfo appInfo = userAppInfos.get(position);
			switch (index) {
				case 0:
					startApplication(appInfo);
					break;
				case 1:
				    uninstallAppliation(appInfo,position);

					break;
				default:
					break;
				}
			}
		});  
		// test item long click
		lv_app_manager.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getApplicationContext(), position + " long click", 0).show();
				return false;
			}
		});			
}
	
	private void fillData() {
		mLlLoading.setVisibility(View.VISIBLE);
		new Thread(){	
				public void run() {
					userAppInfos = AppInfoProvider.getAppInfos(getApplicationContext());
					
					runOnUiThread(new Runnable() {						
						@Override
						public void run() {
							mLlLoading.setVisibility(View.GONE);
							if (adapter == null) {
								adapter = new AppManagerAdapter();
								lv_app_manager.setAdapter(adapter);							
							} else {
							    adapter.notifyDataSetChanged();	
							}
							tv_total.setText("用户程序: " +userAppInfos.size()+" 个"  );
						}
					});
				}			
		}.start();		
	}	
	
	private void updateView() {
		
	}
		
	private class AppManagerAdapter extends BaseAdapter{
		// 控制listview有多少个条目
		@Override
		public int getCount() {
              return userAppInfos.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
		  
		  if (convertView == null ) {
			  convertView = View.inflate(getApplicationContext(), R.layout.list_item_appinfo,null);
			  new ViewHolder(convertView);
		  }
		  ViewHolder holder = (ViewHolder) convertView.getTag();
		  AppInfo appInfo = getItem(position);
		  holder.tv_name.setText(appInfo.getName());
		  if (appInfo.isInRom()) {
			  holder.tv_location.setText("位置:手机内存 "+"uid:"+appInfo.getUid());
		  } else {
			  holder.tv_location.setText("位置:外部存储 "+"uid:"+appInfo.getUid());
		  }
		  holder.iv_icon.setImageDrawable(appInfo.getIcon());		  
		  return convertView;
		}

		@Override
		public AppInfo getItem(int position) {
			return userAppInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

	}
	class ViewHolder{
		TextView tv_name;
		TextView tv_location;
		ImageView iv_icon;
		
		public ViewHolder(View view) {
			tv_name = (TextView) view.findViewById(R.id.tv_app_name);
			tv_location = (TextView) view.findViewById(R.id.tv_app_location);
			iv_icon = (ImageView) view.findViewById(R.id.iv_app_icon);
			view.setTag(this);
		}
	}
	
    /**
	 * 卸载程序的应用
	 */
   private void uninstallAppliation(AppInfo appInfo,int position) {
	   try {
		   Intent intent=new Intent();
		   intent.setAction("android.intent.action.DELETE");
		   intent.setData(Uri.parse("package:"+appInfo.getPackname()));
		   startActivity(intent);
		   userAppInfos.remove(position);
	} catch (Exception e) {
		e.printStackTrace();
	  }	   
  }


/**
 * 开启一个应用程序
 */
	private void startApplication(AppInfo appInfo) {
		//查询这个应用程序的入口activity。把他开启起来
		PackageManager pm=getPackageManager();
		Intent intent=pm.getLaunchIntentForPackage(appInfo.getPackname());
		if(intent!=null){
			startActivity(intent);
		}
	}
	
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
}




