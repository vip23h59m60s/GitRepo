package com.chinapalms.kwobox.service;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.impl.WeightSensorStatusDAOImpl;
import com.chinapalms.kwobox.javabean.Boxes;
import com.chinapalms.kwobox.javabean.WeightSensorStatus;
import com.chinapalms.kwobox.utils.JavaMailUtil;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class WeightSensorStatusService extends WeightSensorStatusDAOImpl {

    Log log = LogFactory.getLog(WeightSensorStatusService.class);

    @Override
    public boolean addWeightSensorStatus(WeightSensorStatus weightSensorStatus) {
        return super.addWeightSensorStatus(weightSensorStatus);
    }

    /**
     * 保存工控机上报层架是否断开和恢复状态
     * 
     * @param boxId
     * @param state
     */
    public void saveWeightSensorStatus(String boxId, int state) {
        BoxesService boxesService = new BoxesService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        // 售货机不为空并且售货机状态为已完成商户部署
        if (boxes != null
                && boxes.getBoxState() == ResponseStatus.BOX_STATE_FINISHED_MERCHANT_DEPLOYMENT) {
            String boxName = boxes.getBoxName();
            try {
                // 收件人
                String receiver = "panan@zhigoumao.cn";
                // 抄送人
                String[] copyReceiversArray = { "chenchengshun@zhigoumao.cn" };
                if (state == ResponseStatus.WEIGHT_SENSOR_STATUS_ERROR) {
                    JavaMailUtil.sendEmailToDatabaseSender(receiver,
                            copyReceiversArray, "智购猫层架或传感器异常，请及时处理", boxName
                                    + "(boxId:" + boxId + ")"
                                    + " 层架或传感器异常，请及时处理");
                } else {
                    JavaMailUtil.sendEmailToDatabaseSender(receiver,
                            copyReceiversArray, "智购猫层架或传感器已从异常中恢复，请知悉", boxName
                                    + "(boxId:" + boxId + ")"
                                    + " 层架或传感器已从异常中恢复，请知悉");
                }
                // 同时保存当前层架上报状态
                WeightSensorStatusService weightSensorStatusService = new WeightSensorStatusService();
                WeightSensorStatus weightSensorStatus = new WeightSensorStatus();
                weightSensorStatus.setBoxId(boxId);
                weightSensorStatus.setState(state);
                weightSensorStatus.setReportTime(new Date());
                weightSensorStatusService
                        .addWeightSensorStatus(weightSensorStatus);
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        }
    }
}
