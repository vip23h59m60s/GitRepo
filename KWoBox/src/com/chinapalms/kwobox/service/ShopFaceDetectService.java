package com.chinapalms.kwobox.service;

import java.util.Date;

import com.chinapalms.kwobox.dao.impl.ShopFaceDetectDAOImpl;
import com.chinapalms.kwobox.javabean.ShopFaceDetect;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class ShopFaceDetectService extends ShopFaceDetectDAOImpl {

    @Override
    public com.chinapalms.kwobox.javabean.ShopFaceDetect findShopFaceDetectByShopIdAndPhoneNumber(
            int shopId, String phoneNumber) {
        return super.findShopFaceDetectByShopIdAndPhoneNumber(shopId,
                phoneNumber);
    }

    @Override
    public boolean addShopFaceDetect(
            com.chinapalms.kwobox.javabean.ShopFaceDetect shopFaceDetect) {
        return super.addShopFaceDetect(shopFaceDetect);
    }

    @Override
    public boolean updateShopFaceDetectState(
            com.chinapalms.kwobox.javabean.ShopFaceDetect shopFaceDetect) {
        return super.updateShopFaceDetectState(shopFaceDetect);
    }

    @Override
    public boolean updateLastShopFaceDetectShoppingTime(
            com.chinapalms.kwobox.javabean.ShopFaceDetect shopFaceDetect) {
        return super.updateLastShopFaceDetectShoppingTime(shopFaceDetect);
    }

    /**
     * 判断当前门店免密模式识别出来的user是否属于有效状态
     * 
     * @param shopId
     * @param phoneNumber
     * @return
     */
    public boolean checkShopDetectState(int shopId, String phoneNumber) {
        ShopFaceDetectService faceDetectService = new ShopFaceDetectService();
        ShopFaceDetect shopFaceDetect = faceDetectService
                .findShopFaceDetectByShopIdAndPhoneNumber(shopId, phoneNumber);
        if (shopFaceDetect != null) {
            if (!isTimeOut(shopFaceDetect.getLastFaceDetectShoppingTime(),
                    new Date()) && shopFaceDetect.getState() == 1) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 判断门店免密模式人脸识别是否超时
     * 
     * @param queryTime
     * @param currentTime
     * @return
     */
    private boolean isTimeOut(Date queryTime, Date currentTime) {
        boolean isTimeOut = false;
        long timeMinute = (currentTime.getTime() - queryTime.getTime()) / 1000 / 60;
        if (timeMinute >= ResponseStatus.SHOP_FACE_DETECT_TIMEOUT_MINUTES) {
            isTimeOut = true;
        }
        return isTimeOut;
    }

}
