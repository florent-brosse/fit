package com.datastax.fit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;

import java.util.*;
import java.io.IOException;

public class JsonMapConverter {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String,List<String>> createMapList(String json){
        Map<String,List<String>> map = new HashMap<>();
        try {
            addKeysList("", objectMapper.readTree(json), map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private static void addKeysList(String currentPath, JsonNode jsonNode, Map<String, List<String>> map) {
        if (jsonNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();
            String pathPrefix = currentPath.isEmpty() ? "" : currentPath + ".";

            while (iter.hasNext()) {
                Map.Entry<String, JsonNode> entry = iter.next();
                addKeysList(pathPrefix + entry.getKey(), entry.getValue(), map);
            }
        } else if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for (int i = 0; i < arrayNode.size(); i++) {
                addKeysList(currentPath + "_", arrayNode.get(i), map);
            }
        } else if (jsonNode.isValueNode()) {
            ValueNode valueNode = (ValueNode) jsonNode;
            List<String> currentValue = map.get(currentPath);
            if (currentValue == null) {
                map.put(currentPath, new ArrayList<String>());
                currentValue = map.get(currentPath);
            }
            currentValue.add(valueNode.asText());
        }
    }



}
