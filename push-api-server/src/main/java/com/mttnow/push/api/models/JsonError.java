package com.mttnow.push.api.models;

import com.google.common.collect.ImmutableMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

public class JsonError {
    private final String message;

    public JsonError(String message) {
        this.message = message;
    }

    public ModelAndView asModelAndView() {
        MappingJacksonJsonView jsonView = new MappingJacksonJsonView();
        return new ModelAndView(jsonView, ImmutableMap.of("error", message));
    }
}
