package com.binxin.zdapp.classes;

import java.util.Set;
import java.util.HashSet;

import android.content.Context;
import android.widget.Toast;

public class Common
{
	public static void showShortToast(Context context, String msg)
	{
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
	public static void showLongToast(Context context, String msg)
	{
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
	
	public static void showShortToast(Context context, int rid)
	{
		Toast.makeText(context, context.getResources().getString(rid), Toast.LENGTH_SHORT).show();
	}

	public static void showLongToast(Context context, int rid)
	{
		Toast.makeText(context, context.getResources().getString(rid), Toast.LENGTH_LONG).show();
	}
	
	public static String toFilter(String key)
	{
		String [] keys = {"<br>","&quot;","&lt;","&gt;"};
		String [] filt = {"\n","\"","<",">"};
		for (int i = 0; i < keys.length; i++)
		{
			key = key.replace(keys[i],filt[i]);
		}
		return key;
	}
	
	public static String trim(String str,String key)
	{
		boolean begin = true,end = true;
		do{
			int beginIndex = str.indexOf(key) == 0 ? 1 : 0;
			int endIndex = str.lastIndexOf(key) + 1 == str.length() ? str.lastIndexOf(key) : str.length();
			str = str.substring(beginIndex,endIndex);
			begin = (str.indexOf(key) == 0);
			end = (str.lastIndexOf(key) + 1 == str.length());
		}
		while (begin || end);
		return str;
	}
}
