package com.blackbeard.test;

public class SortUtils {

	  public static String printAges(int[] ages){
	         if(ages==null){
	           return "print error";
	         }
	         String result_ages ="";
	         for (int age : ages) {
	             result_ages+=age+",";
	         }
	         result_ages = result_ages.substring(0,result_ages.length()-1);
	         return result_ages;
	  }

	
	
}
