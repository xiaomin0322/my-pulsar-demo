package org.vander.deadletter;

import java.util.concurrent.TimeUnit;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.DeadLetterPolicy;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.SubscriptionType;

public class PulsarConsumers {

    private static PulsarClient client;
    private static Consumer<byte[]> consumer;

    public static void main(String[] args) throws Exception {
        client = PulsarClient.builder()
                .serviceUrl(org.vander.common.Config.URL)
                .build();

        consumer = client.newConsumer()
                .topic("my-topic")
                .ackTimeout(30, TimeUnit.SECONDS)
                .subscriptionName("my-subscription")
                .subscriptionType(SubscriptionType.Shared)
                .deadLetterPolicy(DeadLetterPolicy.builder()
                        .maxRedeliverCount(2)
                        .deadLetterTopic("dl-topic-name")
                        .build())
                .subscribe();

        startConsumer();

    }

    private static void startConsumer() throws PulsarClientException {

    	 int numReceived = 0;
        while (true) {
            // Wait for a message
            Message<byte[]> msg = consumer.receive();
            try {
                System.out.printf("Message received: %s", new String(msg.getData())+ "; Don't send ack \r\n");
                //consumer.acknowledge(msg);
                ++numReceived;
            } catch (Exception ie) {
            	if (ie.getCause() instanceof InterruptedException) {
                    System.out.println("Successfully received " + numReceived + " messages \r\n");
                    Thread.currentThread().interrupt();
                } else {
                    throw ie;
                }
               // consumer.negativeAcknowledge(msg);
            }
        }
    }
}
