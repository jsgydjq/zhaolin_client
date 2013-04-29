package com.deepred.zhaolin.entity;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

public class MyAppEntity implements Serializable{	
	
	private static final long serialVersionUID = -1040056093667027209L;
	/** 标题 */
	private String title;

	/** 内容 */
	private String text;

	/** 应用大小 */
	private long size;

	private byte[] imgByte;
	
	public void setImgByte(Drawable icon){
		Bitmap bitMap = ((BitmapDrawable)icon).getBitmap();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitMap.compress(Bitmap.CompressFormat.PNG, 100, baos); 
		imgByte = baos.toByteArray();
	}
	public Bitmap getImgBitmap(){
		return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
	}
	
	public String getImgString(){
		return Base64.encodeToString(imgByte, Base64.DEFAULT);
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}