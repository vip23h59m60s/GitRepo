package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.SerializeBoxBody;

public interface SerializeBoxBodyDAO {

    public boolean addSerializeBoxBody(SerializeBoxBody serializeBoxBody);

    public SerializeBoxBody findSerializeBoxBodyByBoxBodySnNumber(
            String boxBodySnNumber);

    public SerializeBoxBody findSerializeBoxBodyBySerializeBoxBodyId(
            int serializeBoxBodyId);

    public boolean updateSerializeBoxBody(SerializeBoxBody serializeBoxBody);

}
