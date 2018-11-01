package com.chinapalms.kwobox.service;

import com.chinapalms.kwobox.dao.impl.BoxStockSyncStatusDAOImpl;
import com.chinapalms.kwobox.javabean.BoxStockSyncStatus;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class BoxStockSyncStatusService extends BoxStockSyncStatusDAOImpl {

    @Override
    public boolean addBoxStockSyncStatus(BoxStockSyncStatus boxStockSyncStatus) {
        return super.addBoxStockSyncStatus(boxStockSyncStatus);
    }

    @Override
    public boolean updateBoxStockSyncStatus(
            BoxStockSyncStatus boxStockSyncStatus) {
        return super.updateBoxStockSyncStatus(boxStockSyncStatus);
    }

    @Override
    public BoxStockSyncStatus findBoxStockSyncStatusByBoxId(String boxId) {
        return super.findBoxStockSyncStatusByBoxId(boxId);
    }

    public String doSyncBoxStockInfoFinished(String boxId, String state) {
        BoxStockSyncStatus boxStockSyncStatus = new BoxStockSyncStatus();
        boxStockSyncStatus.setBoxId(boxId);
        boxStockSyncStatus.setState(Integer.valueOf(state));
        BoxStockSyncStatus boxStockSyncStatusQuery = findBoxStockSyncStatusByBoxId(boxId);
        if (boxStockSyncStatusQuery != null) {
            updateBoxStockSyncStatus(boxStockSyncStatus);
        } else {
            addBoxStockSyncStatus(boxStockSyncStatus);
        }
        return ResponseStatus.SUCCESS;
    }

    public String doGetBoxSyncStockStatus(String boxId) {
        BoxStockSyncStatus boxStockSyncStatus = findBoxStockSyncStatusByBoxId(boxId);
        if (boxStockSyncStatus != null) {
            if (boxStockSyncStatus.getState() == ResponseStatus.SYNC_BOX_STOCK_SUCCESS) {
                return ResponseStatus.SUCCESS;
            } else {
                return ResponseStatus.FAIL;
            }
        }
        return ResponseStatus.FAIL;
    }

}
