package org.vander.security;

import org.apache.pulsar.client.api.AuthenticationFactory;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;


/**
 * 
 * pulsar security 

支持这样得功能吗，比如说 topic-A  生产者.得密码只能topic-A生产者使用


比如说 topic-A  消费者.得密码只能topic-A消费者使用
??
 * 
 * 
 * http://pulsar.apache.org/docs/zh-CN/security-overview/
 * 	http://pulsar.apache.org/docs/zh-CN/security-jwt/
 *   目前支持Nmaespace级别权限控制
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
