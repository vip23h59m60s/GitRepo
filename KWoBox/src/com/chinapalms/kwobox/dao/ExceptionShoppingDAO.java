package com.chinapalms.kwobox.dao;

import java.util.List;

import com.chinapalms.kwobox.javabean.ExceptionShopping;

public interface ExceptionShoppingDAO {

    public boolean addExceptionShopping(ExceptionShopping exceptionShopping);

    public boolean updateExceptionShoppingState(
            ExceptionShopping exceptionShopping);

    public List<ExceptionShopping> findExceptionShoppingsByState(int state);

}
