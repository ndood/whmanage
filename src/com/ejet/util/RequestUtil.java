package com.ejet.util;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * HttpServletRequest������
 * Company��������###EJET###���޹�˾
 * Copyright: Copyright (c) 2009
 * version 2.0.0.0
 * @author ###ejet###
 */
public class RequestUtil
{
    /**
     * ��ñ��������в���������Map����ʽ���ء�
     * @param request http�������
     * @return Map(String, String[]) �������ƶ�Ӧ��ֵ��
     */
    public static Map getParamMap(HttpServletRequest request)
    {
        Map map = new LinkedHashMap();

        Enumeration names = request.getParameterNames();
        while (names.hasMoreElements())
        {
            String name = (String) names.nextElement();
            String[] values = request.getParameterValues(name);
            map.put(name, values);
        }

        return map;
    }
}