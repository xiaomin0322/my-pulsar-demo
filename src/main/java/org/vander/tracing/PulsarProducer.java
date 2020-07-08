package org.vander.tracing;

import java.util.Random;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;

import io.streamnative.pulsar.tracing.TracingProducerInterceptor;

public class PulsarProducer {

    private static PulsarClient client;
    private static Producer<byte[]> producer;
    
    static {
    	OpenTracingZipkin.init();
    }

    public static void main(String[] args) throws Exception {
        client = PulsarClient.builder()
                .serviceUrl(org.vander.common.Config.URL)
                .build();

        producer = client.newProducer()
                .topic("my-topic")
                .enableBatching(false)
                .intercept(new TracingProducerInterceptor())
                .create();

        startProducer();

    }

    private static void startProducer() throws Exception {

        while (true) {
            System.out.println("Start produce");
            producer.newMessage()
                    .value("my-message-".getBytes())
                    .sequenceId(new Random().nextInt(100000))
                    .send();


            Thread.sleep(1000);
        }
    }
}
