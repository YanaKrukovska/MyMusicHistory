package com.ritacle.mymusichistory.model;

import java.util.List;

public class ResponseMMH<T> {

    private T object;
    private List<InputError> errors;

    public ResponseMMH(T object, List<InputError> errors) {
        this.object = object;
        this.errors = errors;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public List<InputError> getErrors() {
        return errors;
    }

    public void setErrors(List<InputError> errors) {
        this.errors = errors;
    }

    public boolean isOkay() {
        return errors.size() == 0;
    }
}