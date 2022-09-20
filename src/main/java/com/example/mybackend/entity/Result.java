package com.example.mybackend.entity;

import org.springframework.data.relational.core.sql.In;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Result<T>{
    private String msg;
    private int code;
    private T detail;


    public Result() {}

    public Result(int code, String msg, T detail) {
        this.msg = msg;
        this.code = code;
        this.detail = detail;
    }
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getDetail() {
        return detail;
    }

    public void setDetail(T detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "Result{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", detail=" + detail.toString() +
                '}';
    }

    public String buildJson() throws IllegalAccessException,
            NoSuchMethodException,
            InvocationTargetException {
        /* Build JSON Object Model */
        Field[] fields = detail.getClass().getDeclaredFields();
        JsonArrayBuilder detail = Json.createArrayBuilder();
        for (int i = 0, len = fields.length; i < len; ++i) {
            String varName = fields[i].getName();
            String type = fields[i].getGenericType().toString();
            if (type.equals("class java.lang.String")) {
                Method m = detail.getClass().getMethod("get" + varName);
                String value = (String) m.invoke(detail);
                detail.add(Json.createObjectBuilder().add(varName, value));
            }
            if (type.equals("class java.lang.Integer")) {
                Method m = detail.getClass().getMethod("get" + varName);
                Integer value = (Integer) m.invoke(detail);
                detail.add(Json.createObjectBuilder().add(varName, value));
            }
        }

        JsonObject model = Json.createObjectBuilder()
                .add("code", code)
                .add("msg", msg)
                .add("detail", detail)
                .build();

//        JsonObject model = Json.createObjectBuilder()
//                .add("firstName", "Tom")
//                .add("lastName", "Jerry")
//                .add("age", 10)
//                .add("streetAddress", "Disney Avenue")
//                .add("city", "los angles")
//                .add("state", "CA")
//                .add("postalCode", "12345")
//                .add("phoneNumbers", Json.createArrayBuilder()
//                        .add(Json.createObjectBuilder()
//                                .add("number", "911")
//                                .add("type", "HOME"))
//                        .add(Json.createObjectBuilder()
//                                .add("number", "110")
//                                .add("type", "OFFICE")))
//                .build();

        /* Write JSON Output */
        StringWriter stWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stWriter)) {
            jsonWriter.writeObject(model);
        }
        //return stWriter.toString();

        /* Write formatted JSON Output */
        Map<String,String> config = new HashMap<>();
        config.put(JsonGenerator.PRETTY_PRINTING, "");
        JsonWriterFactory factory = Json.createWriterFactory(config);

        StringWriter stWriterF = new StringWriter();
        try (JsonWriter jsonWriterF = factory.createWriter(stWriterF)) {
            jsonWriterF.writeObject(model);
        }

        return stWriterF.toString();
    }
}
