package com.deepred.zhaolin.entity;

import java.util.List;


public class NewUserDetail {
	public long primKey;
	public UserInfo userInfo;
	public int followerCount; // in db, with a list of followers
	public NewUserStatus userStatus; // in db, a list of all status
	public List<NewAppInfo> appList;
	public List<NewActionDetail> actionList;
}
