package com.binxin.zdapp.classes;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
//文件管理器
public class FilesTextView extends LinearLayout
{
	//一个文件包括文件名和图表
	//采用一个垂直线性布局
	private TextView	mText	= null;
	private ImageView	mIcon	= null;
	public FilesTextView(Context context, FilesText aIconifiedText) 
	{
		super(context);
		//设置布局方式
		this.setOrientation(HORIZONTAL);
		mIcon = new ImageView(context);
		//设置ImageView为文件的图标
		mIcon.setImageDrawable(aIconifiedText.getIcon());
		//设置图标在该布局中的填充位置
		mIcon.setPadding(6, 4, 4, 8); 
		//将ImageView即图表添加到该布局中
		addView(mIcon,  new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		//设置文件名、填充方式、字体大小
		mText = new TextView(context);
		mText.setText(aIconifiedText.getText());
		mText.setPadding(8, 6, 6, 10); 
		mText.setTextSize(15);
		//将文件名添加到布局中
		addView(mText, new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}
	//设置文件名
	public void setText(String words)
	{
		mText.setText(words);
	}
	//设置图标
	public void setIcon(Drawable bullet)
	{
		mIcon.setImageDrawable(bullet);
	}
}

