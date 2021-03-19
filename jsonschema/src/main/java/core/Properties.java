package core;

import java.util.HashMap;

public class Properties extends HashMap<String, JsonSchema> {

    public void addProperties(String key, JsonSchema jsonSchema) {
        this.put(key, jsonSchema);
    }

    public Properties getProperties() {
        return this;
    }
}
