package core;

public enum DataTypeEnum {

    INTEGER(0), NUMBER(1), STRING(2), BOOLEAN(3), OBJECT(4), ARRAY(5);

    private final int type;

    DataTypeEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
