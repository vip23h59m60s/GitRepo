package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.User;

public interface UserDAO {

    public boolean addUser(User user);

    public boolean deleteUser(User user);

    public User queryUserByPhoneNumber(String phoneNumber);

    public boolean updateConstractIdByPhoneNumberAndCustomType(
            String phoneNumber, String customType, String constractId);

    public boolean clearContractId(String contractId, String customType);

    public boolean updateFaceFuntion(User user);

    public boolean updatePersonalCreditByPhoneNumber(String phoneNumber,
            int creditChangeValue);

    public boolean updateUserPointsByPhoneNumber(String phoneNumber,
            int pointsChangeValue);

    public boolean updateUserUnReadOrderNumber(String phoneNumber,
            int changeValue);
    
    public boolean updateOpendIdByPhoneNumber(String phoneNumber, String openId);

}
