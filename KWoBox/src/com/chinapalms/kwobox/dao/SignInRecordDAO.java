package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.SignInRecord;

public interface SignInRecordDAO {

    public boolean addSignInRecord(SignInRecord signInRecord);

    public int findTotalSignInPoints(String phoneNumber);

}
