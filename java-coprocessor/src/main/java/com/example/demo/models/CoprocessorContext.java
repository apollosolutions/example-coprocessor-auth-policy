package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class CoprocessorContext {
    private JsonNode entries;

    public CoprocessorContext() {
    }

    public CoprocessorContext(JsonNode entries) {
        this.entries = entries;
    }

    public JsonNode getEntries() {
        return entries;
    }

    public void setEntries(JsonNode entries) {
        this.entries = entries;
    }
}
