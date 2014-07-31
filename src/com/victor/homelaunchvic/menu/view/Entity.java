package com.victor.homelaunchvic.menu.view;


public class Entity {
	public static final int C_iMenuFrom_third=101;
	public static final int C_iMenuThird_Html5=1;
	public static final int C_iMenuThird_Native=2;
	private String name;
	private int id;
	private int count = -1;
	private boolean isNews;
	private int type;
	private int color;
	private boolean isHide;
	private boolean isBusiness;
	private long sourceID;
	private int from;
	private int appType;
	private String http;
	private String action;
	private String downLoadAddress;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public boolean isNews() {
		return isNews;
	}

	public void setNews(boolean isNews) {
		this.isNews = isNews;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public boolean isHide() {
		return isHide;
	}

	public void setHide(boolean isHide) {
		this.isHide = isHide;
	}

	public boolean isBusiness() {
		return isBusiness;
	}

	public void setBusiness(boolean isBusiness) {
		this.isBusiness = isBusiness;
	}

	public long getSourceID() {
		return sourceID;
	}

	public void setSourceID(long sourceID) {
		this.sourceID = sourceID;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getAppType() {
		return appType;
	}

	public void setAppType(int appType) {
		this.appType = appType;
	}

	public String getHttp() {
		return http;
	}

	public void setHttp(String http) {
		this.http = http;
	}

	public String getDownLoadAddress() {
		return downLoadAddress;
	}

	public void setDownLoadAddress(String downLoadAddress) {
		this.downLoadAddress = downLoadAddress;
	}

}
