package com.deepred.zhaolin.entity;

import java.io.Serializable;

import android.graphics.drawable.Drawable;

/**
 * ListView 内容实体类
 * 
 * @author Administrator
 */
public class DetailEntity implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1040056093667027209L;

	/** 布局ID */
	private int layoutID;

	/** 标题 */
	private String title;

	/** 内容 */
	private String text;

	/** user ID*/
	private String user;
	
	/** 图标URL */
	private String imgUrl;

	/** 应用大小 */
	private long size;

	/** 图标 */
	private Drawable Img;
	
	private Integer count;
	
	private boolean status;
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getImageUrl() {
		return imgUrl;
	}

	public void setImageUrl(String url) {
		imgUrl = url;
	}

	public int getLayoutID() {
		return layoutID;
	}

	public void setImg(Drawable d) {
		Img = d;
	}

	public Drawable getImg() {
		return Img;
	}

	public void setLayoutID(int layoutID) {
		this.layoutID = layoutID;
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