package com.chinapalms.kwobox.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.processors.JsonBeanProcessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.util.logging.resources.logging;

import com.chinapalms.kwobox.dao.impl.BoxStructureDAOImpl;
import com.chinapalms.kwobox.javabean.BoxStructure;

public class BoxStructureService extends BoxStructureDAOImpl {

    Log log = LogFactory.getLog(BoxStructureService.class);

    @Override
    public BoxStructure findBoxStructureByStructureId(int structureId) {
        return super.findBoxStructureByStructureId(structureId);
    }

    // 获取当前售货机的实际sensor列表
    public List<Integer> getBoxAllSensorList(int structureId, int totalSensor) {
        List<Integer> allSensorList = new ArrayList<Integer>();
        List<Integer> actualSensorList = new ArrayList<Integer>();
        BoxStructure boxStructure = findBoxStructureByStructureId(structureId);
        int sensor1 = boxStructure.getSensor1();
        int sensor2 = boxStructure.getSensor2();
        int sensor3 = boxStructure.getSensor3();
        int sensor4 = boxStructure.getSensor4();
        int sensor5 = boxStructure.getSensor5();
        int sensor6 = boxStructure.getSensor6();
        int sensor7 = boxStructure.getSensor7();
        int sensor8 = boxStructure.getSensor8();
        int sensor9 = boxStructure.getSensor9();
        int sensor10 = boxStructure.getSensor10();
        int sensor11 = boxStructure.getSensor11();
        int sensor12 = boxStructure.getSensor12();
        int sensor13 = boxStructure.getSensor13();
        int sensor14 = boxStructure.getSensor14();
        int sensor15 = boxStructure.getSensor15();
        int sensor16 = boxStructure.getSensor16();
        int sensor17 = boxStructure.getSensor17();
        int sensor18 = boxStructure.getSensor18();
        int sensor19 = boxStructure.getSensor19();
        int sensor20 = boxStructure.getSensor20();
        int sensor21 = boxStructure.getSensor21();
        int sensor22 = boxStructure.getSensor22();
        int sensor23 = boxStructure.getSensor23();
        int sensor24 = boxStructure.getSensor24();
        int sensor25 = boxStructure.getSensor25();
        int sensor26 = boxStructure.getSensor26();
        int sensor27 = boxStructure.getSensor27();
        int sensor28 = boxStructure.getSensor28();
        int sensor29 = boxStructure.getSensor29();
        int sensor30 = boxStructure.getSensor30();
        int sensor31 = boxStructure.getSensor31();
        int sensor32 = boxStructure.getSensor32();
        int sensor33 = boxStructure.getSensor33();
        int sensor34 = boxStructure.getSensor34();
        int sensor35 = boxStructure.getSensor35();
        int sensor36 = boxStructure.getSensor36();
        int sensor37 = boxStructure.getSensor37();
        int sensor38 = boxStructure.getSensor38();
        int sensor39 = boxStructure.getSensor39();
        int sensor40 = boxStructure.getSensor40();
        int sensor41 = boxStructure.getSensor41();
        int sensor42 = boxStructure.getSensor42();
        int sensor43 = boxStructure.getSensor43();
        int sensor44 = boxStructure.getSensor44();
        int sensor45 = boxStructure.getSensor45();
        int sensor46 = boxStructure.getSensor46();
        int sensor47 = boxStructure.getSensor47();
        int sensor48 = boxStructure.getSensor48();
        int sensor49 = boxStructure.getSensor49();
        int sensor50 = boxStructure.getSensor50();
        int sensor51 = boxStructure.getSensor51();
        int sensor52 = boxStructure.getSensor52();
        int sensor53 = boxStructure.getSensor53();
        int sensor54 = boxStructure.getSensor54();
        int sensor55 = boxStructure.getSensor55();
        int sensor56 = boxStructure.getSensor56();
        int sensor57 = boxStructure.getSensor57();
        int sensor58 = boxStructure.getSensor58();
        int sensor59 = boxStructure.getSensor59();
        int sensor60 = boxStructure.getSensor60();
        int sensor61 = boxStructure.getSensor61();
        int sensor62 = boxStructure.getSensor62();
        int sensor63 = boxStructure.getSensor63();
        int sensor64 = boxStructure.getSensor64();
        int sensor65 = boxStructure.getSensor65();
        allSensorList.add(sensor1);
        allSensorList.add(sensor2);
        allSensorList.add(sensor3);
        allSensorList.add(sensor4);
        allSensorList.add(sensor5);
        allSensorList.add(sensor6);
        allSensorList.add(sensor7);
        allSensorList.add(sensor8);
        allSensorList.add(sensor9);
        allSensorList.add(sensor10);
        allSensorList.add(sensor11);
        allSensorList.add(sensor12);
        allSensorList.add(sensor13);
        allSensorList.add(sensor14);
        allSensorList.add(sensor15);
        allSensorList.add(sensor16);
        allSensorList.add(sensor17);
        allSensorList.add(sensor18);
        allSensorList.add(sensor19);
        allSensorList.add(sensor20);
        allSensorList.add(sensor21);
        allSensorList.add(sensor22);
        allSensorList.add(sensor23);
        allSensorList.add(sensor24);
        allSensorList.add(sensor25);
        allSensorList.add(sensor26);
        allSensorList.add(sensor27);
        allSensorList.add(sensor28);
        allSensorList.add(sensor29);
        allSensorList.add(sensor30);
        allSensorList.add(sensor31);
        allSensorList.add(sensor32);
        allSensorList.add(sensor33);
        allSensorList.add(sensor34);
        allSensorList.add(sensor35);
        allSensorList.add(sensor36);
        allSensorList.add(sensor37);
        allSensorList.add(sensor38);
        allSensorList.add(sensor39);
        allSensorList.add(sensor40);
        allSensorList.add(sensor41);
        allSensorList.add(sensor42);
        allSensorList.add(sensor43);
        allSensorList.add(sensor44);
        allSensorList.add(sensor45);
        allSensorList.add(sensor46);
        allSensorList.add(sensor47);
        allSensorList.add(sensor48);
        allSensorList.add(sensor49);
        allSensorList.add(sensor50);
        allSensorList.add(sensor51);
        allSensorList.add(sensor52);
        allSensorList.add(sensor53);
        allSensorList.add(sensor54);
        allSensorList.add(sensor55);
        allSensorList.add(sensor56);
        allSensorList.add(sensor57);
        allSensorList.add(sensor58);
        allSensorList.add(sensor59);
        allSensorList.add(sensor60);
        allSensorList.add(sensor61);
        allSensorList.add(sensor62);
        allSensorList.add(sensor63);
        allSensorList.add(sensor64);
        allSensorList.add(sensor65);

        for (int i = 0; i < totalSensor; i++) {
            actualSensorList.add(allSensorList.get(i));
        }
        return actualSensorList;
    }

