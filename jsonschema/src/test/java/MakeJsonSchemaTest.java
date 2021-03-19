import core.DataTypeEnum;
import core.JsonSchemaGenerate;
import core.JsonSchemaInterpreter;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class MakeJsonSchemaTest {

    @Test
    public void testGenerateJsonSchema() {
        JsonSchemaGenerate jsonSchemaGenerate = new JsonSchemaGenerate();

        ArrayList<JsonSchemaInterpreter> jsonSchemaInterpreters = new ArrayList<JsonSchemaInterpreter>();

        //第一层，生成school对象
        JsonSchemaInterpreter jsonSchemaInterpreter1 = new JsonSchemaInterpreter();
        jsonSchemaInterpreter1.setCurrentKey("school");
        jsonSchemaInterpreter1.setFullKey("school");
        jsonSchemaInterpreter1.setKeyType(DataTypeEnum.OBJECT.getType());
        jsonSchemaInterpreters.add(jsonSchemaInterpreter1);

        //第二层，生成school属性
        JsonSchemaInterpreter jsonSchemaInterpreter2 = new JsonSchemaInterpreter();
        jsonSchemaInterpreter2.setCurrentKey("name");
        jsonSchemaInterpreter2.setFullKey("school^name");
        jsonSchemaInterpreter2.setCaseValue("哈佛大学");
        jsonSchemaInterpreter2.setKeyType(DataTypeEnum.STRING.getType());
        jsonSchemaInterpreters.add(jsonSchemaInterpreter2);

        //第二层，生成school属性
        JsonSchemaInterpreter jsonSchemaInterpreter3 = new JsonSchemaInterpreter();
        jsonSchemaInterpreter3.setCurrentKey("age");
        jsonSchemaInterpreter3.setFullKey("school^age");
        jsonSchemaInterpreter3.setCaseValue("100");
        jsonSchemaInterpreter3.setKeyType(DataTypeEnum.INTEGER.getType());
        jsonSchemaInterpreters.add(jsonSchemaInterpreter3);

        //生成第二层，数组结构
        JsonSchemaInterpreter jsonSchemaInterpreter4 = new JsonSchemaInterpreter();
        jsonSchemaInterpreter4.setCurrentKey("students");
        jsonSchemaInterpreter4.setFullKey("school^students");
        jsonSchemaInterpreter4.setKeyType(DataTypeEnum.ARRAY.getType());
        jsonSchemaInterpreters.add(jsonSchemaInterpreter4);

        //生成第三层，为数组结构添加元素，比如添加两个学生
        JsonSchemaInterpreter jsonSchemaInterpreter5 = new JsonSchemaInterpreter();
        jsonSchemaInterpreter5.setCurrentKey("zs");
        jsonSchemaInterpreter5.setFullKey("school^students^0^zs");
        jsonSchemaInterpreter5.setKeyType(DataTypeEnum.OBJECT.getType());
        jsonSchemaInterpreters.add(jsonSchemaInterpreter5);

        //为张三添加属性信息
        JsonSchemaInterpreter jsonSchemaInterpreter6 = new JsonSchemaInterpreter();
        jsonSchemaInterpreter6.setCurrentKey("name");
        jsonSchemaInterpreter6.setFullKey("school^students^0^zs^name");
        jsonSchemaInterpreter6.setKeyType(DataTypeEnum.STRING.getType());
        jsonSchemaInterpreter6.setCaseValue("张三");
        jsonSchemaInterpreters.add(jsonSchemaInterpreter6);

        JsonSchemaInterpreter jsonSchemaInterpreter7 = new JsonSchemaInterpreter();
        jsonSchemaInterpreter7.setCurrentKey("age");
        jsonSchemaInterpreter7.setFullKey("school^students^0^zs^age");
        jsonSchemaInterpreter7.setKeyType(DataTypeEnum.INTEGER.getType());
        jsonSchemaInterpreter7.setCaseValue("10");
        jsonSchemaInterpreters.add(jsonSchemaInterpreter7);

        //为students再添加一个学生
        JsonSchemaInterpreter jsonSchemaInterpreter8 = new JsonSchemaInterpreter();
        jsonSchemaInterpreter8.setCurrentKey("lisi");
        jsonSchemaInterpreter8.setFullKey("school^students^1^lisi");
        jsonSchemaInterpreter8.setKeyType(DataTypeEnum.OBJECT.getType());
        jsonSchemaInterpreters.add(jsonSchemaInterpreter8);

        String jsonSchema = jsonSchemaGenerate.generateSchema(jsonSchemaInterpreters, "http://json-schema.org/draft-07/schema#");
        System.out.println(jsonSchema);

    }
}
