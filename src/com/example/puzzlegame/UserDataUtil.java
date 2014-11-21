package com.example.puzzlegame;

import java.io.InputStream;
import java.io.OutputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

public class UserDataUtil {
	
	public static UserData loadUserData(InputStream inStream) throws Exception {
        UserData data =  null;
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inStream, "UTF-8");
        int eventType = parser.getEventType();
        while(eventType != XmlPullParser.END_DOCUMENT){//�ж��ļ��Ƿ����ļ��Ľ�β��END_DOCUMENT�ļ���β����
            switch(eventType){
            case XmlPullParser.START_DOCUMENT://�ļ���ʼ��START_DOCUMENT�ļ���ʼ��ʼ����
                break;
                
            case XmlPullParser.START_TAG://Ԫ�ر�ǩ��ʼ��START_TAG��ǩ��ʼ����
                String name = parser.getName();
                if("userData".equals(name)){
                    data = new UserData();
                }
                
                if(data != null){
                    if("imagePath".equals(name)){
                        data.setImagePath(parser.nextText());
                    }
                    else if("level".equals(name)){
                    	data.setLevel(Integer.parseInt(parser.nextText()));
                    }  
                }            
                break;
                
            case XmlPullParser.END_TAG://Ԫ�ر�ǩ������END_TAG��������
                break;
            }
            //��ȡ��ǰԪ�ر�ǩ������
            eventType = parser.next();
        }
        return data;
	}
	
	public static void saveUserData(UserData data, OutputStream out) throws Exception {
	    XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(out, "UTF-8");
        serializer.startDocument("UTF-8", true);
        serializer.text("\n"); 
           
    	serializer.startTag(null, "userData");
    	serializer.text("\n"); 
    	
    	serializer.text("\t"); 
        serializer.startTag(null, "imagePath");            
        serializer.text(data.getImagePath());
        serializer.endTag(null, "imagePath");
        serializer.text("\n");
        
        serializer.text("\t"); 
        serializer.startTag(null, "level");            
        serializer.text(Integer.toString(data.getLevel()));
        serializer.endTag(null, "level"); 
        serializer.text("\n"); 
        
        serializer.endTag(null, "userData"); 
        serializer.text("\n"); 

        serializer.endDocument();
        out.flush();
        out.close();
	}
}
