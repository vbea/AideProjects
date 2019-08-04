package com.niit.rolant;

import com.cellcom.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

//自动提示框
public class AutoCompleteTextViewActivity extends Activity {

	private AutoCompleteTextView autoComplete;
	private Button cleanButton;
	static final String[] COUNTRIES = new String[] {
		"China" ,"Russia", "Germany",
		"Ukraine", "Belarus", "USA" ,"China1" ,"China12", "Germany",
		"Russia2", "Belarus", "USA" ,"UAA","UBC","UBB","CCC","BBB","广州1","广州2","广州3","广州4","广东1","广东2","广东3",
	    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auto_complete_text_view);
		setTitle("AutoCompleteTextView示例！");
		autoComplete=(AutoCompleteTextView)findViewById(R.id.auto_complete);
		cleanButton=(Button)findViewById(R.id.cleanButton);
		
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(AutoCompleteTextViewActivity.this,android.R.layout.simple_dropdown_item_1line,COUNTRIES);
		autoComplete.setAdapter(adapter);
		
		//清空
		cleanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				autoComplete.setText("");
			}
		});
	}
	
}
