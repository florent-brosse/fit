package com.datastax.fit;

import com.datastax.bdp.search.solr.FieldInputTransformer;
import org.apache.lucene.document.Document;
import org.apache.solr.core.SolrCore;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class JsonSimpleFieldInputTransformer extends FieldInputTransformer {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonSimpleFieldInputTransformer.class);

    @Override
    public boolean evaluate(String field) {
        return field.equals("complexvalue");
    }


    @Override
    public void addFieldToDocument(SolrCore core,
                                   IndexSchema schema,
                                   String key,
                                   Document doc,
                                   SchemaField fieldInfo,
                                   String fieldValue,
                                   DocumentHelper helper)
            throws IOException {
        try {
            LOGGER.debug("JsonFieldInputTransformer called");
            LOGGER.debug("fieldValue: " + fieldValue);
            Map<String, String> stringMap = JsonMapConverter.createMap(fieldValue);

            for (Map.Entry<String, String> entry : stringMap.entrySet()) {
                SchemaField mapField = core.getLatestSchema().getField("map_" + entry.getKey());
                helper.addFieldToDocument(core, core.getLatestSchema(), key, doc, mapField, entry.getValue());
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }


}
