package com.example.searchcity.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**流操作使用的工具类*/
public class StreamUtil {

	/**将给定的输入流内容输出到制定的输出流中
	 * @throws IOException */
	public static void getInputStream(InputStream in,OutputStream out) throws IOException{
		BufferedInputStream bin = new BufferedInputStream(in);
		BufferedOutputStream bout = new BufferedOutputStream(out);
		byte[] buf = new byte[1024];  //缓存
		int len = -1;
		while((len = bin.read(buf)) != -1){
			bout.write(buf, 0, len);   //如果输入流还没有读到结尾，则把读到的内容输出到输出流中
		}
		bout.flush();
	}
}
