package com.company.common.tools;

import java.util.Map;
import java.util.TreeMap;

public class SignUtil {

    public static String generateSign(String secretKey, Map<String, String> map) {
        if (!(map instanceof TreeMap)) {
            map = new TreeMap<>(map);
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry :map.entrySet()) {
            if (entry.getKey().equals("sign")) {
                continue;
            }
            if (entry.getValue().trim().length() > 0) //参数值为空，则不参与签名
                sb.append(entry.getKey()).append("=").append(entry.getValue().trim()).append("&");
        }
        sb.append("key=").append(secretKey);

        return Utils.md5Encoding(sb.toString());
    }
}
