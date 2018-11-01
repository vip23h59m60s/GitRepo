package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.CustomRequestInfo;

public interface CustomRequestInfoDAO {

    public boolean addCustomRequestInfo(CustomRequestInfo customRequestInfo);

    public boolean updateCustomRequestInfo(CustomRequestInfo customRequestInfo);

    public CustomRequestInfo findCustomRequestInfoByBoxId(String boxId);

}
