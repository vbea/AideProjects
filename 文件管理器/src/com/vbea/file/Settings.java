package com.vbea.file;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.KeyEvent;
import android.content.Intent;
import android.content.Context;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.binxin.zdapp.view.SlidButton;

public class Settings extends Activity
{
	private int item1;
	private LinearLayout titleLayout;
	private Button btn_filemode,btn_folder,btn_fhide,btn_savedc;
	private SlidButton sld_folder,sld_fhide,sld_savedc;
	private SharedPreferences sube1;
	private Editor edt1;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Holo_Light_NoActionBar);
		setContentView(R.layout.settings);
		
		ImageButton back = (ImageButton) findViewById(R.id.btn_close);
		btn_filemode = (Button)findViewById(R.id.opt_fileInter);
		btn_folder = (Button) findViewById(R.id.opt_setFolder);
		btn_fhide = (Button) findViewById(R.id.opt_setFileHide);
		btn_savedc = (Button) findViewById(R.id.opt_setFileSave);
		
		sld_folder = (SlidButton)findViewById(R.id.sld_folder);
		sld_fhide = (SlidButton)findViewById(R.id.sld_fileHide);
		sld_savedc = (SlidButton)findViewById(R.id.sld_fileSave);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		
		sube1 = getSharedPreferences("files", Context.MODE_PRIVATE);
		edt1 = sube1.edit();
		getSlidState();
		
		back.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
				overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			}
		});
		
		btn_filemode.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				String[] items = {getString(R.string.file_mode1), getString(R.string.file_mode2)};
				item1 = sube1.getInt("mode", 0);
				Builder builder = new Builder(Settings.this);
				builder.setTitle(R.string.file_inter);
				builder.setSingleChoiceItems(items, item1, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int item)
					{
						item1 = item;
						SharedPreferences sube = getSharedPreferences("files", Context.MODE_PRIVATE);
						SharedPreferences.Editor edit = sube.edit();
						switch(item)
						{
							case 0:
								edit.putInt("mode", 0);
								break;
							case 1:
								edit.putInt("mode", 1);
								break;
						}
						edit.commit();
						dialog.cancel();
					}
				});
				builder.show();
			}
		});
		btn_fhide.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (setBooleanValue(edt1,"hide",!sld_fhide.getChecked()))
					sld_fhide.setChecked(!sld_fhide.getChecked());
			}
		});
		btn_folder.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (setBooleanValue(edt1,"folder",!sld_folder.getChecked()))
					sld_folder.setChecked(!sld_folder.getChecked());
			}
		});
		btn_savedc.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (setBooleanValue(edt1,"savedc",!sld_savedc.getChecked()))
					sld_savedc.setChecked(!sld_savedc.getChecked());
			}
		});
	}
	
	private void getSlidState()
	{
		sld_folder.setChecked(getBooleanValue(sube1,"folder",false));
		sld_fhide.setChecked(getBooleanValue(sube1,"hide",false));
		sld_savedc.setChecked(getBooleanValue(sube1,"savedc",true));
		sld_folder.setEnabled(false);
		sld_fhide.setEnabled(false);
		sld_savedc.setEnabled(false);
	}
	
	private boolean getBooleanValue(SharedPreferences sh,String key,boolean dfault)
	{
		return sh.getBoolean(key, dfault);
	}
	private boolean setBooleanValue(Editor edt,String kty, boolean value)
	{
		edt.putBoolean(kty,value);
		return edt.commit();
	}
	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
	
}
