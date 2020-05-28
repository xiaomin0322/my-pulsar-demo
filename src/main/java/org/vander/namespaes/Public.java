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





 * 
 * @author Zengmin.Zhang
 *
 */
public class Public {
	
	
	/**
	 * 
	 */

}
