package com.myspark.programs.spark_programs

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import com.databricks.spark.xml._

object XMLFileRead {
  
  def main(args: Array[String]) {
    
    val inputXMLFile = args(0)
    val hiveTableName = args(1)
  
    val spark =  SparkSession.builder()
                .appName("LoadXMlFile")
                .enableHiveSupport()
                .getOrCreate()
              
  val getXml = loadXML(spark,inputXMLFile)
  val writeToHive = getXml.write.insertInto(hiveTableName)
 }
  def loadXML(spark:SparkSession,inputPath:String):DataFrame = {
      val schemaXML = new StructType(Array(StructField("GEO_REGION_CD",CharType(2),true),
                                           StructField("STORE_NBR",IntegerType,true),
                                           StructField("BU_ID",IntegerType,true),
                                           StructField("STORE_NM",VarcharType(30),true),
                                           StructField("SUBDIV_NBR",CharType(2),true),
                                           StructField("SUBDIV_NM",VarcharType(30),true),
                                           StructField("REGION_NBR",IntegerType,true),
                                           StructField("REGION_NM",VarcharType(30),true),
                                           StructField("PHARM_REGION_MGR_USERID",VarcharType(8),true),
                                           StructField("MARKET_NBR",IntegerType,true),
                                           StructField("MARKET_NM",VarcharType(30),true),
                                           StructField("PHARM_MARKET_MGR_USERID",VarcharType(30),true),
                                           StructField("PHARM_ADDR",VarcharType(35),true),
                                           StructField("PHARM_CITY_NM",VarcharType(30),true),
                                           StructField("PHARM_ST_PROV_CD",CharType(2),true),
                                           StructField("POSTAL_CD",CharType(9),true),
                                           StructField("CNTRY_CD",CharType(2),true),
                                           StructField("NABP_LIC_NBR",IntegerType,true),
                                           StructField("NPI_LIC_NBR",IntegerType,true),
                                           StructField("SCHED_II_LIC_NBR",VarcharType(12),true),
                                           StructField("CNTRL_RECON_DT",DateType,true),
                                           StructField("PHARM_PH_NBR",VarcharType(18),true),
                                           StructField("DRV_THRU_IND",IntegerType,true),
                                           StructField("PHARM_TYPE_CD",CharType(2),true),
                                           StructField("SALES_VOL_CLASS_CD",CharType(1),true),
                                           StructField("OP_CMPNY_CD",CharType(8),true),
                                           StructField("SRC_RCV_TS",TimestampType,true),
                                           StructField("LOAD_TS",TimestampType,true),
                                           StructField("LOAD_USERID",VarcharType(20),true),
                                           StructField("UPD_TS",TimestampType,true),
                                           StructField("UPD_USERID",VarcharType(20),true)))
         
    val readXMl = spark.read.format("com.databricks.spark.xml")
                  .option("rowTag", "book")
                  .schema(schemaXML)
                  .load(inputPath)                   
        readXMl
  }
}