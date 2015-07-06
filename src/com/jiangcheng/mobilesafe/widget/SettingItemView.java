package com.jiangcheng.mobilesafe.widget;


import com.jiangcheng.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

/*
 * 我们自定义的组合控件，它里面有两个TextView，还有一个Checkobx
 */
public class SettingItemView extends RelativeLayout {
	private CheckBox cb_status;
	private TextView tv_desc;
	private TextView tv_title;
	
	private String desc_on;
	private String desc_off;
	/*
	 * 初始化布局文件
	 */
	private void iniView(Context context) {
		// TODO Auto-generated method stub
		//把一个布局文件---》View，并且加载在SettingItemView
		View.inflate(context, R.layout.setting_item_view, this);
		cb_status=(CheckBox) this.findViewById(R.id.cb_status);
		tv_desc=(TextView) this.findViewById(R.id.tv_desc);
		tv_title=(TextView) this.findViewById(R.id.tv_title);
	}
	
	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	   iniView(context);
	}

	/*
	 * 带有两个参数的构造方法，布局文件使用的时候要用
	 */
	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		iniView(context);
		String title=attrs.getAttributeValue("http://schemas.android.com/apk/res/com.jiangcheng.mobilesafe", "title");
		desc_on=attrs.getAttributeValue("http://schemas.android.com/apk/res/com.jiangcheng.mobilesafe", "desc_on");
		desc_off=attrs.getAttributeValue("http://schemas.android.com/apk/res/com.jiangcheng.mobilesafe", "desc_off");
		tv_title.setText(title);
		setDesc(desc_off);
	}

	

	public SettingItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		iniView(context);
	}
	
	/*
	 * 校验组合控件是否有选中
	 */

	public boolean isChecked(){
		return cb_status.isChecked();
	}
	
	/*
	 * 设置组合控件状态
	 */
	public void setChecked(boolean checked){
		cb_status.setChecked(checked);
		if(checked){
			setDesc(desc_on);
		}else{
			setDesc(desc_off);
		}
		
	}
	
	/*
	 * 设置组合控件的描述信息
	 */
	public void setDesc(String text){
		tv_desc.setText(text);
	}
	
	
}
