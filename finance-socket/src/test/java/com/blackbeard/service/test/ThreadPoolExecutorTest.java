package com.blackbeard.service.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolExecutorTest {
//测试线程池，创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
	
	 public static void main(String[] args) {  
		  ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);  
		  for (int i = 0; i < 20; i++) {  
		   final int index = i;  
		   fixedThreadPool.execute(new Runnable() {  
		    public void run() {  
		     try {  
		      System.out.println(index+",thread="+Thread.currentThread().getName());  
		      Thread.sleep(2000);  
		     } catch (Exception e) {  
		      e.printStackTrace();  
		     }  
		    }  
		   });  
		  }  
		 } 
	
}
