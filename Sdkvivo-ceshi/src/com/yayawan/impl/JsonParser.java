package com.yayawan.impl;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonParser {

    public static String getString(JSONObject json, String name) {
        if (json == null || TextUtils.isEmpty(name) || json.isNull(name)) {
            return null;
        }
        try {
            return json.getString(name);
        } catch (JSONException e) {
        }
        return null;
    }

    public static JSONArray getJSONArray(JSONObject json, String name) {
        if (json == null || TextUtils.isEmpty(name) || json.isNull(name)) {
            return null;
        }
        try {
            return json.getJSONArray(name);
        } catch (JSONException e) {
        }
        return null;
    }

    public static Boolean getBoolean(JSONObject json, String name) {
        if (json == null || TextUtils.isEmpty(name) || json.isNull(name)) {
            return false;
        }
        try {
            return json.getBoolean(name);
        } catch (JSONException e) {
            return false;
        }

    }

    public static JSONObject getJsonObject(JSONObject json, String name) {
        if (json == null || TextUtils.isEmpty(name) || json.isNull(name)) {
            return null;
        }
        try {
            return json.getJSONObject(name);
        } catch (JSONException e) {
        }
        return null;
    }

    public static int getInt(JSONObject json, String name) {
        int result = 0;
        String value = getString(json, name);
        if (TextUtils.isEmpty(value)) {
            return result;
        }
        try {
            result = Integer.parseInt(value);
        } catch (NumberFormatException e) {
        }
        return result;
    }

    public static long getLong(JSONObject json, String name) {
        long result = 0;
        String value = getString(json, name);
        if (TextUtils.isEmpty(value)) {
            return result;
        }
        try {
            result = Long.parseLong(value);
        } catch (NumberFormatException e) {
        }
        return result;
    }

    public static float getFloat(JSONObject json, String name) {
        float result = 0;
        String value = getString(json, name);
        if (TextUtils.isEmpty(value)) {
            return result;
        }
        try {
            result = Float.parseFloat(value);
        } catch (NumberFormatException e) {
        }
        return result;
    }

    public static List<String> getArrayStr(String name, JSONObject json) {
        List<String> strs = new ArrayList<String>();
        try {
            JSONArray jsonArray = json.getJSONArray(name);
            for (int i = 0; i < jsonArray.length(); i++) {
                String str = jsonArray.getString(i);
                strs.add(str);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return strs;
    }

    public static Map<String, Object> jsonToMap(String jsonStr) throws JSONException {
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }
        return toMap(new JSONObject(jsonStr));
    }

    private static Map<String, Object> toMap(JSONObject json) throws JSONException {
        Iterator<String> iterator = json.keys();
        Map<String, Object> map = new HashMap<String, Object>();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = json.get(key);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    private static List<Object> toList(JSONArray jsonArray) throws JSONException {
        if (jsonArray == null) {
            return null;
        }

        int len = jsonArray.length();
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < len; i++) {
            Object value = jsonArray.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public static JSONObject mapToJson(Map<String, ? extends Object> map) {
        if (map == null) {
            return null;
        }

        JSONObject result = new JSONObject();
        for (String key : map.keySet()) {
            if (TextUtils.isEmpty(key)) {
                continue;
            }
            Object value = map.get(key);
            if (value instanceof Map) {
                JSONObject json = mapToJson((Map<String, Object>) value);
                try {
                    result.put(key, json);
                } catch (JSONException e) {
                }
            } else if (value instanceof List) {
                List<Map<String, Object>> list = (List<Map<String, Object>>) value;
                JSONArray jsonArray = new JSONArray();
                for (Map<String, Object> m : list) {
                    JSONObject json = mapToJson(m);
                    jsonArray.put(json);
                    try {
                        result.put(key, jsonArray);
                    } catch (JSONException e) {
                    }
                }
            } else {
                try {
                    result.put(key, value == null ? "" : value.toString());
                } catch (JSONException e) {
                }
            }
        }

        return result;
    }

    public static Map<String, String> jsonToStringMap(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }

        JSONObject json = null;
        try {
            json = new JSONObject(jsonStr);
        } catch (JSONException e) {
        }
        if (json == null) {
            return null;
        }

        Iterator<String> iterator = json.keys();
        Map<String, String> map = new HashMap<String, String>();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = JsonParser.getString(json, key);
            if (value == null) {
                value = "";
            }
            map.put(key, value);
        }
        return map;
    }

    public static String stringMapToJson(Map<String, String> map) {
        if (map == null || map.size() <= 0) {
            return null;
        }

        JSONObject result = new JSONObject();
        for (String key : map.keySet()) {
            if (TextUtils.isEmpty(key)) {
                continue;
            }
            String value = map.get(key);
            try {
                result.put(key, value == null ? "" : value);
            } catch (JSONException e) {
            }
        }

        return result == null ? null : result.toString();
    }

}
