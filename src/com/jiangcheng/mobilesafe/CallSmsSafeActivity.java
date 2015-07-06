package com.jiangcheng.mobilesafe;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiangcheng.mobilesafe.R;
import com.jiangcheng.mobilesafe.db.dao.BlackNumberDao;
import com.jiangcheng.mobilesafe.domain.BlackNumberInfo;

public class CallSmsSafeActivity extends Activity {
private ListView lv_callsms_safe;
private List<BlackNumberInfo> infos;
private CallSmsSafeAdapter adapter;
private BlackNumberDao dao;
private LinearLayout ll_loading;
private int offset=0;
private int maxnumber=20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_call_sms_safe);
	    ll_loading=(LinearLayout) findViewById(R.id.ll_loading);
	    lv_callsms_safe=(ListView) findViewById(R.id.lv_callsms_safe);
	    dao=new BlackNumberDao(this);
	    fillData();
	    //listview注册一个滚动事件的监听器
	    lv_callsms_safe.setOnScrollListener(new OnScrollListener() {
			//当滚动的状态发生变化的时候
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				switch(scrollState){
				case OnScrollListener.SCROLL_STATE_IDLE://空闲状态
					//判断当前的listview滚动的位置
					//获取最后一个课件条目在集合里面的位置
					int lastposition=lv_callsms_safe.getLastVisiblePosition();
					
					//集合里面有20个item，位置从0开始，最后一个条目的位置是19
					if(lastposition==(infos.size()-1)){
						offset+=maxnumber;
						fillData();
						//int end_position=lv_callsms_safe.get;
					}
					
					
					
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://收触摸状态
					
					break;
				case OnScrollListener.SCROLL_STATE_FLING://关系滑行状态
					
					break;
				}
				
			}
			
			//滚动时候调用的方法
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
	    
	    
	}


	private void fillData() {
		ll_loading.setVisibility(View.VISIBLE);
	     new Thread(){
	    	public void run(){
	    		if(infos==null){
	    			infos=dao.findPart(offset, maxnumber);
	    		}else{ //原来已经加载过的数据了
	    			infos.addAll(dao.findPart(offset, maxnumber));
	    			
	    		}
	    		
	    		runOnUiThread(new Runnable(){
					@Override
					public void run() {
						int flag_num=maxnumber;
					
						ll_loading.setVisibility(View.INVISIBLE);
						if(adapter == null){
							adapter = new CallSmsSafeAdapter();
						    lv_callsms_safe.setAdapter(adapter);
						}else{
							 flag_num+=maxnumber;
								int count=dao.querycountAll();
								if(flag_num >= count){
									return;
								} 
							adapter.notifyDataSetChanged();
						}
						
					}
	    			
	    		});
	    		
	    	}
	    }.start();
	}
	
	private class CallSmsSafeAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return infos.size();
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view;
			ViewHolder holder;
			//利用缓存
			if(convertView==null){
				view=View.inflate(getApplicationContext(),R.layout.list_item_callsms,null);
				holder=new ViewHolder();
				holder.tv_number=(TextView) view.findViewById(R.id.tv_black_number);
				holder.tv_mode=(TextView) view.findViewById(R.id.tv_block_mode);
			    holder.iv_delete=(ImageView) view.findViewById(R.id.iv_delete);
				view.setTag(holder);
			}else{
				view=convertView;
				holder=(ViewHolder) view.getTag();
			}
			//减少孩子查询的次数，内存中对象的地址
			holder.tv_number.setText(infos.get(position).getNumber());
			String mode=infos.get(position).getMode();
			if("1".equals(mode)){
				holder.tv_mode.setText("电话拦截");
				
			}else if("2".equals(mode)){
				holder.tv_mode.setText("短信拦截");
				
			}else if("3".equals(mode)){
				holder.tv_mode.setText("全部拦截");
			
		}
		holder.iv_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				AlertDialog.Builder builder=new Builder(CallSmsSafeActivity.this);
			    //在确定和取消按钮的时候，因为是内部类访问外部方法,故要将其声明为final型
				builder.setTitle("删除黑名单");
				builder.setMessage("是否删除黑名单?");
				builder.setIcon(R.drawable.app);
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//删除数据库的内容
						dao.delete(infos.get(position).getNumber());
						//更新界面
						infos.remove(position);
						//通知listview数据适配器
						adapter.notifyDataSetChanged();
					}
				});
				builder.setNegativeButton("取消",null);
				builder.show();
			}
		});
			return view;
		}

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
	}
	/*
	 * View对象的容器，记录孩子的内存地址，
	 *相当于一个记事本
	 */
	
	static class ViewHolder{
		TextView tv_number;
		TextView tv_mode;
		ImageView iv_delete;
	}
	
	
	private EditText et_blacknumber;
	private CheckBox cb_phone;
	private CheckBox cb_sms;
	private Button bt_ok;
	private Button bt_cancel;
	
	
	public void addBlackNumber(View view){
		AlertDialog.Builder builder=new Builder(this);
	    //在确定和取消按钮的时候，因为是内部类访问外部方法,故要将其声明为final型
		final AlertDialog dialog=builder.create();
		View contentView=View.inflate(this, R.layout.dialog_add_blacknumber, null);
		et_blacknumber=(EditText)contentView.findViewById(R.id.et_blacknumber);
		cb_phone=(CheckBox)contentView.findViewById(R.id.cb_phone);
		cb_sms=(CheckBox)contentView.findViewById(R.id.cb_sms);
		bt_ok=(Button)contentView.findViewById(R.id.ok);
		bt_cancel=(Button)contentView.findViewById(R.id.cancel);
		
		dialog.setView(contentView, 0, 0, 0, 0);
		dialog.show();
		bt_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		bt_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String blacknumber=et_blacknumber.getText().toString().trim();
				if(TextUtils.isEmpty(blacknumber)){
					Toast.makeText(getApplicationContext(), "黑名单号码为空", 0);
					return ;
				}
				String mode;
				if(cb_phone.isChecked()&&cb_sms.isChecked()){
					//全部拦截
					mode="3";
				}else if(cb_phone.isChecked()){
					//电话拦截
					mode="1";
				}else if(cb_sms.isChecked()){
					//短信拦截
					mode="2";
				}else{
					Toast.makeText(getApplicationContext(), "请选择模式", 0);
					return ;
				}
				//数据被加载到数据库
				dao.add(blacknumber, mode);
				//更新listview集合里面的内容
				BlackNumberInfo info=new BlackNumberInfo();
				info.setMode(mode);
				info.setNumber(blacknumber);
				infos.add(0,info);
				//通知listview数据适配器数据更新了
				adapter.notifyDataSetChanged();
				dialog.dismiss();
				
			}
		});
	}
}
