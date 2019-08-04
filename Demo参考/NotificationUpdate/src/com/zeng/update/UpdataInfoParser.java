package com.zeng.update;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;


public class UpdataInfoParser {

	/**
	 * 
	 * @param is
	 *            解析的xml的inputstream
	 * @return updateinfo
	 */
	public static UpdataInfo getUpdataInfo(InputStream is) throws Exception {
		XmlPullParser parser = Xml.newPullParser();
		UpdataInfo info = new UpdataInfo();
		parser.setInput(is, "utf-8");
		int type = parser.getEventType();

		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
			case XmlPullParser.START_TAG:
				if("version".equals(parser.getName())){
					String version = parser.nextText();
					info.setVersion(version);
				}else if("description".equals(parser.getName())){
					String description = parser.nextText();
					info.setDescription(description);
				}else if("apkurl".equals(parser.getName())){
					String apkurl = parser.nextText();
					info.setApkurl(apkurl);
				}
				
				break;

			}

			type = parser.next();
		}
		return info;
	}

}
