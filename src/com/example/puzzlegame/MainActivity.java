package com.example.puzzlegame;

import com.ant.liao.GifView;
import com.ant.liao.GifView.GifImageType;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends BaseActivity implements OnClickListener{

	private GifView exampleGif;
	private Button resumeGame;
	private Button newGame;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		  
		exampleGif = (GifView) findViewById(R.id.example_gif); 
		exampleGif.setGifImage(R.drawable.puzzle_example); 
		
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.puzzle_example);
	    int width = bitmap.getWidth();
	    int height = bitmap.getHeight();
	    exampleGif.setShowDimension(width, height);	     
	    exampleGif.setGifImageType(GifImageType.COVER);
	    
	    resumeGame = (Button)findViewById(R.id.resume_game);
	    resumeGame.setOnClickListener(this);
	    
	    newGame = (Button)findViewById(R.id.new_game);
	    newGame.setOnClickListener(this);
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		PuzzleGameApplication application = (PuzzleGameApplication)this.getApplication();
		if(!application.isUserDataFileExist())
	    {
	    	resumeGame.setVisibility(View.GONE);
	    }
	}
	
	@Override
	public void onClick(View v){
		switch(v.getId()){
			case R.id.resume_game:{
				PuzzleGameApplication application = (PuzzleGameApplication)this.getApplication();
				UserData data = application.loadUserData();
				if(data != null){
					Intent intentGame = new Intent(this, PuzzleGameActivity.class);
					intentGame.putExtra("imagePath", data.getImagePath());
					startActivity(intentGame);
				}
				break;
			}
			case R.id.new_game:{
				Intent intent = new Intent(this, SelectImageActivity.class);
				startActivity(intent);
				break;
			}
			default:{
				break;
			}
		}
	}
	
	@Override  
    public void onBackPressed() {  
        this.finishAll(); 
        super.onBackPressed();  
    }
}
