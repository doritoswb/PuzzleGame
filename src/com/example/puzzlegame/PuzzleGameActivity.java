package com.example.puzzlegame;

import android.os.Bundle;
import android.widget.TextView;

public class PuzzleGameActivity extends BaseActivity {
	
	private PuzzleGameLayout gameLayout;
	private TextView levelTextView;
	private int level = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_game);
		
		String imagePath = getIntent().getStringExtra("imagePath");
		gameLayout = (PuzzleGameLayout) findViewById(R.id.id_puzzle_game);
		gameLayout.setImagePath(imagePath);
		
		levelTextView = (TextView) findViewById(R.id.level);
		levelTextView.setText("Level " + level);
	}
	
	public int getLevel(){
		return level;
	}
	
	public void setLevel(int level){
		this.level = level;
	}
}
