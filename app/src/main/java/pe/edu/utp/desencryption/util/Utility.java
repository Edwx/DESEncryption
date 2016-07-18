package pe.edu.utp.desencryption.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by uadin12 on 14/07/2016.
 */
public class Utility {

    public static final String PREFS = "prefs";
    public static final String PREF_KEY = "Key";

    public static SharedPreferences sharedPreferences;

    static public void alert(Context context, String message, boolean voz) {
        CharSequence text = message;
        int duration = Toast.LENGTH_LONG;

        CustomToast miToast = new CustomToast(context,duration);
        miToast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
        miToast.show(text);
        if(voz) {
            ToSpeech a=new ToSpeech(context, message);
        }
    }

    static public String displayKey(Context context){
        openPreferences(context);
        return restoreKeptKey();
    }

    static public void openPreferences(Context context){
        sharedPreferences = context.getSharedPreferences(PREFS, context.MODE_PRIVATE);
    }

    static public void updateKey(String Key){
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString(PREF_KEY, Key);
        e.commit();
    }

    static public String restoreKeptKey(){
        return sharedPreferences.getString(PREF_KEY,"");
    }
}
