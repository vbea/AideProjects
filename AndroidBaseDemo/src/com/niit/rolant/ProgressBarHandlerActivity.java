package com.niit.rolant;
import com.cellcom.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import android.widget.TextView;

/**
 * 
 * @author nwang
 * 
 * 进度条ProgressBar与线程使用。单击开始按钮，进度条水平方向前进！
 *
 */
public class ProgressBarHandlerActivity extends Activity {
    /** Called when the activity is first created. */
	
	private Button startButton;
	private Button stopButton;
	private TextView result;
	private ProgressBar firstProgressBar;
	private final static int defaultValue=10;
	private int i=0;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_bar);
        setTitle("ProgressBar使用！");
        startButton=(Button)findViewById(R.id.startButton);
        stopButton=(Button)findViewById(R.id.stopButton);
        result=(TextView)findViewById(R.id.result);
        firstProgressBar=(ProgressBar)findViewById(R.id.firstProgressBar);
        
        firstProgressBar.setVisibility(0);//刚开始设置进度条可见
        
        startButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*i+=defaultValue;
				firstProgressBar.setProgress(i);
				firstProgressBar.setSecondaryProgress(i+defaultValue);*/
				result.setText(((double)i/firstProgressBar.getMax()*100)+"%");
				handler.post(progressBarThread);
			}
		});
        
        stopButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				handler.removeCallbacks(progressBarThread);			
			}
		});
    }
    
    //创建Handler对象
    Handler handler=new Handler();
    
    //创建线程对象
    MyRunnable progressBarThread=new MyRunnable();
    class MyRunnable implements Runnable{
		@Override
		public void run() {
			i+=defaultValue;
			handler.postDelayed(progressBarThread, 1000);
			firstProgressBar.setProgress(i);
			firstProgressBar.setSecondaryProgress(i+defaultValue);
			result.setText(((double)i/firstProgressBar.getMax()*100)+"%");
			if(i>=firstProgressBar.getMax()) i=0;
			
		}
    	
    }
    
}