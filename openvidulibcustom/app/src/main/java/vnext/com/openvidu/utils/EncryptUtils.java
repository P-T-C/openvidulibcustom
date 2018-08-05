package vnext.com.openvidu.utils;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class EncryptUtils {
    public static String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(Contants.INITVECTOR.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(Contants.KEY.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            String encodedString = new String(Base64.encodeBase64(encrypted));
            String safeString = encodedString.replace('+','-').replace('/','_');
            return safeString;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public static void main(String[]args){
        String testKey = encrypt("Test key");
        System.out.println(testKey);
    }

/*	public static String decrypt(String encrypted) throws DecoderException {
		try {
			IvParameterSpec iv = new IvParameterSpec(Constants.INITVECTOR.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(Constants.KEY.getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
			return new String(original);
		} catch (Exception ex) {
			throw new DecoderException(ex.getMessage());
		}
	}*/



}