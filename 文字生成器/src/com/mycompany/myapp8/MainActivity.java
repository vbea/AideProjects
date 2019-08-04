package com.mycompany.myapp8;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.graphics.*;
import android.view.View.*;

public class MainActivity extends Activity
{final int WIDTH=3;
  final int COLOR=0xffff0000;
  final int SIZE=20;
  Paint paint;
  Bitmap bmp;
  
  EditText et1,et2;
  Button btn;
   
public void alert(String str)
{Toast.makeText(this,str,Toast.LENGTH_SHORT).show();}

  
  
   /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
 paint=new Paint();
 paint.setTextSize(SIZE);
paint.setColor(COLOR); 
 paint.setStrokeWidth(WIDTH);
 et1=(EditText)findViewById(R.id.et1);
et2=(EditText)findViewById(R.id.et2);
btn=(Button)findViewById(R.id.btn);
		btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					String str=et1.getText().toString();
					if(str.length()<1)
{alert("非法");}
else
{
	et2.setText("");
	bmp=Bitmap.createBitmap(SIZE,str.length()*SIZE,Bitmap.Config.ARGB_8888);

	Canvas canvas=new Canvas(bmp);
	for(int i=0;i<str.length();i++)
	{canvas.drawText(str.substring(i,i+1),0,SIZE*(i+1),paint);}
	for(int j=0;j<bmp.getHeight();j++)
	{
		for(int k=0;k<bmp.getWidth();k++)
		{
			if(bmp.getPixel(k,j)==COLOR)
			{et2.setText(et2.getText().toString()+"#");}
			else
			{et2.setText(et2.getText().toString()+"\t");}


		}
		et2.setText(et2.getText().toString()+"\n");
	}
	
	
}



}			});


 }
}
