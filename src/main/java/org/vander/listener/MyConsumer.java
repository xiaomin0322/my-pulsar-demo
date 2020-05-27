package org.vander.listener;

import java.util.concurrent.TimeUnit;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.ConsumerBuilder;
import org.apache.pulsar.client.api.MessageListener;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.SubscriptionType;
/**
 * Ŀǰ������
 * @author Zengmin.Zhang
 *
 */
public class MyConsumer {
	private static final int NUM_CONSUMERS = 20;

	public static void main(String[] args) throws PulsarClientException {
		String topic = "my-topic";
		//topic = "public/dominos/coupon5";
		String subscriptionName = "my-subscription2";
		System.err.println(topic + " " + subscriptionName);

		PulsarClient client = PulsarClient.builder().serviceUrl(org.vander.common.Config.URL)
				//Ϊ��Ӧ����topic���ѵ� ,����topic��ʵ�ϣ�����һ���߳������� ��Զ�topic�����Ҫʵ�ֵ�topic���߳����ѣ��Լ�ʵ���̳߳� 
				.listenerThreads(NUM_CONSUMERS).build();

		MessageListener<byte[]> listener = (consumer, msg) -> {
			synchronizedPrint(consumer.getConsumerName() + " " + msg.getKey() + " " + new String(msg.getValue()));
			consumer.acknowledgeAsync(msg);
		};

		ConsumerBuilder<byte[]> builder = client.newConsumer().topic(topic).subscriptionName(subscriptionName)
				.subscriptionType(SubscriptionType.Shared).messageListener(listener)
				.receiverQueueSize(10)
				.negativeAckRedeliveryDelay(1, TimeUnit.SECONDS);
		

		Consumer<byte[]> consumer =  builder.subscribe();
		
		/*for (int i = 0; i < NUM_CONSUMERS; ++i) {
			builder.subscribe();
		}*/

		/*while (true) {
			try {
				Thread.sleep(1);
			} catch (Exception e) {
				Thread.interrupted();
				e.printStackTrace();
			}
		}*/
	}

	private static /*synchronized*/ void synchronizedPrint(String str) {
		System.out.println(str);
	}
}