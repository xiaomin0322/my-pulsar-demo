package org.vander.tracing;

import java.util.concurrent.TimeUnit;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.DeadLetterPolicy;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.SubscriptionType;

import io.streamnative.pulsar.tracing.TracingConsumerInterceptor;

public class PulsarConsumers {

    private static PulsarClient client;
    private static Consumer<byte[]> consumer;
    
    static {
    	OpenTracingZipkin.init();
    }

    @SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
        client = PulsarClient.builder()
                .serviceUrl(org.vander.common.Config.URL)
                .build();

        consumer = client.newConsumer()
                .topic("my-topic")
                .intercept(new TracingConsumerInterceptor<>())
                .ackTimeout(30, TimeUnit.SECONDS)
                .subscriptionName("my-subscription")
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
            Message<byte[]> msg = consumer.receive();
            try {
            	System.out.println("====="+msg.getMessageId());
                System.out.println("Message received: %s   "+ new String(msg.getData()));
                consumer.acknowledge(msg);
            } catch (Exception e) {
                System.err.printf("Unable to consume message: %s", e.getMessage());
                consumer.negativeAcknowledge(msg);
            }
        }
    }
}
