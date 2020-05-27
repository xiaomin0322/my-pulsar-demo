package org.vander.keyshared;

import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.impl.Murmur3_32Hash;

import java.util.concurrent.TimeUnit;

public class PulsarConsumers2 {

    private static PulsarClient client;
    private static Consumer<byte[]> consumer;

    public static void main(String[] args) throws Exception {
        client = PulsarClient.builder()
                .serviceUrl(org.vander.common.Config.URL)
                .build();

        int hashcode = Murmur3_32Hash.getInstance().makeHash("key-1") % 65536;
        
        consumer = client.newConsumer()
        		//.messageListener(messageListener)
                //.topic("public/dominos/coupon3")
        		 .topic("my-topic")
                .ackTimeout(30, TimeUnit.SECONDS)
                .subscriptionName("my-subscription")
                //.keySharedPolicy(KeySharedPolicy.stickyHashRange().ranges(Range.of(0,65535)))
               // .keySharedPolicy(KeySharedPolicy.autoSplitHashRange())
                .keySharedPolicy(KeySharedPolicy.stickyHashRange().ranges(Range.of(hashcode - 1, hashcode + 1)))
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
