package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.IdentifyCode;

public interface IdentifyCodeDAO {

    public boolean addIdentifyCode(IdentifyCode idendifyCode);

    public boolean deleteIdentifyCodeByPhoneNumber(String phoneNumber);

    public boolean updateIdentifyCode(IdentifyCode idendifyCode);

    public IdentifyCode findIdentifyCodeByPhoneNumber(String phoneNumber);

    public IdentifyCode findIdentifyCodeByPhoneNumberAndIdentifyCode(
            String phoneNumber, String identifyCode);

}
