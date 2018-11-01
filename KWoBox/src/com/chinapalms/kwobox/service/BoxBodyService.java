package com.chinapalms.kwobox.service;

import com.chinapalms.kwobox.dao.impl.BoxBodyDAOImpl;
import com.chinapalms.kwobox.javabean.BoxBody;

public class BoxBodyService extends BoxBodyDAOImpl {

    @Override
    public BoxBody findBoxBodyByBoxBodyId(int boxBodyId) {
        return super.findBoxBodyByBoxBodyId(boxBodyId);
    }

    @Override
    public BoxBody findBoxBodyByBoxBodyModel(String boxBodyModel) {
        return super.findBoxBodyByBoxBodyModel(boxBodyModel);
    }

}
