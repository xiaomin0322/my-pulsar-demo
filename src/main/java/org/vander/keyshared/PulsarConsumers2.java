package org.vander.keyshared;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.SubscriptionType;

public class PulsarConsumers2 {

	private static PulsarClient client;
	private static Consumer<String> consumer;

	public static void main(String[] args) throws Exception {
		
		client = PulsarClient.builder()
                .serviceUrl(org.vander.common.Config.URL)
                .build();

		
		Consumer consumer1 = client.newConsumer().topic("my-topic").subscriptionName("my-subscription").consumerName("consumer1")
				.subscriptionType(SubscriptionType.Key_Shared).subscribe();

		Consumer consumer2 = client.newConsumer().topic("my-topic").subscriptionName("my-subscription").consumerName("consumer2")
				.subscriptionType(SubscriptionType.Key_Shared).subscribe();

		new Thread() {
			@Override
			public void run() {
				try {
					startConsumer(consumer1);
				} catch (PulsarClientException e) {
					e.printStackTrace();
				}
			}
		}.start();;
		

		new Thread() {
			@Override
			public void run() {
				try {
					startConsumer(consumer2);
				} catch (PulsarClientException e) {
					e.printStackTrace();
				}
			}
		}.start();;

	}

	private static void startConsumer(Consumer consumer1) throws PulsarClientException {

		while (true) {
			// Wait for a message
			Message<byte[]> msg = consumer1.receive();
			try {
				System.out.printf(consumer1.getConsumerName()+"  Message received: %s", new String(msg.getData()) + "\r\n");
				consumer1.acknowledge(msg);
			} catch (Exception e) {
				System.err.printf("Unable to consume message: %s", e.getMessage());
				consumer1.negativeAcknowledge(msg);
			}
		}
	}

	private static void startConsumer() throws PulsarClientException {

		while (true) {
			// Wait for a message
			Message<String> msg = consumer.receive();
			try {
				System.out.printf("Message received: %s", new String(msg.getData()) + "\r\n");
				consumer.acknowledge(msg);
			} catch (Exception e) {
				System.err.printf("Unable to consume message: %s", e.getMessage());
				consumer.negativeAcknowledge(msg);
			}
		}
	}
}
