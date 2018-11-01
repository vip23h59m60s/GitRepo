package com.chinapalms.kwobox.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.chinapalms.kwobox.dao.impl.InOutGoodsDAOImpl;
import com.chinapalms.kwobox.javabean.GoodsCategory;
import com.chinapalms.kwobox.javabean.GoodsStateRecord;
import com.chinapalms.kwobox.javabean.InOutGoods;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class InOutGoodsService extends InOutGoodsDAOImpl {

    Log log = LogFactory.getLog(InOutGoodsService.class);

    @Override
    public boolean addInOutGoods(
            com.chinapalms.kwobox.javabean.InOutGoods inOutGoods) {
        return super.addInOutGoods(inOutGoods);
    }

    @Override
    public InOutGoods findInOutGoodsByGoodsRFID(String goodsRFID) {
        return super.findInOutGoodsByGoodsRFID(goodsRFID);
    }

    @Override
    public boolean deleteInOutGoodsByGoodsRFID(String goodsRFID) {
        return super.deleteInOutGoodsByGoodsRFID(goodsRFID);
    }

    @Override
    public boolean updateInOutGoods(InOutGoods inOutGoods) {
        return super.updateInOutGoods(inOutGoods);
    }

    public String doInGoodss(String inGoodss) throws Exception {
        JSONObject inGoodssJsonObjectReceived = JSONObject.fromObject(inGoodss);

        log.info("InOutGoodsService->inGoodssJsonObjectReceived="
                + inGoodssJsonObjectReceived);
        InOutGoodsService inOutGoodsService = new InOutGoodsService();
        GoodsStateRecordService goodsStateRecordService = new GoodsStateRecordService();
        GoodsCategoryService goodsCategoryService = new GoodsCategoryService();
        // 数据插入t_inout_goods表的同时，插入t_goods_state_record通过RFID表示的商品记录表中
        JSONArray inOutGoodsObjectsJsonArray = inGoodssJsonObjectReceived
                .getJSONArray("InOutGoodss");
        Date inOutTime = new Date();
        for (int i = 0; i < inOutGoodsObjectsJsonArray.size(); i++) {
            JSONObject inOutGoodsJsonObject = inOutGoodsObjectsJsonArray
                    .getJSONObject(i);
            String goodsRFID = inOutGoodsJsonObject
                    .getString("inOutGoodsDistinguishId");
            String barCodeId = inOutGoodsJsonObject.getString("barCodeId");
            // 根据barCodeId从内部限定可售商品类别表中查询出商品类别信息
            GoodsCategory goodsCategory = goodsCategoryService
                    .findGoodsCategoryByBarCodeId(barCodeId);
            // 商品名称构造为实际商品名称+规格(容量(ml)或者重量(g))
            // 从商品类别表中获取商品单位：无单位或ml或者g
            int goodsUnit = goodsCategory.getGoodsUnit();
            String goodsName = "";
            String goodsUnitSuffix = "";
            if (goodsUnit == 0) {
                goodsUnitSuffix = ResponseStatus.GOODS_UNIT_NONE;
                goodsName = goodsCategory.getGoodsName() + goodsUnitSuffix;
            } else if (goodsUnit == 1) {
                goodsUnitSuffix = ResponseStatus.GOODS_UNIT_ML;
                goodsName = goodsCategory.getGoodsName() + " "
                        + goodsCategory.getGoodsSpec() + goodsUnitSuffix;
            } else if (goodsUnit == 2) {
                goodsUnitSuffix = ResponseStatus.GOODS_UNIT_G;
                goodsName = goodsCategory.getGoodsName() + " "
                        + goodsCategory.getWeight() + goodsUnitSuffix;
            }
            String goodsPrice = inOutGoodsJsonObject.getString("goodsPrice");
            String goodsDiscount = inOutGoodsJsonObject
                    .getString("goodsDiscount");
            String favourable = inOutGoodsJsonObject.getString("favourable");
            // 商品生产厂商从内部系统限定商品类别表中查询得到
            String manufacturer = goodsCategory.getBrandCompany();
            String goodsChannel = inOutGoodsJsonObject
                    .getString("goodsChannel");
            String makeDate = inOutGoodsJsonObject.getString("makeDate");
            String expiryTime = String.valueOf(goodsCategory.getExpiryTime());
            // 商品图片从内部系统限定商品类别表中查询得到
            String goodsPicture = goodsCategory.getGoodsPicture();
            int customerId = Integer.valueOf(inOutGoodsJsonObject
                    .getString("customerId"));
            int workerId = Integer.valueOf(inOutGoodsJsonObject
                    .getString("workerId"));

            InOutGoods inOutGoods = new InOutGoods();
            inOutGoods.setGoodsRFID(goodsRFID);
            inOutGoods.setBarCodeId(barCodeId);
            inOutGoods.setGoodsName(goodsName);
            inOutGoods.setGoodsPrice(BigDecimal.valueOf(Double
                    .valueOf(goodsPrice)));
            inOutGoods.setGoodsDiscount(BigDecimal.valueOf(Double
                    .valueOf(goodsDiscount)));
            inOutGoods.setFavourable(BigDecimal.valueOf(Double
                    .valueOf(favourable)));
            inOutGoods.setManufacturer(manufacturer);
            inOutGoods.setGoodsChannel(goodsChannel);
            inOutGoods.setMakeDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .parse(makeDate));
            inOutGoods.setExpiryTime(Integer.valueOf(expiryTime));
            inOutGoods.setGoodsPicture(goodsPicture);
            inOutGoods.setCustomerId(customerId);
            inOutGoods.setWorkerId(workerId);
            inOutGoods.setInOutTime(inOutTime);

            InOutGoods queryInOutGoods = inOutGoodsService
                    .findInOutGoodsByGoodsRFID(goodsRFID);
            // 如果已经存在直接更新，否则插入数据库，保证商品可以重复入库
            if ((queryInOutGoods != null && inOutGoodsService
                    .updateInOutGoods(inOutGoods))
                    || (queryInOutGoods == null && inOutGoodsService
                            .addInOutGoods(inOutGoods))) {
                // 将入库商品信息同步插入t_goods_state_record通过RFID表示的商品记录表中
                GoodsStateRecord goodsStateRecord = new GoodsStateRecord();
                goodsStateRecord.setGoodsRFID(goodsRFID);
                goodsStateRecord.setBarCodeId(barCodeId);
                goodsStateRecord.setGoodsName(goodsName);
                goodsStateRecord.setGoodsPrice(BigDecimal.valueOf(Double
                        .valueOf(goodsPrice)));
                goodsStateRecord.setGoodsDiscount(BigDecimal.valueOf(Double
                        .valueOf(goodsDiscount)));
                goodsStateRecord.setFavourable(BigDecimal.valueOf(Double
                        .valueOf(favourable)));
                goodsStateRecord.setManufacturer(manufacturer);
                goodsStateRecord.setGoodsChannel(goodsChannel);
                goodsStateRecord.setMakeDate(new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss").parse(makeDate));
                goodsStateRecord.setExpiryTime(Integer.valueOf(expiryTime));
                goodsStateRecord.setGoodsPicture(goodsPicture);
                goodsStateRecord.setState(0);
                goodsStateRecord.setCustomerId(customerId);
                goodsStateRecord.setWorkerId(workerId);
                goodsStateRecord.setUpdateDateTime(inOutTime);
                // 考虑到可能有标签复用，如果有该调商品记录,则更新状态,如果没有改商品记录则直接插入该商品记录
                if (goodsStateRecordService
                        .findGoodsStateRecordByDistinguishId(goodsRFID) != null) {
                    goodsStateRecordService
                            .updateGoodsStateRecord(goodsStateRecord);
                } else {
                    goodsStateRecordService
                            .addGoodsStateRecord(goodsStateRecord);
                }
            }
        }
        return ResponseStatus.SUCCESS;
    }
}
