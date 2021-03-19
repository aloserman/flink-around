package core;

public class JsonSchemaLimit {

    private Object minimum;
    private Boolean exclusiveMinimum;
    private Object maximum;
    private Boolean exclusiveMaximum;
    private boolean useOneOf;

    public Object getMinimum() {
        return minimum;
    }

    public void setMinimum(Object minimum) {
        this.minimum = minimum;
    }

    public Boolean getExclusiveMinimum() {
        return exclusiveMinimum;
    }

    public void setExclusiveMinimum(Boolean exclusiveMinimum) {
        this.exclusiveMinimum = exclusiveMinimum;
    }

    public Object getMaximum() {
        return maximum;
    }

    public void setMaximum(Object maximum) {
        this.maximum = maximum;
    }

    public Boolean getExclusiveMaximum() {
        return exclusiveMaximum;
    }

    public void setExclusiveMaximum(Boolean exclusiveMaximum) {
        this.exclusiveMaximum = exclusiveMaximum;
    }

    public boolean isUseOneOf() {
        return useOneOf;
    }

    public void setUseOneOf(boolean useOneOf) {
        this.useOneOf = useOneOf;
    }

}

