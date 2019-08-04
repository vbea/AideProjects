package com.niit.rolant;

import com.cellcom.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class CheckBoxActivity extends Activity {

	private CheckBox plainCB;
	private CheckBox serifCB;
	private CheckBox boldCB;
	private CheckBox italicCB;
	private Button button1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_box);
		
		plainCB=(CheckBox)findViewById(R.id.plain_cb);
		serifCB=(CheckBox)findViewById(R.id.serif_cb);
		boldCB=(CheckBox)findViewById(R.id.bold_cb);
		italicCB=(CheckBox)findViewById(R.id.italic_cb);
		button1=(Button)findViewById(R.id.button1);
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String result="";
				if(plainCB.isChecked()){
					result+=plainCB.getText()+"¡¢";
				}
				if(serifCB.isChecked()){
					result+=serifCB.getText()+"¡¢";
				}
				if(boldCB.isChecked()){
					result+=boldCB.getText()+"¡¢";
				}
				if(italicCB.isChecked()){
					result+=italicCB.getText()+"¡¢";
				}
				if(!"".equals(result)){
					result=result.substring(0,result.length()-1);
				}else{
					result="ÄãÃ»ÓÐÑ¡Ôñ£¡£¡£¡";
				}
				setTitle("Checked:"+result);
			}
		});
		
	}

}
