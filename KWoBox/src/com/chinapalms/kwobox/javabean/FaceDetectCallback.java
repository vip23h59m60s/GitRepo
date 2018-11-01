package com.chinapalms.kwobox.javabean;

public class FaceDetectCallback {

    private int customerId;// 对接第三方的商户ID
    private String faceDetectCallbackUrl;// 对接第三方商户服务器的人脸识别Url

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getFaceDetectCallbackUrl() {
        return faceDetectCallbackUrl;
    }

    public void setFaceDetectCallbackUrl(String faceDetectCallbackUrl) {
        this.faceDetectCallbackUrl = faceDetectCallbackUrl;
    }

    @Override
    public String toString() {
        return "FaceDetectCallback [customerId=" + customerId
                + ", faceDetectCallbackUrl=" + faceDetectCallbackUrl + "]";
    }

}
