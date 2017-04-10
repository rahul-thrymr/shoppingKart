package utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Util {
	
	private static final String AES = "AES";
	public static String encode(String str){
        SecretKeySpec key = new SecretKeySpec(getCipherKey().getBytes(), AES);
        String encodedStr;
        byte[] b=null;
        try {
            Cipher cipher= Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE,key);
            b=cipher.doFinal(str.getBytes());
        }catch(Exception e){
            e.printStackTrace();
        }
        encodedStr=Base64.encodeBase64URLSafeString(b);
        return encodedStr;
    }
	
	public static String decode(String token) {
		   SecretKeySpec key = new SecretKeySpec(getCipherKey().getBytes(), AES);
		   byte[] b=null;
		   try {
		       b=Base64.decodeBase64(token.getBytes());
		       Cipher cipher= Cipher.getInstance("AES/ECB/PKCS5Padding");
		       cipher.init(Cipher.DECRYPT_MODE,key);
		       b=cipher.doFinal(b);
		   }catch(Exception e){
		       e.printStackTrace();
		   }
		   return new String(b);
	    }
	
	private static String getCipherKey() {
	    return "truweight>!*~`']";
	}
}
