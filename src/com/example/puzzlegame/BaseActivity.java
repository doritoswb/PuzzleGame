package com.example.puzzlegame;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity {

	private List<Activity> activities = new ArrayList<Activity>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activities.add(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(activities.contains(this)){
			activities.remove(this);
		}
	}
	
	public void finishAll(){
		for(Activity activity : activities){
			activities.remove(activity);
		}
	}
}
