package com.chinapalms.kwobox.dao;

import java.util.List;

import com.chinapalms.kwobox.javabean.ShoppingInfo;

public interface ShoppingInfoDAO {

    public boolean addShoppingInfo(ShoppingInfo shoppingInfo);

    public ShoppingInfo findShoppingInfoByShoppingInfoId(String shoppingInfoId);

    public ShoppingInfo findShoppingInfoByOrderId(String orderId);

    public int findShoppInfoCountByBoxIdAndState(String boxId, int state,
            String startTime, String endTime);

    public List<ShoppingInfo> findShoppInfoByBoxIdAndState(String boxId,
            int state);

    public List<ShoppingInfo> findShoppingInfosByBoxIdAndStateAndPageNumber(
            String boxId, int state, String startTime, String endTime,
            int pageNumber, int pageSize);

    public List<ShoppingInfo> findShoppingInfosByCustomerIdAndPhoneNumberAndState(
            int customerId, String phoneNumber, int state);

    public int findShoppingInfosBoxesCountByCustomerIdAndState(int customerId,
            int state);

    public int findShoppingInfosCountByBoxIdAndState(String boxId, int state);

    public boolean updateShoppingInfoState(ShoppingInfo shoppingInfo);

    public boolean updateShoppingInfoVideosUrl(ShoppingInfo shoppingInfo);

    public boolean updateShoppingInfoOrderId(ShoppingInfo shoppingInfo);

}
