package com.niit.rolant;

import java.util.ArrayList;
import java.util.List;

import com.cellcom.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

//下拉框
public class SpinnerActivity extends Activity {

	private Spinner spinner1;
	private Spinner spinner2;
	private Button ok;
	private ArrayAdapter countiesAdapter;
	private String[] mCounties={"beijing","guangdong","guangxi","hunan"};
	private List<String> allCounties=new ArrayList<String>();
	private String result="你选择的是：";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spinner);
		
		spinner1=(Spinner)findViewById(R.id.spinner1);
		spinner2=(Spinner)findViewById(R.id.spinner2);
		ok=(Button)findViewById(R.id.ok);
		
		for(int i=0;i<mCounties.length;i++){
			allCounties.add(mCounties[i]);
		}
		
		countiesAdapter=new ArrayAdapter<String>(SpinnerActivity.this,android.R.layout.simple_spinner_item,allCounties);
		countiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(countiesAdapter);
		
		ArrayAdapter adapter=ArrayAdapter.createFromResource(SpinnerActivity.this,R.array.counties,android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(adapter);
		
		//单击第一个下拉按钮时，显示选择的值。 
		spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String str=(String)spinner1.getAdapter().getItem((int)id);
				setTitle(result+str);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//单击第二个下拉按钮时，显示选择的值。 
		spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long id) {
				String str=(String)spinner2.getAdapter().getItem(position);
				setTitle(result+str);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		
		
		//单击确定按钮，提取选择的值.
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setTitle(result+spinner1.getSelectedItem()+"  - >>  "+spinner2.getSelectedItem());
			}
		});
		
	}

}
