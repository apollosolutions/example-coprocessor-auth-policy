package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class CoprocessorBody {

    private String query;
    private String operationName;
    private Object variables;

    public CoprocessorBody() {}

    public CoprocessorBody(String query, String operationName, Object variables) {
        this.query = query;
        this.operationName = operationName;
        this.variables = variables;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public Object getVariables() {
        return variables;
    }

    public void setVariables(Object variables) {
        this.variables = variables;
    }
}
