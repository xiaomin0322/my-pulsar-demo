package org.vander.security;

import org.apache.pulsar.client.api.AuthenticationFactory;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;


/**
 * http://pulsar.apache.org/docs/zh-CN/security-overview/
 * 	http://pulsar.apache.org/docs/zh-CN/security-jwt/
 * @author Zengmin.Zhang
 *
 */
public class PulsarProducer {

    private static PulsarClient client;
    private static Producer<byte[]> producer;

    public static void main(String[] args) throws Exception {
    	 client = PulsarClient.builder()
    		    .serviceUrl("pulsar://broker.example.com:6650/")
    		    .authentication(
    		        AuthenticationFactory.token(() -> {
    		            // Read token from custom source
							return "123123";
    		        }))
    		    .build();

        producer = client.newProducer()
                .topic("my-topic")
                .create();

        startProducer();

    }

    private static void startProducer() throws Exception {

        while (true) {
            System.out.println("Start produce");
            producer.newMessage()
                    .value("my-message-".getBytes())
                    .send();


            Thread.sleep(1000);
        }
    }
}
