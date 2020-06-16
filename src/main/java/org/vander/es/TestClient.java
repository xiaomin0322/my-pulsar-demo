package org.vander.es;

import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

public class TestClient {

	public static void main(String[] args) throws Exception {
		test4();
		test5();
	}
	
	public static void test4() throws Exception {
		RestClient restClient = RestClient.builder(new HttpHost("cpp.es.com", 9400, "http")).build();
		Request request = new Request("POST", "/_sql");
		request.setJsonEntity("{\"query\":\"SELECT main_id FROM allworks  WHERE MATCH(content,'老图')   limit 10 \"}");
		Response response = restClient.performRequest(request);
		String responseBody = EntityUtils.toString(response.getEntity());
		System.out.println(responseBody);
		restClient.close();
	}
	
	public static void test5() throws Exception {
		RestClient restClient = RestClient.builder(new HttpHost("cpp.es.com", 9400, "http")).build();
		Request request = new Request("POST", "/_sql/translate");
		request.setJsonEntity("{\"query\":\"SELECT main_id FROM allworks  WHERE MATCH(content,'老图')   limit 10 \"}");
		Response response = restClient.performRequest(request);
		String responseBody = EntityUtils.toString(response.getEntity());
		System.out.println(responseBody);
		restClient.close();
	}

	/**
	 *     代码中的sql检索
	 * @throws Exception
	 */
	public static void test() throws Exception {
		RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200, "http")).build();
		Request request = new Request("POST", "/_sql");
		request.setJsonEntity("{\"query\":\"SELECT * FROM library WHERE release_date < '2000-01-01'\"}");
		Response response = restClient.performRequest(request);
		String responseBody = EntityUtils.toString(response.getEntity());
		System.out.println(responseBody);
		restClient.close();
	}

	public static void test2() throws Exception {
		RestClient restClient = RestClient.builder(new HttpHost("cpp.es.com", 9400, "http")).build();
		Request request = new Request("POST", "/_sql");
		request.setJsonEntity("{\"query\":\"SELECT * FROM allworks WHERE QUERY('content:老图')   limit 10 \"}");
		Response response = restClient.performRequest(request);
		String responseBody = EntityUtils.toString(response.getEntity());
		System.out.println(responseBody);
		restClient.close();
	}
	
	public static void test3() throws Exception {
		RestClient restClient = RestClient.builder(new HttpHost("cpp.es.com", 9400, "http")).build();
		Request request = new Request("POST", "/_sql/translate");
		request.setJsonEntity("{\"query\":\"SELECT * FROM allworks WHERE QUERY('content:123')   limit 10 \"}");
		Response response = restClient.performRequest(request);
		String responseBody = EntityUtils.toString(response.getEntity());
		System.out.println(responseBody);
		restClient.close();
	}

	/**
	 * sql转换为dsl
	 * @throws Exception
	 */
	public static void testTranslate() throws Exception {
		RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200, "http")).build();
		Request request = new Request("POST", "/_sql/translate");
		request.setJsonEntity("{\"query\":\"SELECT * FROM library WHERE release_date < '2000-01-01'\"}");
		Response response = restClient.performRequest(request);
		String responseBody = EntityUtils.toString(response.getEntity());
		System.out.println(responseBody);
		restClient.close();

	}
}
