package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.BoxStockSyncStatus;

public interface BoxStockSyncStatusDAO {

    public BoxStockSyncStatus findBoxStockSyncStatusByBoxId(String boxId);

    public boolean addBoxStockSyncStatus(BoxStockSyncStatus boxStockSyncStatus);

    public boolean updateBoxStockSyncStatus(
            BoxStockSyncStatus boxStockSyncStatus);

}
