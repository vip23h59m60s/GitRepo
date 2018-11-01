package com.chinapalms.kwobox.utils;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonParseUtil {

    public static List<JSONObject> parseJsonObjectToObjectList(
            JSONObject jsonObject, String arrayName) {
        List<JSONObject> jsonObjectsList = new ArrayList<JSONObject>();
        JSONArray jsonArray = jsonObject.getJSONArray(arrayName);
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonObjectsList.add(jsonArray.getJSONObject(i));
        }
        return jsonObjectsList;
    }

    public static List<JSONObject> parseJsonArrayToObjectList(
            JSONArray jsonArray) {
        List<JSONObject> jsonObjectsList = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonObjectsList.add(jsonArray.getJSONObject(i));
        }
        return jsonObjectsList;
    }

}
