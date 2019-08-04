package com.binxin.zdapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.vbea.file.R;

public class SlidButton extends View implements OnTouchListener
{
	private Bitmap bg_on, bg_off, slipper_btn;
	//按下时的x和当前的x 
	private float downX, nowX;
	//记录用户是否在滑动 
	private boolean onSlip = false;
	//当前的状态 
	private boolean nowStatus = false;
	//监听接口 
	private OnChangedListener listener;

	public SlidButton(Context context)
	{
		super(context);
		init();
	}

	public SlidButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public void init()
	{
		//载入图片资源
		bg_on = BitmapFactory.decodeResource(getResources(), R.drawable.sild_bg_on);
		bg_off = BitmapFactory.decodeResource(getResources(), R.drawable.sild_bg_off);
		slipper_btn = BitmapFactory.decodeResource(getResources(), R.drawable.sild_bg_btn);
		setOnTouchListener(this);
	}

	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		Matrix matrix = new Matrix();
		Paint paint = new Paint();
		float x = 0;

		//根据nowX设置背景，开或者关状态
		if (nowX < (bg_on.getWidth()/2))
			canvas.drawBitmap(bg_off, matrix, paint);//画出关闭时的背景
		else
			canvas.drawBitmap(bg_on, matrix, paint);//画出打开时的背景 

		if (onSlip)
		{//是否是在滑动状态,
			if(nowX >= bg_on.getWidth())//是否划出指定范围,不能让滑块跑到外头,必须做这个判断
				x = bg_on.getWidth() - slipper_btn.getWidth()/2;//减去滑块1/2的长度
			else
				x = nowX - slipper_btn.getWidth()/2;
		}else
		{
			if(nowStatus)
			{//根据当前的状态设置滑块的x值
				x = bg_on.getWidth() - slipper_btn.getWidth();
			}
			else
			{
				x = 0;
			}
		}

		//对滑块滑动进行异常处理，不能让滑块出界
		if (x < 0 )
		{
			x = 0;
		}
		else if(x > bg_on.getWidth() - slipper_btn.getWidth())
		{
			x = bg_on.getWidth() - slipper_btn.getWidth();
		}

		//画出滑块
		canvas.drawBitmap(slipper_btn, x , 0, paint); 
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
			{
				if (event.getX() > bg_off.getWidth() || event.getY() > bg_off.getHeight())
				{
					return false;
				}
				else
				{
					onSlip = true;
					downX = event.getX();
					nowX = downX;
				}
				break;
			}
			case MotionEvent.ACTION_MOVE:
				nowX = event.getX();
				break;
			case MotionEvent.ACTION_UP:
			{
				onSlip = false;
				if(event.getX() >= (bg_on.getWidth()/2))
				{
					nowStatus = true;
					nowX = bg_on.getWidth() - slipper_btn.getWidth();
				}
				else
				{
					nowStatus = false;
					nowX = 0;
				}

				if(listener != null)
					listener.OnChanged(SlidButton.this, nowStatus);
				break;
			}
		}
		//刷新界面
		invalidate();
		return true;
	}



	/** 
	 * 为SlidButton设置一个监听，供外部调用的方法 
	 * @param listener 
	 */
	public void setOnChangedListener(OnChangedListener listener)
	{
		this.listener = listener;
	}


	/** 
	 * 设置滑动开关的初始状态，供外部调用 
	 * @param checked 
	 */
	public void setChecked(boolean checked)
	{
		if(checked)
		{
			nowX = bg_off.getWidth();
		}
		else
		{
			nowX = 0;
		}
		invalidate();
		nowStatus = checked;
	}
	
	//获取状态
	public boolean getChecked()
	{
		return nowStatus;
	}

	//回调接口 
	public interface OnChangedListener
	{
		public void OnChanged(SlidButton witch, boolean checkState);
	}
}
