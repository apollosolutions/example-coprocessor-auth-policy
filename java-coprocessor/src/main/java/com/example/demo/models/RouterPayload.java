package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class RouterPayload {
    private Object control;
    private CoprocessorStage stage;
    private Integer version;
    private String id;
    private String method;
    private LinkedHashMap<String, ArrayList<String>> headers;
    private String sdl;
    private CoprocessorBody body;
    private CoprocessorContext context;

    RouterPayload() {
        this.control = "continue";
        this.id = "";
    }

    public Object getControl() {
        return control;
    }

    public void setControl(Object control) {
        this.control = control;
    }

    public CoprocessorStage getStage() {
        return stage;
    }

    public void setStage(CoprocessorStage stage) {
        this.stage = stage;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public LinkedHashMap<String, ArrayList<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(LinkedHashMap<String, ArrayList<String>> headers) {
        this.headers = headers;
    }

    public String getSdl() {
        return sdl;
    }

    public void setSdl(String sdl) {
        this.sdl = sdl;
    }

    public CoprocessorBody getBody() {
        return body;
    }

    public void setBody(CoprocessorBody body) {
        this.body = body;
    }

    public CoprocessorContext getContext() {
        return context;
    }

    public void setContext(CoprocessorContext context) {
        this.context = context;
    }
}
