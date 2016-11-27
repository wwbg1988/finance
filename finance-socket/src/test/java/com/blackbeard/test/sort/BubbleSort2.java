package com.blackbeard.test.sort;

import com.blackbeard.test.SortUtils;


public class BubbleSort2{

   public static void main(String[] args) {

     //冒泡排序
     int[] ages = new int[]{6,4,5,3,2,8,7,1};
     for (int i=0;i<ages.length ;i++ ) {
            //System.out.println("for1="+ages[i]);
        for (int j=i+1;j<ages.length ;j++ ) {
            //System.out.println("for2="+ages[j]);
            if (ages[i]>ages[j]) {
               int age = ages[i];
               ages[i] = ages[j];
               ages[j] = age;
            }
        }
     }
    System.out.println(SortUtils.printAges(ages));
   }




}
