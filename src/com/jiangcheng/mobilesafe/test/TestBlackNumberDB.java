package com.jiangcheng.mobilesafe.test;

import java.util.List;
import java.util.Random;

import com.jiangcheng.mobilesafe.db.BlackNumberDBOpenHelper;
import com.jiangcheng.mobilesafe.db.dao.BlackNumberDao;
import com.jiangcheng.mobilesafe.domain.BlackNumberInfo;

import android.test.AndroidTestCase;

public class TestBlackNumberDB extends AndroidTestCase {
    
	public void testCreateDB() throws Exception{
		BlackNumberDBOpenHelper helper=new BlackNumberDBOpenHelper(getContext());
		helper.getWritableDatabase();
	}
	public void testAdd() throws Exception{
		BlackNumberDao dao=new BlackNumberDao(getContext());
		//dao.add("110", "1");
		long basenumber=135223465;
		Random random =new Random();
		for(int i=0;i<100;i++){
			dao.add(String.valueOf(basenumber+i), String.valueOf(random.nextInt(3)+1));
		}
	}
	
	public void testFindAll() throws Exception{
		BlackNumberDao dao=new BlackNumberDao(getContext());
		 List<BlackNumberInfo> infos=dao.findAll();
		 for(BlackNumberInfo info:infos){
			 System.out.println(info.toString());
		 }
	}
	
	
	public void testDelete() throws Exception{
		BlackNumberDao dao=new BlackNumberDao(getContext());
		dao.delete("110");
	}
	public void testUpdate() throws Exception{
		BlackNumberDao dao=new BlackNumberDao(getContext());
		dao.update("110", "3");
	}
	public void testFind() throws Exception{
		BlackNumberDao dao=new BlackNumberDao(getContext());
		boolean result=dao.find("110");
		assertEquals(true,result);
	}
	 
	public void testquerycountAll() throws Exception{
		BlackNumberDao dao=new BlackNumberDao(getContext());
		int count=dao.querycountAll();
	}
	
	
	
	
	
}
