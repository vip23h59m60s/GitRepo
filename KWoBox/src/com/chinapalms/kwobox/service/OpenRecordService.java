package com.chinapalms.kwobox.service;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.impl.OpenRecordDAOImpl;
import com.chinapalms.kwobox.javabean.CurrentUser;
import com.chinapalms.kwobox.javabean.OpenRecord;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class OpenRecordService extends OpenRecordDAOImpl {

    Log log = LogFactory.getLog(OpenRecordService.class);

    @Override
    public boolean addOpenRecord(OpenRecord openRecord) {
        return super.addOpenRecord(openRecord);
    }

    public boolean doAddOpenRecord(String boxId) {
        CurrentUserService currentUserService = new CurrentUserService();
        CurrentUser currentUser = currentUserService
                .findCurrentUserByBoxId(boxId);
        if (currentUser != null) {
            OpenRecord openRecord = new OpenRecord();
            openRecord.setBoxId(boxId);
            // 如果是普通消费者，把CustomerWorkerId设置为数据库默认值0，否则，吧phoneNumber设置为数据库默认值“0”
            if (currentUser.getUserType().equals(
                    ResponseStatus.PERMISSION_NORMAL_USER)) {
                openRecord.setCustomerWorkerId(0);
                openRecord.setPhoneNumber(currentUser.getPhoneNumber());
            } else if (currentUser.getUserType().equals(
                    ResponseStatus.PERMISSION_MANAGER)) {
                openRecord.setCustomerWorkerId(currentUser
                        .getCustomerWorkerId());
                openRecord.setPhoneNumber("0");
            }

            openRecord.setOpenWay(currentUser.getCustomType());
            openRecord.setOpenPicture(ResponseStatus.BOX_PICTURE_URL + "/"
                    + boxId + ".jpg");
            openRecord.setOpenTime(new Date());
            if (addOpenRecord(openRecord)) {
                log.info("OpenRecordService->doAddOpenRecord success phoneNumber="
                        + currentUser.getPhoneNumber());
                return true;
            }
        }
        return false;
    }

}
