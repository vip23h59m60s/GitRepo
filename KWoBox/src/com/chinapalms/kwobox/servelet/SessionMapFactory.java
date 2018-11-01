package com.chinapalms.kwobox.servelet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @Description: 单例工具类，保存所有WebSocket客户端连接
 * @author whl
 * @date 2014-9-29 上午10:09:15
 * 
 */
public class SessionMapFactory {

    Log log = LogFactory.getLog(SessionMapFactory.class);

    private static SessionMapFactory sessionMapFactory = null;
    private static Map<String, Session> map = null;

    // 构造私有化 单例
    private SessionMapFactory() {
    }

    public static SessionMapFactory getInstance() {
        if (sessionMapFactory == null) {
            sessionMapFactory = new SessionMapFactory();
        }
        return sessionMapFactory;
    }

    /**
     * @Description: 获取唯一实例
     * @author whl
     * @date 2014-9-29 下午1:29:33
     */
    public static Map<String, Session> getSessionMap() {
        if (map == null) {
            map = new HashMap<String, Session>();
        }
        return map;
    }

    /**
     * @Description: 保存session会话
     * @author whl
     * @date 2014-9-29 下午1:31:05
     */
    public void addSession(String key, Session session) {
        getSessionMap().put(key, session);
    }

    public void removeSession(String key) {
        getSessionMap().remove(key);
    }

    /**
     * @Description: 根据key查找缓存的session
     * @author whl
     * @date 2014-9-29 下午1:31:55
     */
    public Session getSession(String key) {
        return getSessionMap().get(key);
    }

    /**
     * @Description: 获取当前需要处理的Session
     * @param customCategory
     * @param customId
     * @return
     */
    public Session getCurrentSession(String customCategory, String customId) {
        String mapKey = customCategory + "_" + customId;
        return getSessionMap().get(mapKey);
    }

    /**
     * 通过WebSocket长连接向微信小程序或Android客户端发送操作消息
     * 
     * @param session
     * @param message
     * @throws IOException
     */
    public void sendMessage(Session session, String message) throws IOException {
        if (session != null) {
            session.getBasicRemote().sendText(message);
        }
    }

}