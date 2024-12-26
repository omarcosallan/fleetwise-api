package com.omarcosallan.fleetwise.mappers;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import java.util.Collections;
import java.util.Map;

public class ResponseWrapper<T> {
    private final String fieldName;
    private final T data;

    public ResponseWrapper(String fieldName, T data) {
        this.fieldName = fieldName;
        this.data = data;
    }

    @JsonAnyGetter
    public Map<String, T> getData() {
        return Collections.singletonMap(fieldName, data);
    }
}

