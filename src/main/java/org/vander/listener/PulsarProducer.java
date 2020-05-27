package org.vander.listener;

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
                .batchingMaxPublishDelay(10, TimeUnit.SECONDS)
                .batchingMaxMessages(100)
                .enableBatching(true).blockIfQueueFull(true)
                .create();

        startProducer();

    }

    private static void startProducer() throws Exception {
 
    	int index =0;
        while (true) {
        	index ++;
            //System.out.println("Start produce");
            producer.newMessage()
                    .value(("my-message-"+index).getBytes())
                    .sendAsync();

            Thread.sleep(100);
        }
    }
}
