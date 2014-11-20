package com.example.puzzlegame;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

public class ImageSplitter {
	
	public static List<ImagePiece> split(Bitmap bitmap, int column){
		List<ImagePiece> pieces = new ArrayList<ImagePiece>(column * column);
		
		int imageWidth = bitmap.getWidth();
		int imageHeight = bitmap.getHeight();
		
		int pieceWidth = Math.min(imageHeight, imageWidth)/column;
		
		for(int i = 0; i < column; i++){
			for(int j = 0; j < column; j++){
				ImagePiece piece = new ImagePiece();
				piece.index = i * column + j;
				piece.bitmap = Bitmap.createBitmap(bitmap, j * pieceWidth, i * pieceWidth , pieceWidth, pieceWidth);
				pieces.add(piece);
			}
		}
		
		return pieces;
	}
}
