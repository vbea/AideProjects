package com.pocketdigi.animation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.os.*;

public class main extends Activity {
    /** Called when the activity is first created. */
	TextView tv,tv2,tv3,tv4,tr1;
	Button bt3,bt;
	Animation translate,translate1,scale,rotate,alpha,cycles,sh1,sh2,sh3,sh4;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        bt=(Button)findViewById(R.id.bt);
        tv=(TextView)findViewById(R.id.tv);
        tv2=(TextView)findViewById(R.id.tv2);
        tv3=(TextView)findViewById(R.id.tv3);
        tv4=(TextView)findViewById(R.id.tv4);
		tr1=(TextView)findViewById(R.id.tr1);
		translate=AnimationUtils.loadAnimation(main.this, R.anim.translate);
		translate1=AnimationUtils.loadAnimation(main.this, R.anim.translate);
		scale=AnimationUtils.loadAnimation(main.this, R.anim.scale);
		rotate=AnimationUtils.loadAnimation(main.this, R.anim.rotate);
		alpha=AnimationUtils.loadAnimation(main.this, R.anim.a1);
		sh1=AnimationUtils.loadAnimation(main.this, R.anim.slump);
		sh2=AnimationUtils.loadAnimation(main.this, R.anim.shake1);
		sh3=AnimationUtils.loadAnimation(main.this, R.anim.shake2);
		sh4=AnimationUtils.loadAnimation(main.this, R.anim.shake3);
        tv.setVisibility(View.INVISIBLE);
        
        bt3=(Button)findViewById(R.id.bt3);
        bt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(main.this,activity2.class);
				startActivity(intent);
				overridePendingTransition(R.anim.a2,R.anim.a1);
				//淡出淡入动画效果
			}
        	
        });
        bt3.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread()
				{
					public void run()
					{
						try
						{
		        			//载入XML文件成Animation对象
							for (int i = 0;i < 5; i++)
							{
								mHandle.sendEmptyMessage(i);
								sleep(1000);
							}
		        			//应用动画
							mHandle.sendEmptyMessage(4);
							sleep(500);
							int j = 6;
							for (int i = 0;i < 16;i++)
							{
								mHandle.sendEmptyMessage(j);
								j = (j == 5) ? 6 : 5;
								sleep(100);
							}
							//mHandle.sendEmptyMessage(7);
						}
						catch (InterruptedException e)
						{
					
						}
					}
		        }.start();
			}
		});
    }
	public final Handler mHandle =new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 0:
					tv.startAnimation(translate);
					tv.setVisibility(View.VISIBLE);
					break;
				case 1:
					tr1.startAnimation(translate1);
					tr1.setVisibility(View.VISIBLE);
					break;
				case 2:
					tv2.startAnimation(scale);
					break;
				case 3:
					tv3.startAnimation(alpha);
					break;
				case 4:
					tv4.startAnimation(sh1);
					break;
				case 5:
					tv4.startAnimation(sh2);
					break;
				case 6:
					tv4.startAnimation(sh3);
					break;
				case 7:
					tv4.startAnimation(sh4);
					break;
			}
			super.handleMessage(msg);
		}
	};
}
