package com.vbea.decode;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.vbea.decode.classes.DecHelper;

public class MainActivity extends Activity
{
	private EditText result, message, password;
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		result = (EditText) findViewById(R.id.txtResult);
		message = (EditText) findViewById(R.id.txtMsg);
		password = (EditText) findViewById(R.id.txtPsd);
		Button dcip = (Button) findViewById(R.id.btnDcip);
		Button ecip = (Button) findViewById(R.id.btnEcip);
		
		//加密
		ecip.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (message.getText().toString().length() == 0)
				{
					Toast.makeText(getApplicationContext(),"请输入信息",Toast.LENGTH_SHORT).show();
					return;
				}
				try
				{
					DecHelper ec = new DecHelper(password.getText().toString());
					result.setText(ec.encrypt(message.getText().toString()));
					//Toast.makeText(getApplicationContext(),"加密:"+msg,Toast.LENGTH_SHORT).show();
				}
				catch (Exception e)
				{
					Toast.makeText(getApplicationContext(),"加密失败",Toast.LENGTH_SHORT).show();
				}
			}
		});
		//解密
		dcip.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (message.getText().toString().length() == 0)
				{
					Toast.makeText(getApplicationContext(),"请输入信息",Toast.LENGTH_SHORT).show();
					return;
				}
				try
				{
					DecHelper dc = new DecHelper(password.getText().toString());
					result.setText(dc.decrypt(message.getText().toString()));
				}
				catch (Exception e)
				{
					Toast.makeText(getApplicationContext(),"解密失败",Toast.LENGTH_SHORT).show();
				}
			}
		});
    }
}
