package core;

import java.util.Map;

public class JsonSchemaInterpreter {

    private String currentKey;

    private String fullKey;

    private String keyName;

    private String description;

    private Integer ruleType;

    private Integer keyType;

    private Boolean required;

    private String specialValue;

    private String caseValue;

    private String gt;

    private String lt;

    //是否包含大于边界
    private Boolean gtb;

    //是否包含小于边界
    private Boolean ltb;

    private Boolean useOneOf;

    Map<String, JsonSchemaInterpreter> jsonSchemaInterpreter;

    public String getCurrentKey() {
        return currentKey;
    }

    public void setCurrentKey(String currentKey) {
        this.currentKey = currentKey;
    }

    public String getFullKey() {
        return fullKey;
    }

    public void setFullKey(String fullKey) {
        this.fullKey = fullKey;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRuleType() {
        return ruleType;
    }

    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
    }

    public Integer getKeyType() {
        return keyType;
    }

    public void setKeyType(Integer keyType) {
        this.keyType = keyType;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getSpecialValue() {
        return specialValue;
    }

    public void setSpecialValue(String specialValue) {
        this.specialValue = specialValue;
    }

    public String getCaseValue() {
        return caseValue;
    }

    public void setCaseValue(String caseValue) {
        this.caseValue = caseValue;
    }

    public String getGt() {
        return gt;
    }

    public void setGt(String gt) {
        this.gt = gt;
    }

    public String getLt() {
        return lt;
    }

    public void setLt(String lt) {
        this.lt = lt;
    }

    public Boolean getGtb() {
        return gtb;
    }

    public void setGtb(Boolean gtb) {
        this.gtb = gtb;
    }

    public Boolean getLtb() {
        return ltb;
    }

    public void setLtb(Boolean ltb) {
        this.ltb = ltb;
    }

    public Boolean getUseOneOf() {
        return useOneOf;
    }

    public void setUseOneOf(Boolean useOneOf) {
        this.useOneOf = useOneOf;
    }

    public Map<String, JsonSchemaInterpreter> getJsonSchemaInterpreter() {
        return jsonSchemaInterpreter;
    }

    public void setJsonSchemaInterpreter(Map<String, JsonSchemaInterpreter> jsonSchemaInterpreter) {
        this.jsonSchemaInterpreter = jsonSchemaInterpreter;
    }
}
