package org.vander.schema;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.impl.schema.JSONSchema;


/**
 * Exception in thread "main" org.apache.pulsar.client.api.PulsarClientException$IncompatibleSchemaException: org.apache.avro.SchemaValidationException: Unable to read schema: 
{
  "type" : "record",
  "name" : "SensorReading",
  "namespace" : "org.vander.schema",
  "fields" : [ {
    "name" : "temperature",
    "type" : "float"
  }, {
    "name" : "addStr",
    "type" : [ "null", "string" ],
    "default" : null
  } ]
}
using schema:
{
  "type" : "record",
  "name" : "SensorReading",
  "namespace" : "org.vander.schema",
  "fields" : [ {
    "name" : "temperature",
    "type" : [ "null", "string" ],
    "default" : null
  }, {
    "name" : "addStr",
    "type" : [ "null", "string" ],
    "default" : null
  } ]
}
	at org.apache.pulsar.client.api.PulsarClientException.unwrap(PulsarClientException.java:673)
	at org.apache.pulsar.client.impl.ConsumerBuilderImpl.subscribe(ConsumerBuilderImpl.java:99)
	at org.vander.schema.PulsarConsumers.main(PulsarConsumers.java:33)
	
	
	SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
	SLF4J: Defaulting to no-operation (NOP) logger implementation
	SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
	Exception in thread "main" org.apache.pulsar.client.api.PulsarClientException$IncompatibleSchemaException: org.apache.pulsar.broker.service.schema.exceptions.IncompatibleSchemaException: org.apache.avro.SchemaValidationException: Unable to read schema: 
	{
	  "type" : "record",
	  "name" : "SensorReading",
	  "namespace" : "org.vander.schema",
	  "fields" : [ {
	    "name" : "temperature",
	    "type" : [ "null", "string" ],
	    "default" : null
	  }, {
	    "name" : "addStr",
	    "type" : [ "null", "string" ],
	    "default" : null
	  } ]
	}
	using schema:
	{
	  "type" : "record",
	  "name" : "SensorReading",
	  "namespace" : "org.vander.schema",
	  "fields" : [ {
	    "name" : "temperature",
	    "type" : "float"
	  }, {
	    "name" : "addStr",
	    "type" : [ "null", "string" ],
	    "default" : null
	  } ]
	}
		at org.apache.pulsar.client.api.PulsarClientException.unwrap(PulsarClientException.java:673)
		at org.apache.pulsar.client.impl.ProducerBuilderImpl.create(ProducerBuilderImpl.java:93)
		at org.vander.schema.PulsarProducer.main(PulsarProducer.java:62)

	
	
	场景：
	1.修改老字段类型不兼容 (发送者跟消费者都回报))  (当队列中还有老的消息，消费者，消息的schema改变时报以上异常)
	2.新增字段兼容
	3.
	
	
	https://pulsar.apache.org/docs/zh-CN/2.5.2/schema-evolution-compatibility/#always_compatible-%E5%92%8C-always_incompatible
 * @author Zengmin.Zhang
 *
 */
public class PulsarProducer {

    private static PulsarClient client;
    private static Producer<SensorReading> producer;

    public static void main(String[] args) throws Exception {
        client = PulsarClient.builder()
                .serviceUrl(org.vander.common.Config.URL)
                .build();

        producer = client.newProducer(JSONSchema.of(SensorReading.class))
                .topic("my_topicschema")
                .create();

        startProducer();

    }

    private static void startProducer() throws Exception {

    	int index = 10;
        while (true) {
        	SensorReading reading = new SensorReading();
        	reading.setTemperature("name"+index);
        	index ++;
            System.out.println("Start produce");
            producer.newMessage()
                    .value(reading)
                    .send();


            Thread.sleep(1000);
        }
    }
}
