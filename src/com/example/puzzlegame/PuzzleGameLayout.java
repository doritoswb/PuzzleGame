package com.example.puzzlegame;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.BitmapFactory.Options;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PuzzleGameLayout extends RelativeLayout implements OnClickListener{
	
	private int column;
	
	private int width;
	private int padding;
	
	private int itemWidth;
	private int itemMargin = 3;
	private ImageView[] items;
	
	private Bitmap bitmap;
	private List<ImagePiece> pieces;
	
	private boolean once = false;
	
	private ImageView first = null;
	private ImageView second = null;
	
	private boolean isAnimating = false;
	private RelativeLayout animateLayer = null;
	
	private String imagePath;
	
    public PuzzleGameLayout(Context context) {
        this(context, null);
    }

    public PuzzleGameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PuzzleGameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        itemMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, itemMargin, getResources().getDisplayMetrics());
        padding = Math.min(Math.min(getPaddingLeft(), getPaddingRight()), Math.min(getPaddingTop(), getPaddingBottom()));
    }
    
    public void setImagePath(String path){
    	imagePath = path;
    }
    
    public String getImagePath(){
    	return imagePath;
    }
    
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    	
    	width = Math.min(getMeasuredHeight(), getMeasuredWidth()); 	
    	initialize();
    	setMeasuredDimension(width, width);
    }
    
    public void initialize(){    	
    	if(!once){
        	PuzzleGameActivity activity = (PuzzleGameActivity)getContext();
        	PuzzleGameApplication application = (PuzzleGameApplication)activity.getApplication();
        	
        	int level = application.getLevel();
        	column = level + 2;
    		
    		initImagePieces();
    		initItems();
    		once = true;
    	}
    }
    
    private void initImagePieces(){
    	if(bitmap == null){
    		//bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.beauty);
            Options options = new Options();  
            // 不去真正解析图片，只是获取图片的宽高  
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imagePath, options);  
            int imageWidth = options.outWidth;  
            int imageHeight = options.outHeight;    
            
            int layoutWidth = getMeasuredWidth();
            int layoutHeight = getMeasuredHeight();  
            int scale = 1;    
            
            if (imageHeight > layoutHeight || imageWidth > layoutWidth) {
        		if (imageWidth > imageHeight) {
        			scale = Math.round((float)imageHeight / (float)layoutHeight);
        		} else {
        			scale = Math.round((float)imageWidth / (float)layoutWidth);
        		}    
        	}  
            // 真正解析图片  
            options.inJustDecodeBounds = false;  
            // 设置采样率  
            options.inSampleSize = scale;  
            bitmap = BitmapFactory.decodeFile(imagePath,options);
    	}
    	
    	pieces = ImageSplitter.split(bitmap, column);
    	Collections.sort(pieces, new Comparator<ImagePiece>(){
    		@Override
    		public int compare(ImagePiece lhs, ImagePiece rhs){
    			return Math.random() > 0.5? 1: -1;
    		}
    	});
    }
    
    private void initItems(){
    	
    	itemWidth = (width - padding * 2 - itemMargin * (column - 1))/column;
    	items = new ImageView[column * column];
    	
    	for(int i = 0; i < items.length; i++){
    		ImageView item = new ImageView(getContext());
    		item.setOnClickListener(this);
    		
    		item.setImageBitmap(pieces.get(i).bitmap);
    		item.setId(i + 1);
    		item.setTag(i + "_" + pieces.get(i).index);
    		
    		RelativeLayout.LayoutParams params = new LayoutParams(itemWidth, itemWidth);

            // 如果不是第一列 , 设置横向边距
            if (i % column != 0)  
            {  
            	params.leftMargin = itemMargin;
            	params.addRule(RelativeLayout.RIGHT_OF, items[i - 1].getId());  
            }  
            // 如果不是第一行，设置纵向边距  
            if ((i + 1) > column)  
            {  
            	params.topMargin = itemMargin;  
            	params.addRule(RelativeLayout.BELOW, items[i - column].getId());
            }
            items[i] = item;
            addView(item, params); 
    	}
    }
    
    @Override
    public void onClick(View v){
    	
    	if (isAnimating)  
            return;  
    	
    	if(first == v){
    		first.setColorFilter(null);
    		first = null;
    		return;
    	}
    	
    	if(first == null){
    		first = (ImageView) v;
    		first.setColorFilter(Color.parseColor("#55ff0000"));
    	}else{
    		second = (ImageView) v;
        	exchangeImage();
    	}
    }
    
    private void exchangeImage(){
    	first.setColorFilter(null);
    	setupAnimation();
    	
        ImageView firstImage = new ImageView(getContext());  
        firstImage.setImageBitmap(pieces.get(getImageIndexByTag((String) first.getTag())).bitmap);
        LayoutParams lp = new LayoutParams(itemWidth, itemWidth);  
        lp.leftMargin = first.getLeft() - padding;  
        lp.topMargin = first.getTop() - padding;  
        firstImage.setLayoutParams(lp);  
        animateLayer.addView(firstImage);  
         
        ImageView secondImage = new ImageView(getContext());  
        secondImage.setImageBitmap(pieces  
                .get(getImageIndexByTag((String) second.getTag())).bitmap);  
        LayoutParams lp2 = new LayoutParams(itemWidth, itemWidth);  
        lp2.leftMargin = second.getLeft() - padding;  
        lp2.topMargin = second.getTop()- padding;  
        secondImage.setLayoutParams(lp2);  
        animateLayer.addView(secondImage); 
    	       
        TranslateAnimation anim = new TranslateAnimation(0, second.getLeft()  
                - first.getLeft(), 0, second.getTop() - first.getTop());  
        anim.setDuration(300);  
        anim.setFillAfter(true);  
        firstImage.startAnimation(anim);  
  
        TranslateAnimation animSecond = new TranslateAnimation(0,  
                first.getLeft() - second.getLeft(), 0, first.getTop()  
                        - second.getTop());  
        animSecond.setDuration(300);  
        animSecond.setFillAfter(true);  
        secondImage.startAnimation(animSecond);  
         
        anim.setAnimationListener(new AnimationListener()  
        {  
            @Override  
            public void onAnimationStart(Animation animation)  
            {  
                isAnimating = true;  
                first.setVisibility(INVISIBLE);  
                second.setVisibility(INVISIBLE);  
            }  
  
            @Override  
            public void onAnimationRepeat(Animation animation)  
            {  
  
            }  
  
            @Override  
            public void onAnimationEnd(Animation animation)  
            {  
                String firstTag = (String) first.getTag();  
                String secondTag = (String) second.getTag();  
  
                String[] firstParams = firstTag.split("_");  
                String[] secondParams = secondTag.split("_");  
  
                first.setImageBitmap(pieces.get(Integer  
                        .parseInt(secondParams[0])).bitmap);  
                second.setImageBitmap(pieces.get(Integer  
                        .parseInt(firstParams[0])).bitmap);  
  
                first.setTag(secondTag);  
                second.setTag(firstTag);  
                
                first.setVisibility(VISIBLE);  
                second.setVisibility(VISIBLE);  
                
            	first = second = null;
                //animateLayer.removeAllViews();  
                checkSuccess();  
                isAnimating = false;  
            }  
        });
    }
    
    private void setupAnimation(){
    	if(animateLayer == null){
    		animateLayer = new RelativeLayout(getContext());
    		addView(animateLayer);
    	}
    	animateLayer.removeAllViews();
    }
    
    private int getImageIndexByTag(String tag)  
    {  
        String[] split = tag.split("_");  
        return Integer.parseInt(split[0]);  
    } 
    
    private void checkSuccess()  
    {  
        boolean isSuccess = true;  
        for (int i = 0; i < items.length; i++)  
        {  
            ImageView first = items[i];  
            if (getIndexByTag((String) first.getTag()) != i)  
            {  
                isSuccess = false;  
            }  
        }  
  
        if (isSuccess)  
        {  
            Toast.makeText(getContext(), "Success , Level Up !",  
                    Toast.LENGTH_LONG).show();  
            nextLevel();  
        }  
    }  
  
    private int getIndexByTag(String tag)  
    {  
        String[] split = tag.split("_");  
        return Integer.parseInt(split[1]);  
    } 
    
    private void nextLevel(){
    	once = false;
    	this.removeAllViews();
    	if(animateLayer != null){
    		this.addView(animateLayer);
    	}
    	
    	PuzzleGameActivity activity = (PuzzleGameActivity)getContext();
    	PuzzleGameApplication application = (PuzzleGameApplication)activity.getApplication();
    	
    	int level = application.getLevel();
    	level++;
    	application.setLevel(level);
 
    	application.saveUserData();
    	
    	TextView textView = (TextView) activity.findViewById(R.id.level);
    	textView.setText("Level " + level);
    	initialize();
    }
}
