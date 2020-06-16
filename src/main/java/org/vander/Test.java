package org.vander;
import java.util.HashMap;
import java.util.Map;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Map<Number,Number>  pidMap = new HashMap<>();
		Number key = 149395;
		pidMap.put(key, key);
		Long l= new Long(149395);
		
		Integer v = new Integer(149395);
		System.out.println(pidMap.get(v));
		
		System.out.println(l.equals(v));
		
		
		

	}

}
