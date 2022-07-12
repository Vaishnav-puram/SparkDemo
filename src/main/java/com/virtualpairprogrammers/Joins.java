package com.virtualpairprogrammers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.Optional;


import scala.Tuple2;

public class Joins {

	public static void main(String[] args) {
		// TODO Auto-generated method stub'
		List<Tuple2<Integer,Integer>> inputData1=new ArrayList<>();
		
		inputData1.add(new  Tuple2<>(4,18));
		inputData1.add(new  Tuple2<>(6,4));
		inputData1.add(new  Tuple2<>(10,9));
		inputData1.add(new  Tuple2<>(5,18));
	
		
		List<Tuple2<Integer,String>> inputData2=new ArrayList<>();
		
		inputData2.add(new  Tuple2<>(1,"JAVA"));
		inputData2.add(new  Tuple2<>(2,"C"));
		inputData2.add(new  Tuple2<>(3,"C++"));
		inputData2.add(new  Tuple2<>(4,"HTML"));
		inputData2.add(new  Tuple2<>(5,"CSS"));
		inputData2.add(new  Tuple2<>(6,"Javascript"));
		
		
		Logger.getLogger("org.apache").setLevel(Level.WARN);
		
		SparkConf conf=new SparkConf().setAppName("starting spark").setMaster("local[*]");
		
		//connecting to spark cluster
		JavaSparkContext sc=new JavaSparkContext(conf);
		
		//to load java list into RDD we use parallelize
		
		JavaPairRDD<Integer,Integer>one=sc.parallelizePairs(inputData1);
		JavaPairRDD<Integer,String>two=sc.parallelizePairs(inputData2);
		
		//*****INNER JOIN******
		System.out.println("*****INNER JOIN******");
		JavaPairRDD<Integer,Tuple2<Integer,String>>res=one.join(two);
		res.foreach(val->System.out.println(val));
		System.out.println("*****LEFT OUTER JOIN******");
		//*****LEFT OUTER JOIN*******
		JavaPairRDD<Integer, Tuple2<Integer, Optional<String>>>res1=one.leftOuterJoin(two);
		res1.foreach(val->System.out.println(val));
		res1.foreach(val->System.out.println(val._2._2.orElse("blank").toUpperCase()));
		System.out.println("*****RIGHT OUTER JOIN******");
		//*****RIGHT OUTER JOIN******
		JavaPairRDD<Integer, Tuple2<Optional<Integer>, String>>res2=one.rightOuterJoin(two);
		res2.foreach(val->System.out.println(val));
		System.out.println("*****FULL OUTER JOIN******");
		//*******FULL OUTER JOIN********
		JavaPairRDD<Integer, Tuple2<Optional<Integer>, Optional<String>>>res3=one.fullOuterJoin(two);
		res3.foreach(val->System.out.println(val));
	
	
		
		sc.close();
		
		
	}

}
