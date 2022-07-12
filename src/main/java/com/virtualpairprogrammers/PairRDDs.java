package com.virtualpairprogrammers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.google.common.collect.Iterables;

import scala.Tuple2;

public class PairRDDs {

	public static void main(String[] args) {
		// TODO Auto-generated method stub'
		List<String> inputData=new ArrayList<>();
		
		inputData.add("WARN: Tuesday 05 0408");
		inputData.add("ERROR: Tuesday 05 0404");
		inputData.add("FATAL: Wednesday 06 1632");
		inputData.add("WARN: Friday 07 2333");
		inputData.add("WARN: Saturday 08 1222");
		Logger.getLogger("org.apache").setLevel(Level.WARN);
		
		SparkConf conf=new SparkConf().setAppName("starting spark").setMaster("local[*]");
		
		//connecting to spark cluster
		JavaSparkContext sc=new JavaSparkContext(conf);
		
		//to load java list into RDD we use parallelize
		
		//*********reduce by key***********
		sc.parallelize(inputData)
			.mapToPair(rawValue-> new Tuple2<>(rawValue.split(":")[0],1L))
			.reduceByKey((val1,val2)->val1+val2)
			.foreach(tuple->System.out.println(tuple._1+": has "+tuple._2+" instances"));
			
//		JavaPairRDD<String,Long> pairRdd= logrdd.mapToPair(rawValue->{
//			String column[]=rawValue.split(":");
//			String level=column[0];
//			//String date=column[1];
//			return new Tuple2<>(level,1L);
//		});
//		
		//JavaPairRDD<String,Long> pairRdd= logrdd.mapToPair(rawValue-> new Tuple2<>(rawValue.split(":")[0],1L));
		
		//to print no of levels
		//JavaPairRDD<String,Long> sumRdd=pairRdd.reduceByKey((val1,val2)->val1+val2);
		//sumRdd.foreach(tuple->System.out.println(tuple._1+": has "+tuple._2+" instances"));
		
		//********group by key**********
//		sc.parallelize(inputData)
//		.mapToPair(rawValue-> new Tuple2<>(rawValue.split(":")[0],1L))
//		.groupByKey()
//		.foreach(tuple->System.out.println(tuple._1+": has "+Iterables.size(tuple._2)+" instances"));
//		
		
		
		
		sc.close();
		
		
	}

}
