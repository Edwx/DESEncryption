package pe.edu.utp.desencryption.app;

import android.util.Base64;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

/**
 * Created by uadin12 on 14/07/2016.
 */
public class Encryption {
    private static final String UNICODE_FORMAT = "UTF8";
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    public static final String DES_ENCRYPTION_SCHEME = "DES";
    private KeySpec myKeySpec;
    private SecretKeyFactory mySecretKeyFactory;
    private Cipher cipher;
    byte[] keyAsBytes;
    private String myEncryptionKey;
    private String myEncryptionScheme;
    SecretKey key;

    public Encryption(boolean type) throws Exception {

        myEncryptionKey = Utility.myStaticEncryptionKey;
        //myEncryptionKey = "ThisIsSecretEncryptionKey";
        myEncryptionScheme = (type) ? DESEDE_ENCRYPTION_SCHEME : DES_ENCRYPTION_SCHEME;
        keyAsBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
        myKeySpec = (type) ? new DESedeKeySpec(keyAsBytes) : new DESKeySpec(keyAsBytes);
        mySecretKeyFactory = SecretKeyFactory.getInstance(myEncryptionScheme);
        cipher = Cipher.getInstance(myEncryptionScheme);
        key = mySecretKeyFactory.generateSecret(myKeySpec);
    }

    /**
     * Method To Encrypt The String
     */
    public String encrypt(String unencryptedString) {
        String encryptedString = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);

            //BASE64Encoder base64encoder = new BASE64Encoder();
            //encryptedString = base64encoder.encode(encryptedText);
            encryptedString = Base64.encodeToString(encryptedText, Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedString;
    }
    /**
     * Method To Decrypt An Ecrypted String
     */
    public String decrypt(String encryptedString) {
        String decryptedText=null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);

            //BASE64Decoder base64decoder = new BASE64Decoder();
            //byte[] encryptedText = base64decoder.decodeBuffer(encryptedString);
            byte[] encryptedText = Base64.decode(encryptedString, Base64.DEFAULT);

            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText= bytes2String(plainText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }
    /**
     * Returns String From An Array Of Bytes
     */
    private static String bytes2String(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i<bytes.length; i++) {
            stringBuffer.append((char) bytes[i]);
        }
        return stringBuffer.toString();
    }

}