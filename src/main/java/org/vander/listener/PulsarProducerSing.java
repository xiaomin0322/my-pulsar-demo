package org.vander.listener;

import java.util.UUID;

import org.apache.pulsar.client.api.MessageRoutingMode;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;

public class PulsarProducerSing {

    private static PulsarClient client;
    private static Producer<byte[]> producer;

    public static void main(String[] args) throws Exception {
        client = PulsarClient.builder()
                .serviceUrl(org.vander.common.Config.URL)
                .build();

        producer = client.newProducer()
        		//.messageRoutingMode(MessageRoutingMode.SinglePartition)
                .topic("public/dominos/coupon5")
               // .topic("my-topic")
                .create();

        startProducer3();

    }
    
    private static void startProducer3() throws Exception {
    	for(int i=0;i<20;i++) {
    		 producer.newMessage().key(UUID.randomUUID().toString()).value(("message-1-"+i+"\n").getBytes()).send();
    		 producer.newMessage().key(UUID.randomUUID().toString()).value(("message-2-"+i+"\n").getBytes()).send();
    	}
    }
    
    
    private static void startProduce2() throws Exception {
        while (true){
            producer.newMessage().orderingKey("key-1".getBytes()).value("message-1-1\n".getBytes()).send();
            producer.newMessage().key("key-1").value("message-1-2\n".getBytes()).send();
            producer.newMessage().key("key-1").value("message-1-3\n".getBytes()).send();
            producer.newMessage().key("key-2").value("message-2-1\n".getBytes()).send();
            producer.newMessage().key("key-2").value("message-2-2\n".getBytes()).send();
            producer.newMessage().key("key-2").value("message-2-3\n".getBytes()).send();
            producer.newMessage().key("key-3").value("message-3-1\n".getBytes()).send();
            producer.newMessage().key("key-3").value("message-3-2\n".getBytes()).send();
            producer.newMessage().key("key-4").value("message-4-1\n".getBytes()).send();
            producer.newMessage().key("key-4").value("message-4-2\n".getBytes()).send();

            Thread.sleep(1000);
        }
    }
        
        

    private static void startProducer() throws Exception {
       // while (true){
            producer.newMessage().key("key-1").value("message-1-1\n".getBytes()).send();
            producer.newMessage().key("key-1").value("message-1-2\n".getBytes()).send();
            producer.newMessage().key("key-1").value("message-1-3\n".getBytes()).send();
            producer.newMessage().key("key-2").value("message-2-1\n".getBytes()).send();
            producer.newMessage().key("key-2").value("message-2-2\n".getBytes()).send();
            producer.newMessage().key("key-2").value("message-2-3\n".getBytes()).send();
            producer.newMessage().key("key-3").value("message-3-1\n".getBytes()).send();
            producer.newMessage().key("key-3").value("message-3-2\n".getBytes()).send();
            producer.newMessage().key("key-4").value("message-4-1\n".getBytes()).send();
            producer.newMessage().key("key-4").value("message-4-2\n".getBytes()).send();

            Thread.sleep(1000);
       // }

    }
}
