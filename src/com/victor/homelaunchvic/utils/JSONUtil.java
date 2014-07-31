/*
 * $Revision: $
 * $Date: $
 * $Id: $
 * ====================================================================
 * Copyright © 2012 Beijing seeyon software Co..Ltd..All rights reserved.
 *
 * This software is the proprietary information of Beijing seeyon software Co..Ltd.
 * Use is subject to license terms.
 */
package com.victor.homelaunchvic.utils;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.util.Log;

public class JSONUtil {
    private static ObjectMapper mapper = ObjectMapperFactory.getInstance();

    /**
     * ����json ����
     * 
     * @param result
     * @param cls
     * @return
     * @throws M1Exception
     */
    public static <T> T parseJSONString(String result, Class<T> cls)
             {
        try {
            T t = mapper.readValue(result, cls);
            return t;
        } catch (Exception e) {
            if (result != null) {
                Log.e("result", result);
            }
            return null;
           
        }
    }

    /**
     * ����json ����
     * 
     * @param result
     * @param cls
     * @return
     * @throws M1Exception
     */
    public static <T> T parseJSONString(String result,
            TypeReference<T> typeReference)  {
        try {
            T t = mapper.readValue(result, typeReference);
            return t;
        } catch (Exception e) {
            if (result != null)
                Log.e("result", result);
            Log.e("error", e.toString());
            return null;
        }
    }

    /**
     * ���� ת��Ϊjson�ַ�
     * 
     * @param result
     * @param cls
     * @return
     * @throws M1Exception
     */
    public static String writeEntityToJSONString(Object entity)
            {
        try {
            return mapper.writeValueAsString(entity);
        } catch (Exception e) {
            Log.e("error", e.toString());
           return null;
        }
    }

}
