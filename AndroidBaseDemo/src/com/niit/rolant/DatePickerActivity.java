package com.niit.rolant;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.cellcom.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

//DatePicker日期选择器、TimePicker时间选择器 使用
public class DatePickerActivity extends Activity {

	private Button datePickerButton;
	private DatePicker datePicker1;
	private TimePicker timePicker1;
	
	//默认生成系统当前时间
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");		
	String str=sdf.format(new Date());
	int year=Integer.parseInt(str.substring(0, 4));
	int month=Integer.parseInt(str.substring(5,7))-1;
	int day=Integer.parseInt(str.substring(8,10));
	int hour=Integer.parseInt(str.substring(11,13));
	int minute=Integer.parseInt(str.substring(14,16));
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.date_picker);
		setTitle("DatePicker、TimePicker示例!");
		
		datePicker1=(DatePicker)findViewById(R.id.datePicker1);
		timePicker1=(TimePicker)findViewById(R.id.timePicker1);
		datePickerButton=(Button)findViewById(R.id.datePickerButton1);

		datePicker1.init(year,month,day, null);
		
		timePicker1.setCurrentHour(hour);
		timePicker1.setCurrentMinute(minute);
		
		timePicker1.setIs24HourView(true);
		
		datePickerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setTitle("你选择的是："+datePicker1.getYear()+"年"+datePicker1.getMonth()+"月"+datePicker1.getDayOfMonth()+"日 "+timePicker1.getCurrentHour()+"小时"+timePicker1.getCurrentMinute()+"分");
			}
		});
	}

}
