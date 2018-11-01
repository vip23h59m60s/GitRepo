package com.chinapalms.kwobox.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.chinapalms.kwobox.dao.impl.BoxManagerDAOImpl;
import com.chinapalms.kwobox.javabean.BoxManager;

public class BoxManagerService extends BoxManagerDAOImpl {

    @Override
    public List<BoxManager> findBoxManagerByBoxDeliveryId(int boxDeliveryId) {
        return super.findBoxManagerByBoxDeliveryId(boxDeliveryId);
    }

    @Override
    public List<BoxManager> findBoxManagerByBoxDeliveryIdAndPageNumber(
            int boxDeliveryId, int pageNumber) {
        return super.findBoxManagerByBoxDeliveryIdAndPageNumber(boxDeliveryId,
                pageNumber);
    }

    @Override
    public int findBoxManagerCountByBoxDeliveryId(int boxDeliveryId) {
        return super.findBoxManagerCountByBoxDeliveryId(boxDeliveryId);
    }

    @Override
    public List<BoxManager> findBoxManagersByBoxId(String boxId) {
        return super.findBoxManagersByBoxId(boxId);
    }

    @Override
    public BoxManager findBoxManagerByBoxIdAndDeliveryId(String boxId,
            int boxDeliveryId) {
        return super.findBoxManagerByBoxIdAndDeliveryId(boxId, boxDeliveryId);
    }

    public List<Integer> doFindUniqueDeliveryIdOnSameBox(String boxId) {
        List<BoxManager> boxManagersList = findBoxManagersByBoxId(boxId);
        List<Integer> deliveryIdsList = new ArrayList<Integer>();
        for (int i = 0; i < boxManagersList.size(); i++) {
            deliveryIdsList.add(boxManagersList.get(i).getBoxDeliveryId());
        }
        // 筛选出不同的送货员ID
        Set<Integer> uniqueSetDeliveryIdSet = new HashSet<Integer>(
                deliveryIdsList);
        List<Integer> deliveryIdsListUnique = new ArrayList<Integer>();
        for (int deliveryId : uniqueSetDeliveryIdSet) {
            deliveryIdsListUnique.add(deliveryId);
        }
        return deliveryIdsListUnique;
    }

}
