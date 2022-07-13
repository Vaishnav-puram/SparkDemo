package com.virtualpairprogrammers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

/**
 * This class is used in the chapter late in the course where we analyse viewing figures.
 * You can ignore until then.
 */
public class ViewingFigures 
{
	@SuppressWarnings("resource")
	public static void main(String[] args)
	{
		System.setProperty("hadoop.home.dir", "c:/hadoop");
		Logger.getLogger("org.apache").setLevel(Level.WARN);

		SparkConf conf = new SparkConf().setAppName("startingSpark").setMaster("local[*]");
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		// Use true to use hardcoded data identical to that in the PDF guide.
		boolean testMode = true;
		
		JavaPairRDD<Integer, Integer> viewData = setUpViewDataRdd(sc, testMode);
		JavaPairRDD<Integer, Integer> chapterData = setUpChapterDataRdd(sc, testMode);
		JavaPairRDD<Integer, String> titlesData = setUpTitlesDataRdd(sc, testMode);

		// TODO - over to you!
		JavaPairRDD<Integer,Integer> chapterCountRDD=chapterData.mapToPair(val->new Tuple2<Integer,Integer>(val._2,1))
									.reduceByKey((val1,val2)->val1+val2);
		chapterCountRDD.foreach(val->System.out.println(val));
		
		//Step1 -- remove duplicates from viewData
		viewData=viewData.distinct();
		viewData.foreach(val->System.out.println(val));
		System.out.println();
		
		//Step2 -- get courseid into viewdata
		viewData=viewData.mapToPair(tuple->new Tuple2<Integer,Integer>(tuple._2,tuple._1));
		//join viewdata with chapterdata
		JavaPairRDD<Integer,Tuple2<Integer,Integer>> joinedRDD=viewData.join(chapterData);
		joinedRDD.foreach(val->System.out.println(val));
		System.out.println();
		
		//Step3 -- lose the chapterid
		JavaPairRDD<Tuple2<Integer,Integer>,Integer> removedChapterId=joinedRDD.mapToPair(val->new Tuple2<Tuple2<Integer,Integer>,Integer>(val._2,1));
		removedChapterId.foreach(val->System.out.println(val));
		System.out.println();
		
		//Step4 -- count views for users/course
		JavaPairRDD<Tuple2<Integer,Integer>,Integer> countViews=removedChapterId.reduceByKey((val1,val2)->val1+val2);
		countViews.foreach(val->System.out.println(val));
		System.out.println();
		
		//Step5 -- drop the userId
		JavaPairRDD<Integer,Integer> removeUserId=countViews.mapToPair(val->new Tuple2<Integer,Integer>(val._1._2,val._2));
		removeUserId.foreach(val->System.out.println(val));
		System.out.println();
		
		//Step6 -- how many chapters -- joining removeUserId and viewData
		JavaPairRDD<Integer,Tuple2<Integer,Integer>> chaptersView=removeUserId.join(chapterCountRDD);
		chaptersView.foreach(val->System.out.println(val));
		System.out.println();
		
		//Step7 -- convert into percentages
		JavaPairRDD<Integer,Double> percentage=chaptersView.mapValues(values->(double)values._1/values._2);
		percentage.foreach(val->System.out.println(val));
		System.out.println();
		
		//Step8 --provide scores
		JavaPairRDD<Integer,Long> scores=percentage.mapValues(val->{
			if(val>0.9) return 10L;
			if(val>0.5) return 4L;
			if(val>0.25) return 2l;
			return 0L;
			
		});
		scores.foreach(val->System.out.println(val));
		System.out.println();
		
		//Step9 -- adding up total scores
		JavaPairRDD<Integer,Long> sumScores= scores.reduceByKey((val1,val2)->val1+val2);
		sumScores.foreach(val->System.out.println(val));
		System.out.println();
		
		//Step10 -- adding title to sumScores
		JavaPairRDD<Integer,Tuple2<Long,String>> titleData =sumScores.join(titlesData);
		titleData.foreach(val->System.out.println(val));
		System.out.println();
		//removing chapterId
		JavaPairRDD<Long,String> finalData=titleData.mapToPair(val->new Tuple2<Long,String>(val._2._1,val._2._2));
		finalData.sortByKey(false).collect().forEach(System.out::println);
//		Scanner scanner=new Scanner(System.in);
//		scanner.nextLine();
		sc.close();
	}

