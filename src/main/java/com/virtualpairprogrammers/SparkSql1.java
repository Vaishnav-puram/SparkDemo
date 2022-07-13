package com.virtualpairprogrammers;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SparkSql1 {

	@SuppressWarnings("resource")
	public static void main(String[] args) 
	{
		System.setProperty("hadoop.home.dir", "c:/hadoop");
		Logger.getLogger("org.apache").setLevel(Level.WARN);
		
		SparkSession spark = SparkSession.builder().appName("testingSql").master("local[*]").getOrCreate();
		
		//read data
		Dataset<Row> dataset = spark.read().option("header", true).csv("src/main/resources/exams/students.csv");
		
		//temporary view for SQL
		dataset.createOrReplaceTempView("my_custom_table");
		Dataset<Row> result=spark.sql("select * from my_custom_table where subject='Math'");
		result.show();  
		Dataset<Row> result1=spark.sql("select score,grade from my_custom_table where subject='Math'");
		result1.show();  
		Dataset<Row> result2=spark.sql("select max(score) from my_custom_table where subject='Math'");
		result2.show();  
		Dataset<Row> result3=spark.sql("select distinct(year) from my_custom_table order by year");
		result3.show();  
		
		spark.stop();

	}

}
