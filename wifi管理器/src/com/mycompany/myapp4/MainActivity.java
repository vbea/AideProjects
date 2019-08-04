package com.mycompany.myapp4;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.*;

public class MainActivity extends Activity
{
	private WifiManage wifiManage;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTheme(android.R.style.Theme_DeviceDefault_Light);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifi);
		ListView wifiInfosView=(ListView)findViewById(R.id.WifiInfosView);
		try
		{
			wifiManage = new WifiManage();
			WifiAdapter ad = new WifiAdapter(wifiManage.Read(),MainActivity.this);
			wifiInfosView.setAdapter(ad);
		}
		catch (Exception e)
		{
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	public class WifiAdapter extends BaseAdapter
	{
		List<WifiInfo> wifiInfos;
		LayoutInflater inflate;

		public WifiAdapter(List<WifiInfo> wifiInfo,Context context)
		{
			wifiInfos = wifiInfo;
			inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount()
		{
			return wifiInfos.size();
		}

		@Override
		public WifiInfo getItem(int position)
		{
			return wifiInfos.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			//if (convertView == null)
				convertView = inflate.inflate(android.R.layout.simple_list_item_1, null);
			TextView tv = (TextView)convertView.findViewById(android.R.id.text1);
			tv.setText("Wifi:"+wifiInfos.get(position).Ssid+"\n密码:"+wifiInfos.get(position).Password);
			return convertView;
		}

	}

}
