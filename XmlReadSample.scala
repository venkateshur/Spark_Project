package com.myspark.programs.sparkprograms

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types._
import com.databricks.spark.xml._

object XmlReadSample {
  
  def main(args: Array[String]) {
    
    val inputXMLFile = args(0)
    val hiveTableName = args(1)
  
    val conf        =  new SparkConf().setAppName("LoadXMLFIle").setMaster("local[*]")
    val sc          =  new SparkContext(conf)
    val hiveContext = new HiveContext(sc)
    
               
  val getXml = loadXML(hiveContext,inputXMLFile)
  val getXml_show = getXml.show(50,false)
  println("getXml_show:" + getXml.printSchema())
  val writeToHive = getXml.write.mode("overwrite").format("orc").saveAsTable(hiveTableName)
 }
  def loadXML(hiveContext:HiveContext,inputPath:String):DataFrame = {
      val customSchema = StructType(Array(StructField("_id", StringType, nullable = true),
                                          StructField("author", StringType, nullable = true),
                                          StructField("description", StringType, nullable = true),
                                          StructField("genre", StringType ,nullable = true),
                                          StructField("price", DoubleType, nullable = true),
                                          StructField("publish_date", StringType, nullable = true),
                                          StructField("title", StringType, nullable = true)))

    val readXML =  hiveContext.read
                  .format("com.databricks.spark.xml")
                  .option("rowTag", "book")
                  .schema(customSchema)
                  .load(inputPath)            
        readXML
  }
}