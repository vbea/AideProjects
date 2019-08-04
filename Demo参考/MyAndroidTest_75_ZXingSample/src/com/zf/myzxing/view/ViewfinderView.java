package com.zf.myzxing.view;

import com.google.zxing.ResultPoint;
import com.zf.myzxing.R;
import com.zf.myzxing.camera.CameraManager;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public final class ViewfinderView extends View {

	private static final int[] SCANNER_ALPHA = { 0, 64, 128, 192, 255, 192,
			128, 64 };
	private static final int CURRENT_POINT_OPACITY = 0xA0;
	private static final int MAX_RESULT_POINTS = 20;
	private static final long ANIMATION_DELAY = 80L;
	private CameraManager cameraManager;
	private final Paint paint;
	private Bitmap resultBitmap;
	private final int maskColor;
	private final int resultColor;
	private int scannerAlpha;
	private List<ResultPoint> possibleResultPoints;

	private int i = 0;// 添加的
	private Rect mRect;// 扫描线填充边界
	private GradientDrawable mDrawable;// 采用渐变图作为扫描线
	private Drawable lineDrawable;// 采用图片作为扫描线

	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);

		// GradientDrawable、lineDrawable
		mRect = new Rect();
		int left = getResources().getColor(R.color.lightgreen);
		int center = getResources().getColor(R.color.green);
		int right = getResources().getColor(R.color.lightgreen);
		lineDrawable = getResources().getDrawable(R.drawable.zx_code_line);
		mDrawable = new GradientDrawable(
				GradientDrawable.Orientation.LEFT_RIGHT, new int[] { left,
						left, center, right, right });

		scannerAlpha = 0;
		possibleResultPoints = new ArrayList<ResultPoint>(5);
	}

	public void setCameraManager(CameraManager cameraManager) {
		this.cameraManager = cameraManager;
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (cameraManager == null) {
			return;
		}

		Rect frame = cameraManager.getFramingRect();
		if (frame == null) {
			return;
		}

		int width = canvas.getWidth();
		int height = canvas.getHeight();

		// 画扫描框外部的暗色背景
		// 设置蒙板颜色
		paint.setColor(resultBitmap != null ? resultColor : maskColor);
		// 头部
		canvas.drawRect(0, 0, width, frame.top, paint);
		// 左边
		canvas.drawRect(0, frame.top, frame.left, frame.bottom, paint);
		// 右边
		canvas.drawRect(frame.right, frame.top, width, frame.bottom, paint);
		// 底部
		canvas.drawRect(0, frame.bottom, width, height, paint);

		if (resultBitmap != null) {
			// 在扫描框中画出预览图
			paint.setAlpha(CURRENT_POINT_OPACITY);
			canvas.drawBitmap(resultBitmap, null, frame, paint);
		} else {
			// 画出四个角
			paint.setColor(getResources().getColor(R.color.green));
			// 左上角
			canvas.drawRect(frame.left, frame.top, frame.left + 15,
					frame.top + 5, paint);
			canvas.drawRect(frame.left, frame.top, frame.left + 5,
					frame.top + 15, paint);
			// 右上角
			canvas.drawRect(frame.right - 15, frame.top, frame.right,
					frame.top + 5, paint);
			canvas.drawRect(frame.right - 5, frame.top, frame.right,
					frame.top + 15, paint);
			// 左下角
			canvas.drawRect(frame.left, frame.bottom - 5, frame.left + 15,
					frame.bottom, paint);
			canvas.drawRect(frame.left, frame.bottom - 15, frame.left + 5,
					frame.bottom, paint);
			// 右下角
			canvas.drawRect(frame.right - 15, frame.bottom - 5, frame.right,
					frame.bottom, paint);
			canvas.drawRect(frame.right - 5, frame.bottom - 15, frame.right,
					frame.bottom, paint);

			// 在扫描框中画出模拟扫描的线条
			// 设置扫描线条颜色为绿色
			paint.setColor(getResources().getColor(R.color.green));
			// 设置绿色线条的透明值
			paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
			// 透明度变化
			scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;

			// 画出固定在中部的线条
			// int middle = frame.height() / 2 + frame.top;
			// canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1,
			// middle + 2, paint);

			// 将扫描线修改为上下走的线
			if ((i += 5) < frame.bottom - frame.top) {
				/* 以下为用渐变线条作为扫描线 */
				// 渐变图为矩形
				// mDrawable.setShape(GradientDrawable.RECTANGLE);
				// 渐变图为线型
				// mDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
				// 线型矩形的四个圆角半径
				// mDrawable
				// .setCornerRadii(new float[] { 8, 8, 8, 8, 8, 8, 8, 8 });
				// 位置边界
				// mRect.set(frame.left + 10, frame.top + i, frame.right - 10,
				// frame.top + 1 + i);
				// 设置渐变图填充边界
				// mDrawable.setBounds(mRect);
				// 画出渐变线条
				// mDrawable.draw(canvas);

				/* 以下为图片作为扫描线 */
				mRect.set(frame.left - 6, frame.top + i - 6, frame.right + 6,
						frame.top + 6 + i);
				lineDrawable.setBounds(mRect);
				lineDrawable.draw(canvas);

				// 刷新
				invalidate();
			} else {
				i = 0;
			}

			// 重复执行扫描框区域绘制(画四个角及扫描线)
			postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
					frame.right, frame.bottom);
		}
	}

	public void drawViewfinder() {
		Bitmap resultBitmap = this.resultBitmap;
		this.resultBitmap = null;
		if (resultBitmap != null) {
			resultBitmap.recycle();
		}
		invalidate();
	}

	public void drawResultBitmap(Bitmap barcode) {
		resultBitmap = barcode;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		List<ResultPoint> points = possibleResultPoints;
		synchronized (points) {
			points.add(point);
			int size = points.size();
			if (size > MAX_RESULT_POINTS) {
				points.subList(0, size - MAX_RESULT_POINTS / 2).clear();
			}
		}
	}

	public void recycleLineDrawable() {
		if (mDrawable != null) {
			mDrawable.setCallback(null);
		}
		if (lineDrawable != null) {
			lineDrawable.setCallback(null);
		}
	}
}
