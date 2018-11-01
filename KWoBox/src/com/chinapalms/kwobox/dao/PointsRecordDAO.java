package com.chinapalms.kwobox.dao;

import java.util.List;

import com.chinapalms.kwobox.javabean.PointsRecord;

public interface PointsRecordDAO {

    public boolean addPointsRecordCreditRecord(PointsRecord pointsRecord);

    public List<PointsRecord> findPointsRecordsByPhoneNumber(String phoneNumber);

    public int findPointsRecordsCountByPhoneNumber(String phoneNumber);

    public List<PointsRecord> findPointsRecordsByPhoneNumberAndPageNumber(
            String phoneNumber, int pageNumber);

}
