package com.zzh.lib.utils;

import java.util.Iterator;
import java.util.Map;

public class HStringUtil {
    private HStringUtil() {
    }

    public static String buildUrl(String baseUrl, Map<String, String> params) {
        if (baseUrl == null || baseUrl.isEmpty() ||
                params == null || params.isEmpty()) {
            return baseUrl;
        }

        StringBuilder sb = new StringBuilder(baseUrl);
        if (!baseUrl.contains("?")) {
            sb.append("?");
        } else {
            if (!baseUrl.endsWith("&")) {
                sb.append("&");
            }
        }

        Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> item = it.next();
            sb.append(item.getKey()).append("=").append(item.getValue());
            sb.append('&');
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }
}
