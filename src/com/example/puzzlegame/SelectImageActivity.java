package com.example.puzzlegame;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class SelectImageActivity extends BaseActivity implements OnClickListener{
	
	private ImageButton selectImage;
	private ImageView imageview;
	private Button start;
	
	private String imagePath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectimage);
		
		selectImage = (ImageButton) findViewById(R.id.select_image);
		selectImage.setOnClickListener(this);
		
		start = (Button) findViewById(R.id.start);
		start.setOnClickListener(this);
		
		imageview = (ImageView) findViewById(R.id.imageview);
	}
	
	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.select_image:
	        Intent intent = new Intent();  
	        intent.setAction(Intent.ACTION_PICK);  
	        intent.setType("image/*");  
	        startActivityForResult(intent, 0); 
			break;
		case R.id.start:
			PuzzleGameApplication application = (PuzzleGameApplication) this.getApplication();
			application.setLevel(1);
			
			Intent intentGame = new Intent(this, PuzzleGameActivity.class);
			intentGame.putExtra("imagePath", imagePath);
			startActivity(intentGame);
			break;
		default:
			break;
		}
	}
	
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if(requestCode == 0){
        	if(resultCode == RESULT_OK){
            	if (data != null) {  
                    Uri uri = data.getData();  
                    // 通过路径加载图片  
                    //这里省去了图片缩放操作，如果图片过大，可能会导致内存泄漏  
                    //图片缩放的实现，请看：http://blog.csdn.net/reality_jie_blog/article/details/16891095  
                    if(uri != null){
                        Bitmap bitmap = getBitmap(uri);
                        if(bitmap != null){
                        	imageview.setImageBitmap(bitmap);
                            start.setVisibility(View.VISIBLE);
                        } 
                    }          
                } 
        	}
        }
 
        super.onActivityResult(requestCode, resultCode, data);  
    } 
	
    private Bitmap getBitmap(Uri uri){ 	
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        
        Cursor cursor = getContentResolver().query(uri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        
        imagePath = picturePath;
        cursor.close();
        
        Options options = new Options();  
        // 不去真正解析图片，只是获取图片的宽高  
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, options);  
        int imageWidth = options.outWidth;  
        int imageHeight = options.outHeight;
    
        int scale = 1;  
        if (imageHeight > imageview.getHeight() || imageWidth > imageview.getWidth()) {
    		if (imageWidth > imageHeight) {
    			scale = Math.round((float)imageHeight / (float)imageview.getHeight());
    		} else {
    			scale = Math.round((float)imageWidth / (float)imageview.getWidth());
    		}    
    	}   
        // 真正解析图片  
        options.inJustDecodeBounds = false;
        // 设置采样率  
        options.inSampleSize = scale;  
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);  
        return bitmap;
    }
    
	@Override
	protected void onDestroy(){
		super.onDestroy();
	}
}
