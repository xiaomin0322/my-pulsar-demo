package org.vander.delayed;

import java.util.concurrent.TimeUnit;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;

public class PulsarProducer {

    private static PulsarClient client;
    private static Producer<byte[]> producer;

    public static void main(String[] args) throws Exception {
        client = PulsarClient.builder()
                .serviceUrl(org.vander.common.Config.URL)
                .build();

        producer = client.newProducer()
                .topic("my-topic")
                .create();

        startProducer2();

    }
    
    private static void startProducer2() throws Exception {

            System.out.println("Start produce");
            
            producer.newMessage()
            .value("my-message-".getBytes())
            .send();
            
            producer.newMessage()
                    .value("my-DeliverAfter -message-".getBytes())
                    .deliverAfter(5, TimeUnit.SECONDS)
                    .send();
            
            
    }
            

    private static void startProducer() throws Exception {

        while (true) {
            System.out.println("Start produce");
            producer.newMessage()
                    .value("my-DeliverAfter -message-".getBytes())
                    .deliverAfter(5, TimeUnit.SECONDS)
                    .send();


            Thread.sleep(1000);
        }
    }
}
