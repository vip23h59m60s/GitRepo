package com.chinapalms.kwobox.service;

import com.chinapalms.kwobox.dao.impl.ICSDAOImpl;
import com.chinapalms.kwobox.javabean.ICS;

public class ICSService extends ICSDAOImpl {

    @Override
    public boolean addICS(ICS ics) {
        return super.addICS(ics);
    }

    @Override
    public ICS findICSByIMEINumber(String imeiNumber) {
        return super.findICSByIMEINumber(imeiNumber);
    }

    @Override
    public boolean updateICS(ICS ics) {
        return super.updateICS(ics);
    }

}
