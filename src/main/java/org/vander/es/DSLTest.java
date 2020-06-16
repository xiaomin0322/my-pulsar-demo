package org.vander.es;

import org.nlpcn.es4sql.SearchDao;

public class DSLTest {
	
	public static void main(String[] args) throws Exception{
		SearchDao searchDao = new SearchDao(null);
		String dsl = searchDao.explain("SELECT * FROM bank WHERE q=query('880 Holmes Lane', 'standard', 1.0)").explain().explain();
		System.out.println(dsl);
	}

}
