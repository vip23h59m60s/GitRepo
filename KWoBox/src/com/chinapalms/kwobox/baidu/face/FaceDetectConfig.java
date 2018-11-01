package com.chinapalms.kwobox.baidu.face;

public class FaceDetectConfig {

    public static String APP_ID = "10336312";

    public static String API_KEY = "ydGyl11WExlVm5P0NZWpag17";

    public static String SECRET_KEY = "gB6mjfC553GHEtuLd7mBhyicVhUwRGkG";

    // 人脸识别匹配最低分值
    public static Double FACE_MATCH_BASELINE_SCORES = 70d;
    // 活体检测最低值
    public static Double FACE_MATCH_FACELIVENESS_BASELINE_SCORES = 0.393241d;

    // 人脸识别模式 门店免密模式或者柜子非免密模式
    public static final String FACE_TYPE = "faceType";
    public static final String FACE_TYPE_NORMAL = "faceTypeNormal";
    public static final String FACE_TYPE_SHOP = "faceTypeShop";

}
