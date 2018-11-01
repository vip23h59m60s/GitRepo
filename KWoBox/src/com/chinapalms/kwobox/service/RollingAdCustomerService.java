package com.chinapalms.kwobox.service;

import java.util.Date;

import net.sf.json.JSONArray;

import com.chinapalms.kwobox.dao.impl.RollingAdCustomerDAOImpl;
import com.chinapalms.kwobox.javabean.RollingAdBox;
import com.chinapalms.kwobox.javabean.RollingAdCustomer;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class RollingAdCustomerService extends RollingAdCustomerDAOImpl {

    @Override
    public RollingAdCustomer findRollingAdCustomerByCustomerId(int customerId) {
        return super.findRollingAdCustomerByCustomerId(customerId);
    }

    public String getRollingAdCustomer(int customerId) {
        RollingAdCustomer rollingAdCustomer = findRollingAdCustomerByCustomerId(customerId);
        // 广告状态为可用
        if (rollingAdCustomer != null && rollingAdCustomer.getAdState() == 1) {
            Date currrentDate = new Date();
            Date adStartTime = rollingAdCustomer.getStartTime();
            Date adEndTime = rollingAdCustomer.getEndTime();
            // 广告时间在有效期内
            if (currrentDate.after(adStartTime)
                    && currrentDate.before(adEndTime)) {
                String adUrlsString = rollingAdCustomer.getCustomerAdUrl();
                if (adUrlsString != null) {
                    String[] urlArray = adUrlsString.split(",");
                    // 将相对url转变为绝对Url
                    String[] urlAbsolutely = new String[urlArray.length];
                    for (int i = 0; i < urlArray.length; i++) {
                        urlAbsolutely[i] = ResponseStatus.SERVER_URL + "/"
                                + urlArray[i];
                    }
                    JSONArray jsonArray = JSONArray.fromObject(urlAbsolutely);
                    return jsonArray.toString();
                } else {
                    return ResponseStatus.FAIL;
                }
            } else {
                return ResponseStatus.FAIL;
            }
        } else {
            return ResponseStatus.FAIL;
        }
    }

}
