package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.ICS;

public interface ICSDAO {

    public boolean addICS(ICS ics);

    public ICS findICSByIMEINumber(String imeiNumber);

    public boolean updateICS(ICS ics);

}
