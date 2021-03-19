package core;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;

import java.util.*;

public class JsonSchemaGenerate {
    private static final int propertiesType = 0;
    private static final int itemType = 1;

    public String generateSchema(List<JsonSchemaInterpreter> jsonSchemaInterpreters, String schemaVersion) {
        JsonInterpreterToJson jsonInterpreterToJson = new JsonInterpreterToJson();
        com.alibaba.fastjson.JSONObject fastJsonObject = jsonInterpreterToJson.makeSampleJsonWithInterpreters(jsonSchemaInterpreters);
        System.out.println("sample json: " + fastJsonObject.toString());
        JSONObject jsonObject = JSONUtil.parseObj(fastJsonObject);
        Map<String, JsonSchemaInterpreter> pathDescription = getPathDescription(jsonSchemaInterpreters);
        Set<Map.Entry<String, Object>> entries = jsonObject.entrySet();
        JsonSchema jsonSchema = new JsonSchema();
        jsonSchema.setSchema(schemaVersion);
        generateJsonSchema(entries, pathDescription, jsonSchema);
        return JSON.toJSONString(jsonSchema);
    }


    private void generateJsonSchema(Set<Map.Entry<String, Object>> entries, Map<String, JsonSchemaInterpreter> schemaDescription, JsonSchema jsonSchema) {
        for (Map.Entry<String, Object> entry : entries) {
            String key = entry.getKey();
            Object value = entry.getValue();
            JsonSchemaInterpreter jsonSchemaInterpreter = schemaDescription.get(key);
            Properties properties = jsonSchema.getProperties();
            if (properties == null) {
                properties = new Properties();
                jsonSchema.setProperties(properties);
            }
            //因为是否必须是限定子级，所以从描述种取到的限定要限定到父级中
            Boolean required = jsonSchemaInterpreter.getRequired();
            if (required != null && required) {
                if (jsonSchema.getRequired() == null) {
                    jsonSchema.setRequired(new ArrayList<String>() {{
                        add(key);
                    }});
                } else {
                    jsonSchema.getRequired().add(key);
                }
            }
            if (value instanceof JSONObject) {
                resolveJsonSchemaJsonObject(jsonSchema, key, jsonSchemaInterpreter, value, propertiesType);
            } else if (value instanceof JSONArray) {
                resolveJsonSchemaArray(value, jsonSchema, key, jsonSchemaInterpreter);
            } else {
                resolveJsonGenera(jsonSchemaInterpreter, key, jsonSchema, propertiesType);
            }

        }
    }

    public void resolveJsonGenera(JsonSchemaInterpreter jsonSchemaInterpreter, String key, JsonSchema jsonSchema, int addType) {
        Properties properties = jsonSchema.getProperties();
        JsonSchema jsonSchemaTmp = jsonSchema;
        Items items = jsonSchema.getItems();

        //往上是父schema
        jsonSchema = new JsonSchema();
        Integer keyType = jsonSchemaInterpreter.getKeyType();
        jsonSchema.setType(getSchemaType(keyType));
        String fullKey = jsonSchemaInterpreter.getFullKey();
        jsonSchema.setId(getId(fullKey));
        String description = jsonSchemaInterpreter.getDescription();
        jsonSchema.setDescription(description);

        //设置限定值
        if (jsonSchemaInterpreter.getSpecialValue() != null) {
            List<Object> objects = parseValueToArray(jsonSchemaInterpreter.getSpecialValue(), keyType);
            jsonSchema.setEnum(objects);
        }

        //设置样例值
        if (jsonSchemaInterpreter.getCaseValue() != null) {
            List<Object> objects = parseValueToArray(jsonSchemaInterpreter.getCaseValue(), keyType);
            jsonSchema.setExample(objects);
        }

        //设置值限定
        String gt = jsonSchemaInterpreter.getGt();
        Boolean gtb = jsonSchemaInterpreter.getGtb();
        String lt = jsonSchemaInterpreter.getLt();
        Boolean ltb = jsonSchemaInterpreter.getLtb();
        JsonSchemaLimit jsonSchemaLimit = new JsonSchemaLimit();
        if (gt != null || gtb != null || lt != null || ltb != null) {
            if (jsonSchemaInterpreter.getUseOneOf() != null && jsonSchemaInterpreter.getUseOneOf()) {
                jsonSchemaLimit.setUseOneOf(true);
            }
            if (gt != null) {
                Object parseValue = parseValueTypeWithKeyType(gt, keyType);
                jsonSchemaLimit.setMaximum(parseValue);
                jsonSchemaLimit.setExclusiveMaximum(gtb);
            }
            if (lt != null) {
                Object parseValue = parseValueTypeWithKeyType(lt, keyType);
                jsonSchemaLimit.setMinimum(parseValue);
                jsonSchemaLimit.setExclusiveMinimum(ltb);
            }
        }
        jsonSchema.addJsonSchemaLimit(jsonSchemaLimit);
        if (addType == propertiesType) {
            properties.addProperties(key, jsonSchema);
        }
        if (addType == itemType) {
            items.addItems(jsonSchema);
        }
        jsonSchema = jsonSchemaTmp;
    }

    public Object parseValueTypeWithKeyType(String sourceValue, int keyType) {
        if (keyType == 0) {
            long longValue = Long.parseLong(sourceValue);
            return longValue;
        }
        if (keyType == 1) {
            double doubleValue = Double.parseDouble(sourceValue);
            return doubleValue;
        } else if (keyType == 2) {
            return sourceValue;
        } else {
            throw new RuntimeException("如果不是整数或浮点数或者字符串，请不要设置最大最小值");
        }
    }

