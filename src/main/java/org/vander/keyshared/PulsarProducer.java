package org.vander.keyshared;

import java.util.UUID;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;

public class PulsarProducer {

	private static PulsarClient client;
	private static Producer<String> producer;

	public static void main(String[] args) throws Exception {
		client = PulsarClient.builder().serviceUrl(org.vander.common.Config.URL).build();

		producer = client.newProducer(Schema.STRING)
				// .topic("public/dominos/coupon3")
				.topic("my-topic").create();

		startProducer();

		System.exit(1);

	}

	private static void startProducer3() throws Exception {
		for (int i = 0; i < 100; i++) {
			producer.newMessage().key(UUID.randomUUID().toString()).value(("message-1-" + i + "\n")).send();
		}
	}

	private static void startProduce2() throws Exception {
		while (true) {
			producer.newMessage().key("key-1").value("message-1-2\n").send();
			producer.newMessage().key("key-1").value("message-1-3\n").send();
			producer.newMessage().key("key-2").value("message-2-1\n").send();
			producer.newMessage().key("key-2").value("message-2-2\n").send();
			producer.newMessage().key("key-2").value("message-2-3\n").send();
			producer.newMessage().key("key-3").value("message-3-1\n").send();
			producer.newMessage().key("key-3").value("message-3-2\n").send();
			producer.newMessage().key("key-4").value("message-4-1\n").send();
			producer.newMessage().key("key-4").value("message-4-2\n").send();

			Thread.sleep(1000);
		}
	}

	private static void startProducer() throws Exception {
		// while (true){
		producer.newMessage().key("key-1").value("message-1-1\n").send();
		producer.newMessage().key("key-1").value("message-1-2\n").send();
		producer.newMessage().key("key-1").value("message-1-3\n").send();
		producer.newMessage().key("key-2").value("message-2-1\n").send();
		producer.newMessage().key("key-2").value("message-2-2\n").send();
		producer.newMessage().key("key-2").value("message-2-3\n").send();
		producer.newMessage().key("key-3").value("message-3-1\n").send();
		producer.newMessage().key("key-3").value("message-3-2\n").send();
		producer.newMessage().key("key-4").value("message-4-1\n").send();
		producer.newMessage().key("key-4").value("message-4-2\n").send();

		Thread.sleep(1000);
		// }

	}
}
