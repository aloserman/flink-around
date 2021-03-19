package core;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

public class JsonSchema {
    @JSONField(name = "$id")
    protected String id;
    @JSONField(name = "$schema")
    protected String schema;
    private String title;
    private String description;
    private String type;
    private List<String> required;
    private Properties properties;

    private Object minimum;
    private Boolean exclusiveMinimum;
    private Object maximum;
    private Boolean exclusiveMaximum;
    private Items items;

    //正则
    private String pattern;

    private List<Object> example;

    private List<JsonSchema> oneOf;

    @JSONField(name = "enum")
    private List<Object> Enum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public List<String> getRequired() {
        return required;
    }

    public void setRequired(List<String> required) {
        this.required = required;
    }

    @Override
    public String toString() {
        return "JsonSchema{" +
                "$id='" + id + '\'' +
                ", $schema='" + schema + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", properties=" + properties +
                ", required=" + required +
                '}';
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public List<Object> getExample() {
        return example;
    }

    public void setExample(List<Object> example) {
        this.example = example;
    }

    public List<Object> getEnum() {
        return Enum;
    }

    public void setEnum(List<Object> anEnum) {
        this.Enum = anEnum;
    }

    public void addJsonSchemaLimit(JsonSchemaLimit jsonSchemaLimit) {
        if (jsonSchemaLimit.isUseOneOf()) {
            this.oneOf = new ArrayList();
            JsonSchema maximumJsonSchema = new JsonSchema();
            Object maximum = jsonSchemaLimit.getMaximum();
            if (maximum != null) {
                maximumJsonSchema.maximum = maximum;
                Boolean exclusiveMaximum = jsonSchemaLimit.getExclusiveMaximum();
                if (exclusiveMaximum != null) {
                    maximumJsonSchema.exclusiveMaximum = exclusiveMaximum;
                }
                oneOf.add(maximumJsonSchema);
            }

            JsonSchema minimumJsonSchema = new JsonSchema();
            Object minimum = jsonSchemaLimit.getMinimum();
            if (minimum != null) {
                minimumJsonSchema.minimum = minimum;
                Boolean exclusiveMinimum = jsonSchemaLimit.getExclusiveMinimum();
                if (exclusiveMinimum != null) {
                    minimumJsonSchema.exclusiveMinimum = exclusiveMinimum;
                }
                oneOf.add(minimumJsonSchema);
            }
        } else {
            Boolean exclusiveMaximum = jsonSchemaLimit.getExclusiveMaximum();
            if (exclusiveMaximum != null) {
                this.exclusiveMaximum = exclusiveMaximum;
            }
            Object maximum = jsonSchemaLimit.getMaximum();
            if (maximum != null) {
                this.maximum = maximum;
            }
            Boolean exclusiveMinimum = jsonSchemaLimit.getExclusiveMinimum();
            if (exclusiveMinimum != null) {
                this.exclusiveMinimum = exclusiveMinimum;
            }
            Object minimum = jsonSchemaLimit.getMinimum();
            if (minimum != null) {
                this.minimum = minimum;
            }
        }
    }

    public Object getMinimum() {
        return minimum;
    }

    public Boolean getExclusiveMinimum() {
        return exclusiveMinimum;
    }

    public Object getMaximum() {
        return maximum;
    }

    public Boolean getExclusiveMaximum() {
        return exclusiveMaximum;
    }

    public List<JsonSchema> getOneOf() {
        return oneOf;
    }

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }
}
