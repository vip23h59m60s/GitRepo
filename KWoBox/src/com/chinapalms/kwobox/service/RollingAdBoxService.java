package com.chinapalms.kwobox.service;

import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.chinapalms.kwobox.dao.impl.RollingAdBoxDAOImpl;
import com.chinapalms.kwobox.javabean.RollingAdBox;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class RollingAdBoxService extends RollingAdBoxDAOImpl {

    @Override
    public RollingAdBox findRollingAdBoxByBoxId(String boxId) {
        return super.findRollingAdBoxByBoxId(boxId);
    }

    public String getRollingAdBox(String boxId) {
        RollingAdBox rollingAdBox = findRollingAdBoxByBoxId(boxId);
        // 广告状态为可用
        if (rollingAdBox != null && rollingAdBox.getAdState() == 1) {
            Date currrentDate = new Date();
            Date adStartTime = rollingAdBox.getStartTime();
            Date adEndTime = rollingAdBox.getEndTime();
            // 广告时间在有效期内
            if (currrentDate.after(adStartTime)
                    && currrentDate.before(adEndTime)) {
                String adUrlsString = rollingAdBox.getBoxAdUrl();
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
