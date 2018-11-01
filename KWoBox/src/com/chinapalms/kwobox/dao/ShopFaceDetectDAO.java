package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.ShopFaceDetect;

public interface ShopFaceDetectDAO {

    public ShopFaceDetect findShopFaceDetectByShopIdAndPhoneNumber(int shopId,
            String phoneNumber);

    public boolean addShopFaceDetect(ShopFaceDetect shopFaceDetect);

    public boolean updateShopFaceDetectState(ShopFaceDetect shopFaceDetect);

    public boolean updateLastShopFaceDetectShoppingTime(
            ShopFaceDetect shopFaceDetect);

}