    public String getSensorAndCardgoRoadStructure(List<Integer> sensorList) {
        List<Integer> testList = new ArrayList<Integer>();
        int cardgoRoadId = 1;
        for (int i = 0; i < sensorList.size(); i++) {
            if (i < sensorList.size() - 1) {
                // 前一个和下一个比较
                // 取出sensor结构数据的第一位（层号）和最后一位（货到号）
                String sensor = String.valueOf(sensorList.get(i));
                String sensorNext = String.valueOf(sensorList.get(i + 1));
                int sensorfirst = Integer.valueOf(sensor.substring(0, 1));
                int sensorlast = Integer.valueOf(sensor.substring(sensor
                        .length() - 1));
                int sensorNextfirst = Integer.valueOf(sensorNext
                        .substring(0, 1));
                int sensorNextlast = Integer.valueOf(sensorNext
                        .substring(sensor.length() - 1));
                testList.add(cardgoRoadId);
                if (!(sensorfirst == sensorNextfirst && sensorlast == sensorNextlast)) {
                    if (i != sensorList.size() - 2) {
                        cardgoRoadId++;
                    }
                }
            } else {
                // 最后一个和前一个比较
                // 取出sensor结构数据的第一位（层号）和最后一位（货到号）
                String sensor = String.valueOf(sensorList.get(i));
                String sensorPre = String.valueOf(sensorList.get(i - 1));
                int sensorfirst = Integer.valueOf(sensor.substring(0, 1));
                int sensorlast = Integer.valueOf(sensor.substring(sensor
                        .length() - 1));
                int sensorPrefirst = Integer.valueOf(sensorPre.substring(0, 1));
                int sensorPrelast = Integer.valueOf(sensorPre.substring(sensor
                        .length() - 1));
                if (!(sensorfirst == sensorPrefirst && sensorlast == sensorPrelast)) {
                    cardgoRoadId++;
                }
                testList.add(cardgoRoadId);
            }
        }
        return testList.toString();
    }

