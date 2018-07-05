package com.myspark.programs.sparkprograms

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.types._
import com.databricks.spark.xml._

object XMLFileRead {
  def main(args: Array[String]) {
    val inputXMLFile = args(0)
    val hiveTableName = args(1)
    val conf        =   new SparkConf().setAppName("LoadXMLFIle").setMaster("local[*]")
    val sc          =   new SparkContext(conf)
    val hiveContext =   new HiveContext(sc)
    val schemaXML = new StructType(Array(StructField("GEO_REGION_CD",StringType,true),
                                           StructField("STORE_NBR",IntegerType,true),
                                           StructField("BU_ID",IntegerType,true),
                                           StructField("STORE_NM",StringType,true),
                                           StructField("SUBDIV_NBR",StringType,true),
                                           StructField("SUBDIV_NM",StringType,true),
                                           StructField("REGION_NBR",IntegerType,true),
                                           StructField("REGION_NM",StringType,true),
                                           StructField("PHARM_REGION_MGR_USERID",StringType,true),
                                           StructField("MARKET_NBR",IntegerType,true),
                                           StructField("MARKET_NM",StringType,true),
                                           StructField("PHARM_MARKET_MGR_USERID",StringType,true),
                                           StructField("PHARM_ADDR",StringType,true),
                                           StructField("PHARM_CITY_NM",StringType,true),
                                           StructField("PHARM_ST_PROV_CD",StringType,true),
                                           StructField("POSTAL_CD",StringType,true),
                                           StructField("CNTRY_CD",StringType,true),
                                           StructField("NABP_LIC_NBR",IntegerType,true),
                                           StructField("NPI_LIC_NBR",IntegerType,true),
                                           StructField("SCHED_II_LIC_NBR",StringType,true),
                                           StructField("CNTRL_RECON_DT",DateType,true),
                                           StructField("PHARM_PH_NBR",StringType,true),
                                           StructField("DRV_THRU_IND",IntegerType,true),
                                           StructField("PHARM_TYPE_CD",StringType,true),
                                           StructField("SALES_VOL_CLASS_CD",StringType,true),
                                           StructField("OP_CMPNY_CD",StringType,true),
                                           StructField("SRC_RCV_TS",TimestampType,true),
                                           StructField("LOAD_TS",TimestampType,true)))
  //                                         StructField("LOAD_USERID",StringType,true),
  //                                         StructField("UPD_TS",TimestampType,true),
  //                                        StructField("UPD_USERID",StringType,true)))
    val readXML =  hiveContext.read
                  .format("com.databricks.spark.xml")
                  .option("rowTag", "book")
                  .schema(schemaXML)
                  .load(inputXMLFile)   
    val requiredDf = readXML
                     .withColumn("LOAD_USERID", null)
                     .withColumn("UPD_TS", null)
                     .withColumn("UPD_USERID", null)
                  
   val writeToHive = requiredDf.write.mode("overwrite").format("orc").saveAsTable(hiveTableName)
  
 }
}