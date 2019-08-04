package com.umeng.soexample;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;

public class BitmapUtils {

	public static byte[] bitmap2Bytes(Bitmap bitmap) {
		ByteArrayOutputStream baos = null;
		if (bitmap == null) return null;
		try {
			baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			byte[] b = baos.toByteArray();
			return b;
		} catch (Exception e) {
			if (baos != null) try {
				baos.close();
			} catch (IOException e1) {
			}
		}
		return null;
	}
}
