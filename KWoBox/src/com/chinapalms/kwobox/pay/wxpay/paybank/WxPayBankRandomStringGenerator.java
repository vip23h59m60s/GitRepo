package com.chinapalms.kwobox.pay.wxpay.paybank;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class WxPayBankRandomStringGenerator {

    // 获取指定位数随机数
    public static String getRandomStringByLength(int length) {
        String base = "123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    // 该随机数由日期length位随机数构成，共20位
    public static String makeDateStyleRandomString(int length) {
        Date date = new Date();
        Random random = new Random();
        String result = "";
        for (int i = 0; i < length - 14; i++) {
            result += random.nextInt(10);
        }
        return new SimpleDateFormat("yyyyMMddHHmmss").format(date) + result;
    }

    // 通过System.currentTimeMillis()生成随机数
    public static String makeSystemCurrentTimeMillisStyleRandomString(int length) {
        String currentTimeMillisString = String.valueOf(System
                .currentTimeMillis());
        String result = currentTimeMillisString;
        Random random = new Random();
        for (int i = 0; i < length - currentTimeMillisString.length(); i++) {
            result += random.nextInt(10);
        }
        return result;
    }
}
