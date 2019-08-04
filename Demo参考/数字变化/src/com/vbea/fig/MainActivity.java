package com.vbea.fig;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.widget.EditText;
import android.widget.Button;
import android.view.View.*;
import android.graphics.*;

public class MainActivity extends Activity
{
    private String text0;
	private int text0;
	private EditText mview;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		mview = (EditText) findViewById(R.id.textView01);
		Button mbtn = (Button) findViewById(R.id.button01);
		Button mbtn1 = (Button) findViewById(R.id.button02);
		Button black = (Button) findViewById(R.id.black);
		Button red = (Button) findViewById(R.id.red);
		Button green=(Button) findViewById(R.id.green);
		Button blue= (Button) findViewById(R.id.blue);
		//mview.setText(text0);
		text0 = getText(mview);
		mbtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				/*for (int i=0; i>=100; i++)
				{
					mview.setText("数字"+text0);
					text0 = text0 + 1;
				}*/
				onStar();
				mview.setText(""+text0);
			}
		});
		mbtn1.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				onStar1();
				mview.setText(""+text0);
			}
		});
		//mview.setText(text0);
		black.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				mview.setTextColor(Color.BLACK);
			}
		});
		red.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				mview.setTextColor(Color.RED);
			}
		});
		green.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				mview.setTextColor(Color.GREEN);
			}
		});
		blue.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				mview.setTextColor(Color.BLUE);
			}
		});
	}
	public void onStar()
	{
		//数字增加1
		text0 = text0 + 1;
	}
	public void onStar1()
	{
		//数字减少1
		text0 = text0 - 1;
	}
}
