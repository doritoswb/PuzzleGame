package com.example.puzzlegame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.app.Application;

public class PuzzleGameApplication extends Application {
	private String USER_DATA_FILE_NAME = "userData.xml";
	private String FilesFolderPath;
	
	private String imagePath;
	private int level = 1;
	
	@Override
    public void onCreate() {
		super.onCreate();
		File file = getFilesDir();
		FilesFolderPath = file.getPath();
    }
	
	public void setImagePath(String imagePath){
		this.imagePath = imagePath;
	}
	
	public String getImagePath(){
		return this.imagePath;
	}
	
	public void setLevel(int level){
		this.level = level;
	}
	
	public int getLevel(){
		return level;
	}
	
	public void saveUserData(){		 
		 try{
			 FileOutputStream fileOutputStream = new FileOutputStream(FilesFolderPath + USER_DATA_FILE_NAME);		 
			 UserData data = new UserData(imagePath, level);
			 UserDataUtil.saveUserData(data, (OutputStream)fileOutputStream);
		 } catch (Exception e){
			 e.printStackTrace();
		 }
	}
	
	public UserData loadUserData(){
		try{
			FileInputStream fileInputStream = new FileInputStream(FilesFolderPath + USER_DATA_FILE_NAME);
			UserData data = UserDataUtil.loadUserData(fileInputStream);
			imagePath = data.getImagePath();
			level = data.getLevel();
			return data;
			
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean isUserDataFileExist(){
		File file = new File(FilesFolderPath + USER_DATA_FILE_NAME);
		if(file.exists()){
			return true;
		} else {
			return false;
		}		
	}
}
