package lzh.com.translate;

import android.widget.Toast;


public class MyToast {
    private static boolean canShow = true;


    public static Toast toast=Toast.makeText(MyApp.getInstance(),"",Toast.LENGTH_SHORT);


    public static void makeText(String text) {
            toast.setText(text);
        toast.show();
    }


}
