package pe.edu.utp.desencryption.app;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by uadin12 on 14/07/2016.
 */
public class Utility {
    public static String myStaticEncryptionKey = "123456789012345678901234";

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
}
