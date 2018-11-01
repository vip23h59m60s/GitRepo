package com.chinapalms.kwobox.dao;

import java.util.List;

import com.chinapalms.kwobox.javabean.BoxShopes;

public interface BoxShopesDAO {

    public BoxShopes findBoxShopByShopId(int shopId);

    public List<BoxShopes> findBoxShopesByCustomerId(int customerId);

}
