package com.binxin.zdapp.classes;

import java.text.Collator;
import java.text.CollationKey;
import android.graphics.drawable.Drawable;
//邠心工作室_文件管理器
public class FilesText implements Comparable<FilesText>
{
	/* 文件名 */
	private String mText = "";
	/* 文件的图标ICNO */
	private Drawable mIcon = null;
	/* 文件时间&大小 */
	private String detail = "";
	/* 路径 */
	private String mPath = "";
	/* 能否选中 */
	private boolean	mSelectable	= true;
	//private Collator colla = Collator.getInstance();
	public FilesText(String text, String path, Drawable bullet, String deta)
	{
		mIcon = bullet;
		mText = text;
		mPath = path;
		detail = deta;
	}
	public FilesText(String text, Drawable bullet, String deta)
	{
		mIcon = bullet;
		mText = text;
		mPath = text;
		detail = deta;
	}
	//是否可以选中
	public boolean isSelectable()
	{
		return mSelectable;
	}
	//设置是否可用选中
	public void setSelectable(boolean selectable)
	{
		mSelectable = selectable;
	}
	//得到文件名
	public String getText()
	{
		return mText;
	}
	public String getPath()
	{
		return mPath;
	}
	public void setPath(String path)
	{
		this.mPath = path;
	}
	//设置文件名
	public void setText(String text)
	{
		if (text.substring(0,1).equals("/"))
		{
			mText = text.substring(1);
		}
		else
			mText = text;
	}
	//设置图标
	public void setIcon(Drawable icon)
	{
		mIcon = icon;
	}
	//得到图标
	public Drawable getIcon()
	{
		return mIcon;
	}
	//设置大小
	public void setDetail(String s)
	{
		detail = s;
	}
	//得到大小
	public String getDetail()
	{
		return detail;
	}
	//比较文件名是否相同
	public int compareTo(FilesText other)
	{
		if (this.mText != null)
		{
			//CollationKey key1 = colla.getCollationKey(this.mText);
			//CollationKey key2 = colla.getCollationKey(other.getText());
			return this.mText.toLowerCase().compareTo(other.getText().toLowerCase());
			//return key1.compareTo(key2);
		}
		else
			throw new IllegalArgumentException();
	}
}
