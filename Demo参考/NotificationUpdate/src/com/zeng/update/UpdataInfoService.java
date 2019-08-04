package com.zeng.update;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.Context;


public class UpdataInfoService {
	private Context context ;
	

	public UpdataInfoService(Context context) {
		this.context = context;
	}


	/**
	 * 
	 * @param urlid 服务器路径string对应的id
	 * @return 更新的信息
	 */
	public UpdataInfo getUpdataInfo(int urlid) throws Exception{
		String path = context.getResources().getString(urlid);
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(2000);
		conn.setRequestMethod("GET");
		InputStream is = conn.getInputStream();
		return  UpdataInfoParser.getUpdataInfo(is);
	}
	
}
