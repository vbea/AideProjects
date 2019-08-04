package lzh.com.translate;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity {

    TransApi transApi;
    File file;
    String appid = "20161223000034515";
    String securityKey = "dI3mIDtvXpYMVDPW_D2k";
    Map<String, String> zhMap = new HashMap<>();
    List<String> keys=new ArrayList<>();
    List<String> valuse=new ArrayList<>();
    File en;
    int p=0;
    LinearLayout view;
    ScrollView scrollView;
    RadioGroup radioGroup;

    String to="en";
    boolean isLoading=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view= (LinearLayout) findViewById(R.id.view);
        scrollView= (ScrollView) findViewById(R.id.scrollView);
        radioGroup= (RadioGroup) findViewById(R.id.radio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (isLoading){
                    MyToast.makeText("翻译完成后再操作");
                    return;
                }
                switch (radioGroup.getId()) {
                    case R.id.en:to="en";
                        break;
                    case R.id.th:to="th";
                        break;
                    case R.id.kh:to="kh";
                        break;
                    case R.id.in:to="in";
                        break;
                }
            }
        });
        findViewById(R.id.chose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLoading){
                    MyToast.makeText("翻译完成后再操作");
                    return;
                }
                choseFile();
            }
        });
        transApi = new TransApi("20161223000034515", "dI3mIDtvXpYMVDPW_D2k");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String re=transApi.getTransResult("中国","zh","en");
