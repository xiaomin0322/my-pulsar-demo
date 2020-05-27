package org.vander.failover;

import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.impl.Murmur3_32Hash;

import java.util.concurrent.TimeUnit;

public class PulsarConsumers {

    private static PulsarClient client;
    private static Consumer<byte[]> consumer;

    public static void main(String[] args) throws Exception {
        client = PulsarClient.builder()
                .serviceUrl(org.vander.common.Config.URL)
                .build();

        consumer = client.newConsumer()
                .topic("public/dominos/coupon4")
                .ackTimeout(30, TimeUnit.SECONDS)
                .subscriptionName("my-subscription")
                .subscriptionType(SubscriptionType.Failover)
                .subscribe();

        startConsumer();

    }

    private static void startConsumer() throws PulsarClientException {

        while (true) {
            // Wait for a message
            Message<byte[]> msg = consumer.receive();
            try {
                System.out.printf("Message received: %s", new String(msg.getData()));
                consumer.acknowledge(msg);
            } catch (Exception e) {
                System.err.printf("Unable to consume message: %s", e.getMessage());
                consumer.negativeAcknowledge(msg);
            }
        }
    }
}
