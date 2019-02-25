package cn.lxj.spark.practice

import org.apache.spark.{SparkConf, SparkContext}

object ForeachDemo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("ForeachDemo").setMaster("local")
    val sc = new SparkContext(conf)
    //
    //

    sc.stop()
  }
}
