package org.vander.failover;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;

public class PulsarProducer {

	private static PulsarClient client;
	private static Producer<byte[]> producer;

	public static void main(String[] args) throws Exception {
		client = PulsarClient.builder().serviceUrl(org.vander.common.Config.URL).build();

		producer = client.newProducer().topic("public/dominos/coupon4").create();

		startProducer2();

	}
	
	private static void startProducer2() throws Exception {
		producer.newMessage().key("1").value("message-3\n".getBytes()).send();
	}

	private static void startProducer() throws Exception {
		producer.newMessage().value("message-1\n".getBytes()).send();
		producer.newMessage().value("message-2\n".getBytes()).send();
		producer.newMessage().value("message-3\n".getBytes()).send();
		producer.newMessage().value("message-4\n".getBytes()).send();
		producer.newMessage().value("message-5\n".getBytes()).send();
		producer.newMessage().value("message-6\n".getBytes()).send();
		producer.newMessage().value("message-7\n".getBytes()).send();
		producer.newMessage().value("message-8\n".getBytes()).send();
		producer.newMessage().value("message-9\n".getBytes()).send();
		producer.newMessage().value("message-10\n".getBytes()).send();
	}
}
