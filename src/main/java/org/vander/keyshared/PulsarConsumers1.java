package org.vander.keyshared;

import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.impl.Murmur3_32Hash;

import java.util.concurrent.TimeUnit;

/**
 * ”–Œ Ã‚
 * @author Zengmin.Zhang
 *
 */
public class PulsarConsumers1 {

    private static PulsarClient client;
    private static Consumer<byte[]> consumer;

    public static void main(String[] args) throws Exception {
        client = PulsarClient.builder()
                .serviceUrl(org.vander.common.Config.URL)
                .build();

        consumer = client.newConsumer()
                //.topic("public/dominos/coupon3")
        		 .topic("my-topic")
                .ackTimeout(30, TimeUnit.SECONDS)
                .subscriptionName("my-subscription")
                .keySharedPolicy(KeySharedPolicy.stickyHashRange().ranges(Range.of(0,35535)))
               // .keySharedPolicy(KeySharedPolicy.autoSplitHashRange())
                .subscriptionType(SubscriptionType.Key_Shared)
                .subscribe();

        startConsumer();

    }

    private static void startConsumer() throws PulsarClientException {

        while (true) {
            // Wait for a message
            Message<byte[]> msg = consumer.receive();
            try {
                System.out.printf("Message received: %s", new String(msg.getData())+"\r\n");
                consumer.acknowledge(msg);
            } catch (Exception e) {
                System.err.printf("Unable to consume message: %s", e.getMessage());
                consumer.negativeAcknowledge(msg);
            }
        }
    }
}
