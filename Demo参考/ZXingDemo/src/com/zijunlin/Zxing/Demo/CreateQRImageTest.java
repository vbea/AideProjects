package com.zijunlin.Zxing.Demo;

import java.util.Hashtable;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
/**
 * @类功能说明: 生成二维码图片示例
 * @作者:饶正勇
 * @时间:2013-4-18上午11:10:42
 * @版本:V1.0
 */
public class CreateQRImageTest extends Activity
{
	private ImageView sweepIV;
	private EditText sweepEdt;
	private TextView okBtn;
	private int QR_WIDTH = 200, QR_HEIGHT = 200;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		
		sweepIV = (ImageView) findViewById(R.id.test_iv);
		sweepEdt = (EditText) findViewById(R.id.CodeEdt);
		okBtn = (TextView) findViewById(R.id.txtResult);
		
		okBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				createQRImage(sweepEdt.getText().toString());
			}
		});
	}
	
	
	//要转换的地址或字符串,可以是中文
	public void createQRImage(String url)
	{
		int defCol = 0xffffff00;
		try
		{
			//判断URL合法性
			if (url == null || "".equals(url) || url.length() < 1)
			{
				Toast.makeText(getApplicationContext(),"输入错误",Toast.LENGTH_SHORT).show();
				return;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			//图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
			//下面这里按照二维码的算法，逐个生成二维码的图片，
			//两个for循环是图片横列扫描的结果
			for (int y = 0; y < QR_HEIGHT; y++)
			{
				for (int x = 0; x < QR_WIDTH; x++)
				{
					if (bitMatrix.get(x, y))
					{
						pixels[y * QR_WIDTH + x] = defCol;
					}
					else
					{
						pixels[y * QR_WIDTH + x] = 0xffffffff;
					}
				}
			}
			//生成二维码图片的格式，使用ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
			//显示到一个ImageView上面
			sweepIV.setImageBitmap(bitmap);
		}
		catch (WriterException e)
		{
			e.printStackTrace();
		}
	}
}
