package com.chinapalms.kwobox.service;

import java.text.SimpleDateFormat;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.chinapalms.kwobox.dao.impl.PointsRecordDAOImpl;
import com.chinapalms.kwobox.javabean.PointsRecord;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class PointsRecordService extends PointsRecordDAOImpl {

    @Override
    public boolean addPointsRecordCreditRecord(PointsRecord pointsRecord) {
        return super.addPointsRecordCreditRecord(pointsRecord);
    }

    @Override
    public List<PointsRecord> findPointsRecordsByPhoneNumber(String phoneNumber) {
        return super.findPointsRecordsByPhoneNumber(phoneNumber);
    }

    @Override
    public int findPointsRecordsCountByPhoneNumber(String phoneNumber) {
        return super.findPointsRecordsCountByPhoneNumber(phoneNumber);
    }

    @Override
    public List<PointsRecord> findPointsRecordsByPhoneNumberAndPageNumber(
            String phoneNumber, int pageNumber) {
        return super.findPointsRecordsByPhoneNumberAndPageNumber(phoneNumber,
                pageNumber);
    }

    // 分页查询逻辑
    public String doFindMyCreditRecords(String phoneNumber, int pageNumber) {
        int count = findPointsRecordsCountByPhoneNumber(phoneNumber);
        int totalPages = 0;
        // 计算总页数
        if (count % PointsRecord.PAGE_SIZE == 0) {
            // 对总页数赋值
            totalPages = count / PointsRecord.PAGE_SIZE;
        } else {
            // 对总页数赋值
            totalPages = count / PointsRecord.PAGE_SIZE + 1;
        }

        List<PointsRecord> pointsRecordsList = findPointsRecordsByPhoneNumberAndPageNumber(
                phoneNumber, pageNumber);
        if (pageNumber == 1 && pointsRecordsList.size() <= 0) {
            return ResponseStatus.NO_POINTS_RECORD;
        } else if (pageNumber <= totalPages && pointsRecordsList.size() > 0) {
            JSONObject pointsRecordJsonObject = new JSONObject();
            JSONObject pointsRecordBeanJsonObject = new JSONObject();
            JSONArray personalCreditRecordsJsonArray = new JSONArray();
            JSONObject pointsRecordsJsonObject = new JSONObject();
            for (int i = 0; i < pointsRecordsList.size(); i++) {
                PointsRecord pointsRecord = pointsRecordsList.get(i);
                pointsRecordBeanJsonObject = JSONObject
                        .fromObject(pointsRecord);
                pointsRecordBeanJsonObject.put("changeTime",
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                .format(pointsRecord.getChangeTime()));
                pointsRecordJsonObject.put("PointsRecord",
                        pointsRecordBeanJsonObject);
                personalCreditRecordsJsonArray.add(pointsRecordJsonObject);
            }
            pointsRecordsJsonObject.put("PointsRecords",
                    personalCreditRecordsJsonArray);
            return pointsRecordsJsonObject.toString();
        } else {
            return ResponseStatus.LAST_PAGE;
        }
    }

}
