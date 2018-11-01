package com.chinapalms.kwobox.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.chinapalms.kwobox.dao.impl.CardgoroadGoodsCalibrationDAOImpl;
import com.chinapalms.kwobox.javabean.CardgoroadGoodsCalibration;

public class CardgoroadGoodsCalibrationService extends
        CardgoroadGoodsCalibrationDAOImpl {

    @Override
    public boolean addCardgoroadGoodsCalibration(
            CardgoroadGoodsCalibration cardgoroadGoodsCalibration) {
        return super.addCardgoroadGoodsCalibration(cardgoroadGoodsCalibration);
    }

    // 校准单号由日期+6位随机数构成，共20位
    public String makeCalibrationRecordId(Date orderDate) {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < 6; i++) {
            result += random.nextInt(10);
        }
        return new SimpleDateFormat("yyyyMMddHHmmss").format(orderDate)
                + result;
    }

}
