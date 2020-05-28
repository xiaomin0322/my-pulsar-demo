package org.vander.namespaes;



/**
 * 其中 property 是租户名称，namespace 则是业务线名称，故一个租户可以有多个 namespace，租户可以针对 namespace 设置 ACL、
 * 调整副本数目、消息过期时间等参数，至于多租户的资源控制无非是借助配额、限流、流控等手段进行。
 * 租户实质上是一种资源隔离手段，把不同业务混部在一起，可以提高资源的利用率。
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


# 默认是10G
root@pulsar:/pulsar/bin# ./pulsar-admin namespaces get-backlog-quotas public/default
{ 
  "destination_storage" : {
    "limit" : 10737418240,
    "policy" : "producer_request_hold"
  }
}


http://pulsar.apache.org/docs/zh-CN/cookbooks-retention-expiry/#%E8%8E%B7%E5%8F%96-backlog-%E5%A4%A7%E5%B0%8F%E9%98%88%E5%80%BC%E5%92%8C-backlog-%E4%BF%9D%E7%95%99%E7%AD%96%E7%95%A5

策略	触发的操作
producer_request_hold	Broker 会持有生产者投递的消息，但并不会把投递的消息进行持久化存储
producer_exception	Broker 会与客户端断开连接并抛出异常
consumer_backlog_eviction	Broker 将开始丢弃backlog的消息






 * 
 * @author Zengmin.Zhang
 *
 */
public class Public {
	
	
	/**
	 * 
	 */

}
