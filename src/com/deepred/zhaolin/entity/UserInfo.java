package com.deepred.zhaolin.entity;

import java.io.Serializable;

public class UserInfo implements Serializable{
	private static final long serialVersionUID = -4821131414778791971L;
	public long userPrimkey;
	public String userName;
	public String headUrl;
	public String status;
	public String mobileModel;
	public int collectCount;
	public int followCount;
}
