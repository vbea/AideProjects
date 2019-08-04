package com.niit.rolant;

import com.cellcom.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * @author nwang
 * 
 *	android基本布局、组件等等操作！
 */
public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	
	private Button frameLayout;
	private Button relativeLayout;
	private Button relativeAndLinear;
	private Button tableLayout;
	//选项卡按钮
	private Button tabWidget;
	private Button checkbox;
	private Button radionGroup;
	private Button spinner;
	private Button autoCompleteTextView;
	private Button datePicker;
	private Button progressBar;
	private Button ratingBar;
	private Button imageShow;
	private Button gridView;
	private Button tabDemo;
	private Button menu1;
	private Button menu2;
	private Button menu3;
	private Button bundle;
	private Button alertDialog;
	private Button notification;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        frameLayout=(Button)findViewById(R.id.frameLayout);
        relativeLayout=(Button)findViewById(R.id.relativeLayout);
        relativeAndLinear=(Button)findViewById(R.id.relativeAndLinear);
        tableLayout=(Button)findViewById(R.id.tableLayout);
        tabWidget=(Button)findViewById(R.id.tabWidget);
        checkbox=(Button)findViewById(R.id.checkbox);
        radionGroup=(Button)findViewById(R.id.radioGroup);
        spinner=(Button)findViewById(R.id.spinner);
        autoCompleteTextView=(Button)findViewById(R.id.autoCompleteTextView);
        datePicker=(Button)findViewById(R.id.datePicker);
        progressBar=(Button)findViewById(R.id.progressBar);
        ratingBar=(Button)findViewById(R.id.ratingBar);
        imageShow=(Button)findViewById(R.id.imageShow);
        gridView=(Button)findViewById(R.id.gridView);
        tabDemo=(Button)findViewById(R.id.tabDemo);
        menu1=(Button)findViewById(R.id.menu1);
        menu2=(Button)findViewById(R.id.menu2);
        menu3=(Button)findViewById(R.id.menu3);
        bundle=(Button)findViewById(R.id.bundle);
        alertDialog=(Button)findViewById(R.id.alertDialog);
        notification=(Button)findViewById(R.id.notification);
        
        //FrameLayout布局使用
        frameLayout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, FrameLayoutActivity.class);
				startActivity(intent);
			}
		});
        
        //RelativeLayout布局使用
        relativeLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, RelativeLayoutActivity.class);
				startActivity(intent);
			}
		});
        
        //RelativeLayout和LinearLayout综合使用
        relativeAndLinear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, RelativeAndLinearActivity.class);
				startActivity(intent);
			}
		});
        
        //TableLayout布局使用
        tableLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, TableLayoutActivity.class);
				startActivity(intent);
			}
		});
        
        //切换选项卡TabWidget
        tabWidget.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, TabWidgetActivity.class);
				startActivity(intent);
			}
		});
        
        //多选控件CheckBox
        checkbox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, CheckBoxActivity.class);
				startActivity(intent);
			}
		});
        
        //单选控件RadioGroup使用
        radionGroup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, RadioGroupActivity.class);
				startActivity(intent);
			}
		});
        
        //下拉框使用
        spinner.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, SpinnerActivity.class);
				startActivity(intent);
			}
		});
        
        //自动提示框
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent =new Intent();
				intent.setClass(MainActivity.this, AutoCompleteTextViewActivity.class);
				startActivity(intent);
			}
		});
        
        //日期选择器使用
        datePicker.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, DatePickerActivity.class);
				startActivity(intent);
			}
		});
        
        //进度条使用
        progressBar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, ProgressBarHandlerActivity.class);
				startActivity(intent);
			}
		});
        
        //评分组件RatingBar
        ratingBar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, RatingBarActivity.class);
				startActivity(intent);
			}
		});
        
        //浏览图片
        imageShow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, ImageShowActivity.class);
				startActivity(intent);
			}
		});
        
        //网络视图
        gridView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, GridViewActivity.class);
				startActivity(intent);
			}
		});
        
        //标签控件tab
        tabDemo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, TabDemoActivity.class);
				startActivity(intent);
			}
		});
        
        //OptionsMenu菜单
        menu1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, OptionsMenuActivity.class);
				startActivity(intent);
			}
		});
        
      //ContextMenu菜单
        menu2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, ContextMenuActivity.class);
				startActivity(intent);
			}
		});
        
      //SubMenu菜单
        menu3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, SubMenuActivity.class);
				startActivity(intent);
			}
		});
        
        //Activity值传递
        bundle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, Bundle1Activity.class);
				startActivity(intent);
			}
		});
        
        //4中对话框
        alertDialog.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, AlertDialogActivity.class);
				startActivity(intent);
			}
		});
        
        //Notification状态栏提示
        notification.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, NotificationActivity.class);
				startActivity(intent);
			}
		});
    }
}