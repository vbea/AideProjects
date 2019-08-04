package com.niit.rolant;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.cellcom.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Bundle2Activity extends Activity{

	private TextView result;
	private Button backButton;
	Intent intent=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bundle2);
		result=(TextView)findViewById(R.id.result);
		backButton=(Button)findViewById(R.id.button_back);
		
		intent=this.getIntent();
		Bundle bundle=intent.getExtras();
		double height=bundle.getDouble("height");
		String sex=bundle.getString("sex");
		String sexStr="";
		if("M".equals(sex)){
			sexStr="男性";
		}else{
			sexStr="女性";
		}
		String weight=this.getWeight(sex, height);
		result.setText("你是一位："+sexStr+"\n身高是："+height+"厘米 \n你的标准体重："+weight+"公斤");
		
		//返回上一页
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle2Activity.this.setResult(RESULT_OK, intent);	
				Bundle2Activity.this.finish();
			}
		});
	}
	
	public String getWeight(String sex,double height){
		String weight="";
		if("M".endsWith(sex)){
			weight=format((height-80)*0.7);
		}else{
			weight=format((height-70)*0.6);
		}
		return weight;
	}
	
	public String format(double num){
		NumberFormat formatter=new DecimalFormat("0.00");
		String s=formatter.format(num);
		return s;
	}
}
