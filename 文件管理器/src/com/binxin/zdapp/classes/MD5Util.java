package com.binxin.zdapp.classes;

import java.io.File;
import java.security.MessageDigest;
import java.io.FileInputStream;
import java.math.BigInteger;

public class MD5Util
{
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','A', 'B', 'C', 'D', 'E', 'F' };
	
	public static String toHexString(byte[] b)
	{  //String to  byte
		StringBuilder sb = new StringBuilder(b.length * 2); 
		for (int i = 0; i < b.length; i++)
		{
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);  
		}
		return sb.toString(); 
	}
	public static String md5(String s)
	{
		try
		{
			// Create MD5 Hash
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();
			
			return toHexString(messageDigest);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "转换错误";
		}
	}
	//获取单个文件的MD5值！
	public static String getFileMD5(File file)
	{
		if (!file.isFile())
		{
			return "拒绝访问";
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try
		{
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1)
			{
				digest.update(buffer, 0, len);
			}
			in.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "拒绝访问";
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}
	
	//获取单个文件的SHA1值！
	public static String getFileSHA1(File file)
	{
		if (!file.isFile())
		{
			return "拒绝访问";
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try
		{
			digest = MessageDigest.getInstance("SHA1");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1)
			{
				digest.update(buffer, 0, len);
			}
			in.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "拒绝访问";
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}
	//获取文件夹中文件的MD5值
	/*public static Map<String, String> getDirMD5(File file, boolean listChild) {
		if (!file.isDirectory()) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		String md5;
		File files[] = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isDirectory() && listChild) {
				map.putAll(getDirMD5(f, listChild));
			} else {
				md5 = getFileMD5(f);
				if (md5 != null) {
					map.put(f.getPath(), md5);
				}
			}
		}
		return map;
	}*/
}
