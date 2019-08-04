package com.xiaovvv.scaletrans;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		Button btChange=(Button)findViewById(R.id.btn_chng);
		btChange.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				EditText etText=(EditText)findViewById(R.id.et_text);
				EditText etAfter=(EditText)findViewById(R.id.et_after);
				EditText etAs=(EditText)findViewById(R.id.et_aS);
				EditText etBs=(EditText)findViewById(R.id.et_bS);
				
				if(etText.getText().toString().length()<1||etAs.getText().toString().length()<1||etBs.getText().toString().length()<1)
				{
					Toast.makeText(MainActivity.this,"请输入相关数据  ",Toast.LENGTH_SHORT).show();		
				}
				else
				{
					ScaleThransform st=new ScaleThransform();
					st.setParameter(strToInt(etAs.getText().toString()),strToInt(etBs.getText().toString()));
					st.changeTO(etText.getText().toString());
					etAfter.setText(st.getTransformatnl());
					Toast.makeText(MainActivity.this,"转换为: "+st.getTransformatnl()+"",Toast.LENGTH_LONG).show();
				}
			}
		});
    }
	private int strToInt(String str)
	{
		return Integer.parseInt(str,10);
	}
}
