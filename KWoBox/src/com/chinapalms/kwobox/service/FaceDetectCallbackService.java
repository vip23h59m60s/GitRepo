package com.chinapalms.kwobox.service;

import com.chinapalms.kwobox.dao.impl.FaceDetectCallbackDAOImpl;
import com.chinapalms.kwobox.javabean.FaceDetectCallback;

public class FaceDetectCallbackService extends FaceDetectCallbackDAOImpl {

    @Override
    public FaceDetectCallback findFaceDetectCallbackByCustomerId(int customerId) {
        return super.findFaceDetectCallbackByCustomerId(customerId);
    }

}
