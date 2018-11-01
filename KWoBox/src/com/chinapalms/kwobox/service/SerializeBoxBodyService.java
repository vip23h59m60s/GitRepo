package com.chinapalms.kwobox.service;

import com.chinapalms.kwobox.dao.impl.SerializeBoxBodyDAOImpl;
import com.chinapalms.kwobox.javabean.SerializeBoxBody;

public class SerializeBoxBodyService extends SerializeBoxBodyDAOImpl {

    @Override
    public boolean addSerializeBoxBody(SerializeBoxBody serializeBoxBody) {
        return super.addSerializeBoxBody(serializeBoxBody);
    }

    @Override
    public SerializeBoxBody findSerializeBoxBodyByBoxBodySnNumber(
            String boxBodySnNumber) {
        return super.findSerializeBoxBodyByBoxBodySnNumber(boxBodySnNumber);
    }

    @Override
    public boolean updateSerializeBoxBody(SerializeBoxBody serializeBoxBody) {
        return super.updateSerializeBoxBody(serializeBoxBody);
    }

    @Override
    public SerializeBoxBody findSerializeBoxBodyBySerializeBoxBodyId(
            int serializeBoxBodyId) {
        return super
                .findSerializeBoxBodyBySerializeBoxBodyId(serializeBoxBodyId);
    }

}
