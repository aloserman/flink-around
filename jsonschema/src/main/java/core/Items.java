package core;

import java.util.ArrayList;
import java.util.List;

public class Items {

    private List<JsonSchema> anyOf;

    public void addItems(JsonSchema jsonSchema) {
        if (anyOf == null) {
            anyOf = new ArrayList();
        }
        this.anyOf.add(jsonSchema);
    }

//    public Items getItems() {
//        return this;
//    }

    public List<JsonSchema> getAnyOf() {
        return anyOf;
    }

    public void setAnyOf(List<JsonSchema> anyOf) {
        this.anyOf = anyOf;
    }
}
