package com.chinapalms.kwobox.service;

import java.text.SimpleDateFormat;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.chinapalms.kwobox.dao.impl.PersonalCreditRecordDAOImpl;
import com.chinapalms.kwobox.javabean.Boxes;
import com.chinapalms.kwobox.javabean.Order;
import com.chinapalms.kwobox.javabean.OrderDetails;
import com.chinapalms.kwobox.javabean.PersonalCreditRecord;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class PersonalCreditRecordService extends PersonalCreditRecordDAOImpl {

    @Override
    public boolean addPersonalCreditRecord(
            PersonalCreditRecord personalCreditRecord) {
        return super.addPersonalCreditRecord(personalCreditRecord);
    }

    @Override
    public List<PersonalCreditRecord> findPersonalCreditRecordsByPhoneNumber(
            String phoneNumber) {
        return super.findPersonalCreditRecordsByPhoneNumber(phoneNumber);
    }

    @Override
    public int findPersionalCreditRecordsCountByPhoneNumber(String phoneNumber) {
        return super.findPersionalCreditRecordsCountByPhoneNumber(phoneNumber);
    }

    @Override
    public List<PersonalCreditRecord> findCreditRecordsByPhoneNumberAndPageNumber(
            String phoneNumber, int pageNumber) {
        return super.findCreditRecordsByPhoneNumberAndPageNumber(phoneNumber,
                pageNumber);
    }

    // 分页查询逻辑
    public String doFindMyCreditRecords(String phoneNumber, int pageNumber) {
        int count = findPersionalCreditRecordsCountByPhoneNumber(phoneNumber);
        int totalPages = 0;
        // 计算总页数
        if (count % PersonalCreditRecord.PAGE_SIZE == 0) {
            // 对总页数赋值
            totalPages = count / PersonalCreditRecord.PAGE_SIZE;
        } else {
            // 对总页数赋值
            totalPages = count / PersonalCreditRecord.PAGE_SIZE + 1;
        }

        List<PersonalCreditRecord> personalCreditRecordsList = findCreditRecordsByPhoneNumberAndPageNumber(
                phoneNumber, pageNumber);
        if (pageNumber == 1 && personalCreditRecordsList.size() <= 0) {
            return ResponseStatus.NO_PERSONAL_CREDIT_RECORD;
        } else if (pageNumber <= totalPages
                && personalCreditRecordsList.size() > 0) {
            JSONObject personalCreditRecordJsonObject = new JSONObject();
            JSONObject personalCreditRecordBeanJsonObject = new JSONObject();
            JSONArray personalCreditRecordsJsonArray = new JSONArray();
            JSONObject personalCreditRecordsJsonObject = new JSONObject();
            for (int i = 0; i < personalCreditRecordsList.size(); i++) {
                PersonalCreditRecord personalCreditRecord = personalCreditRecordsList
                        .get(i);
                personalCreditRecordBeanJsonObject = JSONObject
                        .fromObject(personalCreditRecord);
                personalCreditRecordBeanJsonObject.put("changeTime",
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                .format(personalCreditRecord.getChangeTime()));
                personalCreditRecordJsonObject.put("CreditRecord",
                        personalCreditRecordBeanJsonObject);
                personalCreditRecordsJsonArray
                        .add(personalCreditRecordJsonObject);
            }
            personalCreditRecordsJsonObject.put("CreditRecords",
                    personalCreditRecordsJsonArray);
            return personalCreditRecordsJsonObject.toString();
        } else {
            return ResponseStatus.LAST_PAGE;
        }
    }

}
