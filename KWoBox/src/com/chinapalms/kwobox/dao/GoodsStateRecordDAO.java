package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.GoodsStateRecord;

public interface GoodsStateRecordDAO {

    public boolean addGoodsStateRecord(GoodsStateRecord goodsStateRecord);

    public GoodsStateRecord findGoodsStateRecordByDistinguishId(
            String distinguishId);

    public boolean updateGoodsStateRecordByDistinguishId(String distinguishId,
            int goodsState);

    public boolean updateGoodsStateRecord(GoodsStateRecord goodsStateRecord);

}
