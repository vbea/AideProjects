package com.binxin.zdapp.classes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.DataOutputStream;

public class ProgressClass
{
	public static boolean RootCmd(String cmd,boolean exit)
	{
		Process process = null;
		DataOutputStream os = null;
		try
		{
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(cmd + "\n");
			if (exit)
				os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
			return true;
		}
		catch (Exception e)
		{
			ExceptionHandler.log(e.toString());
			return false;
		}
		finally
		{
			try
			{
				if (os != null)
				{
					os.close();
				}
				process.destroy();
			}
			catch (Exception e)
			{
				ExceptionHandler.log(e.toString());
			}
		}
	}
	
	public static boolean ExecCmd(String cmd)
	{
		Process process = null;
		try
		{
			process = Runtime.getRuntime().exec(cmd + "\n");
			process.waitFor();
		}
		catch (Exception e)
		{
			ExceptionHandler.log(e.toString());
			return false;
		}
		return true;
	}
	
	public static boolean ExecCmd(String [] cmd)
	{
		Process process = null;
		DataOutputStream os = null;
		try
		{
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			for (int i = 0;i < cmd.length; i++)
			{
				os.writeBytes(cmd[i] + "\n");
			}
			os.flush();
			process.waitFor();
		}
		catch (Exception e)
		{
			ExceptionHandler.log(e.toString());
			return false;
		}
		finally
		{
			try
			{
				if (os != null)
				{
					os.close();
				}
				process.destroy();
			}
			catch (Exception e)
			{
				ExceptionHandler.log(e.toString());
			}
		}
		return true;
	}
	
	public static boolean RootCmd(String cmd1)
	{
		return RootCmd(cmd1, false);
	}
	
	public static void RootCmdv(String cmd, boolean exit)
	{
		RootCmd(cmd,exit);
	}
	
	public static String [] getFileDetails(File file)
	{
		Process _p = null;
		DataOutputStream os = null;
		try
		{
			String _ls = " -l";
			if (file.isDirectory())
				_ls = " -dl";
			String cmd = "ls " + _ls + " " + file.getAbsolutePath() + "\n";
			_p = Runtime.getRuntime().exec("sh");
			os = new DataOutputStream(_p.getOutputStream());
			os.write(cmd.getBytes("UTF-8"));
			os.writeBytes("exit\n");
			os.flush();
			if (_p.waitFor() == 0)
			{
				InputStream in = _p.getInputStream();
				byte [] b = new byte[in.available()];
				in.read(b);
				String s = new String(b);
				s = s.replaceAll(" +"," ");
				String [] st = s.split(" ");
				return st;
			}
			else
				return getFileDetails(file.getParentFile());
		}
		catch (Exception e)
		{
			ExceptionHandler.log(e.toString());
			return null;
		}
		finally
		{
			try
			{
				if (os != null)
				{
					os.close();
				}
				_p.destroy();
			}
			catch (Exception e)
			{
				ExceptionHandler.log(e.toString());
			}
		}
	}
	/*public static String [] getSuFileDetails(File file)
	{
		try
		{
			Process process = Runtime.getRuntime().exec("su");
			DataOutputStream os = new DataOutputStream(process.getOutputStream());
			String _ls = " -l";
			if (file.isDirectory())
				_ls = " -dl";
			os.writeBytes("ls " + file.getPath() + _ls + "\n");
			os.flush();
			int d = process.waitFor();
			if (d <= 0)
			{
				InputStream in = process.getInputStream();
				byte [] b = new byte[in.available()];
				in.read(b);
				String s = new String(b);
				s = s.replaceAll(" +"," ");
				String [] st = s.split(" ");
				return st;
			}
			else
				return new String[]{"_error:"+d,"root","root"};
		}
		catch (Exception e)
		{
			return null;
		}
	}*/
}
