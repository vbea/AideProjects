package com.binxin.zdapp.classes;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import android.os.Environment;
import android.webkit.MimeTypeMap;

public class FileCommon
{
	//计算文件夹内总大小
	public long forderTotalSize(File folder)
	{
		long tmpSize = 0;
		try
		{
			String childs[] = folder.list();
			if (childs == null || childs.length <= 0)
			{
				return tmpSize;/*folder.length()*/
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
						tmpSize += filePath.length();
					}
					else if (filePath.exists() && filePath.isDirectory())
					{
						tmpSize += forderTotalSize(filePath);
					}
				}
			}
			return tmpSize;
		}
		catch (Exception e)
		{
			ExceptionHandler.log(e.toString());
			return 0;
		}
	}
	//格式化文件大小显示
	public String formatSize(long Bytes, boolean detail)
	{
		String size = "";
		float tempSize = 0;
		if (Bytes >= 1024)
		{
			tempSize = (float)Bytes / 1024;
			if (tempSize >= 1024)
			{
				tempSize = tempSize / 1024;
				if (tempSize >= 1024)
				{
					tempSize = tempSize / 1024;
					size = "GB";
				}
				else
				{
					size = "MB";
				}
			}
			else
			{
				size = "KB";
			}
		}
		else
		{
			size = "B";
		}
		if (tempSize != 0)
		{
			if (!detail)
				return Float.parseFloat(new DecimalFormat("#0.00").format(tempSize)) + size;
			else
				return Float.parseFloat(new DecimalFormat("#0.00").format(tempSize)) + size + " ("+new DecimalFormat().format(Bytes) + "字节)";
		}
		else
			return Bytes + size;
	}
	//获取文件扩展名
	private String getExtension(String name)
	{
		String suffix = "";
		int idx = name.lastIndexOf(".");
		if(idx > 0) {
			suffix = name.substring(idx + 1);
		}
		return suffix;
	}
	//得到扩展名MIME类型
	public String getMimeType(File file)
	{
		String mime = "未知";
		if (file.isFile())
		{
			String tmp = MimeTypeMap.getSingleton().getMimeTypeFromExtension(getExtension(file.getName()));
			if (tmp != null)
				mime = tmp;
		}
		return mime;
	}
	//SD卡是否存在
	public boolean ExistSDCard()
	{
		boolean blnState = false;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			blnState = true;
		}
		return blnState;
	}
	//获取文件修改时间
	public String getFileDete(File file)
	{
		Date fileDate = new Date(file.lastModified());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(fileDate);
	}
	//是否为数字
	public boolean isNumber(String s)
	{
		try
		{
			Integer.parseInt(s);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
}