    public List<Object> parseValueToArray(String sourceValue, int keyType) {
        ArrayList<Object> objects = new ArrayList<>();
        String[] split = sourceValue.split(",");
        for (String value : split) {
            if (keyType == 0) {
                long longValue = Long.parseLong(value);
                objects.add(longValue);
            } else if (keyType == 1) {
                double doubleValue = Double.parseDouble(value);
                objects.add(doubleValue);
            } else {
                objects.add(value);
            }
        }
        return objects;
    }

    public void resolveJsonSchemaArray(Object value, JsonSchema jsonSchema, String key, JsonSchemaInterpreter jsonSchemaInterpreter) {
        if (value instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) value;
            Properties properties = jsonSchema.getProperties();
            Properties sourceProperties = properties.getProperties();
            jsonSchema = new JsonSchema();
            Integer keyType = jsonSchemaInterpreter.getKeyType();
            jsonSchema.setType(getSchemaType(keyType));
            String fullKey = jsonSchemaInterpreter.getFullKey();
            jsonSchema.setId(getId(fullKey));
            String description = jsonSchemaInterpreter.getDescription();
            jsonSchema.setDescription(description);

            sourceProperties.addProperties(key, jsonSchema);

            Items items = jsonSchema.getItems();
            if (items == null) {
                items = new Items();
                jsonSchema.setItems(items);
            }

            for (int i = 0; i < jsonArray.size(); i++) {
                //如果o是object，则和上面一样，如果是基本数据类型，则和下面一样，如果是array，则和当前一样。
                Object o = jsonArray.get(i);
                if (o instanceof JSONObject) {
                    resolveJsonSchemaJsonObject(jsonSchema, String.valueOf(key), jsonSchemaInterpreter.getJsonSchemaInterpreter().get(String.valueOf(i)), o, itemType);
                } else if (o instanceof JSONArray) {
                    resolveJsonSchemaArray(o, jsonSchema, String.valueOf(key), jsonSchemaInterpreter.getJsonSchemaInterpreter().get(String.valueOf(i)));
                } else {
                    resolveJsonGenera(jsonSchemaInterpreter.getJsonSchemaInterpreter().get(String.valueOf(i)), String.valueOf(key), jsonSchema, itemType);
                }
            }
        }
    }

    public void resolveJsonSchemaJsonObject(JsonSchema jsonSchema, String key, JsonSchemaInterpreter jsonSchemaInterpreter, Object value, int addType) {
        Properties properties = jsonSchema.getProperties();
        Items items = jsonSchema.getItems();
        jsonSchema = new JsonSchema();
        Integer keyType = jsonSchemaInterpreter.getKeyType();
        jsonSchema.setType(getSchemaType(keyType));
        String fullKey = jsonSchemaInterpreter.getFullKey();
        jsonSchema.setId(getId(fullKey));
        String description = jsonSchemaInterpreter.getDescription();
        jsonSchema.setDescription(description);

        if (addType == propertiesType) {
            Properties sourceProperties = properties.getProperties();
            sourceProperties.addProperties(key, jsonSchema);
        }
        if (addType == itemType) {
            items.addItems(jsonSchema);
        }
        generateJsonSchema(((JSONObject) value).entrySet(), jsonSchemaInterpreter.getJsonSchemaInterpreter(), jsonSchema);

    }

    public Map<String, JsonSchemaInterpreter> getPathDescription(List<JsonSchemaInterpreter> logRuleModels) {
        HashMap<String, JsonSchemaInterpreter> hashMap = new HashMap<>();
        HashMap<String, JsonSchemaInterpreter> tmpHashMap;
        for (JsonSchemaInterpreter logRuleModel : logRuleModels) {
            tmpHashMap = hashMap;
            String fullKey = logRuleModel.getFullKey();
            String[] split = fullKey.split("\\^");
            for (int i = 0; i < split.length; i++) {
                String path = split[i];
                JsonSchemaInterpreter jsonSchemaInterpreter = tmpHashMap.get(path);
                if (i != split.length - 1) {
                    if (jsonSchemaInterpreter == null) {
                        jsonSchemaInterpreter = new JsonSchemaInterpreter();
                        HashMap<String, JsonSchemaInterpreter> innerHashMap = new HashMap<>();
                        jsonSchemaInterpreter.setJsonSchemaInterpreter(innerHashMap);
                        tmpHashMap.put(path, jsonSchemaInterpreter);
                        tmpHashMap = innerHashMap;
                    } else {
                        if (jsonSchemaInterpreter.getJsonSchemaInterpreter() == null) {
                            jsonSchemaInterpreter.setJsonSchemaInterpreter(new HashMap<>());
                        }
                        tmpHashMap = (HashMap<String, JsonSchemaInterpreter>) jsonSchemaInterpreter.getJsonSchemaInterpreter();
                    }
                } else {
                    //最后一个时，把当前位置的map取出来
                    if (jsonSchemaInterpreter != null) {
                        Map<String, JsonSchemaInterpreter> sourcejsonSchemaInterpreter = jsonSchemaInterpreter.getJsonSchemaInterpreter();
                        logRuleModel.setJsonSchemaInterpreter(sourcejsonSchemaInterpreter);
                    }
                    tmpHashMap.put(path, logRuleModel);
                }
            }
        }
        return hashMap;
    }

    public String getSchemaType(int keyType) {
        switch (keyType) {
            case 0:
                return "integer";
            case 1:
                return "number";
            case 2:
                return "string";
            case 3:
                return "boolean";
            case 4:
                return "object";
            case 5:
                return "array";
            default:
                throw new RuntimeException("未知类型");
        }

    }

    public String getId(String fullKey) {
        String[] split = fullKey.split("\\^");
        String idPath = "/properties";
        if (split.length > 1) {
            for (String path : split) {
                idPath = (idPath + "/" + path);
            }
            return "#" + idPath;
        }
        return null;
    }
}
