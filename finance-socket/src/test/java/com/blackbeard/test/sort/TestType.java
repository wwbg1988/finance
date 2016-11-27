package com.blackbeard.test.sort;

import java.util.Arrays;

public class TestType {

	public static void main(String[] args) {
	   String password = "123";
	   int h1 = password.hashCode();
	   System.out.println("h1="+h1);
	   int h2 = (password+"4").hashCode();
	   System.out.println("h2="+h2);
	}


	
	
	
}
