package com.zeng.update;

import android.app.Application;

public class MyApp extends Application {

	private boolean isDownload;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		isDownload = false;
	}

	public boolean isDownload() {
		return isDownload;
	}

	public void setDownload(boolean isDownload) {
		this.isDownload = isDownload;
	}
	
	
}
