package com.wheel;


import com.timepicker.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.Button;

public class ImageTextButton extends Button 
{
	private final String namespace = "http://www.javaeye.com/custom";
	private int resourceId = 0;
	private Bitmap bitmap;

	public ImageTextButton(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		setClickable(true);
		// 默认使用R.drawable.icon这张图片，通过icon这个属性来获取图片
		resourceId = attrs.getAttributeResourceValue(namespace, "icon",R.drawable.icon);
		bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
		this.setTextSize(12);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		// 图片顶部居中显示
		int x = (this.getMeasuredWidth() - bitmap.getWidth()) >> 1;
		int y = 0;
		canvas.drawBitmap(bitmap, x, y, null);
		// 坐标需要转换，因为默认情况下Button中的文字居中显示
		// 这里需要让文字在底部显示
		canvas.translate(0,(this.getMeasuredHeight() >> 1) - (int) this.getTextSize());
		super.onDraw(canvas);
	}

	public void setIcon(Bitmap bitmap) 
	{
		this.bitmap = bitmap;
		invalidate();
	}

	public void setIcon(int resourceId) 
	{
		this.bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
		invalidate();
	}

}
