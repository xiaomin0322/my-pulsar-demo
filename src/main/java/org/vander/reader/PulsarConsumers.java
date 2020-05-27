package org.vander.reader;

import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Reader;
import org.apache.pulsar.client.impl.MessageIdImpl;
import org.apache.pulsar.client.util.MessageIdUtils;
import org.apache.pulsar.common.api.proto.PulsarMarkers.MessageIdData;

public class PulsarConsumers {

	private static PulsarClient client;

	public static void main(String[] args) throws Exception {
		client = PulsarClient.builder().serviceUrl(org.vander.common.Config.URL).build();

		
		
		//MessageId id = MessageIdUtils.getMessageId(21);
		//byte[] msgIdBytes = new String("10968:17:-1:0").getBytes();
		//MessageId id = MessageId.fromByteArray(msgIdBytes);
		
		 MessageId id = new MessageIdImpl(10968, 17, -1);
		 
		// MessageId id2 = MessageIdUtils.getMessageId(offset);
		

		Reader<byte[]> reader = client.newReader().topic("my-topic")
				 //.startMessageId(MessageId.earliest)
				.startMessageId(id)
				.create();

		while (true) {
			Message msg = reader.readNext();
			//10968:17:-1:0  ledgerID:EntresIndex:
			//10968:14:-1
			System.out.printf("Message received: %s %s", msg.getMessageId(),new String(msg.getData()) + "\r\n");
			// 处理消息
		}
	}

}
