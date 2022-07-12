package com.virtualpairprogrammers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2; 
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub'
		List<Integer> inputData=new ArrayList<>();
		
		inputData.add(12);
		inputData.add(235);
		inputData.add(23);
		inputData.add(45);
		
		Logger.getLogger("org.apache").setLevel(Level.WARN);
		
		SparkConf conf=new SparkConf().setAppName("starting spark").setMaster("local[*]");
		
		//connecting to spark cluster
		JavaSparkContext sc=new JavaSparkContext(conf);
		
		//to load java list into RDD we use parallelize
		JavaRDD<Integer> myrdd =sc.parallelize(inputData);
		
		Integer res=myrdd.reduce((value1,value2)->value1+value2);
		System.out.println(res);
		
		JavaRDD<Double> sqrtRdd =myrdd.map(value->Math.sqrt(value));
		sqrtRdd.foreach(value->System.out.println(value));
		//or
		//sqrtRdd.collect().forEach(System.out::println);
		
		//to merge the myrdd and sqrtrdd
		//JavaRDD<IntegerWithSquareRoot> sqrtRdd=myrdd.map(value-> new IntegerWithSquareRoot(value));
		//sqrtRdd.foreach(value->System.out.println(value));
		
		//using tuples we can avoid creating a class like above
		//syntax---Tuple2<Integer,Double> mytuple=new Tuple2<>(9,3.0);
		//JavaRDD<Tuple2<Integer,Double>> sqrtRdd =myrdd.map(value->new Tuple2<>(value,Math.sqrt(value)));
		//sqrtRdd.foreach(value->System.out.println(value));

		
		
		
		
		
		//using map and reduce
		JavaRDD<Long> countRdd=sqrtRdd.map(value->1L);
		Long ans=countRdd.reduce((value1,value2)->value1+value2);
		System.out.println(ans);
		
		
		
		sc.close();
		
		
	}

}