	private static JavaPairRDD<Integer, String> setUpTitlesDataRdd(JavaSparkContext sc, boolean testMode) {
		
		if (testMode)
		{
			// (chapterId, title)
			List<Tuple2<Integer, String>> rawTitles = new ArrayList<>();
			rawTitles.add(new Tuple2<>(1, "How to find a better job"));
			rawTitles.add(new Tuple2<>(2, "Work faster harder smarter until you drop"));
			rawTitles.add(new Tuple2<>(3, "Content Creation is a Mug's Game"));
			return sc.parallelizePairs(rawTitles);
		}
		return sc.textFile("src/main/resources/viewing figures/titles.csv")
				                                    .mapToPair(commaSeparatedLine -> {
														String[] cols = commaSeparatedLine.split(",");
														return new Tuple2<Integer, String>(new Integer(cols[0]),cols[1]);
				                                    });
	}

	private static JavaPairRDD<Integer, Integer> setUpChapterDataRdd(JavaSparkContext sc, boolean testMode) {
		
		if (testMode)
		{
			// (chapterId, (courseId, courseTitle))
			List<Tuple2<Integer, Integer>> rawChapterData = new ArrayList<>();
			rawChapterData.add(new Tuple2<>(96,  1));
			rawChapterData.add(new Tuple2<>(97,  1));
			rawChapterData.add(new Tuple2<>(98,  1));
			rawChapterData.add(new Tuple2<>(99,  2));
			rawChapterData.add(new Tuple2<>(100, 3));
			rawChapterData.add(new Tuple2<>(101, 3));
			rawChapterData.add(new Tuple2<>(102, 3));
			rawChapterData.add(new Tuple2<>(103, 3));
			rawChapterData.add(new Tuple2<>(104, 3));
			rawChapterData.add(new Tuple2<>(105, 3));
			rawChapterData.add(new Tuple2<>(106, 3));
			rawChapterData.add(new Tuple2<>(107, 3));
			rawChapterData.add(new Tuple2<>(108, 3));
			rawChapterData.add(new Tuple2<>(109, 3));
			return sc.parallelizePairs(rawChapterData);
		}

		return sc.textFile("src/main/resources/viewing figures/chapters.csv")
													  .mapToPair(commaSeparatedLine -> {
															String[] cols = commaSeparatedLine.split(",");
															return new Tuple2<Integer, Integer>(new Integer(cols[0]), new Integer(cols[1]));
													  	}).cache();
	}

	private static JavaPairRDD<Integer, Integer> setUpViewDataRdd(JavaSparkContext sc, boolean testMode) {
		
		if (testMode)
		{
			// Chapter views - (userId, chapterId)
			List<Tuple2<Integer, Integer>> rawViewData = new ArrayList<>();
			rawViewData.add(new Tuple2<>(14, 96));
			rawViewData.add(new Tuple2<>(14, 97));
			rawViewData.add(new Tuple2<>(13, 96));
			rawViewData.add(new Tuple2<>(13, 96));
			rawViewData.add(new Tuple2<>(13, 96));
			rawViewData.add(new Tuple2<>(14, 99));
			rawViewData.add(new Tuple2<>(13, 100));
			return  sc.parallelizePairs(rawViewData);
		}
		
		return sc.textFile("src/main/resources/viewing figures/views-*.csv")
				     .mapToPair(commaSeparatedLine -> {
				    	 String[] columns = commaSeparatedLine.split(",");
				    	 return new Tuple2<Integer, Integer>(new Integer(columns[0]), new Integer(columns[1]));
				     });
	}
}
