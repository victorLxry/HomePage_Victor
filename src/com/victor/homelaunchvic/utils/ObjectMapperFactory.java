package com.victor.homelaunchvic.utils;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author wangx
 * 
 * @date 2011-11-4
 * 
 * @version 1.0
 * 
 * @since SDK1.5
 */
public class ObjectMapperFactory {
    private static ObjectMapper mapper = null;

    public static ObjectMapper getInstance() {
        if (mapper == null) {
            mapper = new ObjectMapper();
            DeserializationConfig config = mapper.getDeserializationConfig();
            DeserializationConfig newConfig = config
                    .without(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.setDeserializationConfig(newConfig);
        }

        return mapper;
    }
}
