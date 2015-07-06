package com.jiangcheng.mobilesafe.db.dao;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jiangcheng.mobilesafe.db.BlackNumberDBOpenHelper;
import com.jiangcheng.mobilesafe.domain.BlackNumberInfo;

/**
 * 病毒数据库查询业务类
 */
public class AntivirsuDao {
	private static final String TAG="AntivirsuDao";
	/**
	 * 查询一个md5是否在病毒数据库里面存在
	 */
	 public static boolean isVirus(String md5){
		String path="/data/data/com.jiangcheng.mobilesafe/files/antivirus.db";
		boolean result = false;
		Cursor cursor = null;
		SQLiteDatabase db = null;
		//打开病毒数据库文件
		try {
			db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
			
			cursor = db.rawQuery("select * from datable where md5=?", new String[]{md5});
			if(cursor.moveToNext()){
				result = true;
			}
		} catch (Exception e) {
			 Log.e(TAG, e.getMessage());
		} finally {
			cursor.close();
			db.close();
		}		
		return result;
	}
}
