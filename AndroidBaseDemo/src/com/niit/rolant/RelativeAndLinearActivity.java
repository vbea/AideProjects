package com.niit.rolant;

import com.cellcom.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class RelativeAndLinearActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//创建线性布局LinearLayout对象
		LinearLayout layoutMain=new LinearLayout(this);
		//设置水平方向
		layoutMain.setOrientation(LinearLayout.HORIZONTAL);
		setContentView(layoutMain);
		
		/**
		 * LayoutInflater的作用类似于 findViewById(),不同点是LayoutInflater是用来找layout下xml布局文件，并且实例化！而findViewById()是找具体xml下的具体 widget控件(如:Button,TextView等)。 
		 */
		LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout layoutLeft=(RelativeLayout)inflater.inflate(R.layout.relative_linear_left, null);
		RelativeLayout layoutRight=(RelativeLayout)inflater.inflate(R.layout.relative_linear_right, null);
		
		RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutMain.addView(layoutLeft, 100, 100);
		layoutMain.addView(layoutRight,layoutParams);
	}

}
