package com.jiangcheng.mobilesafe.test;

import java.util.List;

import com.jiangcheng.mobilesafe.domain.TaskInfo;
import com.jiangcheng.mobilesafe.engine.AppInfoProvider;
import com.jiangcheng.mobilesafe.engine.TaskInfoProvider;

import android.test.AndroidTestCase;

public class TestTaskInfoProvider extends AndroidTestCase {
	
	public void testGetTaskInfos(){
		List<TaskInfo> infos=TaskInfoProvider.getTaskInfos(getContext());
		for(TaskInfo info:infos){
			System.out.println(info.toString());
		}
		
	}
	

}
