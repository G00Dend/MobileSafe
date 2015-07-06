package com.jiangcheng.mobilesafe.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import com.jiangcheng.mobilesafe.domain.SmsInfo;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

/*
 * ���ŵĹ�����
 */
public class SmsUtils {

	
	
	/**
	 * ���ݶ��ŵĻص��ӿ�
	 */
	public interface BackUpCallBack{
		/**
		 * ��ʼ���ݵ�ʱ�����ý��ȵ����ֵ
		 * @param max  �ܽ���
		 */
		public void beforeBackup(int max);
		/**
		 * ���ݹ��������ӽ���
		 * @param progress ��ǰ����
		 */
		public void onSmsBackup(int progress);
		
		
	}
	
	
/**
 * �����û��Ķ���
 * @param context������
 * @param pd  �������Ի���
 * 
 */
	public static void backupSms(Context context,BackUpCallBack callBack) throws Exception{
		Cursor cursor = null;
		ContentResolver resolver=context.getContentResolver();
		String path = Environment.getExternalStorageDirectory().getPath() + "/SMSBackup";
		File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        File backupFile = new File(path, "message.xml");
		FileOutputStream fos=new FileOutputStream(backupFile);
		//���û��Ķ���һ��һ��������������һ���ĸ�ʽд���ļ���ȥ
		XmlSerializer serializer=Xml.newSerializer(); //��ȡxml�ļ�������
		//��ʼ��������
		serializer.setOutput(fos,"utf-8");
		serializer.startDocument("utf-8", true);
		serializer.startTag(null, "sms");
        try{
		Uri uri=Uri.parse("content://sms/");
		String[] projection = new String[] { SmsField.ADDRESS, SmsField.PERSON, SmsField.DATE, SmsField.PROTOCOL,
                                   SmsField.READ, SmsField.STATUS, SmsField.TYPE, SmsField.REPLY_PATH_PRESENT,
                                   SmsField.BODY,SmsField.LOCKED,SmsField.ERROR_CODE,SmsField.SEEN};
		cursor = resolver.query(uri, projection, null, null, "_id asc");
		//��ʼ���ݵ�ʱ�����ý����������ֵ
		int max=cursor.getCount();
		callBack.beforeBackup(max);
		serializer.attribute(null, "max", max+"");		
		int process=0;
			if (cursor.moveToFirst()) {
                // �鿴���ݿ�sms���֪ subject��service_centerʼ����null��������Ͳ���ȡ���ǵ������ˡ�
                String address;
                String person;
                String date;
                String protocol;
                String read;
                String status;
                String type;
                String reply_path_present;
                String body;
                String locked;
                String error_code;
                String seen;
                do {
                    // ���address == null��xml�ļ����ǲ������ɸ����Ե�,Ϊ�˱�֤����ʱ�������ܹ���������һһ��Ӧ������Ҫ��֤���е�item��ǵ�����������˳����һ�µ�
                    address = cursor.getString(cursor.getColumnIndex(SmsField.ADDRESS));
                    if (address == null) {
                        address = "";
                    }
                    person = cursor.getString(cursor.getColumnIndex(SmsField.PERSON));
                    if (person == null) {
                        person = "";
                    }
                    date = cursor.getString(cursor.getColumnIndex(SmsField.DATE));
                    if (date == null) {
                        date = "";
                    }
                    protocol = cursor.getString(cursor.getColumnIndex(SmsField.PROTOCOL));
                    if (protocol == null) {// Ϊ�˱���xml����
                        protocol = "";
                    }
                    read = cursor.getString(cursor.getColumnIndex(SmsField.READ));
                    if (read == null) {
                        read = "";
                    }
                    status = cursor.getString(cursor.getColumnIndex(SmsField.STATUS));
                    if (status == null) {
                        status = "";
                    }
                    type = cursor.getString(cursor.getColumnIndex(SmsField.TYPE));
                    if (type == null) {
                        type = "";
                    }
                    reply_path_present = cursor.getString(cursor.getColumnIndex(SmsField.REPLY_PATH_PRESENT));
                    if (reply_path_present == null) {// Ϊ�˱���XML����
                        reply_path_present = "";
                    }
                    body = cursor.getString(cursor.getColumnIndex(SmsField.BODY));
                    if (body == null) {
                        body = "";
                    }
                    locked = cursor.getString(cursor.getColumnIndex(SmsField.LOCKED));
                    if (locked == null) {
                        locked = "";
                    }
                    error_code = cursor.getString(cursor.getColumnIndex(SmsField.ERROR_CODE));
                    if (error_code == null) {
                        error_code = "";
                    }
                    seen = cursor.getString(cursor.getColumnIndex(SmsField.SEEN));
                    if (seen == null) {
                        seen = "";
                    }
                    // ����xml�ӱ��
                    // ��ʼ���
                    serializer.startTag(null, "item");
                    // ��������
                    serializer.attribute(null, SmsField.ADDRESS, address);
                    serializer.attribute(null, SmsField.PERSON, person);
                    serializer.attribute(null, SmsField.DATE, date);
                    serializer.attribute(null, SmsField.PROTOCOL, protocol);
                    serializer.attribute(null, SmsField.READ, read);
                    serializer.attribute(null, SmsField.STATUS, status);
                    serializer.attribute(null, SmsField.TYPE, type);
                    serializer.attribute(null, SmsField.REPLY_PATH_PRESENT, reply_path_present);
                    serializer.attribute(null, SmsField.BODY, body);
                    serializer.attribute(null, SmsField.LOCKED, locked);
                    serializer.attribute(null, SmsField.ERROR_CODE, error_code);
                    serializer.attribute(null, SmsField.SEEN, seen);
                    // �������
                    serializer.endTag(null, "item");
                    process++;
                } while (cursor.moveToNext());
            } else { 
                Toast.makeText(context,"���������ļ�ʧ��!", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.d("SQLiteException:", e.getMessage());
        }finally {
            if(cursor != null) {
                cursor.close();//�ֶ��ر�cursor����ʱ����
            }
        }
        serializer.endTag(null, "sms");
        serializer.endDocument();
        fos.flush();
        fos.close();
	}
	/*
	 * ��ԭ����
	 * @param context
	 * @param flag  �Ƿ�����ԭ������
	 */
	public static void restoreSms(Context context,boolean flag){
		List<SmsInfo> smsInfos = null;
		ContentResolver conResolver;
		Uri uri=Uri.parse("content://sms/");
		if(flag){
			context.getContentResolver().delete(uri, null, null);

		}
		smsInfos = getSmsItemsFromXml(context);
		conResolver = context.getContentResolver();
		
		for (SmsInfo item : smsInfos) {
			Cursor cursor = conResolver.query(uri, new String[] { SmsField.DATE }, SmsField.DATE + "=?",
                    new String[] { item.getDate() }, null);
			if (!cursor.moveToFirst()) {// û�и�������
                
                ContentValues values = new ContentValues();
                values.put(SmsField.ADDRESS, item.getAddress());
                // ����ǿ��ַ���˵��ԭ����ֵ��null���������ﻹԭΪnull�������ݿ�
                values.put(SmsField.PERSON, item.getPerson().equals("") ? null : item.getPerson());
                values.put(SmsField.DATE, item.getDate());
                values.put(SmsField.PROTOCOL, item.getProtocol().equals("") ? null : item.getProtocol());
                values.put(SmsField.READ, item.getRead());
                values.put(SmsField.STATUS, item.getStatus());
                values.put(SmsField.TYPE, item.getType());
                values.put(SmsField.REPLY_PATH_PRESENT, item.getReply_path_present().equals("") ? null : item.getReply_path_present());
                values.put(SmsField.BODY, item.getBody());
                values.put(SmsField.LOCKED, item.getLocked());
                values.put(SmsField.ERROR_CODE, item.getError_code());
                values.put(SmsField.SEEN, item.getSeen());
                conResolver.insert(Uri.parse("content://sms"), values);
            }
            cursor.close();
		}
		
	}
	
	
	/**
	 * ��ȡxml�ļ�,��SmsInfos��ȥ
	 * @param context
	 * @return
	 */
    private static List<SmsInfo> getSmsItemsFromXml(Context context){
    	List<SmsInfo> smsInfos = null;
        SmsInfo smsItem = null;
        XmlPullParser parser = Xml.newPullParser();
        String absolutePath = Environment.getExternalStorageDirectory() + "/SMSBackup/message.xml";
        File file = new File(absolutePath);
        if (!file.exists()) {
 
            Looper.prepare();
            Toast.makeText(context, "message.xml���ű����ļ�����sd����", 1).show();
            Looper.loop();//�˳��߳�
//          return null;
        }
        try {
            FileInputStream fis = new FileInputStream(file);
            parser.setInput(fis, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                case XmlPullParser.START_DOCUMENT:
                    smsInfos = new ArrayList<SmsInfo>();
                    break;
 
                case XmlPullParser.START_TAG: // ���������ʼ��ǣ���<smsItems>,<smsItem>��
                    if ("item".equals(parser.getName())) {
                        smsItem = new SmsInfo();
 
                        smsItem.setAddress(parser.getAttributeValue(0));
                        smsItem.setPerson(parser.getAttributeValue(1));
                        smsItem.setDate(parser.getAttributeValue(2));
                        smsItem.setProtocol(parser.getAttributeValue(3));
                        smsItem.setRead(parser.getAttributeValue(4));
                        smsItem.setStatus(parser.getAttributeValue(5));
                        smsItem.setType(parser.getAttributeValue(6));
                        smsItem.setReply_path_present(parser.getAttributeValue(7));
                        smsItem.setBody(parser.getAttributeValue(8));
                        smsItem.setLocked(parser.getAttributeValue(9));
                        smsItem.setError_code(parser.getAttributeValue(10));
                        smsItem.setSeen(parser.getAttributeValue(11));
 
                    }
                    break;
                case XmlPullParser.END_TAG:// �������,��</smsItems>,</smsItem>��
                    if ("item".equals(parser.getName())) {
                        smsInfos.add(smsItem);
                        smsItem = null;
                    }
                    break;
                }
                event = parser.next();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            Looper.prepare();
            Toast.makeText(context, "���Żָ�����", 1).show();
            Looper.loop();
            e.printStackTrace();
             
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            Looper.prepare();
            Toast.makeText(context, "���Żָ�����", 1).show();
            Looper.loop();
            e.printStackTrace();       
             
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Looper.prepare();
            Toast.makeText(context, "���Żָ�����", 1).show();
            Looper.loop();
            e.printStackTrace();
        }
        return smsInfos;
    }
	
}
