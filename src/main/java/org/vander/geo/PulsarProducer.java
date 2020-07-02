package org.vander.geo;

import java.util.UUID;

import org.apache.pulsar.client.api.BatcherBuilder;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;


/**
 * 嗯嗯，这个的问题是将几条消息打包在一起发送了，而默认打包是根据大小和时间来定的
 * ，这样就会导致不同key的消息打包成一个command发过去。而消息的时候 broker 不会再去拆分开每一条消息，
 * 也是整个包发送给consumer。所以会出现都跑到一个 consumer 的情况。关掉batch 相当于不会把多条消息打包了，
 * keybased 是按照 消息的key 打包，一样的key打包在一起。所以这两个都可以
 * 
 * https://github.com/apache/pulsar/issues/7121
 * 
 * @author Zengmin.Zhang
 *
 */
public class PulsarProducer {

	private static PulsarClient client;
	private static Producer<String> producer;

	public static void main(String[] args) throws Exception {
		client = PulsarClient.builder().serviceUrl(org.vander.common.Config.URL).build();

		producer = client.newProducer(Schema.STRING)
				// .topic("public/dominos/coupon3")
				.enableBatching(false)
				//.batcherBuilder(BatcherBuilder.KEY_BASED)
				.topic("my-topic").create();

		startProducer3();

		System.exit(1);

	}

	private static void startProducer3() throws Exception {
		for (int i = 0; i < 10; i++) {
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
