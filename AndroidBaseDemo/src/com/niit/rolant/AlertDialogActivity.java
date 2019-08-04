package com.niit.rolant;

import com.cellcom.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//对话框测试
public class AlertDialogActivity extends Activity {

	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_dialog);
		setTitle("4种对话框！");
		
		button1=(Button)findViewById(R.id.button1);
		button2=(Button)findViewById(R.id.button2);
		button3=(Button)findViewById(R.id.button3);
		button4=(Button)findViewById(R.id.button4);
		
		//第一种对话框
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Builder builder=new AlertDialog.Builder(AlertDialogActivity.this);
				builder.setIcon(R.drawable.alert_dialog_icon);
				builder.setTitle("哇哈哈！");
				builder.setMessage("去不去？");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(AlertDialogActivity.this, "你选择了确定按钮！", Toast.LENGTH_SHORT).show();
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(AlertDialogActivity.this, "你选择了取消按钮！", Toast.LENGTH_SHORT).show();
					}
				});
				builder.show();
			}
		});
		
		//第二种对话框
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(AlertDialogActivity.this)
				.setIcon(R.drawable.alert_dialog_icon)
				.setTitle("温馨提示")
				.setMessage("提示内容：三个按钮")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(AlertDialogActivity.this, "你选择了确定按钮！", Toast.LENGTH_SHORT).show();
					}
				})
				.setNeutralButton("详情", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(AlertDialogActivity.this, "你选择了详情按钮！", Toast.LENGTH_SHORT).show();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(AlertDialogActivity.this, "你选择了取消按钮！", Toast.LENGTH_SHORT).show();
					}
				})
				.show();
			}
		});
		
		//第三种按钮
		button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LayoutInflater inflater=LayoutInflater.from(AlertDialogActivity.this);
				final View textEntryView=inflater.inflate(R.layout.alert_dialog_text_entry, null);
				
				final EditText usernameET=(EditText)textEntryView.findViewById(R.id.username_value);
				final EditText passwordET=(EditText)textEntryView.findViewById(R.id.password_value);
				//final String username=usernameET.getText().toString();
				
				new AlertDialog.Builder(AlertDialogActivity.this)
				.setIcon(R.drawable.alert_dialog_icon)
				.setTitle("温馨提醒")
				.setView(textEntryView)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(AlertDialogActivity.this, "用户名="+usernameET.getText().toString()+"\n密码="+passwordET.getText().toString(), Toast.LENGTH_LONG).show();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(AlertDialogActivity.this, "你选择了确定取消！", Toast.LENGTH_SHORT).show();
					}
				})
				.show();
			}
		});
		
		//第四种对话框
		button4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ProgressDialog dialog=new ProgressDialog(AlertDialogActivity.this);
				dialog.setTitle("处理中。。。");
				dialog.setMessage("请稍后。。。");
				dialog.show();
			}
		});
	}
}
