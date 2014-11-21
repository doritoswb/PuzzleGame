package com.example.puzzlegame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class PuzzleGameActivity extends BaseActivity {
	
	private PuzzleGameLayout gameLayout;
	private TextView levelTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_game);
		
		PuzzleGameApplication application = (PuzzleGameApplication)this.getApplication();
		
		String imagePath = getIntent().getStringExtra("imagePath");
		gameLayout = (PuzzleGameLayout) findViewById(R.id.id_puzzle_game);
		gameLayout.setImagePath(imagePath);
		
		application.setImagePath(imagePath);
		
		int level = application.getLevel();
		levelTextView = (TextView) findViewById(R.id.level);
		levelTextView.setText("Level " + level);
		
		application.saveUserData();
	}
	
	@Override  
    public void onBackPressed() {  
/*        Intent intent = new Intent(this, MainActivity.class); 
        startActivity(intent);*/
        super.onBackPressed();  
    }  
}
