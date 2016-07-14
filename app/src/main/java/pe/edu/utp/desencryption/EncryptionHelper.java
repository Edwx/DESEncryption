package pe.edu.utp.desencryption;

/**
 * Created by uadin12 on 14/07/2016.
 */

import android.util.Base64;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
//string encryption

public class EncryptionHelper {
    private static final String UNICODE_FORMAT = "UTF8";
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";

    // Encrypts string and encode in Base64
    public static String encryptText(String plainText) throws Exception {
        // ---- Use specified 3DES key and IV from other source --------------
        byte[] plaintext = plainText.getBytes();//input

        String myEncryptionKey = "ThisIsSecretEncryptionKey";
        byte[] keyAsBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
        KeySpec myKeySpec = new DESedeKeySpec(keyAsBytes);
        SecretKeyFactory mySecretKeyFactory = SecretKeyFactory.getInstance(DESEDE_ENCRYPTION_SCHEME);
        SecretKey key = mySecretKeyFactory.generateSecret(myKeySpec);

        //byte[] tdesKeyData = Constants.getKey().getBytes();// your encryption key
        //byte[] myIV = Constants.getInitializationVector().getBytes();// initialization vector

        Cipher c3des = Cipher.getInstance("DESede");
        //SecretKeySpec myKey = new SecretKeySpec(tdesKeyData, DESEDE_ENCRYPTION_SCHEME);
        //IvParameterSpec ivspec = new IvParameterSpec(myIV);

        c3des.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherText = c3des.doFinal(plaintext);
        String encryptedString = Base64.encodeToString(cipherText, Base64.DEFAULT);
        // return Base64Coder.encodeString(new String(cipherText));
        return encryptedString;
    }

    public static String decryptText(String plainText) throws Exception {
        // ---- Use specified 3DES key and IV from other source --------------
        byte[] plaintext = plainText.getBytes();//input

        String myEncryptionKey = "ThisIsSecretEncryptionKey";
        byte[] keyAsBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
        KeySpec myKeySpec = new DESedeKeySpec(keyAsBytes);
        SecretKeyFactory mySecretKeyFactory = SecretKeyFactory.getInstance(DESEDE_ENCRYPTION_SCHEME);
        SecretKey key = mySecretKeyFactory.generateSecret(myKeySpec);

        //byte[] tdesKeyData = Constants.getKey().getBytes();// your encryption key
        //byte[] myIV = Constants.getInitializationVector().getBytes();// initialization vector

        Cipher c3des = Cipher.getInstance("DESede");
        //SecretKeySpec myKey = new SecretKeySpec(tdesKeyData, DESEDE_ENCRYPTION_SCHEME);
        //IvParameterSpec ivspec = new IvParameterSpec(myIV);

        c3des.init(Cipher.DECRYPT_MODE, key);
        byte[] decipherText = c3des.doFinal(Base64.decode(plaintext, Base64.DEFAULT));

        String encryptedString = decipherText.toString();
        // return Base64Coder.encodeString(new String(cipherText));
        return encryptedString;
    }

}
