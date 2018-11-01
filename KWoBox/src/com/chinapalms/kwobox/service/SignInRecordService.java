package com.chinapalms.kwobox.service;

import com.chinapalms.kwobox.dao.impl.SignInRecordDAOImpl;
import com.chinapalms.kwobox.javabean.SignInRecord;

public class SignInRecordService extends SignInRecordDAOImpl {

    @Override
    public boolean addSignInRecord(SignInRecord signInRecord) {
        return super.addSignInRecord(signInRecord);
    }

    @Override
    public int findTotalSignInPoints(String phoneNumber) {
        return super.findTotalSignInPoints(phoneNumber);
    }

}
