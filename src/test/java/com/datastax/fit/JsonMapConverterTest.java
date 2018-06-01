package com.datastax.fit;

import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonMapConverterTest {
    @Test
    void createMap() throws IOException, URISyntaxException {
        String json = new String(Files.readAllBytes(Paths.get(this.getClass().getResource("/json1.txt").toURI())),"UTF-8");
        Map<String,List<String>> map = JsonMapConverter.createMapList(json);
        assertTrue(map.containsKey("credit.amount"));
        assertEquals(map.get("credit.amount"), Arrays.asList("100000"));
        assertEquals(map.toString(),"{credit.duration=[60], credit.garantor.partyId1=[abc123], creditRequestId=[azerty123], credit.name=[Mortgage], credit.amount=[100000], credit.garantor.partyId2=[def456]}");
    }

    @Test
    void createMapWithArrayList() throws IOException, URISyntaxException {
        String json = new String(Files.readAllBytes(Paths.get(this.getClass().getResource("/json2.txt").toURI())),"UTF-8");
        Map<String,List<String>> map = JsonMapConverter.createMapList(json);
        assertEquals(map.toString(),"{cars_=[Ford, BMW, Fiat], name=[John], age=[30]}");
    }
}