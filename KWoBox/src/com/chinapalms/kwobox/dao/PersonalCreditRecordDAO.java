package com.chinapalms.kwobox.dao;

import java.util.List;

import com.chinapalms.kwobox.javabean.PersonalCreditRecord;

public interface PersonalCreditRecordDAO {

    public boolean addPersonalCreditRecord(
            PersonalCreditRecord personalCreditRecord);

    public List<PersonalCreditRecord> findPersonalCreditRecordsByPhoneNumber(
            String phoneNumber);

    public int findPersionalCreditRecordsCountByPhoneNumber(String phoneNumber);

    public List<PersonalCreditRecord> findCreditRecordsByPhoneNumberAndPageNumber(
            String phoneNumber, int pageNumber);

}
