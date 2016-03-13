package com.mttnow.push.api.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PushObjectMapper extends ObjectMapper {

    public PushObjectMapper() {
        this.registerModule(new JodaModule());
    }

    private static Logger log = LoggerFactory.getLogger(PushObjectMapper.class);

    @Override
    @SuppressWarnings("unchecked")
    public <T> T readValue(String content, Class<T> valueType)
            throws IOException, JsonParseException, JsonMappingException {

        if (content == null) {
            return null;
        }

        log.info("JSON DETAILS: {}", content);
        return super.readValue(content, valueType);
    }

}
