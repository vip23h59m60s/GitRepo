package com.chinapalms.kwobox.service;

import java.util.Date;

import net.sf.json.JSONArray;

import com.chinapalms.kwobox.dao.impl.RollingAdPlatformImpl;
import com.chinapalms.kwobox.javabean.RollingAdBox;
import com.chinapalms.kwobox.javabean.RollingAdPlatform;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class RollingAdPlatformService extends RollingAdPlatformImpl {

    @Override
    public RollingAdPlatform findRollingAdPlatform() {
        return super.findRollingAdPlatform();
    }

    public String getRollingAdPlatform() {
        RollingAdPlatform rollingAdPlatform = findRollingAdPlatform();
        // 广告状态为可用
        if (rollingAdPlatform != null && rollingAdPlatform.getAdState() == 1) {
            Date currrentDate = new Date();
            Date adStartTime = rollingAdPlatform.getStartTime();
            Date adEndTime = rollingAdPlatform.getEndTime();
            // 广告时间在有效期内
            if (currrentDate.after(adStartTime)
                    && currrentDate.before(adEndTime)) {
                String adUrlsString = rollingAdPlatform.getPlatformAdUrl();
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
