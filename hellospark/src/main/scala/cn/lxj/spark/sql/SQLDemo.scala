package cn.lxj.spark.sql

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

object SQLDemo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("sqldemo").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    System.setProperty("user.name", "bigdata")

    val personRdd = sc.textFile("hdfs://hadoop1:9000/person.txt").map(line => {
      val fields = line.split(",")
      Person(fields(0).toLong, fields(1), fields(2).toInt)
    })

    import sqlContext.implicits._
    val personDf = personRdd.toDF
    personDf.registerTempTable("person")
    sqlContext.sql("select * from person where age >= 20 order by age desc limit 2").show()
    sc.stop()
  }
}

case class Person(id: Long, name: String, age: Int)