package com.highgo.opendbt.login.model;

import java.util.ArrayList;
import java.util.List;

public class NoticesListTO {

	private int count;// 所有通知消息个数
	private List<Notice> noticeList = new ArrayList<Notice>();// 通知list
	private List<Upcom> upcomList = new ArrayList<Upcom>();// 待办list

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<Notice> getNoticeList() {
		return noticeList;
	}

	public void setNoticeList(List<Notice> noticeList) {
		this.noticeList = noticeList;
	}

	public List<Upcom> getUpcomList() {
		return upcomList;
	}

	public void setUpcomList(List<Upcom> upcomList) {
		this.upcomList = upcomList;
	}

}
