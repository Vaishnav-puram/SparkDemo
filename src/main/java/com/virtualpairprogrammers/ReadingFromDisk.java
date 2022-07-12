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

public class ReadingFromDisk {

	public static void main(String[] args) {
		// TODO Auto-generated method stub'
		
		Logger.getLogger("org.apache").setLevel(Level.WARN);
		
		SparkConf conf=new SparkConf().setAppName("starting spark").setMaster("local[*]");
		
		//connecting to spark cluster
		JavaSparkContext sc=new JavaSparkContext(conf);
		
		JavaRDD<String> inputText=sc.textFile("src/main/resources/subtitles/input.txt");
		inputText.flatMap(value->Arrays.asList(value.split(" ")).iterator()).filter(value->value.length()>5).foreach(value->System.out.println(value));
		System.out.println();
		JavaRDD<String> inputText1=sc.textFile("src/main/resources/subtitles/boringwords.txt");
		inputText1.filter(value->value.contains("the")).foreach(value->System.out.println(value));
		
		sc.close();
		
		
	}

}
