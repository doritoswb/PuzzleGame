package com.example.puzzlegame;

public class UserData {

	private String imagePath;
	private int level;
	
	public UserData(){
		
	}
	
	public UserData(String path, int level){
		this.imagePath = path;
		this.level = level;
	}
	
	public void setImagePath(String path){
		this.imagePath = path;
	}
	
	public String getImagePath(){
		return imagePath;
	}
	
	public void setLevel(int level){
		this.level = level;
	}
	
	public int getLevel(){
		return level;
	}
}
