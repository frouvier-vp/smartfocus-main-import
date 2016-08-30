package com.fredrouvier.smartfocus

import org.apache.spark.{SparkContext, SparkConf}

trait Spark {
  /**
    * The Spark config
    */
  lazy val sparkConfig = new SparkConf()
    .setMaster("local[5]")
    .setAppName("smartfocus-main-import")

  /**
    * The Spark context
    */
  lazy val sparkContext = new SparkContext(sparkConfig)
}
