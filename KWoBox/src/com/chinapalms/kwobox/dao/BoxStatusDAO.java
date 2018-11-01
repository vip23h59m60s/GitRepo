package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.BoxStatus;

public interface BoxStatusDAO {

    public boolean addBoxStatus(BoxStatus boxStatus);

    public boolean updateBoxStatus(BoxStatus boxStatus);

    public BoxStatus findBoxStatusByBoxId(String boxId);

}
