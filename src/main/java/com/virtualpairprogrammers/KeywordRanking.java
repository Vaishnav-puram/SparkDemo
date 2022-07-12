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

public class KeywordRanking {

	public static void main(String[] args) {
		// TODO Auto-generated method stub'
		
		Logger.getLogger("org.apache").setLevel(Level.WARN);
		
		SparkConf conf=new SparkConf().setAppName("starting spark").setMaster("local[*]");
		
		//connecting to spark cluster
		JavaSparkContext sc=new JavaSparkContext(conf);
		
		JavaRDD<String> inputText=sc.textFile("src/main/resources/subtitles/input.txt");
		JavaRDD<String> wordsrdd=inputText.map(sentence->sentence.replaceAll("[^a-zA-Z\\s]", "").toLowerCase());
		JavaRDD<String> filteredData=wordsrdd.filter(words->words.trim().length()>0);
		JavaRDD<String> wordsonly=filteredData.flatMap(sentence->Arrays.asList(sentence.split(" ")).iterator());
		JavaRDD<String> blanksremoved=wordsonly.filter(words->words.trim().length()>0);
		JavaRDD<String> interestingWords=blanksremoved.filter(word->Util.isNotBoring(word));
		JavaPairRDD<String,Long> count=interestingWords.mapToPair(word->new Tuple2<String,Long>(word,1L));
		JavaPairRDD<String,Long> countadd=count.reduceByKey((val1,val2)->val1+val2);
		JavaPairRDD<Long,String> switched=countadd.mapToPair(tuple->new Tuple2<Long,String>(tuple._2,tuple._1));
		JavaPairRDD<Long,String> sorted=switched.sortByKey(false);	
			
		List<Tuple2<Long,String>> res=sorted.take(10);
		res.forEach(System.out::println);
		sc.close();
		
		
	}

}
