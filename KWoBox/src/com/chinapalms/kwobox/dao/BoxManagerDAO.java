package com.chinapalms.kwobox.dao;

import java.util.List;

import com.chinapalms.kwobox.javabean.BoxManager;

public interface BoxManagerDAO {

    public List<BoxManager> findBoxManagerByBoxDeliveryId(int boxDeliveryId);

    public List<BoxManager> findBoxManagerByBoxDeliveryIdAndPageNumber(
            int boxDeliveryId, int pageNumber);

    public int findBoxManagerCountByBoxDeliveryId(int boxDeliveryId);

    public List<BoxManager> findBoxManagersByBoxId(String boxId);

    public BoxManager findBoxManagerByBoxIdAndDeliveryId(String boxId,
            int boxDeliveryId);

}
