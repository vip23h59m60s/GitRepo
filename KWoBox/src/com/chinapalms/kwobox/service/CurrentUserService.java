package com.chinapalms.kwobox.service;

import java.io.IOException;

import javax.websocket.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.impl.CurrentUserDAOImpl;
import com.chinapalms.kwobox.javabean.CurrentUser;
import com.chinapalms.kwobox.javabean.User;
import com.chinapalms.kwobox.pay.wxpay.papay.WxPapayMD5;
import com.chinapalms.kwobox.servelet.SessionMapFactory;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class CurrentUserService extends CurrentUserDAOImpl {

    Log log = LogFactory.getLog(CurrentUserService.class);

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

    public String doRegisterCurrentUser(CurrentUser currentUser) {
        try {
            // 如果是普通消费者，把CustomerWorkerId设置为数据库默认值0，否则，吧phoneNumber设置为数据库默认值“0”
            if (currentUser.getUserType().equals(
                    ResponseStatus.PERMISSION_NORMAL_USER)) {
                currentUser.setCustomerWorkerId(0);
                UserService userService = new UserService();
                User user = userService.queryUserByPhoneNumber(currentUser
                        .getPhoneNumber());
                if (user != null) {
                    currentUser.setVipLevel(user.getVipLevel());
                }
            } else if (currentUser.getUserType().equals(
                    ResponseStatus.PERMISSION_MANAGER)) {
                currentUser.setPhoneNumber("0");
                currentUser.setVipLevel(ResponseStatus.VIP_LEVEL_NORMAL);
            }
            CurrentUser currentUserQuery = findCurrentUserByBoxId(currentUser
                    .getBoxId());
            // 如果当前售货柜用户不为空，则关闭当前长连接，重连创建新的长连接，防止将购物过程相关信息推送给之前用户
            if (currentUserQuery != null) {
                Session currentSession = SessionMapFactory.getInstance()
                        .getCurrentSession(currentUser.getCustomType(),
                                currentUser.getBoxId());
                if (currentSession != null) {
                    currentSession.close();
                }
            }
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

    public boolean deleteCurrentUserByBoxId(String boxId) {
        CurrentUser currentUser = new CurrentUser();
        currentUser.setBoxId(boxId);
        if (deleteCurrentUser(currentUser)) {
            return true;
        } else {
            return false;
        }
    }
}
