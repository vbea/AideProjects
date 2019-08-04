package com.binxin.zdapp.classes;

import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.lang.Thread.UncaughtExceptionHandler;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class ExceptionHandler extends Application implements UncaughtExceptionHandler
{
	private Context mContext;
	private static ExceptionHandler instance; //单例模式
	private ExceptionHandler(){}
	//锁定代码快
	public synchronized static ExceptionHandler getInstance()
	{
		if (instance == null)
		{
			instance = new ExceptionHandler();
		}
		return instance;
	}
	public void init(Context context)
	{
		this.mContext = context;
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	//抽象方法
	@Override
	public void uncaughtException(Thread thread, Throwable able)
	{
		//ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		//ComponentName cnm = am.getRunningTasks(1).get(0).topActivity;
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String temp = "\ntime:" + time + "\nthread:" + thread.toString() + "\nmessage:" + able.toString() + "\n";
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			try
			{
				File file = new File(Environment.getExternalStorageDirectory()+"/ZDApp/Log");
				if (!file.exists())
				{
					file.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(file+"/ZDApp.log",true);
				fos.write(temp.getBytes());//写入信息
				fos.close();
				Toast.makeText(mContext,"Android Exception: Runing Error!",Toast.LENGTH_SHORT).show();
			}
			catch (IOException e)
			{
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		}
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	public static void log(String msg)
	{
		String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
		String temp = "[" + time +"]\n" + msg + "\n";
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			try
			{
				File file = new File(Environment.getExternalStorageDirectory()+"/ZDApp/Log");
				if (!file.exists())
				{
					file.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(file +"/"+ date + ".log",true);
				fos.write(temp.getBytes());//写入信息
				fos.close();
				//Toast.makeText(mContext,"Android Exception: Runing Error!",Toast.LENGTH_SHORT).show();
			}
			catch (IOException e)
			{
				//android.os.Process.killProcess(android.os.Process.myPid());
			}
		}
	}
}
