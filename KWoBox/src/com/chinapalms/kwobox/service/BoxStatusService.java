package com.chinapalms.kwobox.service;

import java.util.Date;

import com.chinapalms.kwobox.dao.impl.BoxStatusDAOImpl;
import com.chinapalms.kwobox.javabean.BoxStatus;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class BoxStatusService extends BoxStatusDAOImpl {

    @Override
    public boolean updateBoxStatus(BoxStatus boxStatus) {
        return super.updateBoxStatus(boxStatus);
    }

    @Override
    public BoxStatus findBoxStatusByBoxId(String boxId) {
        return super.findBoxStatusByBoxId(boxId);
    }

    @Override
    public boolean addBoxStatus(BoxStatus boxStatus) {
        return super.addBoxStatus(boxStatus);
    }

    /**
     * 设置门开关状态，更新数据库
     * 
     * @param box
     * @param opened
     * @return
     * @throws Exception
     */
    public String setOpenDoorState(String boxId, boolean opened)
            throws Exception {

        BoxStatus boxStatus = findBoxStatusByBoxId(boxId);
        if (boxStatus != null) {
            boxStatus.setState(opened ? ResponseStatus.BOX_STATUS_BUSY
                    : ResponseStatus.BOX_STATUS_NORMAL);
            boxStatus.setReportTime(new Date());
            if (updateBoxStatus(boxStatus)) {
                return ResponseStatus.SUCCESS;
            } else {
                return ResponseStatus.FAIL;
            }
        } else {
            BoxStatus boxStatusAdd = new BoxStatus();
            boxStatusAdd.setBoxId(boxId);
            boxStatusAdd.setState(opened ? ResponseStatus.BOX_STATUS_BUSY
                    : ResponseStatus.BOX_STATUS_NORMAL);
            boxStatusAdd.setReportTime(new Date());
            if (addBoxStatus(boxStatusAdd)) {
                return ResponseStatus.SUCCESS;
            } else {
                return ResponseStatus.FAIL;
            }
        }
    }

    /**
     * 获取售货机状态
     * 
     * @param boxId
     * @return
     */
    public int doGetBoxStatus(String boxId) {
        // 售货机当前状态
        BoxStatus boxStatus = findBoxStatusByBoxId(boxId);
        int boxState = ResponseStatus.BOX_STATUS_NORMAL;
        if (boxStatus != null) {
            boxState = boxStatus.getState();
        }
        return boxState;
    }

}
