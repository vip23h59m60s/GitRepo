package com.chinapalms.kwobox.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class LogRandomStringGenerator {
    // 该随机数由日期length位随机数构成，共20位
    public static String makeDateStyleRandomString(int length) {
        Date date = new Date();
        Random random = new Random();
        String result = "";
        for (int i = 0; i < length - 14; i++) {
            result += random.nextInt(10);
        }
        return new SimpleDateFormat("yyyyMMddHHmmss").format(date) + "-"
                + result;
    }
}
