package wen.SmartBPractice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class Util {
    static public String mapToJsonString (String K, String V) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();
        map.put(K, V);
        String str = mapper.writeValueAsString(map);

        return str;
    }
}
