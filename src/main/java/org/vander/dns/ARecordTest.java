package org.vander.dns;

import org.xbill.DNS.ARecord;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

public class ARecordTest {

	public static void main(String[] args) {
		try {
			Record[] records = null;
			Lookup lookup = new Lookup("pulsar-sh.pythonic.in", Type.A);
			lookup.run();

			if (lookup.getResult() == Lookup.SUCCESSFUL) {
				records = lookup.getAnswers();
			} else {
				System.out.println("未查询到结果!");
				return;
			}
			/*for (int i = 0; i < records.length; i++) {
				MXRecord mx = (MXRecord) records[i];
				System.out.println("Host " + mx.getTarget() + " has preference " + mx.getPriority());
			}*/
			
			for (int i = 0; i < records.length; i++) {
				ARecord mx = (ARecord) records[i];
				System.out.println("Address " + mx.getAddress() + " has getName " + mx.getName() + " ttl "+mx.getTTL());
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}