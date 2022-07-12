package com.virtualpairprogrammers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.google.common.collect.Iterables;

import scala.Tuple2;

public class FlatMap {

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
		
	
		JavaRDD<String> sentences=sc.parallelize(inputData);
		
		JavaRDD<String> words=sentences.flatMap(value->Arrays.asList(value.split(" ")).iterator());
		
		JavaRDD<String> filteredWords=words.filter(word->word.length()>4);
		words.collect().forEach(System.out::println);
		System.out.println();
		filteredWords.collect().forEach(System.out::println);
		
		sc.close();
		
		
	}

}
