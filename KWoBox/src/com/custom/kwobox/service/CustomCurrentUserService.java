package com.custom.kwobox.service;

import java.io.IOException;

import javax.websocket.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.impl.CurrentUserDAOImpl;
import com.chinapalms.kwobox.javabean.CurrentUser;
import com.chinapalms.kwobox.javabean.User;
import com.chinapalms.kwobox.pay.wxpay.papay.WxPapayMD5;
import com.chinapalms.kwobox.servelet.SessionMapFactory;
import com.chinapalms.kwobox.service.UserService;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class CustomCurrentUserService extends CurrentUserDAOImpl {

    Log log = LogFactory.getLog(CustomCurrentUserService.class);

    @Override
    public boolean addCurrentUser(CurrentUser currentUser) {
        return super.addCurrentUser(currentUser);
    }

    @Override
    public boolean deleteCurrentUser(CurrentUser currentUser) {
        return super.deleteCurrentUser(currentUser);
    }

    @Override
    public CurrentUser findCurrentUserByBoxId(String boxId) {
        return super.findCurrentUserByBoxId(boxId);
    }

    @Override
    public boolean updateCurrentUser(CurrentUser currentUser) {
        return super.updateCurrentUser(currentUser);
    }

    /**
     * 注册当前使用售货机用户
     * 
     * @param currentUser
     * @return
     */
    public String registerCurrentUser(CurrentUser currentUser) {
        try {
            CurrentUser currentUserQuery = findCurrentUserByBoxId(currentUser
                    .getBoxId());
            // 如果当前数据库中currentUser已存在更新，否则增加
            if (currentUserQuery != null) {
                if (updateCurrentUser(currentUser)) {
                    return ResponseStatus.SUCCESS;
                } else {
                    return ResponseStatus.FAIL;
                }
            } else {
                if (addCurrentUser(currentUser)) {
                    return ResponseStatus.SUCCESS;
                } else {
                    return ResponseStatus.FAIL;
                }
            }
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return ResponseStatus.FAIL;
    }

}
