package com.mttnow.push.api.models;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import java.util.HashMap;
import java.util.Map;

public class ErrorMessage {
    private String error;
    private Map<String, String> fieldErrors;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ErrorMessage that = (ErrorMessage) o;

        if (!error.equals(that.error)) return false;
        if (fieldErrors != null ? !fieldErrors.equals(that.fieldErrors) : that.fieldErrors != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = error.hashCode();
        result = 31 * result + (fieldErrors != null ? fieldErrors.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "error='" + error + '\'' +
                ", fieldErrors=" + fieldErrors +
                '}';
    }

    public ModelAndView asModelAndView() {
        MappingJacksonJsonView jsonView = new MappingJacksonJsonView();
        Map<String, String> responseMap= new HashMap<String, String>();
        responseMap.put("error",error);
        if(fieldErrors != null){
            responseMap.putAll(fieldErrors);
        }
        return new ModelAndView(jsonView, responseMap);
    }
}
