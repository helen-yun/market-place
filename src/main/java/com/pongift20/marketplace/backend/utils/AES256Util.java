package com.pongift20.marketplace.backend.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * 암호화 유틸 클래스
 */
public class AES256Util {

    /**
     * ************************************************************************************************
     * AES256 암복호화 처리
     * ************************************************************************************************
     */
    private final String secretKey = "A1aEYORHJBiErB1SilGWC";
    private final String iv = secretKey.substring(0, 16);
    private final Key keySpec;
    private String encoding = "UTF-8";

    public AES256Util(String encoding) throws UnsupportedEncodingException {
        if (!encoding.equals(""))
            this.encoding = encoding;

        byte[] keyBytes = new byte[16];
        byte[] b = secretKey.getBytes(this.encoding);
        int len = b.length;
        if (len > keyBytes.length) {
            len = keyBytes.length;
        }
        System.arraycopy(b, 0, keyBytes, 0, len);

        this.keySpec = new SecretKeySpec(keyBytes, "AES");
    }


    //암호화
    public String aesEncode(String str) throws UnsupportedEncodingException,
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            IllegalBlockSizeException,
            BadPaddingException {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));

        byte[] encrypted = c.doFinal(str.getBytes(this.encoding));
        return new String(Base64.encodeBase64URLSafe(encrypted));
    }


    //복호화
    public String aesDecode(String str) throws UnsupportedEncodingException,
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            IllegalBlockSizeException,
            BadPaddingException {

        if (!StringUtils.hasText(str)) {
            return "";
        }

        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes(this.encoding)));
        byte[] byteStr = Base64.decodeBase64(str.getBytes());

        return new String(c.doFinal(byteStr), this.encoding);
    }

}
