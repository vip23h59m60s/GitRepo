package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.DynamicPassword;

public interface DynamicPasswordDAO {

    public boolean addDynamicPassword(DynamicPassword dynamicPassword);

    public boolean updateDynamicPassword(DynamicPassword dynamicPassword);

    public DynamicPassword findDynamicPasswordByPhoneNumber(String phoneNumber);

    public DynamicPassword findDynamicPasswordByDynamicPassword(
            String dynamicPassword);

    public boolean deleteDynamicPasswordByPhoneNumber(String phoneNumber);

}
