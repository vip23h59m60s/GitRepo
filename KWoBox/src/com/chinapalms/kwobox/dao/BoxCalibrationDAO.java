package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.BoxCalibration;

public interface BoxCalibrationDAO {

    public boolean addBoxCalibration(BoxCalibration boxCalibration);

    public boolean updateBoxCalibration(BoxCalibration boxCalibration);

    public BoxCalibration findBoxCalibrationByBoxId(String boxId);

}
