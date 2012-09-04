package com.telenav.cserver.common.encryption;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.log4j.Logger;

import com.sun.crypto.provider.SunJCE;

/**
 * DES encrypt and decrypt, Cserver and Mobile Store project should always keep the same.
 * 
 * @author yhhuang 2007-5-17
 */
public class CipherUtil
{

    private static Logger logger = Logger.getLogger(CipherUtil.class);

    private CipherUtil()
    {
    }

    private static final String ALGORITHM = "DES";

    public static final String STRING_ENCODING = "ISO-8859-1";

    private static final byte keyCode[] =
    { 47, 66, 25, 98, 97, 54, 67, 124, 37 };

    private static SecretKey key = null;

    private static Cipher enCipher = null;

    private static Cipher deCipher = null;

    static
    {
        try
        {
            Security.addProvider(new SunJCE());
            DESKeySpec dks = new DESKeySpec(keyCode);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            key = keyFactory.generateSecret(dks);
            enCipher = Cipher.getInstance(ALGORITHM);
            enCipher.init(Cipher.ENCRYPT_MODE, key);
            deCipher = Cipher.getInstance(ALGORITHM);
            deCipher.init(Cipher.DECRYPT_MODE, key);
        }
        catch (Exception e)
        {
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
        String result=decryptByteArray(hex.getBytes(STRING_ENCODING));
        if(logger.isDebugEnabled())
        {
            logger.debug("before encryption, is "+hex+" after encryption is "+result);
        }
        return result;
    }

    private static String decryptByteArray(byte info[]) throws Exception
    {

        String result = null;
        try
        {
            byte clearByte[] = deCipher.doFinal(info);
            result = new String(clearByte, STRING_ENCODING);
        }
        catch (Exception e)
        {
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
    public static String encrypt(String info) throws Exception
    {
        String result = null;
        try
        {
            byte cipherByte[] = enCipher.doFinal(info.getBytes(STRING_ENCODING));
            result = new String(cipherByte, STRING_ENCODING);

            if (logger.isDebugEnabled())
            {
                logger.debug("before encryption, is " + info + " after encryption is " + result);
            }

        }
        catch (Exception e)
        {
            throw new Exception("DES encrypt error " + e, e);
        }
        return result;
    }

    // https://222.66.34.235:9078/wapstore/outlink.do?ls=snc&carrier=4&sptn=W1%D9%07%13%07%C7F%FD%0F%03%94%DE%B4%5E%90&plan=tn_sn_mrc&model=8830_Amr

//    public static void main(String args[])
//    {
//        String min = "5198887465";
//        System.out.println("orgin text:" + min);
//        try
//        {
//            String cipher = CipherUtil.encrypt(min);
//            System.out.println("cipher text:" + cipher);
//            String urlEncodeStr = URLEncoder.encode(cipher, "ISO-8859-1");
//            System.out.println("url encoded text:" + urlEncodeStr);
//            String urlDecodeStr = URLDecoder.decode(urlEncodeStr, "ISO-8859-1");
//            System.out.println("url decoded text:" + urlDecodeStr);
//            String plain = CipherUtil.decrypt(urlDecodeStr);
//            System.out.println("plain text:" + plain);
//            
//            
//            String encryptedStr="D%C2%B3G%10%28L%C3%A5%02%C2%A3%C2%B8%C2%A3z%C3%8F%C2%B0%C2%832";
//            CipherUtil.decrypt(URLDecoder.decode(encryptedStr, "ISO-8859-1"));
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
}