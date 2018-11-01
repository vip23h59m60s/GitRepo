package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.BoxBody;

public interface BoxBodyDAO {

    public BoxBody findBoxBodyByBoxBodyId(int boxBodyId);

    public BoxBody findBoxBodyByBoxBodyModel(String boxBodyModel);

}
