package com.vbes.lubanchi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.graphics.Color;

public class MainActivity extends Activity 
{
	EditText edtText;
	TextView txtResult;
	double cun = 100 / 3;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		edtText = (EditText) findViewById(R.id.mainEditText);
		Button button = (Button) findViewById(R.id.mainButton);
		txtResult = (TextView) findViewById(R.id.mainTextView);
		
		button.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (edtText.getText().toString().equals(""))
				{
					Toast.makeText(getApplicationContext(), "请输入要计算的值", Toast.LENGTH_SHORT).show();
					return;
				}
				double chks = Double.parseDouble(edtText.getText().toString());
				if (chks <= (1.6 * cun))
				{
					ji();
					return;
				}
				double unit = chks / cun;
				Toast.makeText(getApplicationContext(), chks + "毫米为"+ unit + "寸", Toast.LENGTH_SHORT).show();
				double st = (unit - 1.6) / 3.2;
				if (Math.ceil(st) % 2 == 0)
					ji();
				else
					xiong();
			}
		});
    }
	
	private void ji()
	{
		txtResult.setText("吉");
		txtResult.setTextColor(Color.RED);
	}
	
	private void xiong()
	{
		txtResult.setText("凶");
		txtResult.setTextColor(Color.BLACK);
	}
}
