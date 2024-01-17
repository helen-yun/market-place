package com.pongift20.marketplace.backend.utils;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AES256GCMUtil {
    private static final String KEY_SPEC = "AES";
    private static final String CIPHER = "AES/GCM/NoPadding";
    private static final Charset defaultCharset = StandardCharsets.UTF_8;

    /**
     * 인코딩(aes256gcm)
     * @param plainText 평문 값
     * @param key       키
     * @param iv        초기화 백터(또는 nonce)
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static byte[] encrypt(String plainText, byte[] key, byte[] iv)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

        return encrypt(plainText.getBytes(defaultCharset), key, iv, 128, null);
    }

    public static byte[] encrypt(byte[] plain, byte[] key, byte[] iv, int tLen, byte[] aad)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

        SecretKeySpec secretKey = new SecretKeySpec(key, KEY_SPEC);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(tLen, iv);
        Cipher cipher = Cipher.getInstance(CIPHER);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);

        if (aad != null)
            cipher.updateAAD(aad);
        return cipher.doFinal(plain);
    }

    /**
     * 디코딩(aes256gcm)
     * @param encrypted 인코딩 된 값
     * @param iv        초기화 백터(또는 nonce)
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static byte[] decrypt(byte[] encrypted, byte[] key, byte[] iv)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

        return decrypt(encrypted, key, iv, 128, null);
    }

    public static byte[] decrypt(byte[] encrypted, byte[] key, byte[] iv, int tLen, byte[] aad)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

        SecretKeySpec secretKey = new SecretKeySpec(key, KEY_SPEC);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(tLen, iv);
        Cipher cipher = Cipher.getInstance(CIPHER);

        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);

        if (aad != null)
            cipher.updateAAD(aad);
        return cipher.doFinal(encrypted);
    }
}
