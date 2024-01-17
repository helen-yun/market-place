package com.pongift20.marketplace.backend.utils;

import com.pongift20.marketplace.backend.partner.kyobo.model.vo.AuthKyoboVo;
import com.pongift20.marketplace.backend.partner.kyobo.res.KyoboApiResponse;
import com.pongift20.marketplace.backend.partner.kyobo.service.AuthKyoboService;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


class AES256GCMUtilTest {
    private final AuthKyoboService authKyoboService;

    AES256GCMUtilTest(AuthKyoboService authKyoboService) {
        this.authKyoboService = authKyoboService;
    }

    @Test
    void test1() {
        KyoboApiResponse kyoboApiResponse = new KyoboApiResponse();
        kyoboApiResponse.getBody().setData("gL3RQ7RJE0tEi8zjxTuRThM_oNWH_x7dTVr-Wfi3XlRzrvjzv46eLlIBKN7eGqSeQJigzmwkDbhX0p1HXiGaafIYkMV2BtU4QDYba8r_WtitHOnKPlo1lr5mIrSVJ2co6x4kvzb-NE4e4A45dlILio_LvpzBW8DRbf7fekWC");
        AuthKyoboVo authKyoboVo = authKyoboService.decryptedUserInfo(kyoboApiResponse, "eyGN9CTr9MRIIFdNv4xqoQYly4xgAtkS");

    }



    @Test
    void test2() throws InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
        byte[] plain = {-46, -29, 53, -52, -42, 98, -47, 73, 59, -115, 9, 47, 54, -28, -105, -104, 76, 45, 29, 11, -13, -51, -90, -20, -21, -62, -56, -70, -95, 67, 68, -75, -57, -83, -100, 62, -57, 70, -53, 77, -125, 113, 19, 124, 103, -38, 81, -115, -97, -116};
        byte[] key=  "d91733afa054426da1a95cf2fa3b945b".getBytes();
        byte[] iv = "20230831192212".getBytes();
        int tLen = 128;
        byte[] aad = null;

        String KEY_SPEC = "AES";
        String CIPHER = "AES/GCM/NoPadding";

        SecretKeySpec secretKey = new SecretKeySpec(key, KEY_SPEC);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(tLen, iv);
        Cipher cipher = Cipher.getInstance(CIPHER);

        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);
        if (aad != null) cipher.updateAAD(aad);
        System.out.println(cipher.doFinal(plain));
    }

    public static byte[] convertToByteArray(int[] numbers) {
        byte[] byteArray = new byte[numbers.length];

        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] < -128) {
                byteArray[i] = (byte) -128;
            } else if (numbers[i] > 127) {
                byteArray[i] = (byte) 127;
            } else {
                byteArray[i] = (byte) numbers[i];
            }
        }

        return byteArray;
    }

    public static void main(String[] args) {
        int[] numbers = {-46, -29, 53, -52, -42, 98, -47, 73, 59, -115, 9, 47, 54, -28, -105, -104, 76, 45, 29, 11, -13, -51, -90, -20, -21, -62, -56, -70, -95, 67, 68, -75, -57, -83, -100, 62, -57, 70, -53, 77, -125, 113, 19, 124, 103, -38, 81, -115, -97, -116};
        byte[] byteArray = convertToByteArray(numbers);

        System.out.println(Arrays.toString(byteArray));
    }
}