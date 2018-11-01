package com.chinapalms.kwobox.service;

import com.chinapalms.kwobox.dao.impl.CustomRequestInfoDAOImpl;
import com.chinapalms.kwobox.javabean.CustomRequestInfo;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class CustomRequestInfoService extends CustomRequestInfoDAOImpl {

    @Override
    public boolean addCustomRequestInfo(CustomRequestInfo customRequestInfo) {
        return super.addCustomRequestInfo(customRequestInfo);
    }

    @Override
    public boolean updateCustomRequestInfo(CustomRequestInfo customRequestInfo) {
        return super.updateCustomRequestInfo(customRequestInfo);
    }

    @Override
    public CustomRequestInfo findCustomRequestInfoByBoxId(String boxId) {
        return super.findCustomRequestInfoByBoxId(boxId);
    }

    public String registerCustomRequestInfo(CustomRequestInfo customRequestInfo) {
        CustomRequestInfo customRequestInfoQuery = findCustomRequestInfoByBoxId(customRequestInfo
                .getBoxId());
        if (customRequestInfoQuery != null) {
            if (updateCustomRequestInfo(customRequestInfo)) {
                return ResponseStatus.SUCCESS;
            } else {
                return ResponseStatus.FAIL;
            }
        } else {
            if (addCustomRequestInfo(customRequestInfo)) {
                return ResponseStatus.SUCCESS;
            } else {
                return ResponseStatus.FAIL;
            }
        }
    }

}
