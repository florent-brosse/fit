package com.datastax.fit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonMapConverter {
    private static final int POSITION_INCREMENTAL_GAP = 100;
    private static final String SPACE_INCREMENTAL_STRING = new String(new char[POSITION_INCREMENTAL_GAP]).replace('\0', ' ');
    private static ObjectMapper objectMapper = new ObjectMapper();


    public static Map<String,String> createMap(String json){
        Map<String, String> map = new HashMap<String, String>();
        try {
            addKeys("", objectMapper.readTree(json), map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private static void addKeys(String currentPath, JsonNode jsonNode, Map<String, String> map) {
        if (jsonNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();
            String pathPrefix = currentPath.isEmpty() ? "" : currentPath + ".";

            while (iter.hasNext()) {
                Map.Entry<String, JsonNode> entry = iter.next();
                addKeys(pathPrefix + entry.getKey(), entry.getValue(), map);
            }
        } else if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for (int i = 0; i < arrayNode.size(); i++) {
                addKeys(currentPath + "_", arrayNode.get(i), map);
            }
        } else if (jsonNode.isValueNode()) {
            ValueNode valueNode = (ValueNode) jsonNode;
            String currentValue = map.get(currentPath);
            if (currentValue == null) {
                map.put(currentPath, valueNode.asText());
            } else {
                map.put(currentPath, currentValue + SPACE_INCREMENTAL_STRING + valueNode.asText());
            }
        }
    }


}
