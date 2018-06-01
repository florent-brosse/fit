## How to search into schema-less JSON with DSE?

The idea is to flatten JSON keys and put keys/values in a dynamic map field through a Field Input Transformer.


### Field input/output (FIT) transformer API

This is an example of https://docs.datastax.com/en/dse/5.1/dse-dev/datastax_enterprise/search/fieldInputOutputTransformerApi.html


CREATE KEYSPACE fit WITH replication = {'class': 'SimpleStrategy' , 'replication_factor': 1 };
 

CREATE TABLE fit.json (
    id text PRIMARY KEY,
    complexvalue text
);

You need to download and unzip dse and set path in maven of 3 jars

mvn clean install

copy target/fit-1.0-SNAPSHOT.jar to install-location/resources/solr/lib 

in my laptop

cp target/fit-1.0-SNAPSHOT.jar /home/florent/soft/dse-5.1.6/resources/solr/lib/

restart node

dsetool create_core fit.json schema=schema.xml solrconfig=config.xml

INSERT INTO fit.json (id, complexvalue) VALUES ('test2','{"name":"Mortgage","amount":"100000","duration":"60","garantor":{"partyId1":"abc123","partyId2":"def456"}}');

cqlsh> select * from fit.json where solr_query='map_garantor.partyId1:abc123' ;

 id    | complexvalue                                                                                               | solr_query
-------+------------------------------------------------------------------------------------------------------------+------------
 test2 | {"name":"Mortgage","amount":"100000","duration":"60","garantor":{"partyId1":"abc123","partyId2":"def456"}} |       null
 test3 | {"name":"Mortgage","amount":"100000","duration":"60","garantor":{"partyId1":"abc123","partyId2":"def456"}} |       null