//                MyLog.e("RESULT"+re);
//            }
//        }).start();

    }

    public void choseFile() {

        AndPermission.with(this)
                .requestCode(100)
                .permission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .send();


//        translate("china","中国");
//        googleTranslate("中国");
//        youdaoTranslate("中国");

    }

    private void youdaoTranslate(String text){
        try {
            text=URLEncoder.encode(text,"utf-8");
            String url="http://fanyi.youdao.com/openapi.do?keyfrom=laizuhong&key=1919947087&type=data&doctype=json&version=1.1&q="+text;
            OkHttpUtils.get().url(url).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    MyLog.e(e.toString());
                }

                @Override
                public void onResponse(String response, int id) {
                    MyLog.e(response);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    private void googleTranslate(String text){
        String url="https://translation.googleapis.com/language/translate/v2?parameters";
        Map<String,String> map=new HashMap<>();
        map.put("q",text);
        map.put("source","zh");
        map.put("target","en");
        map.put("format","text");
        url=getUrlWithQueryString(url,map);
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                MyLog.e(e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                MyLog.e(response);
            }
        });
    }


    private void showFileDialog() {
        file = new File(getInnerSDCardPath()+"/Download/");
        en = new File(file.getParent() + "/"+to+".xml");
        if (!en.exists()) {
            try {
                en.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File[] files = file.listFiles();
        final List<File> list=new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()){
                list.add(files[i]);
            }
        }
        String[] s = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            s[i] = list.get(i).getName();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(s, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                isLoading=true;
                File f = list.get(i);
                MyLog.e(f.getAbsolutePath());
                initString(f);
            }
        }).create().show();
    }

    private void initString(File file) {
        zhMap = readFile(file.getAbsolutePath());

        if (zhMap.size()==keys.size()){
            MyLog.e("同步完成"+keys.size());
            translate();
        }else {
            MyLog.e("同步出错"+keys.size());
        }
    }


    public  Map<String, String> readFile(String filePath) {
        File file = new File(filePath);
        BufferedReader reader = null;
        Map<String, String> map = new HashMap<>();
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
//                System.out.println("line " + line + ": " + tempString);
                if (!tempString.equals("")) {
                    if (tempString.trim().startsWith("<string name")) {
                        System.out.println("line " + line + ": " + tempString);
                        tempString = tempString.replace("<string name=\"", "");
                        tempString = tempString.replace("</string>", "");
                        String[] item = tempString.split("\">");
                        map.put(item[0], item[1]);
                        keys.add(item[0]);
                    }else if (tempString.trim().startsWith("<string-array")){
                        tempString=tempString.replace("<string-array name=\"","");
                        tempString=tempString.replace("\">","");
                        map.put("string-array"+line,tempString);
                        keys.add("string-array"+line);
                    }else if (tempString.trim().startsWith("<item>")){
                        tempString=tempString.replace("<item>","");
                        tempString=tempString.replace("</item>","");
                        map.put("string-item"+line,tempString);
                        keys.add("string-item"+line);
                    } else if (tempString.trim().equals("</string-array>")) {
                        map.put("string-array"+line,"");
                        keys.add("string-array"+line);
                    }
                }
                line++;
            }
            reader.close();
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {

                }
            }
        }
        return null;
    }

    private void writeFile() {

        for (int i = 0; i < keys.size(); i++) {
            String key=keys.get(i);
            String content=valuse.get(i);
            if (key.startsWith("string-array")&&zhMap.get(key).equals("")){
                write("</string-array>");
            }else if (key.startsWith("string-array")){
                write("<string-array name=\""+content+"\">");
            }else if(key.startsWith("string-item")){
                write("<item>"+content+"</item>");
            }else{
                write("<string name=\""+key+"\">"+content+"</string>");
            }
        }
//        for (String key:enMap.keySet()){
//            String content=enMap.get(key);
//            MyLog.e(en.getAbsolutePath());
//            if (key.startsWith("string-array")&&zhMap.get(key).equals("")){
//                write("</string-array>");
//            }else if (key.startsWith("string-array")){
//                write("<string-array name=\""+content+"\">");
//            }else if(key.startsWith("string-item")){
//                write("<item>"+content+"</item>");
//            }else{
//                write("<string name=\""+key+"\">"+content+"</string>");
//            }
//        }
        addView("写入完成");
        isLoading=false;


    }

    private void write(String content){
        FileWriter fw = null;
        try {
            //如果文件存在，则追加内容；如果文件不存在，则创建文件
            fw = new FileWriter(en, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fw);
        pw.println(content);
        pw.flush();
        try {
            fw.flush();
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode) {
            if (requestCode == 100) {
                showFileDialog();
            }
        }

        @Override
        public void onFailed(int requestCode) {
            if (requestCode == 100) {
                MyToast.makeText("请先打开文件读写权限");
            }
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 只需要调用这一句，剩下的AndPermission自动完成。
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, listener);
    }


    private void translate() {

        if (p==keys.size()){
            addView("开始写文件");
            writeFile();
            return;
        }
        final String key=keys.get(p);
        String text=zhMap.get(key);
        if (key.startsWith("string-array")&&zhMap.get(key).equals("")){
            addView("数组结束");
            valuse.add(p,text);
//            enMap.put(key,text);
            p++;
            translate();
        }else if (key.startsWith("string-array")){
            addView("数组开始");
            valuse.add(p,text);
//            enMap.put(key,text);
            p++;
            translate();
        }else if (key.startsWith("string-item")){
            addView("数组内容");
            translate(key,text);
        }else {
            translate(key,text);
        }
    }

    /**
     * 注意:
     1、请先将需要翻译的文本转换为UTF-8编码
     2、在发送HTTP请求之前需要对各字段做URL encode。
     3、在生成签名拼接 appid+q+salt+密钥 字符串时，text不需要做URL encode，在生成签名之后，发送HTTP请求之前才需要对要发送的待翻译文本字段text做URL encode。
     * @param key
     * @param text
     */
    private void translate(final String key, String text){

        Map<String, String> map = buildParams(text, "zh", to);
        String url=getUrlWithQueryString("http://api.fanyi.baidu.com/api/trans/vip/translate",map);
        MyLog.e(url);
        OkHttpUtils.get().url(url)
                .params(map)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                MyLog.e(e.toString());
                valuse.add(p,"");
                p++;
                translate();
            }

            @Override
            public void onResponse(String response, int id) {
                MyLog.e(response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.has("error_code")){
                        String msg=jsonObject.getString("error_msg")+jsonObject.getString("error_code");
                        MyToast.makeText(msg);
                        isLoading=false;
                    }else {
                        Gson gson = new Gson();
                        Result result = gson.fromJson(response, Result.class);
                        String s = result.getTrans_result().get(0).getDst();
                        valuse.add(p,s);
                        addView(key+"   "+s);
                        p++;
                        MyLog.e(s);
                        translate();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }


    private void addView(String text){
        TextView textView=new TextView(this);
        textView.setText(text);
        view.addView(textView);
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }


    private Map<String, String> buildParams(String query, String from, String to) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);

        params.put("appid", appid);

        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);

        // 签名
        String src = appid + query + salt + securityKey; // 加密前的原文
        params.put("sign", MD5.md5(src));

        return params;
    }


    /**
     * 获取内置SD卡路径
     *
     * @return
     */
    public String getInnerSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 获取外置SD卡路径
     *
     * @return 应该就一条记录或空
     */
    public List<String> getExtSDCardPath() {
        List<String> lResult = new ArrayList<String>();
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("extSdCard")) {
                    String[] arr = line.split(" ");
                    String path = arr[1];
                    File file = new File(path);
                    if (file.isDirectory()) {
                        lResult.add(path);
                    }
                }
            }
            isr.close();
        } catch (Exception e) {
        }
        return lResult;
    }


    public static String getUrlWithQueryString(String url, Map<String, String> params) {
        if (params == null) {
            return url;
        }

        StringBuilder builder = new StringBuilder(url);
        if (url.contains("?")) {
            builder.append("&");
        } else {
            builder.append("?");
        }

        int i = 0;
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (value == null) { // 过滤空的key
                continue;
            }

            if (i != 0) {
                builder.append('&');
            }

            builder.append(key);
            builder.append('=');
            builder.append(encode(value));

            i++;
        }

        return builder.toString();
    }



    /**
     * 对输入的字符串进行URL编码, 即转换为%20这种形式
     *
     * @param input 原文
     * @return URL编码. 如果编码失败, 则返回原文
     */
    public static String encode(String input) {
        if (input == null) {
            return "";
        }

        try {
            return URLEncoder.encode(input, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return input;
    }
}
