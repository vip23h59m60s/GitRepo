package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.CurrentUser;

public interface CurrentUserDAO {

    public boolean addCurrentUser(CurrentUser currentUser);

    public boolean deleteCurrentUser(CurrentUser currentUser);

    public CurrentUser findCurrentUserByBoxId(String boxId);

    public boolean updateCurrentUser(CurrentUser currentUser);

}
