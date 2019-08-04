package com.example.searchcity.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/** 处理文字与拼音转换的类 */
public class PinYinUtil {

	private HanyuPinyinOutputFormat format = null;

	private String[] pinyin;

	public PinYinUtil()

	{

		format = new HanyuPinyinOutputFormat();

		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

		pinyin = null;

	}

	// 转换单个字符

	public String getCharacterPinYin(char c)

	{

		try

		{

			pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);

		}

		catch (BadHanyuPinyinOutputFormatCombination e)

		{

			e.printStackTrace();

		}

		// 如果c不是汉字，toHanyuPinyinStringArray会返回null

		if (pinyin == null)
			return null;

		// 只取一个发音，如果是多音字，仅取第一个发音

		return pinyin[0];

	}

	// 获取首字母
	public static String getFirstSpell(String chinese) {
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(
							arr[i], defaultFormat);
					if (temp != null) {
						pybf.append(temp[0].charAt(0));
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString().replaceAll("\\W", "").trim();
	}

	// 转换一个字符串

	public String getStringPinYin(String str)

	{

		StringBuilder sb = new StringBuilder();

		String tempPinyin = null;

		for (int i = 0; i < str.length(); ++i)

		{

			tempPinyin = getCharacterPinYin(str.charAt(i));

			if (tempPinyin == null)

			{

				// 如果str.charAt(i)非汉字，则保持原样

				sb.append(str.charAt(i));

			}

			else

			{

				sb.append(tempPinyin);

			}

		}

		return sb.toString();

	}

}
