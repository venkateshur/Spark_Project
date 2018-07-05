package com.myspark.programs.spark_programs

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import com.databricks.spark.xml._

object XmlReadSample {
  
  def main(args: Array[String]) {
    
    val inputXMLFile = args(0)
    val hiveTableName = args(1)
  
    val spark =  SparkSession.builder()
                .master("local")
                .appName("LoadSampleXMlFile")
                .enableHiveSupport()
                .getOrCreate()
              
  val getXml = loadXML(spark,inputXMLFile)
  val getXml_show = getXml.show(50,false)
  println("getXml_show:" + getXml.printSchema())
  val writeToHive = getXml.write.mode("overwrite").format("orc")saveAsTable(hiveTableName)
 }
  def loadXML(spark:SparkSession,inputPath:String):DataFrame = {
      val customSchema = StructType(Array(StructField("_id", StringType, nullable = true),
                                          StructField("author", StringType, nullable = true),
                                          StructField("description", StringType, nullable = true),
                                          StructField("genre", StringType ,nullable = true),
                                          StructField("price", DoubleType, nullable = true),
                                          StructField("publish_date", StringType, nullable = true),
                                          StructField("title", StringType, nullable = true)))

    val readXMl = spark.read.format("com.databricks.spark.xml")
                  .option("rowTag", "book")
                  .schema(customSchema)
                  .load(inputPath)                   
        readXMl
  }
}