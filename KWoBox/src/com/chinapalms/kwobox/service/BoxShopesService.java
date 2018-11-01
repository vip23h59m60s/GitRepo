package com.chinapalms.kwobox.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.chinapalms.kwobox.dao.impl.BoxShopesDAOImpl;
import com.chinapalms.kwobox.javabean.BoxShopes;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class BoxShopesService extends BoxShopesDAOImpl {

    Log log = LogFactory.getLog(BoxShopesService.class);

    @Override
    public BoxShopes findBoxShopByShopId(int shopId) {
        return super.findBoxShopByShopId(shopId);
    }

    @Override
    public List<BoxShopes> findBoxShopesByCustomerId(int customerId) {
        return super.findBoxShopesByCustomerId(customerId);
    }

    public String doFindCustomerShopes(String customerId) {
        List<BoxShopes> boxShopesList = findBoxShopesByCustomerId(Integer
                .valueOf(customerId));
        if (boxShopesList.size() > 0) {
            JSONObject jsonObject = new JSONObject();
            JSONArray shopJsonArray = new JSONArray();
            for (int i = 0; i < boxShopesList.size(); i++) {
                JSONObject shopjsonObject = JSONObject.fromObject(boxShopesList
                        .get(i));
                shopJsonArray.add(shopjsonObject);
            }
            jsonObject.put("Shopes", shopJsonArray);
            return jsonObject.toString();
        } else {
            return ResponseStatus.FAIL;
        }
    }

}
