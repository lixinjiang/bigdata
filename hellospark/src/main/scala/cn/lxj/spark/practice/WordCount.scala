package cn.lxj.spark.practice

import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
  def main(args: Array[String]): Unit = {
    // 向spark集群的入口
    val conf = new SparkConf().setMaster("WC")
    val sc = new SparkContext(conf)
    sc.textFile(args(0)).flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _).sortBy(_._2, ascending = false).saveAsTextFile(args(1))
  }
}
