package org.vander.namespaes;



/**
 * ���� property ���⻧���ƣ�namespace ����ҵ�������ƣ���һ���⻧�����ж�� namespace���⻧������� namespace ���� ACL��
 * ����������Ŀ����Ϣ����ʱ��Ȳ��������ڶ��⻧����Դ�����޷��ǽ��������������ص��ֶν��С�
 * �⻧ʵ������һ����Դ�����ֶΣ��Ѳ�ͬҵ��첿��һ�𣬿��������Դ�������ʡ�
 * 
 * http://pulsar.apache.org/docs/zh-CN/admin-api-namespaces/#%E8%AE%BE%E7%BD%AE%E7%A7%AF%E5%8E%8B%E9%85%8D%E9%A2%9D%E6%94%BF%E7%AD%96
 * 
 * 
 * $ pulsar-admin namespaces set-backlog-quota my-tenant/my-ns \
  --limit 2G \
  --policy producer_request_hold
  
 * 
 * long sizeLimit = 2147483648L;
BacklogQuota.RetentionPolicy policy = BacklogQuota.RetentionPolicy.producer_request_hold;
BacklogQuota quota = new BacklogQuota(sizeLimit, policy);
admin.namespaces().setBacklogQuota(namespace, quota);


# Ĭ����10G
root@pulsar:/pulsar/bin# ./pulsar-admin namespaces get-backlog-quotas public/default
{ 
  "destination_storage" : {
    "limit" : 10737418240,
    "policy" : "producer_request_hold"
  }
}





 * 
 * @author Zengmin.Zhang
 *
 */
public class Public {
	
	
	/**
	 * 
	 */

}
