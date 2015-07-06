package com.jiangcheng.mobilesafe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiangcheng.mobilesafe.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SelectContactActivity extends Activity {
	private ListView list_select_contact;
	private LinearLayout mLlLoading;
	private List<Map<String,String>> data;
	private SimpleAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_contact);
		list_select_contact=(ListView) findViewById(R.id.list_select_contact);
		mLlLoading = (LinearLayout) findViewById(R.id.ll_loading);	
	    fillData(); 		
	    list_select_contact.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String phone=data.get(position).get("phone");
				Intent data=new Intent();
				data.putExtra("phone", phone);
				setResult(0,data);
				//�رյ�ǰҳ��
				finish();
			}
	    });
	     
	   
	}
	
	 private void  fillData() {
		 mLlLoading.setVisibility(View.VISIBLE);
	    	new Thread(){
					public void run() {		
						data = getContactInfo();
						
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								mLlLoading.setVisibility(View.GONE);
								if (mAdapter == null) {
									mAdapter = new SimpleAdapter(SelectContactActivity.this,
								    		data, R.layout.contact_item_view,
								    		new String[]{"name","phone"}, new int[]{R.id.tv_name,R.id.tv_phone});
									list_select_contact.setAdapter(mAdapter);
								} else {
									mAdapter.notifyDataSetChanged();
								}
							}
						});
					}
				}.start();  	
	    }
	 
	 
	 
	/**
	 * ��ȡ�ֻ��������ϵ��
	 */
	private List<Map<String, String>> getContactInfo() {
		//�����е���ϵ�˷��������������
       List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		//�õ�һ�����ݽ�����
		ContentResolver resolver=getContentResolver();
		//raw_contacts uri
		Uri uri=Uri.parse("content://com.android.contacts/raw_contacts");
		Uri uriData=Uri.parse("content://com.android.contacts/data");
		Cursor cursor=resolver.query(uri, new String[]{"contact_id"},
				null, null, null);
		//resolver.query(uri, projection, selection, selectionArgs, sortOrder)
		while(cursor.moveToNext()){
			String contact_id=cursor.getString(0);
			if(contact_id!=null){
				//�����ĳһ����ϵ��
				Map<String,String> map=new HashMap<String, String>();  
				Cursor dataCursor=resolver.query(uriData, new String[]{
						"data1","mimetype"}, 
						"contact_id=?",new String[]{contact_id},
						null);
				while(dataCursor.moveToNext()){
					String data1=dataCursor.getString(0);
					String minetype=dataCursor.getString(1);
					System.out.println("data1="+data1+"     minetype:"+minetype);
					if("vnd.android.cursor.item/name".equals(minetype)){
						//��ϵ�� ����
						map.put("name", data1);
					}
					else if("vnd.android.cursor.item/phone_v2".equals(minetype)){
						//��ϵ�˵ĵ绰����
						map.put("phone", data1);
					}
				}
				list.add(map);
				dataCursor.close();
 			}
		}
	    cursor.close();
		return list;
	}
}
