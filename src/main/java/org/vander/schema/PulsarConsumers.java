package org.vander.schema;

import java.util.concurrent.TimeUnit;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.DeadLetterPolicy;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.SubscriptionType;
import org.apache.pulsar.client.impl.schema.JSONSchema;

public class PulsarConsumers {

    private static PulsarClient client;
    private static Consumer<SensorReading> consumer;

    public static void main(String[] args) throws Exception {
        client = PulsarClient.builder()
                .serviceUrl(org.vander.common.Config.URL)
                .build();

        consumer = client.newConsumer(JSONSchema.of(SensorReading.class))
        		.replicateSubscriptionState(true)
        		.topic("my-topicschema-")
                .ackTimeout(30, TimeUnit.SECONDS)
                .subscriptionName("my-my_topicschema")
                .subscriptionType(SubscriptionType.Shared)
                .deadLetterPolicy(DeadLetterPolicy.builder()
                        .maxRedeliverCount(10)
                        .deadLetterTopic("dl-topic-name")
                        .build())
                .subscribe();

        startConsumer();

    }

    private static void startConsumer() throws PulsarClientException {

        while (true) {
            // Wait for a message
            Message<SensorReading> msg = consumer.receive();
            try {
                System.out.printf("Message received: %s", msg.getValue().getTemperature());
                consumer.acknowledge(msg);
            } catch (Exception e) {
                System.err.printf("Unable to consume message: %s", e.getMessage());
                consumer.negativeAcknowledge(msg);
            }
        }
    }
}
