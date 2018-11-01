package com.chinapalms.kwobox.service;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.impl.CloseRecordDAOImpl;
import com.chinapalms.kwobox.javabean.CloseRecord;
import com.chinapalms.kwobox.javabean.CurrentUser;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class CloseRecordService extends CloseRecordDAOImpl {

    Log log = LogFactory.getLog(CloseRecordService.class);

    @Override
    public boolean addCloseRecord(CloseRecord closeRecord) {
        return super.addCloseRecord(closeRecord);
    }

    public boolean doCloseOpenRecord(String boxId) {
        CurrentUserService currentUserService = new CurrentUserService();
        CurrentUser currentUser = currentUserService
                .findCurrentUserByBoxId(boxId);
        if (currentUser != null) {
            CloseRecord closeRecord = new CloseRecord();
            closeRecord.setBoxId(boxId);
            // 如果是普通消费者，把CustomerWorkerId设置为数据库默认值0，否则，吧phoneNumber设置为数据库默认值“0”
            if (currentUser.getUserType().equals(
                    ResponseStatus.PERMISSION_NORMAL_USER)) {
                closeRecord.setCustomerWorkerId(0);
                closeRecord.setPhoneNumber(currentUser.getPhoneNumber());
            } else if (currentUser.getUserType().equals(
                    ResponseStatus.PERMISSION_MANAGER)) {
                closeRecord.setCustomerWorkerId(currentUser
                        .getCustomerWorkerId());
                closeRecord.setPhoneNumber("0");
            }
            closeRecord.setClosePicture(ResponseStatus.BOX_PICTURE_URL + "/"
                    + boxId + ".jpg");
            closeRecord.setCloseTime(new Date());
            if (addCloseRecord(closeRecord)) {
                log.info("CloseRecordService->doCloseOpenRecord success phoneNumber="
                        + currentUser.getPhoneNumber());
                return true;
            }
        }
        return false;
    }
}
