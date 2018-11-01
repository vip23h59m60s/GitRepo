package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.FaceDetectCallback;

public interface FaceDetectCallbackDAO {

    public FaceDetectCallback findFaceDetectCallbackByCustomerId(int customerId);

}
