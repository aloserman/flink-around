package core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class JsonInterpreterToJson {
    //通过规则限定，生成简单的json对象
    public JSONObject makeSampleJsonWithInterpreters(List<JsonSchemaInterpreter> jsonSchemaInterpreters) {
        JSONObject sampleJson = new JSONObject();
        for (JsonSchemaInterpreter jsonSchemaInterpreter : jsonSchemaInterpreters) {
            String fullKey = jsonSchemaInterpreter.getFullKey();
            String[] keyPaths = fullKey.split("\\" + JsonValidateConstants.KEY_LEVELS_SEPARATOR);
            //获取数据类型
            Integer currentKeyType = jsonSchemaInterpreter.getKeyType();

            Object lastJSONObj = sampleJson;
            //结构层
            for (int i = 0; i < keyPaths.length; i++) {
                String currentKey = keyPaths[i];
                Object currentJsonObj = getValue(lastJSONObj, currentKey);
                if (i != keyPaths.length - 1) {
                    //当之前的路径不存在时，假定原有数据结构一定是JSONObject
                    if (currentJsonObj == null) {
                        JSONObject newJsonObj = new JSONObject();
                        addValue(lastJSONObj, currentKey, newJsonObj);
                        lastJSONObj = newJsonObj;
                    } else {
                        lastJSONObj = currentJsonObj;
                    }
                } else {
                    //最底层级,校正逻辑，当原始放入的结构不一致时，生成新的结构，并将原来结构下的值赋给新结构
                    //先判断当前的位置的结构类型
                    if (currentJsonObj != null) {
                        if (currentKeyType == 4) {
                            //什么也不做，默认就是obj
                            //当前key对应的value
                            if (currentJsonObj instanceof JSONArray) {
                                JSONArray currentJsonArray = (JSONArray) currentJsonObj;
                                Object o = currentJsonArray.get(0);
                                addValue(lastJSONObj, currentKey, o);
                            }
                            //将数组转化为jsonObj
                        } else if (currentKeyType == 5) {
                            //将jsonObj转换为数组
                            if (currentJsonObj instanceof JSONObject) {
                                JSONArray jsonArray = new JSONArray();
                                jsonArray.add(currentJsonObj);
                                addValue(lastJSONObj, currentKey, jsonArray);
                            }
                        }
                    } else {
                        //判断当前类型
                        switch (currentKeyType) {
                            case 0: {
                                //整数
//                                String caseValue = jsonSchemaInterpreter.getCaseValue();
//                                String[] split = caseValue.split(",");
//                                if (split.length)
//                                if (caseValue != null) {
//                                }
                                addValue(lastJSONObj, currentKey, 123);

                            }
                            break;
                            case 1: {
                                addValue(lastJSONObj, currentKey, 123.12D);
                            }
                            break;
                            case 2: {
                                addValue(lastJSONObj, currentKey, "abc");
                            }
                            break;
                            case 3: {
                                addValue(lastJSONObj, currentKey, false);
                            }
                            break;
                            case 4: {
                                addValue(lastJSONObj, currentKey, new JSONObject());
                            }
                            break;
                            case 5: {
                                addValue(lastJSONObj, currentKey, new JSONArray());
                            }
                            break;
                            default: {
                                break;
                            }
                        }
                    }
                }
            }
        }
        return sampleJson;
    }

    public void addValue(Object jsonObj, String key, Object value) {
        if (value == null) {
            value = new JSONObject();
        }
        if (jsonObj instanceof JSONObject) {
            ((JSONObject) jsonObj).put(key, value);
        }
        if (jsonObj instanceof JSONArray) {
            JSONArray jsonObjArray = (JSONArray) jsonObj;
            //找到一个OBJ，考虑多个obj的情况下,如果没有则直接创建
            addJsonArr(jsonObjArray, Integer.parseInt(key), value);
        }
    }

    public Object getValue(Object obj, String key) {
        if (obj instanceof JSONArray) {
            JSONArray jsonObj = (JSONArray) obj;
            int index = Integer.parseInt(key);
            if (jsonObj.size() - 1 >= index) {
                return jsonObj.get(index);
            } else {
                return null;
            }
        }
        if (obj instanceof JSONObject) {
            return ((JSONObject) obj).get(key);
        }
        return null;
    }

    public void addJsonArr(JSONArray jsonArray, int index, Object value) {
        if (jsonArray.size() - 1 >= index) {
            jsonArray.add(index, value);
            if (jsonArray.size() >= index) {
                jsonArray.remove(index + 1);
            }
        } else {
            while (jsonArray.size() - 1 < index) {
                jsonArray.add(null);
            }
            jsonArray.add(index, value);
            if (jsonArray.size() >= index) {
                jsonArray.remove(index + 1);
            }
        }
    }
}
