package com.vbea.talk;

import java.net.URLEncoder;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ScrollView;
import android.widget.LinearLayout;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.HttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.impl.client.DefaultHttpClient;

public class MainActivity extends Activity
{
	private static int CHILD_SEND_ID = 0;
	private static int CHILD_FROM_ID = 0;
	private static int CHILD_ERROR_ID = 0;
	private long exitTime = 0;
	private EditText edit;
	private TextView txtNouse;
	private ScrollView scl_msg;
	private LinearLayout adapter;
	private LayoutInflater inflate;
	String result = "";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		edit = (EditText) findViewById(R.id.txtEdit);
		adapter = (LinearLayout) findViewById(R.id.msgAdapter);
		Button btnSend = (Button) findViewById(R.id.btnSend);
		scl_msg = (ScrollView) findViewById(R.id.scl_msg);
		txtNouse = (TextView) findViewById(R.id.txt_nouse);
		inflate = getLayoutInflater();
		addFromMsg("您好！");
		
		btnSend.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (!isNetWork())
				{
					addErrorMsg("网络连接失败，请检查你的网络");
					mHandler.sendEmptyMessage(7);
					return;
				}
				addSendMsg(edit.getText().toString());
				mHandler.sendEmptyMessage(7);
				if(edit.getText().toString().trim().length() == 0)
				{
					addFromMsg("只要输入文字就可以跟我对话！");
					mHandler.sendEmptyMessage(7);
					return;
				}
				try
				{
					String msg = URLEncoder.encode(edit.getText().toString(), "utf-8");
					String requesturl = "http://www.tuling123.com/openapi/api?key=58ef3070adf8b6ad9903256b12f479db&info="+msg;
					//text.setText(requesturl);
					edit.setText("");
					final HttpGet httpget = new HttpGet(requesturl);
					final HttpClient httpClient = new DefaultHttpClient();
					//Toast.makeText(getApplicationContext(),INFO,Toast.LENGTH_SHORT).show();
					
					new Thread()
					{
						@Override
						public void run()
						{
							try
							{
								//mHandler.sendEmptyMessage(1);
								HttpResponse response = httpClient.execute(httpget);
								//mHandler.sendEmptyMessage(2);
								if(response.getStatusLine().getStatusCode()==200&&response != null)//200即正确的返回码
								{
									HttpEntity entity = response.getEntity();
									InputStream is = entity.getContent();
									InputStreamReader isr = new InputStreamReader(is);
									BufferedReader reader = new BufferedReader(isr);
									//String result = EntityUtils.toString(response.getEntity());
									if (getJson(reader.readLine()))
										mHandler.sendEmptyMessage(4);
									else
										mHandler.sendEmptyMessage(3);
								}
								//super.run();
							}
							catch(Exception e)
							{
								e.printStackTrace();
								mHandler.sendEmptyMessage(5);
							}
						}
					}.start();
				}
				catch(IOException e)
				{
					e.printStackTrace();
					mHandler.sendEmptyMessage(6);
				}
			}
		});
	}
	
	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
				case 1:
					Toast.makeText(getApplicationContext(),"发起请求",Toast.LENGTH_SHORT).show();
					break;
				case 2:
					Toast.makeText(getApplicationContext(),"判断数据",Toast.LENGTH_SHORT).show();
					break;
				case 3:
					//Toast.makeText(getApplicationContext(),"获取数据",Toast.LENGTH_SHORT).show();
					addErrorMsg(result);
					mHandler.sendEmptyMessage(7);
					break;
				case 4:
					//Toast.makeText(getApplicationContext(),"返回结果："+result,Toast.LENGTH_SHORT).show();
					addFromMsg(result);
					mHandler.sendEmptyMessage(7);
					break;
				case 5:
					Toast.makeText(getApplicationContext(),"请求异常",Toast.LENGTH_SHORT).show();
					break;
				case 6:
					Toast.makeText(getApplicationContext(),"IO异常",Toast.LENGTH_SHORT).show();
					break;
				case 7:
					scl_msg.scrollTo(0,adapter.getMeasuredHeight() - scl_msg.getHeight() + edit.getHeight() + txtNouse.getHeight());
					break;
			}
			super.handleMessage(msg);
		}
	};
	private boolean getJson(String json)
	{
		String jsonStr = "{'name':" + json + "}";
		String code = "";
		String jstr = "";
		boolean state = true;
		try
		{
			JSONObject jsobj = new JSONObject(jsonStr);
			JSONObject jo = jsobj.getJSONObject("name");
			code= jo.getString("code");
			switch(code)
			{
				case "100000"://文本类数据
					jstr = jo.getString("text");
					break;
				case "200000"://网址类数据
					jstr = jo.getString("text") + "\n" + jo.getString("url");
					break;
				case "301000"://小说
					jstr = jo.getString("text");
					break;
				case "302000"://新闻
					jstr = jo.getString("text");
					break;
				case "304000"://应用、软件、下载
					jstr = jo.getString("text");
					break;
				case "305000"://列车
					jstr = jo.getString("text");
					break;
				case "306000"://航班
					jstr = jo.getString("text");
					break;
				case "307000"://团购
					jstr = jo.getString("text");
					break;
				case "308000"://优惠
					jstr = jo.getString("text");
					break;
				case "309000"://酒店
					jstr = jo.getString("text");
					break;
				case "310000"://彩票
					jstr = jo.getString("text");
					break;
				case "311000"://价格
					jstr = jo.getString("text");
					break;
				case "312000"://餐厅
					jstr = jo.getString("text");
					break;
				//以下为错误返回
				case "40001":
					state = false;
					jstr = jo.getString("text");
					break;
				case "40002":
					state = false;
					jstr = jo.getString("text");
					break;
				case "40003":
					state = false;
					jstr = jo.getString("text");
					break;
				case "40004":
					state = false;
					jstr = jo.getString("text");
					break;
				case "40005":
					state = false;
					jstr = jo.getString("text");
					break;
				case "40006":
					state = false;
					jstr = jo.getString("text");
					break;
				case "40007":
					state = false;
					jstr = jo.getString("text");
					break;
			}
			//+ "\ntext=" + jo.getString("text");
			result = jstr;
		}
		catch(JSONException e)
		{
			
		}
		return state;
	}
	private boolean isNetWork()
	{
		boolean net = false;
		ConnectivityManager conm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conm.getActiveNetworkInfo();
		if (info != null && info.isAvailable())
			net = true;
		return net;
	}
	private void addSendMsg(String msg)
	{
		View sendview = inflate.inflate(R.layout.msgsend,null);
		TextView txtSend = (TextView) sendview.findViewById(R.id.txt_msgsend);
		txtSend.setText(msg);
		txtSend.setTextIsSelectable(true);
		sendview.setId(CHILD_SEND_ID);
		CHILD_SEND_ID++;
		adapter.addView(sendview);
	}
	private void addFromMsg(String msg)
	{
		View fromview = inflate.inflate(R.layout.msgfrom,null);
		TextView txtFrom = (TextView) fromview.findViewById(R.id.txt_msgfrom);
		txtFrom.setText(msg);
		txtFrom.setTextIsSelectable(true);
		fromview.setId(CHILD_FROM_ID);
		CHILD_FROM_ID++;
		adapter.addView(fromview);
	}
	private void addErrorMsg(String msg)
	{
		View errorview = inflate.inflate(R.layout.errormsg,null);
		TextView txtErr = (TextView) errorview.findViewById(R.id.txt_msgerror);
		txtErr.setText(msg);
		errorview.setId(CHILD_ERROR_ID);
		CHILD_ERROR_ID++;
		adapter.addView(errorview);
	}

	private void createSimpleDialog(String title,String msg)
	{
		Builder builder = new Builder(this);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton("确定",null);
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(0,1,0,"帮助");
		menu.add(0,2,0,"关于");
		menu.add(0,3,0,"退出");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case 1:
				createSimpleDialog("帮助",getResources().getString(R.string.help));
				break;
			case 2:
				createSimpleDialog("关于",getResources().getString(R.string.about));
				break;
			case 3:
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			if((System.currentTimeMillis() - exitTime) > 2000)
			{
				Toast.makeText(getApplicationContext(),"再按一次退出程序",Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			}
			else
			{
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
