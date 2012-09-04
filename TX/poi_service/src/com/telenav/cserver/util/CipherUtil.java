/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.sun.crypto.provider.SunJCE;

/**
 * Copy from 5.x DES encrypt and decrypt, Cserver and Mobile Store project
 * should always keep the same.
 *
 * @author yhhuang 2007-5-17
 * @author zhjdou
 */
public class CipherUtil {

    private CipherUtil() {
    }

    private static final String ALGORITHM = "DES";

    private static final String STRING_ENCODING = "ISO-8859-1";

    private static final byte keyCode[] = { 47, 66, 25, 98, 97, 54, 67, 124, 37 };

    private static SecretKey key = null;

    private static Cipher enCipher = null;

    private static Cipher deCipher = null;
    private static final String ENCRYPT_ERROR = "Encrypt Error:";
    static {
        try {
            Security.addProvider(new SunJCE());
            DESKeySpec dks = new DESKeySpec(keyCode);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            key = keyFactory.generateSecret(dks);
            enCipher = Cipher.getInstance(ALGORITHM);
            enCipher.init(Cipher.ENCRYPT_MODE, key);
            deCipher = Cipher.getInstance(ALGORITHM);
            deCipher.init(Cipher.DECRYPT_MODE, key);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * decrypt input String with DES and return the decrypted reuslt
     *
     * @param hex
     * @return
     * @throws Exception
     */
    public static String decrypt(String hex) throws Exception {
        return decryptByteArray(hex.getBytes(STRING_ENCODING));
    }

    private static String decryptByteArray(byte info[]) throws Exception {

        String result = null;
        try {
            byte clearByte[] = deCipher.doFinal(info);
            result = new String(clearByte, STRING_ENCODING);
        } catch (Exception e) {
            throw new Exception("DES decrypt error " + e, e);
        }
        return result;
    }

    /**
     * encrypt input String with DES and return the encrypted reuslt
     *
     * @param info
     * @return
     * @throws Exception
     */
    public static String encrypt(String info) {
        String result = null;
        try {
            byte cipherByte[] = enCipher
                    .doFinal(info.getBytes(STRING_ENCODING));
            result = new String(cipherByte, STRING_ENCODING);
            return URLEncoder.encode(result, "ISO-8859-1");
        } catch (UnsupportedEncodingException ue) {
            throw new IllegalArgumentException(ENCRYPT_ERROR, ue);
        } catch (BadPaddingException be) {
              throw new IllegalArgumentException(ENCRYPT_ERROR, be);
        } catch (IllegalBlockSizeException ie) {
              throw new IllegalArgumentException(ENCRYPT_ERROR, ie);
        }
    }

    // https://222.66.34.235:9078/wapstore/outlink.do?ls=snc&carrier=4&sptn=W1%D9%07%13%07%C7F%FD%0F%03%94%DE%B4%5E%90&plan=tn_sn_mrc&model=8830_Amr

    public static void main(String args[]) {
        String min = "5198887465";
        System.out.println("orgin text:" + min);
        try {
            String cipher = CipherUtil.encrypt(min);
            System.out.println("cipher text:" + cipher);
            String urlEncodeStr = URLEncoder.encode(cipher, "ISO-8859-1");
            System.out.println("url encoded text:" + urlEncodeStr);
            String urlDecodeStr = URLDecoder.decode(urlEncodeStr, "ISO-8859-1");
            System.out.println("url decoded text:" + urlDecodeStr);
            String plain = CipherUtil.decrypt(cipher);
            System.out.println("plain text:" + plain);
        } catch (Exception e) {
            // unify FIXME Auto-generated catch block
            e.printStackTrace();
        }
    }
}
