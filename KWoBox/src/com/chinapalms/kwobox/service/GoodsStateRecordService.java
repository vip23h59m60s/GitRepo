package com.chinapalms.kwobox.service;

import com.chinapalms.kwobox.dao.impl.GoodsStateRecordDAOImpl;
import com.chinapalms.kwobox.javabean.GoodsStateRecord;

public class GoodsStateRecordService extends GoodsStateRecordDAOImpl {

    @Override
    public boolean addGoodsStateRecord(GoodsStateRecord goodsStateRecord) {
        return super.addGoodsStateRecord(goodsStateRecord);
    }

    @Override
    public GoodsStateRecord findGoodsStateRecordByDistinguishId(
            String distinguishId) {
        return super.findGoodsStateRecordByDistinguishId(distinguishId);
    }

    @Override
    public boolean updateGoodsStateRecordByDistinguishId(String distinguishId,
            int goodsState) {
        return super.updateGoodsStateRecordByDistinguishId(distinguishId,
                goodsState);
    }

    @Override
    public boolean updateGoodsStateRecord(GoodsStateRecord goodsStateRecord) {
        return super.updateGoodsStateRecord(goodsStateRecord);
    }

}
