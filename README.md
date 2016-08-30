# Task main import - CSV Smartfocus

> LANGUAGE: Scala [http://www.scala-lang.org](http://www.scala-lang.org)  
> COMPILER: SBT [http://www.scala-sbt.org](http://www.scala-sbt.org)  
> LIBRARY:  Spark [http://spark.apache.org](http://spark.apache.org)

Use to import Smartfocus CSV files into HDFS with partitioning.

## Example

```bash
> sbt run \
-DinputPath="/tmp/voyageprive_2_m_campaign_historique_*.txt.histo" \
-DoutputPath="hdfs://hostname:8020/tmp/smartfocus/campaign" \
-Dbu=fr \
-DdateColumnIndex=7 \
-DdtColumnIndex=10
```