    /**
     * 获取售货机层数以及每层的货到数
     */
    public String getCardgoRoadAndLayerStructureInfo(int structureId) {
        BoxStructure boxStructure = findBoxStructureByStructureId(structureId);
        int totalLayer = boxStructure.getTotalLayer();
        int totalCardgoRoad = boxStructure.getTotalCardgoroad();
        int totalSensor = boxStructure.getTotalSensor();

        // 获取当前售货机的实际sensor列表
        List<Integer> boxActualSensorList = getBoxAllSensorList(structureId,
                totalSensor);

        // 通过layer和货道标记统计出每层货道数
        JSONArray layerCardgoRoadNumberJsonArray = new JSONArray();
        for (int layer = 1; layer <= totalLayer; layer++) {
            for (int i = 0; i < boxActualSensorList.size(); i++) {
                String boxSensor = String.valueOf(boxActualSensorList.get(i));
                // 分别取层号，找出每层对应的货道数并保存
                if (boxSensor.substring(0, 1).equals(String.valueOf(layer))) {
                    JSONObject layerCardgoRoadNumberJsonObject = new JSONObject();
                    layerCardgoRoadNumberJsonObject.put("number",
                            Integer.valueOf(boxSensor.substring(2, 3)));
                    // 计算出每层for循环开始标志
                    int layerForLoopStart = 0;
                    for (int j = 0; j < layerCardgoRoadNumberJsonArray.size(); j++) {
                        layerForLoopStart = layerForLoopStart
                                + Integer
                                        .valueOf(layerCardgoRoadNumberJsonArray
                                                .getJSONObject(j).getString(
                                                        "number"));
                    }
                    layerCardgoRoadNumberJsonObject.put("layerForLoopStart",
                            layerForLoopStart);
                    layerCardgoRoadNumberJsonArray
                            .add(layerCardgoRoadNumberJsonObject);
                    break;
                }
            }
        }

        // 记录总层数+总货道数+每层货道数
        JSONObject layerAndCardgoRoadJsonObject = new JSONObject();
        layerAndCardgoRoadJsonObject.put("totalLayer", totalLayer);
        layerAndCardgoRoadJsonObject.put("totalCardgoRoad", totalCardgoRoad);
        layerAndCardgoRoadJsonObject.put("layerCardgoRoadNumber",
                layerCardgoRoadNumberJsonArray);

        // JSONObject layerAndCardgoRoadInfoJsonObject = new JSONObject();
        // layerAndCardgoRoadInfoJsonObject.put("LayerAndCardgoRoadInfo",
        // layerAndCardgoRoadJsonObject);
        return layerAndCardgoRoadJsonObject.toString();
    }

    /**
     * 获取售货机每层秤的个数
     * 
     * @return
     */
    public String getLayerSensorStructureInfo(
            List<Integer> boxActualSensorList, int structureId) {
        BoxStructure boxStructure = findBoxStructureByStructureId(structureId);
        int totalLayer = boxStructure.getTotalLayer();
        List<Integer> layerSensorList = new ArrayList<Integer>();
        for (int i = 1; i <= totalLayer; i++) {
            for (int j = 0; j < boxActualSensorList.size(); j++) {
                String sensor = String.valueOf(boxActualSensorList.get(j));
                if (Integer.valueOf(sensor.substring(0, 1)) == i) {
                    layerSensorList
                            .add(Integer.valueOf(sensor.substring(1, 2)));
                    break;
                }
            }
        }
        return layerSensorList.toString();
    }

}
