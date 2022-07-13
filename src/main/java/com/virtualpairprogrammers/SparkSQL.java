package com.virtualpairprogrammers;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import static org.apache.spark.sql.functions.*;

import scala.Tuple2;

public class SparkSQL {

	@SuppressWarnings("resource")
	public static void main(String[] args) 
	{
		System.setProperty("hadoop.home.dir", "c:/hadoop");
		Logger.getLogger("org.apache").setLevel(Level.WARN);
		
		SparkSession spark = SparkSession.builder().appName("testingSql").master("local[*]").getOrCreate();
		
		//read data
		Dataset<Row> dataset = spark.read().option("header", true).csv("src/main/resources/exams/students.csv");
//		dataset.show();
//		long total=dataset.count();
//		System.out.println("There are "+total+" records");
		
		//filters using regular expressions
//		Row firstRow=dataset.first();
//		System.out.println(firstRow.get(2));
//		int year=Integer.parseInt(firstRow.getAs("year")); //everything in csv is a string so we parse it to int
//		System.out.println(year);
		
		//filters using lambda
		Dataset<Row> subject=dataset.filter("subject='Math'AND year>2007");
		subject.show();
		Dataset<Row> mathresult=dataset.filter(row->row.getAs("subject").equals("Math")&&Integer.parseInt(row.getAs("year"))>2007);
		mathresult.show();
		
		//filters using colums
		Column subjectName=col("subject");
		Column year=col("year");
		Dataset<Row> result=dataset.filter(subjectName.equalTo("Math").and(year.geq(2007)));
		result.show();
		spark.stop();

	}

}
