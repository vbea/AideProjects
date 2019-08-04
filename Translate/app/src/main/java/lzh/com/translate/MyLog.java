package lzh.com.translate;

import android.util.Log;

public class MyLog{

    public static void e(String str1, String str2) {
        if (BuildConfig.DEBUG) {
            Log.e(str1, str2);
        }
    }

    public static void e(String s) {
        if (BuildConfig.DEBUG) {
            Log.e("LOG", s+"");
        }
    }
    public static void d(String s) {
        if (BuildConfig.DEBUG) {
            Log.e("LOG", s+"");
        }
    }

    public static void d(String tag,String s) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, s+"");
        }
    }

    public static void println(String json){
        String[] item=splitStrToArray(json,3000);
        for (int i=0;i<item.length;i++){
            e(item[i]);
        }
    }

    public static String[] splitStrToArray(String str, int length){
        int i = 0;
        boolean isNum = true;
        StringBuffer sb = new StringBuffer("");
        char[] c = str.toCharArray();

        for(int k=0 ; k<c.length; k++){
            isNum = true;
            if(c[k] > 255){
                i+=2;
                isNum = false;
            }else {
                i++;
            }

            if(i >= length + 1){
                sb.append(" | ");

                if(isNum){
                    i=1;
                }else {
                    i=2;
                }
            }

            sb.append(c[k]);
        }

        return sb.toString().split("\\|");
    }
}
