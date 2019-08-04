package com.vbea.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

import android.app.AlertDialog;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Bundle;
import android.os.StatFs;
import android.os.Message;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.PopupWindow;
import android.net.Uri;

import com.binxin.zdapp.classes.MD5Util;
import com.binxin.zdapp.classes.FilesText;
import com.binxin.zdapp.classes.FilesTextListAdapter;
import com.binxin.zdapp.classes.ExceptionHandler;
import com.binxin.zdapp.classes.ProgressClass;
import com.binxin.zdapp.classes.FileCommon;
import com.binxin.zdapp.classes.Common;

//正德应用6.7
//新版文件管理器
//创建时间：2015年04月27日
//创建者：邠心
public class MainActivity extends Activity
{
	private List<FilesText> mFilesList = new ArrayList<FilesText>();
	private File curDirectory = new File("");
	private File myTmpFile = null;
	private int	myTmpOpt = -1,mode = 0,popCode = 0;
	private final int FI_COPY = 0, FI_CUT = 1;
	private Builder menuDialog;
	private boolean showHide,dfolder,isRun;
	private TextView attr_md5,attr_sha1,attr_size,title,parent,attr_name,attr_path,attr_time,attr_mime,attr_md5t,attr_attr,attr_owner,attr_user,tname,tpers,opers;
	private ProgressBar pros;
	private Button btn_paste,btn_cancel;
	private ImageButton btn_option;
	private FolderSizeThread folderThread = null;
	private FileMD5Thread md5thread = null;
	private SharedPreferences spf;
	private ListView mListView;
	private LinearLayout actionBar,titleLayout;
	private long copyCur = 0;
	private long FILE_BIG = 10485760;
	private Builder copyDialog;
	private AlertDialog copyAlert;
	private PopupWindow popWindow;
	private Thread copyThread;
	private final FileCommon filecom = new FileCommon();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_mng);
		init();
		if (spf.getBoolean("savedc",true))
			browseTo(new File(getSavePath()));
		else
			browseToRoot();
		
	}
	//初始化
	private void init()
	{
		mListView = (ListView) findViewById(R.id.file_listView);
		actionBar = (LinearLayout) findViewById(R.id.file_copyBar);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		btn_option = (ImageButton) findViewById(R.id.btn_fileopt);
		btn_paste = (Button) findViewById(R.id.btn_filePaste);
		btn_cancel = (Button) findViewById(R.id.btn_cancelPaste);
		title = (TextView) findViewById(R.id.txt_fileTitle);
		parent = (TextView) findViewById(R.id.txt_fileParent);
		
		getSet();
		poppWindow();
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> l, View v, int position, long id)
			{
				// 取得选中的一项的文件名
				String selectedFileString = mFilesList.get(position).getPath();
				if (selectedFileString.equals(getResources().getString(R.string.up_one_level)))
				{
					//返回上一级目录
					upOneLevel();
				}
				else
				{	
					File clickedFile = new File(selectedFileString);
					if(clickedFile != null)
						browseTo(clickedFile);
				}
			}
		});
		
		mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> p1, View p2, int position, long id)
			{
				String tmpFile = mFilesList.get(position).getPath();
				if (!tmpFile.equals(getResources().getString(R.string.up_one_level)))
				{
					fileOptMenu(new File(tmpFile));
				}
				return true;
			}
		});
		
		btn_paste.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				MyPaste();
				actionBar.setVisibility(View.GONE);
			}
		});
		
		btn_cancel.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				myTmpFile = null;
				actionBar.setVisibility(View.GONE);
			}
		});
	}
	
	//返回上一级目录
	private void upOneLevel()
	{
		if(this.curDirectory.getParent() != null)
			this.browseTo(this.curDirectory.getParentFile());
		else
			this.browseTo(new File("/"));
	}
	//浏览文件系统的根目录
	private void browseToRoot() 
	{
		browseTo(new File("/"));
    }
	//浏览指定的目录,如果是文件则进行打开操作
	private void browseTo(final File file)
	{
		try
		{
			if (file.isDirectory())
			{
				setTitle(file);
				curDirectory = file;
				fill(file.listFiles());
			}
			else
			{
				openFile(file);
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.log(e.toString());
			setTitle(file);
			curDirectory = file;
			filltemp();
		}
	}
	//浏览文件系统SD卡目录
	private void browseToSD()
	{
		if (filecom.ExistSDCard())
		{
			browseTo(Environment.getExternalStorageDirectory());
		}
		else
		{
			Common.showShortToast(getApplicationContext(), "当前未挂载SD卡");
		}
	}
	//打开指定文件
	protected void openFile(File aFile)
	{
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		File file = new File(aFile.getAbsolutePath());
		// 取得文件名
		String fileName = file.getName();
		// 根据不同的文件类型来打开文件
		if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingImage)))
		{
			intent.setDataAndType(Uri.fromFile(file), "image/*");
		}
		else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingAudio)))
		{
			intent.setDataAndType(Uri.fromFile(file), "audio/*");
		}
		else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingVideo)))
		{
			intent.setDataAndType(Uri.fromFile(file), "video/*");
		}
		else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.app)))
		{
			intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		}
		else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.textfile)))
		{
			intent.setDataAndType(Uri.fromFile(file), "text/plain");
		}
		else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingWebText)))
		{
			intent.setDataAndType(Uri.fromFile(file), "text/html");
		}
		else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingPackage)))
		{
			intent.setDataAndType(Uri.fromFile(file), "application/zip");
		}
		else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.word)))
		{
			intent.setDataAndType(Uri.fromFile(file), "application/msword");
		}
		else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.excel)))
		{
			intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
		}
		else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.ppt)))
		{
			intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-powerpoint");
		}
		else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.pdf)))
		{
			intent.setDataAndType(Uri.fromFile(file), "application/pdf");
		}
		else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.database)))
		{
			intent.setDataAndType(Uri.fromFile(file), "application/octet-stream");
		}
		startActivity(intent);
	}
	//这里可以理解为设置ListActivity的源
	private void fill(File[] files)
	{
		List<FilesText> _fileDestry,_filesList = null;
		if (dfolder)
		{
			_fileDestry = new ArrayList<FilesText>();
			_filesList = new ArrayList<FilesText>();
		}
		else
			_fileDestry = new ArrayList<FilesText>();

		//清空列表
		this.mFilesList.clear();
		//如果不是根目录则添加上一级目录项
		if (this.curDirectory.getParent() != null)
		{
			this.mFilesList.add(new FilesText(getString(R.string.up_one_level), getResources().getDrawable(R.drawable.folder),""));
		}

		Drawable currentIcon = null;
		for (File currentFile : files)
		{
			if (!showHide)
			{
				if (currentFile.getName().substring(0,1).equals("."))
					continue;
			}
			String details = "";
			if (mode == 1)
			{
				if (currentFile.isDirectory())
					details = filecom.getFileDete(currentFile);
				else
					details = filecom.getFileDete(currentFile) + " " + formatSize(currentFile.length());
			}
			//判断是一个文件夹还是一个文件
			if (currentFile.isDirectory())
			{
				currentIcon = getResources().getDrawable(R.drawable.folder);
				_fileDestry.add(new FilesText(currentFile.getName(), currentFile.getAbsolutePath(), currentIcon, details));
			}
			else
			{
				//取得文件名
				String fileName = currentFile.getName();
				//根据文件名来判断文件类型，设置不同的图标
				if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingImage)))
				{
					currentIcon = getResources().getDrawable(R.drawable.image);
				}
				else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingWebText)))
				{
					currentIcon = getResources().getDrawable(R.drawable.webtext);
				}
				else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingPackage)))
				{
					currentIcon = getResources().getDrawable(R.drawable.packed);
				}
				else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingAudio)))
				{
					currentIcon = getResources().getDrawable(R.drawable.audio);
				}
				else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingVideo)))
				{
					currentIcon = getResources().getDrawable(R.drawable.video);
				}
				else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.textfile)))
				{
					currentIcon = getResources().getDrawable(R.drawable.text);
				}
				else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.app)))
				{
					currentIcon = getResources().getDrawable(R.drawable.ic_app);
				}
				else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.word)))
				{
					currentIcon = getResources().getDrawable(R.drawable.word_icon);
				}
				else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.excel)))
				{
					currentIcon = getResources().getDrawable(R.drawable.excel_icon);
				}
				else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.ppt)))
				{
					currentIcon = getResources().getDrawable(R.drawable.ppt_icon);
				}
				else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.pdf)))
				{
					currentIcon = getResources().getDrawable(R.drawable.pdf_icon);
				}
				else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.database)))
				{
					currentIcon = getResources().getDrawable(R.drawable.database);
				}
				else
				{
					currentIcon = getResources().getDrawable(R.drawable.default2);
				}
				if (dfolder)
				{
					_filesList.add(new FilesText(currentFile.getName(),currentFile.getAbsolutePath(), currentIcon, details));
				}
				else
				{
					_fileDestry.add(new FilesText(currentFile.getName(),currentFile.getAbsolutePath(), currentIcon, details));
				}
			}
			//确保只显示文件名、不显示路径如：/sdcard/111.txt就只是显示111.txt
			//int currentPathStringLenght = this.currentDirectory.getAbsolutePath().length();
		}
		if (dfolder)
		{
			//排序
			Collections.sort(_fileDestry);
			Collections.sort(_filesList);
			//列表显示
			this.mFilesList.addAll(_fileDestry);
			this.mFilesList.addAll(_filesList);
			//清空
			_filesList.clear();
		}
		else
		{
			Collections.sort(_fileDestry);
			this.mFilesList.addAll(_fileDestry);
		}
		_fileDestry.clear();

		FilesTextListAdapter ftla = new FilesTextListAdapter(getApplicationContext(),this);
		//将表设置到ListAdapter中
		ftla.setListItems(this.mFilesList);
		//为ListActivity添加一个ListAdapter
		mListView.setAdapter(ftla);
	}

	private void filltemp()
	{
		this.mFilesList.clear();
		this.mFilesList.add(new FilesText(getString(R.string.up_one_level), getResources().getDrawable(R.drawable.folder),""));
		FilesTextListAdapter ftla = new FilesTextListAdapter(getApplicationContext(),this);
		//将表设置到ListAdapter中
		ftla.setListItems(this.mFilesList);
		//为ListActivity添加一个ListAdapter
		mListView.setAdapter(ftla);
	}
	//得到当前目录的绝对路劲
	public String getCurDirectory()
	{
		return this.curDirectory.getAbsolutePath();
	}
	//通过文件名判断是什么类型的文件
	private boolean checkEndsWithInStringArray(String checkItsEnd, String[] fileEndings)
	{
		for(String aEnd : fileEndings)
		{
			if(checkItsEnd.endsWith(aEnd))
				return true;
		}
		return false;
	}
	//设置标题
	public void setTitle(File file)
	{
		title.setText(file.getName());
		if (file.getParent() != null)
		{
			parent.setText(file.getParent());
			parent.setVisibility(View.VISIBLE);
		}
		else
		{
			title.setText(file.getPath());
			parent.setVisibility(View.GONE);
		}
	}
	//处理文件，包括打开，重命名等操作
	public void fileOptMenu(final File file)
	{
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				switch(which)
				{
					case 0://打开
					{
						if (file.isFile())
							openFile(file);
						else
						{
							browseTo(file);
						}
						break;
					}
					case 1://重命名
						myRename(file);
						break;
					case 2://删除
						myDelete(file);
						break;
					case 3://复制
					{
						//保存复制的文件目录
						myTmpFile = file;
						myTmpOpt = FI_COPY;
						actionBar.setVisibility(View.VISIBLE);
						break;
					}
					case 4://剪切
					{
						//if (file.isFile())
						{
							//保存复制的文件目录
							myTmpFile = file;
							myTmpOpt = FI_CUT;
							actionBar.setVisibility(View.VISIBLE);
						}
						//else
							//Common.showShortToast(getApplicationContext(),"文件夹暂不支持此操作");
						break;
					}
					case 5://属性
					{
						showAttrDialog(file);
						break;
					}
				}
			}
		};
		//显示操作菜单
		String[] menu={getString(R.string.open),getString(R.string.rename),getString(R.string.delete),getString(R.string.dc_copy),getString(R.string.cut),getString(R.string.attr)};
	    menuDialog = new Builder(MainActivity.this);
		menuDialog.setTitle(file.getName());
		menuDialog.setItems(menu,listener);
		menuDialog.create();
		menuDialog.show();
	}
	//重命名
	private void myRename(final File file)
	{
		//自定义一个带输入的对话框由TextView和EditText构成
		final LayoutInflater factory = LayoutInflater.from(MainActivity.this);
		final View dialogview = factory.inflate(R.layout.file_rename, null);
		//设置TextView的提示信息
		((TextView) dialogview.findViewById(R.id.TextView01)).setText("重命名");
		//设置EditText输入框初始值
		((EditText) dialogview.findViewById(R.id.EditText01)).setText(file.getName());

		Builder builder = new Builder(MainActivity.this);
		builder.setTitle(R.string.rename);
		builder.setView(dialogview);
		builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				//点击确定之后
				String value = getCurDirectory()+"/"+((EditText) dialogview.findViewById(R.id.EditText01)).getText().toString();
				if(new File(value).exists())
				{
					Builder builder = new Builder(MainActivity.this);
					builder.setTitle(R.string.rename);
					builder.setMessage(R.string.file_namewarning);
					builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{
							//String str2 = getCurDirectory()+"/"+((EditText) dialogview.findViewById(R.id.EditText01)).getText().toString();
							//file.renameTo(new File(str2));
							//browseTo(new File(getCurDirectory()));
							Common.showShortToast(getApplicationContext(),R.string.operate_success);
						}
					});
					builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{
							dialog.cancel();
						}
					});
					builder.setCancelable(false);
					builder.create();
					builder.show();
				}
				else 
				{
					//重命名
					if (file.renameTo(new File(value)))
					{
						browseTo(new File(getCurDirectory()));
						Common.showShortToast(getApplicationContext(),R.string.operate_success);
					}
					else
						Common.showShortToast(getApplicationContext(),R.string.operate_fail);
				}
			}
		});
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener()
		{
			public void onCancel(DialogInterface dialog)
			{
				dialog.cancel();
			}
		});
		builder.show();
	}
	//删除文件(夹)
	private void myDelete(final File file)
	{
		String titleM = getString(R.string.delete_file);
		if (file.isDirectory())
			titleM = getString(R.string.delete_folder);
		Builder builder = new Builder(MainActivity.this);
		builder.setTitle(titleM);
		builder.setMessage(R.string.delete_warn);
		builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				if (file.isFile())
				{
					if (deleteFile(file))
					{
						browseTo(new File(getCurDirectory()));
						Common.showShortToast(getApplicationContext(),R.string.delete_success);
					}
					else 
					{
						Common.showShortToast(getApplicationContext(),R.string.delete_fail);
					}
				}
				else
					deleteFolder(file);
			}
		});
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		});
		builder.setCancelable(false);
		builder.create();
		builder.show();
	}
	//删除文件
    public boolean deleteFile(File file)
	{
		boolean result = false;
		if (file != null)
		{
			try
			{
				File file2 = file;
				file2.delete();
				result = true;
			}
			catch (Exception e)
			{
				ExceptionHandler.log(e.getMessage());
				result = false;
			}
		}
		return result;
	} 
    //删除文件夹
	public void deleteFolder(File folder)
	{
		try
		{
			String childs[] = folder.list();
			if (childs == null || childs.length <= 0)
			{
				if (folder.delete())
				{
					Common.showShortToast(getApplicationContext(),R.string.delete_success);
					this.browseTo(this.curDirectory);
				}
			}
			else
			{
				deleteDesDialog(folder);
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.log(e.toString());
			Common.showShortToast(getApplicationContext(),R.string.delete_fail);
		}
	}
	public void deleteDesDialog(final File folder)
	{
		Builder builde = new Builder(this);
		builde.setTitle(R.string.tips);
		builde.setMessage(R.string.deletefile_notempty);
		builde.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int swich)
			{
				if (deleteChiFolder(folder))
				{
					browseTo(new File(getCurDirectory()));
					Common.showShortToast(getApplicationContext(),R.string.delete_success);
				}
				else
					Common.showShortToast(getApplicationContext(),R.string.delete_fail);
			}
		});
		builde.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int swith)
			{
				dialog.cancel();
			}
		});
		builde.setCancelable(false);
		builde.create();
		builde.show();
	}
	//删除子文件夹
	public boolean deleteChiFolder(File folder)
	{
		boolean result = false;
		try
		{
			String childs[] = folder.list();
			if (childs == null || childs.length <= 0)
			{
				if (folder.delete())
				{
					result = true;
				}
			}
			else
			{
				for (int i = 0; i < childs.length; i++)
				{
					String childName = childs[i];
					String childPath = folder.getPath() + File.separator + childName;
					File filePath = new File(childPath);
					if (filePath.exists() && filePath.isFile())
					{
						if (filePath.delete())
						{
							result = true;
						}
						else
						{
							result = false;
							break;
						}
					}
					else if (filePath.exists() && filePath.isDirectory())
					{
						if (deleteChiFolder(filePath))
						{
							result = true;
						}
						else
						{
							result = false;
							break;
						}
					}
				}
				folder.delete();
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.log(e.toString() + "\nDelete Child Folder");
			result = false;
		}
		return result;
	}
	//粘贴操作
	public void MyPaste()
	{
		if (myTmpFile == null)
		{
			actionBar.setVisibility(View.GONE);
			return;
		}
		else
		{
			if (myTmpOpt == FI_COPY)//复制操作
			{
				String tmpFilename = myTmpFile.getName();
				String tmpName = "" ,tmpExe = "";
				int cfs = 1;
				boolean exists = new File(getCurDirectory()+"/"+tmpFilename).exists();
				if (myTmpFile.isFile())
				{
					int pashNameLength = tmpFilename.lastIndexOf(".");
					if (pashNameLength > 0 && pashNameLength < (tmpFilename.length() -1))
					{
						tmpName = tmpFilename.substring(0,pashNameLength);
						tmpExe = tmpFilename.substring(pashNameLength);
					}
					else
					{
						tmpName = tmpFilename;
						tmpExe = "";
					}
				}
				else
				{
					tmpName = tmpFilename;
				}
				if(exists)
				{
					int lastc = tmpFilename.lastIndexOf("(");
					if (lastc != -1)
					{
						tmpName = tmpFilename.substring(0,lastc);
						while (new File(getCurDirectory()+"/"+tmpName + "(" + cfs + ")" + tmpExe).exists())
						{
							cfs ++;
						}
					}
					else
					{
						while (new File(getCurDirectory()+"/"+tmpName + "(" + cfs + ")" + tmpExe).exists())
						{
							cfs ++;
						}
					}
				}
				if (myTmpFile.isFile())
				{
					if(exists)
					{
						if (myTmpFile.length() < FILE_BIG)
						{
							copyFile(myTmpFile,new File(getCurDirectory()+"/"+ tmpName + "("+cfs+")" + tmpExe));
							browseTo(new File(getCurDirectory()));
						}
						else
							copyFiles(myTmpFile,new File(getCurDirectory()+"/"+ tmpName + "("+cfs+")" + tmpExe));
					}	
					else
					{
						if (myTmpFile.length() < FILE_BIG)
						{
							copyFile(myTmpFile,new File(getCurDirectory()+"/"+tmpFilename));
							browseTo(new File(getCurDirectory()));
						}
						else
							copyFiles(myTmpFile,new File(getCurDirectory()+"/"+tmpFilename));
						
					}
				}
				else
				{
					if (myTmpFile.listFiles().length > 0 && myTmpFile.listFiles() != null)
					{
						if(exists)
							copyFiles(myTmpFile,new File(getCurDirectory()+"/"+tmpName + "(" + cfs + ")"));
						else
							copyFiles(myTmpFile,new File(getCurDirectory()+"/"+tmpName));
					}
					else
					{
						if(exists)
							new File(getCurDirectory()+"/"+tmpName + "(" + cfs + ")").mkdir();
						else
							new File(getCurDirectory()+"/"+tmpName).mkdir();
					}
					browseTo(new File(getCurDirectory()));
				}
			}
			else if(myTmpOpt == FI_CUT)//剪切操作
			{
				if(new File(getCurDirectory()+"/"+myTmpFile.getName()).exists())
				{
					Builder builder = new Builder(MainActivity.this);
					builder.setTitle(R.string.tips);
					builder.setMessage(R.string.file_namewarning);
					builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which) 
						{
							//moveFile(myTmpFile.getAbsolutePath(),getCurDirectory()+"/"+myTmpFile.getName());
							browseTo(new File(getCurDirectory()));
							Common.showShortToast(getApplicationContext(),R.string.paste_success);
							myTmpFile = null;
						}
					});
					builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{
							dialog.cancel();
						}
					});
					builder.setCancelable(false);
					builder.create();
					builder.show();
				}	
				else
				{
					String tmpFilename1 = myTmpFile.getName();
					if (myTmpFile.isFile())
					{
						if (myTmpFile.length() < FILE_BIG)
						{
							moveFile(myTmpFile,new File(getCurDirectory()+"/" + tmpFilename1));
							browseTo(new File(getCurDirectory()));
						}
						else
							moveFiles(myTmpFile,new File(getCurDirectory()+"/"+ tmpFilename1));
					}
					else
					{
						if (filecom.forderTotalSize(myTmpFile) < FILE_BIG)
						{
							moveFile(myTmpFile,new File(getCurDirectory()+"/" + tmpFilename1));
							browseTo(new File(getCurDirectory()));
						}
						else
						{
							moveFiles(myTmpFile, new File(getCurDirectory()+"/"+ tmpFilename1));
						}
					}
				}
			}
		}
	}
	//复制文件
	public void copyFiles(final File src, final File newf)
	{
		LayoutInflater inflet = LayoutInflater.from(MainActivity.this);
		View copyv = inflet.inflate(R.layout.file_copy,null);
		tname = (TextView) copyv.findViewById(R.id.file_copyName);
		tpers = (TextView) copyv.findViewById(R.id.file_copyPercent);
		opers = (TextView) copyv.findViewById(R.id.file_copyOprate);
		pros = (ProgressBar) copyv.findViewById(R.id.file_copyProgress);
		opers.setText(R.string.copying);
		copyDialog = new Builder(MainActivity.this);
		copyDialog.setTitle(R.string.dc_copy);
		copyDialog.setView(copyv);
		copyDialog.setCancelable(false);
		copyDialog.setPositiveButton(R.string.cancel,null);
		copyAlert = copyDialog.create();
		copyAlert.show();
		copyAlert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				try
				{
					isRun = false;
					opers.setText(R.string.canceling);
					copyThread.interrupt();
				}
				catch (Exception e)
				{
					ExceptionHandler.log(e.toString());
				}
				finally
				{
					handler.sendEmptyMessageDelayed(5,2000);
				}
			}
		});
		copyThread = new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					isRun = true;
					Thread.sleep(500);
					if (src.isFile())
						copyChildFile(src.length(), src, newf);
					else
						copyFolder(filecom.forderTotalSize(src), src, newf);
				}
				catch(Exception e)
				{
					ExceptionHandler.log(e.toString());
				}
			}
		});
		copyThread.start();
	}
	//复制单个文件
	public void copyFile(File src, File target)
	{
		InputStream in = null;
		OutputStream out = null;

		BufferedInputStream bin = null;
		BufferedOutputStream bout = null;
		try
		{
			in = new FileInputStream(src);
			out = new FileOutputStream(target);
			bin = new BufferedInputStream(in);
			bout = new BufferedOutputStream(out);

			byte[] b = new byte[8192];
			int len = bin.read(b);
			while (len != -1)
			{
				bout.write(b, 0, len);
				len = bin.read(b);
			}
			if (target.exists())
				Common.showShortToast(getApplicationContext(),R.string.copy_success);
			else
				Common.showShortToast(getApplicationContext(),R.string.copy_fail);
		}
		catch (FileNotFoundException e)
		{
			ExceptionHandler.log(e.toString());
		}
		catch (IOException e)
		{
			ExceptionHandler.log(e.toString());
		}
		finally
		{
			try
			{
				if (bin != null)
				{
					bin.close();
				}
				if (bout != null)
				{
					bout.close();
				}
			}
			catch (IOException e)
			{
				ExceptionHandler.log(e.toString());
			}
		}
	}
	
	//复制文件夹
	private void copyFolder(long total, File src, File tar)
	{
		tar.mkdir();
		if (src.listFiles().length > 0 && src.listFiles() != null)
		{
			for(File file : src.listFiles())
			{
				if (isRun)
				{
					if (file.isFile())
						copyChildFile(total, file, new File(tar.getPath() + "/" + file.getName()));
					else
						copyFolder(total, file, new File(tar.getPath() + "/" + file.getName()));
				}
			}
		}
	}
	
	//复制子文件
	private void copyChildFile(long total,File src,File target)
	{
		InputStream in = null;
		OutputStream out = null;

		BufferedInputStream bin = null;
		BufferedOutputStream bout = null;
		try
		{
			in = new FileInputStream(src);
			out = new FileOutputStream(target);
			bin = new BufferedInputStream(in);
			bout = new BufferedOutputStream(out);

			byte[] b = new byte[8192];
			int len = bin.read(b);
			int i = 1;
			while (len != -1 && isRun)
			{
				bout.write(b, 0, len);
				len = bin.read(b);
				Message msg = new Message();
				msg.what = 4;
				msg.obj = (Object)(src.getName());
				msg.arg1 = (int)((float)((i * b.length + copyCur) / (float)total) * 100);
				handler.sendMessage(msg);
				i++;
			}
			copyCur += src.length();
		}
		catch (FileNotFoundException e)
		{
			ExceptionHandler.log(e.toString());
		}
		catch (IOException e)
		{
			ExceptionHandler.log(e.toString());
		}
		finally
		{
			try
			{
				if (bin != null)
				{
					bin.close();
				}
				if (bout != null)
				{
					bout.close();
				}
			}
			catch (IOException e)
			{
				ExceptionHandler.log(e.toString());
			}
		}
	}
	
	//移动文件
	public void moveFiles(final File src, final File newf)
	{
		LayoutInflater inflet = LayoutInflater.from(MainActivity.this);
		View copyv = inflet.inflate(R.layout.file_copy,null);
		tname = (TextView) copyv.findViewById(R.id.file_copyName);
		tpers = (TextView) copyv.findViewById(R.id.file_copyPercent);
		opers = (TextView) copyv.findViewById(R.id.file_copyOprate);
		pros = (ProgressBar) copyv.findViewById(R.id.file_copyProgress);
		opers.setText(R.string.moving);
		copyDialog = new Builder(MainActivity.this);
		copyDialog.setTitle(R.string.move);
		copyDialog.setView(copyv);
		copyDialog.setCancelable(false);
		copyDialog.setPositiveButton(R.string.cancel,null);
		copyAlert = copyDialog.create();
		copyAlert.show();
		copyAlert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				try
				{
					isRun = false;
					opers.setText(R.string.canceling);
					copyThread.interrupt();
				}
				catch (Exception e)
				{
					ExceptionHandler.log(e.toString());
				}
				finally
				{
					handler.sendEmptyMessageDelayed(5,2000);
				}
			}
		});
		copyThread = new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					isRun = true;
					Thread.sleep(500);
					if (src.isFile())
						moveChildFile(src.length(), src, newf);
					else
						moveFolder(filecom.forderTotalSize(src), src, newf);
				}
				catch(Exception e)
				{
					ExceptionHandler.log(e.toString());
				}
			}
		});
		copyThread.start();
	}
	//移动单个文件或文件夹
	public void moveFile(File src, File target)
	{
		try
		{
			if (src.renameTo(target))
				Common.showShortToast(getApplicationContext(),R.string.move_success);
			else
				Common.showShortToast(getApplicationContext(),R.string.move_fail);
		}
		catch (Exception e)
		{
			Common.showShortToast(getApplicationContext(),R.string.operate_fail);
			ExceptionHandler.log(e.getMessage());
		}
	}

	//移动文件夹
	private void moveFolder(long total, File src, File tar)
	{
		tar.mkdir();
		if (src.listFiles().length > 0 && src.listFiles() != null)
		{
			for(File file : src.listFiles())
			{
				if (isRun)
				{
					if (file.isFile())
						moveChildFile(total, file, new File(tar.getPath() + "/" + file.getName()));
					else
						moveFolder(total, file, new File(tar.getPath() + "/" + file.getName()));
				}
			}
		}
	}

	//移动子文件
	private void moveChildFile(long total, File src,File target)
	{
		InputStream in = null;
		OutputStream out = null;

		BufferedInputStream bin = null;
		BufferedOutputStream bout = null;
		try
		{
			in = new FileInputStream(src);
			out = new FileOutputStream(target);
			bin = new BufferedInputStream(in);
			bout = new BufferedOutputStream(out);

			byte[] b = new byte[8192];
			int len = bin.read(b);
			int i = 1;
			while (len != -1 && isRun)
			{
				bout.write(b, 0, len);
				len = bin.read(b);
				Message msg = new Message();
				msg.what = 4;
				msg.obj = (Object)(src.getName());
				msg.arg1 = (int)((float)((i * b.length + copyCur) / (float)total) * 100);
				handler.sendMessage(msg);
				i++;
			}
			copyCur += src.length();
		}
		catch (FileNotFoundException e)
		{
			ExceptionHandler.log(e.toString());
		}
		catch (IOException e)
		{
			ExceptionHandler.log(e.toString());
		}
		finally
		{
			try
			{
				if (bin != null)
				{
					bin.close();
				}
				if (bout != null)
				{
					bout.close();
				}
			}
			catch (IOException e)
			{
				ExceptionHandler.log(e.toString());
			}
		}
	}
	//计算文件MD5和SHA-1
	final public class FileMD5Thread extends Thread
	{
		private File _file;
		public FileMD5Thread(File file)
		{
			_file = file;
		}
		@Override
		public void run()
		{
			try
			{
				Message msg,msg1;
				msg = new Message();
				msg1 = new Message();
				msg.what = 1;
				msg1.what = 2;
				msg.obj = (Object)MD5Util.getFileMD5(_file);
				msg1.obj = (Object)MD5Util.getFileSHA1(_file);
				sleep(10);
				handler.sendMessage(msg);
				handler.sendMessage(msg1);
			}
			catch (java.lang.InterruptedException e)
			{
				ExceptionHandler.log(e.toString());
			}
		}
	}
	final public class FolderSizeThread extends Thread
	{
		File _file;
		public FolderSizeThread(File file)
		{
			_file = file;
		}
		@Override
		public void run()
		{
			try
			{
				sleep(10);
				Message msg = new Message();
				msg.what = 3;
				msg.obj = (Object)filecom.formatSize(filecom.forderTotalSize(_file),true);
				handler.sendMessage(msg);
			}
			catch (java.lang.InterruptedException e)
			{

			}
		}
	}
	final Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					attr_md5.setText(msg.obj.toString());
					break;
				case 2:
					attr_sha1.setText(msg.obj.toString());
					break;
				case 3:
					attr_size.setText(msg.obj.toString());
					break;
				case 4:
				{
					//Common.showShortToast(getApplicationContext(),"percent:" + msg.arg1);
					if (msg.arg1 > 100)
						msg.arg1 = 100;
					tname.setText(msg.obj.toString());
					tpers.setText(msg.arg1 + "%");
					pros.setProgress(msg.arg1);
					if (msg.arg1 >= 100)
					{
						if (myTmpOpt == FI_COPY)
						{
							Common.showShortToast(getApplicationContext(),R.string.copy_success);
						}
						if (myTmpOpt == FI_CUT)
						{
							boolean bol = false;
							if (myTmpFile.isFile())
								bol = deleteFile(myTmpFile);
							else
								bol = deleteChiFolder(myTmpFile);
							if (bol)
								Common.showShortToast(getApplicationContext(),R.string.move_success);
							else
								Common.showShortToast(getApplicationContext(),R.string.move_fail);
						}
						copyAlert.dismiss();
						browseTo(new File(getCurDirectory()));
						copyCur = 0;
						myTmpFile = null;
					}
					break;
				}
				case 5:
				{
					copyAlert.dismiss();
					browseTo(new File(getCurDirectory()));
					copyCur = 0;
					myTmpFile = null;
					Common.showShortToast(getApplicationContext(),R.string.operate_cancel);
					break;
				}
			}
			super.handleMessage(msg);
		}
	};
	
	//保存当前浏览的位置
	private void saveFilePath()
	{
		if (spf.getBoolean("savedc",true))
		{
			SharedPreferences.Editor _edt = spf.edit();
			_edt.putString("ZDpath",getCurDirectory());
			_edt.commit();
		}
	}
	//得到保存的位置
	private String getSavePath()
	{
		return spf.getString("ZDpath", "/");
	}
	
	//SD卡剩余空间
	public long getSDFreeSize()
	{
		//取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		//获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		//空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		//返回SD卡空闲大小
		return freeBlocks * blockSize; //单位Byte
	}
	//SD卡总容量
	public long getSDAllSize()
	{
		//取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		//获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		//获取所有数据块数
		long allBlocks = sf.getBlockCount();
		//返回SD卡大小
		return allBlocks * blockSize; //单位Byte
	}

	private String formatSize(long Bytes)
	{
		return filecom.formatSize(Bytes, false);
	}
	
	private void getSet()
	{
		spf = getSharedPreferences("files", Context.MODE_PRIVATE);
		showHide = spf.getBoolean("hide",false);
		mode = spf.getInt("mode",0);
		dfolder = spf.getBoolean("folder",false);
	}
	
	//显示属性对话框
	private void showAttrDialog(final File file)
	{
		LayoutInflater inflat = LayoutInflater.from(MainActivity.this);
		View attrView = inflat.inflate(R.layout.file_details,null);
		attr_name = (TextView) attrView.findViewById(R.id.fileattr_name);
		attr_path = (TextView) attrView.findViewById(R.id.fileattr_path);
		attr_size = (TextView) attrView.findViewById(R.id.fileattr_size);
		attr_time = (TextView) attrView.findViewById(R.id.fileattr_time);
		attr_mime = (TextView) attrView.findViewById(R.id.fileattr_type);
		attr_md5t = (TextView) attrView.findViewById(R.id.fileattr_md5t);
		attr_md5 = (TextView) attrView.findViewById(R.id.fileattr_md5);
		attr_sha1 = (TextView) attrView.findViewById(R.id.fileattr_sha1);
		attr_attr = (TextView) attrView.findViewById(R.id.fileattr_attr);
		attr_user = (TextView) attrView.findViewById(R.id.fileattr_user);
		attr_owner = (TextView) attrView.findViewById(R.id.fileattr_owner);
		LinearLayout sha1l = (LinearLayout) attrView.findViewById(R.id.file_sha1l);
		if (file.isDirectory())
		{
			attr_md5t.setText("类型：");
			attr_md5.setText("文件夹");
			sha1l.setVisibility(View.GONE);
			folderThread = new FolderSizeThread(file);
			folderThread.start();
		}
		else
		{
			attr_size.setText(filecom.formatSize(file.length(),true));//大小
			md5thread = new FileMD5Thread(file);
			md5thread.start();
			//attr_md5.setText(MD5Util.getFileMD5(file));
		}
		String [] attrs = ProgressClass.getFileDetails(file);
		if (attrs != null)
		{
			attr_attr.setText(attrs[0].substring(1));//权限
			attr_owner.setText(attrs[1]);//所有者
			attr_user.setText(attrs[2]);//用户组
		}
		attr_mime.setText(filecom.getMimeType(file));//MIME
		attr_name.setText(file.getName());//名称
		attr_path.setText(file.getParent());//位置
		attr_time.setText(filecom.getFileDete(file));//时间戳
		Builder builder = new Builder(MainActivity.this);
		builder.setTitle(R.string.attr);
		builder.setView(attrView);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface p1)
			{
				try
				{
					if (md5thread != null)
					{
						md5thread.interrupt();
						md5thread = null;
					}
					if (folderThread != null)
					{
						folderThread.interrupt();
						folderThread = null;
					}
				}
				catch (Exception e)
				{
					ExceptionHandler.log(e.toString());
				}
			}
		});
		builder.create();
		builder.show();
	}
	//新建文件夹
	public void myNewFolder()
	{
		final LayoutInflater factory = LayoutInflater.from(MainActivity.this);
		final View dialogview = factory.inflate(R.layout.file_dialog, null);
		//设置TextView
		((TextView) dialogview.findViewById(R.id.TextView_PROM)).setText("请输入文件夹名称");
		//设置EditText
		((EditText) dialogview.findViewById(R.id.EditText_PROM)).setText("新建文件夹");

		Builder builder = new Builder(MainActivity.this);
		builder.setTitle(R.string.newfolder);
		builder.setView(dialogview);
		builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				String value = ((EditText) dialogview.findViewById(R.id.EditText_PROM)).getText().toString();
				if (newFolder(value))
				{
					Common.showShortToast(getApplicationContext(),R.string.operate_success);
				}
				else
				{
					Common.showShortToast(getApplicationContext(),R.string.operate_fail);
				}
			}
		});
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() 
		{
			public void onCancel(DialogInterface dialog) 
			{
				dialog.cancel();
			}
		});
		builder.show();
	}
	//新建文件
	public void myNewFile()
	{
		final LayoutInflater factory = LayoutInflater.from(MainActivity.this);
		final View dialogview = factory.inflate(R.layout.file_dialog, null);
		//设置TextView
		((TextView) dialogview.findViewById(R.id.TextView_PROM)).setText("请输入文件名称");
		//设置EditText
		((EditText) dialogview.findViewById(R.id.EditText_PROM)).setText("未命名");

		Builder builder = new Builder(MainActivity.this);
		builder.setTitle(R.string.newfile);
		builder.setView(dialogview);
		builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				String value = ((EditText) dialogview.findViewById(R.id.EditText_PROM)).getText().toString();
				if (newFilesFun(value))
				{
					Common.showShortToast(getApplicationContext(),R.string.operate_success);
				}
				else
				{
					Common.showShortToast(getApplicationContext(),R.string.operate_fail);
				}
			}
		});
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() 
		{
			public void onCancel(DialogInterface dialog) 
			{
				dialog.cancel();
			}
		});
		builder.show();
	}
	//新建文件夹逻辑
	public boolean newFolder(String file)
	{
		File dirFile = new File(curDirectory.getAbsolutePath()+"/"+file);
		try
		{
			if (!(dirFile.exists()) && !(dirFile.isDirectory()))
			{
				boolean creadok = dirFile.mkdirs();
				if (creadok)
				{
					this.browseTo(curDirectory);
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.log(e.toString());
			return false;
		}
		return true;
	}
	//新建文件逻辑
	public boolean newFilesFun(String file)
	{
		File dirFile = new File(curDirectory.getAbsolutePath()+"/"+file);
		try
		{
			if (!dirFile.exists())
			{
				if (dirFile.createNewFile())
				{
					this.browseTo(curDirectory);
					return true;
				}
				else
				{
					return false;
				}
			}
			else
				return false;
		}
		catch (Exception e)
		{
			ExceptionHandler.log(e.toString());
			return false;
		}
	}
	//菜单栏
	public void poppWindow()
	{
		LayoutInflater inflater = LayoutInflater.from(this);
		// 引入窗口配置文件   
		View popview = inflater.inflate(R.layout.file_menu, null);
		//setTheme(android.R.style.Theme_Holo_Light_NoActionBar);
		// 创建PopupWindow对象
		popWindow = new PopupWindow(popview, ListView.LayoutParams.WRAP_CONTENT, ListView.LayoutParams.WRAP_CONTENT, false);
		// 需要设置一下此参数，点击外边可消失 
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		//设置点击窗口外边窗口消失 
		popWindow.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击   
		popWindow.setFocusable(true);
		popWindow.setOnDismissListener(new PopupWindow.OnDismissListener()
		{
			@Override
			public void onDismiss()
			{
				btn_option.setVisibility(View.VISIBLE);
			}
		});

		btn_option.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(popWindow.isShowing())
				{
					// 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
					popDismiss();
				}
				else
				{
					// 显示窗口   
					popShow();
				}
			}
		});
		btn_option.setOnLongClickListener(new View.OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View p1)
			{
				if(!popWindow.isShowing())
				{
					popShow();
				}
				return true;
			}
		});

		Button btn_newFolder = (Button) popview.findViewById(R.id.file_optNewFolder);
		Button btn_newFile = (Button) popview.findViewById(R.id.file_optNewFile);
		Button btn_refresh = (Button) popview.findViewById(R.id.file_optRefresh);
		Button btn_home = (Button) popview.findViewById(R.id.file_optHome);
		Button btn_sdcard = (Button) popview.findViewById(R.id.file_optSDcard);
		Button btn_store = (Button) popview.findViewById(R.id.file_optStore);
		Button btn_setting = (Button)popview.findViewById(R.id.file_optSetting);
		Button btn_exit = (Button) popview.findViewById(R.id.file_optExit);
		LinearLayout popcv = (LinearLayout) popview.findViewById(R.id.file_optChildView);
		
		btn_newFolder.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				popDismiss();
				myNewFolder();
			}
		});
		btn_newFile.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				popDismiss();
				myNewFile();
			}
		});
		btn_refresh.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				browseTo(curDirectory);
				popDismiss();
			}
		});
		btn_home.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				browseToRoot();
				popDismiss();
			}
		});
		btn_sdcard.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				browseToSD();
				popDismiss();
			}
		});
		btn_store.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				popDismiss();
				Builder builder2 = new Builder(MainActivity.this);
				builder2.setTitle(R.string.store);
				builder2.setMessage("SD卡总容量：" + formatSize(getSDAllSize()) + "\nSD卡已使用：" + formatSize(getSDAllSize() - getSDFreeSize()) + "\nSD卡剩余空间：" + formatSize(getSDFreeSize()));
				builder2.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						//点击确定按钮之后
						dialog.cancel();
					}
				});
				builder2.create();
				builder2.show();
			}
		});
		btn_setting.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				popDismiss();
				startActivity(new Intent(MainActivity.this, Settings.class));
				//overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			}
		});
		
		btn_exit.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				popDismiss();
				saveFilePath();
				finish();
				//overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			}
		});
		popcv.setFocusableInTouchMode(true);//获取焦点
		popcv.setOnKeyListener(new View.OnKeyListener()
		{
			@Override
			public boolean onKey(View v,int keyCode,KeyEvent event)
			{
				if(keyCode == KeyEvent.KEYCODE_MENU && popWindow.isShowing())
				{
					if (popCode == 1)
						popDismiss();
					popCode = 1;
					return true;
				}
				return false;
			}
		});
	}
	public void popShow()
	{
		//设置位置
		popWindow.showAsDropDown(this.findViewById(R.id.file_hidden));//, Gravity.NO_GRAVITY,0,0); //设置在屏幕中的显示位置
		//popWindow.getSoftInputMode();
		btn_option.setVisibility(View.INVISIBLE);
	}
	public void popDismiss()
	{
		popWindow.dismiss();
		btn_option.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		getSet();
		browseTo(curDirectory);
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	//返回键功能定制
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			if (this.curDirectory.getParent() != null)
			{
				this.upOneLevel();
				return false;
			}
			else
			{
				saveFilePath();
				finish();
				//overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			}
		}
		if (keyCode == KeyEvent.KEYCODE_MENU)
		{
			if(!popWindow.isShowing())
			{
				popCode = 0;
				popShow();
			}
			return true;
		}
		return super.onKeyDown(keyCode,event);
	}
}
