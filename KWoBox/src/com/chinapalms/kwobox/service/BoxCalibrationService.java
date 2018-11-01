package com.chinapalms.kwobox.service;

import com.chinapalms.kwobox.dao.impl.BoxCalibrationDAOImpl;
import com.chinapalms.kwobox.javabean.BoxCalibration;

public class BoxCalibrationService extends BoxCalibrationDAOImpl {

    @Override
    public boolean addBoxCalibration(BoxCalibration boxCalibration) {
        return super.addBoxCalibration(boxCalibration);
    }

    @Override
    public boolean updateBoxCalibration(BoxCalibration boxCalibration) {
        return super.updateBoxCalibration(boxCalibration);
    }

    @Override
    public BoxCalibration findBoxCalibrationByBoxId(String boxId) {
        return super.findBoxCalibrationByBoxId(boxId);
    }

}
