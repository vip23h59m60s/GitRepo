package com.chinapalms.kwobox.utils;

public class Util {
    /**
     * 判断是linux系统还是其他系统 如果是Linux系统，返回true，否则返回false
     */
    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().indexOf("linux") >= 0;
    }

}
