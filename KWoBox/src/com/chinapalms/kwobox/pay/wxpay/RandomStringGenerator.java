package com.chinapalms.kwobox.pay.wxpay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 随机字符串生成
 * 
 * @author zuoliangzhu
 * 
 */
public class RandomStringGenerator {
    /**
     * 获取一定长度的随机字符串
     * 
     * @param length
     *            指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomStringByLength(int length) {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
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

}
