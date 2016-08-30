package com.fredrouvier.smartfocus

import org.apache.spark.rdd.RDD

object Main extends App with Spark {

  val inputPath: String = sys.props.getOrElse(
    "inputPath",
    throw new RuntimeException("`inputPath` arg is required")
  )
  val outputPath: String = sys.props.getOrElse(
    "outputPath",
    throw new RuntimeException("`outputPath` arg is required")
  )
  val bu: String = sys.props.getOrElse(
    "bu",
    throw new RuntimeException("`bu` arg is required")
  )
  val dateColumnIndex: Int = sys.props.getOrElse(
    "dateColumnIndex",
    throw new RuntimeException("`dateColumnIndex` arg is required")
  ).toString.toInt
  val dtColumnIndex: Int = sys.props.getOrElse(
    "dtColumnIndex",
    throw new RuntimeException("`dtColumnIndex` arg is required")
  ).toString.toInt


  // load FS files
  val fsFiles = sparkContext.textFile(inputPath)
    .mapPartitionsWithIndex { (idx, iter) => if (idx == 0) iter.drop(1) else iter } // skip CSV header line
    .map(line => line.split("\\|")) // split on delimiter
    .filter(row => row.length == dtColumnIndex) // exclude bad records
    .map(row => row :+ row(dateColumnIndex).substring(0, 7)) // add the column "dt" (use for partitioning)

  // performance check
  val dtDistinctValues = fsFiles.map(row => row(dtColumnIndex)).distinct()
  dtDistinctValues.cache()
  if(dtDistinctValues.count() > 100) {
    throw new RuntimeException("Number of distinct of values to split is greater than the limit `100`")
  }

  // prepare a rdd for each "dt" value
  val rddByDt:Seq[(String, RDD[Array[String]])] = dtDistinctValues
    .collect()
    .map(v => (v, fsFiles.filter(row => row(dtColumnIndex) == v)))
    .toSeq

  // save in HDFS each rdd
  rddByDt.foreach(part => {
    val path = s"$outputPath/dt=${part._1}/bu=$bu/"
    part._2.map(r => {
      r.take(dtColumnIndex).mkString("|")
    }).repartition(1).saveAsTextFile(path, classOf[org.apache.hadoop.io.compress.GzipCodec])
  })
}
